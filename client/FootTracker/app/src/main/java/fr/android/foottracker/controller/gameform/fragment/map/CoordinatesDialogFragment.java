package fr.android.foottracker.controller.gameform.fragment.map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import fr.android.foottracker.R;
import fr.android.foottracker.utils.GameFormValidator;
import fr.android.foottracker.utils.StringUtil;
import fr.android.foottracker.utils.TagUtil;

// Dialog de choix de latitude/longitude pour le match.
public class CoordinatesDialogFragment extends DialogFragment {

    private final CoordinatesDialogFragment.CoordinatesDialogListener listener;

    private View dialogView;

    public static final String TAG = CoordinatesDialogFragment.class.getSimpleName();

    public CoordinatesDialogFragment(@NonNull CoordinatesDialogFragment.CoordinatesDialogListener listener) {
        this.listener = listener;
    }

    public interface CoordinatesDialogListener {
        void onCoordinatesSet(double latitude, double longitude);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_static_coordinates_form, null);
        setCoordinateWatchers(view);
        final AlertDialog.Builder builder = getDialogBuilder(view);
        dialogView = view;
        return builder.create();
    }

    private void setCoordinateWatchers(@NonNull View view) {
        final List<EditText> coordinateSlots = getAllCoordinateSlots(view);
        coordinateSlots.forEach(this::setCoordinateSlotWatcher);
    }

    private void setCoordinateSlotWatcher(@NonNull EditText coordinateSlot) {
        coordinateSlot.addTextChangedListener(new GameFormValidator(coordinateSlot) {
            @Override
            public void validate(@NonNull TextView coordinateSlot, @Nullable String coordinate) {
                validateAddressSlot(coordinateSlot, coordinate);
            }
        });
    }

    // Si on a tout effacé dans l'EditText de la latitude ou de la longitude, erreur rouge de champ vide
    private void validateAddressSlot(@NonNull TextView coordinateSlot, @Nullable String coordinate) {
        if (StringUtil.isNullOrEmptyOrWhiteSpaceInput(coordinate))
            coordinateSlot.setError(getResources().getString(R.string.empty_slot_error));
        else if (isInvalidLatitude(coordinateSlot))
            coordinateSlot.setError(getResources().getString(R.string.invalid_latitude));
        else if (isInvalidLongitude(coordinateSlot))
            coordinateSlot.setError(getResources().getString(R.string.invalid_longitude));
    }

    private boolean isInvalidLatitude(@NonNull TextView coordinateSlot) {
        final int coordinateSlotId = coordinateSlot.getId();
        if (coordinateSlotId != R.id.latitude)
            return false;
        return isInvalidLatitudeValue();
    }

    private boolean isInvalidLatitudeValue() {
        final double LATITUDE_MIN_VAL = -90;
        final double LATITUDE_MAX_VAL = 90;
        try {
            final double latitude = Double.parseDouble(getProvidedLatitude());
            return latitude < LATITUDE_MIN_VAL || latitude > LATITUDE_MAX_VAL;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean isInvalidLongitude(@NonNull TextView coordinateSlot) {
        final int coordinateSlotId = coordinateSlot.getId();
        if (coordinateSlotId != R.id.longitude)
            return false;
        return isInvalidLongitudeValue();
    }

    private boolean isInvalidLongitudeValue() {
        final double LONGITUDE_MIN_VAL = -180;
        final double LONGITUDE_MAX_VAL = 180;
        try {
            final double longitude = Double.parseDouble(getProvidedLongitude());
            return longitude < LONGITUDE_MIN_VAL || longitude > LONGITUDE_MAX_VAL;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private AlertDialog.Builder getDialogBuilder(@NonNull View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view)
                .setPositiveButton(R.string.ok, (dialog, id) -> onAddressSet())
                .setNegativeButton(R.string.cancel, (dialog, id) -> onCancel());
        return builder;
    }

    private void onAddressSet() {
        if (canCallListener()) {
            final double latitude = Double.parseDouble(getProvidedLatitude());
            final double longitude = Double.parseDouble(getProvidedLongitude());
            listener.onCoordinatesSet(latitude, longitude);
        } else
            onCancel();
    }

    private boolean canCallListener() {
        return !hasEmptyField()
                && !isInvalidLatitudeValue()
                && !isInvalidLongitudeValue();
    }

    private void onCancel() {
        CoordinatesDialogFragment.this.requireDialog().cancel();
    }

    private boolean hasEmptyField() {
        return StringUtil.isNullOrEmptyOrWhiteSpaceInput(getProvidedLatitude())
                || StringUtil.isNullOrEmptyOrWhiteSpaceInput(getProvidedLongitude());
    }

    private String getProvidedLatitude() {
        final EditText latitudeField = dialogView.findViewById(R.id.latitude);
        return latitudeField.getText().toString();
    }

    private String getProvidedLongitude() {
        final EditText longitudeField = dialogView.findViewById(R.id.longitude);
        return longitudeField.getText().toString();
    }

    private List<EditText> getAllCoordinateSlots(@NonNull View view) {
        final ViewGroup rootElement = view.findViewById(R.id.coordinates_form);
        final String discriminatingTag = getResources().getString(R.string.coordinate_edit_tag);
        return TagUtil.getTaggedElements(rootElement, discriminatingTag);
    }
}
