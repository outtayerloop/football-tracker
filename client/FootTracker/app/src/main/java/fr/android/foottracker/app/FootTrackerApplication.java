package fr.android.foottracker.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FootTrackerApplication extends Application {

    private final FootTrackerAppContainer appContainer;

    public FootTrackerApplication() {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        appContainer = new FootTrackerAppContainer(executorService, handler);
    }

    public FootTrackerAppContainer getAppContainer() {
        return appContainer;
    }
}
