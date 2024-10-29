package com.statix.android.settings.gestures

import android.content.Context
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.android.settings.R
import com.android.settings.core.BasePreferenceController
import com.android.settingslib.PrimarySwitchPreference

class SwipeGesturePreferenceController(context: Context, preferenceKey: String) :
  BasePreferenceController(context, preferenceKey), Preference.OnPreferenceChangeListener {

  override fun getAvailabilityStatus() = AVAILABLE

  override fun displayPreference(screen: PreferenceScreen) {
    super.displayPreference(screen)
    val preference = screen.findPreference(preferenceKey) as? PrimarySwitchPreference ?: return
    val checkedByDefault =
      preference.context.resources.getBoolean(
        com.android.internal.R.bool.config_assistTouchGestureEnabledDefault
      )
    preference.apply {
      summaryProvider =
        object : Preference.SummaryProvider<PrimarySwitchPreference> {
          override fun provideSummary(preference: PrimarySwitchPreference): CharSequence {
            val assistAction =
              Settings.Secure.getInt(
                preference.context.contentResolver,
                ASSIST_ACTION_PREFERENCE,
                /*def =*/ -1,
              )
            return preference.context.getString(
              when (assistAction) {
                0 -> R.string.screenshot_corner_gesture_summary
                else -> R.string.assistant_corner_gesture_summary
              }
            )
          }
        }
      setChecked(
        Settings.Secure.getInt(
          context.contentResolver,
          Settings.Secure.ASSIST_TOUCH_GESTURE_ENABLED,
          if (checkedByDefault) 1 else 0,
        ) == 1
      )
    }
  }

  override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
    val newChecked = newValue as Boolean
    (preference as PrimarySwitchPreference).setChecked(newChecked)
    Settings.Secure.putInt(
      preference.context.contentResolver,
      Settings.Secure.ASSIST_TOUCH_GESTURE_ENABLED,
      if (newChecked) 1 else 0,
    )
    return true
  }

  companion object {
    private const val ASSIST_ACTION_PREFERENCE = "assist_action"
  }
}
