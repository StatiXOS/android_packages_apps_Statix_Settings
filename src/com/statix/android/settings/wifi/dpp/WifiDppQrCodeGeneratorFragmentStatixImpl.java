package com.statix.android.settings.wifi.dpp;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.settings.wifi.dpp.WifiDppQrCodeGeneratorFragment;
import com.statix.android.settings.R$id;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: WifiDppQrCodeGeneratorFragmentGoogleImpl.kt */
/* loaded from: classes3.dex */
public final class WifiDppQrCodeGeneratorFragmentStatixImpl extends WifiDppQrCodeGeneratorFragment {
    public static final Companion Companion = new Companion(null);

    @Override // com.android.settings.wifi.dpp.WifiDppQrCodeGeneratorFragment, com.android.settings.wifi.dpp.WifiDppQrCodeBaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkNotNullParameter(view, "view");
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(R$id.qr_code_fragment_container_view);
        if (findViewById == null) {
            return;
        }
        findViewById.setVisibility(0);
    }

    @Override // com.android.settings.wifi.dpp.WifiDppQrCodeGeneratorFragment
    protected void setQrCode() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkNotNullExpressionValue(childFragmentManager, "getChildFragmentManager(...)");
        int i = R$id.qr_code_fragment_container_view;
        if (childFragmentManager.findFragmentById(i) == null) {
            childFragmentManager.beginTransaction().setReorderingAllowed(true).replace(i, new MaterialShapeQrFragment()).commitNow();
        }
        Fragment findFragmentById = childFragmentManager.findFragmentById(i);
        Intrinsics.checkNotNull(findFragmentById, "null cannot be cast to non-null type com.statix.android.settings.wifi.dpp.MaterialShapeQrFragment");
        String mQrCode = this.mQrCode;
        Intrinsics.checkNotNullExpressionValue(mQrCode, "mQrCode");
        MaterialShapeQrFragment.updateQrCodeContent$default((MaterialShapeQrFragment) findFragmentById, mQrCode, null, 2, null);
    }

    /* compiled from: WifiDppQrCodeGeneratorFragmentGoogleImpl.kt */
    /* loaded from: classes3.dex */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
