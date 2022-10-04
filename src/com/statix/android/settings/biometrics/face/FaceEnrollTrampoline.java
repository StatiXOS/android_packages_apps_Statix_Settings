package com.statix.android.settings.biometrics.face;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.window.R;

public class FaceEnrollTrampoline extends FragmentActivity {
    private Intent mExtras;
    private boolean mFirstTime = true;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mExtras = getIntent();
    }

    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 4) {
            Intent intent2 = new Intent(this.mExtras);
            intent2.putExtra("accessibility_diversity", false);
            intent2.putExtra("from_multi_timeout", true);
            startEnrollActivity(intent2);
        } else if (i2 == 5) {
            startEnrollActivity(this.mExtras);
        } else {
            setResult(i2, intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mFirstTime) {
            this.mFirstTime = false;
            if (Build.IS_ENG || Build.IS_USERDEBUG) {
                Intent intent = new Intent(this, FaceEnrollParticipation.class);
                intent.putExtras(this.mExtras);
                startActivityForResult(intent, 2);
                return;
            }
            startEnrollActivity(this.mExtras);
        }
    }

    private void startEnrollActivity(Intent intent) {
        Intent intent2;
        boolean z = getResources().getBoolean(R.bool.config_face_enroll_use_traffic_light);
        if (z) {
            intent2 = new Intent("com.statix.android.settings.future.biometrics.faceenroll.action.ENROLL");
        } else {
            intent2 = new Intent(this, FaceEnrollEnrolling.class);
        }
        if (z) {
            String string = getString(R.string.config_face_enroll_traffic_light_package);
            if (TextUtils.isEmpty(string)) {
                throw new IllegalStateException("Package name must not be empty");
            }
            intent2.setPackage(string);
        }
        intent2.putExtras(intent);
        startActivityForResult(intent2, 1);
    }
}
