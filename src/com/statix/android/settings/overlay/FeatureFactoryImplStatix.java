package com.statix.android.settings.overlay;

import android.content.Context;

import com.android.settings.overlay.FeatureFactoryImpl;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.accounts.AccountFeatureProvider;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.google.android.settings.fuelgauge.PowerUsageFeatureProviderGoogleImpl;

public final class FeatureFactoryImplStatix extends FeatureFactoryImpl {

    private PowerUsageFeatureProvider mPowerUsageFeatureProvider;
    private AccountFeatureProvider mAccountFeatureProvider;

    @Override
    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(Context context) {
        if (mPowerUsageFeatureProvider == null) {
            mPowerUsageFeatureProvider = new PowerUsageFeatureProviderGoogleImpl(
                    context.getApplicationContext());
        }
        return mPowerUsageFeatureProvider;
    }

    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        if (mAccountFeatureProvider == null) {
            mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return mAccountFeatureProvider;
    }

}
