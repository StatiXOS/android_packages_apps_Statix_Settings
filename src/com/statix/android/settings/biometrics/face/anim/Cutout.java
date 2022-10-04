package com.statix.android.settings.biometrics.face.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.TypedValue;

public class Cutout {
    public static Bitmap createCutoutBitmap(Context context, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f = i;
        RectF rectF = new RectF(0.0f, 0.0f, f, f);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getColorAttr(context, 16842836));
        canvas.drawRect(rectF, paint);
        paint.setColor(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float f2 = f / 2.0f;
        canvas.drawCircle(f2, f2, i2, paint);
        return createBitmap;
    }

    private static int getColorAttr(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(i, typedValue, true);
        return typedValue.data;
    }
}
