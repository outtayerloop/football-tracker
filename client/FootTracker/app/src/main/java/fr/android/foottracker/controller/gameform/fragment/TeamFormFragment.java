package fr.android.foottracker.controller.gameform.fragment;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fr.android.foottracker.R;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.TeamData;
import fr.android.foottracker.model.view.GameFormViewModel;
import fr.android.foottracker.utils.DataSourceUtil;
import fr.android.foottracker.utils.DeviceKeyboardUtil;
import fr.android.foottracker.utils.GameFormValidator;
import fr.android.foottracker.utils.PlayerUtil;
import fr.android.foottracker.utils.StringUtil;
import fr.android.foottracker.utils.TagUtil;

public class TeamFormFragment extends Fragment {

    private GameFormViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_team_form, container, false);
        // Si on clique à côté, on enlève le clavier.
        DeviceKeyboardUtil.setOnTouchListener(view, requireActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GameFormViewModel.class);
        setAllAutocompletionSlotsItemClickListeners(); // Ici car appel à requireView() induit.
        setAllPlayerSlotsTextChangeListeners(); // Ici car appel à requireView() induit.
        observeTrackedTeam(); // à chaque changement du champ trackedTeam du view model, listener déclenché
    }

    @Override
    public void onResume() {
        super.onResume();
        // L'observer du champ tracked team du view model ne se déclenche pas si on arrive sur cette page
        // sans avoir rien fait dans le champ de l'equipe suivie de la page precedente.
        // Gestion de ce cas ici :
        checkForUnobservedTrackedTeam();
    }

    // Set un observer pour le champ trackedTeam du view model : notifié à chaque changement de ce champ.
    private void observeTrackedTeam() {
        viewModel.getTrackedTeamMutableLiveData().observe(getViewLifecycleOwner(), trackedTeam -> {
            // observer déclenché au moins 1 fois : se déclenchera par la suite en cas de changement
            // et pas de problème
            // d'observer qui ne se sera pas déclenché
            viewModel.setHasTriggeredTeamFormObserver(true);
            if (mustFillAllPlayerSlots(trackedTeam))
                fillAllPlayerSlots(); // Si l'equipe suivie remplie a deja des joueurs, autocompletion direct
            // Si equipe suivie remplie n'a pas de joueurs, soit l'input de l'equipe suivie a été effacé,
            // soit il faut que l'utilisateur remplisse lui-même les joueurs de l'equipe.
            else
                handleTeamCreation();
        });
    }

    // L'observer du champ tracked team du view model ne se déclenche pas si on arrive sur cette page
    // sans avoir rien fait dans le champ de l'equipe suivie de la page precedente.
    // Gestion de ce cas ici :
    private void checkForUnobservedTrackedTeam() {
        if (!viewModel.hasTriggeredTeamFormObserver()){
            if (mustFillAllPlayerSlots(viewModel.getTrackedTeam()))
                fillAllPlayerSlots(); // Si l'equipe suivie remplie a deja des joueurs, autocompletion direct
            // Si equipe suivie remplie n'a pas de joueurs, soit l'input de l'equipe suivie a été effacé,
            // soit il faut que l'utilisateur remplisse lui-même les joueurs de l'equipe.
            else
                handleTeamCreation();
        }
    }

    // Si equipe suivie remplie n'a pas de joueurs, soit l'input de l'equipe suivie a été effacé,
    // soit il faut que l'utilisateur remplisse lui-même les joueurs de l'equipe.
    private void handleTeamCreation() {
        final TeamData trackedTeam = viewModel.getTrackedTeam();
        final List<AutoCompleteTextView> playerSlots = getAllPlayerSlots();
        // Si champ de l'equipe suivie effacé, on bloque les champs des joueurs
        if (missingTrackedTeam(trackedTeam))
            handleMissingTrackedTeam();
        // Si l'equipe suivie n'a pas de joueurs, on efface tous les joueurs et on laisse l'user compléter.
        else if (mustResetPlayerSlots(trackedTeam))
            resetPlayerSlots(playerSlots);
    }

    // Determine si l'equipe suivie n'a pas de joueurs et qu'on doit laisser l'user les compléter
    private boolean mustResetPlayerSlots(@NonNull TeamData trackedTeam) {
        return viewModel.getTeamByName(trackedTeam.getName()) == null
                || !trackedTeam.hasPlayers();
    }

    // Determine si on doit autocompléter les noms des joueurs
    private boolean mustFillAllPlayerSlots(@Nullable TeamData trackedTeam) {
        return !missingTrackedTeam(trackedTeam) && !mustResetPlayerSlots(trackedTeam);
    }

    // Determine si le champ de l'equipe suivie a été effacé par l'user
    private boolean missingTrackedTeam(@Nullable TeamData trackedTeam) {
        return trackedTeam == null
                || trackedTeam.getName() == null
                || trackedTeam.getName().isEmpty()
                || trackedTeam.getName().replaceAll(" ", "").equals("");
    }

    // Si le champ de l'equipe suivie a été effacé par l'user, on efface tout et on bloque tous les slots
    // des joueurs : on interdit à l'user de les compléter tant qu'il n'a pas renseigné d'equipe suivie
    private void handleMissingTrackedTeam() {
        toggleMissingTrackedTeamErrorMessage(true);
        List<AutoCompleteTextView> playerSlots = getAllPlayerSlots();
        playerSlots.forEach(slot -> {
            slot.setText("");
            slot.setError(null); // S'il y avait une erreur rouge sur un slot on l'enlève
            slot.setFocusableInTouchMode(false); // Read-only ==> champ bloqué.
        });
    }

    // Message d'erreur si l'user a effacé l'equipe suivie car il ne peut alors pas completer les joueurs.
    // Ici on fait apparaître ou disparaître ce message
    private void toggleMissingTrackedTeamErrorMessage(boolean isVisible) {
        final TextView errorMessage = requireView().findViewById(R.id.missing_team_error_message);
        int visibility = isVisible
                ? View.VISIBLE
                : View.INVISIBLE;
        errorMessage.setVisibility(visibility);
    }

    // Autocompletion des slots des joueurs.
    private void fillAllPlayerSlots() {
        // Disparition du message de l'equipe suivie manquante.
        toggleMissingTrackedTeamErrorMessage(false);
        viewModel.setWasTeamFormPreviouslyReset(false);
        final List<AutoCompleteTextView> playerSlots = getAllPlayerSlots();
        playerSlots.forEach(slot -> {
            fillSinglePlayerSlot(slot); // Autocompletion de chaque slot de joueur.
            slot.setFocusable(false); // Read-only : bloqué, on ne peut pas ecrire dedans.
            slot.setError(null); // On enleve l'erreur rouge s'il y en avait une
        });
    }

    // Autocompletion d'un slot de joueur
    private void fillSinglePlayerSlot(@NonNull AutoCompleteTextView slot) {
        final String position = PlayerUtil.getPlayerPositionBySlotId(slot.getId());
        final PlayerData playerSlotData = viewModel.getTrackedTeamPlayerByPosition(position);
        slot.setText(playerSlotData.getName()); // Nom du joueur NOT NULL dans la BD.
    }

    // On efface le contenu de tous les slots de joueur
    private void resetPlayerSlots(@NonNull List<AutoCompleteTextView> playerSlots) {
        // Disparition du message de l'equipe suivie manquante
        toggleMissingTrackedTeamErrorMessage(false);
        // Redonne la possibilite d'ecrire dans chaque slot de joueur pour que l'user le complète.
        playerSlots.forEach(slot -> slot.setFocusableInTouchMode(true));
        if(!viewModel.wasTeamFormPreviouslyReset()){
            cleanPlayerSlots(playerSlots); // Efface le contenu des slots et enleve leurs erreurs.
            if (mustInitPlayers(playerSlots))
                initPlayers(playerSlots); // Remplit les suggestions de joueur de chaque slot
            // On indique qu'on vient de reset l'ensemble de la page TeamFormFragment
            // comme ça si on revient à une autre page et qu'on re-revient ici sans rien faire,
            // on ne va pas encore une fois reset ce formulaire.
            viewModel.setWasTeamFormPreviouslyReset(true);
        }
    }

    // Efface le contenu des slots et enleve leurs erreurs.
    private void cleanPlayerSlots(@NonNull List<AutoCompleteTextView> playerSlots) {
        playerSlots.forEach(slot -> {
            slot.setText("");
            slot.setError(null);
        });
    }

    // Determine si on doit remplir les suggestions des slots de joueurs ou si c'est deja fait
    private boolean mustInitPlayers(@NonNull List<AutoCompleteTextView> playerSlots) {
        final List<ListAdapter> adapters = playerSlots.stream().map(AutoCompleteTextView::getAdapter).collect(Collectors.toList());
        return adapters.stream().anyMatch(Objects::isNull); // si au moins un adapter est null, on doit remplir
    }

    // Recupere tous les slots de joueurs
    private List<AutoCompleteTextView> getAllPlayerSlots() {
        final ViewGroup rootElement = requireView().findViewById(R.id.team_form);
        final String discriminatingTag = getResources().getString(R.string.team_form_player_slot_tag);
        return TagUtil.getTaggedElements(rootElement, discriminatingTag);
    }

    // Remplit les suggestions de chaque slot de player à partir des joueurs recuperes dans le view model
    // depuis l'initialisation de GameFormFragment.
    private void initPlayers(@NonNull List<AutoCompleteTextView> playerSlots) {
        playerSlots.forEach(slot -> {
            final String position = PlayerUtil.getPlayerPositionBySlotId(slot.getId());
            final List<PlayerData> filteredPlayers = viewModel.getFilteredPlayersByPosition(position);
            DataSourceUtil.fillAutocompletionSlotData(filteredPlayers, slot, requireContext());
        });
    }

    // Listener déclenché à chaque changement de texte de chaque slot de joueur.
    private void setAllPlayerSlotsTextChangeListeners() {
        final List<AutoCompleteTextView> slots = getAllPlayerSlots();
        slots.forEach(playerSlot -> {
            final TextWatcher slotWatcher = getNewSlotWatcher(playerSlot);
            playerSlot.addTextChangedListener(slotWatcher);
        });
    }

    // Construit un nouvel observateur de changement de texte + son handler de changement de texte
    private TextWatcher getNewSlotWatcher(@NonNull AutoCompleteTextView slot) {
        return new GameFormValidator(slot) {
            @Override
            public void validate(@NonNull TextView playerSlot, @Nullable String playerFullName) {
                validateSlot(playerSlot, playerFullName);
            }
        };
    }

    // Déclenché à chaque changement de texte pour chaque slot de joueur :
    // si le texte du slot est vide, erreur rouge de slot vide.
    // On met à jour les joueurs de l'equipe suivie dans le view model en consequence.
    private void validateSlot(@NonNull TextView playerSlot, @Nullable String playerFullName) {
        if (StringUtil.isNullOrEmptyOrWhiteSpaceInput(playerFullName))
            playerSlot.setError(getResources().getString(R.string.empty_slot_error));
        setViewModelPlayer(playerSlot.getId(), playerFullName);
    }

    // Set les Listeners Déclenchés à chaque click sur une suggestion de joueur.
    private void setAllAutocompletionSlotsItemClickListeners() {
        final List<AutoCompleteTextView> playerSlots = getAllPlayerSlots();
        playerSlots.forEach(this::setViewModelFieldFromClickedItem);
    }

    // Des qu'on clique sur une suggestion de joueur, on modifie le joueur de l'equipe suivie
    // correspondant dans le view model.
    private void setViewModelFieldFromClickedItem(@NonNull AutoCompleteTextView slot) {
        slot.setOnItemClickListener((parent, view, position, id) -> {
            final PlayerData playerItem = (PlayerData) slot.getAdapter().getItem(position);
            setViewModelFromPlayerData(playerItem);
        });
    }

    // Modifie un joueur de l'equipe suivie des que son champ est modifie par le contenu tapé par l'user.
    private void setViewModelPlayer(int slotId, @Nullable String playerFullName) {
        final String playerPosition = PlayerUtil.getPlayerPositionBySlotId(slotId);
        // Enleve le joueur ayant cette position du view model dans les joueurs de l'equipe suivie.
        removeAlreadyExistingPlayerFromTrackedTeam(playerPosition);
        if (!StringUtil.isNullOrEmptyOrWhiteSpaceInput(playerFullName)) {
            PlayerData foundPlayer = viewModel.getPlayerByFullName(playerFullName);
            if (foundPlayer == null)
                foundPlayer = new PlayerData(playerFullName, playerPosition);
            viewModel.getTrackedTeam().getPlayers().add(foundPlayer);
        }
    }

    private void setViewModelFromPlayerData(@NonNull PlayerData player) {
        removeAlreadyExistingPlayerFromTrackedTeam(player.getPosition());
        viewModel.getTrackedTeam().getPlayers().add(player);
    }

    private void removeAlreadyExistingPlayerFromTrackedTeam(@NonNull String playerPosition) {
        final PlayerData foundPlayer = viewModel.getTrackedTeamPlayerByPosition(playerPosition);
        if (foundPlayer != null)
            viewModel.getTrackedTeam().getPlayers().remove(foundPlayer);
    }
}
