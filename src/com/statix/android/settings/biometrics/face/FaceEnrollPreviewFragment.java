package com.statix.android.settings.biometrics.face;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.window.R;
import com.statix.android.settings.biometrics.face.FaceEnrollSidecar;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationBase;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationMultiAngleDrawable;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationSingleCaptureDrawable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FaceEnrollPreviewFragment extends Fragment implements FaceEnrollSidecar.Listener, FaceEnrollSidecar.PreviewSurfaceProvider {
    private FaceEnrollAnimationBase mAnimationDrawable;
    private CameraDevice mCameraDevice;
    private String mCameraId;
    private CameraManager mCameraManager;
    private CameraCaptureSession mCaptureSession;
    private ImageView mCircleView;
    private FaceEnrollAnimationBase.AnimationListener mClientAnimationListener;
    private boolean mFromSetupWizard;
    private CaptureRequest mPreviewRequest;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private Size mPreviewSize;
    private boolean mRequireDiversity;
    private boolean mShouldManagePreview;
    private SquareTextureView mTextureView;
    private boolean mTextureViewDestroyed;
    private Handler mHandler = new Handler();
    private final FaceEnrollAnimationBase.AnimationListener mLocalAnimationListener = new AnonymousClass1();
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            FaceEnrollPreviewFragment.this.mTextureViewDestroyed = false;
            FaceEnrollPreviewFragment.this.setUpPreview(i, i2);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            FaceEnrollPreviewFragment.this.configureTransform(i, i2);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            FaceEnrollPreviewFragment.this.mTextureViewDestroyed = true;
            return true;
        }
    };
    private final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j, long j2) {
            super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
            if (j2 == 1) {
                FaceEnrollPreviewFragment.this.mAnimationDrawable.onFirstFrameReceived();
            }
        }
    };
    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            FaceEnrollPreviewFragment.this.mCameraDevice = cameraDevice;
            try {
                if (FaceEnrollPreviewFragment.this.mTextureViewDestroyed) {
                    Log.e("FaceEnroll/PreviewFragment", "Texture view destroyed but camera is open");
                }
                if (!FaceEnrollPreviewFragment.this.mTextureView.isAvailable()) {
                    Log.e("FaceEnroll/PreviewFragment", "Error the surface texture was not attached to the window");
                }
                SurfaceTexture surfaceTexture = FaceEnrollPreviewFragment.this.mTextureView.getSurfaceTexture();
        
        surfaceTexture.setDefaultBufferSize(FaceEnrollPreviewFragment.this.mPreviewSize.getWidth(), FaceEnrollPreviewFragment.this.mPreviewSize.getHeight());
                Surface surface = new Surface(surfaceTexture);
                FaceEnrollPreviewFragment faceEnrollPreviewFragment = FaceEnrollPreviewFragment.this;
                faceEnrollPreviewFragment.mPreviewRequestBuilder = faceEnrollPreviewFragment.mCameraDevice.createCaptureRequest(1);
                FaceEnrollPreviewFragment.this.mPreviewRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, 0);
                FaceEnrollPreviewFragment.this.mPreviewRequestBuilder.addTarget(surface);
                FaceEnrollPreviewFragment.this.mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        if (FaceEnrollPreviewFragment.this.mCameraDevice == null) {
                            return;
                        }
                        FaceEnrollPreviewFragment.this.mCaptureSession = cameraCaptureSession;
                        try {
                            FaceEnrollPreviewFragment.this.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                            FaceEnrollPreviewFragment faceEnrollPreviewFragment2 = FaceEnrollPreviewFragment.this;
                            faceEnrollPreviewFragment2.mPreviewRequest = faceEnrollPreviewFragment2.mPreviewRequestBuilder.build();
                            FaceEnrollPreviewFragment.this.mCaptureSession.setRepeatingRequest(FaceEnrollPreviewFragment.this.mPreviewRequest, FaceEnrollPreviewFragment.this.mCaptureCallback, FaceEnrollPreviewFragment.this.mHandler);
                        } catch (CameraAccessException e) {
                            Log.e("FaceEnroll/PreviewFragment", "Unable to access camera", e);
                        }
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        Log.e("FaceEnroll/PreviewFragment", "Unable to configure camera");
                    }
                }, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            FaceEnrollPreviewFragment.this.mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            FaceEnrollPreviewFragment.this.mCameraDevice = null;
        }
    };

    /* renamed from: com.google.android.settings.biometrics.face.FaceEnrollPreviewFragment$1  reason: invalid class name */
    private class AnonymousClass1 implements FaceEnrollAnimationBase.AnimationListener {
        AnonymousClass1() {
        }

        @Override
        public void onEnrollAnimationStarted() {
            FaceEnrollPreviewFragment.this.mClientAnimationListener.onEnrollAnimationStarted();
        }

        @Override
        public void onEnrollAnimationFinished() {
            FaceEnrollPreviewFragment.this.mClientAnimationListener.onEnrollAnimationFinished();
            if (FaceEnrollPreviewFragment.this.mShouldManagePreview) {
                FaceEnrollPreviewFragment.this.mHandler.post(new Runnable() {
                    @Override
                    public final void run() {
                        FaceEnrollPreviewFragment.AnonymousClass1.this.lambda$onEnrollAnimationFinished$0();
                    }
                });
            }
        }

        private /* synthetic */ void lambda$onEnrollAnimationFinished$0() {
            FaceEnrollPreviewFragment.this.closeCamera();
        }

        @Override
        public void showHelp(CharSequence charSequence) {
            FaceEnrollPreviewFragment.this.mClientAnimationListener.showHelp(charSequence);
        }

        @Override
        public void clearHelp() {
            FaceEnrollPreviewFragment.this.mClientAnimationListener.clearHelp();
        }
    }

    public void setFromSetupWizard(boolean z) {
        this.mFromSetupWizard = z;
    }

    protected void setShouldManagePreview(boolean z) {
        this.mShouldManagePreview = z;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mTextureView = (SquareTextureView) getActivity().findViewById(R.id.texture_view);
        this.mCircleView = (ImageView) getActivity().findViewById(R.id.circle_view);
        if (bundle != null) {
            this.mRequireDiversity = bundle.getBoolean("accessibility_diversity");
            this.mFromSetupWizard = bundle.getBoolean("is_suw");
            this.mShouldManagePreview = bundle.getBoolean("should_manage_preview");
        }
        if (this.mRequireDiversity) {
            this.mAnimationDrawable = new FaceEnrollAnimationMultiAngleDrawable(getContext(), this.mLocalAnimationListener, (ImageView) getActivity().findViewById(R.id.indicator_view), (ImageView) getActivity().findViewById(R.id.distance_indicator_view), this.mFromSetupWizard, bundle);
        } else {
            this.mAnimationDrawable = new FaceEnrollAnimationSingleCaptureDrawable(getContext(), this.mLocalAnimationListener, (ImageView) getActivity().findViewById(R.id.distance_indicator_view), this.mFromSetupWizard);
        }
        this.mCircleView.setImageDrawable(this.mAnimationDrawable);
        this.mCameraManager = (CameraManager) getContext().getSystemService("camera");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mTextureView.isAvailable()) {
            setUpPreview(this.mTextureView.getWidth(), this.mTextureView.getHeight());
        } else {
            this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mShouldManagePreview) {
            closeCamera();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("accessibility_diversity", this.mRequireDiversity);
        this.mAnimationDrawable.onSaveInstanceState(bundle);
        bundle.putBoolean("is_suw", this.mFromSetupWizard);
        bundle.putBoolean("should_manage_preview", this.mShouldManagePreview);
    }

    @Override
    public void onEnrollmentError(int i, CharSequence charSequence) {
        FaceEnrollAnimationBase faceEnrollAnimationBase = this.mAnimationDrawable;
        if (faceEnrollAnimationBase != null) {
            faceEnrollAnimationBase.onEnrollmentError(i, charSequence);
        }
    }

    @Override
    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        FaceEnrollAnimationBase faceEnrollAnimationBase = this.mAnimationDrawable;
        if (faceEnrollAnimationBase != null) {
            faceEnrollAnimationBase.onEnrollmentHelp(i, charSequence);
        }
    }

    @Override
    public void onEnrollmentProgressChange(int i, int i2) {
        FaceEnrollAnimationBase faceEnrollAnimationBase = this.mAnimationDrawable;
        if (faceEnrollAnimationBase != null) {
            faceEnrollAnimationBase.onEnrollmentProgressChange(i, i2);
        }
    }

    public void setAnimationListener(FaceEnrollAnimationBase.AnimationListener animationListener) {
        this.mClientAnimationListener = animationListener;
    }

    public void setAnimationDrawableMode(boolean z) {
        this.mRequireDiversity = z;
    }

    private void setUpCameraOutputs() {
        String[] cameraIdList;
        try {
            for (String str : this.mCameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(str);
                Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (num != null && num.intValue() == 0) {
                    this.mCameraId = str;
                    this.mPreviewSize = chooseOptimalSize(((StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(SurfaceTexture.class));
                    return;
                }
            }
        } catch (CameraAccessException e) {
            Log.e("FaceEnroll/PreviewFragment", "Unable to access camera", e);
        }
    }

    private void setUpPreview(int i, int i2) {
        try {
            setUpCameraOutputs();
            if (this.mShouldManagePreview) {
                this.mCameraManager.openCamera(this.mCameraId, this.mCameraStateCallback, this.mHandler);
            } else {
                this.mAnimationDrawable.onFirstFrameReceived();
            }
            configureTransform(i, i2);
        } catch (CameraAccessException e) {
            Log.e("FaceEnroll/PreviewFragment", "Unable to open camera", e);
        }
    }

    private Size chooseOptimalSize(Size[] sizeArr) {
        return (Size) Collections.min(Arrays.asList(sizeArr), new Comparator<Size>() {
            @Override
            public int compare(Size size, Size size2) {
                if (size.getHeight() >= 480 || size2.getHeight() < 480) {
                    if (size.getHeight() >= 480 && size2.getHeight() < 480) {
                        return -1;
                    }
                    int compare = Float.compare(Math.abs((size.getWidth() / size.getHeight()) - 1.3f), Math.abs((size2.getWidth() / size2.getHeight()) - 1.3f));
                    return compare != 0 ? compare : Integer.compare(size.getHeight(), size2.getHeight());
                }
                return 1;
            }
        });
    }

    private void configureTransform(int i, int i2) {
        if (this.mTextureView == null) {
            return;
        }
        float width = i / this.mPreviewSize.getWidth();
        float height = i2 / this.mPreviewSize.getHeight();
        float min = Math.min(width, height);
        float f = width / min;
        float f2 = height / min;
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.dimen.face_preview_scale, typedValue, true);
        Matrix matrix = new Matrix();
        this.mTextureView.getTransform(matrix);
        TypedValue typedValue2 = new TypedValue();
        TypedValue typedValue3 = new TypedValue();
        getResources().getValue(R.dimen.face_preview_translate_x, typedValue2, true);
        getResources().getValue(R.dimen.face_preview_translate_y, typedValue3, true);
        matrix.setScale(f * typedValue.getFloat(), f2 * typedValue.getFloat());
        matrix.postTranslate(typedValue2.getFloat(), typedValue3.getFloat());
        this.mTextureView.setTransform(matrix);
    }

    private void closeCamera() {
        CameraCaptureSession cameraCaptureSession = this.mCaptureSession;
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            this.mCaptureSession = null;
        }
        CameraDevice cameraDevice = this.mCameraDevice;
        if (cameraDevice != null) {
            cameraDevice.close();
            this.mCameraDevice = null;
        }
    }

    public Surface getPreviewSurface() {
        if (this.mTextureViewDestroyed) {
            Log.e("FaceEnroll/PreviewFragment", "Failed to get the preview surface, the surface texture is destroyed.");
            return null;
        }
        SurfaceTexture surfaceTexture = this.mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(this.mPreviewSize.getWidth(), this.mPreviewSize.getHeight());
        return new Surface(surfaceTexture);
    }
}
