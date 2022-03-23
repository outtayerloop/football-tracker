package fr.android.foottracker.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Locale;

import fr.android.foottracker.R;

import static android.content.Context.MODE_PRIVATE;

public final class LocaleUtil {

    private final static String FOOT_TRACKER_SHARED_PREFERENCES_FILE = "fr.android.foottracker.shared.preferences";
    private final static String LOCALE_PREFERENCE_KEY = "LOCALE_PREFERENCE_KEY";
    private final static String CHANGED_LOCALE_PREFERENCE_KEY = "LOCALE_PREFERENCE_CHANGE_KEY";

    private LocaleUtil(){}

    public static void setLocaleSwitch(@NonNull Menu menu, @NonNull Activity activity) {
        final SharedPreferences preferences = activity.getApplication().getSharedPreferences(FOOT_TRACKER_SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        final SwitchCompat localeSwitch = getLocaleSwitch(menu, activity, preferences);
        if(mustInitLocale(localeSwitch, activity))
            recreateFromLocaleChange(preferences, activity);
        localeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            localeSwitch.setChecked(isChecked);
            persistUpdatedLocale(isChecked, preferences);
            updateLocale(isChecked, activity);
        });
    }

    public static void initLocale(@NonNull Activity activity){
        final SharedPreferences preferences = activity.getApplication().getSharedPreferences(FOOT_TRACKER_SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        if(mustRecreateFromLocaleChange(preferences))
            recreateFromLocaleChange(preferences, activity);
    }

    public static void updateLocaleChangeIndicator(@NonNull SharedPreferences preferences, boolean wasChanged){
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CHANGED_LOCALE_PREFERENCE_KEY, wasChanged);
        editor.apply();
    }

    private static boolean mustInitLocale(@NonNull SwitchCompat localeSwitch, @NonNull Activity activity){
        return localeSwitch.isChecked()
                && !activity.getApplication().getResources().getConfiguration().getLocales().get(0).equals(Locale.FRANCE);
    }

    private static void recreateFromLocaleChange(@NonNull SharedPreferences preferences, @NonNull Activity activity){
        updateLocaleChangeIndicator(preferences, true);
        final boolean isLocaleSwitchChecked = preferences.getBoolean(LOCALE_PREFERENCE_KEY, false);
        updateLocale(isLocaleSwitchChecked, activity);
    }

    private static boolean mustRecreateFromLocaleChange(@NonNull SharedPreferences preferences){
        return !preferences.getBoolean(CHANGED_LOCALE_PREFERENCE_KEY, false);
    }

    private static SwitchCompat getLocaleSwitch(@NonNull Menu menu, @NonNull Activity activity, @NonNull SharedPreferences preferences){
        final SwitchCompat localeSwitch = (SwitchCompat)menu.findItem(R.id.locale_toggle).getActionView();
        localeSwitch.setThumbTintList(ResourcesCompat.getColorStateList(activity.getResources(), R.color.switch_background_purple, activity.getTheme()));
        localeSwitch.setTrackTintList(ResourcesCompat.getColorStateList(activity.getResources(), R.color.light_purple, activity.getTheme()));
        localeSwitch.setChecked(preferences.getBoolean(LOCALE_PREFERENCE_KEY, false));
        return localeSwitch;
    }

    private static void persistUpdatedLocale(boolean isLocaleSwitchChecked, @NonNull SharedPreferences preferences){
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOCALE_PREFERENCE_KEY, isLocaleSwitchChecked);
        editor.putBoolean(CHANGED_LOCALE_PREFERENCE_KEY, true);
        editor.apply();
    }

    private static void updateLocale(boolean isLocaleSwitchChecked, @NonNull Activity activity){
        setNewLocale(isLocaleSwitchChecked, activity);
        activity.recreate();
    }

    private static void setNewLocale(boolean isLocaleSwitchChecked, @NonNull Activity activity){
        final Locale locale = isLocaleSwitchChecked ? Locale.FRANCE : Locale.ENGLISH;
        final Configuration configuration = activity.getApplication().getResources().getConfiguration();
        configuration.setLocale(locale);
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());
    }
}
