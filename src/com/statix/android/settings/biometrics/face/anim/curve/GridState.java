package com.statix.android.settings.biometrics.face.anim.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import androidx.window.R;

public class GridState {
    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private final Paint mEdgePaint;
    private final Handler mHandler;
    private int mState;

    public GridState(Context context, Handler handler) {
        this.mHandler = handler;
        Paint paint = new Paint();
        this.mEdgePaint = paint;
        paint.setColor(context.getColor(R.color.face_enroll_grid));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f);
        paint.setAlpha(0);
        this.mState = 0;
        this.mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                GridState.this.lambda$new$0(valueAnimator);
            }
        };
    }

    private /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mEdgePaint.setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void fadeIn() {
        if (this.mState == 1) {
            return;
        }
        this.mState = 2;
        ValueAnimator ofInt = ValueAnimator.ofInt(this.mEdgePaint.getAlpha(), 64);
        this.mAnimator = ofInt;
        ofInt.removeAllUpdateListeners();
        this.mAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mAnimator.removeAllListeners();
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                GridState.this.mState = 1;
            }
        });
        this.mAnimator.start();
    }

    public void fadeOut(final Runnable runnable) {
        if (this.mState == 0) {
            this.mHandler.post(runnable);
            return;
        }
        this.mState = 2;
        ValueAnimator ofInt = ValueAnimator.ofInt(this.mEdgePaint.getAlpha(), 0);
        this.mAnimator = ofInt;
        ofInt.addUpdateListener(this.mAnimatorUpdateListener);
        this.mAnimator.removeAllListeners();
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                GridState.this.mState = 0;
                GridState.this.mHandler.post(runnable);
            }
        });
        this.mAnimator.start();
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth() / 2;
        int height = canvas.getHeight() / 2;
        float f = width;
        canvas.drawCircle(0.0f, 0.0f, f - (this.mEdgePaint.getStrokeWidth() / 2.0f), this.mEdgePaint);
        float width2 = canvas.getWidth() * 0.32f;
        float width3 = canvas.getWidth() * 0.78f;
        float f2 = (-width2) / 2.0f;
        float f3 = -height;
        float f4 = width2 / 2.0f;
        float f5 = height;
        canvas.drawArc(new RectF(f2, f3, f4, f5), 0.0f, 360.0f, false, this.mEdgePaint);
        float f6 = -width;
        canvas.drawArc(new RectF(f6, f2, f, f4), 0.0f, 360.0f, false, this.mEdgePaint);
        float f7 = (-width3) / 2.0f;
        float f8 = width3 / 2.0f;
        canvas.drawArc(new RectF(f7, f3, f8, f5), 0.0f, 360.0f, false, this.mEdgePaint);
        canvas.drawArc(new RectF(f6, f7, f, f8), 0.0f, 360.0f, false, this.mEdgePaint);
    }
}
