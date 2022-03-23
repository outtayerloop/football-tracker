package fr.android.foottracker.model.repositories.gameupdate;

import androidx.annotation.NonNull;


public class GameUpdateRepository {
    private final GameUpdateLocalDataSource localDataSource;
    private final GameUpdateRemoteDataSource remoteDataSource;

    public GameUpdateRepository(@NonNull GameUpdateLocalDataSource localDataSource, @NonNull GameUpdateRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public GameUpdateLocalDataSource getLocalDataSource() {
        return localDataSource;
    }

    public GameUpdateRemoteDataSource getRemoteDataSource() {
        return remoteDataSource;
    }
}
