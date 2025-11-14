/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.statix.android.settings;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.settings.SettingsApplication;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.metadata.FixedArrayMap;
import com.android.settingslib.metadata.PreferenceScreenMetadataFactory;

import com.statix.android.settings.overlay.FeatureFactoryImplStatix;

/** Settings application which sets up activity embedding rules for the large screen device. */
public class StatixSettingsApplication extends SettingsApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        FeatureFactory.setFactory(this, getFeatureFactory());
    }

    /** Returns the factories of preference screen metadata. */
    @Override
    protected FixedArrayMap<String, PreferenceScreenMetadataFactory> preferenceScreenFactories() {
        FixedArrayMap preferenceScreenFactories = super.preferenceScreenFactories();
        FixedArrayMap fixedArrayMap = StatixSettingsScreenCollector.get();
        fixedArrayMap.getClass();
        return preferenceScreenFactories.merge(fixedArrayMap);
    }

    @Override
    @NonNull
    protected FeatureFactory getFeatureFactory() {
        return new FeatureFactoryImplStatix();
    }
}
