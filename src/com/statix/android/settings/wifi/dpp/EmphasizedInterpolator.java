package com.statix.android.settings.wifi.dpp;

import android.graphics.Path;
import android.view.animation.PathInterpolator;
/* compiled from: EmphasizedInterpolator.kt */
/* loaded from: classes3.dex */
public final class EmphasizedInterpolator {
    public static final int $stable;
    public static final EmphasizedInterpolator INSTANCE = new EmphasizedInterpolator();
    private static PathInterpolator interpolator;

    private EmphasizedInterpolator() {
    }

    static {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(0.05f, 0.0f, 0.133333f, 0.06f, 0.166666f, 0.4f);
        path.cubicTo(0.208333f, 0.82f, 0.25f, 1.0f, 1.0f, 1.0f);
        interpolator = new PathInterpolator(path);
        $stable = 8;
    }

    public final float getInterpolation(float f) {
        return interpolator.getInterpolation(f);
    }
}
