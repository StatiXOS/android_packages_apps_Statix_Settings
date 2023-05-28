package com.statix.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.android.settings.R;
import com.android.settings.widget.SettingsMainSwitchPreferenceController;

public class ClearCallingTogglePreferenceController extends SettingsMainSwitchPreferenceController {
    private static final String CCA_ENABLED_FLAG = "CcaConfig__is_enabled";
    private static final String CCA_STATUS_KEY = "cca_pixel_enabled";
    private static final String CCA_STATUS_OFF = "cca_pixel_enabled=false";
    static final String CCA_STATUS_ON = "cca_pixel_enabled=true";
    private final AudioManager mAudioManager;

    public ClearCallingTogglePreferenceController(Context context, String str) {
        super(context, str);
        mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public int getSliceHighlightMenuRes() {
        return R.string.menu_key_sound;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        if (mContext.getResources().getBoolean(R.bool.config_clear_calling_enabled)) {
            return 0;
        }
        return 5;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return mAudioManager.getParameters(CCA_STATUS_KEY).contains(CCA_STATUS_ON);
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        mAudioManager.setParameters(z ? CCA_STATUS_ON : CCA_STATUS_OFF);
        return true;
    }
}
