package com.statix.android.settings.notification;

import com.android.settings.SettingsActivity;

public class ClearCallingSettingsActivity extends SettingsActivity {
    @Override // com.android.settings.SettingsActivity
    protected boolean isValidFragment(String str) {
        return ClearCallingSettings.class.getName().equals(str);
    }
}
