package fr.android.foottracker.model.view;

import android.location.Address;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.android.foottracker.model.entities.data.common.AbstractData;
import fr.android.foottracker.model.entities.data.ChampionshipData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.RefereeData;
import fr.android.foottracker.model.entities.data.TeamData;
import fr.android.foottracker.utils.DateTimeUtil;
import fr.android.foottracker.utils.StringUtil;

public class GameFormViewModel extends ViewModel {

    private final MutableLiveData<ChampionshipData> championship;

    private final MutableLiveData<TeamData> trackedTeam;

    private final MutableLiveData<TeamData> opponentTeam;

    private final MutableLiveData<RefereeData> referee;

    private final MutableLiveData<Date> date;

    private final MutableLiveData<Date> time;

    private final MutableLiveData<Address> address;

    private final MutableLiveData<List<TeamData>> allTeams;

    private final MutableLiveData<List<PlayerData>> allPlayers;

    private final MutableLiveData<List<ChampionshipData>> allChampionships;

    private final MutableLiveData<List<RefereeData>> allReferees;

    private final MutableLiveData<Boolean> wasTeamFormPreviouslyReset; // Determine si les slots des joueurs ont deja ete reinitialises precedemment.

    private final MutableLiveData<Boolean> hasTriggeredTeamFormObserver; // Determine si l'observeur de l'equipe suivie a été déclenché au moins 1 fois.

    private final MutableLiveData<Boolean> doNotAskLocationPermissionAgainOptionPressed; // Determine s'il est encore possible de demander la permission de localisation à l'utilisateur.

    private final MutableLiveData<FusedLocationProviderClient> fusedLocationClient;

    private final MutableLiveData<LocationCallback> locationCallback;

    private final MutableLiveData<LocationRequest> locationRequest;

    private final MutableLiveData<Location> currentLocation;

    private final MutableLiveData<Marker> lastPositionMarker;

    private final MutableLiveData<MapView> mapView;

    private final MutableLiveData<GoogleMap> map;

    public GameFormViewModel() {
        this.championship = new MutableLiveData<>();
        this.trackedTeam = new MutableLiveData<>();
        this.opponentTeam = new MutableLiveData<>();
        this.referee = new MutableLiveData<>();
        this.date = new MutableLiveData<>();
        this.time = new MutableLiveData<>();
        this.allTeams = new MutableLiveData<>();
        this.allPlayers = new MutableLiveData<>();
        this.address = new MutableLiveData<>();
        this.allChampionships = new MutableLiveData<>();
        this.allReferees = new MutableLiveData<>();
        this.wasTeamFormPreviouslyReset = new MutableLiveData<>();
        this.hasTriggeredTeamFormObserver = new MutableLiveData<>();
        this.doNotAskLocationPermissionAgainOptionPressed = new MutableLiveData<>();
        this.fusedLocationClient = new MutableLiveData<>();
        this.locationCallback = new MutableLiveData<>();
        this.locationRequest = new MutableLiveData<>();
        this.currentLocation = new MutableLiveData<>();
        this.lastPositionMarker = new MutableLiveData<>();
        this.mapView = new MutableLiveData<>();
        this.map = new MutableLiveData<>();
    }

    public TeamData getTeamByName(@NonNull String teamName) {
        return getByName(teamName, allTeams.getValue());
    }

    public ChampionshipData getChampionshipByName(@NonNull String championshipName) {
        return getByName(championshipName, allChampionships.getValue());
    }

    public RefereeData getRefereeByFullName(@NonNull String refereeFullName) {
        return getByName(refereeFullName, allReferees.getValue());
    }

    public PlayerData getPlayerByFullName(@NonNull String fullName) {
        return getByName(fullName, allPlayers.getValue());
    }

    @Nullable
    public PlayerData getTrackedTeamPlayerByPosition(@NonNull String position) {
        TeamData trackedTeam = getTrackedTeam();
        return trackedTeam != null
                ? getPlayerByPosition(trackedTeam.getPlayers(), position)
                : null;
    }

    public List<PlayerData> getFilteredPlayersByPosition(@NonNull String position) {
        return getPlayerStreamByPosition(position, allPlayers.getValue()).collect(Collectors.toList());
    }

    public TeamData getTrackedTeam() {
        return trackedTeam.getValue();
    }

    public MutableLiveData<TeamData> getTrackedTeamMutableLiveData() {
        return trackedTeam;
    }

    public Date getDate() {
        return date.getValue();
    }

    public Date getTime() {
        return time.getValue();
    }

    public Address getAddress(){
        return address.getValue();
    }

    public boolean wasTeamFormPreviouslyReset() {
        return getMutableBooleanValue(wasTeamFormPreviouslyReset);
    }

    public boolean hasTriggeredTeamFormObserver() {
        return getMutableBooleanValue(hasTriggeredTeamFormObserver);
    }

    public boolean canStillAskForLocationPermission() {
        return !getMutableBooleanValue(doNotAskLocationPermissionAgainOptionPressed);
    }

    public void setChampionship(ChampionshipData championship) {
        this.championship.setValue(championship);
    }

    public void setTrackedTeam(TeamData team) {
        this.trackedTeam.setValue(team);
    }

    public void setOpponentTeam(TeamData team) {
        this.opponentTeam.setValue(team);
    }

    public void setReferee(RefereeData referee) {
        this.referee.setValue(referee);
    }

    public void setDate(Date date) {
        this.date.setValue(date);
    }

    public void setTime(Date time) {
        this.time.setValue(time);
    }

