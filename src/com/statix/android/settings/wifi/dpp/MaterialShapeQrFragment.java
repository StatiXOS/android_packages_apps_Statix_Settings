package com.statix.android.settings.wifi.dpp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Trace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.statix.android.settings.R$color;
import com.statix.android.settings.R$drawable;
import com.statix.android.settings.R$id;
import com.statix.android.settings.R$layout;
import com.statix.android.settings.wifi.dpp.MaterialShapeRenderer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$IntRef;
import kotlin.random.Random;
import kotlin.reflect.KFunction;
/* compiled from: MaterialShapeQrFragment.kt */
/* loaded from: classes3.dex */
public final class MaterialShapeQrFragment extends Fragment {
    private static boolean disableAnimationForTesting;
    private int finderPatternCenterShapeIndex;
    private View frameLayoutView;
    private boolean hasFinalDataImage;
    private Context hostContext;
    private String qrCodeContent;
    private LottieAnimationView qrCodeViewForBackground;
    private ImageView qrCodeViewForFinderPatterns;
    private ImageView qrCodeViewForNonFinderPatterns;
    private long qrStartTime;
    private QRCode qrcode;
    private int qrcodeBitmapSize;
    private int qrcodeLineCount;
    public static final Companion Companion = new Companion(null);
    public static final int $stable = 8;
    private ErrorCorrectionLevel qrCodeEcLevel = ErrorCorrectionLevel.L;
    private List finderPatternShapeList = new ArrayList();
    private List dataModuleShapeList = new ArrayList();
    private List backgroundModuleShapeList = new ArrayList();
    private final ViewTreeObserver.OnDrawListener onDrawListener = new ViewTreeObserver.OnDrawListener() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$onDrawListener$1
        @Override // android.view.ViewTreeObserver.OnDrawListener
        public final void onDraw() {
            MaterialShapeQrFragment.this.onDrawCallback();
        }
    };
    private Boolean[][] hasCreated = {new Boolean[]{Boolean.FALSE}};
    private VectorDrawable[] arrayOf1x1Shapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOf1x1SemiCircleShapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOf2x2Shapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOf3x3Shapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOf7x7Shapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOfHorizontalBarShapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOfHorizontalHalfCapsuleBarShapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOfVerticalBarShapes = new VectorDrawable[0];
    private VectorDrawable[] arrayOfFinderPatternCenterShapes = new VectorDrawable[0];
    private int foregroundColorPrimary = -16711936;
    private int foregroundColorSecondary = -16776961;
    private int foregroundColorAccent = -65536;
    private Integer[] mainForegroundColorArray = {-16711936, Integer.valueOf(this.foregroundColorSecondary)};
    private int backgroundShapeColor = -65536;
    private int backgroundDotColor1 = -65536;
    private int backgroundDotColor2 = -65536;
    private int backgroundDotColor3 = -65536;
    private Integer[] mainBackgroundColorArray = {-65536, Integer.valueOf(this.backgroundDotColor2), Integer.valueOf(this.backgroundDotColor3)};

    private final float calculateAnimationBackgroundAlpha(long j) {
        if (j < 250) {
            return 0.0f;
        }
        if (j < 583) {
            return ((float) (j - 250)) / 333;
        }
        return 1.0f;
    }

    public static /* synthetic */ void getBackgroundModuleShapeList$annotations() {
    }

    public static /* synthetic */ void getDataModuleShapeList$annotations() {
    }

    public static /* synthetic */ void getQrcode$annotations() {
    }

    public final QRCode getQrcode() {
        QRCode qRCode = this.qrcode;
        if (qRCode != null) {
            return qRCode;
        }
        Intrinsics.throwUninitializedPropertyAccessException("qrcode");
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkNotNullParameter(inflater, "inflater");
        Trace.beginSection("onCreateView");
        View inflate = inflater.inflate(R$layout.material_shape_qr_fragment, viewGroup, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "inflate(...)");
        View findViewById = inflate.findViewById(R$id.material_shape_qr_fragment);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(...)");
        this.frameLayoutView = findViewById;
        LottieAnimationView lottieAnimationView = null;
        if (!disableAnimationForTesting) {
            if (findViewById == null) {
                Intrinsics.throwUninitializedPropertyAccessException("frameLayoutView");
                findViewById = null;
            }
            findViewById.getViewTreeObserver().addOnDrawListener(this.onDrawListener);
        }
        View findViewById2 = inflate.findViewById(R$id.qr_code_img_view_for_non_finder_patterns);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(...)");
        this.qrCodeViewForNonFinderPatterns = (ImageView) findViewById2;
        View findViewById3 = inflate.findViewById(R$id.qr_code_img_view_for_finder_patterns);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(...)");
        this.qrCodeViewForFinderPatterns = (ImageView) findViewById3;
        View findViewById4 = inflate.findViewById(R$id.qr_code_background_view);
        Intrinsics.checkNotNullExpressionValue(findViewById4, "findViewById(...)");
        this.qrCodeViewForBackground = (LottieAnimationView) findViewById4;
        applyLottieDynamicColor();
        LottieAnimationView lottieAnimationView2 = this.qrCodeViewForBackground;
        if (lottieAnimationView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForBackground");
        } else {
            lottieAnimationView = lottieAnimationView2;
        }
        lottieAnimationView.playAnimation();
        Trace.endSection();
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        View view = this.frameLayoutView;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("frameLayoutView");
            view = null;
        }
        view.getViewTreeObserver().removeOnDrawListener(this.onDrawListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        super.onAttach(context);
        Trace.beginSection("onAttach");
        VectorDrawable onAttach$loadVectorDrawable = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s1_circle);
        VectorDrawable onAttach$loadVectorDrawable2 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s1_drop);
        VectorDrawable onAttach$loadVectorDrawable3 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s1_semi_circle);
        this.arrayOf1x1Shapes = new VectorDrawable[]{onAttach$loadVectorDrawable, onAttach$loadVectorDrawable2, onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s1_square)};
        this.arrayOf1x1SemiCircleShapes = new VectorDrawable[]{onAttach$loadVectorDrawable3};
        this.arrayOf2x2Shapes = new VectorDrawable[]{onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s2_circle), onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s2_clover), onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s2_hexagonal), onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s2_meteroid), onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s2_wiggle_star)};
        VectorDrawable onAttach$loadVectorDrawable4 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s3_circle);
        VectorDrawable onAttach$loadVectorDrawable5 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s3_clover);
        VectorDrawable onAttach$loadVectorDrawable6 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s3_hexagonal);
        VectorDrawable onAttach$loadVectorDrawable7 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s3_meteroid);
        VectorDrawable onAttach$loadVectorDrawable8 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s3_wiggle_star);
        this.arrayOf3x3Shapes = new VectorDrawable[]{onAttach$loadVectorDrawable4, onAttach$loadVectorDrawable5, onAttach$loadVectorDrawable6, onAttach$loadVectorDrawable7, onAttach$loadVectorDrawable8};
        VectorDrawable[] vectorDrawableArr = {onAttach$loadVectorDrawable6, onAttach$loadVectorDrawable7, onAttach$loadVectorDrawable8};
        this.arrayOfFinderPatternCenterShapes = vectorDrawableArr;
        ArraysKt.shuffle(vectorDrawableArr);
        this.finderPatternCenterShapeIndex = 0;
        this.arrayOf7x7Shapes = new VectorDrawable[]{onAttach$loadVectorDrawable(context, R$drawable.qrcode_square_s7_ring)};
        VectorDrawable onAttach$loadVectorDrawable9 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_hor_bar_s2_capsule);
        VectorDrawable onAttach$loadVectorDrawable10 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_hor_bar_s3_capsule);
        VectorDrawable onAttach$loadVectorDrawable11 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_hor_bar_s2_half_capsule);
        VectorDrawable onAttach$loadVectorDrawable12 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_hor_bar_s3_half_capsule);
        VectorDrawable onAttach$loadVectorDrawable13 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_ver_bar_s2_capsule);
        VectorDrawable onAttach$loadVectorDrawable14 = onAttach$loadVectorDrawable(context, R$drawable.qrcode_ver_bar_s3_capsule);
        this.arrayOfHorizontalBarShapes = new VectorDrawable[]{onAttach$loadVectorDrawable9, onAttach$loadVectorDrawable10};
        this.arrayOfHorizontalHalfCapsuleBarShapes = new VectorDrawable[]{onAttach$loadVectorDrawable11, onAttach$loadVectorDrawable12};
        this.arrayOfVerticalBarShapes = new VectorDrawable[]{onAttach$loadVectorDrawable13, onAttach$loadVectorDrawable14};
        loadDynamicColors(context);
        this.hostContext = context;
        Trace.endSection();
    }

    private static final VectorDrawable onAttach$loadVectorDrawable(Context context, int i) {
        Drawable drawable = ContextCompat.getDrawable(context, i);
        Intrinsics.checkNotNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.VectorDrawable");
        return (VectorDrawable) drawable;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        this.hostContext = null;
        this.arrayOf1x1Shapes = new VectorDrawable[0];
        this.arrayOf1x1SemiCircleShapes = new VectorDrawable[0];
        this.arrayOf2x2Shapes = new VectorDrawable[0];
        this.arrayOf3x3Shapes = new VectorDrawable[0];
        this.arrayOfFinderPatternCenterShapes = new VectorDrawable[0];
        this.arrayOf7x7Shapes = new VectorDrawable[0];
        this.arrayOfHorizontalBarShapes = new VectorDrawable[0];
        this.arrayOfVerticalBarShapes = new VectorDrawable[0];
        this.arrayOfHorizontalHalfCapsuleBarShapes = new VectorDrawable[0];
        super.onDetach();
    }

    private final void applyLottieDynamicColor() {
        for (Map.Entry entry : MapsKt.mapOf(TuplesKt.to(".bg", Integer.valueOf(this.backgroundShapeColor)), TuplesKt.to(".dot1", Integer.valueOf(this.backgroundDotColor1)), TuplesKt.to(".dot2", Integer.valueOf(this.backgroundDotColor2)), TuplesKt.to(".dot3", Integer.valueOf(this.backgroundDotColor3))).entrySet()) {
            String str = (String) entry.getKey();
            final int intValue = ((Number) entry.getValue()).intValue();
            LottieAnimationView lottieAnimationView = this.qrCodeViewForBackground;
            if (lottieAnimationView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForBackground");
                lottieAnimationView = null;
            }
            lottieAnimationView.addValueCallback(new KeyPath("**", str, "**"), LottieProperty.COLOR_FILTER, new SimpleLottieValueCallback() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$applyLottieDynamicColor$1
                @Override // com.airbnb.lottie.value.SimpleLottieValueCallback
                public final ColorFilter getValue(LottieFrameInfo lottieFrameInfo) {
                    return new PorterDuffColorFilter(intValue, PorterDuff.Mode.SRC);
                }
            });
        }
    }

    private final void drawModules(long j) {
        int i = this.qrcodeBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Intrinsics.checkNotNullExpressionValue(createBitmap, "createBitmap(...)");
        Canvas canvas = new Canvas(createBitmap);
        drawShapeListOnCanvas(this.dataModuleShapeList, canvas, j);
        drawShapeListOnCanvas(this.backgroundModuleShapeList, canvas, j);
        ImageView imageView = this.qrCodeViewForNonFinderPatterns;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForNonFinderPatterns");
            imageView = null;
        }
        imageView.setImageBitmap(createBitmap);
    }

    private final void drawFinderPatterns(long j) {
        int i = this.qrcodeBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Intrinsics.checkNotNullExpressionValue(createBitmap, "createBitmap(...)");
        drawShapeListOnCanvas(this.finderPatternShapeList, new Canvas(createBitmap), j);
        ImageView imageView = this.qrCodeViewForFinderPatterns;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForFinderPatterns");
            imageView = null;
        }
        imageView.setImageBitmap(createBitmap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onDrawCallback() {
        if (this.qrcodeLineCount == 0) {
            return;
        }
        Trace.beginSection("onDraw");
        long currentTimeMillis = System.currentTimeMillis() - this.qrStartTime;
        if (currentTimeMillis <= 3000) {
            drawModules(currentTimeMillis);
        } else if (!this.hasFinalDataImage) {
            drawModules(currentTimeMillis);
            this.hasFinalDataImage = true;
        }
        drawFinderPatterns(currentTimeMillis);
        drawAnimationBackground(currentTimeMillis);
        Trace.endSection();
    }

    private final void drawAnimationBackground(long j) {
        LottieAnimationView lottieAnimationView = null;
        if (j < 1100) {
            float calculateAnimationBackgroundScale = calculateAnimationBackgroundScale(j);
            LottieAnimationView lottieAnimationView2 = this.qrCodeViewForBackground;
            if (lottieAnimationView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForBackground");
                lottieAnimationView2 = null;
            }
            lottieAnimationView2.setScaleX(calculateAnimationBackgroundScale);
            LottieAnimationView lottieAnimationView3 = this.qrCodeViewForBackground;
            if (lottieAnimationView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForBackground");
                lottieAnimationView3 = null;
            }
            lottieAnimationView3.setScaleY(calculateAnimationBackgroundScale);
        }
        if (j <= 600) {
            float calculateAnimationBackgroundAlpha = calculateAnimationBackgroundAlpha(j);
            final Ref$IntRef ref$IntRef = new Ref$IntRef();
            ref$IntRef.element = ColorUtils.setAlphaComponent(this.backgroundShapeColor, (int) (calculateAnimationBackgroundAlpha * 255));
            LottieAnimationView lottieAnimationView4 = this.qrCodeViewForBackground;
            if (lottieAnimationView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("qrCodeViewForBackground");
            } else {
                lottieAnimationView = lottieAnimationView4;
            }
            lottieAnimationView.addValueCallback(new KeyPath("**", ".bg", "**"), LottieProperty.COLOR_FILTER, new SimpleLottieValueCallback() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$drawAnimationBackground$1
                @Override // com.airbnb.lottie.value.SimpleLottieValueCallback
                public final ColorFilter getValue(LottieFrameInfo lottieFrameInfo) {
                    return new PorterDuffColorFilter(Ref$IntRef.this.element, PorterDuff.Mode.SRC);
                }
            });
        }
    }

    private final float calculateAnimationBackgroundScale(long j) {
        if (j < 250) {
            return 0.0f;
        }
        if (j < 1083) {
            return (EmphasizedInterpolator.INSTANCE.getInterpolation(((float) (j - 250)) / 833) * 0.19999999f) + 0.8f;
        }
        return 1.0f;
    }

    public static /* synthetic */ void updateQrCodeContent$default(MaterialShapeQrFragment materialShapeQrFragment, String str, ErrorCorrectionLevel errorCorrectionLevel, int i, Object obj) {
        if ((i & 2) != 0) {
            errorCorrectionLevel = ErrorCorrectionLevel.L;
        }
        materialShapeQrFragment.updateQrCodeContent(str, errorCorrectionLevel);
    }

    public final void updateQrCodeContent(String data, ErrorCorrectionLevel ecLevel) {
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(ecLevel, "ecLevel");
        this.qrCodeContent = data;
        this.qrCodeEcLevel = ecLevel;
        if (this.hostContext != null) {
            Trace.beginSection("createMaterialShapeQRCode");
            createMaterialShapeQRCode(data, ecLevel);
            Trace.endSection();
        }
    }

    private final VectorDrawable randomSquare(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i == 7) {
                        return (VectorDrawable) ArraysKt.first(this.arrayOf7x7Shapes);
                    }
                    throw new IllegalArgumentException("Unsupported square shape: " + i);
                }
                return (VectorDrawable) ArraysKt.random(this.arrayOf3x3Shapes, Random.Default);
            }
            return (VectorDrawable) ArraysKt.random(this.arrayOf2x2Shapes, Random.Default);
        }
        return (VectorDrawable) ArraysKt.random(this.arrayOf1x1Shapes, Random.Default);
    }

    private final VectorDrawable randomHorizontalBar(int i) {
        return this.arrayOfHorizontalBarShapes[i - 2];
    }

    private final VectorDrawable randomHorizontalHalfCapsuleBar(int i) {
        return this.arrayOfHorizontalHalfCapsuleBarShapes[i - 2];
    }

    private final VectorDrawable randomVerticalBar(int i) {
        return this.arrayOfVerticalBarShapes[i - 2];
    }

    private final VectorDrawable getSemiCircle() {
        return (VectorDrawable) ArraysKt.first(this.arrayOf1x1SemiCircleShapes);
    }

    private final VectorDrawable nextFinderPatternCenter() {
        VectorDrawable[] vectorDrawableArr = this.arrayOfFinderPatternCenterShapes;
        int i = this.finderPatternCenterShapeIndex;
        this.finderPatternCenterShapeIndex = i + 1;
        return vectorDrawableArr[i % vectorDrawableArr.length];
    }

    private final void createMaterialShapeQRCode(String str, ErrorCorrectionLevel errorCorrectionLevel) {
        if (this.hostContext == null) {
            throw new IllegalArgumentException("Host context cannot be null");
        }
        QRCode encode = Encoder.encode(str, errorCorrectionLevel, null);
        Intrinsics.checkNotNullExpressionValue(encode, "encode(...)");
        this.qrcode = encode;
        this.qrStartTime = System.currentTimeMillis();
        this.hasFinalDataImage = false;
        this.qrcodeLineCount = getQrcode().getMatrix().getWidth();
        this.qrcodeBitmapSize = getQrcode().getMatrix().getWidth() * 17;
        int i = this.qrcodeLineCount;
        Boolean[][] boolArr = new Boolean[i];
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = this.qrcodeLineCount;
            Boolean[] boolArr2 = new Boolean[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                boolArr2[i4] = Boolean.FALSE;
            }
            boolArr[i2] = boolArr2;
        }
        this.hasCreated = boolArr;
        this.finderPatternShapeList.clear();
        this.dataModuleShapeList.clear();
        int colorForFinderPattern = getColorForFinderPattern();
        createRendererForShape(0, 0, 7, 7, this.finderPatternShapeList, randomSquare(7), colorForFinderPattern, new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createMaterialShapeQRCode$2
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.EmphasizedZoomIn);
                it.setStartDelay(833L);
                it.setDuration(834L);
            }
        });
        createRendererForShape(this.qrcodeLineCount - 7, 0, 7, 7, this.finderPatternShapeList, randomSquare(7), colorForFinderPattern, new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createMaterialShapeQRCode$3
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.EmphasizedZoomIn);
                it.setStartDelay(833L);
                it.setDuration(834L);
            }
        });
        createRendererForShape(0, this.qrcodeLineCount - 7, 7, 7, this.finderPatternShapeList, randomSquare(7), colorForFinderPattern, new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createMaterialShapeQRCode$4
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.EmphasizedZoomIn);
                it.setStartDelay(833L);
                it.setDuration(834L);
            }
        });
        createRendererForShape(2, 2, 3, 3, this.finderPatternShapeList, nextFinderPatternCenter(), colorForFinderPattern, new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createMaterialShapeQRCode$5
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.RotateEmphasizedZoomIn);
                it.setStartDelay(1167L);
                it.setDuration(1667L);
            }
        });
        createRendererForShape(this.qrcodeLineCount - 5, 2, 3, 3, this.finderPatternShapeList, nextFinderPatternCenter(), colorForFinderPattern, new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createMaterialShapeQRCode$6
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.RotateEmphasizedZoomIn);
                it.setStartDelay(1167L);
                it.setDuration(1667L);
            }
        });
        createRendererForShape(2, this.qrcodeLineCount - 5, 3, 3, this.finderPatternShapeList, nextFinderPatternCenter(), colorForFinderPattern, new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createMaterialShapeQRCode$7
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.RotateEmphasizedZoomIn);
                it.setStartDelay(1167L);
                it.setDuration(1667L);
            }
        });
        searchAndCreateLargeSquareShapes(3, this.dataModuleShapeList);
        searchAndCreateLargeSquareShapes(2, this.dataModuleShapeList);
        searchAndCreateHorizontalBars(4, this.dataModuleShapeList);
        searchAndCreateBars(3, this.dataModuleShapeList);
        searchAndCreateBars(2, this.dataModuleShapeList);
        searchAndCreateSmallForegroundSquareShapes(this.dataModuleShapeList);
        searchAndCreateSmallBackgroundSquareShapes(this.backgroundModuleShapeList);
    }

    private final void drawShapeListOnCanvas(List list, Canvas canvas, long j) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((MaterialShapeRenderer) it.next()).draw(canvas, j);
        }
    }

    private final void loadDynamicColors(Context context) {
        this.foregroundColorPrimary = context.getColor(R$color.wifi_qr_primary_foreground);
        this.foregroundColorSecondary = context.getColor(R$color.wifi_qr_secondary_foreground);
        this.foregroundColorAccent = context.getColor(R$color.wifi_qr_accent_foreground);
        this.backgroundShapeColor = context.getColor(R$color.wifi_qr_background_surface);
        this.mainForegroundColorArray = new Integer[]{Integer.valueOf(this.foregroundColorPrimary), Integer.valueOf(this.foregroundColorSecondary)};
        this.backgroundDotColor1 = ColorUtils.setAlphaComponent(context.getColor(R$color.wifi_qr_primary_background), 38);
        this.backgroundDotColor2 = ColorUtils.setAlphaComponent(context.getColor(R$color.wifi_qr_secondary_background), 25);
        this.backgroundDotColor3 = ColorUtils.setAlphaComponent(context.getColor(R$color.wifi_qr_tertiary_background), 76);
        this.mainBackgroundColorArray = new Integer[]{Integer.valueOf(this.backgroundDotColor1), Integer.valueOf(this.backgroundDotColor2), Integer.valueOf(this.backgroundDotColor3)};
    }

    private final int getColorForFinderPattern() {
        return this.foregroundColorPrimary;
    }

    private final int randomForegroundColor(int i, int i2) {
        if (i2 == 1) {
            if (Random.Default.nextFloat() <= ((Number) CollectionsKt.listOf((Object[]) new Float[]{Float.valueOf(0.2f), Float.valueOf(0.04f), Float.valueOf(0.01f), Float.valueOf(0.0f)}).get(i - 1)).floatValue()) {
                return this.foregroundColorAccent;
            }
        }
        return ((Number) ArraysKt.random(this.mainForegroundColorArray, Random.Default)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int randomBackgroundColor() {
        return ((Number) ArraysKt.random(this.mainBackgroundColorArray, Random.Default)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long calculateStartDelay(int i, int i2, int i3, int i4) {
        return (calculateRatioToCenter(i, i2, i3, i4) * ((float) 1000)) + ((i3 == 1 && i4 == 1) ? 0L : Random.Default.nextLong(400L));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final float calculateRatioToCenter(int i, int i2, int i3, int i4) {
        float f = this.qrcodeLineCount / 2.0f;
        float f2 = (i + (i3 / 2.0f)) - f;
        float f3 = (i2 + (i4 / 2.0f)) - f;
        return ((float) Math.sqrt((f2 * f2) + (f3 * f3))) / (1.414f * f);
    }

    private final void markAsCreated(int i, int i2, int i3, int i4) {
        for (int i5 = 0; i5 < i3; i5++) {
            for (int i6 = 0; i6 < i4; i6++) {
                this.hasCreated[i + i5][i2 + i6] = Boolean.TRUE;
            }
        }
    }

    private final boolean isForeground(int i, int i2) {
        return (getQrcode().getMatrix().get(i, i2) & 15) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void tryFindingHorizontalBar(int i, int i2, int i3, List list) {
        if (i3 > 4) {
            throw new IllegalArgumentException("barLen must be <= 4");
        }
        int i4 = i + i3;
        int i5 = this.qrcodeLineCount;
        if (i4 > i5 || i2 + 1 > i5) {
            return;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = i + i6;
            if (this.hasCreated[i7][i2].booleanValue() || !isForeground(i7, i2)) {
                return;
            }
        }
        createHorizontalBar(i, i2, i3, list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void tryFindingVerticalBar(int i, int i2, int i3, List list) {
        int i4 = i2 + i3;
        int i5 = this.qrcodeLineCount;
        if (i4 > i5 || i + 1 > i5) {
            return;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = i2 + i6;
            if (!isForeground(i, i7) || this.hasCreated[i][i7].booleanValue()) {
                return;
            }
        }
        createSingleVerticalBar(i, i2, i3, list);
    }

    private final void searchAndCreateHorizontalBars(int i, List list) {
        if (i > 4) {
            throw new IllegalArgumentException("barLen must be <= 4");
        }
        int i2 = this.qrcodeLineCount;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = (this.qrcodeLineCount - i) + 1;
            for (int i5 = 0; i5 < i4; i5++) {
                if (!this.hasCreated[i5][i3].booleanValue() && isForeground(i5, i3)) {
                    int i6 = 1;
                    while (true) {
                        if (i6 < i) {
                            int i7 = i5 + i6;
                            if (!this.hasCreated[i7][i3].booleanValue() && isForeground(i7, i3)) {
                                i6++;
                            }
                        } else {
                            createHorizontalBar(i5, i3, i, list);
                            break;
                        }
                    }
                }
            }
        }
    }

    private final void searchAndCreateBars(int i, List list) {
        List listOf = CollectionsKt.listOf((Object[]) new KFunction[]{new MaterialShapeQrFragment$searchAndCreateBars$horizontalFirst$1(this), new MaterialShapeQrFragment$searchAndCreateBars$horizontalFirst$2(this)});
        List reversed = CollectionsKt.reversed(listOf);
        int i2 = this.qrcodeLineCount;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = this.qrcodeLineCount;
            for (int i5 = 0; i5 < i4; i5++) {
                if (!this.hasCreated[i5][i3].booleanValue() && isForeground(i5, i3)) {
                    for (KFunction kFunction : ((double) Random.Default.nextFloat()) < 0.5d ? listOf : reversed) {
                        ((Function4) kFunction).invoke(Integer.valueOf(i5), Integer.valueOf(i3), Integer.valueOf(i), list);
                    }
                }
            }
        }
    }

    private final void searchAndCreateLargeSquareShapes(final int i, List list) {
        int i2 = (this.qrcodeLineCount - i) + 1;
        for (final int i3 = 0; i3 < i2; i3++) {
            int i4 = (this.qrcodeLineCount - i) + 1;
            for (final int i5 = 0; i5 < i4; i5++) {
                if (!this.hasCreated[i5][i3].booleanValue() && isForeground(i5, i3)) {
                    int i6 = 0;
                    while (true) {
                        if (i6 >= i) {
                            createRendererForShape(i5, i3, i, i, list, randomSquare(i), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$searchAndCreateLargeSquareShapes$1
                                /* JADX INFO: Access modifiers changed from: package-private */
                                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                                {
                                    super(1);
                                }

                                @Override // kotlin.jvm.functions.Function1
                                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                                    invoke((MaterialShapeRenderer) obj);
                                    return Unit.INSTANCE;
                                }

                                public final void invoke(MaterialShapeRenderer it) {
                                    long calculateStartDelay;
                                    Intrinsics.checkNotNullParameter(it, "it");
                                    it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.SpringZoomIn);
                                    MaterialShapeQrFragment materialShapeQrFragment = MaterialShapeQrFragment.this;
                                    int i7 = i5;
                                    int i8 = i3;
                                    int i9 = i;
                                    calculateStartDelay = materialShapeQrFragment.calculateStartDelay(i7, i8, i9, i9);
                                    it.setStartDelay(calculateStartDelay);
                                    it.setInitialRotation(MaterialShapeQrFragment.Companion.randomRotationForSquareShape());
                                }
                            });
                            break;
                        }
                        for (int i7 = 0; i7 < i; i7++) {
                            int i8 = i5 + i6;
                            int i9 = i3 + i7;
                            if (!this.hasCreated[i8][i9].booleanValue() && isForeground(i8, i9)) {
                            }
                        }
                        i6++;
                    }
                }
            }
        }
    }

    private final void searchAndCreateSmallForegroundSquareShapes(List list) {
        int i = this.qrcodeLineCount;
        for (final int i2 = 0; i2 < i; i2++) {
            int i3 = this.qrcodeLineCount;
            for (final int i4 = 0; i4 < i3; i4++) {
                if (!this.hasCreated[i4][i2].booleanValue() && isForeground(i4, i2)) {
                    createRendererForShape(i4, i2, 1, 1, list, randomSquare(1), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$searchAndCreateSmallForegroundSquareShapes$1
                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @Override // kotlin.jvm.functions.Function1
                        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                            invoke((MaterialShapeRenderer) obj);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(MaterialShapeRenderer it) {
                            long calculateStartDelay;
                            Intrinsics.checkNotNullParameter(it, "it");
                            it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.ZoomIn);
                            calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay(i4, i2, 1, 1);
                            it.setStartDelay(calculateStartDelay);
                            it.setSkipStartProgress(0.3f);
                        }
                    });
                }
            }
        }
    }

    private final void searchAndCreateSmallBackgroundSquareShapes(List list) {
        int i = this.qrcodeLineCount;
        for (final int i2 = 0; i2 < i; i2++) {
            int i3 = this.qrcodeLineCount;
            for (final int i4 = 0; i4 < i3; i4++) {
                if (!this.hasCreated[i4][i2].booleanValue() && !isForeground(i4, i2)) {
                    createRendererForShape(i4, i2, 1, 1, list, randomSquare(1), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$searchAndCreateSmallBackgroundSquareShapes$1
                        /* JADX INFO: Access modifiers changed from: package-private */
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @Override // kotlin.jvm.functions.Function1
                        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                            invoke((MaterialShapeRenderer) obj);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(MaterialShapeRenderer it) {
                            float calculateRatioToCenter;
                            int randomBackgroundColor;
                            Intrinsics.checkNotNullParameter(it, "it");
                            it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.EmphasizedZoomIn);
                            calculateRatioToCenter = MaterialShapeQrFragment.this.calculateRatioToCenter(i4, i2, 1, 1);
                            it.setStartDelay(83 + (1250 * calculateRatioToCenter));
                            it.setSkipStartProgress(0.3f);
                            randomBackgroundColor = MaterialShapeQrFragment.this.randomBackgroundColor();
                            Paint paint = new Paint();
                            paint.setColorFilter(new PorterDuffColorFilter(randomBackgroundColor, PorterDuff.Mode.SRC_IN));
                            it.setPaint(paint);
                        }
                    });
                }
            }
        }
    }

    private final void createHorizontalBar(final int i, final int i2, final int i3, List list) {
        if (i3 > 4) {
            throw new IllegalArgumentException("barLen must be <= 4");
        }
        if (i3 <= 2 || (i3 == 3 && Random.Default.nextFloat() < 0.5f)) {
            createRendererForShape(i, i2, i3, 1, list, randomHorizontalBar(i3), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createHorizontalBar$1
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                    invoke((MaterialShapeRenderer) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(MaterialShapeRenderer it) {
                    long calculateStartDelay;
                    Intrinsics.checkNotNullParameter(it, "it");
                    it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.SpringZoomIn);
                    calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay(i, i2, i3, 1);
                    it.setStartDelay(calculateStartDelay);
                }
            });
        } else if (Random.Default.nextFloat() > 0.5f) {
            createRendererForShape(i, i2, 1, 1, list, getSemiCircle(), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createHorizontalBar$2
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                    invoke((MaterialShapeRenderer) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(MaterialShapeRenderer it) {
                    long calculateStartDelay;
                    Intrinsics.checkNotNullParameter(it, "it");
                    it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.ZoomIn);
                    calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay(i, i2, 1, 1);
                    it.setStartDelay(calculateStartDelay);
                    it.setSkipStartProgress(0.3f);
                }
            });
            int i4 = i3 - 1;
            createRendererForShape(i + 1, i2, i4, 1, list, randomHorizontalHalfCapsuleBar(i4), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createHorizontalBar$3
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                    invoke((MaterialShapeRenderer) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(MaterialShapeRenderer it) {
                    long calculateStartDelay;
                    Intrinsics.checkNotNullParameter(it, "it");
                    it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.SpringZoomIn);
                    calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay(i + 1, i2, i3 - 1, 1);
                    it.setStartDelay(calculateStartDelay);
                }
            });
        } else {
            int i5 = i3 - 1;
            createRendererForShape(i, i2, i5, 1, list, randomHorizontalHalfCapsuleBar(i5), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createHorizontalBar$4
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                    invoke((MaterialShapeRenderer) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(MaterialShapeRenderer it) {
                    long calculateStartDelay;
                    Intrinsics.checkNotNullParameter(it, "it");
                    it.setInitialRotation(2);
                    it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.SpringZoomIn);
                    calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay(i, i2, i3 - 1, 1);
                    it.setStartDelay(calculateStartDelay);
                }
            });
            createRendererForShape((i + i3) - 1, i2, 1, 1, list, getSemiCircle(), randomForegroundColor(1, 1), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createHorizontalBar$5
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                    invoke((MaterialShapeRenderer) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(MaterialShapeRenderer it) {
                    long calculateStartDelay;
                    Intrinsics.checkNotNullParameter(it, "it");
                    it.setInitialRotation(2);
                    it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.ZoomIn);
                    calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay((i + i3) - 1, i2, 1, 1);
                    it.setStartDelay(calculateStartDelay);
                    it.setSkipStartProgress(0.3f);
                }
            });
        }
    }

    private final void createSingleVerticalBar(final int i, final int i2, final int i3, List list) {
        createRendererForShape(i, i2, 1, i3, list, randomVerticalBar(i3), new Function1() { // from class: com.google.android.settings.wifi.dpp.MaterialShapeQrFragment$createSingleVerticalBar$1
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                invoke((MaterialShapeRenderer) obj);
                return Unit.INSTANCE;
            }

            public final void invoke(MaterialShapeRenderer it) {
                long calculateStartDelay;
                Intrinsics.checkNotNullParameter(it, "it");
                it.setAnimationStyle(MaterialShapeRenderer.EntryAnimationStyle.SpringZoomIn);
                calculateStartDelay = MaterialShapeQrFragment.this.calculateStartDelay(i, i2, 1, i3);
                it.setStartDelay(calculateStartDelay);
            }
        });
    }

    private final void createRendererForShape(int i, int i2, int i3, int i4, List list, VectorDrawable vectorDrawable, Function1 function1) {
        createRendererForShape(i, i2, i3, i4, list, vectorDrawable, randomForegroundColor(i3, i4), function1);
    }

    private final void createRendererForShape(int i, int i2, int i3, int i4, List list, VectorDrawable vectorDrawable, int i5, Function1 function1) {
        RectF rectF = new RectF(i * 17, i2 * 17, (i + i3) * 17, (i2 + i4) * 17);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(i5, PorterDuff.Mode.SRC_IN));
        MaterialShapeRenderer materialShapeRenderer = new MaterialShapeRenderer(vectorDrawable, rectF, paint);
        materialShapeRenderer.setStartDelay(calculateStartDelay(i, i2, i3, i4));
        function1.invoke(materialShapeRenderer);
        list.add(materialShapeRenderer);
        markAsCreated(i, i2, i3, i4);
    }

    /* compiled from: MaterialShapeQrFragment.kt */
    /* loaded from: classes3.dex */
    public final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getDisableAnimationForTesting$annotations() {
        }

        public static /* synthetic */ void getMODULE_SIZE$annotations() {
        }

        private Companion() {
        }

        public final int randomRotationForSquareShape() {
            return Random.Default.nextInt() % 4;
        }
    }
}
