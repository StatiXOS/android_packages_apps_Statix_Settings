package com.statix.android.settings.biometrics.face;

import android.content.Context;

public class Utils {
    public static float dpToPx(Context context, int i) {
        return i * (context.getResources().getDisplayMetrics().densityDpi / 160.0f);
    }
}
