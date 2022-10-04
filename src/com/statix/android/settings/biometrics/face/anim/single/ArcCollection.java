package com.statix.android.settings.biometrics.face.anim.single;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import androidx.window.R;
import java.util.ArrayList;
import java.util.List;

public class ArcCollection {
    private final List<RotatingArc> mArcs;
    private final Handler mHandler;
    private float mSpeed;
    private ValueAnimator mSpeedAnimator;
    private int mState = 0;
    private float mSweepAngle;
    private ValueAnimator mSweepAnimator;

    public ArcCollection(Context context, Handler handler) {
        this.mHandler = handler;
        int[] iArr = {context.getResources().getColor(R.color.face_enroll_single_capture_rotating_4), context.getResources().getColor(R.color.face_enroll_single_capture_rotating_3), context.getResources().getColor(R.color.face_enroll_single_capture_rotating_2), context.getResources().getColor(R.color.face_enroll_single_capture_rotating_1)};
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 4; i++) {
            arrayList.add(new RotatingArc(i, 4, iArr));
        }
        this.mArcs = arrayList;
    }

    public void update(long j, long j2) {
        for (int i = 0; i < this.mArcs.size(); i++) {
            this.mArcs.get(i).update(j, j2);
        }
    }

    public void draw(Canvas canvas) {
        for (int i = 0; i < this.mArcs.size(); i++) {
            this.mArcs.get(i).draw(canvas);
        }
    }

    public void setSweepAngle(float f) {
        this.mSweepAngle = f;
        for (int i = 0; i < this.mArcs.size(); i++) {
            this.mArcs.get(i).setSweepAngle(f);
        }
    }

    public void setSpeed(float f) {
        this.mSpeed = f;
        for (int i = 0; i < this.mArcs.size(); i++) {
            this.mArcs.get(i).setRotateSpeed(f);
        }
    }

    public void stopCurrentAnimation() {
        ValueAnimator valueAnimator = this.mSweepAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mSweepAnimator.cancel();
        }
        ValueAnimator valueAnimator2 = this.mSpeedAnimator;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.mSpeedAnimator.cancel();
        }
        for (int i = 0; i < this.mArcs.size(); i++) {
            this.mArcs.get(i).stopCurrentAnimation();
        }
    }

    public void stopRotating() {
        int i = this.mState;
        if (i == 1 || i == 3) {
            return;
        }
        stopCurrentAnimation();
        this.mState = 3;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mSweepAngle, 0.0f);
        this.mSweepAnimator = ofFloat;
        ofFloat.setDuration(1100L);
        this.mSweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArcCollection.this.lambda$stopRotating$0(valueAnimator);
            }
        });
        this.mSweepAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ArcCollection.this.mState = 1;
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.mSpeed, 0.0f);
        this.mSpeedAnimator = ofFloat2;
        ofFloat2.setDuration(1100L);
        this.mSpeedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArcCollection.this.lambda$stopRotating$1(valueAnimator);
            }
        });
        this.mSweepAnimator.start();
        for (int i2 = 0; i2 < this.mArcs.size(); i2++) {
            this.mArcs.get(i2).stopRotating(1100L);
        }
    }

    private /* synthetic */ void lambda$stopRotating$0(ValueAnimator valueAnimator) {
        setSweepAngle(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private /* synthetic */ void lambda$stopRotating$1(ValueAnimator valueAnimator) {
        setSpeed(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void startRotating() {
        if (this.mState == 2) {
            return;
        }
        stopCurrentAnimation();
        this.mState = 2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mSweepAngle, 90.0f);
        this.mSweepAnimator = ofFloat;
        ofFloat.setDuration(800L);
        this.mSweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArcCollection.this.lambda$startRotating$2(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.mSpeed, 200.0f);
        this.mSpeedAnimator = ofFloat2;
        ofFloat2.setDuration(800L);
        this.mSpeedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArcCollection.this.lambda$startRotating$3(valueAnimator);
            }
        });
        this.mSweepAnimator.start();
        this.mSpeedAnimator.start();
        for (int i = 0; i < this.mArcs.size(); i++) {
            this.mArcs.get(i).startRotating(800L);
        }
    }

    private /* synthetic */ void lambda$startRotating$2(ValueAnimator valueAnimator) {
        setSweepAngle(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private /* synthetic */ void lambda$startRotating$3(ValueAnimator valueAnimator) {
        setSpeed(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void startFinishing(final Runnable runnable) {
        int i = this.mState;
        if (i == 4 || i == 5) {
            return;
        }
        stopCurrentAnimation();
        this.mState = 4;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mSweepAngle, 360.0f);
        this.mSweepAnimator = ofFloat;
        ofFloat.setDuration(800L);
        this.mSweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArcCollection.this.lambda$startFinishing$4(valueAnimator);
            }
        });
        this.mSweepAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ArcCollection.this.mHandler.post(runnable);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.mSpeed, 200.0f);
        this.mSpeedAnimator = ofFloat2;
        ofFloat2.setDuration(800L);
        this.mSpeedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ArcCollection.this.lambda$startFinishing$5(valueAnimator);
            }
        });
        this.mSweepAnimator.start();
        this.mSpeedAnimator.start();
        for (int i2 = 0; i2 < this.mArcs.size(); i2++) {
            this.mArcs.get(i2).startFinishing(800L);
        }
    }

    private /* synthetic */ void lambda$startFinishing$4(ValueAnimator valueAnimator) {
        setSweepAngle(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private /* synthetic */ void lambda$startFinishing$5(ValueAnimator valueAnimator) {
        setSpeed(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}
