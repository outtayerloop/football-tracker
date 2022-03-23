package fr.android.foottracker.model.entities.dao.common;

import androidx.annotation.NonNull;

import java.util.List;

import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

// Interface implementee par AbstractDAO (et par consequent par tous les DAO).
public interface FootTrackerDao {

    // Recupere l'ensemble des donnees d'une table.
    @NonNull
    List<AbstractIdentifiedData> getAll();

    /**
     * Insere une nouvelle ligne dans la table.
     * @param data Donnee à insere.
     * @return L'identifiant de BD donne à la donnee creee.
     */
    long insert(@NonNull AbstractIdentifiedData data);

    long update(@NonNull AbstractIdentifiedData data);

}

