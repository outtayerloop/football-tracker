package fr.android.foottracker.model.repositories.gameform;

import java.util.List;

import fr.android.foottracker.model.entities.data.ChampionshipData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.RefereeData;
import fr.android.foottracker.model.entities.data.TeamData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GameFormService {

    @GET("/gameform/championships")
    Call<List<ChampionshipData>> getAllChampionships();

    @GET("/gameform/referees")
    Call<List<RefereeData>> getAllReferees();

    @GET("/gameform/teams")
    Call<List<TeamData>> getAllTeams();

    @GET("/gameform/players")
    Call<List<PlayerData>> getAllPlayers();

    @POST("/gameform/games")
    Call<GameData> createGame(@Body GameData gameData);

}
