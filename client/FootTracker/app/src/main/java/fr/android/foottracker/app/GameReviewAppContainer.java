package fr.android.foottracker.app;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.repositories.common.GenericLocalDataSource;
import fr.android.foottracker.model.repositories.gamereview.GameReviewRemoteDataSource;
import fr.android.foottracker.model.repositories.gamereview.GameReviewRepository;
import retrofit2.Retrofit;

public class GameReviewAppContainer {

    private final GameReviewRepository gameReviewRepository;

    public GameReviewAppContainer(@NonNull Retrofit jsonRetrofit){
        final GenericLocalDataSource localDataSource = new GenericLocalDataSource();
        final GameReviewRemoteDataSource remoteDataSource = new GameReviewRemoteDataSource(jsonRetrofit);
        gameReviewRepository = new GameReviewRepository(localDataSource, remoteDataSource);
    }

    public GameReviewRepository getGameReviewRepository(){
        return gameReviewRepository;
    }

}
