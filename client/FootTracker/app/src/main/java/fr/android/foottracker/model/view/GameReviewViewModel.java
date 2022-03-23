package fr.android.foottracker.model.view;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

public class GameReviewViewModel extends ViewModel {

    private final MutableLiveData<List<GameData>> allGames;

    public GameReviewViewModel(){
        this.allGames = new MutableLiveData<>();
    }

    public List<GameData> getAllGames(){
        return allGames.getValue();
    }

    public void setAllGames(List<GameData> allGames){
        this.allGames.setValue(allGames);
    }

    @Nullable
    public List<Long> getAllGamesIdList(){
        return allGames.getValue() != null
                ? allGames.getValue().stream()
                    .map(AbstractIdentifiedData::getId)
                    .collect(Collectors.toList())
                : null;
    }
}
