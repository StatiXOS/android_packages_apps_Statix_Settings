package com.statix.android.settings.notification;

import com.android.settings.SettingsActivity;

public class ClearCallingSettingsActivity extends SettingsActivity {
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return ClearCallingSettings.class.getName().equals(fragmentName);
    }
}
