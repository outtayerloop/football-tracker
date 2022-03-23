package fr.android.foottracker.model.repositories.gamereview;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.repositories.common.AbstractRemoteDataSource;
import retrofit2.Call;
import retrofit2.Retrofit;

public class GameReviewRemoteDataSource extends AbstractRemoteDataSource {

    private final IGameReviewService gameReviewService;

    public GameReviewRemoteDataSource(@NonNull Retrofit jsonRetrofit) {
        super(jsonRetrofit);
        gameReviewService = jsonRetrofit.create(IGameReviewService.class);
    }

    public List<GameData> getAllGames(){
        Call<List<GameData>> gamesCall = gameReviewService.getAllGames();
        try{
            return gamesCall.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
