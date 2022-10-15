/*
 * Copyright (C) 2020 The Proton AOSP Project
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
 * limitations under the License
 */

package com.statix.android.settings.elmyra

import android.os.Bundle
import android.content.SharedPreferences
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.ListPreference
import androidx.preference.SwitchPreference
import com.android.settings.widget.LabeledSeekBarPreference
import com.android.settings.widget.SeekBarPreference

import com.statix.android.systemui.elmyra.R
import com.statix.android.systemui.elmyra.getDePrefs
import com.statix.android.systemui.elmyra.PREFS_NAME
import com.statix.android.systemui.elmyra.getEnabled
import com.statix.android.systemui.elmyra.getSensitivity
import com.statix.android.systemui.elmyra.getAction
import com.statix.android.systemui.elmyra.getActionName
import com.statix.android.systemui.elmyra.getAllowScreenOff

class ElmyraSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var prefs: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(com.android.settings.R.xml.elmyra_settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.setStorageDeviceProtected()
        preferenceManager.sharedPreferencesName = PREFS_NAME

        prefs = context!!.getDePrefs()
        prefs.registerOnSharedPreferenceChangeListener(this)
        updateUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, key: String) {
        updateUi()
    }

    private fun updateUi() {
        // Enabled
        findPreference<SwitchPreference>(getString(R.string.pref_key_enabled))?.apply {
            setChecked(prefs.getEnabled(context))
        }

        // Sensitivity value
        findPreference<LabeledSeekBarPreference>(getString(R.string.pref_key_sensitivity))?.apply {
            progress = prefs.getSensitivity(context)
            setHapticFeedbackMode(SeekBarPreference.HAPTIC_FEEDBACK_MODE_ON_TICKS)
        }

        // Action value and summary
        findPreference<ListPreference>(getString(R.string.pref_key_action))?.apply {
            value = prefs.getAction(context)
            summary = prefs.getActionName(context)
        }

        // Screen state based on action
        findPreference<SwitchPreference>(getString(R.string.pref_key_allow_screen_off))?.apply {
            val screenForced = prefs.getBoolean(getString(R.string.pref_key_allow_screen_off_action_forced), false)
            setEnabled(!screenForced)
            if (screenForced) {
                setSummary(getString(com.android.settings.R.string.elmyra_setting_screen_off_blocked_summary))
                setPersistent(false)
                setChecked(false)
            } else {
                setSummary(getString(com.android.settings.R.string.elmyra_setting_screen_off_summary))
                setPersistent(true)
                setChecked(prefs.getAllowScreenOff(context))
            }
        }
    }
}
