package fr.android.foottracker.model.repositories.gamereview;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.repositories.common.AbstractRemoteDataSource;
import fr.android.foottracker.model.repositories.common.FootTrackerRepository;
import fr.android.foottracker.model.repositories.common.GenericLocalDataSource;

public class GameReviewRepository implements FootTrackerRepository {

    private final GenericLocalDataSource localDataSource;

    private final GameReviewRemoteDataSource remoteDataSource;

    public GameReviewRepository(@NonNull GenericLocalDataSource localDataSource, @NonNull GameReviewRemoteDataSource remoteDataSource){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public GenericLocalDataSource getLocalDataSource() {
        return localDataSource;
    }

    @Override
    public AbstractRemoteDataSource getRemoteDataSource(){
        return remoteDataSource;
    }
}
