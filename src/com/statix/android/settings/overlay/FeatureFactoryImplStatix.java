package com.statix.android.settings.overlay;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.settings.overlay.FeatureFactory;
import com.android.settings.overlay.FeatureFactoryImpl;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.fuelgauge.PowerUsageFeatureProviderImpl;
import com.android.settings.accounts.AccountFeatureProvider;

import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.google.android.settings.fuelgauge.PowerUsageFeatureProviderGoogleImpl;

public final class FeatureFactoryImplStatix extends FeatureFactoryImpl implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PowerUsageFeatureProvider mPowerUsageFeatureProvider;
    private AccountFeatureProvider mAccountFeatureProvider;

    private SharedPreferences mSharedPreferences;

    private static final String KEY_GOOGLE_USAGE_FEATURE = "enhanced_usage_feature";

    public FeatureFactoryImplStatix() {
        Context context = FeatureFactory.getAppContext();
        mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void refreshPowerUsageProvider() {
        Context context = FeatureFactory.getAppContext();
        boolean isPowerUsageFeatureGoogle = mSharedPreferences.getInt(KEY_GOOGLE_USAGE_FEATURE, 1) == 1;
        if (isPowerUsageFeatureGoogle) {
            mPowerUsageFeatureProvider = new PowerUsageFeatureProviderGoogleImpl(
                    context);
        } else {
            mPowerUsageFeatureProvider = new PowerUsageFeatureProviderImpl(
                    context);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_GOOGLE_USAGE_FEATURE)) {
            refreshPowerUsageProvider();
        }
    }

    @Override
    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(Context context) {
        boolean isPowerUsageFeatureGoogle = mSharedPreferences.getInt(KEY_GOOGLE_USAGE_FEATURE, 1) == 1;
        if (mPowerUsageFeatureProvider == null) {
            if (isPowerUsageFeatureGoogle) {
                mPowerUsageFeatureProvider = new PowerUsageFeatureProviderGoogleImpl(
                        context.getApplicationContext());
            } else {
                mPowerUsageFeatureProvider = new PowerUsageFeatureProviderImpl(
                        context.getApplicationContext());
            }
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
