package fr.android.foottracker.model.repositories.gameform;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.repositories.common.FootTrackerRepository;

public class GameFormRepository implements FootTrackerRepository {

    private final GameFormLocalDataSource localDataSource;
    private final GameFormRemoteDataSource remoteDataSource;

    public GameFormRepository(@NonNull GameFormLocalDataSource localDataSource, @NonNull GameFormRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public GameFormLocalDataSource getLocalDataSource() {
        return localDataSource;
    }

    @Override
    public GameFormRemoteDataSource getRemoteDataSource() {
        return remoteDataSource;
    }
}
