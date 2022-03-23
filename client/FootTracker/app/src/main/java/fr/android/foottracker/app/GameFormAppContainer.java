package fr.android.foottracker.app;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.repositories.gameform.GameFormLocalDataSource;
import fr.android.foottracker.model.repositories.gameform.GameFormRemoteDataSource;
import fr.android.foottracker.model.repositories.gameform.GameFormRepository;
import retrofit2.Retrofit;

public class GameFormAppContainer {

    private final GameFormRepository gameFormRepository;

    public GameFormAppContainer(@NonNull Retrofit jsonRetrofit) {
        final GameFormLocalDataSource localDataSource = new GameFormLocalDataSource();
        final GameFormRemoteDataSource remoteDataSource = new GameFormRemoteDataSource(jsonRetrofit);
        gameFormRepository = new GameFormRepository(localDataSource, remoteDataSource);
    }

    public GameFormRepository getGameFormRepository() {
        return gameFormRepository;
    }
}
