package com.statix.android.settings.gestures;

import android.content.Context;
import android.provider.DeviceConfig;
import android.provider.Settings;

import com.android.settings.core.TogglePreferenceController;

public class GestureNavigationSettingsGoogleSearchController extends TogglePreferenceController {
    private final NavigationSettingsGoogleSearchUtil mNavigationCheckUtil;

    @Override
    public int getSliceHighlightMenuRes() {
        return 0;
    }

    @Override
    public boolean isSliceable() {
        return false;
    }

    public GestureNavigationSettingsGoogleSearchController(Context context, String str) {
        super(context, str);
        mNavigationCheckUtil = NavigationSettingsGoogleSearchUtil.getInstance(context);
    }

    @Override
    public boolean isChecked() {
        return Settings.Secure.getInt(
                        mContext.getContentResolver(),
                        "search_press_hold_nav_handle_enabled",
                        mContext.getResources()
                                        .getBoolean(
                                                com.android.internal.R.bool
                                                        .config_searchPressHoldNavHandleEnabledDefault)
                                ? 1
                                : 0)
                == 1;
    }

    @Override
    public boolean setChecked(boolean isSearchPressHoldNavHandleEnabled) {
        return Settings.Secure.putInt(
                mContext.getContentResolver(),
                "search_press_hold_nav_handle_enabled",
                isSearchPressHoldNavHandleEnabled ? 1 : 0);
    }

    @Override
    public int getAvailabilityStatus() {
        return (isFlagEnabled() && mNavigationCheckUtil.isOmniSupported(mContext))
                ? AVAILABLE
                : UNSUPPORTED_ON_DEVICE;
    }

    private boolean isFlagEnabled() {
        return DeviceConfig.getBoolean("launcher", "press_hold_nav_handle_to_search", true);
    }
}
