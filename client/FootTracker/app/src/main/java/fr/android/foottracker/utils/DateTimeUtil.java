package fr.android.foottracker.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import fr.android.foottracker.R;
import fr.android.foottracker.model.entities.data.GameData;

public final class DateTimeUtil {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FULL_TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private DateTimeUtil() { }

    public static String getFormattedDateTimeComponent(int dateComponent) {
        return dateComponent >= 10 // On veut toujours avoir 2 chiffre pour un jour ou un mois.
                ? "" + dateComponent
                : "0" + dateComponent;
    }

    public static Date toDate(@NonNull String dateToConvert) {
        try {
            return DATE_FORMATTER.parse(dateToConvert);
        } catch (ParseException e) {
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static Date toTime(@NonNull String timeToConvert) {
        try {
            return TIME_FORMATTER.parse(timeToConvert);
        } catch (ParseException e) {
            return null;
        }
    }

    /***
     * Retourne la duree totale du match à partir des prolongations fournies (format mm:ss).
     * @param overTime Prolongations (format mm:ss).
     * @return La duree totale du match à partir des prolongations fournies (format mm:ss).
     */
    public static String getTimeLapseDetail(String overTime, Resources resources){
        final String noOvertimeLabel = resources.getString(R.string.no_overtime_label);
        final String overTimeLabel = resources.getString(R.string.overtime_label);
        return overTime.equals(GameData.DEFAULT_OVERTIME)
                ? "01:30:00 (" + noOvertimeLabel + ")"
                : getTimeLapseFromOverTime(overTime) + " (" + overTimeLabel + ": " + overTime + ")";
    }

    public static String dateToString(@Nullable Date date) {
        return date != null
                ? DATE_FORMATTER.format(date)
                : "";
    }

    public static String timeToString(@Nullable Date time) {
        return time != null
                ? TIME_FORMATTER.format(time)
                : "";
    }

    public static String toDateTime(@NonNull Date date, @NonNull Date time){
        String convertedDate = dateToString(date);
        String convertedTime = timeToString(time);
        return convertedDate + " " + convertedTime;
    }

    private static String getTimeLapseFromOverTime(@NonNull String overTime){
        final Calendar overTimeCalendar = Calendar.getInstance();
        overTimeCalendar.setTime(Objects.requireNonNull(toOverTime(overTime)));
        final Calendar timeLapseCalendar = Calendar.getInstance();
        timeLapseCalendar.set(Calendar.HOUR, 1); // Par defaut, match dure 1h30.
        timeLapseCalendar.set(Calendar.MINUTE, 30); // Par defaut, match dure 1h30.
        timeLapseCalendar.set(Calendar.SECOND, 0); // Par defaut, match dure 1h30.
        timeLapseCalendar.set(Calendar.AM_PM, Calendar.AM); // Pour avoir 1h et pas 13h (AM et pas PM).
        timeLapseCalendar.add(Calendar.HOUR, overTimeCalendar.get(Calendar.HOUR));
        timeLapseCalendar.add(Calendar.MINUTE, overTimeCalendar.get(Calendar.MINUTE));
        timeLapseCalendar.add(Calendar.SECOND, overTimeCalendar.get(Calendar.SECOND));
        return timeLapseToString(timeLapseCalendar.getTime());
    }

    /***
     * Convertit des prolongations sous forme de chaîne de caractères (format mm:ss) en Date.
     * @param overTimeToConvert Prolongations sous forme de chaîne de caractères (format mm:ss).
     * @return Les prolongations sous forme de chaîne de caractères (format mm:ss) converties en Date.
     */
    private static Date toOverTime(@NonNull String overTimeToConvert){
        try {
            return FULL_TIME_FORMATTER.parse(overTimeToConvert);
        } catch (ParseException e) {
            return null;
        }
    }

    private static String timeLapseToString(@NonNull Date timeLapse){
        return FULL_TIME_FORMATTER.format(timeLapse);
    }
}
