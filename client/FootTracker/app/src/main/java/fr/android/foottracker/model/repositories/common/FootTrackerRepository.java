package fr.android.foottracker.model.repositories.common;

public interface FootTrackerRepository {

    GenericLocalDataSource getLocalDataSource();

    AbstractRemoteDataSource getRemoteDataSource();
}
