package com.statix.android.settings.biometrics.face;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.face.Face;
import android.hardware.face.FaceManager;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.window.R;
import com.android.settings.biometrics.face.FaceUpdater;
import com.statix.android.settings.biometrics.face.FaceEnrollDialogFactory;
import com.statix.android.settings.biometrics.face.FaceEnrollSidecar;
import com.statix.android.settings.biometrics.face.anim.FaceEnrollAnimationBase;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeResolver;
import java.util.ArrayList;
import java.util.List;

public class FaceEnrollEnrolling extends FragmentActivity implements FaceEnrollSidecar.Listener {
    private static final AudioAttributes SONIFICATION_AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private boolean mCenterAcquired;
    private boolean mDebugConsent;
    private boolean mDidCommitPartialEnrollment;
    private long mEnrollmentStartTime;
    private TextView mErrorText;
    private FooterBarMixin mFooterBarMixin;
    private boolean mFromSetupWizard;
    private int mGazeFailCount;
    private Handler mHandler;
    private HelpController mHelpController;
    private Interpolator mLinearOutSlowInInterpolator;
    private FaceEnrollPreviewFragment mPreviewFragment;
    private List<Face> mPreviouslyEnrolledFaces;
    private boolean mRequireAttention;
    private boolean mRequireDiversity;
    private boolean mShouldManagePreview;
    private boolean mShowingAnimationHelp;
    private FaceEnrollSidecar mSidecar;
    private boolean mSingleFromMulti;
    private boolean mTalkbackEnabled;
    protected byte[] mToken;
    protected int mUserId;
    private UserManager mUserManager;
    private VibrationEffect mVibrationEffect;
    private Vibrator mVibrator;
    private ArrayList<Integer> mDisabledFeatures = new ArrayList<>();
    private int mRemaining = -1;
    private Runnable mMultiAngleNotCenteredBeforeZeroZeroRunnable = new Runnable() {
        @Override
        public final void run() {
            FaceEnrollEnrolling.this.lambda$new$0();
        }
    };
    private Runnable mNoProgressTimeoutRunnable = new Runnable() {
        @Override
        public final void run() {
            FaceEnrollEnrolling.this.lambda$new$1();
        }
    };
    private FaceEnrollAnimationBase.AnimationListener mAnimationListener = new FaceEnrollAnimationBase.AnimationListener() {
        @Override
        public void onEnrollAnimationStarted() {
            FaceEnrollEnrolling.this.mFooterBarMixin.getSecondaryButton().setVisibility(4);
        }

        @Override
        public void onEnrollAnimationFinished() {
            if (!FaceEnrollEnrolling.this.mRequireDiversity) {
                Intent intent = new Intent(FaceEnrollEnrolling.this, FaceEnrollConfirmation.class);
                intent.putExtras(FaceEnrollEnrolling.this.getIntent());
                FaceEnrollEnrolling.this.startActivityForResult(intent, 2);
            }
        }

        @Override
        public void showHelp(CharSequence charSequence) {
            FaceEnrollEnrolling.this.mShowingAnimationHelp = true;
            FaceEnrollEnrolling.this.mHelpController.showHelp(charSequence);
        }

        @Override
        public void clearHelp() {
            if (FaceEnrollEnrolling.this.mShowingAnimationHelp) {
                FaceEnrollEnrolling.this.mShowingAnimationHelp = false;
                FaceEnrollEnrolling.this.mHelpController.clearHelp();
            }
        }
    };

    private class HelpController {
        private Debouncer mDebouncer;
        private Runnable mHelpFinishedRunnable;
        private ViewPropertyAnimator mTextAnimation;
        private long mTextShownTime;

        private HelpController() {
            this.mDebouncer = new Debouncer(10);
            this.mHelpFinishedRunnable = new Runnable() {
                @Override
                public final void run() {
                    FaceEnrollEnrolling.HelpController.this.clearHelp();
                }
            };
        }

