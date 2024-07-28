package com.statix.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.DeviceConfig;
import android.provider.Settings;
import com.android.settings.core.TogglePreferenceController;

public class ButtonNavigationSettingsGoogleSearchController extends TogglePreferenceController {
    private final NavigationSettingsGoogleSearchUtil mNavigationCheckUtil;

    @Override
    public Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override
    public IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return 0;
    }

    @Override
    public boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override
    public boolean isSliceable() {
        return false;
    }

    @Override
    public boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ButtonNavigationSettingsGoogleSearchController(Context context, String str) {
        super(context, str);
        this.mNavigationCheckUtil = NavigationSettingsGoogleSearchUtil.getInstance(context);
    }

    ButtonNavigationSettingsGoogleSearchController(Context context, String str, NavigationSettingsGoogleSearchUtil navigationSettingsGoogleSearchUtil) {
        super(context, str);
        this.mNavigationCheckUtil = navigationSettingsGoogleSearchUtil;
    }

    @Override
    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "search_long_press_home_enabled", this.mContext.getResources().getBoolean(com.android.internal.R.bool.config_searchLongPressHomeEnabledDefault) ? 1 : 0) == 1;
    }

    @Override
    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "search_long_press_home_enabled", z ? 1 : 0);
    }

    @Override
    public int getAvailabilityStatus() {
        return (isFlagEnabled() && this.mNavigationCheckUtil.isOmniSupported(this.mContext)) ? 0 : 3;
    }

    private boolean isFlagEnabled() {
        return DeviceConfig.getBoolean("launcher", "long_press_home_button_to_search", true);
    }
}
