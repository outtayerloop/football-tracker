package fr.android.foottracker.model.repositories.gameupdate;

import androidx.annotation.NonNull;

import java.io.IOException;

import fr.android.foottracker.model.entities.data.AttachmentData;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.GoalData;
import retrofit2.Call;
import retrofit2.Retrofit;

public class GameUpdateRemoteDataSource {
    private final GameUpdateService gameUpdateService;

    public GameUpdateRemoteDataSource(@NonNull Retrofit jsonRetrofit) {
        gameUpdateService = jsonRetrofit.create(GameUpdateService.class);
    }

    public GameData updateGame(GameData gameData){
        Call<GameData> gameUpdateCall = gameUpdateService.updateGame(gameData);
        try{
            return gameUpdateCall.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public AttachmentData insertAttachment(AttachmentData attachmentData){
        Call<AttachmentData> gameUpdateCall = gameUpdateService.insertAttachment(attachmentData);
        try{
            return gameUpdateCall.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public CardData insertCard(CardData cardData){
        Call<CardData> gameUpdateCall = gameUpdateService.insertCard(cardData);
        try{
            return gameUpdateCall.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public GoalData insertGoal(GoalData goalData){
        Call<GoalData> gameUpdateCall = gameUpdateService.insertGoal(goalData);
        try{
            return gameUpdateCall.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
