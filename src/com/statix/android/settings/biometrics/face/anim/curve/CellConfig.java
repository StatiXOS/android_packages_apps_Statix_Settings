package com.statix.android.settings.biometrics.face.anim.curve;

import android.graphics.Path;

public class CellConfig {
    final boolean mFlipVertical;
    final Path mPath;
    final int mRotation;

    private CellConfig(Path path, int i) {
        this(path, i, false);
    }

    private CellConfig(Path path, int i, boolean z) {
        this.mPath = path;
        this.mRotation = i;
        this.mFlipVertical = z;
    }
}
