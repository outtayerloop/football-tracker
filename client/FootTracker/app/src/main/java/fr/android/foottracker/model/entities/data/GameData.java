package fr.android.foottracker.model.entities.data;

import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.utils.StringUtil;

public class GameData extends AbstractIdentifiedData {

    public static final String DEFAULT_OVERTIME = "00:00:00"; // 0 minute et 0 seconde.

    // Date et heure du match.
    private final String dateTime;

    // Adresse complete du match issue de la geolocalisation Google Maps.
    private final String address;

    // Determine si le match a ete valide ou non (apres modification terminee).
    private boolean isValidated;

    private int trackedTeamScore;

    private int opponentScore;

    // Nombre de sorties.
    private int outCount;

    private int penaltyCount;

    // Nombre de coup-francs.
    private int freeKickCount;

    private int cornerCount;

    private int occasionCount;

    private int faultCount;

    private int stopCount;

    // Nombre de hors-jeux.
    private int offsideCount;

    // Duree des prolongations du match.
    private String overTime;

    /***
     * Nombre total de possessions de l'equipe suivie.
     * Pour chaque joueur de l'equipe suivie, ce compteur est incremente de 1.
     */
    private int trackedTeamPossessionCount;

    /**
     * Nombre total de possessions de l'equipe adverse.
     * Pour chaque joueur de l'equipe adverse, ce compteur est incremente de 1.
     */
    private int opponentTeamPossessionCount;

    private final TeamData trackedTeam;

    private final TeamData opponentTeam;

    private final RefereeData referee;

    private final ChampionshipData championship;

    public GameData(long gameId, String dateTime, String address,
                    TeamData trackedTeam, TeamData opponentTeam,
                    RefereeData referee, ChampionshipData championship){

        super(gameId);
        this.dateTime = dateTime;
        this.address = address;
        this.trackedTeam = trackedTeam;
        this.opponentTeam = opponentTeam;
        this.referee = referee;
        this.championship = championship;
    }

    public GameData(String dateTime, String address,
                    TeamData trackedTeam, TeamData opponentTeam,
                    RefereeData referee, ChampionshipData championship){

        this.dateTime = dateTime;
        this.address = address;
        this.trackedTeam = trackedTeam;
        this.opponentTeam = opponentTeam;
        this.referee = referee;
        this.championship = championship;
    }

    public String getOvertimeToInsertInDatabase(){
        return StringUtil.isNullOrEmptyOrWhiteSpaceInput(overTime)
                ? GameData.DEFAULT_OVERTIME
                : overTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getAddress() {
        return address;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public int getTrackedTeamScore() {
        return trackedTeamScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public int getOutCount() {
        return outCount;
    }

    public int getPenaltyCount() {
        return penaltyCount;
    }

    public int getFreeKickCount() {
        return freeKickCount;
    }

    public int getCornerCount() {
        return cornerCount;
    }

    public int getOccasionCount() {
        return occasionCount;
    }

    public int getFaultCount() {
        return faultCount;
    }

    public int getStopCount() {
        return stopCount;
    }

    public int getOffsideCount() {
        return offsideCount;
    }

    public int getTrackedTeamPossessionCount() {
        return trackedTeamPossessionCount;
    }

    public String getOverTime() {
        return overTime;
    }

    public TeamData getTrackedTeam() {
        return trackedTeam;
    }

    public TeamData getOpponentTeam() {
        return opponentTeam;
    }

    public RefereeData getReferee() {
        return referee;
    }

    public ChampionshipData getChampionship() {
        return championship;
    }

    public void setValidationState(boolean isValidated){
        this.isValidated = isValidated;
    }

    public void setTrackedTeamScore(int trackedTeamScore) {
        this.trackedTeamScore = trackedTeamScore;
    }
    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public void setPenaltyCount(int penaltyCount) {
        this.penaltyCount = penaltyCount;
    }

    public void setFreeKickCount(int freeKickCount) {
        this.freeKickCount = freeKickCount;
    }

    public void setCornerCount(int cornerCount) {
        this.cornerCount = cornerCount;
    }

    public void setOccasionCount(int occasionCount) {
        this.occasionCount = occasionCount;
    }

    public void setFaultCount(int faultCount) {
        this.faultCount = faultCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    public void setOffsideCount(int offsideCount) {
        this.offsideCount = offsideCount;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public void setTrackedTeamPossessionCount(int trackedTeamPossessionCount) {
        this.trackedTeamPossessionCount = trackedTeamPossessionCount;
    }

    public int getOpponentTeamPossessionCount() {
        return opponentTeamPossessionCount;
    }

    public void setOpponentTeamPossessionCount(int opponentTeamPossessionCount) {
        this.opponentTeamPossessionCount = opponentTeamPossessionCount;
    }

    public int getTrackedTeamPossessionPercentage(){
        if(trackedTeamPossessionCount + opponentTeamPossessionCount == 0)
            return 0;
        final float percentage = 100 * ((float)trackedTeamPossessionCount) / ((float)trackedTeamPossessionCount + (float)opponentTeamPossessionCount);
        return Math.round(percentage);
    }

    public int getOpponentTeamPossessionPercentage(){
        if(trackedTeamPossessionCount + opponentTeamPossessionCount == 0)
            return 0;
        final float percentage = 100 * ((float)opponentTeamPossessionCount) / ((float)trackedTeamPossessionCount + (float)opponentTeamPossessionCount);
        return Math.round(percentage);
    }

    public int getTrackedTeamGameYellowCardCount(){
        return getTrackedTeam().getPlayers()
                .stream()
                .mapToInt(player -> player.getGameYellowCardCount(getId()))
                .sum();
    }

    public int getTrackedTeamGameRedCardCount(){
        return getTrackedTeam().getPlayers()
                .stream()
                .mapToInt(player -> player.getGameRedCardCount(getId()))
                .sum();
    }

    public boolean hasNoGoalsScoredByTrackedTeam(){
        return getTrackedTeamGoalCount() == 0;
    }

    public boolean hasNoCardsFromTrackedTeam(){
        return getTrackedTeamCardCount() == 0;
    }

    private int getTrackedTeamGoalCount(){
        return trackedTeam.getPlayers()
                .stream()
                .mapToInt(player -> player.getGameGoalCount(getId()))
                .sum();
    }

    private int getTrackedTeamCardCount(){
        return trackedTeam.getPlayers()
                .stream()
                .mapToInt(player -> player.getGameTotalCardCount(getId()))
                .sum();
    }
}
