package fr.android.foottracker.utils;

import androidx.appcompat.app.ActionBar;

import fr.android.foottracker.R;

public final class ActionBarUtil {

    private ActionBarUtil(){}

    public static void setToolBar(ActionBar actionBar){
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
            actionBar.setHomeActionContentDescription(R.string.home_btn_description);
        }
    }

}
