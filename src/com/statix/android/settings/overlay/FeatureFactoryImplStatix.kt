package com.statix.android.settings.overlay

import android.content.Context

import com.android.settings.accounts.AccountFeatureProvider
import com.android.settings.overlay.FeatureFactoryImpl
import com.android.settings.search.SearchFeatureProvider

import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl
import com.google.android.settings.search.SearchFeatureProviderGoogleImpl

class FeatureFactoryImplStatix : FeatureFactoryImpl() {
    override val accountFeatureProvider: AccountFeatureProvider by lazy {
        AccountFeatureProviderGoogleImpl()
    }

    override val searchFeatureProvider: SearchFeatureProvider = SearchFeatureProviderGoogleImpl
}
