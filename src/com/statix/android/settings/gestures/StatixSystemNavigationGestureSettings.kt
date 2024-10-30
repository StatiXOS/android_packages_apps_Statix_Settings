package com.statix.android.settings.gestures

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.android.settings.core.BasePreferenceController
import com.android.settings.core.PreferenceControllerListHelper
import com.android.settings.gestures.SystemNavigationGestureSettings
import com.android.settingslib.core.lifecycle.Lifecycle

class StatixSystemNavigationGestureSettings : SystemNavigationGestureSettings() {

  private val controllers = mutableListOf<BasePreferenceController>()

  override fun onAttach(context: Context) {
    super.onAttach(context)
    // Load preference controllers from xml definition
    val controllersFromXml: List<BasePreferenceController> =
      PreferenceControllerListHelper.getPreferenceControllersFromXml(
        context,
        getPreferenceScreenResId(),
      )

    // And wire up with lifecycle.
    val lifecycle: Lifecycle = getSettingsLifecycle()
    controllersFromXml.forEach { controller ->
      if (controller is LifecycleObserver) {
        lifecycle.addObserver(controller)
      }
    }
    controllers.addAll(controllersFromXml)
  }
}
