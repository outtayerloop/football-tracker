package fr.android.foottracker.model.entities.dao.common;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import fr.android.foottracker.model.entities.dao.AttachmentDao;
import fr.android.foottracker.model.entities.dao.CardDao;
import fr.android.foottracker.model.entities.dao.ChampionshipDao;
import fr.android.foottracker.model.entities.dao.GameDao;
import fr.android.foottracker.model.entities.dao.GoalDao;
import fr.android.foottracker.model.entities.dao.PlayerDao;
import fr.android.foottracker.model.entities.dao.RefereeDao;
import fr.android.foottracker.model.entities.dao.TeamDao;
import fr.android.foottracker.model.entities.data.AttachmentData;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.GoalData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;
import fr.android.foottracker.model.entities.data.ChampionshipData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.PlayerData;
import fr.android.foottracker.model.entities.data.RefereeData;
import fr.android.foottracker.model.entities.data.TeamData;

// Factory creant un DAO en fonction de la classe de la donnee de sortie fournie.
public final class DaoFactory {

    private DaoFactory() {
    }

    public static <TData extends AbstractIdentifiedData> AbstractDao create(@NonNull SQLiteDatabase db, @NonNull Class<TData> dataClass) {
        if (dataClass.equals(ChampionshipData.class))
            return new ChampionshipDao(db);
        else if (dataClass.equals(RefereeData.class))
            return new RefereeDao(db);
        else if (dataClass.equals(TeamData.class))
            return new TeamDao(db);
        else if (dataClass.equals(PlayerData.class))
            return new PlayerDao(db);
        else if(dataClass.equals(GameData.class))
            return new GameDao(db);
        else if(dataClass.equals(AttachmentData.class))
            return new AttachmentDao(db);
        else if(dataClass.equals(GoalData.class))
            return new GoalDao(db);
        else if(dataClass.equals(CardData.class))
            return new CardDao(db);
        else
            throw new UnsupportedOperationException("An unknown data class was provided to the DAO factory.");
    }
}
