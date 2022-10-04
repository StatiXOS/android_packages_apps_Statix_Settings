package com.statix.android.settings.biometrics.face;

import android.content.Context;
import androidx.window.R;
import com.android.settings.biometrics.face.FaceFeatureProvider;

public class FaceFeatureProviderGoogleImpl implements FaceFeatureProvider {
    @Override
    public boolean isAttentionSupported(Context context) {
        return context.getResources().getBoolean(R.bool.config_face_settings_attention_supported);
    }
}
