package com.nader.intelligent.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.aliyun.iot.aep.sdk.scan.manager.camera.CameraManager;
import com.aliyun.iot.aep.sdk.scan.scansurface.AScanView;


/**
 * The type View scan view.
 *
 * @author sinyuk
 * @date 2018 /6/22
 * <p> this is the view of scan area,and this view need get the Area(Rect) from the CameraManager,this is why it need extend from AScanView
 */
public final class ViewScanView extends AScanView {

    /**
     * 扫描区域的样式
     */
    public static final int STYLE_GRID = 0;

    /**
     * The constant STYLE_RADAR.
     */
    public static final int STYLE_RADAR = 1;
    /**
     * The constant STYLE_HYBRID.
     */
    public static final int STYLE_HYBRID = 2;
    /**
     * The constant STYLE_SELF_SCAN_LINE.
     */
    public static final int STYLE_SELF_SCAN_LINE = 3;

    /**
     * 相机管理器，拿取最佳的扫描区域
     */
    private Rect mFrame;
    /**
     * 最佳扫描区域的Rect
     */
    private CameraManager cameraManager;
    /**
     * 边框画笔
     */
    private Paint mLinePaint;
    /**
     * 脚线画笔
     */
    private Paint mCornerLinePaint;
    /**
     * 网格样式画笔
     */
    private Paint mScanPaintGrid;
    /**
     * 雷达样式画笔
     */
    private Paint mScanPaintRadio;

    /**
     * 扫描颜色
     */
    private int mScanColor;

    private int mBoundaryColor;
    /**
     * 扫描区域边线样式-线宽
     */
    private float mBoundaryStrokeWidth;

    /**
     * 雷达样式的画笔shader
     */
    private LinearGradient mLinearGradientRadar;

    /**
     * 网格样式的path
     */
    private Path mGridPath;
    /**
     * 网格画笔的shader
     */
    private LinearGradient mLinearGradientGrid;
    /**
     * 网格线的线宽，单位pix
     */
    private float mGridLineWidth;
    /**
     * 网格样式的，网格密度，值越大越密集
     */
    private int mGridDensity;

    /**
     * 变换矩阵，用来实现动画效果
     */
    private Matrix mScanMatrix;
    /**
     * 值动画，用来变换矩阵操作
     */
    private ValueAnimator mValueAnimator;
    private int mScanAnimatorDuration;

    /**
     * 网格 0：网格，1：纵向雷达 2:综合
     */
    private int mScanStyle = 2;

    /**
     * 扫描边框角线占边总长的比例
     */
    private float mCornerLineLenRatio;
    /**
     * 根据比例计算的边框长度，从四角定点向临近的定点画出的长度
     */
    private float mCornerLineLen;
    /**
     * 边框path
     */
    private Path mCornerBoundaryLinePath;
    private int mCornerBoundaryColor;
    private float mCornerBoundaryStrokeWidth;

    /**
     * 扫描线的图片--设置该图片则默认为-STYLE_SELF_SCAN_LINE
     */
    private Drawable mSelfScanLineDrawable;
    private int mSelfScanDraLineHeight;
    /**
     * 自定义扫描线的bounds，即将绘制的区域
     */
    private Rect mSelfScanDraLineRect;

    private String mScanTip;
    private float tipMargin2ScanBottom;
    private TextPaint mTipTextPaint;
    private float mTipLength;

    /**
     * Instantiates a new View scan view.
     *
     * @param context the context
     */
    public ViewScanView(Context context) {
        this(context, null);
    }

