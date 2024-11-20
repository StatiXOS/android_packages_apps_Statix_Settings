package com.statix.android.settings.overlay

import com.android.settings.accounts.AccountFeatureProvider
import com.android.settings.overlay.FeatureFactoryImpl
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl
import com.statix.android.settings.wifi.factory.WifiFeatureProviderStatixImpl

class FeatureFactoryImplStatix : FeatureFactoryImpl() {
  override val accountFeatureProvider: AccountFeatureProvider by lazy {
    AccountFeatureProviderGoogleImpl()
  }
  override val wifiFeatureProvider by lazy { WifiFeatureProviderStatixImpl(appContext) }
}
