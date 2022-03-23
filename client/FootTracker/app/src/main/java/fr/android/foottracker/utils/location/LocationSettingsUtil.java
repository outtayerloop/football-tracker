package fr.android.foottracker.utils.location;

import android.app.Activity;
import android.content.IntentSender;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public final class LocationSettingsUtil {

    public static final int LOCATION_UPDATE_MILLISECONDS_INTERVAL = 5000;
    public static final int SMALLEST_DISPLACEMENT_METERS = 1;
    public static final int LOCATION_REQUEST_CHECK_SETTINGS = 2;

    private LocationSettingsUtil() {
    }

    public static void ensureDeviceLocationCapabilities(@NonNull Activity locationActivity, @NonNull Runnable successCallback, @NonNull Runnable failureCallback) {
        final Task<LocationSettingsResponse> locationSettingsResponseTask = getLocationSettingsResponseTask(locationActivity);
        handleLocationCapabilitiesFromSettingsTask(locationSettingsResponseTask, locationActivity, successCallback, failureCallback);
    }

    public static LocationRequest getLocationRequest() {
        final int LOCATION_UPDATE_FASTEST_MILLISECONDS_INTERVAL = 1000;
        return LocationRequest.create()
                .setInterval(LOCATION_UPDATE_MILLISECONDS_INTERVAL)
                .setFastestInterval(LOCATION_UPDATE_FASTEST_MILLISECONDS_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT_METERS);
    }

    private static Task<LocationSettingsResponse> getLocationSettingsResponseTask(@NonNull Activity locationActivity) {
        final LocationRequest locationRequest = getLocationRequest();
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().setAlwaysShow(false).addLocationRequest(locationRequest);
        final SettingsClient settingsClient = LocationServices.getSettingsClient(locationActivity);
        return settingsClient.checkLocationSettings(builder.build());
    }

    private static void handleLocationCapabilitiesFromSettingsTask(@NonNull Task<LocationSettingsResponse> locationSettingsResponseTask, @NonNull Activity locationActivity, @NonNull Runnable successCallback, @NonNull Runnable failureCallback) {
        handleEnabledLocationCapabilities(locationSettingsResponseTask, locationActivity, successCallback);
        handleDisabledLocationCapabilities(locationSettingsResponseTask, locationActivity, failureCallback);
    }

    private static void handleEnabledLocationCapabilities(@NonNull Task<LocationSettingsResponse> locationSettingsResponseTask, @NonNull Activity locationActivity, @NonNull Runnable successCallback) {
        locationSettingsResponseTask.addOnSuccessListener(locationActivity, locationSettingsResponse -> successCallback.run());
    }

    private static void handleDisabledLocationCapabilities(@NonNull Task<LocationSettingsResponse> locationSettingsResponseTask, @NonNull Activity locationActivity, @NonNull Runnable failureCallback) {
        locationSettingsResponseTask.addOnFailureListener(locationActivity, exception -> {
            if (exception instanceof ResolvableApiException)
                tryToResolveLocationSettings(exception, locationActivity, failureCallback);
        });
    }

    private static void tryToResolveLocationSettings(@NonNull Exception exception, @NonNull Activity locationActivity, @NonNull Runnable failureCallback) {
        try {
            final ResolvableApiException resolvable = (ResolvableApiException) exception;
            resolvable.startResolutionForResult(locationActivity, LOCATION_REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException sendEx) {
            failureCallback.run();
        }
    }
}