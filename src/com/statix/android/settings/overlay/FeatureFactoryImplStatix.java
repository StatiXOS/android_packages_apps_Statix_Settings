package com.statix.android.settings.overlay;

import android.content.Context;

import com.android.settings.accounts.AccountFeatureProvider;
import com.android.settings.fuelgauge.BatteryStatusFeatureProvider;
import com.android.settings.overlay.FeatureFactoryImpl;

import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.statix.android.settings.fuelgauge.BatteryStatusFeatureProviderStatixImpl;

public final class FeatureFactoryImplStatix extends FeatureFactoryImpl {

    private AccountFeatureProvider mAccountFeatureProvider;
    private BatteryStatusFeatureProvider mBatteryStatusFeatureProvider;

    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        return mAccountFeatureProvider;
    }
}
