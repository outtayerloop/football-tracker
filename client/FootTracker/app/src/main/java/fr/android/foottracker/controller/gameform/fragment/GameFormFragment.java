package fr.android.foottracker.controller.gameform.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.util.List;

import fr.android.foottracker.R;
import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.app.FootTrackerApplication;
import fr.android.foottracker.controller.DatePickerFragment;
import fr.android.foottracker.controller.TimePickerFragment;
import fr.android.foottracker.controller.gameform.GameFormActivity;
import fr.android.foottracker.model.database.DbHelper;
import fr.android.foottracker.model.entities.data.common.AbstractData;
import fr.android.foottracker.model.entities.data.ChampionshipData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.RefereeData;
import fr.android.foottracker.model.entities.data.TeamData;
import fr.android.foottracker.model.repositories.gameform.GameFormLocalDataSource;
import fr.android.foottracker.model.repositories.gameform.GameFormRemoteDataSource;
import fr.android.foottracker.model.view.GameFormViewModel;
import fr.android.foottracker.utils.DataSourceUtil;
import fr.android.foottracker.utils.DeviceKeyboardUtil;
import fr.android.foottracker.utils.GameFormValidator;
import fr.android.foottracker.utils.StringUtil;
import fr.android.foottracker.utils.TagUtil;

public class GameFormFragment extends Fragment {

