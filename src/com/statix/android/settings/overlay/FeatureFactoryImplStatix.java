package com.statix.android.settings.overlay;

import android.content.Context;

import com.android.settings.accounts.AccountFeatureProvider;
import com.android.settings.fuelgauge.BatteryStatusFeatureProvider;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.overlay.FeatureFactoryImpl;

import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.statix.android.settings.fuelgauge.BatteryStatusFeatureProviderStatixImpl;
import com.statix.android.settings.fuelgauge.PowerUsageFeatureProviderStatixImpl;

public final class FeatureFactoryImplStatix extends FeatureFactoryImpl {

    private AccountFeatureProvider mAccountFeatureProvider;
    private BatteryStatusFeatureProvider mBatteryStatusFeatureProvider;
    private PowerUsageFeatureProvider mPowerUsageFeatureProvider;

    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        if (mAccountFeatureProvider == null) {
            mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return mAccountFeatureProvider;
    }

    @Override
    public BatteryStatusFeatureProvider getBatteryStatusFeatureProvider(Context context) {
        if (mBatteryStatusFeatureProvider == null) {
            mBatteryStatusFeatureProvider = new BatteryStatusFeatureProviderStatixImpl(context);
        }
        return mBatteryStatusFeatureProvider;
    }

    @Override
    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(Context context) {
        if (mPowerUsageFeatureProvider == null) {
            mPowerUsageFeatureProvider = new PowerUsageFeatureProviderStatixImpl(context);
        }
        return mPowerUsageFeatureProvider;
    }
}
