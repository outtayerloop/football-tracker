package fr.android.foottracker.controller.gameform;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import fr.android.foottracker.R;
import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.app.FootTrackerApplication;
import fr.android.foottracker.controller.DatePickerFragment;
import fr.android.foottracker.controller.TimePickerFragment;
import fr.android.foottracker.controller.gameupdate.GameUpdateActivity;
import fr.android.foottracker.controller.gameform.fragment.GameFormCollectionFragment;
import fr.android.foottracker.controller.gameform.fragment.GameFormFragment;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.repositories.gameform.GameFormLocalDataSource;
import fr.android.foottracker.model.repositories.gameform.GameFormRemoteDataSource;
import fr.android.foottracker.model.view.GameFormViewModel;
import fr.android.foottracker.utils.DataSourceUtil;
import fr.android.foottracker.utils.DateTimeUtil;
import fr.android.foottracker.utils.DeviceKeyboardUtil;
import fr.android.foottracker.utils.JsonUtil;
import fr.android.foottracker.utils.LocaleUtil;

public class GameFormActivity extends AppCompatActivity
                              implements DatePickerFragment.OnDateSetListener,
                                         TimePickerFragment.OnTimeSetListener {

    public static final String EXTRA_MESSAGE = "fr.android.foottracker.MESSAGE";

    private FootTrackerAppContainer appContainer;
    private GameFormViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContainer = ((FootTrackerApplication) getApplication()).getAppContainer();
        viewModel = new ViewModelProvider(this).get(GameFormViewModel.class);
        LocaleUtil.initLocale(this);
        setToolBar();
    }

    /**
     * Declenchée lorsque l'utilisateur choisit une date sur le picker.
     * @param year Annee choisie
     * @param month Mois choisi
     * @param dayOfMonth jour choisi
     */
    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        final EditText dateEditText = findViewById(R.id.date_picker);
        final String formattedDayOfMonth = DateTimeUtil.getFormattedDateTimeComponent(dayOfMonth);
        final String formattedMonth = DateTimeUtil.getFormattedDateTimeComponent(month);
        final String chosenDate = formattedDayOfMonth + "/" + formattedMonth + "/" + year;
        dateEditText.setText(chosenDate);
        viewModel.setDate(DateTimeUtil.toDate(chosenDate));
    }

    /***
     * Declenchée lorsque l'utilisateur choisit une heure et une minute sur le picker.
     * @param hourOfDay Heure choisie
     * @param minute Minute choisie
     */
    @Override
    public void onTimeSet(int hourOfDay, int minute) {
        final EditText timeEditText = findViewById(R.id.time_picker);
        final String formattedHourOfDay = DateTimeUtil.getFormattedDateTimeComponent(hourOfDay);
        final String formattedMinute = DateTimeUtil.getFormattedDateTimeComponent(minute);
        final String chosenTime = formattedHourOfDay + ":" + formattedMinute;
        timeEditText.setText(chosenTime);
        viewModel.setTime(DateTimeUtil.toTime(chosenTime));
    }

    /**
     * Déclenché lorsque le menu est créé. On remplace celui par défaut par le nôtre (R.menu.menu_game_form).
     * @param menu Menu qu'on veut modifier.
     * @return true pour terminer la modification.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_form, menu); // Remplace le menu actuel par celui indiqué dans l'inflate.
        menu.getItem(3).setOnMenuItemClickListener(this::onGameCreationItemClick);
        LocaleUtil.setLocaleSwitch(menu, this);
        setContentView(R.layout.activity_game_form);
        DeviceKeyboardUtil.setOnTouchListener(findViewById(R.id.activity_game_form), this);
        return true;
    }

    /**
     * Handler du click du bouton de création de match dans le manu en haut :
     * vérifie que tous les champs du view model ont bien été remplis avant de lancer la
     * creation du match.
     * @param item Item du menu sur lequel on a cliqué.
     */
    private boolean onGameCreationItemClick(@NonNull MenuItem item) {
        final ViewPager2 viewPager = findViewById(R.id.game_form_pager);
        if(viewModel.hasMissingGameFormFields())
            handleMissingGameFormFields(viewPager); // champs manquant dans GameFormFragment, on redirige là.
        else if(viewModel.hasMissingTeamFormFields())
            handleMissingTeamFormFields(viewPager); // champs manquant dans TeamFormFragment, on redirige là.
        else if(viewModel.hasMissingMapFormFields())
            handleMissingMapFormFields(viewPager); // champs manquant dans MapFormFragment, on redirige là.
        else
            createGame(); // Tous les champs du view model sont ok, on lance la création du match.
        return true;
    }

    private void setToolBar() {
        final ActionBar actionBar = getSupportActionBar(); // Recupere la barre d'action violette.
        if (actionBar != null)
            actionBar.setElevation(0f); // On enleve "l'effet 3D" de la barre violette par dessus les tabs.
    }

    private void handleMissingGameFormFields(@NonNull ViewPager2 viewPager){
        final int GAME_FORM_FRAGMENT_POSITION = 0; // Position de GameFormFragment dans la collection de fragments.
        viewPager.setCurrentItem(GAME_FORM_FRAGMENT_POSITION);
        Toast.makeText(this, getResources().getString(R.string.missing_game_form_fields_message), Toast.LENGTH_SHORT).show();
    }

    private void handleMissingTeamFormFields(@NonNull ViewPager2 viewPager){
        final int TEAM_FORM_FRAGMENT_POSITION = 1; // Position de TeamFormFragment dans la collection de fragments.
        viewPager.setCurrentItem(TEAM_FORM_FRAGMENT_POSITION);
        Toast.makeText(this, getResources().getString(R.string.missing_team_form_fields_message), Toast.LENGTH_SHORT).show();
    }

    private void handleMissingMapFormFields(@NonNull ViewPager2 viewPager){
        final int MAP_FORM_FRAGMENT_POSITION = 2; // Position de MapFormFragment dans la collection de fragments.
        viewPager.setCurrentItem(MAP_FORM_FRAGMENT_POSITION);
        Toast.makeText(this, getResources().getString(R.string.missing_map_form_fields_message), Toast.LENGTH_SHORT).show();
    }

    private void createGame(){
        final GameData newGame = viewModel.getGame();
        if(newGame != null){
            appContainer.getExecutorService().execute(() -> {
                final String createdGameJson = getCreatedGameJson(newGame);
                appContainer.getHandler().post(() -> startUpdatingGame(createdGameJson));
            });
        }
        else
            throw new IllegalArgumentException("Can not create null game.");
    }

    private void startUpdatingGame(@NonNull String createdGameJson){
        final Intent intent = new Intent(this, GameUpdateActivity.class);
        intent.putExtra(EXTRA_MESSAGE, createdGameJson);
        startActivity(intent);
    }

    private String getCreatedGameJson(@NonNull GameData newGame){
        final GameFormFragment gameFormFragment = getGetFormFragment();
        ((GameFormLocalDataSource)DataSourceUtil.getLocalDataSource(appContainer, GameFormLocalDataSource.class)).insert(gameFormFragment.getDatabase(), newGame);
        final GameData createdGame = ((GameFormRemoteDataSource)DataSourceUtil.getRemoteDataSource(appContainer, GameFormRemoteDataSource.class)).createGame(newGame);
        return JsonUtil.toJson(createdGame);
    }

    private GameFormFragment getGetFormFragment(){
        final GameFormCollectionFragment collectionFragment = getCollectionFragment();
        return (GameFormFragment) collectionFragment.getChildFragmentManager()
                .getFragments()
                .stream()
                .filter(fragment -> fragment.getClass().equals(GameFormFragment.class))
                .findFirst()
                .orElse(null);
    }

    private GameFormCollectionFragment getCollectionFragment(){
        return (GameFormCollectionFragment)getSupportFragmentManager()
                .getFragments()
                .get(0);
    }
}