    /**
     * This constructor is used when the class is built from an XML resource.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ViewScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new View scan view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public ViewScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mBoundaryColor = Color.TRANSPARENT;
        mBoundaryStrokeWidth = 2f;

        mCornerBoundaryColor = Color.WHITE;
        //扫描区域边线样式-线宽nce rather than calling them every time in onDraw().
        mCornerBoundaryStrokeWidth = 8f;

        mScanAnimatorDuration = 1800;

        mCornerLineLen = 50f;
        mCornerLineLenRatio = 0.06f;

        mGridDensity = 40;
        mGridLineWidth = 2;

        if (mTipTextPaint == null) {
            mTipTextPaint = new TextPaint();
            mTipTextPaint.setColor(Color.WHITE);
            mTipTextPaint.setTextSize(20);
        }

        mScanPaintGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScanPaintGrid.setStyle(Paint.Style.STROKE);
        mScanPaintGrid.setStrokeWidth(mGridLineWidth);

        mScanPaintRadio = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScanPaintRadio.setStyle(Paint.Style.FILL);
        mScanColor = Color.parseColor("#3f99ee00");


        //变换矩阵，用来处理扫描的上下扫描效果
        mScanMatrix = new Matrix();
        mScanMatrix.setTranslate(0, 30);
    }

    @Override
    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null || cameraManager.getFramingRect() == null) {
            return; // not ready yet, early draw before done configuring
        }
        if (mFrame == null) {
            mFrame = cameraManager.getFramingRect();
        }

        initCornerBoundaryStyle();
        initBoundaryStyle();
        //先绘制 边框线
        canvas.drawRect(mFrame, mLinePaint);
        //再绘制，脚线
        canvas.drawPath(mCornerBoundaryLinePath, mCornerLinePaint);
        switch (mScanStyle) {
            case STYLE_GRID:
            default:
                initGridPathAndStyle();
                initScanValueAnim(-mFrame.height(), 0);
                canvas.drawPath(mGridPath, mScanPaintGrid);
                break;
            case STYLE_RADAR:
                initRadarStyle();
                initScanValueAnim(-mFrame.height(), 0);
                canvas.drawRect(mFrame, mScanPaintRadio);
                break;
            case STYLE_HYBRID:
                initGridPathAndStyle();
                initRadarStyle();
                initScanValueAnim(-mFrame.height(), 0);
                canvas.drawPath(mGridPath, mScanPaintGrid);
                canvas.drawRect(mFrame, mScanPaintRadio);
                break;
            case STYLE_SELF_SCAN_LINE:
                initSelfScanLineStyle();
                initScanValueAnim(mFrame.top, mFrame.bottom - mSelfScanDraLineHeight);
                if (mSelfScanLineDrawable != null) {
                    mSelfScanLineDrawable.draw(canvas);
                }
                Log.d("draw", "onDraw: ");
                break;

        }
        if (!TextUtils.isEmpty(mScanTip) && mTipTextPaint != null) {
            canvas.drawText(mScanTip, (getMeasuredWidth() - mTipLength) / 2, mFrame.bottom + tipMargin2ScanBottom, mTipTextPaint);
        }

    }

    private void initBoundaryStyle() {
        if (mLinePaint == null) {
            //扫描区域的四角线框的样式
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(mBoundaryColor);
            mLinePaint.setStrokeWidth(mBoundaryStrokeWidth);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setColor(mBoundaryColor);
            mLinePaint.setStrokeWidth(mBoundaryStrokeWidth);
        }
    }

    private void initCornerBoundaryStyle() {
        // 初始化定点角的样式
        if (mCornerBoundaryLinePath == null) {
            mCornerLineLen = mFrame.width() * mCornerLineLenRatio;
            int offset = (int) (mCornerBoundaryStrokeWidth / 2);
            Rect boundaryRect = new Rect(mFrame.left - offset, mFrame.top - offset, mFrame.right + offset, mFrame.bottom + offset);
            mCornerBoundaryLinePath = new Path();
            mCornerBoundaryLinePath.moveTo(boundaryRect.left, boundaryRect.top + mCornerLineLen);
            mCornerBoundaryLinePath.lineTo(boundaryRect.left, boundaryRect.top);
            mCornerBoundaryLinePath.lineTo(boundaryRect.left + mCornerLineLen, boundaryRect.top);
            mCornerBoundaryLinePath.moveTo(boundaryRect.right - mCornerLineLen, boundaryRect.top);
            mCornerBoundaryLinePath.lineTo(boundaryRect.right, boundaryRect.top);
            mCornerBoundaryLinePath.lineTo(boundaryRect.right, boundaryRect.top + mCornerLineLen);
            mCornerBoundaryLinePath.moveTo(boundaryRect.right, boundaryRect.bottom - mCornerLineLen);
            mCornerBoundaryLinePath.lineTo(boundaryRect.right, boundaryRect.bottom);
            mCornerBoundaryLinePath.lineTo(boundaryRect.right - mCornerLineLen, boundaryRect.bottom);
            mCornerBoundaryLinePath.moveTo(boundaryRect.left + mCornerLineLen, boundaryRect.bottom);
            mCornerBoundaryLinePath.lineTo(boundaryRect.left, boundaryRect.bottom);
            mCornerBoundaryLinePath.lineTo(boundaryRect.left, boundaryRect.bottom - mCornerLineLen);
        }
        if (mCornerLinePaint == null) {
            mCornerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCornerLinePaint.setColor(mCornerBoundaryColor);
            mCornerLinePaint.setStrokeWidth(mCornerBoundaryStrokeWidth);
            mCornerLinePaint.setStyle(Paint.Style.STROKE);
            mCornerLinePaint.setColor(mCornerBoundaryColor);
            mCornerLinePaint.setStrokeWidth(mCornerBoundaryStrokeWidth);
        }
    }

    private void initRadarStyle() {
        if (mLinearGradientRadar == null) {
            mLinearGradientRadar = new LinearGradient(0, mFrame.top, 0, mFrame.bottom + 0.01f * mFrame.height(), new int[]{Color.TRANSPARENT, Color.TRANSPARENT, mScanColor, Color.TRANSPARENT}, new float[]{0, 0.85f, 0.99f, 1f}, LinearGradient.TileMode.CLAMP);
            mLinearGradientRadar.setLocalMatrix(mScanMatrix);
            mScanPaintRadio.setShader(mLinearGradientRadar);
        }
    }

    /**
     * Init self scan line style.
     */
    public void initSelfScanLineStyle() {
        if (mSelfScanLineDrawable != null && mSelfScanDraLineRect == null) {
            mSelfScanDraLineHeight = mSelfScanDraLineHeight == 0 ? 50 : mSelfScanDraLineHeight;
            mSelfScanDraLineRect = new Rect(mFrame.left, mFrame.top, mFrame.right, mFrame.top + mSelfScanDraLineHeight);
            mSelfScanLineDrawable.setBounds(mSelfScanDraLineRect);
        }
    }

