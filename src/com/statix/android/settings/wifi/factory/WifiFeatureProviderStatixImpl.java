package com.statix.android.settings.wifi.factory;

import android.content.Context;
import com.android.settings.wifi.dpp.WifiDppQrCodeGeneratorFragment;
import com.android.settings.wifi.factory.WifiFeatureProvider;
import com.statix.android.settings.wifi.dpp.WifiDppQrCodeGeneratorFragmentStatixImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: WifiFeatureProviderGoogleImpl.kt */
/* loaded from: classes3.dex */
public final class WifiFeatureProviderStatixImpl extends WifiFeatureProvider {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WifiFeatureProviderStatixImpl(Context appContext) {
        super(appContext);
        Intrinsics.checkNotNullParameter(appContext, "appContext");
    }

    @Override // com.android.settings.wifi.factory.WifiFeatureProvider
    public WifiDppQrCodeGeneratorFragment getWifiDppQrCodeGeneratorFragment() {
        return new WifiDppQrCodeGeneratorFragmentStatixImpl();
    }
}
