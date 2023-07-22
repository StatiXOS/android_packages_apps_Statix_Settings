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

import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.provider.Settings
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.android.settings.widget.LabeledSeekBarPreference
import com.android.settings.widget.SeekBarPreference
import com.statix.android.systemui.elmyra.R
import com.statix.android.systemui.elmyra.getAction
import com.statix.android.systemui.elmyra.getActionName
import com.statix.android.systemui.elmyra.getAllowScreenOff
import com.statix.android.systemui.elmyra.getBoolean
import com.statix.android.systemui.elmyra.getEnabled
import com.statix.android.systemui.elmyra.getSensitivity
import com.statix.android.systemui.elmyra.uriForAction
import com.statix.android.systemui.elmyra.uriForEnabled
import com.statix.android.systemui.elmyra.uriForScreenOff
import com.statix.android.systemui.elmyra.uriForSensitivity

class ElmyraSettingsFragment : PreferenceFragmentCompat() {

  private val settingsObserver = SettingsObserver(Handler(Looper.getMainLooper()))

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(com.android.settings.R.xml.elmyra_settings, rootKey)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    updateUi()
    context!!
        .contentResolver
        .registerContentObserver(uriForAction, false, settingsObserver, UserHandle.USER_CURRENT)
    context!!
        .contentResolver
        .registerContentObserver(uriForEnabled, false, settingsObserver, UserHandle.USER_CURRENT)
    context!!
        .contentResolver
        .registerContentObserver(uriForScreenOff, false, settingsObserver, UserHandle.USER_CURRENT)
    context!!
        .contentResolver
        .registerContentObserver(
            uriForSensitivity, false, settingsObserver, UserHandle.USER_CURRENT)
    findPreference<LabeledSeekBarPreference>(context!!.getString(R.string.pref_key_sensitivity))
        ?.apply {
          setOnPreferenceChangeStopListener(
              object : Preference.OnPreferenceChangeListener {
                override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
                  val progress = newValue as Int
                  Settings.System.putInt(
                      context.contentResolver,
                      context!!.getString(R.string.pref_key_sensitivity),
                      progress)
                  return true
                }
              })
        }
  }

  override fun onDestroy() {
    super.onDestroy()
    context!!.contentResolver.unregisterContentObserver(settingsObserver)
  }

  inner class SettingsObserver(handler: Handler) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri) {
      updateUi()
    }
  }

  private fun updateUi() {
    // Enabled
    findPreference<SwitchPreference>(context!!.getString(R.string.pref_key_enabled))?.apply {
      setChecked(getEnabled(context))
    }

    // Sensitivity value
    findPreference<LabeledSeekBarPreference>(context!!.getString(R.string.pref_key_sensitivity))
        ?.apply {
          progress = getSensitivity(context)
          setHapticFeedbackMode(SeekBarPreference.HAPTIC_FEEDBACK_MODE_ON_TICKS)
        }

    // Action value and summary
    findPreference<ListPreference>(context!!.getString(R.string.pref_key_action))?.apply {
      value = getAction(context)
      summary = getActionName(context)
    }

    // Screen state based on action
    findPreference<SwitchPreference>(context!!.getString(R.string.pref_key_allow_screen_off))
        ?.apply {
          val screenForced =
              getBoolean(
                  context.contentResolver,
                  getString(R.string.pref_key_allow_screen_off_action_forced),
                  false)
          setEnabled(!screenForced)
          if (screenForced) {
            setSummary(
                getString(com.android.settings.R.string.elmyra_setting_screen_off_blocked_summary))
            setPersistent(false)
            setChecked(false)
          } else {
            setSummary(
                context.getString(com.android.settings.R.string.elmyra_setting_screen_off_summary))
            setPersistent(true)
            setChecked(getAllowScreenOff(context))
          }
        }
  }
}
