package fr.android.foottracker.model.repositories.gameupdate;

import fr.android.foottracker.model.entities.data.AttachmentData;
import fr.android.foottracker.model.entities.data.CardData;
import fr.android.foottracker.model.entities.data.GameData;
import fr.android.foottracker.model.entities.data.GoalData;
import fr.android.foottracker.model.entities.data.PlayerData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Méthodes qui serviront à faire des requêtes serveur
 */
public interface GameUpdateService {

    @POST("/gameupdate/games")
    Call<GameData> updateGame(@Body GameData gameData);

    @POST("/gameupdate/attachment")
    Call<AttachmentData> insertAttachment(@Body AttachmentData attachmentData);

    @POST("/gameupdate/card")
    Call<CardData> insertCard(@Body CardData attachmentData);

    @POST("/gameupdate/goal")
    Call<GoalData> insertGoal(@Body GoalData goalData);

    @Headers({"Accept: application/json"})
    @GET("/gameupdate/player/{name}")
    Call<PlayerData> getPlayerByName(@Path(value="name" , encoded=true) String playerName);

}
