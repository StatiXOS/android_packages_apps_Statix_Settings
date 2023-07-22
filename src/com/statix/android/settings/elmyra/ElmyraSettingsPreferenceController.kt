package com.statix.android.settings.elmyra

import android.content.Context
import com.android.settings.R
import com.android.settings.core.BasePreferenceController
import com.statix.android.systemui.elmyra.getActionName
import com.statix.android.systemui.elmyra.getEnabled

class ElmyraSettingsPreferenceController(context: Context) :
    BasePreferenceController(context, KEY_ELMYRA_SETTING) {

  override fun getAvailabilityStatus(): Int {
    return if (mContext.packageManager.hasSystemFeature("android.hardware.context_hub") &&
        mContext.packageManager.hasSystemFeature("android.hardware.sensor.assist")) {
      AVAILABLE
    } else {
      UNSUPPORTED_ON_DEVICE
    }
  }

  override fun getSummary(): CharSequence {
    return if (getEnabled(mContext)) {
      mContext.getString(R.string.elmyra_settings_entry_summary_on, getActionName(mContext))
    } else {
      mContext.getString(R.string.elmyra_settings_entry_summary_off)
    }
  }

  companion object {
    private const val KEY_ELMYRA_SETTING = "gesture_active_edge_summary"
  }
}
