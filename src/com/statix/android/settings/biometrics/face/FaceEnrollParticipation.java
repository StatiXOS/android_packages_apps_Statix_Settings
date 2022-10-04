package com.statix.android.settings.biometrics.face;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import androidx.fragment.app.FragmentActivity;
import androidx.window.R;
import com.android.internal.annotations.VisibleForTesting;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceEnrollParticipation extends FragmentActivity {
    private boolean mDebugConsent;
    private IBinder mFaceService;
    private boolean mNextLaunched;
    private FooterButton mPrimaryButton;
    private int mUserId;

    @Override
    protected void onCreate(Bundle bundle) {
        ThemeHelper.applyTheme(this);
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
        setContentView(R.layout.face_enroll_participation);
        FooterBarMixin footerBarMixin = (FooterBarMixin) getLayout().getMixin(FooterBarMixin.class);
        FooterButton build = new FooterButton.Builder(this).setText(R.string.face_enrolling_confirm_help_debug).setListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                FaceEnrollParticipation.this.onButtonPositive(view);
            }
        }).setButtonType(5).setTheme(R.style.SudGlifButton.Primary).build();
        this.mPrimaryButton = build;
        build.setEnabled(false);
        footerBarMixin.setPrimaryButton(this.mPrimaryButton);
        footerBarMixin.setSecondaryButton(new FooterButton.Builder(this).setText(R.string.face_enrolling_skip_help_debug).setListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                FaceEnrollParticipation.this.onButtonNegative(view);
            }
        }).setButtonType(7).setTheme(R.style.SudGlifButton.Secondary).build());
        this.mUserId = getIntent().getIntExtra("android.intent.extra.USER_ID", UserHandle.myUserId());
        ((CheckBox) findViewById(R.id.agree_to_participate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                FaceEnrollParticipation.this.lambda$onCreate$0(view);
            }
        });
        this.mDebugConsent = false;
        getApplicationContext();
        IBinder service = ServiceManager.getService("face");
        this.mFaceService = service;
        if (service == null) {
            Log.e("FaceEnrollParticipation", "Could not connect to face service");
        }
    }

    private /* synthetic */ void lambda$onCreate$0(View view) {
        this.mPrimaryButton.setEnabled(((CheckBox) view).isChecked());
    }

    @Override
    protected void onApplyThemeResource(Resources.Theme theme, int i, boolean z) {
        theme.applyStyle(R.style.SetupWizardPartnerResource, true);
        super.onApplyThemeResource(theme, i, z);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() || WizardManagerHelper.isAnySetupWizard(getIntent()) || this.mNextLaunched) {
            return;
        }
        setResult(3);
        finish();
    }

    private GlifLayout getLayout() {
        return (GlifLayout) findViewById(R.id.face_enroll_participation);
    }

    private void onButtonPositive(View view) {
        Log.d("FaceEnrollParticipation", "Participant agreed to data collection");
        sendDebugMessageToFaceService("--enable");
        this.mDebugConsent = true;
        Settings.Secure.putIntForUser(getContentResolver(), "biometric_debug_enabled", 1, this.mUserId);
        startEnrolling();
    }

    private void onButtonNegative(View view) {
        sendDebugMessageToFaceService("--disable");
        Settings.Secure.putIntForUser(getContentResolver(), "biometric_debug_enabled", 0, this.mUserId);
        startEnrolling();
    }

    @VisibleForTesting
    void sendDebugMessageToFaceService(String str) {
        FileOutputStream fileOutputStream;
        if (this.mFaceService != null) {
            FileOutputStream fileOutputStream2 = null;
            try {
                try {
                    try {
                        fileOutputStream = new FileOutputStream("/dev/null");
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (IOException e) {
                    e = e;
                }
                try {
                    this.mFaceService.dump(fileOutputStream.getFD(), new String[]{"--hal", str});
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        e = e2;
                        Log.e("FaceEnrollParticipation", "IOException", e);
                    }
                } catch (IOException e3) {
                    e = e3;
                    fileOutputStream2 = fileOutputStream;
                    e.printStackTrace();
                    Log.e("FaceEnrollParticipation", "IOException", e);
                    if (fileOutputStream2 == null) {
                        return;
                    }
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e4) {
                        e = e4;
                        Log.e("FaceEnrollParticipation", "IOException", e);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream2 = fileOutputStream;
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException e5) {
                            Log.e("FaceEnrollParticipation", "IOException", e5);
                        }
                    }
                    throw th;
                }
            } catch (RemoteException e6) {
                e6.printStackTrace();
            }
        }
    }

    private void startEnrolling() {
        Intent intent;
        this.mNextLaunched = true;
        boolean z = getResources().getBoolean(R.bool.config_face_enroll_use_traffic_light);
        if (z) {
            intent = new Intent("com.google.android.settings.future.biometrics.faceenroll.action.ENROLL");
        } else {
            intent = new Intent(this, FaceEnrollEnrolling.class);
        }
        if (z) {
            String string = getString(R.string.config_face_enroll_traffic_light_package);
            if (TextUtils.isEmpty(string)) {
                throw new IllegalStateException("Package name must not be empty");
            }
            intent.setPackage(string);
        }
        intent.putExtras(getIntent());
        intent.putExtra("debug_consent", this.mDebugConsent);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            setResult(i2, intent);
            finish();
        }
    }
}
