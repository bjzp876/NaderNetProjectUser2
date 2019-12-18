package com.nader.intelligent.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nader.intelligent.R;


/**
 * author:zhangpeng
 * date: 2019/9/9
 */
public class CircleView extends View {

    /**
     * The constant PROGRESS_PROPERTY.
     */
    public static final String PROGRESS_PROPERTY = "progress";

    private static final float MAX_VALUE = 360f;

    private static final float TICK_SHOW_TIME = 220f;

    private static final float DISAPPEAR_TIME = 300f;

    private Paint roundPaint;
    private Paint roundProgressPaint;
    private Paint textPaint;
    private Paint bitmapPaint;
    /**
     * The Oval.
     */
    RectF oval;
    private int roundColor = 0xFFCCCCCC;
    private float roundWidth = 3;
    private int roundProgressColor = 0xFF29CDFF;

    private Rect textRect;
    private RectF tickRectF;

    private int textColor = 0xFF29CDFF;

    /**
     * The Progress.
     */
    protected float progress = MAX_VALUE;

    private AnimateCallback callback;

    private Bitmap mBitmap;

    /**
     * Instantiates a new Circle view.
     *
     * @param context the context
     */
    public CircleView(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Circle view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Circle view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    private void initPaint() {
        textRect = new Rect();
        tickRectF = new RectF();
        roundPaint = new Paint();
        roundProgressPaint = new Paint();
        textPaint = new Paint();
        bitmapPaint = new Paint();
        oval = new RectF();
        roundWidth = dip2px(getContext(), 1.5f);
        if (ContextCompat.getDrawable(getContext(), R.drawable.ic_tick) != null) {
            //noinspection ConstantConditions
            mBitmap = ((BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.ic_tick))
                    .getBitmap();
        }

        roundPaint.setColor(roundColor);
        roundPaint.setStyle(Paint.Style.STROKE);
        roundPaint.setStrokeWidth(roundWidth);
        roundPaint.setAntiAlias(true);
        roundPaint.setStrokeCap(Paint.Cap.ROUND);

        roundProgressPaint.setColor(roundProgressColor);
        roundProgressPaint.setStyle(Paint.Style.STROKE);
        roundProgressPaint.setStrokeWidth(roundWidth);
        roundProgressPaint.setAntiAlias(true);
        roundProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(dip2px(getContext(), 10f));
        textPaint.setTextAlign(Paint.Align.CENTER);

        bitmapPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();

        float center = getWidth() / 2;
        float radius = (center - roundWidth / 2);

        // progress background
        roundPaint.setColor(roundColor);
        canvas.drawCircle(center, center, radius, roundPaint);

        roundProgressPaint.setColor(roundProgressColor);
        oval.set(center - radius, center - radius, center + radius, center
                + radius);
        // arc
        canvas.drawArc(oval, -90, progress, false, roundProgressPaint);

        // text
        textRect.set(
                (int) (center - radius),
                (int) (center - radius),
                (int) (center + radius),
                (int) (center + radius));

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;

        int baseLineY = (int) (textRect.centerY() - top / 2 - bottom / 2);

        int textAlpha = 255;
        if (progress >= DISAPPEAR_TIME && progress <= MAX_VALUE) {
            textAlpha = (int) ((progress - DISAPPEAR_TIME) / (MAX_VALUE - DISAPPEAR_TIME) * 255f * 0.75) + 63;
        } else if (progress < DISAPPEAR_TIME && progress > 0) {
            textAlpha = 64;
        }
        textPaint.setAlpha(textAlpha);

        canvas.drawText(getResources().getString(R.string.action_execute),
                textRect.centerX(),
                baseLineY,
                textPaint);

        // show tick
        if (progress >= TICK_SHOW_TIME && progress < MAX_VALUE) {
            // f(x) = 0.1x + 0.3 线性缩放
            float x = (progress - TICK_SHOW_TIME) / (MAX_VALUE - TICK_SHOW_TIME);

            float percent = -0.9f * x * x + 1.2f * x + 0.10f;

            int bitmapAlpha = 255;
            if (progress >= DISAPPEAR_TIME && progress <= MAX_VALUE) {
                bitmapAlpha = (int) ((1 - (progress - DISAPPEAR_TIME) / (MAX_VALUE - DISAPPEAR_TIME)) * 255);
            }

            bitmapPaint.setAlpha(bitmapAlpha);
            tickRectF.set((1 - percent) * center,
                    (1 - 0.75f * percent) * center,
                    (1 + percent) * center,
                    (1 + 0.75f * percent) * center);
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, null, tickRectF, bitmapPaint);
            }
        }
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Sets progress.
     *
     * @param progress the progress
     */
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    private boolean enable = true;

    /**
     * Sets enable.
     *
     * @param enable the enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
        if (!enable) {
            this.textColor = 0xFFCCCCCC;
            this.roundProgressColor = 0xFFCCCCCC;
        } else {
            this.textColor = 0xFF29CDFF;
            this.roundProgressColor = 0xFF29CDFF;
        }
        invalidate();
    }

    /**
     * Dip 2 px int.
     *
     * @param context the context
     * @param dpValue the dp value
     * @return the int
     */
    int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Execute.
     */
    public void execute() {
        if (!enable) {
            return;
        }

        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this, PROGRESS_PROPERTY, 0f, MAX_VALUE);
        progressAnimation.setDuration(800);

        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.playTogether(progressAnimation);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (callback != null) {
                    callback.onStop();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animation.start();
    }

    /**
     * Sets callback.
     *
     * @param callback the callback
     */
    public void setCallback(AnimateCallback callback) {
        this.callback = callback;
    }

    /**
     * The interface Animate callback.
     */
    public interface AnimateCallback {

        /**
         * Call on when animation stops.
         */
        void onStop();
    }

}
