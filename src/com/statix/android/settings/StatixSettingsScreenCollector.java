package com.statix.android.settings;

import com.android.settingslib.metadata.FixedArrayMap;
import com.android.settingslib.metadata.FixedArrayMap.OrderedInitializer;
import com.android.settingslib.metadata.PreferenceScreenMetadataFactory;

import com.statix.android.settings.deviceinfo.firmwareversion.StatixFirmwareVersionScreen;

public abstract class StatixSettingsScreenCollector {
    public static FixedArrayMap get() {
        return new FixedArrayMap(
                4,
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

        orderedInitializer.put(
                "adaptive_battery_entry",
                (PreferenceScreenMetadataFactory) AdaptiveBatteryScreen::new);

        orderedInitializer.put(
                "battery_saver_schedule",
                (PreferenceScreenMetadataFactory)
                        context -> {
                            return new BatterySaverScheduleScreen();
                        });

        orderedInitializer.put(
                "battery_saver_screen",
                (PreferenceScreenMetadataFactory)
                        context -> {
                            return new BatterySaverGoogleScreen();
                        });
    }
}
