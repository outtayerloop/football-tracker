package fr.android.foottracker.controller.gameupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import fr.android.foottracker.R;
import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.app.FootTrackerApplication;
import fr.android.foottracker.controller.gameupdate.fragment.LaunchOvertTimeDalog;
import fr.android.foottracker.controller.gameform.GameFormActivity;
import fr.android.foottracker.controller.gameupdate.fragment.InputControlDialog;
import fr.android.foottracker.controller.home.MainActivity;
import fr.android.foottracker.model.database.DbHelper;
import fr.android.foottracker.model.entities.CardType;
import fr.android.foottracker.model.entities.data.AttachmentData;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.GoalData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.view.GamePerformanceViewModel;
import fr.android.foottracker.utils.DataSourceUtil;
import fr.android.foottracker.utils.DeviceKeyboardUtil;
import fr.android.foottracker.utils.JsonUtil;
import fr.android.foottracker.utils.LocaleUtil;
import fr.android.foottracker.utils.StringUtil;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

public class GameUpdateActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private final ArrayList<Button> updateButtons = new ArrayList<>();
    private final Map<String, Integer> actionsToUpdate = new HashMap<>();
    private String lastPerformanceUpdated=null;
    private GamePerformanceViewModel gamePerformanceViewModel;
    private Spinner cardSpinner;
    private Spinner shotSpinner;
    private Spinner cardPlayerSpinner;
    private Spinner goalPlayerSpinner;
    private Spinner shotPlayerSpinner;
    private InputControlDialog alertManagerInputControlDialog;
    private LaunchOvertTimeDalog alertManagerOvertimeConfirmDialog;
    private ArrayList<String> playerListFromCreatedGame;
    private SQLiteDatabase db;
    private FootTrackerAppContainer appContainer;
    private GameData gameDataFromPreviousActivity;
    private AttachmentData newAttachment;
    private final ArrayList<CardData> currentGameCardList = new ArrayList<>();
    private final ArrayList<GoalData> currentGameGoalsList = new ArrayList<>();
    private final ArrayList<AttachmentData> currentGameAttachmentList = new ArrayList<>();
    private Calendar beginOverTime;
    private Calendar endOverTime;
    private boolean isAlreadyInitializingDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_update);
        settingsForUpdateActivityUi();
        View currentView = findViewById(R.id.updateView);
        DeviceKeyboardUtil.setOnTouchListener(currentView, this);
        initDataBase();
        LocaleUtil.initLocale(this);
    }

    public void onDestroy() {
        super.onDestroy();
        if(db != null)
            db.close();
    }
    
    public void onResume() {
        super.onResume();
        if(db==null && !isAlreadyInitializingDatabase){
            initDataBase();
        }
    }
    
    private void initDataBase() {
        isAlreadyInitializingDatabase=true;
        appContainer.getExecutorService().execute(() -> {
            final SQLiteOpenHelper dbHelper = new DbHelper(this);
            final SQLiteDatabase database = dbHelper.getWritableDatabase();
            db = database;
            isAlreadyInitializingDatabase = false;
        });
    }
    
    /**
     * R??alise l'ensemble des initialisations des composants android utilis??s
     * dans l'interface (boutton, spinner, listener, tag...)
     */
    public void settingsForUpdateActivityUi(){
        gamePerformanceViewModel = new ViewModelProvider(this).get(GamePerformanceViewModel.class);
        appContainer = ((FootTrackerApplication) this.getApplication()).getAppContainer();
        initializeGamePlayerList();
        setAlertDialogForActivity();
        setObserverForViewModelData(gamePerformanceViewModel);
        setSpinnerForUpdateMenu();
        setButtonForUpdateMenu();
    }

    /**
     * D??clench?? lorsque le menu est cr????. On remplace celui par d??faut par le n??tre (R.menu.menu_game_form).
     * @param menu Menu qu'on veut modifier.
     * @return true pour terminer la modification.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_form, menu); // Remplace le menu actuel par celui indiqu?? dans l'inflate.
        menu.getItem(3).setVisible(false); // On cache l'ic??ne de validation de match.
        LocaleUtil.setLocaleSwitch(menu, this);
        return true;
    }
    /**
     * D??clarare les bo??te de dialogue qui seront utilis??es au sein de l'activit??
     */
    private void setAlertDialogForActivity(){
        // click sur un spinner/action sans avoir selectionne d'item du spinner
        alertManagerInputControlDialog = InputControlDialog.newInstance("MyInputControlFragment");
        alertManagerOvertimeConfirmDialog = LaunchOvertTimeDalog.newInstance("MyOverTimeConfirmDialog");
    }
    /**
     * Initialise la liste des joueurs qui sera utilis??e dans les spinners servant ?? renseigner
     * le nom des joueurs pour certaines actions (goal, shot, card...)
     */
    private void initializeGamePlayerList(){
        gameDataFromPreviousActivity = deserializeDataSend();
        List<PlayerData>  playerDataList = gameDataFromPreviousActivity.getTrackedTeam().getPlayers();
        playerListFromCreatedGame = new ArrayList<>();
        playerListFromCreatedGame.add("");
        for(PlayerData player: playerDataList){
            playerListFromCreatedGame.add(player.getName());
        }
    }
    /**
     * D??serialise l'objet Data envoy?? depuis l'activit?? pr??c??dente
     * afin d'extraire des informations tel que la liste des joueurs
     * qui sera utilis??e dans l'activit?? update
     */
    private GameData deserializeDataSend(){
        Intent intent = getIntent();
        String message = intent.getStringExtra(GameFormActivity.EXTRA_MESSAGE);
        gameDataFromPreviousActivity = JsonUtil.fromJson(message, GameData.class);
        return gameDataFromPreviousActivity;
    }
    /**
     * Pour les tir ou les cartons, r??cup??re l'action sp??cifique comme carton rouge ou jaune
     * au lieu de carton tout ou encore penalty ou free kick plut??t que shot
     * pour mettre ?? jour l'action sp??cifique
     */
    private void retrieveSpecificShotOrCardForUpdate() {
        if (lastPerformanceUpdated != null) {
            if(lastPerformanceUpdated.equals(getResources().getString(R.string.shot)))
                lastPerformanceUpdated = shotSpinner.getSelectedItem().toString();
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.card)))
                lastPerformanceUpdated = cardSpinner.getSelectedItem().toString();
        }
    }

    /**
     * Mets ?? jour l'affichage du textView de l'action qui vient d'??tre incr??ment??e
     * @param textViewList la liste des texView qui affiche les perfromances de chaque actions
     * @param newValue la nouvelle valeur ?? affecter au textView ?? actualiser
     */
    private void performUiUpdateAfterChangeValue(List<TextView> textViewList, int newValue){
        appContainer.getHandler().post(() -> {
            if (lastPerformanceUpdated != null) {
                for (Button button : updateButtons) {
                    if (lastPerformanceUpdated.contentEquals(button.getText())) {
                        textViewList.get(Integer.parseInt(String.valueOf(button.getTag()))).setText(String.valueOf(newValue));
                    }
                }
                if(lastPerformanceUpdated.equals(getResources().getString(R.string.red_card)))
                    textViewList.get(6).setText(String.valueOf(newValue));
                else if(lastPerformanceUpdated.equals(getResources().getString(R.string.yellow_card)))
                    textViewList.get(8).setText(String.valueOf(newValue));
                else if(lastPerformanceUpdated.equals(getResources().getString(R.string.penalty)))
                    textViewList.get(9).setText(String.valueOf(newValue));
                else if(lastPerformanceUpdated.equals(getResources().getString(R.string.free_kick)))
                    textViewList.get(11).setText(String.valueOf(newValue));
            }
        });

    }
    
    /**
     * R??cup??re les texView qui affichent les perfromances de l'??quipe
     */
    private List<TextView> retrieveTextViewToUpdateFromUi(){
        //RETRIEVE TEXTVIEW TO UPDATE
        final TextView outValue = (TextView) findViewById(R.id.out_value);
        final TextView offsideValue = (TextView) findViewById(R.id.offside_value);
        final TextView faultValue = (TextView) findViewById(R.id.fault_value);
        final TextView redCardValue = (TextView) findViewById(R.id.redCard_value);
        final TextView yellowCardValue = (TextView) findViewById(R.id.yellowCard_value);
        final TextView penaltyValue = (TextView) findViewById(R.id.penalty_value);
        final TextView freeKickValue = (TextView) findViewById(R.id.freeKick_value);
        final TextView possessionValue = (TextView) findViewById(R.id.possession_value);
        final TextView occasionValue = (TextView) findViewById(R.id.occasion_value);
        final TextView cornerValue = (TextView) findViewById(R.id.corner_value);
        final TextView stopValue = (TextView) findViewById(R.id.stop_value);
        final TextView goalValue = (TextView) findViewById(R.id.goal_value);
        List<TextView> textViewToUpdate=new ArrayList<>();
        textViewToUpdate.add(outValue);
        textViewToUpdate.add(offsideValue);
        textViewToUpdate.add(faultValue);
        textViewToUpdate.add(possessionValue);
        textViewToUpdate.add(occasionValue);
        textViewToUpdate.add(stopValue);
        textViewToUpdate.add(redCardValue);
        textViewToUpdate.add(goalValue);
        textViewToUpdate.add(yellowCardValue);
        textViewToUpdate.add(penaltyValue);
        textViewToUpdate.add(cornerValue);
        textViewToUpdate.add(freeKickValue);
        return textViewToUpdate;
    }
    /**
     * Mets un observer sur chaque data pour suivre leur changement de valeur et permerttre de
     * mettre ?? jour l'interface ensuite
     * @param gamePerformance classe qui stocke les donn??es de performances, qu'on doit suivre
     * @param nameObserver l'objet qui suivra les donn??es tout au long de leur cycle de vie et informera en cas de changement
     */
    private void setObserverForEachDataTrackingGamePerformances(GamePerformanceViewModel gamePerformance, androidx.lifecycle.Observer<Integer> nameObserver){
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        gamePerformance.getOccasionCount().observe(this, nameObserver);
        gamePerformance.getOutCount().observe(this, nameObserver);
        gamePerformance.getFaultCount().observe(this, nameObserver);
        gamePerformance.getOffsideCount().observe(this, nameObserver);
        gamePerformance.getRedcardCount().observe(this, nameObserver);
        gamePerformance.getYellowcardCount().observe(this, nameObserver);
        gamePerformance.getPelnaltyCount().observe(this, nameObserver);
        gamePerformance.getFreekickCount().observe(this, nameObserver);
        gamePerformance.getCornerCount().observe(this,nameObserver);
        gamePerformance.getPossessionCount().observe(this, nameObserver);
        gamePerformance.getStopCount().observe(this, nameObserver);
        gamePerformance.getGoalCount().observe(this, nameObserver);
        gamePerformance.getScoreCount().observe(this, nameObserver);
    }
    
    /**
     * D??fini l'objet qui suivra le changement des donn??es et sp??cifie l'action
     * ?? effectuer en cas de changement de ces valeurs
     * @param gamePerformance classe qui stocke les donn??es de performances, qu'on doit suivre
     */
    public void setObserverForViewModelData(GamePerformanceViewModel gamePerformance){
        final androidx.lifecycle.Observer<Integer> nameObserver = new androidx.lifecycle.Observer<Integer>() {
            public void update(Observable o, Object arg) {}
            public void onChanged(@Nullable final Integer newValue) {
                retrieveSpecificShotOrCardForUpdate();
                List<TextView> textViewList = retrieveTextViewToUpdateFromUi();
                performUiUpdateAfterChangeValue(textViewList, newValue);
                }
            };
            setObserverForEachDataTrackingGamePerformances(gamePerformance,nameObserver);
    }

    /**
     * Met en place les listener, tag, les tableaux de suivi des actions pour le match
     */
    private void setButtonForUpdateMenu() {
        retrieveAndInitializeButtonsFromView(updateButtons);
        setTagAndOnListenerForEachButton(updateButtons);
    }
    /**
     * R??cup??re les boutons de la vue et les ajoutes ?? une liste de boutons
     */
    private void retrieveAndInitializeButtonsFromView(ArrayList<Button> button) {
        for (int i = 0; i < 13; i++) {
            int id = getResources().getIdentifier("button_" + i, "id", getPackageName());
            button.add((Button) findViewById(id));
        }
    }
    /**
     * Met en place des listener et des tags sur chaque bouton.
     * Initialise le dictionnaire qui suit les actions ainsi que leur valeur pour le match
     */
    private void setTagAndOnListenerForEachButton(ArrayList<Button> button) {
        for (int i = 0; i < button.size(); i++) {
            button.get(i).setOnClickListener(this);
            button.get(i).setTag(i);
            if(button.get(i).getText().toString().equals(getResources().getString(R.string.card))){
                initializeActionToUpdateDictionnary(getResources().getString(R.string.red_card));
                initializeActionToUpdateDictionnary(getResources().getString(R.string.yellow_card));
            }
            if(button.get(i).getText().toString().equals(getResources().getString(R.string.shot))){
                initializeActionToUpdateDictionnary(getResources().getString(R.string.penalty));
                initializeActionToUpdateDictionnary(getResources().getString(R.string.free_kick));
            }
            if(button.get(i).getText().toString().equals(getResources().getString(R.string.takePhoto))){
                button.get(i).setOnClickListener(this::onPictureItemClick);
            }
            else
                initializeActionToUpdateDictionnary(button.get(i).getText().toString());
        }

    }
    /**
     * @param buttonText texte du boutton repr??sentant l'action ?? incr??menter
     * Initialise le dictionnaire
     */
    private void initializeActionToUpdateDictionnary(String buttonText){
        actionsToUpdate.put(buttonText, 0);
    }
    /**
     * Initialise les spinner du menu update
     */
    public void setSpinnerForUpdateMenu() {
        setCardOvertimeShotSpinner();
        setPlayerSpinner();
    }
    /**
     * Initialise les spinner de carton et de tir
     */
    private void setCardOvertimeShotSpinner() {
        cardSpinner = (Spinner) findViewById(R.id.card_spinner);
        shotSpinner = (Spinner) findViewById(R.id.shot_spinner);

        Map<Spinner, Integer> spinnerArray = new HashMap<>();
        initializeSpinnerMap(spinnerArray, cardSpinner, shotSpinner);
        spinnerArray.forEach(this::setSingleSpinner);
    }
    /**
     * Initialise un dictionnaire qui contient les spinner ainsi que leur liste d'??l??ments
     * @param spinnerArray le dictionnaire ?? remplir
     * @param cardSpinner le spinner de carton
     * @param shotSpinner le spinner de tir (penalty, free kick)
     */
    private void initializeSpinnerMap(Map<Spinner, Integer> spinnerArray, Spinner cardSpinner,  Spinner shotSpinner) {
        spinnerArray.put(cardSpinner, R.array.cards_array);
        spinnerArray.put(shotSpinner, R.array.shot_array);
    }
    /**
     * Initialise un seul spinner
     * @param spinner le spinner ?? set
     * @param resourceId les ressources ?? ins??rer dans le spinner
     */
    private void setSingleSpinner(Spinner spinner, int resourceId) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
    /**
     * Initialise les spinner qui contiennent la liste des joeurs
     */
    private void setPlayerSpinner() {
        cardPlayerSpinner = (Spinner) findViewById(R.id.player_card);
        goalPlayerSpinner = (Spinner) findViewById(R.id.player_goal);
        shotPlayerSpinner = (Spinner) findViewById(R.id.player_shot);

        ArrayAdapter<String> adapterPlayer = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, playerListFromCreatedGame);
        adapterPlayer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cardPlayerSpinner.setAdapter(adapterPlayer);
        goalPlayerSpinner.setAdapter(adapterPlayer);
        shotPlayerSpinner.setAdapter(adapterPlayer);
        setListenerOnPlayerSpinners();
    }
    /**
     * Applique un listener ?? chaque spinner
     */
    private void setListenerOnPlayerSpinners(){
        cardPlayerSpinner.setOnItemSelectedListener(this);
        goalPlayerSpinner.setOnItemSelectedListener(this);
        shotPlayerSpinner.setOnItemSelectedListener(this);
        cardSpinner.setOnItemSelectedListener(this);
        shotSpinner.setOnItemSelectedListener(this);
    }
    /**
     * Callback m??thode pouvant ??tre ??voqu??e lorsqu'un ??l??ment de la liste est selectionn??
     * @param view la vue se  trouvant ?? l'int??rieur de l'adapteurView o?? l'??l??ment selctionn?? se trouve
     * @param parent L'adapterView dans laquelle l'??lement selectionn?? se trouve
     * @param position position de la vue dans l'adapter
     * @param id id de la ligne correspondante ?? l'??l??ment s??lectionn??
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * R??cup??re l'action de tir ?? incr??menter et affecte la nouvelle valeur ?? l'action
     * @param newValue nouvelle valeur ?? affecter
     */
    public void handleShotButtonClick(int newValue){
        if(shotSpinner.getSelectedItem()!=null && shotPlayerSpinner!=null){
            if(shotSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.penalty))){
                gamePerformanceViewModel.getPelnaltyCount().setValue(newValue);
                gameDataFromPreviousActivity.setPenaltyCount(newValue);
            }
            if(shotSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.free_kick))){
                gamePerformanceViewModel.getFreekickCount().setValue(newValue);
                gameDataFromPreviousActivity.setFreeKickCount(newValue);
            }
            resetSelectionSpinner(shotSpinner,shotPlayerSpinner);
        }
    }
    /**
     * R??cup??re le type de carton ?? incr??menter et affecte la nouvelle valeur ?? l'action
     * @param newValue nouvelle valeur ?? affecter
     */
    public void handleCardButtonClicked(int newValue){

        if(cardSpinner.getSelectedItem()!=null && cardPlayerSpinner!=null) {
            if (cardSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.red_card))) {
                gamePerformanceViewModel.getRedcardCount().setValue(newValue);
            }
            if (cardSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.yellow_card))) {
                gamePerformanceViewModel.getYellowcardCount().setValue(newValue);
            }
            CardData card = (CardData)createCardOrGoalData(cardSpinner.getSelectedItem().toString(), cardPlayerSpinner.getSelectedItem().toString());
            if(StringUtil.areEquivalent(card.getType(), "JAUNE"))
                card.setType(CardType.YELLOW);
            else if(StringUtil.areEquivalent(card.getType(), "ROUGE"))
                card.setType(CardType.RED);
            addGoalOrCardToPlayer(cardPlayerSpinner.getSelectedItem().toString(),card);
            resetSelectionSpinner(cardSpinner,cardPlayerSpinner);
        }
    }
    /**
     * G??re l'ajout du nombre de but
     * @param newValue nouvelle valeur ?? affecter
     */
    public void handleGoalButtonClick(int newValue){
        if(goalPlayerSpinner.getSelectedItem()!=null) {
            GoalData goal = (GoalData)createCardOrGoalData(null,goalPlayerSpinner.getSelectedItem().toString());
            addGoalOrCardToPlayer(goalPlayerSpinner.getSelectedItem().toString(),goal);
            gamePerformanceViewModel.getGoalCount().setValue(newValue);
            gameDataFromPreviousActivity.setTrackedTeamScore(newValue);
            goalPlayerSpinner.setSelection(0);
        }
    }

    /**
     * Remet ?? null la s??lection du spinner, prend deux param??tres pour les spinner fonctionnant par paire
     * @param Spinner spinner principal contenant l'action ?? incr??menter
     * @param playerSpinner spinner de la liste des joueurs
     */
    private void resetSelectionSpinner(Spinner Spinner, Spinner playerSpinner){
        Spinner.setSelection(0);
        playerSpinner.setSelection(0);
    }

    @Override
    public void onClick(View view) {
        Button selectedButton = (Button) view;
        onClickHandler(selectedButton.getText().toString());
    }

    /**
     * Valide le jeu: r??cup??re la valeur de prolongations, effectue le stockage en local et ?? distance des donn??es du match
     * @param buttonText texte pr??sent sur le bouton
     */
    private void handleValidateGameProcess(String buttonText){
        if(buttonText.equals(getResources().getString(R.string.validate))) {
            if (beginOverTime != null) {

                Calendar endOverTime = Calendar.getInstance();
                endOverTime.add(Calendar.HOUR, -beginOverTime.get(Calendar.HOUR));
                endOverTime.add(Calendar.MINUTE, -beginOverTime.get(Calendar.MINUTE));
                endOverTime.add(Calendar.SECOND, -beginOverTime.get(Calendar.SECOND));
                String overTimeHours = Integer.toString(endOverTime.get(Calendar.HOUR)).length() < 2
                        ? "0" + endOverTime.get(Calendar.HOUR)
                        : Integer.toString(endOverTime.get(Calendar.HOUR));
                String overTimeMinutes = Integer.toString(endOverTime.get(Calendar.MINUTE)).length() < 2
                        ? "0" + endOverTime.get(Calendar.MINUTE)
                        : Integer.toString(endOverTime.get(Calendar.MINUTE));
                String overTimeSeconds = Integer.toString(endOverTime.get(Calendar.SECOND)).length() < 2
                        ? "0" + endOverTime.get(Calendar.SECOND)
                        : Integer.toString(endOverTime.get(Calendar.SECOND));
                String overTimeValue = overTimeHours + ":" + overTimeMinutes + ":" + overTimeSeconds;
                gameDataFromPreviousActivity.setOverTime(overTimeValue);
            }

            EditText opponentScore = findViewById(R.id.Opponent_Score);
            if (!opponentScore.getText().equals("")) {
                gameDataFromPreviousActivity.setOpponentScore(Integer.parseInt(opponentScore.getText().toString()));
            } else {
                gameDataFromPreviousActivity.setOpponentScore(0);
            }

            appContainer.getExecutorService().execute(()->{
                int trackedTeamScore = 0;
                List<PlayerData> trackedTeamPlayers = gameDataFromPreviousActivity.getTrackedTeam().getPlayers();
                for(int i = 0; i < trackedTeamPlayers.size(); ++i){
                    trackedTeamScore += trackedTeamPlayers.get(i).getGameGoalCount(gameDataFromPreviousActivity.getId());
                }
                gameDataFromPreviousActivity.setTrackedTeamScore(trackedTeamScore);
                gameDataFromPreviousActivity.setOpponentTeamPossessionCount(new Random().nextInt(100)); // Max nb possessions equipe adverse : 100 (arbitraire temporairement).

                DataSourceUtil.getLocalDataSourceForUpdate(appContainer).update(db, gameDataFromPreviousActivity);
                DataSourceUtil.getRemoteDataSourceForUpdate(appContainer).updateGame(gameDataFromPreviousActivity);

                for (GoalData goal : currentGameGoalsList) {
                    if (goal == null) Log.d("", "Element null avant l'envoi");
                    DataSourceUtil.getLocalDataSourceForUpdate(appContainer).insert(db, goal);
                    DataSourceUtil.getRemoteDataSourceForUpdate(appContainer).insertGoal(goal);
                }
                for (CardData card : currentGameCardList) {
                    if (card == null) Log.d("", "Element null avant l'envoi");
                    DataSourceUtil.getLocalDataSourceForUpdate(appContainer).insert(db, card);
                    DataSourceUtil.getRemoteDataSourceForUpdate(appContainer).insertCard(card);
                }
                for (AttachmentData attachment : currentGameAttachmentList) {
                    if (attachment == null) Log.d("", "Element null avant l'envoi");
                    DataSourceUtil.getLocalDataSourceForUpdate(appContainer).insert(db, attachment);
                    DataSourceUtil.getRemoteDataSourceForUpdate(appContainer).insertAttachment(attachment);
                }
                appContainer.getHandler().post(()->{
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                });
            });

        }
    }
    /**
     * Lance un fen??tre de dialog afin d'avertir l'utilisateur qu'il manque des informations (pr??sent dans les spinner)
     * afin de valider la mise ?? jour
     * @param buttonText texte pr??sent sur le bouton
     */
    private Boolean launchWarningIfInformationsIsMissingForUpdate(String buttonText){
        if(buttonText.equals(getResources().getString(R.string.shot)) && shotSpinner.getSelectedItem().toString().equals("") ||
                buttonText.equals(getResources().getString(R.string.shot)) && shotPlayerSpinner.getSelectedItem().toString().equals("")||
                buttonText.equals(getResources().getString(R.string.card)) && cardSpinner.getSelectedItem().toString().equals("")||
                buttonText.equals(getResources().getString(R.string.card)) && cardPlayerSpinner.getSelectedItem().toString().equals("")||
                buttonText.equals(getResources().getString(R.string.goal)) && goalPlayerSpinner.getSelectedItem().toString().equals("")
        ){
            alertManagerInputControlDialog.show(getSupportFragmentManager(),"Fire");
            return false;
        }
        return true;
    }
    /**
     * Se charge de calculer la valeur de prolongations
     * Il m??morise le d??but des prolongations et le moment o?? le match est valid??.
     * @param buttonText texte pr??sent sur le bouton
     */
    private void handleOvertimeProcess(String buttonText){
        if(buttonText.equals(getResources().getString(R.string.overtime))){
            final androidx.lifecycle.Observer<Boolean> nameObserver = new androidx.lifecycle.Observer<Boolean>() {
                public void update(Observable o, Object arg) { }
                public void onChanged(@Nullable final Boolean newValue) { beginOverTime = Calendar.getInstance();
                if(alertManagerOvertimeConfirmDialog.getPositiveButtonClicked().getValue()){
                    Button overTimeButton = (Button) findViewById(R.id.button_9);
                    overTimeButton.setVisibility(View.INVISIBLE);
                }

                }
            };
            alertManagerOvertimeConfirmDialog.getPositiveButtonClicked().observe(this,nameObserver);
            alertManagerOvertimeConfirmDialog.show(getSupportFragmentManager(),"Fire");
        }
    }

    /**
     * G??re les click sur les boutons de la vue update
     * @param buttonText texte pr??sent sur le bouton
     */
    private void onClickHandler(String buttonText) {

        String actionName=buttonText;
        boolean informationIsCompleted;
        informationIsCompleted=launchWarningIfInformationsIsMissingForUpdate(buttonText);
        if(buttonText.equals(getResources().getString(R.string.shot)) && !shotSpinner.getSelectedItem().toString().equals("")){
            buttonText= shotSpinner.getSelectedItem().toString();
        }
        if(buttonText.equals(getResources().getString(R.string.card)) && !cardSpinner.getSelectedItem().toString().equals("")){
            buttonText= cardSpinner.getSelectedItem().toString();
        }

        lastPerformanceUpdated=actionName;

        if(informationIsCompleted){
            actionsToUpdate.replace(buttonText, actionsToUpdate.get(buttonText) + 1);
            int newValue = actionsToUpdate.get(buttonText);

            if(lastPerformanceUpdated.equals(getResources().getString(R.string.out))){
                gamePerformanceViewModel.getOutCount().setValue(newValue);
                gameDataFromPreviousActivity.setOutCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.offside))){
                gamePerformanceViewModel.getOffsideCount().setValue(newValue);
                gameDataFromPreviousActivity.setOffsideCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.faults))){
                gamePerformanceViewModel.getFaultCount().setValue(newValue);
                gameDataFromPreviousActivity.setFaultCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.corner))){
                gamePerformanceViewModel.getCornerCount().setValue(newValue);
                gameDataFromPreviousActivity.setCornerCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.shot)))
                handleShotButtonClick(newValue);
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.card)))
                handleCardButtonClicked(newValue);
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.possession))){
                gamePerformanceViewModel.getPossessionCount().setValue(newValue);
                gameDataFromPreviousActivity.setTrackedTeamPossessionCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.occasion))){
                gamePerformanceViewModel.getOccasionCount().setValue(newValue);
                gameDataFromPreviousActivity.setOccasionCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.stop))){
                gamePerformanceViewModel.getStopCount().setValue(newValue);
                gameDataFromPreviousActivity.setStopCount(newValue);
            }
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.goal)))
                handleGoalButtonClick(newValue);
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.overtime)))
                handleOvertimeProcess(buttonText);
            else if(lastPerformanceUpdated.equals(getResources().getString(R.string.validate)))
                handleValidateGameProcess(buttonText);
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void onPictureItemClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Action qui permet de prendre en photo depuis notre appareil
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try { photoFile = createImageFile();}
            catch (IOException ex) {}
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fr.android.foottracker.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);//avoir un retour dans le onActivityResult, param??tre r??utilis?? dans la m??thode

            }
        }
    }

    /**
     * CallBack de l'intent qui permet de prendre en photo
     * @param requestCode code de requ??te permettant d'identifier qui a envoy?? la requ??te
     * @param resultCode code de r??sultat
     * @param data la photo qui vient d'??tre prise
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        galleryAddPic();
    }
    String currentPhotoPath;

    /**
     * D??fini la nomenclature pour les titres des photos, le lieu de stockage de la photo
     * Cr???? le fichier de la photo (nom, chemin, stockage...)
     * Stocke l'image dans une liste de suivi des photos du match
     * */
    private File createImageFile() throws IOException {
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + dateTime + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //return Android/data/data/my_package/
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        AttachmentData newAttachment = new AttachmentData(0, currentPhotoPath, gameDataFromPreviousActivity.getId());
        currentGameAttachmentList.add(newAttachment);
        return image;
    }
    /**
     * Ajoute ?? la galerie la photo venant d'??tre prise
     * */
    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    /**
     * Cr???? une entit?? Card ou Goal ?? chaque fois qu'on met ?? jour l'une des deux actions
     * On ins??re ces entit??s au joueur concern??
     */
    public AbstractIdentifiedData createCardOrGoalData(String cardType, String playerName){
       long playerId = 0;
        for(PlayerData player:gameDataFromPreviousActivity.getTrackedTeam().getPlayers()){
            if(playerName.equals(player.getName())){
                playerId=player.getId();
            }
        }
       AbstractIdentifiedData dataToCreate;
       if(cardType==null){
           dataToCreate = new GoalData(gameDataFromPreviousActivity.getId(),playerId, LocalTime.now().toString());
           currentGameGoalsList.add((GoalData)dataToCreate);
           return dataToCreate;
       }
       dataToCreate = new CardData(cardType,playerId, gameDataFromPreviousActivity.getId(),LocalTime.now().toString());
        currentGameCardList.add((CardData)dataToCreate);
        return dataToCreate;
    }

    /**
     * Ajout d'un Goal ou Card ?? la liste correspondante du joueur concern??
     * @param playerName nom du joueur ayant commis l'action
     * @param data le type d'entit?? ?? cr??er (Goal ou Card)
     */
    private void addGoalOrCardToPlayer(String playerName, AbstractIdentifiedData data){
        PlayerData foundPlayer = gameDataFromPreviousActivity.getTrackedTeam().getPlayers()
                .stream()
                .filter(player -> player.getName().equals(playerName))
                .findFirst()
                .orElse(null);
        foundPlayer.addCardOrGoal(data);
    }
}