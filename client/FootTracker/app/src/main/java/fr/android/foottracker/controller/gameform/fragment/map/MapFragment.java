package fr.android.foottracker.controller.gameform.fragment.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import fr.android.foottracker.R;
import fr.android.foottracker.app.FootTrackerAppContainer;
import fr.android.foottracker.app.FootTrackerApplication;
import fr.android.foottracker.model.view.GameFormViewModel;
import fr.android.foottracker.utils.DeviceKeyboardUtil;
import fr.android.foottracker.utils.StringUtil;
import fr.android.foottracker.utils.location.LocationPermissionsUtil;
import fr.android.foottracker.utils.location.LocationSettingsUtil;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        AddressDialogFragment.AddressDialogListener,
        CoordinatesDialogFragment.CoordinatesDialogListener {

    private FootTrackerAppContainer appContainer;
    private Geocoder geocoder;
    private GameFormViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContainer = ((FootTrackerApplication) requireActivity().getApplication()).getAppContainer();
        final Locale currentLocale = getResources().getConfiguration().getLocales().get(0); // La premiere culture est celle indiquee dans les preferences utilisateur.
        geocoder = new Geocoder(requireContext(), currentLocale);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_form, container, false);
        initSpeedDialView(view);
        DeviceKeyboardUtil.setOnTouchListener(view, requireActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(GameFormViewModel.class);
        initMap(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getMapView().onResume();
        initMapFromViewModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.getMapView().onPause();
        removeLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.getMapView().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        viewModel.getMapView().onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        viewModel.setMap(googleMap);
        if (viewModel.getCurrentLocation() != null)
            setMapFromCurrentGPSCoordinates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (hasLocationPermissionResultData(requestCode, permissions, grantResults))
            handleLocationPermissionResults(grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LocationSettingsUtil.LOCATION_REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK)
                initCurrentLocation();
        }
    }

    @Override
    public void onAddressSet(@Nullable String address) {
        if (!StringUtil.isNullOrEmptyOrWhiteSpaceInput(address)) {
            appContainer.getExecutorService().execute(() -> {
                try {
                    final List<Address> foundAddresses = geocoder.getFromLocationName(address, 1);
                    appContainer.getHandler().post(() -> handleFoundAddresses(foundAddresses));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onCoordinatesSet(double latitude, double longitude) {
        LatLng coordinates = new LatLng(latitude, longitude);
        setCurrentLocationFromCoordinates(coordinates.latitude, coordinates.longitude);
        setMapMarkerFromCoordinates(coordinates);
        setViewModelAddress(coordinates);
    }

    private void initMapFromViewModel(){
        Address currentAddress = viewModel.getAddress();
        if(currentAddress != null){
            LatLng coordinates = new LatLng(currentAddress.getLatitude(), currentAddress.getLongitude());
            setMapMarkerFromCoordinates(coordinates);
        }
    }

    private void initMap(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.setMapView(view.findViewById(R.id.map));
        viewModel.getMapView().onCreate(savedInstanceState); // Debut du cycle de vie de la map.
        viewModel.getMapView().onResume(); // Pour afficher la map immediatement.
        viewModel.getMapView().getMapAsync(this);
    }

    private void handleDynamicLocationRequest() {
        if (hasLocationFeature())
            handlePresentLocationFeature();
        else {
            Toast.makeText(requireContext(), "Missing location feature", Toast.LENGTH_SHORT).show();
            showMissingLocationFeatureSnackBar();
        }
    }

    private boolean hasLocationFeature() {
        return requireActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION);
    }

    private void handlePresentLocationFeature() {
        if (LocationPermissionsUtil.hasPermissions(requireContext()))
            ensureDeviceLocationCapabilities();
        else if (viewModel.canStillAskForLocationPermission())
            LocationPermissionsUtil.managePermissionsRequest(this);
        else
            showLocationPermissionsSnackBar();
    }

    private void handleLocationPermissionResults(@NonNull int[] grantResults) {
        if (hasAllLocationPermissionsGranted(grantResults))
            initCurrentLocation();
        else if (shouldShowLocationSnackBarFromPermissionResult()) {
            viewModel.setDoNotAskLocationPermissionAgainOptionPressed(true);
            showLocationPermissionsSnackBar();
        }
    }

    private void setMapFromCurrentGPSCoordinates() {
        final LatLng coordinates = new LatLng(viewModel.getCurrentLocation().getLatitude(), viewModel.getCurrentLocation().getLongitude());
        setViewModelAddress(coordinates);
        setMapMarkerFromCoordinates(coordinates);
    }

    private void setMapMarkerFromCoordinates(@NonNull LatLng coordinates) {
        final String markerTitle = getResources().getString(R.string.marker_title);
        if (viewModel.getLastPositionMarker() != null)
            viewModel.getLastPositionMarker().remove();
        final MarkerOptions markerOptions = new MarkerOptions().position(coordinates).title(markerTitle);
        viewModel.setLastPositionMarker(viewModel.getMap().addMarker(markerOptions));
        final float DEFAULT_ZOOM = 16f;
        viewModel.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, DEFAULT_ZOOM));
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        viewModel.getFusedLocationClient().getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                viewModel.setCurrentLocation(location);
                setMapFromCurrentGPSCoordinates();
            }
        });
        viewModel.getFusedLocationClient().requestLocationUpdates(viewModel.getLocationRequest(), viewModel.getLocationCallback(), Looper.getMainLooper());
    }

    private void initCurrentLocation() {
        if (!hasInitiatedLocation())
            initLocationMetadata();
        startLocationUpdates();
    }

    private void initLocationMetadata() {
        viewModel.setFusedLocationClient(LocationServices.getFusedLocationProviderClient(requireContext()));
        setLocationRequest();
        setLocationCallback();
    }

    private boolean hasInitiatedLocation() {
        return viewModel.getFusedLocationClient() != null
                && viewModel.getLocationRequest() != null
                && viewModel.getLocationCallback() != null;
    }

    private void removeLocationInitiation() {
        viewModel.setFusedLocationClient(null);
        viewModel.setLocationRequest(null);
        viewModel.setLocationCallback(null);
    }

    private void setLocationCallback() {
        viewModel.setLocationCallback(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                viewModel.setCurrentLocation(locationResult.getLastLocation());
                setMapFromCurrentGPSCoordinates();
            }
        });
    }

    private void setLocationRequest() {
        viewModel.setLocationRequest(LocationSettingsUtil.getLocationRequest());
    }

    private boolean hasAllLocationPermissionsGranted(@NonNull int[] grantResults) {
        return Arrays.stream(grantResults).allMatch(result -> result == PackageManager.PERMISSION_GRANTED);
    }

    private boolean shouldShowLocationSnackBarFromPermissionResult() {
        return viewModel.canStillAskForLocationPermission()
                && shouldNotShowLocationPermissionRationale();
    }

    private boolean hasLocationPermissionResultData(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        return requestCode == LocationPermissionsUtil.REQUEST_LOCATION_PERMISSION
                && !hasEmptyPermissionsAndResults(permissions, grantResults);
    }

    private boolean hasEmptyPermissionsAndResults(@NonNull String[] permissions, @NonNull int[] grantResults) {
        return permissions.length == 0 && grantResults.length == 0;
    }

    private boolean shouldNotShowLocationPermissionRationale() {
        return !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void showLocationPermissionsSnackBar() {
        final Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
        showLocationActionSnackBar(settingsIntent, R.string.location_permission_snackbar_title);
    }

    private void showLocationSourceSettingsSnackBar() {
        Intent locationSourceIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        final ComponentName resolvedActivity = locationSourceIntent.resolveActivity(requireActivity().getPackageManager());
        if (resolvedActivity == null)
            locationSourceIntent = new Intent(Settings.ACTION_SETTINGS);
        showLocationActionSnackBar(locationSourceIntent, R.string.location_settings_snackbar_title);
    }

    private void ensureDeviceLocationCapabilities() {
        final Runnable successCallback = this::initCurrentLocation;
        final Runnable failureCallback = this::showLocationSourceSettingsSnackBar;
        LocationSettingsUtil.ensureDeviceLocationCapabilities(requireActivity(), successCallback, failureCallback);
    }

    private void showLocationActionSnackBar(@NonNull Intent locationIntent, int snackBarTextResId) {
        final Snackbar informationSnackBar = Snackbar.make(requireView().findViewById(R.id.map_form), snackBarTextResId, Snackbar.LENGTH_INDEFINITE);
        informationSnackBar.setAction(R.string.settings, snackBar -> startActivity(locationIntent));
        informationSnackBar.show();
    }

    private void showMissingLocationFeatureSnackBar() {
        final Snackbar informationSnackBar = Snackbar.make(requireView().findViewById(R.id.map_form), R.string.missing_location_snackbar, Snackbar.LENGTH_SHORT);
        informationSnackBar.show();
    }

    private void setViewModelAddress(@NonNull LatLng coordinates) {
        appContainer.getExecutorService().execute(() -> {
            try {
                final List<Address> addresses = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
                appContainer.getHandler().post(() -> viewModel.setAddress(addresses.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initSpeedDialView(@NonNull View view) {
        final SpeedDialView mainFab = view.findViewById(R.id.speed_dial);
        final List<SpeedDialActionItem> fabList = getFloatingActionButtonList();
        mainFab.addAllActionItems(fabList);
        mainFab.setOnActionSelectedListener(this::callFabClickListenerById);
    }

    private List<SpeedDialActionItem> getFloatingActionButtonList() {
        final List<SpeedDialActionItem> fabList = new ArrayList<>();
        final SpeedDialActionItem dynamicLocationFab = getFab(R.id.dynamic_location_fab, R.drawable.baseline_my_location_24, R.color.white, R.color.design_default_color_primary);
        final SpeedDialActionItem staticAddressFab = getFab(R.id.static_address_fab, R.drawable.baseline_edit_24, R.color.design_default_color_primary, R.color.white);
        final SpeedDialActionItem staticCoordinatesFab = getFab(R.id.static_coordinates_fab, R.drawable.baseline_explore_24, R.color.design_default_color_primary, R.color.white);
        fabList.add(dynamicLocationFab);
        fabList.add(staticAddressFab);
        fabList.add(staticCoordinatesFab);
        return fabList;
    }

    private SpeedDialActionItem getFab(int fabId, int drawableId, int iconTintId, int backgroundColorId) {
        final Drawable icon = ResourcesCompat.getDrawable(getResources(), drawableId, requireActivity().getTheme());
        return new SpeedDialActionItem.Builder(fabId, icon)
                .setFabImageTintColor(getColorById(iconTintId))
                .setFabBackgroundColor(getColorById(backgroundColorId))
                .create();
    }

    private void removeLocationUpdates() {
        if (viewModel.getFusedLocationClient() != null && viewModel.getLocationCallback() != null)
            viewModel.getFusedLocationClient().removeLocationUpdates(viewModel.getLocationCallback());
    }

    private int getColorById(int colorId) {
        final Resources.Theme theme = requireActivity().getTheme();
        return getResources().getColor(colorId, theme);
    }

    private boolean callFabClickListenerById(@NonNull SpeedDialActionItem fab) {
        final int fabId = fab.getId();
        if (fabId == R.id.dynamic_location_fab)
            return onDynamicLocationFabClick();
        else if (fabId == R.id.static_address_fab)
            return onStaticAddressFabClick();
        else if (fabId == R.id.static_coordinates_fab)
            return onStaticCoordinatesFabClick();
        else
            throw new IllegalArgumentException("This ID does not belong to a known map fragment floating action button.");
    }

    private boolean onDynamicLocationFabClick() {
        handleDynamicLocationRequest();
        return afterChildFabClick();
    }

    private boolean onStaticAddressFabClick() {
        onStaticFabClick();
        final AddressDialogFragment addressDialog = new AddressDialogFragment(this);
        addressDialog.show(requireActivity().getSupportFragmentManager(), AddressDialogFragment.TAG);
        return afterChildFabClick();
    }

    private boolean onStaticCoordinatesFabClick() {
        onStaticFabClick();
        final CoordinatesDialogFragment coordinatesDialog = new CoordinatesDialogFragment(this);
        coordinatesDialog.show(requireActivity().getSupportFragmentManager(), CoordinatesDialogFragment.TAG);
        return afterChildFabClick();
    }

    private boolean afterChildFabClick() {
        final SpeedDialView mainFab = requireView().findViewById(R.id.speed_dial);
        mainFab.close();
        return true;
    }

    private void onStaticFabClick() {
        removeLocationUpdates();
        removeLocationInitiation();
    }

    private void handleFoundAddresses(@Nullable List<Address> foundAddresses) {
        if (noAddressFoundFromGeoCoder(foundAddresses))
            Toast.makeText(requireContext(), getResources().getString(R.string.unknown_address), Toast.LENGTH_SHORT).show();
        else {
            final Address foundAddress = foundAddresses.get(0);
            final LatLng coordinates = new LatLng(foundAddress.getLatitude(), foundAddress.getLongitude());
            setMapMarkerFromCoordinates(coordinates);
            setViewModelAddress(coordinates);
            setCurrentLocationFromCoordinates(coordinates.latitude, coordinates.longitude);
        }
    }

    private boolean noAddressFoundFromGeoCoder(@Nullable List<Address> foundAddresses) {
        return foundAddresses == null || foundAddresses.size() == 0;
    }

    private void setCurrentLocationFromCoordinates(double latitude, double longitude){
        Location currentLocation = new Location(LocationManager.GPS_PROVIDER);
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);
        viewModel.setCurrentLocation(currentLocation);
    }
}