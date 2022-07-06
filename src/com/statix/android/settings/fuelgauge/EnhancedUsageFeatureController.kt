package com.statix.android.settings.fuelgauge

import android.content.Context;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.search.SearchIndexableRaw;

class EnhancedUsageFeatureController constructor(context: Context, preferenceKey: String)
        : BasePreferenceController(context, preferenceKey), PreferenceControllerMixin, Preference.OnPreferenceChangeListener {

    override fun updateState(preference: Preference) {
        val setting = mContext.getApplicationContext().getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE).getInt(KEY_GOOGLE_USAGE_FEATURE, 1)
        (preference as SwitchPreference).setChecked(setting == 1)
    }

    override fun getAvailabilityStatus() = AVAILABLE

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val usageFeature = when((newValue as Boolean)) {
            true -> 1
            false -> 0
        }
        mContext.applicationContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE).edit().putInt(KEY_GOOGLE_USAGE_FEATURE, usageFeature).apply()
        return true
    }

    override fun updateNonIndexableKeys(keys: List<String>) = Unit
    override fun updateRawDataToIndex(rawData: List<SearchIndexableRaw>) = Unit
    override fun updateDynamicRawDataToIndex(rawData: List<SearchIndexableRaw>) = Unit

    companion object {
         private val KEY_GOOGLE_USAGE_FEATURE = "enhanced_usage_feature"
    }
}
