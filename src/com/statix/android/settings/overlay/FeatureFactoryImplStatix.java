package com.statix.android.settings.overlay;

import android.content.Context;

import com.android.settings.overlay.FeatureFactoryImpl;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.accounts.AccountFeatureProvider;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;

public final class FeatureFactoryImplStatix extends FeatureFactoryImpl {

    private AccountFeatureProvider mAccountFeatureProvider;

    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        if (mAccountFeatureProvider == null) {
            mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return mAccountFeatureProvider;
    }

}
