package com.statix.android.settings.biometrics.face.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import androidx.window.R;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationBase;
import com.statix.android.settings.biometrics.face.anim.curve.DirectionIndicationHelper;
import com.statix.android.settings.biometrics.face.anim.curve.DirectionIndicatorController;
import com.statix.android.settings.biometrics.face.anim.curve.GridController;

public class FaceEnrollAnimationMultiAngleDrawable extends FaceEnrollAnimationBase {
    private final BucketListener mBucketListener;
    private boolean[] mBucketsCompleted;
    private final Context mContext;
    private final DirectionIndicationHelper mDirectionIndicationHelper;
    private final DirectionIndicatorController mDirectionIndicatorController;
    private Paint mFinishingArcPaint;
    private final GridController mGridController;
    private final Handler mHandler;
    private long mLastVibrationMs;

    public interface BucketListener {
        void onNoActivityAnimationFinished();

        void onStartFinishing();
    }

    private boolean isLargeAngle(int i) {
        return i >= 1126 && i <= 1133;
    }

    @Override
    public int getOpacity() {
        return -3;
    }

    @Override
    public void setAlpha(int i) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void handleUserNoActivityAnimation() {
        this.mGridController.pulseForNoActivity(this.mDirectionIndicationHelper.getNoProgressBucket(this.mBucketsCompleted), Integer.MAX_VALUE);
        this.mDirectionIndicatorController.pulseForNoActivity(this.mDirectionIndicationHelper.getNoProgressPulseAngle(this.mBucketsCompleted), Integer.MAX_VALUE);
        getListener().showHelp(this.mContext.getString(R.string.face_enrolling_turn_head_to_arrow));
    }

    public FaceEnrollAnimationMultiAngleDrawable(Context context, FaceEnrollAnimationBase.AnimationListener animationListener, ImageView imageView, ImageView imageView2, boolean z, Bundle bundle) {
        super(context, animationListener, imageView2, z);
        this.mBucketsCompleted = new boolean[25];
        this.mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    FaceEnrollAnimationMultiAngleDrawable.this.handleUserNoActivityAnimation();
                    return;
                }
                Log.w("FaceEnroll/AnimationDrawable", "Unknown message: " + message.what);
            }
        };
        BucketListener bucketListener = new BucketListener() {
            @Override
            public void onStartFinishing() {
                boolean z2;
                synchronized (this) {
                    if (SystemClock.uptimeMillis() - FaceEnrollAnimationMultiAngleDrawable.this.mLastVibrationMs > 50) {
                        z2 = true;
                        FaceEnrollAnimationMultiAngleDrawable.this.mLastVibrationMs = SystemClock.uptimeMillis();
                    } else {
                        z2 = false;
                    }
                }
                if (z2) {
                    FaceEnrollAnimationMultiAngleDrawable.this.vibrate();
                }
            }

            @Override
            public void onNoActivityAnimationFinished() {
                FaceEnrollAnimationMultiAngleDrawable.this.mHandler.removeMessages(1);
                FaceEnrollAnimationMultiAngleDrawable.this.getListener().clearHelp();
            }
        };
        this.mBucketListener = bucketListener;
        this.mContext = context;
        Paint paint = new Paint();
        this.mFinishingArcPaint = paint;
        paint.setAntiAlias(true);
        this.mFinishingArcPaint.setStyle(Paint.Style.STROKE);
        this.mFinishingArcPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mFinishingArcPaint.setStrokeWidth(20.0f);
        this.mFinishingArcPaint.setColor(context.getResources().getColor(R.color.face_enroll_single_capture_rotating_1));
        this.mDirectionIndicationHelper = new DirectionIndicationHelper();
        this.mGridController = new GridController(context, bucketListener);
        this.mDirectionIndicatorController = new DirectionIndicatorController(context, imageView);
        if (bundle != null) {
            this.mBucketsCompleted = bundle.getBooleanArray("key_bucket_status");
            int i = 0;
            while (true) {
                boolean[] zArr = this.mBucketsCompleted;
                if (i >= zArr.length) {
                    return;
                }
                this.mGridController.restoreState(i, zArr[i]);
                i++;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBooleanArray("key_bucket_status", this.mBucketsCompleted);
    }

    private boolean isNewBucketAcquired(int i) {
        return isBucket(i) && !this.mBucketsCompleted[i + (-1101)];
    }

    private void stopCurrentDirectionIndication() {
        this.mDirectionIndicatorController.stopCurrentIndication();
        this.mGridController.stopPulseForNoActivity();
    }

    @Override
    protected void onUserLeaveGood(CharSequence charSequence) {
        super.onUserLeaveGood(charSequence);
        this.mGridController.onUserLeaveGood();
        stopCurrentDirectionIndication();
        this.mHandler.removeMessages(1);
    }

    @Override
    protected void onUserEnterGood() {
        super.onUserEnterGood();
        this.mGridController.onUserEnterGood();
    }

    @Override
    protected void bucketAcquiredWhileScrimShowing(int i) {
        int i2 = i - 1101;
        this.mBucketsCompleted[i2] = true;
        this.mGridController.setEarlyDone(i2);
    }

    @Override
    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        super.onEnrollmentHelp(i, charSequence);
        if (isFinishing() || outOfFOVScrimShowing()) {
            return;
        }
        if (isCenterAcquired() && isBucket(i) && !isNewBucketAcquired(i)) {
            addDelayedUserNoActivityAnimation();
        } else if (isNewBucketAcquired(i) || isLargeAngle(i) || i != 0) {
            this.mHandler.removeMessages(1);
            stopCurrentDirectionIndication();
        }
        if (!isBucket(i)) {
            return;
        }
        int i2 = i - 1101;
        this.mBucketsCompleted[i2] = true;
        if (!isCenterAcquired()) {
            return;
        }
        this.mGridController.onAcquired(i2);
    }

    @Override
    public void onEnrollmentError(int i, CharSequence charSequence) {
        this.mHandler.removeMessages(1);
    }

    @Override
    public void onEnrollmentProgressChange(int i, int i2) {
        super.onEnrollmentProgressChange(i, i2);
        if (i2 == 0) {
            this.mHandler.removeMessages(1);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        this.mGridController.draw(canvas);
        this.mDirectionIndicatorController.draw(canvas);
        canvas.restore();
    }

    @Override
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mGridController.onBoundsChange(rect);
        this.mDirectionIndicatorController.onBoundsChange(rect);
    }

    private void addDelayedUserNoActivityAnimation() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendEmptyMessageDelayed(1, 4000L);
        }
    }
}
