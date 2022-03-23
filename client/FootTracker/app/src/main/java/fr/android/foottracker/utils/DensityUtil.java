package fr.android.foottracker.utils;

import android.content.res.Resources;

public final class DensityUtil {

    private DensityUtil(){}

    /**
     * Convertit des dp (density independant pixels) en px (pixels).
     * La formule est issue de la documentation : https://developer.android.com/training/multiscreen/screendensities .
     * @param dp Mesure en dp qu'on veut convertir en px.
     * @return La mesure convertie en px.
     */
    public static int fromDpToPx(float dp, Resources resources){
        final float dpScale = resources.getDisplayMetrics().density;
        return (int) (dp * dpScale + 0.5f);
    }

    /**
     * Convertit des sp (scaleable pixels) en px (pixels).
     * La formule est issue de la documentation : https://developer.android.com/training/multiscreen/screendensities .
     * @param sp Mesure en sp qu'on veut convertir en px.
     * @return La mesure convertie en px.
     */
    public static int fromSpToPx(float sp, Resources resources){
        final float spScale = resources.getDisplayMetrics().scaledDensity;
        return (int) (sp * spScale + 0.5f);
    }
}
