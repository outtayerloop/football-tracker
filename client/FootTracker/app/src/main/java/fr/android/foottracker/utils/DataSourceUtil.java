package fr.android.foottracker.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.R;
import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.model.entities.data.common.AbstractData;
import fr.android.foottracker.model.repositories.common.AbstractRemoteDataSource;
import fr.android.foottracker.model.repositories.common.GenericLocalDataSource;
import fr.android.foottracker.model.repositories.common.DataSourceHelper;
import fr.android.foottracker.model.repositories.gameupdate.GameUpdateLocalDataSource;
import fr.android.foottracker.model.repositories.gameupdate.GameUpdateRemoteDataSource;
import fr.android.foottracker.model.repositories.gameform.GameFormLocalDataSource;

public final class DataSourceUtil {

    private DataSourceUtil() {
    }

    /**
     * Recupere une source de donnees locale qui herite de GenericLocaleDataSource ou qui est directement
     * de type GenericLocalDataSource à partir de la classe fournie.
     * @param appContainer Conteneur
     * @param localDataSourceClass Classe de la source de donnees locale qu'on veut recuperer
     * @param <TLocalDataSource> Type de la source de donnees locale qu'on veut recuperer
     * @return La source de donnes locale recuperee.
     */
    public static <TLocalDataSource extends GenericLocalDataSource> GenericLocalDataSource
        getLocalDataSource(@NonNull FootTrackerAppContainer appContainer, @NonNull Class<TLocalDataSource> localDataSourceClass) {
            return DataSourceHelper.getLocalDataSource(appContainer, localDataSourceClass);
    }

    /**
     * Recupere une source de donnees distante qui herite de AbstractRemoteDataSource à partir de la classe fournie.
     * @param appContainer Conteneur
     * @param remoteDataSourceClass Classe de la source de donnees distante qu'on veut recuperer
     * @param <TRemoteDataSource> Type de la source de donnees distante qu'on veut recuperer
     * @return La source de donnes distante recuperee.
     */
    public static <TRemoteDataSource extends AbstractRemoteDataSource> AbstractRemoteDataSource
        getRemoteDataSource(@NonNull FootTrackerAppContainer appContainer, @NonNull Class<TRemoteDataSource> remoteDataSourceClass) {
            return DataSourceHelper.getRemoteDataSource(appContainer, remoteDataSourceClass);
    }

    public static GameUpdateLocalDataSource getLocalDataSourceForUpdate(@NonNull FootTrackerAppContainer appContainer) {
        return appContainer.getGameUpdateAppContainer().getGameUpdateRepository().getLocalDataSource();
    }

    public static GameUpdateRemoteDataSource getRemoteDataSourceForUpdate(@NonNull FootTrackerAppContainer appContainer) {
        return appContainer.getGameUpdateAppContainer().getGameUpdateRepository().getRemoteDataSource();
    }

    /**
     * Remplit les suggestions d'un AutocompleteTextView à partir des donnees fournies
     * @param data Donnees de remplissage
     * @param slot AutocompleteTextView dont on veut remplir les suggestions
     * @param context Contexte qui sera donné à l'adapteur de l'AutocompleteTextView
     * @param <TData> Type de donnees fournies
     */
    public static <TData extends AbstractData> void fillAutocompletionSlotData(@NonNull List<TData> data, @NonNull AutoCompleteTextView slot, @NonNull Context context) {
        final ArrayAdapter<TData> adapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, data);
        slot.setAdapter(adapter);
    }

    /**
     * Si les donnees fournies doivent être sauvegardees localement, procede à cette sauvegarde
     * puis exécute la fonction fournie dans le UI thread si elle n'est pas null,
     * sinon exécute directement la fonction fournie dans le thread actuel si elle n'est pas null.
     * @param retrievedData Liste d'entites qu'on veut ou non sauvegarder localement
     * @param mustBeSavedLocally Détermine si la liste d'entites doit être sauvegardee localement ou non
     * @param appContainer Conteneur
     * @param db Base de donneees
     * @param postInsertionRunnable Fonction à executer apres l'insertion ou directement si elle n'est pas null
     * @param localDataSourceClass Classe de la source de donnees locale
     * @param <TData> Type des entites fournies
     * @param <TLocalDataSource> Type de la source de donnees locale
     */
    public static <TData extends AbstractData, TLocalDataSource extends GenericLocalDataSource> void
    handleDataToSaveLocally(@NonNull List<TData> retrievedData, boolean mustBeSavedLocally, @NonNull FootTrackerAppContainer appContainer, @NonNull SQLiteDatabase db, @Nullable Runnable postInsertionRunnable, @NonNull Class<TLocalDataSource> localDataSourceClass) {
        if (mustBeSavedLocally)
            saveDataLocally(retrievedData, appContainer, db, postInsertionRunnable, localDataSourceClass);
        else if (postInsertionRunnable != null)
            postInsertionRunnable.run();
    }

    /**
     * Sauvegarde localement la liste d'entites fournie puis execute le Runnable eventuel fourni.
     * Cela bloque le UI thread par nature, cette fonction est donc à appeler dans un thread.
     * @param retrievedData liste d'entites qu'on veut sauvegarder localement
     * @param appContainer Conteneur
     * @param db Base de donnees
     * @param postInsertionRunnable Fonction eventuelle à executer sur le UI thread une fois l'insertion finie.
     * @param localDataSourceClass Classe de la source de donnees locale
     * @param <TData> type des entites à sauvegarder
     * @param <TLocalDataSource> type de la source de donnees locale
     */
    private static <TData extends AbstractData, TLocalDataSource extends GenericLocalDataSource> void
        saveDataLocally(@NonNull List<TData> retrievedData, @NonNull FootTrackerAppContainer appContainer, @NonNull SQLiteDatabase db, @Nullable Runnable postInsertionRunnable, @NonNull Class<TLocalDataSource> localDataSourceClass) {
            final List<AbstractData> dataToSaveList = retrievedData.stream().map(data -> (AbstractData) data).collect(Collectors.toList());
            saveRangeLocally(dataToSaveList, db, appContainer, localDataSourceClass);
            if (postInsertionRunnable != null)
                appContainer.getHandler().post(postInsertionRunnable);
    }

    /**
     * Sauvegarde localement la liste d'entites fournie.
     * Cela bloque le UI thread par nature, cette fonction est donc à appeler dans un thread.
     * @param dataToSaveList Liste d'entites à sauvegarder localement.
     * @param db Base de donnees
     * @param appContainer Conteneur
     * @param localDataSourceClass classe de la source de donnees locale
     * @param <TLocalDataSource> type de la source de donnees locale
     */
    private static <TLocalDataSource extends GenericLocalDataSource> void saveRangeLocally(@NonNull List<AbstractData> dataToSaveList, @NonNull SQLiteDatabase db, @NonNull FootTrackerAppContainer appContainer, @NonNull Class<TLocalDataSource> localDataSourceClass) {
        final GameFormLocalDataSource localDataSource = (GameFormLocalDataSource) getLocalDataSource(appContainer, localDataSourceClass);
        dataToSaveList.forEach(data -> localDataSource.insert(db, data));
    }
}
