package com.statix.android.settings;

import com.statix.android.settings.fuelgauge.batterysaver.SmartPixels;

public class StatixSettingsGateway {

    /**
     * A list of fragment that can be hosted by StatixSettingsActivity. SettingsActivity will throw
     * a security exception if the fragment it needs to display is not in this list.
     */
    public static final String[] ENTRY_FRAGMENTS = {
        SmartPixels.class.getName(),
    };
}
