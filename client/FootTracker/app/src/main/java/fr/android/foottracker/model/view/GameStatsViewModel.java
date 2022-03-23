package fr.android.foottracker.model.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import fr.android.foottracker.model.entities.data.GameData;

public class GameStatsViewModel extends ViewModel {

    private final MutableLiveData<GameData> game;

    public GameStatsViewModel(){
        game = new MutableLiveData<>();
    }

    public GameData getGame(){
        return game.getValue();
    }

    public void setGame(@NonNull GameData game){
        this.game.setValue(game);
    }
}
