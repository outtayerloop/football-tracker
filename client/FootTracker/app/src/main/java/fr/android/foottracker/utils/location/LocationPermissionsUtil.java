package fr.android.foottracker.utils.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import fr.android.foottracker.R;

public final class LocationPermissionsUtil {

    public static final int REQUEST_LOCATION_PERMISSION = 1;

    private LocationPermissionsUtil() { }

    public static boolean hasPermissions(@NonNull Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void managePermissionsRequest(@NonNull Fragment mapFragment) {
        final boolean shouldShowFineLocationRationale = mapFragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
        final boolean shouldShowCoarseLocationRationale = mapFragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (shouldShowFineLocationRationale || shouldShowCoarseLocationRationale)
            showRequestPermissionRationale(mapFragment);
        else
            requestLocationPermissions(mapFragment);
    }

    private static void showRequestPermissionRationale(@NonNull Fragment mapFragment) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mapFragment.requireContext());
        final String message = mapFragment.getResources().getString(R.string.location_permissions_rationale_text);
        alertDialogBuilder.setTitle(R.string.location_permission_dialog_title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> requestLocationPermissions(mapFragment));
        alertDialogBuilder.create().show();
    }

    private static void requestLocationPermissions(@NonNull Fragment mapFragment) {
        final String[] PERMISSIONS_TO_CHECK = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        mapFragment.requestPermissions(PERMISSIONS_TO_CHECK, REQUEST_LOCATION_PERMISSION);
    }
}