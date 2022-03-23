package fr.android.foottracker.controller;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = TimePickerFragment.class.getSimpleName();

    public interface OnTimeSetListener {
        void onTimeSet(int hourOfDay, int minute);
    }

    private OnTimeSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final int hourOfDay = calendar.get(Calendar.HOUR);
        final int minute = calendar.get(Calendar.MINUTE);
        final boolean is24HourFormat = DateFormat.is24HourFormat(requireContext());
        return new TimePickerDialog(requireContext(), this, hourOfDay, minute, is24HourFormat);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.onTimeSet(hourOfDay, minute);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnTimeSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling context must be an activity and implement OnTimeSetListener");
        }
    }
}