    public void setAllTeams(List<TeamData> allTeams) {
        this.allTeams.setValue(allTeams);
    }

    public void setAllPlayers(List<PlayerData> allPlayers) {
        this.allPlayers.setValue(allPlayers);
    }

    public void setAddress(Address address) {
        this.address.setValue(address);
    }

    public void setAllChampionships(List<ChampionshipData> allChampionships) {
        this.allChampionships.setValue(allChampionships);
    }

    public void setAllReferees(List<RefereeData> allReferees) {
        this.allReferees.setValue(allReferees);
    }

    public void setWasTeamFormPreviouslyReset(Boolean wasTeamFormPreviouslyReset) {
        this.wasTeamFormPreviouslyReset.setValue(wasTeamFormPreviouslyReset);
    }

    public void setHasTriggeredTeamFormObserver(Boolean hasTriggeredTeamFormObserver) {
        this.hasTriggeredTeamFormObserver.setValue(hasTriggeredTeamFormObserver);
    }

    public void setDoNotAskLocationPermissionAgainOptionPressed(Boolean doNotAskLocationPermissionAgainOptionPressed) {
        this.doNotAskLocationPermissionAgainOptionPressed.setValue(doNotAskLocationPermissionAgainOptionPressed);
    }

    public boolean hasMissingGameFormFields(){
        return championship.getValue() == null
                && trackedTeam.getValue() == null
                && opponentTeam.getValue() == null
                && referee.getValue() == null
                && date.getValue() == null
                && time.getValue() == null;
    }

    public boolean hasMissingTeamFormFields(){
        if(trackedTeam.getValue() == null)
            return true;
        final int TEAM_PLAYER_COUNT = 11;
        return trackedTeam.getValue().getPlayers() == null
                || trackedTeam.getValue().getPlayers().size() != TEAM_PLAYER_COUNT;
    }

    public boolean hasMissingMapFormFields(){
        return address.getValue() == null;
    }

    @Nullable
    public GameData getGame(){
        if(hasMissingFields()){
            return null;
        }
        final String dateTime = DateTimeUtil.toDateTime(getDate(), getTime());
        final String currentAddress = getFullAddress();
        final TeamData currentTrackedTeam = getTrackedTeam();
        final TeamData currentOpponentTeam = opponentTeam.getValue();
        final RefereeData currentReferee = referee.getValue();
        final ChampionshipData currentChampionship = championship.getValue();
        return new GameData(dateTime, currentAddress, currentTrackedTeam, currentOpponentTeam, currentReferee, currentChampionship);
    }

    public FusedLocationProviderClient getFusedLocationClient() {
        return fusedLocationClient.getValue();
    }

    public LocationCallback getLocationCallback() {
        return locationCallback.getValue();
    }

    public LocationRequest getLocationRequest() {
        return locationRequest.getValue();
    }

    public Location getCurrentLocation() {
        return currentLocation.getValue();
    }

    public Marker getLastPositionMarker() {
        return lastPositionMarker.getValue();
    }

    public MapView getMapView() {
        return mapView.getValue();
    }

    public GoogleMap getMap() {
        return map.getValue();
    }

    public void setFusedLocationClient(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient.setValue(fusedLocationClient);
    }

    public void setLocationCallback(LocationCallback locationCallback) {
        this.locationCallback.setValue(locationCallback);
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest.setValue(locationRequest);
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation.setValue(currentLocation);
    }

    public void setLastPositionMarker(Marker lastPositionMarker) {
        this.lastPositionMarker.setValue(lastPositionMarker);
    }

    public void setMapView(MapView mapView) {
        this.mapView.setValue(mapView);
    }

    public void setMap(GoogleMap map) {
        this.map.setValue(map);
    }

    @Nullable
    private <TData extends AbstractData> TData getByName(@NonNull String name, @Nullable List<TData> dataList) {
        if (dataList == null)
            return null;
        return dataList.stream()
                .filter(data -> StringUtil.areEquivalent(data.getName(), name))
                .findFirst()
                .orElse(null);
    }

    private boolean getMutableBooleanValue(MutableLiveData<Boolean> mutableBoolean) {
        return mutableBoolean.getValue() != null
                ? mutableBoolean.getValue()
                : false;
    }

    private PlayerData getPlayerByPosition(@Nullable List<PlayerData> players, @NonNull String position) {
        return players != null
                ? getPlayerStreamByPosition(position, players).findFirst().orElse(null)
                : null;
    }

    @NonNull
    private Stream<PlayerData> getPlayerStreamByPosition(@NonNull String position, List<PlayerData> players) {
        if (players == null)
            return new ArrayList<PlayerData>().stream();
        return players
                .stream()
                .filter(player -> player.hasPosition(position));
    }

    private void fillStringifiedAddress(Address currentAddress, StringBuilder fullAddress){
        boolean endOfAddress = false;
        for(int i = 0; !endOfAddress; ++i){
            String addressLine = currentAddress.getAddressLine(i);
            if(addressLine == null)
                endOfAddress = true;
            else
                fullAddress.append(addressLine).append(" ");
        }
    }

    @Nullable
    private String getFullAddress(){
        Address currentAddress = address.getValue();
        if(currentAddress == null)
            return null;
        final StringBuilder fullAddress = new StringBuilder();
        fillStringifiedAddress(currentAddress, fullAddress);
        return fullAddress.toString().trim();
    }

    private boolean hasMissingFields(){
        return hasMissingGameFormFields()
                || hasMissingTeamFormFields()
                || hasMissingMapFormFields();
    }
}
