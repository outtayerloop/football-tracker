package fr.android.foottracker.model.repositories.gamereview;

import java.util.List;

import fr.android.foottracker.model.entities.data.GameData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IGameReviewService {

    @GET("/gamereview/games")
    Call<List<GameData>> getAllGames();

}
