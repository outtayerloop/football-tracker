package fr.android.foottracker.controller.gameupdate.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import fr.android.foottracker.R;

public class LaunchOvertTimeDalog extends DialogFragment {
    AlertDialog dialog;

    public MutableLiveData<Boolean> getPositiveButtonClicked() {
        return positiveButtonClicked;
    }

    private MutableLiveData<Boolean> positiveButtonClicked;
    private MutableLiveData<Boolean> negativeButtonClicked;

    public LaunchOvertTimeDalog() {
        this.positiveButtonClicked = new MutableLiveData<>();
        positiveButtonClicked.setValue(false);

        this.negativeButtonClicked = new MutableLiveData<>();
        negativeButtonClicked.setValue(false);
    }

    /**
     * Instancie un fragment et initialize les paramètres de construction de ce fragment
     * @param title tritre du fragment
     * @return un fragment initialisé
     */
    public static LaunchOvertTimeDalog newInstance(String title) {
        LaunchOvertTimeDalog fragment = new LaunchOvertTimeDalog();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Créé un builder pour construire et initialiser le dialogue
     * @param savedInstanceState
     * @return le dialogue venant d'être créé
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getActivity().getResources().getString(R.string.confirmOverTime)).setTitle("LAUNCHING OVERTIME");
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                negativeButtonClicked.setValue(true);
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                positiveButtonClicked.setValue(true);
            }
        });
        dialog = builder.create();
        return dialog;
    }
}