    private void initGridPathAndStyle() {
        if (mGridPath == null) {
            mGridPath = new Path();
            float wUnit = mFrame.width() / (mGridDensity + 0f);
            float hUnit = mFrame.height() / (mGridDensity + 0f);
            for (int i = 0; i <= mGridDensity; i++) {
                mGridPath.moveTo(mFrame.left + i * wUnit, mFrame.top);
                mGridPath.lineTo(mFrame.left + i * wUnit, mFrame.bottom);
            }
            for (int i = 0; i <= mGridDensity; i++) {
                mGridPath.moveTo(mFrame.left, mFrame.top + i * hUnit);
                mGridPath.lineTo(mFrame.right, mFrame.top + i * hUnit);
            }
        }
        if (mLinearGradientGrid == null) {
            mLinearGradientGrid = new LinearGradient(0, mFrame.top, 0, mFrame.bottom + 0.01f * mFrame.height(), new int[]{Color.TRANSPARENT, Color.TRANSPARENT, mScanColor, Color.TRANSPARENT}, new float[]{0, 0.5f, 0.99f, 1f}, LinearGradient.TileMode.CLAMP);
            mLinearGradientGrid.setLocalMatrix(mScanMatrix);
            mScanPaintGrid.setShader(mLinearGradientGrid);

        }
    }

