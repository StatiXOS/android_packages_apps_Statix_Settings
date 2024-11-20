package com.statix.android.settings.overlay

import com.android.settings.accounts.AccountFeatureProvider
import com.android.settings.overlay.FeatureFactoryImpl
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl

class FeatureFactoryImplStatix : FeatureFactoryImpl() {
  override val accountFeatureProvider: AccountFeatureProvider by lazy {
    AccountFeatureProviderGoogleImpl()
    WifiFeatureProviderStatixImpl()
  }
}
