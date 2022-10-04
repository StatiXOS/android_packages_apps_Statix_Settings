package com.statix.android.settings.biometrics.face;

import android.util.SparseIntArray;

public class Debouncer {
    private final int[] mDebounceWindow;
    private final SparseIntArray mStateConfiguration;

    public Debouncer(SparseIntArray sparseIntArray) {
        int i = 0;
        for (int i2 = 0; i2 < sparseIntArray.size(); i2++) {
            if (sparseIntArray.valueAt(i2) > i) {
                i = sparseIntArray.valueAt(i2);
            }
        }
        this.mDebounceWindow = new int[i];
        this.mStateConfiguration = sparseIntArray;
    }

    public Debouncer(int i) {
        this.mDebounceWindow = new int[i];
        this.mStateConfiguration = null;
    }

    public void reset() {
        int i = 0;
        while (true) {
            int[] iArr = this.mDebounceWindow;
            if (i < iArr.length) {
                iArr[i] = 0;
                i++;
            } else {
                return;
            }
        }
    }

    public void updateBuffer(int i) {
        int i2 = 1;
        while (true) {
            int[] iArr = this.mDebounceWindow;
            if (i2 < iArr.length) {
                iArr[i2 - 1] = iArr[i2];
                i2++;
            } else {
                iArr[iArr.length - 1] = i;
                return;
            }
        }
    }

    public boolean passesDebounce(int i) {
        SparseIntArray sparseIntArray = this.mStateConfiguration;
        int length = sparseIntArray != null ? sparseIntArray.get(i, 0) : this.mDebounceWindow.length;
        int length2 = this.mDebounceWindow.length - 1;
        while (true) {
            int[] iArr = this.mDebounceWindow;
            if (length2 >= iArr.length - length) {
                if (iArr[length2] != i) {
                    return false;
                }
                length2--;
            } else {
                return true;
            }
        }
    }
}
