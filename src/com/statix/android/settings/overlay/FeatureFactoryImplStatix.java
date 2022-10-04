package com.statix.android.settings.overlay;

import android.content.Context;

import com.android.settings.overlay.FeatureFactoryImpl;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.accounts.AccountFeatureProvider;
import com.android.settings.biometrics.face.FaceFeatureProvider;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.statix.android.settings.biometrics.face.FaceFeatureProviderGoogleImpl;

public final class FeatureFactoryImplStatix extends FeatureFactoryImpl {
    private AccountFeatureProvider mAccountFeatureProvider;
    private FaceFeatureProvider mFaceFeatureProvider;

    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        if (mAccountFeatureProvider == null) {
            mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return mAccountFeatureProvider;
    }

    @Override
    public FaceFeatureProvider getFaceFeatureProvider() {
        if (mFaceFeatureProvider == null) {
            mFaceFeatureProvider = new FaceFeatureProviderGoogleImpl();
        }
        return mFaceFeatureProvider;
    }

}
