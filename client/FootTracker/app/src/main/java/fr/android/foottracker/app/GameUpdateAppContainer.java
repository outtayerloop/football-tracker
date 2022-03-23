package fr.android.foottracker.app;

import androidx.annotation.NonNull;
import fr.android.foottracker.model.repositories.gameupdate.GameUpdateLocalDataSource;
import fr.android.foottracker.model.repositories.gameupdate.GameUpdateRemoteDataSource;
import fr.android.foottracker.model.repositories.gameupdate.GameUpdateRepository;
import retrofit2.Retrofit;

public class GameUpdateAppContainer {
    private final GameUpdateRepository gameUpdateRepository;

    public GameUpdateAppContainer(@NonNull Retrofit jsonRetrofit) {
        final GameUpdateLocalDataSource localDataSource = new GameUpdateLocalDataSource();
        final GameUpdateRemoteDataSource remoteDataSource = new GameUpdateRemoteDataSource(jsonRetrofit);
        gameUpdateRepository = new GameUpdateRepository(localDataSource, remoteDataSource);
    }

    public GameUpdateRepository getGameUpdateRepository() {
        return gameUpdateRepository;
    }
}
