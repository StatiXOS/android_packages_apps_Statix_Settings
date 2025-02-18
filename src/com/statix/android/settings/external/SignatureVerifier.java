package com.google.android.settings.external;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.util.ArrayUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class SignatureVerifier {
    private static final byte[] DEBUG_DIGEST_GMSCORE = {25, 117, -78, -15, 113, 119, -68, -119, -91, -33, -13, 31, -98, 100, -90, -54, -30, -127, -91, 61, -63, -47, -43, -101, 29, 20, Byte.MAX_VALUE, -31, -56, 42, -6, 0};
    private static final byte[] RELEASE_DIGEST_GMSCORE = {-16, -3, 108, 91, 65, 15, 37, -53, 37, -61, -75, 51, 70, -56, -105, 47, -82, 48, -8, -18, 116, 17, -33, -111, 4, Byte.MIN_VALUE, -83, 107, 45, 96, -37, -125};
    private static final byte[] DEBUG_DIGEST_TIPS = {-85, 24, -24, 44, 97, -94, -43, -117, -41, 24, 20, 119, -68, -97, 117, -88, 33, 77, 23, 98, 115, -112, 37, -84, 36, -111, 9, 20, 17, -72, 79, -77};
    private static final byte[] RELEASE_DIGEST_TIPS = {14, 68, 121, -2, 25, 61, 1, -51, 70, 33, 95, -52, -48, -39, 35, 61, -20, 119, -2, -94, 89, -5, -52, -97, 9, 33, 25, -11, 10, -125, 114, -27};
    private static final byte[] DEBUG_DIGEST_LAUNCHER = {75, 77, -102, -67, -24, -13, -42, -104, 117, 88, -57, 110, 38, 30, 111, -23, -45, -57, -52, 41, -98, -66, -14, 45, 86, -33, 99, 33, -37, -82, 53, 98};
    private static final byte[] RELEASE_DIGEST_LAUNCHER = {-88, 107, -37, 5, -97, 40, -14, 101, 22, 45, 100, -50, 108, -115, -105, 114, -112, 29, 34, 126, 116, 21, -127, -47, -16, 74, 94, -47, 50, -91, 116, -48};
    private static final byte[] DEBUG_DIGEST_SECURITY_HUB = {-42, 99, -61, 29, 42, 7, -22, -121, -5, 45, -103, 65, -78, -100, -63, 26, 29, -45, 69, 2, 120, -33, 97, 67, -9, 92, 47, -27, -126, -44, -27, -90};
    private static final byte[] RELEASE_DIGEST_SECURITY_HUB = {-72, 79, 119, 107, -46, -7, 110, -113, 33, -88, -26, 74, -66, 121, -6, 66, 79, -63, 44, Byte.MAX_VALUE, 34, 16, -101, -40, -19, -127, Byte.MIN_VALUE, 51, -17, -65, 16, -74};
    private static final byte[] DEBUG_DIGEST_ASSISTANT_DEV_APP = {25, 117, -78, -15, 113, 119, -68, -119, -91, -33, -13, 31, -98, 100, -90, -54, -30, -127, -91, 61, -63, -47, -43, -101, 29, 20, Byte.MAX_VALUE, -31, -56, 42, -6, 0};
    private static final byte[] ROUTER_TEST_APP = {16, 57, 56, -18, 69, 55, -27, -98, -114, -25, -110, -10, 84, 80, 79, -72, 52, 111, -58, -77, 70, -48, -69, -60, 65, 95, -61, 57, -4, -4, -114, -63};

    public static String verifyCallerIsAllowlisted(Context context, int i) {
        String isUidAllowlisted = isUidAllowlisted(context, i);
        if (TextUtils.isEmpty(isUidAllowlisted)) {
            throw new SecurityException("UID is not Google Signed");
        }
        return isUidAllowlisted;
    }

    private static String isUidAllowlisted(Context context, int i) {
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
        if (ArrayUtils.isEmpty(packagesForUid)) {
            return null;
        }
        for (String str : packagesForUid) {
            if (isPackageAllowlisted(context, str)) {
                return str;
            }
        }
        return null;
    }

    public static boolean isPackageAllowlisted(Context context, String str) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 64);
            String str2 = packageInfo.packageName;
            if (!verifyAllowlistedPackage(str2)) {
                Log.e("SignatureVerifier", "Package name: " + str2 + " is not allowlisted.");
                return false;
            }
            return isSignatureAllowlisted(packageInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SignatureVerifier", "Could not find package name.", e);
            return false;
        }
    }

    private static boolean isSignatureAllowlisted(PackageInfo packageInfo) {
        Signature[] signatureArr = packageInfo.signatures;
        if (signatureArr.length != 1) {
            Log.w("SignatureVerifier", "Package has more than one signature.");
            return false;
        }
        return isCertAllowlisted(packageInfo.packageName, signatureArr[0].toByteArray(), Build.IS_DEBUGGABLE);
    }

    private static boolean isCertAllowlisted(String str, byte[] bArr, boolean z) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(bArr);
            if (Log.isLoggable("SignatureVerifier", 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Checking cert for ");
                sb.append(z ? "debug" : "release");
                Log.d("SignatureVerifier", sb.toString());
            }
            return Arrays.equals(digest, getDigestBytes(str, z));
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("Failed to obtain SHA-256 digest impl.", e);
        }
    }

    private static boolean verifyAllowlistedPackage(String str) {
        boolean z;
        return "com.google.android.googlequicksearchbox".equals(str) || "com.google.android.gms".equals(str) || "com.google.android.apps.tips".equals(str) || "com.google.android.apps.nexuslauncher".equals(str) || "com.google.android.apps.security.securityhub".equals(str) || ((z = Build.IS_DEBUGGABLE) && "com.google.android.apps.search.assistant.surfaces.voice.devapp".equals(str)) || ((z && "com.google.android.settings.api.tester".equals(str)) || (z && "com.android.settingslib.router.testapp".equals(str)));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static byte[] getDigestBytes(String str, boolean z) {
        char c;
        switch (str.hashCode()) {
            case -1379536638:
                if (str.equals("com.android.settingslib.router.testapp")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -731289169:
                if (str.equals("com.google.android.apps.search.assistant.surfaces.voice.devapp")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -475355492:
                if (str.equals("com.google.android.apps.security.securityhub")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 40935373:
                if (str.equals("com.google.android.apps.tips")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 408846250:
                if (str.equals("com.google.android.apps.nexuslauncher")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            return z ? DEBUG_DIGEST_TIPS : RELEASE_DIGEST_TIPS;
        }
        if (c == 1) {
            return z ? DEBUG_DIGEST_LAUNCHER : RELEASE_DIGEST_LAUNCHER;
        }
        if (c == 2) {
            return z ? DEBUG_DIGEST_SECURITY_HUB : RELEASE_DIGEST_SECURITY_HUB;
        }
        if (c == 3) {
            return DEBUG_DIGEST_ASSISTANT_DEV_APP;
        }
        if (c != 4) {
            return z ? DEBUG_DIGEST_GMSCORE : RELEASE_DIGEST_GMSCORE;
        }
        return ROUTER_TEST_APP;
    }
}
