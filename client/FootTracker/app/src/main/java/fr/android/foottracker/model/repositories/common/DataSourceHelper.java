package fr.android.foottracker.model.repositories.common;

import androidx.annotation.NonNull;

import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.model.repositories.gameform.GameFormLocalDataSource;
import fr.android.foottracker.model.repositories.gameform.GameFormRemoteDataSource;
import fr.android.foottracker.model.repositories.gamereview.GameReviewRemoteDataSource;

public final class DataSourceHelper {

    private DataSourceHelper(){}

    public static <TLocalDataSource extends GenericLocalDataSource> GenericLocalDataSource
        getLocalDataSource(@NonNull FootTrackerAppContainer appContainer, @NonNull Class<TLocalDataSource> localDataSourceClass){
            if(localDataSourceClass.equals(GameFormLocalDataSource.class))
                return appContainer.getGameFormAppContainer().getGameFormRepository().getLocalDataSource();
            else if(localDataSourceClass.equals(GenericLocalDataSource.class))
                return appContainer.getGameReviewAppContainer().getGameReviewRepository().getLocalDataSource();
            else
                throw new UnsupportedOperationException("An unknown local data source class was provided the the data source helper.");
    }

    public static <TRemoteDataSource extends AbstractRemoteDataSource> AbstractRemoteDataSource
        getRemoteDataSource(@NonNull FootTrackerAppContainer appContainer, @NonNull Class<TRemoteDataSource> remoteDataSourceClass){
            if(remoteDataSourceClass.equals(GameFormRemoteDataSource.class))
                return appContainer.getGameFormAppContainer().getGameFormRepository().getRemoteDataSource();
            else if(remoteDataSourceClass.equals(GameReviewRemoteDataSource.class))
                return appContainer.getGameReviewAppContainer().getGameReviewRepository().getRemoteDataSource();
            else
                throw new UnsupportedOperationException("An unknown remote data source class was provided the the data source helper.");
    }
}
