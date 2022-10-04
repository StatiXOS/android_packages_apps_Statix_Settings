package com.statix.android.settings.biometrics.face.anim.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ScrimState {
    private final int mGoneColor;
    private final Paint mPaint;
    private ValueAnimator mScrimAnimator;
    private ValueAnimator.AnimatorUpdateListener mScrimAnimatorListener;
    private final int mShowingColor;
    private int mState = 0;

    public ScrimState(int i, int i2) {
        this.mGoneColor = i;
        this.mShowingColor = i2;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(i);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        this.mScrimAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScrimState.this.lambda$new$0(valueAnimator);
            }
        };
    }

    private /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public boolean isShowing() {
        return this.mState != 0;
    }

    public void fadeOut() {
        int i = this.mState;
        if (i == 0 || i == 2) {
            return;
        }
        this.mState = 2;
        ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mPaint.getColor(), this.mGoneColor);
        this.mScrimAnimator = ofArgb;
        ofArgb.addUpdateListener(this.mScrimAnimatorListener);
        this.mScrimAnimator.setDuration(200L);
        this.mScrimAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ScrimState.this.mState = 0;
            }
        });
        this.mScrimAnimator.start();
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(0.0f, 0.0f, canvas.getWidth() / 2, this.mPaint);
    }
}
