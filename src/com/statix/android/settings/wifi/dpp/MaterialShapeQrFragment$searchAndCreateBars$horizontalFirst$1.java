package com.statix.android.settings.wifi.dpp;

import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MaterialShapeQrFragment.kt */
/* loaded from: classes3.dex */
public final /* synthetic */ class MaterialShapeQrFragment$searchAndCreateBars$horizontalFirst$1 extends FunctionReferenceImpl implements Function4 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public MaterialShapeQrFragment$searchAndCreateBars$horizontalFirst$1(Object obj) {
        super(4, obj, MaterialShapeQrFragment.class, "tryFindingHorizontalBar", "tryFindingHorizontalBar(IIILjava/util/List;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function4
    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
        invoke(((Number) obj).intValue(), ((Number) obj2).intValue(), ((Number) obj3).intValue(), (List) obj4);
        return Unit.INSTANCE;
    }

    public final void invoke(int i, int i2, int i3, List p3) {
        Intrinsics.checkNotNullParameter(p3, "p3");
        ((MaterialShapeQrFragment) this.receiver).tryFindingHorizontalBar(i, i2, i3, p3);
    }
}
