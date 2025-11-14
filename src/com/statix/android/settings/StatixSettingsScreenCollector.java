package com.statix.android.settings;

import com.android.settingslib.metadata.FixedArrayMap;
import com.android.settingslib.metadata.FixedArrayMap.OrderedInitializer;
import com.android.settingslib.metadata.PreferenceScreenMetadataFactory;

import com.statix.android.settings.deviceinfo.firmwareversion.StatixFirmwareVersionScreen;

public abstract class StatixSettingsScreenCollector {
    public static FixedArrayMap get() {
        return new FixedArrayMap(
                1,
                obj -> {
                    init((OrderedInitializer) obj);
                });
    }

    private static void init(OrderedInitializer orderedInitializer) {
        orderedInitializer.put(
                "firmware_version",
                (PreferenceScreenMetadataFactory)
                        context -> {
                            return new StatixFirmwareVersionScreen();
                        });
    }
}
