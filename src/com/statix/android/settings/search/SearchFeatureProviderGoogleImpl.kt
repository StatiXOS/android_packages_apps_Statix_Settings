package com.google.android.settings.search

import android.content.Context

import com.android.settings.search.SearchFeatureProviderImpl

import com.google.android.settings.external.SignatureVerifier

/* compiled from: SearchFeatureProviderGoogleImpl.kt */
/* loaded from: classes3.dex */
class SearchFeatureProviderGoogleImpl : SearchFeatureProviderImpl() {
    override fun isSignatureAllowlisted(context: Context, callerPackage: String): Boolean =
        SignatureVerifier.isPackageAllowlisted(context, callerPackage)
}
