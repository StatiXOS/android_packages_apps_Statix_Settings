package com.statix.android.settings.wifi.dpp;

import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: DampedString.kt */
/* loaded from: classes3.dex */
public final class DampedString {
    public static final Companion Companion = new Companion(null);
    private final float dampedNaturalFrequency;
    private final float dampingRatio;
    private final float stiffness;
    private final float undampedNaturalFrequency;

    public DampedString(int i, float f) {
        double d;
        float pow;
        this.dampingRatio = f;
        this.stiffness = ((float) Math.pow(6.2831855f / i, 2)) * 1.0f;
        float sqrt = (float) Math.sqrt(pow / 1.0f);
        this.undampedNaturalFrequency = sqrt;
        this.dampedNaturalFrequency = sqrt * ((float) Math.sqrt(Math.abs(1 - ((float) Math.pow(f, d)))));
    }

    public final float calculatePosition(int i) {
        float f;
        float f2 = this.undampedNaturalFrequency * this.dampingRatio;
        float f3 = this.dampedNaturalFrequency;
        float f4 = ((f2 * (-1.0f)) + 0.0f) / f3;
        double d = f3 * i;
        return (((float) Math.exp((-f2) * f)) * ((f4 * ((float) Math.sin(d))) + (((float) Math.cos(d)) * (-1.0f)))) + 1.0f;
    }

    /* compiled from: DampedString.kt */
    /* loaded from: classes3.dex */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
