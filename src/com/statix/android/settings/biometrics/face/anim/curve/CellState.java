package com.statix.android.settings.biometrics.face.anim.curve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.window.R;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationMultiAngleDrawable;

public class CellState {
    private final boolean mAlternateCursor;
    private final FaceEnrollAnimationMultiAngleDrawable.BucketListener mBucketListener;
    private CellConfig mCellConfig;
    private ValueAnimator mCursorAnimator;
    private ValueAnimator.AnimatorUpdateListener mCursorAnimatorListener;
    private final int mCursorColorAcquired;
    private final int mCursorColorGone;
    private Paint mCursorEdgePaint;
    private int mCursorState;
    private final boolean mDisableCursor;
    private boolean mDone;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            CellState.this.handleFadeCursor();
        }
    };
    private final int mIndex;
    private ValueAnimator mNoActivityAnimator;
    private ValueAnimator.AnimatorUpdateListener mNoActivityAnimatorListener;
    private Paint mNoActivityPaint;
    private boolean mNoActivityPulseShouldRepeat;
    private int mScrimAnimationState;
    private ValueAnimator mScrimAnimator;
    private ValueAnimator.AnimatorUpdateListener mScrimAnimatorListener;
    private final int mScrimColorEnrolled;
    private final int mScrimColorNoActivityEnd;
    private final int mScrimColorNoActivityStart;
    private int mScrimColorNotEnrolled;
    private Paint mScrimPaint;

    public CellState(Context context, int i, FaceEnrollAnimationMultiAngleDrawable.BucketListener bucketListener, int i2) {
        this.mIndex = i;
        this.mBucketListener = bucketListener;
        this.mScrimColorNotEnrolled = i2;
        this.mScrimColorEnrolled = context.getColor(R.color.face_enroll_cell_enrolled);
        this.mCursorColorAcquired = context.getColor(R.color.face_enroll_cursor_acquired);
        int color = context.getColor(R.color.face_enroll_cursor_gone);
        this.mCursorColorGone = color;
        int color2 = context.getColor(R.color.face_enroll_cell_no_activity_start);
        this.mScrimColorNoActivityStart = color2;
        this.mScrimColorNoActivityEnd = context.getColor(R.color.face_enroll_cell_no_activity_end);
        boolean z = false;
        this.mScrimAnimationState = 0;
        this.mCursorState = 0;
        Paint paint = new Paint();
        this.mScrimPaint = paint;
        paint.setAntiAlias(true);
        this.mScrimPaint.setAlpha(0);
        boolean z2 = Settings.Secure.getInt(context.getContentResolver(), "com.statix.android.settings.future.biometrics.face.anim.curve.alternate_cursor", 0) != 0;
        this.mAlternateCursor = z2;
        this.mDisableCursor = Settings.Secure.getInt(context.getContentResolver(), "com.statix.android.settings.future.biometrics.face.anim.curve.disable_cursor", 0) != 0 ? true : z;
        int color3 = z2 ? -65536 : context.getColor(R.color.face_enroll_cursor_shadow);
        Paint paint2 = new Paint();
        this.mCursorEdgePaint = paint2;
        paint2.setColor(color);
        this.mCursorEdgePaint.setAntiAlias(true);
        this.mCursorEdgePaint.setShadowLayer(6.0f, 0.0f, 0.0f, color3);
        this.mCursorEdgePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mCursorEdgePaint.setStyle(Paint.Style.STROKE);
        this.mCursorEdgePaint.setStrokeWidth(12.0f);
        Paint paint3 = new Paint();
        this.mNoActivityPaint = paint3;
        paint3.setAntiAlias(true);
        this.mNoActivityPaint.setColor(color2);
        this.mScrimAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CellState.this.lambda$new$0(valueAnimator);
            }
        };
        this.mCursorAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CellState.this.lambda$new$1(valueAnimator);
            }
        };
        this.mNoActivityAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CellState.this.lambda$new$2(valueAnimator);
            }
        };
    }

    private /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mScrimPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    private /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.mCursorEdgePaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    private /* synthetic */ void lambda$new$2(ValueAnimator valueAnimator) {
        this.mNoActivityPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void updateConfig(CellConfig cellConfig) {
        this.mCellConfig = cellConfig;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        CellConfig cellConfig = this.mCellConfig;
        if (cellConfig == null) {
            return;
        }
        if (cellConfig.mFlipVertical) {
            canvas.scale(1.0f, -1.0f, 0.0f, 0.0f);
        }
        canvas.rotate(this.mCellConfig.mRotation);
        canvas.drawPath(this.mCellConfig.mPath, this.mScrimPaint);
        canvas.drawPath(this.mCellConfig.mPath, this.mNoActivityPaint);
        canvas.restore();
    }

    public void drawCursor(Canvas canvas) {
        canvas.save();
        CellConfig cellConfig = this.mCellConfig;
        if (cellConfig == null) {
            return;
        }
        if (cellConfig.mFlipVertical) {
            canvas.scale(1.0f, -1.0f, 0.0f, 0.0f);
        }
        canvas.rotate(this.mCellConfig.mRotation);
        if (!this.mDisableCursor) {
            canvas.drawPath(this.mCellConfig.mPath, this.mCursorEdgePaint);
        }
        canvas.restore();
    }

    public boolean isDone() {
        return this.mDone;
    }

    public void setEarlyDone() {
        this.mDone = true;
    }

    public void stopPulseForNoActivity() {
        this.mNoActivityPulseShouldRepeat = false;
    }

    public void pulseForNoActivity(int i) {
        this.mNoActivityPulseShouldRepeat = true;
        if (isAnimating(this.mNoActivityAnimator)) {
            return;
        }
        int i2 = this.mScrimColorNoActivityStart;
        int i3 = this.mScrimColorNoActivityEnd;
        ValueAnimator ofArgb = ValueAnimator.ofArgb(i2, i3, i3, i2);
        this.mNoActivityAnimator = ofArgb;
        ofArgb.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mNoActivityAnimator.addUpdateListener(this.mNoActivityAnimatorListener);
        this.mNoActivityAnimator.setDuration(1233L);
        this.mNoActivityAnimator.addListener(new AnimatorListenerAdapter() {
            int curPulses = 1;
            final int numPulses;
            final /* synthetic */ int val$times;

            {
                this.val$times = i;
                this.numPulses = i;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (CellState.this.mNoActivityPulseShouldRepeat && this.curPulses < this.numPulses) {
                    CellState.this.mNoActivityAnimator.start();
                    this.curPulses++;
                    return;
                }
                CellState.this.mBucketListener.onNoActivityAnimationFinished();
            }
        });
        this.mNoActivityAnimator.start();
    }

    public void fadeScrimOut(int i) {
        int i2;
        if (i == 2) {
            i2 = 0;
        } else {
            i2 = this.mDone ? this.mScrimColorEnrolled : this.mScrimColorNotEnrolled;
        }
        animateScrimColor(i2, 200L, 1);
    }

    public void fadeScrimIn() {
        fadeScrimIn(200L);
    }

    private void fadeScrimIn(long j) {
        animateScrimColor(this.mDone ? this.mScrimColorEnrolled : this.mScrimColorNotEnrolled, j, 2);
    }

    public void onAcquired() {
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 300L);
        }
        if (this.mCursorState == 0 || !this.mDone) {
            this.mCursorState = 1;
            if (!this.mDone) {
                this.mBucketListener.onStartFinishing();
            }
            this.mDone = true;
            ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mCursorEdgePaint.getColor(), this.mCursorColorAcquired);
            this.mCursorAnimator = ofArgb;
            ofArgb.setDuration(300L);
            this.mCursorAnimator.addUpdateListener(this.mCursorAnimatorListener);
            this.mCursorAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    CellState.this.mCursorState = 3;
                    if (CellState.this.mScrimAnimationState != 1) {
                        CellState.this.fadeScrimOut(1);
                    } else {
                        Log.w("FaceEnroll/CellState", "Index " + CellState.this.mIndex + " intentionally not going to SCRIM_FADE_REASON_DONE");
                    }
                    CellState.this.mHandler.sendEmptyMessageDelayed(1, 300L);
                }
            });
            this.mCursorAnimator.start();
        }
    }

    public void updateScrimNotEnrolledColor(int i, boolean z) {
        this.mScrimColorNotEnrolled = i;
        if (!z) {
            return;
        }
        int i2 = this.mScrimAnimationState;
        if (i2 == 0) {
            animateScrimNotEnrolledColor(200L);
        } else if (i2 == 2) {
            fadeScrimIn(getRemainingAnimationTime(this.mScrimAnimator));
        } else if (i2 != 3) {
        } else {
            animateScrimNotEnrolledColor(getRemainingAnimationTime(this.mScrimAnimator));
        }
    }

    private void animateScrimNotEnrolledColor(long j) {
        if (!this.mDone) {
            int color = this.mScrimPaint.getColor();
            int i = this.mScrimColorNotEnrolled;
            if (color == i) {
                return;
            }
            animateScrimColor(i, j, 3);
        }
    }

    private void animateScrimColor(int i, long j, int i2) {
        if (j <= 0) {
            return;
        }
        if (isAnimating(this.mScrimAnimator)) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimationState = i2;
        ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mScrimPaint.getColor(), i);
        this.mScrimAnimator = ofArgb;
        ofArgb.addUpdateListener(this.mScrimAnimatorListener);
        this.mScrimAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animator) {
                CellState.this.mScrimAnimationState = 0;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                CellState.this.mScrimAnimationState = 0;
            }
        });
        this.mScrimAnimator.setDuration(j);
        this.mScrimAnimator.start();
    }

    public void fadeCursorNow() {
        handleFadeCursor();
    }

    private void handleFadeCursor() {
        this.mCursorState = 2;
        if (isAnimating(this.mCursorAnimator)) {
            this.mCursorAnimator.cancel();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mCursorEdgePaint.getColor(), this.mCursorColorGone);
        this.mCursorAnimator = ofArgb;
        ofArgb.setDuration(200L);
        this.mCursorAnimator.addUpdateListener(this.mCursorAnimatorListener);
        this.mCursorAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                CellState.this.mCursorState = 0;
            }
        });
        this.mCursorAnimator.start();
    }

    private static boolean isAnimating(ValueAnimator valueAnimator) {
        return valueAnimator != null && valueAnimator.isRunning();
    }

    private static long getRemainingAnimationTime(ValueAnimator valueAnimator) {
        return Math.round((1.0f - valueAnimator.getAnimatedFraction()) * ((float) valueAnimator.getDuration()));
    }
}
