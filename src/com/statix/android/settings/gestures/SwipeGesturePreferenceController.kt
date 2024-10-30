package com.statix.android.settings.gestures

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.android.settings.R
import com.android.settings.core.BasePreferenceController
import com.android.settingslib.PrimarySwitchPreference

class SwipeGesturePreferenceController(private val context: Context, preferenceKey: String) :
  BasePreferenceController(context, preferenceKey),
  Preference.OnPreferenceChangeListener,
  DefaultLifecycleObserver {

  private val assistActionSettingObserver =
    object : ContentObserver(Handler(Looper.getMainLooper())) {
      override fun onChange(selfChange: Boolean, uri: Uri?) {
        updateSummary()
      }
    }

  private val assistEnabledListener = object : ContentObserver(Handler(Looper.getMainLooper())) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
      updateChecked()
    }
  }

  private var primaryPreference: PrimarySwitchPreference? = null

  override fun getAvailabilityStatus() = AVAILABLE

  override fun displayPreference(screen: PreferenceScreen) {
    super.displayPreference(screen)
    primaryPreference = screen.findPreference(preferenceKey) as? PrimarySwitchPreference ?: return
    updateChecked()
    updateSummary()
  }

  override fun onPause(owner: LifecycleOwner) {
    context.contentResolver.unregisterContentObserver(assistActionSettingObserver)
  }

  override fun onResume(owner: LifecycleOwner) {
    context.contentResolver.registerContentObserver(
      Settings.Secure.getUriFor(ASSIST_ACTION_PREFERENCE),
      false,
      assistActionSettingObserver,
    )
  }

  override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
    val newChecked = newValue as Boolean
    Settings.Secure.putInt(
      preference.context.contentResolver,
      Settings.Secure.ASSIST_TOUCH_GESTURE_ENABLED,
      if (newChecked) 1 else 0,
    )
    return true
  }

  private fun updateChecked() {
    val checkedByDefault =
      context.resources.getBoolean(
        com.android.internal.R.bool.config_assistTouchGestureEnabledDefault
      )
    primaryPreference?.setChecked(
      Settings.Secure.getInt(
        context.contentResolver,
        Settings.Secure.ASSIST_TOUCH_GESTURE_ENABLED,
        if (checkedByDefault) 1 else 0,
      ) == 1
    )
  }

  private fun updateSummary() {
    val assistAction =
      Settings.Secure.getInt(context.contentResolver, ASSIST_ACTION_PREFERENCE, /*def =*/ -1)
    val summary =
      context.getString(
        when (assistAction) {
          0 -> R.string.screenshot_corner_gesture_summary
          else -> R.string.assistant_corner_gesture_summary
        }
      )
    primaryPreference?.setSummary(summary)
  }

  companion object {
    private const val ASSIST_ACTION_PREFERENCE = "assist_action"
  }
}
