package com.statix.android.settings.notification;

import androidx.window.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class ClearCallingSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.clear_calling_settings);

    @Override // com.android.settings.dashboard.DashboardFragment
    protected String getLogTag() {
        return "ClearCallingSettings";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1929;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    protected int getPreferenceScreenResId() {
        return R.xml.clear_calling_settings;
    }
}