    private FootTrackerAppContainer appContainer;
    private GameFormViewModel viewModel;
    private SQLiteDatabase db;
    private boolean isAlreadyInitializingDatabase = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContainer = ((FootTrackerApplication) requireActivity().getApplication()).getAppContainer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_form, container, false);
        setPickersFromView(view);
        DeviceKeyboardUtil.setOnTouchListener(view, requireActivity());
        setAllAutocompletionSlotsItemClickListeners(view);
        setAllSlotsTextChangeListeners(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GameFormViewModel.class);
        initGameFormFragment(() -> initGameSlots(view));
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public SQLiteDatabase getDatabase(){
        return db;
    }

    @Override
    public void onResume() {
        super.onResume();
        ensureOnResumeInitialization();
    }

    // N'initialise pas la connexion à la BD si elle est deja en cours dans onViewCreated().
    private void ensureOnResumeInitialization(){
        if(db == null && !isAlreadyInitializingDatabase)
            initGameFormFragment(null);
    }

    // Runnable : action à exécuter une fois que la BD locale est connectée.
    private void initGameFormFragment(@Nullable Runnable onDatabaseConnected) {
        appContainer.getExecutorService().execute(() -> {
            final SQLiteOpenHelper dbHelper = new DbHelper(requireContext());
            final SQLiteDatabase database = dbHelper.getWritableDatabase();
            isAlreadyInitializingDatabase = false;
            db = database;
            if(onDatabaseConnected != null)
                onDatabaseConnected.run();
        });
    }

    // Click listeners des pickers de date/heure de match
    private void setPickersFromView(@NonNull View view) {
        final EditText datePicker = view.findViewById(R.id.date_picker);
        final EditText timePicker = view.findViewById(R.id.time_picker);
        datePicker.setOnClickListener(this::showDatePicker);
        timePicker.setOnClickListener(this::showTimePicker);
    }

    // Lorsqu'on clique sur le champ de date, la pop up de choix de date s'ouvre.
    private void showDatePicker(@NonNull View view) {
        final DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(requireActivity().getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    // Lorsqu'on clique sur le champ de date, la pop up de choix d'heure/minute s'ouvre.
    private void showTimePicker(@NonNull View view) {
        final DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(requireActivity().getSupportFragmentManager(), TimePickerFragment.TAG);
    }

    private void initGameSlots(@NonNull View view) {
        List<ChampionshipData> championships = DataSourceUtil.getLocalDataSource(appContainer, GameFormLocalDataSource.class).getAll(db, ChampionshipData.class);
        final boolean mustBeSavedLocally = championships.size() == 0;
        if (mustBeSavedLocally)
            championships = ((GameFormRemoteDataSource)DataSourceUtil.getRemoteDataSource(appContainer, GameFormRemoteDataSource.class)).getAllChampionships();
        final List<ChampionshipData> finalChampionships = championships;
        handleUnfilteredSlotData(championships, view.findViewById(R.id.championship), mustBeSavedLocally, () -> viewModel.setAllChampionships(finalChampionships));
        initReferees(view);
    }

    private void initReferees(@NonNull View view) {
        List<RefereeData> referees = DataSourceUtil.getLocalDataSource(appContainer, GameFormLocalDataSource.class).getAll(db, RefereeData.class);
        final boolean mustBeSavedLocally = referees.size() == 0;
        if (mustBeSavedLocally)
            referees = ((GameFormRemoteDataSource)DataSourceUtil.getRemoteDataSource(appContainer, GameFormRemoteDataSource.class)).getAllReferees();
        final List<RefereeData> finalReferees = referees;
        handleUnfilteredSlotData(referees, view.findViewById(R.id.referee), mustBeSavedLocally, () -> viewModel.setAllReferees(finalReferees));
        initPlayers(view);
    }

    private void initPlayers(@NonNull View view) {
        List<PlayerData> players = DataSourceUtil.getLocalDataSource(appContainer, GameFormLocalDataSource.class).getAll(db, PlayerData.class);
        final boolean mustBeSavedLocally = players.size() == 0;
        if (mustBeSavedLocally)
            players = ((GameFormRemoteDataSource)DataSourceUtil.getRemoteDataSource(appContainer, GameFormRemoteDataSource.class)).getAllPlayers();
        final List<PlayerData> finalPlayers = players;
        appContainer.getHandler().post(() -> viewModel.setAllPlayers(finalPlayers));
        DataSourceUtil.handleDataToSaveLocally(players, mustBeSavedLocally, appContainer, db, null, GameFormLocalDataSource.class);
        initTeams(view);
    }

    private void initTeams(@NonNull View view) {
        List<TeamData> teams = DataSourceUtil.getLocalDataSource(appContainer, GameFormLocalDataSource.class).getAll(db, TeamData.class);
        final boolean mustBeSavedLocally = teams.size() == 0;
        if (mustBeSavedLocally)
            teams = ((GameFormRemoteDataSource)DataSourceUtil.getRemoteDataSource(appContainer, GameFormRemoteDataSource.class)).getAllTeams();
        final List<TeamData> finalTeams = teams;
        appContainer.getHandler().post(() -> afterTeamsRetrieval(finalTeams, view, mustBeSavedLocally));
    }

    private void afterTeamsRetrieval(@NonNull List<TeamData> teams, @NonNull View view, boolean mustBeSavedLocally){
        viewModel.setAllTeams(teams);
        postUnfilteredSlotData(teams, view.findViewById(R.id.tracked_team), mustBeSavedLocally, null);
        postUnfilteredSlotData(teams, view.findViewById(R.id.opponent_team), false, null);
    }

    // Execute apres avoir fini de recuperer localement/distant des donnees.
    private <TData extends AbstractData> void handleUnfilteredSlotData(@NonNull List<TData> slotData, @NonNull AutoCompleteTextView slot, boolean mustBeSavedLocally, @Nullable Runnable postInsertionRunnable) {
        appContainer.getHandler().post(() -> postUnfilteredSlotData(slotData, slot, mustBeSavedLocally, postInsertionRunnable));
    }

    // Remplit les suggestions de l'AutocompleteTextView + sauvegarde localement si besoin.
    private <TData extends AbstractData> void postUnfilteredSlotData(@NonNull List<TData> slotData, @NonNull AutoCompleteTextView slot, boolean mustBeSavedLocally, @Nullable Runnable postInsertionRunnable) {
        DataSourceUtil.fillAutocompletionSlotData(slotData, slot, requireContext());
        DataSourceUtil.handleDataToSaveLocally(slotData, mustBeSavedLocally, appContainer, db, postInsertionRunnable, GameFormLocalDataSource.class);
    }

    // Set les click listeners de l'autocompletion : déclenché quand on
    // clique sur une suggestion d'autocompletion.
    private void setAllAutocompletionSlotsItemClickListeners(@NonNull View view) {
        final ViewGroup rootElement = view.findViewById(R.id.game_form);
        final String discriminatingTag = getResources().getString(R.string.game_form_autocompletion_slot_tag);
        final List<AutoCompleteTextView> slots = TagUtil.getTaggedElements(rootElement, discriminatingTag);
        slots.forEach(this::setViewModelFieldFromClickedItem);
    }

    // Quand on clique sur une suggestion d'un AutocompleteTextView, on set le champ correspondant
    // du view model aussi
    private void setViewModelFieldFromClickedItem(@NonNull AutoCompleteTextView slot) {
        slot.setOnItemClickListener((parent, view, position, id) -> {
            final int slotId = slot.getId();
            final Object item = slot.getAdapter().getItem(position);
            setViewModelBySlotId(slotId, item);
        });
    }

    private void setViewModelBySlotId(int slotId, @NonNull Object item) {
        if (slotId == R.id.championship)
            viewModel.setChampionship((ChampionshipData) item);
        else if (slotId == R.id.tracked_team)
            viewModel.setTrackedTeam((TeamData) item);
        else if (slotId == R.id.opponent_team)
            viewModel.setOpponentTeam((TeamData) item);
        else if (slotId == R.id.referee)
            viewModel.setReferee((RefereeData) item);
    }

    // Listeners de chaque changement de texte dans les AutocompleteTextView
    private void setAllSlotsTextChangeListeners(@NonNull View view) {
        final List<AutoCompleteTextView> slots = getAllSlots(view);
        slots.forEach(slot -> slot.addTextChangedListener(new GameFormValidator(slot) {
            @Override
            public void validate(@NonNull TextView slot, @Nullable String slotContent) {
                validateSlot(slot, slotContent);
            }
        }));
    }

    // Recuperation de tous les AutocompleteTextView ayant le tag.
    private List<AutoCompleteTextView> getAllSlots(@NonNull View view) {
        final ViewGroup rootElement = view.findViewById(R.id.game_form);
        final String discriminatingTag = getResources().getString(R.string.game_form_autocompletion_slot_tag);
        return TagUtil.getTaggedElements(rootElement, discriminatingTag);
    }

    private void validateSlot(@NonNull TextView slot, @Nullable String slotContent) {
        if (StringUtil.isNullOrEmptyOrWhiteSpaceInput(slotContent))
            handleEmptySlot(slot);
        else if (isTeamConfrontingItself(slot, slotContent))
            slot.setError(getResources().getString(R.string.double_team_error));
        setViewModelField(slot);
    }

    // On vide l'equipe suivie du view model si on a tout effacé dans l'Autocomplete de l'equipe suivie
    // + erreur rouge de champ vide
    private void handleEmptySlot(@NonNull TextView slot) {
        slot.setError(getResources().getString(R.string.empty_slot_error));
        if (slot.getId() == R.id.tracked_team)
            viewModel.setTrackedTeam(null);
    }

    // Verifie que ce qu'on vient d'ecrire implique qu'une equipe s'affronte elle-même ou pas
    private boolean isTeamConfrontingItself(@NonNull TextView slot, @NonNull String slotContent) {
        if (isTeamSlot(slot)) {
            if (StringUtil.isNullOrEmptyOrWhiteSpaceInput(slotContent))
                return false;
            final String otherTeamName = getOtherTeamNameByCurrentTeamId(slot.getId());
            return StringUtil.areEquivalent(slotContent, otherTeamName);
        }
        return false;
    }

    // Determine si l'AutocompleteTextView actuelle est celle de l'equipe suivie ou de l'equipe adverse
    private boolean isTeamSlot(@NonNull TextView slot) {
        return slot.getId() == R.id.tracked_team
                || slot.getId() == R.id.opponent_team;
    }

    // Recupere le nom de l'equipe de l'autre AutocompleteTextView que celui dans lequel on vient d'ecrire
    private String getOtherTeamNameByCurrentTeamId(int currentTeamId) {
        final AutoCompleteTextView otherTeamSlot = getOtherTeamSlotByCurrentTeamId(currentTeamId);
        return otherTeamSlot.getText().toString();
    }

    // Recupere l'AutocompleteTextView de l'autre equipe que celle dans laquelle on vient d'ecrire.
    private AutoCompleteTextView getOtherTeamSlotByCurrentTeamId(int currentTeamId) {
        final int otherTeamSlotId;
        if (currentTeamId == R.id.tracked_team)
            otherTeamSlotId = R.id.opponent_team;
        else if (currentTeamId == R.id.opponent_team)
            otherTeamSlotId = R.id.tracked_team;
        else
            throw new IllegalArgumentException("Provided current team ID does not belong to a known game form team slot.");
        return requireView().findViewById(otherTeamSlotId);
    }

    // Quand on a ecrit dans un des champs, on modifie ausi le champ correspondant du view model.
    private void setViewModelField(@NonNull TextView slot) {
        final int slotId = slot.getId();
        final String slotContent = slot.getText().toString();
        if (slotId == R.id.championship)
            setViewModelChampionship(slotContent);
        else if (slotId == R.id.referee)
            setViewModelReferee(slotContent);
        else if (slotId == R.id.tracked_team)
            setViewModelTrackedTeam(slotContent);
        else if (slotId == R.id.opponent_team)
            setViewModelOpponentTeam(slotContent);
    }

    // Modification de la competition du view model avec le nouveau nom de competition
    // tapé par l'utilisateur (si efffacé, set à null).
    private void setViewModelChampionship(@Nullable String championshipName) {
        ChampionshipData championship = null;
        if (!StringUtil.isNullOrEmptyOrWhiteSpaceInput(championshipName)) {
            championshipName = StringUtil.toCapitalized(championshipName);
            championship = viewModel.getChampionshipByName(championshipName);
            if (championship == null)
                championship = new ChampionshipData(championshipName);
        }
        viewModel.setChampionship(championship);
    }

    // Modification de l'arbitre du view model avec le nouveau nom d'arbitre (nom + prenom)
    // tapé par l'utilisateur (si efffacé, set à null).
    private void setViewModelReferee(@Nullable String refereeFullName) {
        RefereeData referee = null;
        if (!StringUtil.isNullOrEmptyOrWhiteSpaceInput(refereeFullName)) {
            refereeFullName = StringUtil.toCapitalized(refereeFullName);
            referee = viewModel.getRefereeByFullName(refereeFullName);
            if (referee == null)
                referee = new RefereeData(refereeFullName);
        }
        viewModel.setReferee(referee);
    }

    // Modification de l'equipe suivie du view model avec le nouveau nom d'equipe suivie
    // tapé par l'utilisateur (si efffacé, set à null).
    private void setViewModelTrackedTeam(@Nullable String trackedTeamName) {
        final TeamData trackedTeam = getTeamToSaveInViewModel(trackedTeamName);
        viewModel.setTrackedTeam(trackedTeam);
    }

    // Modification de l'equipe adverse du view model avec le nouveau nom d'equipe adverse
    // tapé par l'utilisateur (si efffacé, set à null).
    private void setViewModelOpponentTeam(@Nullable String opponentTeamName) {
        final TeamData opponentTeam = getTeamToSaveInViewModel(opponentTeamName);
        viewModel.setOpponentTeam(opponentTeam);
    }

    // Recuperation d'une equipe parmi celles du view model à partir du nom d'equipe fourni.
    // Sert à modifier une des equipes du view model à partir du nom d'equipe tapé par l'utilisateur.
    private TeamData getTeamToSaveInViewModel(@Nullable String teamName) {
        TeamData teamToSaveInViewModel = null;
        if (!StringUtil.isNullOrEmptyOrWhiteSpaceInput(teamName)) {
            teamToSaveInViewModel = viewModel.getTeamByName(teamName);
            if (teamToSaveInViewModel == null)
                teamToSaveInViewModel = new TeamData(teamName);
        }
        return teamToSaveInViewModel;
    }
}