    /**
     * Init scan value anim.
     *
     * @param startVal the start val
     * @param endVal   the end val
     */
    public void initScanValueAnim(int startVal, int endVal) {
        if (mValueAnimator == null) {
            mValueAnimator = new ValueAnimator();
            mValueAnimator.setDuration(mScanAnimatorDuration);
            mValueAnimator.setFloatValues(startVal, endVal);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.setInterpolator(new DecelerateInterpolator());
            mValueAnimator.setRepeatCount(Animation.INFINITE);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mScanMatrix != null) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        mScanMatrix.setTranslate(0, animatedValue);
                        if (mLinearGradientGrid != null) {
                            mLinearGradientGrid.setLocalMatrix(mScanMatrix);
                        }
                        if (mLinearGradientRadar != null) {
                            mLinearGradientRadar.setLocalMatrix(mScanMatrix);
                        }
                        if (mSelfScanDraLineRect != null) {
                            mSelfScanDraLineRect.set(mSelfScanDraLineRect.left, (int) animatedValue, mSelfScanDraLineRect.right, (int) (animatedValue + mSelfScanDraLineHeight));
                            mSelfScanLineDrawable.setBounds(mSelfScanDraLineRect);
                        }
                        //mScanPaint.setShader(mLinearGradient); 不是必须的设置到shader即可
                        invalidate();
                    }
                }
            });
            mValueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    /**
     * 设定扫描的颜色
     *
     * @param colorValue the color value
     */
    public void setScanColor(int colorValue) {
        this.mScanColor = colorValue;
    }

    /**
     * Sets corner boundary style.
     *
     * @param colorValue         :扫描区域角边框的颜色，
     * @param strokeWidth        ：扫描区域外边框的线宽，
     * @param cornerLineLenRatio ：扫描区域外边框四角的线长比例
     */
    public void setCornerBoundaryStyle(int colorValue, float strokeWidth, float cornerLineLenRatio) {
        this.mCornerBoundaryColor = colorValue;
        this.mCornerBoundaryStrokeWidth = strokeWidth;
        this.mCornerLineLenRatio = cornerLineLenRatio;
    }

    /**
     * 扫描区域的样式
     *
     * @param scanStyle the scan style
     */
    public void setScanStyle(int scanStyle) {
        this.mScanStyle = scanStyle;
    }


    /**
     * 设置drawable的扫描线，设置后则自动设定当前的扫描样式 ： 3
     *
     * @param scanLineDrawableRes the scan line drawable res
     * @param lineHeight          the line height
     */
    @SuppressWarnings("unused")
    public void setScanStyleDrawableLine(int scanLineDrawableRes, int lineHeight) {
        mSelfScanLineDrawable = ContextCompat.getDrawable(getContext(), scanLineDrawableRes);
        mSelfScanDraLineHeight = lineHeight;
        setScanStyle(STYLE_SELF_SCAN_LINE);
    }

    /**
     * 扫描区域网格的样式
     *
     * @param strokeWidth width
     * @param density     density
     */
    @SuppressWarnings("unused")
    public void setScanGridStyle(float strokeWidth, int density) {
        this.mGridLineWidth = strokeWidth;
        this.mGridDensity = density;
    }

    /**
     * Sets boundary style.
     *
     * @param colorValue  扫描区域边框的颜色，
     * @param strokeWidth 扫描区域外边框的线宽
     */
    public void setBoundaryStyle(int colorValue, float strokeWidth) {
        this.mBoundaryColor = colorValue;
        this.mBoundaryStrokeWidth = strokeWidth;
    }

    /**
     * 设置扫描框下方的文字提示，不设置则不显示
     *
     * @param tip               string
     * @param margin2ScanBottom margin
     * @param textColor         colorInt
     * @param textSize          sp
     */
    public void setScanTip(String tip, float margin2ScanBottom, int textColor, int textSize) {
        this.mScanTip = tip;
        this.tipMargin2ScanBottom = margin2ScanBottom;
        if (mTipTextPaint == null) {
            mTipTextPaint = new TextPaint();
        }
        mTipTextPaint.setColor(textColor);
        mTipTextPaint.setTextSize(textSize);
        mTipLength = mTipTextPaint.measureText(tip);
    }
}