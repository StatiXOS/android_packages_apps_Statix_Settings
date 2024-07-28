package com.statix.android.settings.gestures;

import android.app.appsearch.AppSearchBatchResult;
import android.app.appsearch.AppSearchManager;
import android.app.appsearch.AppSearchResult;
import android.app.appsearch.BatchResultCallback;
import android.app.appsearch.GenericDocument;
import android.app.appsearch.GetByDocumentIdRequest;
import android.app.appsearch.GlobalSearchSession;
import android.app.appsearch.exceptions.AppSearchException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class NavigationSettingsGoogleSearchUtil {
    private static final String ENABLE_SETTINGS_OSE_CUSTOMIZATIONS =
            "ENABLE_SETTINGS_OSE_CUSTOMIZATIONS";
    private static final String GSA_PACKAGE = "com.google.android.googlequicksearchbox";
    private static final String SETTINGS_KEY = "selected_search_engine";
    private static final NavigationSettingsGoogleSearchUtil sInstance =
            new NavigationSettingsGoogleSearchUtil();

    public static NavigationSettingsGoogleSearchUtil getInstance(Context context) {
        updateIsOseRecoveryEnabledFromAppSearch(context);
        updateIsOmniAware(context);
        return sInstance;
    }

    public boolean isOmniSupported(Context context) {
        if (isOseGoogle(context)) {
            return true;
        }
        return isOmniAware(context) && isOseRecoveryEnabled(context);
    }

    public boolean isOseGoogle(Context context) {
        if (DeviceConfig.getBoolean("launcher", ENABLE_SETTINGS_OSE_CUSTOMIZATIONS, true)) {
            String osePackageName =
                    Settings.Secure.getString(context.getContentResolver(), SETTINGS_KEY);
            return TextUtils.isEmpty(osePackageName)
                    || TextUtils.equals(osePackageName, GSA_PACKAGE);
        }
        return true;
    }

    private boolean isOseRecoveryEnabled(Context context) {
        return context.getSharedPreferences("search_settings", 0)
                .getBoolean("is_ose_recovery_enabled", false);
    }

    private static void updateIsOseRecoveryEnabledFromAppSearch(Context context) {
        final GetByDocumentIdRequest build =
                new GetByDocumentIdRequest.Builder("omni").addIds("entry_point").build();
        final ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        final SharedPreferences sharedPreferences =
                context.getSharedPreferences("search_settings", 0);
        ((AppSearchManager) context.getSystemService(AppSearchManager.class))
                .createGlobalSearchSession(
                        newSingleThreadExecutor,
                        new Consumer() {
                            @Override
                            public final void accept(Object obj) {
                                NavigationSettingsGoogleSearchUtil
                                        .lambda$updateIsOseRecoveryEnabledFromAppSearch$0(
                                                build,
                                                newSingleThreadExecutor,
                                                sharedPreferences,
                                                (AppSearchResult) obj);
                            }
                        });
    }

    public static void lambda$updateIsOseRecoveryEnabledFromAppSearch$0(
            GetByDocumentIdRequest getByDocumentIdRequest,
            Executor executor,
            final SharedPreferences sharedPreferences,
            AppSearchResult appSearchResult) {
        if (appSearchResult.isSuccess()) {
            ((GlobalSearchSession) appSearchResult.getResultValue())
                    .getByDocumentId(
                            GSA_PACKAGE,
                            "OmniEntryPoint",
                            getByDocumentIdRequest,
                            executor,
                            new BatchResultCallback() {
                                @Override
                                public void onResult(AppSearchBatchResult appSearchBatchResult) {
                                    try {
                                        Optional findFirst =
                                                appSearchBatchResult
                                                        .getSuccesses()
                                                        .values()
                                                        .stream()
                                                        .findFirst();
                                        if (findFirst.isPresent()) {
                                            boolean propertyBoolean =
                                                    ((GenericDocument) findFirst.get())
                                                            .getPropertyBoolean(
                                                                    "isOseRecoveryEnabled");
                                            Log.d(
                                                    "NavigationSettingsGoogleSearchUtil",
                                                    "Fetched from AppSearch, isOseRecoveryEnabled ="
                                                            + " "
                                                            + propertyBoolean);
                                            sharedPreferences
                                                    .edit()
                                                    .putBoolean(
                                                            "is_ose_recovery_enabled",
                                                            propertyBoolean)
                                                    .apply();
                                        }
                                    } catch (Exception e) {
                                        Log.d(
                                                "NavigationSettingsGoogleSearchUtil",
                                                "Failed to fetch isOseRecoveryEnabled from"
                                                        + " AppSearch result",
                                                e);
                                    }
                                }

                                @Override
                                public void onSystemError(Throwable th) {
                                    Log.d(
                                            "NavigationSettingsGoogleSearchUtil",
                                            "Failed to fetch isOseRecoveryEnabled for AppSearch"
                                                    + " system error",
                                            th);
                                }
                            });
        } else {
            Log.d(
                    "NavigationSettingsGoogleSearchUtil",
                    "Failed to fetch isOseRecoveryEnabled from AppSearch, AppSearchException",
                    new AppSearchException(appSearchResult.getResultCode()));
        }
    }

    private boolean isOmniAware(Context context) {
        return context.getSharedPreferences("search_settings", 0)
                .getBoolean("is_omni_aware", false);
    }

    private static void updateIsOmniAware(final Context context) {
        final SharedPreferences sharedPreferences =
                context.getSharedPreferences("search_settings", 0);
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            try {
                                boolean z =
                                        context.getPackageManager()
                                                .getProperty("omni.AWARE", GSA_PACKAGE)
                                                .getBoolean();
                                sharedPreferences.edit().putBoolean("is_omni_aware", z).commit();
                                Log.d("NavigationSettingsGoogleSearchUtil", "isOmniAware=" + z);
                            } catch (PackageManager.NameNotFoundException e) {
                                sharedPreferences.edit().putBoolean("is_omni_aware", false).apply();
                                Log.d(
                                        "NavigationSettingsGoogleSearchUtil",
                                        "Failed to get isOmniAware",
                                        e);
                            }
                        });
    }
}
