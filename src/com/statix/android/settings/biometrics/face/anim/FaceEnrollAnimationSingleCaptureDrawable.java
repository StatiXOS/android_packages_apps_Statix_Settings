package com.statix.android.settings.biometrics.face.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.os.Handler;
import android.widget.ImageView;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationBase;
import com.statix.android.settings.biometrics.face.anim.single.ArcCollection;

public class FaceEnrollAnimationSingleCaptureDrawable extends FaceEnrollAnimationBase {
    private final Handler mHandler;
    private final ArcCollection mRotatingArcs;

    @Override
    public int getOpacity() {
        return -3;
    }

    @Override
    public void onEnrollmentError(int i, CharSequence charSequence) {
    }

    @Override
    public void setAlpha(int i) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public FaceEnrollAnimationSingleCaptureDrawable(Context context, FaceEnrollAnimationBase.AnimationListener animationListener, ImageView imageView, boolean z) {
        super(context, animationListener, imageView, z);
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mRotatingArcs = new ArcCollection(context, handler);
    }

    @Override
    protected void startFinishing() {
        super.startFinishing();
        this.mRotatingArcs.startFinishing(new Runnable() {
            @Override
            public final void run() {
                FaceEnrollAnimationSingleCaptureDrawable.this.lambda$startFinishing$0();
            }
        });
    }

    private /* synthetic */ void lambda$startFinishing$0() {
        getListener().onEnrollAnimationFinished();
    }

    @Override
    protected void update(long j, long j2) {
        this.mRotatingArcs.update(j, j2);
    }

    @Override
    protected void onUserLeaveGood(CharSequence charSequence) {
        super.onUserLeaveGood(charSequence);
        this.mRotatingArcs.stopRotating();
    }

    @Override
    protected void onUserEnterGood() {
        super.onUserEnterGood();
        this.mRotatingArcs.startRotating();
    }

    @Override
    public void onEnrollmentProgressChange(int i, int i2) {
        super.onEnrollmentProgressChange(i, i2);
        if (i2 == 0) {
            vibrate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mRotatingArcs.draw(canvas);
    }
}
