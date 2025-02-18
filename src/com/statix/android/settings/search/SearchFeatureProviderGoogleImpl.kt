package com.statix.android.settings.search

import android.content.Context
import com.android.settings.search.SearchFeatureProviderImpl
import com.statix.android.settings.external.SignatureVerifier

class SearchFeatureProviderGoogleImpl : SearchFeatureProviderImpl() {
  override fun isSignatureAllowlisted(context: Context, callerPackage: String): Boolean =
    SignatureVerifier.isPackageAllowlisted(context, callerPackage)
}
