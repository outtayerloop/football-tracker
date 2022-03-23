package fr.android.foottracker.model.repositories.gameform;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import fr.android.foottracker.model.entities.data.ChampionshipData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.RefereeData;
import fr.android.foottracker.model.entities.data.TeamData;
import fr.android.foottracker.model.repositories.common.AbstractRemoteDataSource;
import retrofit2.Call;
import retrofit2.Retrofit;

public class GameFormRemoteDataSource extends AbstractRemoteDataSource {

    private final GameFormService gameFormService;

    public GameFormRemoteDataSource(@NonNull Retrofit jsonRetrofit) {
        super(jsonRetrofit);
        gameFormService = jsonRetrofit.create(GameFormService.class);
    }

    public List<ChampionshipData> getAllChampionships() {
        Call<List<ChampionshipData>> championshipCall = gameFormService.getAllChampionships();
        try {
            return championshipCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RefereeData> getAllReferees() {
        Call<List<RefereeData>> refereeCall = gameFormService.getAllReferees();
        try {
            return refereeCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TeamData> getAllTeams() {
        Call<List<TeamData>> teamCall = gameFormService.getAllTeams();
        try {
            return teamCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PlayerData> getAllPlayers() {
        Call<List<PlayerData>> playerCall = gameFormService.getAllPlayers();
        try {
            return playerCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GameData createGame(GameData gameData){
        Call<GameData> gameCreationCall = gameFormService.createGame(gameData);
        try{
            return gameCreationCall.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
