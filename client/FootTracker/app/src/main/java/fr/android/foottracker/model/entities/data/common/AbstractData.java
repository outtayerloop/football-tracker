package fr.android.foottracker.model.entities.data.common;

import androidx.annotation.NonNull;

import fr.android.foottracker.utils.StringUtil;

// Classe mere de toutes les classes de donnees (XxxxData).
public abstract class AbstractData extends AbstractIdentifiedData{

    // Nom complet de l'entite.
    private final String name;

    public AbstractData(long id, @NonNull String name) {
        super(id);
        this.name = name;
    }

    public AbstractData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Retourne le nom complet avec chaque debut de mot en lettre capitale.
    @NonNull
    @Override
    public String toString() {
        return StringUtil.toCapitalized(name);
    }
}
