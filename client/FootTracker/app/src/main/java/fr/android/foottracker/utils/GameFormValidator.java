package fr.android.foottracker.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class GameFormValidator implements TextWatcher {

    private final TextView slot;

    public GameFormValidator(@NonNull TextView slot) {
        this.slot = slot;
    }

    public abstract void validate(@NonNull TextView slot, @Nullable String text);

    @Override
    final public void afterTextChanged(Editable s) {
        final String text = slot.getText().toString();
        validate(slot, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
