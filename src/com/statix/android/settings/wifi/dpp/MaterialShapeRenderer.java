package com.statix.android.settings.wifi.dpp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.VectorDrawable;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.collections.MapsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MaterialShapeRenderer.kt */
/* loaded from: classes3.dex */
public final class MaterialShapeRenderer {
    private EntryAnimationStyle animationStyle;
    private final RectF destRect;
    private long duration;
    private int initialRotation;
    private Paint paint;
    private float skipStartProgress;
    private final VectorDrawable srcImgSvg;
    private long startDelay;
    public static final Companion Companion = new Companion(null);
    public static final int $stable = 8;
    private static Map springScaleCache = new LinkedHashMap();
    private static DampedString dampedString = new DampedString(60, 0.63f);

    /* compiled from: MaterialShapeRenderer.kt */
    /* loaded from: classes3.dex */
    public abstract /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[EntryAnimationStyle.values().length];
            try {
                iArr[EntryAnimationStyle.None.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[EntryAnimationStyle.ZoomIn.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[EntryAnimationStyle.SpringZoomIn.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[EntryAnimationStyle.RotateEmphasizedZoomIn.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[EntryAnimationStyle.EmphasizedZoomIn.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public MaterialShapeRenderer(VectorDrawable srcImgSvg, RectF destRect, Paint paint) {
        Intrinsics.checkNotNullParameter(srcImgSvg, "srcImgSvg");
        Intrinsics.checkNotNullParameter(destRect, "destRect");
        Intrinsics.checkNotNullParameter(paint, "paint");
        this.srcImgSvg = srcImgSvg;
        this.destRect = destRect;
        this.paint = paint;
        this.animationStyle = EntryAnimationStyle.None;
    }

    public final void setPaint(Paint paint) {
        Intrinsics.checkNotNullParameter(paint, "<set-?>");
        this.paint = paint;
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* compiled from: MaterialShapeRenderer.kt */
    /* loaded from: classes3.dex */
    public final class EntryAnimationStyle {
        private static final /* synthetic */ EnumEntries $ENTRIES;
        private static final /* synthetic */ EntryAnimationStyle[] $VALUES;
        public static final EntryAnimationStyle None = new EntryAnimationStyle("None", 0);
        public static final EntryAnimationStyle ZoomIn = new EntryAnimationStyle("ZoomIn", 1);
        public static final EntryAnimationStyle EmphasizedZoomIn = new EntryAnimationStyle("EmphasizedZoomIn", 2);
        public static final EntryAnimationStyle SpringZoomIn = new EntryAnimationStyle("SpringZoomIn", 3);
        public static final EntryAnimationStyle RotateEmphasizedZoomIn = new EntryAnimationStyle("RotateEmphasizedZoomIn", 4);

        private static final /* synthetic */ EntryAnimationStyle[] $values() {
            return new EntryAnimationStyle[]{None, ZoomIn, EmphasizedZoomIn, SpringZoomIn, RotateEmphasizedZoomIn};
        }

        public static EntryAnimationStyle valueOf(String str) {
            return (EntryAnimationStyle) Enum.valueOf(EntryAnimationStyle.class, str);
        }

        public static EntryAnimationStyle[] values() {
            return (EntryAnimationStyle[]) $VALUES.clone();
        }

        private EntryAnimationStyle(String str, int i) {
        }

        static {
            EntryAnimationStyle[] $values = $values();
            $VALUES = $values;
            $ENTRIES = EnumEntriesKt.enumEntries($values);
        }
    }

    public final void setStartDelay(long j) {
        this.startDelay = j;
    }

    public final void setDuration(long j) {
        this.duration = j;
    }

    public final void setInitialRotation(int i) {
        this.initialRotation = i;
    }

    public final void setSkipStartProgress(float f) {
        this.skipStartProgress = f;
    }

    public final void setAnimationStyle(EntryAnimationStyle entryAnimationStyle) {
        Intrinsics.checkNotNullParameter(entryAnimationStyle, "<set-?>");
        this.animationStyle = entryAnimationStyle;
    }

    public final void draw(Canvas canvas, long j) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        long j2 = this.startDelay;
        if (j < j2) {
            return;
        }
        long j3 = j - j2;
        int i = WhenMappings.$EnumSwitchMapping$0[this.animationStyle.ordinal()];
        if (i == 1) {
            drawForNone(canvas, j3);
        } else if (i == 2) {
            drawForZoomIn(canvas, j3);
        } else if (i == 3) {
            drawForSpringZoomIn(canvas, j3);
        } else if (i == 4) {
            drawForRotateEmphasizedZoomIn(canvas, j3);
        } else if (i != 5) {
        } else {
            drawForEmphasizedZoomIn(canvas, j3);
        }
    }

    private final void draw(Canvas canvas, RectF rectF, Paint paint) {
        canvas.save();
        canvas.rotate(this.initialRotation * 90.0f, rectF.centerX(), rectF.centerY());
        VectorDrawable vectorDrawable = this.srcImgSvg;
        Rect rect = new Rect();
        rectF.round(rect);
        vectorDrawable.setBounds(rect);
        this.srcImgSvg.setColorFilter(paint.getColorFilter());
        this.srcImgSvg.draw(canvas);
        canvas.restore();
    }

    private final void drawForNone(Canvas canvas, long j) {
        draw(canvas, this.destRect, this.paint);
    }

    private final void drawForZoomIn(Canvas canvas, long j) {
        long j2 = this.duration;
        float f = j2 > 0 ? (float) j2 : 1000.0f;
        float f2 = (float) j;
        if (f2 / f < this.skipStartProgress) {
            return;
        }
        if (f2 <= f) {
            float cos = (((float) Math.cos(((f2 - 1000.0f) / 1000.0f) * 3.1415927f)) + 1.0f) / 2.0f;
            draw(canvas, new RectF(this.destRect.centerX() - ((this.destRect.width() / 2.0f) * cos), this.destRect.centerY() - ((this.destRect.height() / 2.0f) * cos), this.destRect.centerX() + ((this.destRect.width() / 2.0f) * cos), this.destRect.centerY() + ((this.destRect.height() / 2.0f) * cos)), this.paint);
            return;
        }
        drawForNone(canvas, j);
    }

    private final void drawForEmphasizedZoomIn(Canvas canvas, long j) {
        long j2 = this.duration;
        float f = j2 > 0 ? (float) j2 : 1000.0f;
        float f2 = (float) j;
        if (f2 <= f) {
            float interpolation = EmphasizedInterpolator.INSTANCE.getInterpolation(f2 / f);
            draw(canvas, new RectF(this.destRect.centerX() - ((this.destRect.width() / 2.0f) * interpolation), this.destRect.centerY() - ((this.destRect.height() / 2.0f) * interpolation), this.destRect.centerX() + ((this.destRect.width() / 2.0f) * interpolation), this.destRect.centerY() + ((this.destRect.height() / 2.0f) * interpolation)), this.paint);
            return;
        }
        drawForNone(canvas, j);
    }

    private final void drawForSpringZoomIn(Canvas canvas, long j) {
        if (j > 1500) {
            drawForNone(canvas, j);
            return;
        }
        float calculateSpringScale = Companion.calculateSpringScale(j);
        draw(canvas, new RectF(this.destRect.centerX() - ((this.destRect.width() / 2.0f) * calculateSpringScale), this.destRect.centerY() - ((this.destRect.height() / 2.0f) * calculateSpringScale), this.destRect.centerX() + ((this.destRect.width() / 2.0f) * calculateSpringScale), this.destRect.centerY() + ((this.destRect.height() / 2.0f) * calculateSpringScale)), this.paint);
    }

    private final void drawForRotateEmphasizedZoomIn(Canvas canvas, long j) {
        canvas.save();
        long j2 = this.duration;
        float f = j2 > 0 ? (float) j2 : 1000.0f;
        float f2 = (float) j;
        float f3 = ((f2 - f) * 360.0f) / 4410.0f;
        if (f2 <= f) {
            float interpolation = EmphasizedInterpolator.INSTANCE.getInterpolation(f2 / f);
            RectF rectF = new RectF(this.destRect.centerX() - ((this.destRect.width() / 2.0f) * interpolation), this.destRect.centerY() - ((this.destRect.height() / 2.0f) * interpolation), this.destRect.centerX() + ((this.destRect.width() / 2.0f) * interpolation), this.destRect.centerY() + ((this.destRect.height() / 2.0f) * interpolation));
            canvas.rotate((interpolation * 180.0f) + f3, this.destRect.centerX(), this.destRect.centerY());
            draw(canvas, rectF, this.paint);
        } else {
            canvas.rotate(f3, this.destRect.centerX(), this.destRect.centerY());
            draw(canvas, this.destRect, this.paint);
        }
        canvas.restore();
    }

    public final RectF getDestRect() {
        return this.destRect;
    }

    /* compiled from: MaterialShapeRenderer.kt */
    /* loaded from: classes3.dex */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final float calculateSpringScale(long j) {
            int i = (int) (j / 16);
            if (MaterialShapeRenderer.springScaleCache.containsKey(Integer.valueOf(i))) {
                return ((Number) MapsKt.getValue(MaterialShapeRenderer.springScaleCache, Integer.valueOf(i))).floatValue();
            }
            float calculatePosition = MaterialShapeRenderer.dampedString.calculatePosition(i);
            MaterialShapeRenderer.springScaleCache.put(Integer.valueOf(i), Float.valueOf(calculatePosition));
            return calculatePosition;
        }
    }
}
