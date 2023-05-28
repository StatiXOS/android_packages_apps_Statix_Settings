package com.statix.android.settings.notification;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.PreferenceScreen;
import androidx.window.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.HelpUtils;
import com.android.settingslib.widget.FooterPreference;

public class ClearCallingFooterPreferenceController extends BasePreferenceController {
    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    public ClearCallingFooterPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        FooterPreference footerPreference = (FooterPreference) preferenceScreen.findPreference(getPreferenceKey());
        final String string = mContext.getString(R.string.clear_calling_footer_learn_more_link);
        if (footerPreference == null || TextUtils.isEmpty(string)) {
            return;
        }
        footerPreference.setLearnMoreAction(new View.OnClickListener() { // from class: com.google.android.settings.notification.ClearCallingFooterPreferenceController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                mContext.startActivity(HelpUtils.getHelpIntent(mContext, mPreferenceKey, ""));
            }
        });
    }
}
