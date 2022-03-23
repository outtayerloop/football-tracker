package fr.android.foottracker.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;

public final class DeviceKeyboardUtil {

    private DeviceKeyboardUtil() {}

    // Permet de fermer le clavier du telephone lorsqu'on "clique" à côté.
    public static void setOnTouchListener(View view, FragmentActivity fragmentActivity) {
        view.setOnTouchListener((v, event) -> {
            view.performClick();
            hideDeviceKeyboard(fragmentActivity);
            // Pour permettre au scroll de consommer l'evenement onTouch egalement, sinon il ne marche pas.
            return false;
        });
    }

    private static void hideDeviceKeyboard(FragmentActivity fragmentActivity) {
        final View currentFocus = fragmentActivity.getCurrentFocus();
        if (currentFocus != null) {
            final InputMethodManager inputMethodManager = (InputMethodManager)fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
