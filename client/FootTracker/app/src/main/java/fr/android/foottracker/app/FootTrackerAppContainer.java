package fr.android.foottracker.app;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FootTrackerAppContainer {

    private final ExecutorService executorService;
    private final GameFormAppContainer gameFormAppContainer;
    private final GameReviewAppContainer gameReviewAppContainer;
    private final GameUpdateAppContainer gameUpdateAppContainer;
    private final Handler handler;

    public FootTrackerAppContainer(@NonNull ExecutorService executorService, @NonNull Handler handler) {
        final Retrofit jsonRetrofit = getJsonRetrofit();
        gameFormAppContainer = new GameFormAppContainer(jsonRetrofit);
        gameUpdateAppContainer = new GameUpdateAppContainer(jsonRetrofit);
        gameReviewAppContainer = new GameReviewAppContainer(jsonRetrofit);
        this.executorService = executorService;
        this.handler = handler;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Handler getHandler() {
        return handler;
    }

    public GameFormAppContainer getGameFormAppContainer() {
        return gameFormAppContainer;
    }

    public GameUpdateAppContainer getGameUpdateAppContainer() {
        return gameUpdateAppContainer;
    }

    public GameReviewAppContainer getGameReviewAppContainer(){
        return gameReviewAppContainer;
    }

    private Retrofit getJsonRetrofit() {
        final String API_BASE_URL = "http://10.0.2.2:4000"; // Alias du localhost du PC pour l'AVD.
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getLoggingInterceptorClient())
                .build();
    }

    // Log des flux HTTP.
    private OkHttpClient getLoggingInterceptorClient() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

}