        void debounceAndMaybeShowHelp(int i, CharSequence charSequence) {
            if (TextUtils.isEmpty(charSequence)) {
                this.mDebouncer.reset();
                return;
            }
            this.mDebouncer.updateBuffer(i);
            if (!this.mDebouncer.passesDebounce(i)) {
                return;
            }
            FaceEnrollEnrolling.this.mShowingAnimationHelp = false;
            showHelp(charSequence);
        }

        void clearHelpIfOverAttenuateThreshold() {
            if (System.currentTimeMillis() - this.mTextShownTime >= 3000) {
                clearHelp();
            }
        }

        private void showHelp(CharSequence charSequence) {
            float f;
            FaceEnrollEnrolling.this.mHandler.removeCallbacks(this.mHelpFinishedRunnable);
            FaceEnrollEnrolling.this.mHandler.postDelayed(this.mHelpFinishedRunnable, 3000L);
            if ((FaceEnrollEnrolling.this.mErrorText.getVisibility() != 0 || !TextUtils.equals(charSequence, FaceEnrollEnrolling.this.mErrorText.getText())) && !TextUtils.isEmpty(charSequence)) {
                this.mTextShownTime = System.currentTimeMillis();
                FaceEnrollEnrolling.this.mErrorText.setText(charSequence);
                float dimensionPixelSize = FaceEnrollEnrolling.this.getResources().getDimensionPixelSize(R.dimen.face_error_text_appear_distance);
                Animation animation = FaceEnrollEnrolling.this.mErrorText.getAnimation();
                if (animation != null && !animation.hasEnded()) {
                    FaceEnrollEnrolling.this.mErrorText.getAnimation().cancel();
                }
                if (FaceEnrollEnrolling.this.mErrorText.getVisibility() == 0) {
                    dimensionPixelSize = FaceEnrollEnrolling.this.mErrorText.getTranslationY();
                    f = FaceEnrollEnrolling.this.mErrorText.getAlpha();
                } else {
                    f = 0.0f;
                }
                FaceEnrollEnrolling.this.mErrorText.setVisibility(0);
                FaceEnrollEnrolling.this.mErrorText.setTranslationY(dimensionPixelSize);
                FaceEnrollEnrolling.this.mErrorText.setAlpha(f);
                FaceEnrollEnrolling.this.mErrorText.animate().alpha(1.0f).translationY(0.0f).setDuration(200L).setInterpolator(FaceEnrollEnrolling.this.mLinearOutSlowInInterpolator).start();
            }
        }

        private void clearHelp() {
            FaceEnrollEnrolling.this.mHandler.removeCallbacks(this.mHelpFinishedRunnable);
            if (this.mTextAnimation != null) {
                Log.w("FaceEnrollEnrolling", "Already clearing help");
            } else if (FaceEnrollEnrolling.this.mErrorText.getVisibility() != 0) {
            } else {
                ViewPropertyAnimator withEndAction = FaceEnrollEnrolling.this.mErrorText.animate().alpha(0.0f).translationY(FaceEnrollEnrolling.this.getResources().getDimensionPixelSize(R.dimen.face_error_text_appear_distance)).setDuration(200L).setInterpolator(FaceEnrollEnrolling.this.mLinearOutSlowInInterpolator).withEndAction(new Runnable() {
                    @Override
                    public final void run() {
                        FaceEnrollEnrolling.HelpController.this.lambda$clearHelp$0();
                    }
                });
                this.mTextAnimation = withEndAction;
                withEndAction.start();
            }
        }

        private /* synthetic */ void lambda$clearHelp$0() {
            FaceEnrollEnrolling.this.mErrorText.setVisibility(4);
            this.mTextAnimation = null;
        }
    }

    private /* synthetic */ void lambda$new$0() {
        this.mHelpController.showHelp(getText(R.string.face_enrolling_center_head));
    }

