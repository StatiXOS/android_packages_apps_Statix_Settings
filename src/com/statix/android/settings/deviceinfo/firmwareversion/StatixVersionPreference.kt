/*
 * Copyright (C) 2024 The Android Open Source Project
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

package com.statix.android.settings.deviceinfo.firmwareversion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemProperties
import androidx.preference.Preference
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.preference.PreferenceBinding
import com.statix.android.settings.R

class StatixVersionPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    val KEY_STATIX_VERSION_PROP = "ro.statix.version"

    override val key: String
        get() = "statix_version"

    override val title: Int
        get() = R.string.statix_version

    override fun intent(context: Context): Intent? =
        Intent(Intent.ACTION_VIEW).setData(Uri.parse(context.getString(R.string.statix_uri)))

    override fun isAvailable(context: Context) = true

    override fun getSummary(context: Context) =
        SystemProperties.get(
            KEY_STATIX_VERSION_PROP,
            context.getString(com.android.settings.R.string.unknown),
        )

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        preference.isCopyingEnabled = true
    }
}
