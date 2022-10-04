package com.statix.android.settings.biometrics.face;

import com.android.internal.util.FrameworkStatsLog;

public class FaceUtils {
    public static final int[] CENTER_BUCKETS = {1108, 1112, 1113, 1114, 1118};

    public static void writeVendorLog(int i, int i2) {
        FrameworkStatsLog.write(87, 4, i, false, 1, 0, 22, i2, false, -1, 0, 0, false);
    }

    public static boolean isOneOfCenterBuckets(int i) {
        int i2 = 0;
        while (true) {
            int[] iArr = CENTER_BUCKETS;
            if (i2 < iArr.length) {
                if (i == iArr[i2]) {
                    return true;
                }
                i2++;
            } else {
                return false;
            }
        }
    }
}
