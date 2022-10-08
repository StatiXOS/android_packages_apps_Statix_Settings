package com.google.android.settings.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import com.android.settings.accounts.AccountFeatureProvider;
/* loaded from: classes2.dex */
public class AccountFeatureProviderGoogleImpl implements AccountFeatureProvider {
    @Override // com.android.settings.accounts.AccountFeatureProvider
    public String getAccountType() {
        return "com.google";
    }

    @Override // com.android.settings.accounts.AccountFeatureProvider
    public Account[] getAccounts(Context context) {
        return AccountManager.get(context).getAccountsByType("com.google");
    }
}
