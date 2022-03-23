package fr.android.foottracker.controller.gameupdate.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import fr.android.foottracker.R;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class InputControlDialog extends DialogFragment {

    AlertDialog dialog;


    public InputControlDialog() {
        // Empty constructor required for DialogFragment
    }

    public static InputControlDialog newInstance(String title) {
        InputControlDialog frag = new InputControlDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        dialog = builder.create();
        Log.d("PRINT FROM FRAGMENT","dialog created! ");
        if(dialog==null)
            Log.d("PRINT FROM FRAGMENT","dialog created! ");


        return dialog;
    }
}