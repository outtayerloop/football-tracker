package fr.android.foottracker.controller.gameform.fragment.map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import fr.android.foottracker.R;
import fr.android.foottracker.utils.GameFormValidator;
import fr.android.foottracker.utils.StringUtil;

// Dialog de choix d'adresse/lieu pour le match.
public final class AddressDialogFragment extends DialogFragment {

    private final AddressDialogListener listener;

    private View dialogView;

    public static final String TAG = AddressDialogFragment.class.getSimpleName();

    public AddressDialogFragment(@NonNull AddressDialogListener listener) {
        this.listener = listener;
    }

    public interface AddressDialogListener {
        void onAddressSet(@Nullable String address);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_static_address_form, null);
        setAddressFieldWatcher(view);
        final AlertDialog.Builder builder = getDialogBuilder(view);
        dialogView = view;
        return builder.create();
    }

    private void setAddressFieldWatcher(@NonNull View view) {
        final EditText addressField = view.findViewById(R.id.address);
        addressField.addTextChangedListener(new GameFormValidator(addressField) {
            @Override
            public void validate(@NonNull TextView addressSlot, @Nullable String address) {
                validateAddressSlot(addressSlot, address);
            }
        });
    }

    // Si on a tout effacé dans l'EditText, erreur rouge de champ vide
    private void validateAddressSlot(@NonNull TextView addressSlot, @Nullable String address) {
        if (StringUtil.isNullOrEmptyOrWhiteSpaceInput(address))
            addressSlot.setError(getResources().getString(R.string.empty_slot_error));
    }

    private AlertDialog.Builder getDialogBuilder(@NonNull View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view)
                .setPositiveButton(R.string.ok, (dialog, id) -> onAddressSet())
                .setNegativeButton(R.string.cancel, (dialog, id) -> onCancel());
        return builder;
    }

    // Des qu'on clique sur OK
    private void onAddressSet() {
        final EditText addressField = dialogView.findViewById(R.id.address);
        final String address = addressField.getText().toString();
        listener.onAddressSet(address);
    }

    // Des qu'on clique sur "Annuler"
    private void onCancel() {
        AddressDialogFragment.this.requireDialog().cancel();
    }
}