    private /* synthetic */ void lambda$new$1() {
        int i = this.mRemaining;
        if (i == -1 || i == 25) {
            FaceEnrollSidecar faceEnrollSidecar = this.mSidecar;
            if (faceEnrollSidecar != null) {
                faceEnrollSidecar.cancelEnrollment();
                this.mSidecar.logEnrollmentEnded(0, false);
            }
            showErrorDialog(getText(R.string.security_settings_face_enroll_error_timeout_dialog_message), 3);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        ThemeResolver.getDefault().applyTheme(this);
        setContentView(R.layout.face_enrolling);
        ((SquareFrameLayout) findViewById(R.id.square_frame_layout)).setOuterRegion(R.id.indicator_view, 30);
        super.onCreate(bundle);
        boolean z = false;
        if (bundle != null) {
            this.mToken = bundle.getByteArray("hw_auth_token");
            this.mUserId = bundle.getInt("user_id", UserHandle.myUserId());
            this.mFromSetupWizard = bundle.getBoolean("is_suw");
            this.mRequireDiversity = bundle.getBoolean("accessibility_diversity", true);
            this.mRequireAttention = bundle.getBoolean("accessibility_vision", true);
            this.mSingleFromMulti = bundle.getBoolean("from_multi_timeout", false);
            this.mDebugConsent = bundle.getBoolean("debug_consent", false);
        } else {
            this.mToken = getIntent().getByteArrayExtra("hw_auth_token");
            this.mUserId = getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
            this.mFromSetupWizard = WizardManagerHelper.isAnySetupWizard(getIntent());
            this.mRequireDiversity = getIntent().getBooleanExtra("accessibility_diversity", true);
            this.mRequireAttention = getIntent().getBooleanExtra("accessibility_vision", true);
            this.mSingleFromMulti = getIntent().getBooleanExtra("from_multi_timeout", false);
            this.mDebugConsent = getIntent().getBooleanExtra("debug_consent", false);
        }
        this.mShouldManagePreview = getResources().getBoolean(R.bool.config_face_settings_should_manage_preview);
        this.mVibrator = (Vibrator) getSystemService(Vibrator.class);
        this.mVibrationEffect = VibrationEffect.get(1);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        this.mFooterBarMixin = footerBarMixin;
        footerBarMixin.setRemoveFooterBarWhenEmpty(false);
        if (this.mFromSetupWizard) {
            this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.face_enrolling_do_it_later).setListener(new View.OnClickListener() {
                @Override
                public final void onClick(View view) {
                    FaceEnrollEnrolling.this.onButtonNegative(view);
                }
            }).setButtonType(7).setTheme(R.style.SudGlifButton.Secondary).build());
        } else {
            this.mFooterBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.face_enrolling_gaze_dialog_cancel).setListener(new View.OnClickListener() {
                @Override
                public final void onClick(View view) {
                    FaceEnrollEnrolling.this.onButtonNegative(view);
                }
            }).setButtonType(2).setTheme(R.style.SudGlifButton.Secondary).build());
        }
        this.mFooterBarMixin.getSecondaryButton().setVisibility(0);
        this.mUserManager = (UserManager) getSystemService(UserManager.class);
        this.mHandler = new Handler();
        this.mErrorText = (TextView) findViewById(R.id.error_text);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(this, 17563662);
        this.mHelpController = new HelpController();
        this.mTalkbackEnabled = false;
        AccessibilityManager accessibilityManager = (AccessibilityManager) getApplicationContext().getSystemService(AccessibilityManager.class);
        if (accessibilityManager != null) {
            if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
                z = true;
            }
            this.mTalkbackEnabled = z;
        }
        if (!this.mRequireDiversity) {
            setHeaderText(R.string.face_enrolling_title_accessibility);
            getLayout().setDescriptionText(R.string.face_enrolling_center_head);
            this.mDisabledFeatures.add(2);
        } else {
            setHeaderText(R.string.face_enrolling_title);
        }
        this.mDisabledFeatures.add(1);
        if (this.mRequireDiversity) {
            this.mHandler.postDelayed(this.mMultiAngleNotCenteredBeforeZeroZeroRunnable, 3000L);
            this.mHandler.postDelayed(this.mNoProgressTimeoutRunnable, 33000L);
        }
        getWindow().addFlags(128);
        startEnrollment();
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i != 1) {
            if (i != 2) {
                return;
            }
            setResult(i2);
            finish();
        } else if (i2 != 2 && i2 != 3) {
        } else {
            setResult(i2);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FaceEnrollSidecar faceEnrollSidecar = this.mSidecar;
        if (faceEnrollSidecar != null) {
            faceEnrollSidecar.setListener(null);
        }
        if (isChangingConfigurations() || this.mRemaining == 0) {
            return;
        }
        FaceEnrollSidecar faceEnrollSidecar2 = this.mSidecar;
        if (faceEnrollSidecar2 != null) {
            faceEnrollSidecar2.cancelEnrollment();
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
        }
        if (!this.mFromSetupWizard) {
            setResult(3);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        FaceEnrollSidecar faceEnrollSidecar = this.mSidecar;
        if (faceEnrollSidecar != null) {
            faceEnrollSidecar.setListener(null);
            this.mSidecar.cancelEnrollment();
            getSupportFragmentManager().beginTransaction().remove(this.mSidecar).commitAllowingStateLoss();
            this.mSidecar = null;
        }
        super.onBackPressed();
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        theme.applyStyle(R.style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, i, z);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putByteArray("hw_auth_token", this.mToken);
        bundle.putInt("user_id", this.mUserId);
        bundle.putBoolean("is_suw", this.mFromSetupWizard);
        bundle.putBoolean("accessibility_vision", this.mRequireAttention);
        bundle.putBoolean("accessibility_diversity", this.mRequireDiversity);
        bundle.putBoolean("from_multi_timeout", this.mSingleFromMulti);
        bundle.putBoolean("debug_consent", this.mDebugConsent);
    }

    private void startEnrollment() {
        this.mEnrollmentStartTime = System.currentTimeMillis();
        this.mPreviouslyEnrolledFaces = ((FaceManager) getSystemService(FaceManager.class)).getEnrolledFaces(this.mUserId);
        FaceEnrollPreviewFragment faceEnrollPreviewFragment = (FaceEnrollPreviewFragment) getSupportFragmentManager().findFragmentByTag("tag_preview");
        this.mPreviewFragment = faceEnrollPreviewFragment;
        if (faceEnrollPreviewFragment == null) {
            FaceEnrollPreviewFragment faceEnrollPreviewFragment2 = new FaceEnrollPreviewFragment();
            this.mPreviewFragment = faceEnrollPreviewFragment2;
            faceEnrollPreviewFragment2.setAnimationListener(this.mAnimationListener);
            this.mPreviewFragment.setFromSetupWizard(this.mFromSetupWizard);
            this.mPreviewFragment.setShouldManagePreview(this.mShouldManagePreview);
            if (this.mRequireDiversity) {
                this.mPreviewFragment.setAnimationDrawableMode(true);
            } else {
                this.mPreviewFragment.setAnimationDrawableMode(false);
            }
            getSupportFragmentManager().beginTransaction().add(this.mPreviewFragment, "tag_preview").commitAllowingStateLoss();
        } else {
            faceEnrollPreviewFragment.setAnimationListener(this.mAnimationListener);
        }
        FaceEnrollSidecar faceEnrollSidecar = (FaceEnrollSidecar) getSupportFragmentManager().findFragmentByTag("tag_sidecar");
        this.mSidecar = faceEnrollSidecar;
        if (faceEnrollSidecar == null) {
            int[] iArr = new int[this.mDisabledFeatures.size()];
            for (int i = 0; i < this.mDisabledFeatures.size(); i++) {
                iArr[i] = this.mDisabledFeatures.get(i).intValue();
            }
            FaceEnrollSidecar faceEnrollSidecar2 = new FaceEnrollSidecar();
            this.mSidecar = faceEnrollSidecar2;
            faceEnrollSidecar2.init(iArr, this.mSingleFromMulti, this.mTalkbackEnabled, this.mShouldManagePreview, this.mDebugConsent);
            getSupportFragmentManager().beginTransaction().add(this.mSidecar, "tag_sidecar").commitAllowingStateLoss();
        }
        this.mSidecar.setListener(this);
        if (!this.mShouldManagePreview) {
            this.mSidecar.setPreviewSurfaceProvider(this.mPreviewFragment);
        }
    }

    @Override
    public void onEnrollmentHelp(int i, CharSequence charSequence) {
        if (i == 1140) {
            this.mDidCommitPartialEnrollment = true;
        }
        if (this.mTalkbackEnabled || !this.mRequireDiversity) {
            switch (i) {
                case 4:
                    charSequence = getText(R.string.face_enrolling_too_close);
                    break;
                case 5:
                    charSequence = getText(R.string.face_enrolling_too_far);
                    break;
                case 6:
                    charSequence = getText(R.string.face_enrolling_too_high);
                    break;
                case 7:
                    charSequence = getText(R.string.face_enrolling_too_low);
                    break;
                case 8:
                    charSequence = getText(R.string.face_enrolling_too_right);
                    break;
                case 9:
                    charSequence = getText(R.string.face_enrolling_too_left);
                    break;
                case 11:
                    charSequence = getText(R.string.face_enrolling_center_head);
                    break;
            }
        } else {
            if (i != 11) {
                switch (i) {
                    case 4:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        break;
                    case 5:
                        charSequence = getText(R.string.face_enrolling_too_far);
                        break;
                    default:
                        switch (i) {
                            case 1126:
                            case 1127:
                            case 1128:
                            case 1129:
                            case 1130:
                            case 1131:
                            case 1132:
                            case 1133:
                                charSequence = getText(R.string.face_enrolling_turned_too_far);
                                break;
                        }
                }
            }
            charSequence = getText(R.string.face_enrolling_center_head);
        }
        if (!isFinishing()) {
            if (!this.mCenterAcquired && i == 10 && this.mSidecar.isEnrolling()) {
                int i2 = this.mGazeFailCount + 1;
                this.mGazeFailCount = i2;
                if (i2 >= 10 && System.currentTimeMillis() - this.mEnrollmentStartTime >= 5000) {
                    showGazeDialog();
                }
            }
            if (!this.mRequireDiversity) {
                if (i != 0) {
                    this.mHelpController.debounceAndMaybeShowHelp(i, charSequence);
                } else {
                    this.mHelpController.clearHelpIfOverAttenuateThreshold();
                }
            } else if (FaceUtils.isOneOfCenterBuckets(i) && !this.mCenterAcquired) {
                this.mHandler.removeCallbacks(this.mMultiAngleNotCenteredBeforeZeroZeroRunnable);
                this.mHelpController.clearHelp();
                this.mCenterAcquired = true;
            } else if (i != 0 && this.mCenterAcquired) {
                this.mHelpController.debounceAndMaybeShowHelp(i, charSequence);
            } else if (i != 0 && !this.mCenterAcquired) {
                this.mHelpController.debounceAndMaybeShowHelp(i, charSequence);
            } else if (i == 0) {
                this.mHelpController.clearHelpIfOverAttenuateThreshold();
            }
            this.mPreviewFragment.onEnrollmentHelp(i, charSequence);
        }
    }

    private void showGazeDialog() {
        this.mVibrator.vibrate(this.mVibrationEffect, SONIFICATION_AUDIO_ATTRIBUTES);
        this.mSidecar.cancelEnrollment();
        FaceGazeDialog newInstance = FaceGazeDialog.newInstance();
        newInstance.setButtonListener(new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceEnrollEnrolling.this.lambda$showGazeDialog$2(dialogInterface, i);
            }
        });
        newInstance.show(getSupportFragmentManager(), FaceGazeDialog.class.getName());
    }

    private /* synthetic */ void lambda$showGazeDialog$2(DialogInterface dialogInterface, int i) {
        boolean z;
        if (i == -1) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.mDisabledFeatures.size()) {
                    z = false;
                    break;
                } else if (this.mDisabledFeatures.get(i2).intValue() == 1) {
                    z = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (!z) {
                this.mDisabledFeatures.add(1);
            }
        }
        int[] iArr = new int[this.mDisabledFeatures.size()];
        for (int i3 = 0; i3 < this.mDisabledFeatures.size(); i3++) {
            iArr[i3] = this.mDisabledFeatures.get(i3).intValue();
        }
        this.mSidecar.init(iArr, this.mSingleFromMulti, this.mTalkbackEnabled, this.mShouldManagePreview, this.mDebugConsent);
        this.mEnrollmentStartTime = System.currentTimeMillis();
        this.mGazeFailCount = 0;
        this.mSidecar.startEnrollment();
    }

    @Override
    public void onEnrollmentError(int i, CharSequence charSequence) {
        CharSequence text;
        this.mSidecar.logEnrollmentEnded(i == 3 ? 0 : 2, false);
        if (i == 3) {
            text = getText(R.string.security_settings_face_enroll_error_timeout_dialog_message);
        } else {
            text = (i < 1000 && i != 4) ? getText(R.string.security_settings_face_enroll_error_generic_dialog_message) : charSequence;
        }
        getWindow().clearFlags(128);
        this.mPreviewFragment.onEnrollmentError(i, charSequence);
        if (i != 5) {
            showErrorDialog(text, i);
        }
    }

    @Override
    public void onEnrollmentProgressChange(int i, int i2) {
        Log.v("FaceEnrollEnrolling", "Steps: " + i + " Remaining: " + i2);
        this.mRemaining = i2;
        this.mPreviewFragment.onEnrollmentProgressChange(i, i2);
        if (i2 == 0) {
            if (this.mDidCommitPartialEnrollment) {
                this.mSidecar.logEnrollmentEnded(1, false);
                showPartialEnrollmentDialog();
                return;
            }
            this.mSidecar.logEnrollmentEnded(1, true);
            this.mHandler.postDelayed(new Runnable() {
                @Override
                public final void run() {
                    FaceEnrollEnrolling.this.onEnrollmentComplete();
                }
            }, 500L);
        }
    }

    private void onEnrollmentComplete() {
        this.mHelpController.clearHelp();
        if (!this.mUserManager.getUserInfo(this.mUserId).isManagedProfile()) {
            Settings.Secure.putIntForUser(getContentResolver(), "face_unlock_keyguard_enabled", 1, this.mUserId);
        }
        if (this.mRequireDiversity) {
            Intent intent = new Intent(this, FaceEnrollConfirmation.class);
            intent.putExtras(getIntent());
            startActivityForResult(intent, 2);
        }
    }

    private void showPartialEnrollmentDialog() {
        FaceEnrollDialogFactory.newBuilder(this).setTitle(R.string.security_settings_face_enroll_partial_title).setMessage(R.string.security_settings_face_enroll_partial_message).setPositiveButton(R.string.security_settings_face_enroll_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceEnrollEnrolling.this.lambda$showPartialEnrollmentDialog$3(dialogInterface, i);
            }
        }).setNegativeButton(R.string.security_settings_face_enroll_partial_start_over, new DialogInterface.OnClickListener() {
            @Override
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceEnrollEnrolling.this.lambda$showPartialEnrollmentDialog$4(dialogInterface, i);
            }
        }).setOnBackKeyListener(new FaceEnrollDialogFactory.OnBackKeyListener() {
            @Override
            public final void onBackKeyUp(DialogInterface dialogInterface, KeyEvent keyEvent) {
                FaceEnrollEnrolling.this.lambda$showPartialEnrollmentDialog$5(dialogInterface, keyEvent);
            }
        }).build().show();
    }

    private /* synthetic */ void lambda$showPartialEnrollmentDialog$3(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        onEnrollmentComplete();
    }

    private /* synthetic */ void lambda$showPartialEnrollmentDialog$4(DialogInterface dialogInterface, int i) {
        restartEnrollmentFromDialog(dialogInterface);
    }

    private /* synthetic */ void lambda$showPartialEnrollmentDialog$5(DialogInterface dialogInterface, KeyEvent keyEvent) {
        onEnrollmentComplete();
    }

    private void restartEnrollmentFromDialog(final DialogInterface dialogInterface) {
        FaceManager faceManager = (FaceManager) getSystemService(FaceManager.class);
        if (faceManager == null) {
            Log.e("FaceEnrollEnrolling", "Unable to remove face. Face manager was null!");
            return;
        }
        Face findNewlyEnrolledFace = findNewlyEnrolledFace();
        if (findNewlyEnrolledFace == null) {
            Log.e("FaceEnrollEnrolling", "Unable to remove face. No newly enrolled face found.");
        } else {
            new FaceUpdater(this, faceManager).remove(findNewlyEnrolledFace, this.mUserId, new FaceManager.RemovalCallback() {
                public void onRemovalError(Face face, int i, CharSequence charSequence) {
                    Log.e("FaceEnrollEnrolling", "Unable to remove face: " + face.getBiometricId() + " error: " + i + " " + ((Object) charSequence));
                    Toast.makeText(FaceEnrollEnrolling.this, charSequence, 0).show();
                    FaceEnrollEnrolling.this.finishFromDialog(dialogInterface, 2);
                }

                public void onRemovalSucceeded(Face face, int i) {
                    if (i == 0) {
                        FaceEnrollEnrolling.this.finishFromDialog(dialogInterface, 5);
                    }
                }
            });
        }
    }

    private void finishFromDialog(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        setResult(i);
        finish();
    }

    private GlifLayout getLayout() {
        return (GlifLayout) findViewById(R.id.setup_wizard_layout);
    }

    private void setHeaderText(int i) {
        CharSequence text = getLayout().getHeaderTextView().getText();
        CharSequence text2 = getText(i);
        if (text != text2) {
            getLayout().setHeaderText(text2);
            setTitle(text2);
        }
    }

    private void onButtonNegative(View view) {
        setResult(2);
        finish();
    }

    private void showErrorDialog(CharSequence charSequence, int i) {
        try {
            FaceErrorDialog.newInstance(charSequence, i, this.mRequireDiversity, this.mFromSetupWizard).show(getSupportFragmentManager(), FaceErrorDialog.class.getName());
        } catch (IllegalStateException unused) {
            Log.w("FaceEnrollEnrolling", "Can't show error after onSaveInstanceState, " + i);
        }
    }

    private Face findNewlyEnrolledFace() {
        if (this.mPreviouslyEnrolledFaces == null) {
            Log.w("FaceEnrollEnrolling", "Previously enrolled faces not set!");
        }
        List<Face> enrolledFaces = ((FaceManager) getSystemService(FaceManager.class)).getEnrolledFaces(this.mUserId);
        if (enrolledFaces == null || enrolledFaces.isEmpty()) {
            Log.e("FaceEnrollEnrolling", "Failed to find newly enrolled face. No faces enrolled.");
            return null;
        }
        Face face = null;
        for (Face face2 : enrolledFaces) {
            List<Face> list = this.mPreviouslyEnrolledFaces;
            if (list == null || !list.contains(face2)) {
                if (face != null) {
                    Log.e("FaceEnrollEnrolling", "Found more than one newly enrolled face.");
                    return null;
                }
                face = face2;
            }
        }
        if (face == null) {
            Log.e("FaceEnrollEnrolling", "No newly enrolled face found.");
        }
        return face;
    }
}
