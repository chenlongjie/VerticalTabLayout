package com.clj.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.clj.badgeview.DisplayUtil;
import com.clj.badgeview.Floating;
import com.clj.badgeview.FloatingAnimator;

import java.util.ArrayList;
import java.util.List;

public class CLJFloatView extends View implements Floating {
    protected Drawable mDrawableBackground;
    protected Bitmap mBitmapClip;
    protected boolean mDrawableBackgroundClip;
    protected float mBackgroundBorderWidth;
    protected float mFloatingPadding;
    protected boolean mDraggable;
    protected boolean mDragging;
    protected boolean mExact;
    protected boolean mShowShadow;
    protected int mFloatingGravity;
    protected float mGravityOffsetX;
    protected float mGravityOffsetY;

    protected float mDefalutRadius;
    protected float mFinalDragDistance;
    protected int mDragQuadrant;
    protected boolean mDragOutOfRange;

    protected RectF mFloatingBackgroundRect;
    protected Path mDragPath;

    protected Paint.FontMetrics mFloatingTextFontMetrics;

    protected PointF mFloatingCenter;
    protected PointF mDragCenter;
    protected PointF mRowFloatingCenter;
    protected PointF mControlPoint;

    protected List<PointF> mInnertangentPoints;

    protected View mTargetView;

    protected int mWidth;
    protected int mHeight;

    protected Paint mFloatingBackgroundPaint;
    protected Paint mFloatingBackgroundBorderPaint;

    protected FloatingAnimator mAnimator;

    protected Floating.OnDragStateChangedListener mDragStateChangedListener;

    protected ViewGroup mActivityRoot;

    public CLJFloatView(Context context) {
        this(context, null);
    }

    private CLJFloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private CLJFloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mFloatingBackgroundRect = new RectF();
        mDragPath = new Path();
        mFloatingCenter = new PointF();
        mDragCenter = new PointF();
        mRowFloatingCenter = new PointF();
        mControlPoint = new PointF();
        mInnertangentPoints = new ArrayList<>();
        mFloatingBackgroundPaint = new Paint();
        mFloatingBackgroundPaint.setAntiAlias(true);
        mFloatingBackgroundPaint.setStyle(Paint.Style.FILL);
        mFloatingBackgroundBorderPaint = new Paint();
        mFloatingBackgroundBorderPaint.setAntiAlias(true);
        mFloatingBackgroundBorderPaint.setStyle(Paint.Style.STROKE);
        mFloatingPadding = DisplayUtil.dp2px(getContext(), 5);
        mFloatingGravity = Gravity.CENTER | Gravity.START;
        mGravityOffsetX = DisplayUtil.dp2px(getContext(), 1);
        mGravityOffsetY = DisplayUtil.dp2px(getContext(), 1);
        mFinalDragDistance = DisplayUtil.dp2px(getContext(), 90);
        mShowShadow = true;
        mDrawableBackgroundClip = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslationZ(1000);//Z轴立体值
        }
    }

    public void reset() {
        mDragCenter.x = -1000;
        mDragCenter.y = -1000;
        mDragQuadrant = 4;
        screenFromWindow(false);
        getParent().requestDisallowInterceptTouchEvent(false);
        invalidate();
    }
    private void initRowFloatingCenter() {
        int[] screenPoint = new int[2];
        getLocationOnScreen(screenPoint);
        mRowFloatingCenter.x = mFloatingCenter.x + screenPoint[0];
        mRowFloatingCenter.y = mFloatingCenter.y + screenPoint[1];
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (mDraggable && event.getPointerId(event.getActionIndex()) == 0
                        && (x > mFloatingBackgroundRect.left && x < mFloatingBackgroundRect.right &&
                        y > mFloatingBackgroundRect.top && y < mFloatingBackgroundRect.bottom)) {
                    initRowFloatingCenter();
                    mDragging = true;
                    updataListener(Floating.OnDragStateChangedListener.STATE_START);
                    mDefalutRadius = DisplayUtil.dp2px(getContext(), 7);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    screenFromWindow(true);
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (event.getPointerId(event.getActionIndex()) == 0 && mDragging) {
                    mDragging = false;
//                    onPointerUp();
                }
                break;
        }
        return mDragging || super.onTouchEvent(event);
    }
    private void drawFloating(Canvas canvas, PointF center, float radius) {
        /*if (center.x == -1000 && center.y == -1000) {
            return;
        }
        if (mFloatingText.isEmpty() || mFloatingText.length() == 1) {
            mFloatingBackgroundRect.left = center.x - (int) radius;
            mFloatingBackgroundRect.top = center.y - (int) radius;
            mFloatingBackgroundRect.right = center.x + (int) radius;
            mFloatingBackgroundRect.bottom = center.y + (int) radius;
            if (mDrawableBackground != null) {
                drawFloatingBackground(canvas);
            } else {
                canvas.drawCircle(center.x, center.y, radius, mFloatingBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawCircle(center.x, center.y, radius, mFloatingBackgroundBorderPaint);
                }
            }
        } else {
            mFloatingBackgroundRect.left = center.x - (mFloatingTextRect.width() / 2f + mFloatingPadding);
            mFloatingBackgroundRect.top = center.y - (mFloatingTextRect.height() / 2f + mFloatingPadding * 0.5f);
            mFloatingBackgroundRect.right = center.x + (mFloatingTextRect.width() / 2f + mFloatingPadding);
            mFloatingBackgroundRect.bottom = center.y + (mFloatingTextRect.height() / 2f + mFloatingPadding * 0.5f);
            radius = mFloatingBackgroundRect.height() / 2f;
            if (mDrawableBackground != null) {
                drawFloatingBackground(canvas);
            } else {
                canvas.drawRoundRect(mFloatingBackgroundRect, radius, radius, mFloatingBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawRoundRect(mFloatingBackgroundRect, radius, radius, mFloatingBackgroundBorderPaint);
                }
            }
        }
        if (!mFloatingText.isEmpty()) {
            canvas.drawText("", center.x,
                    (mFloatingBackgroundRect.bottom + mFloatingBackgroundRect.top
                            - mFloatingTextFontMetrics.bottom - mFloatingTextFontMetrics.top) / 2f,
                    mFloatingTextPaint);
        }*/
    }
    protected Bitmap createFloatingBitmap() {
        Bitmap bitmap = Bitmap.createBitmap((int) mFloatingBackgroundRect.width() + DisplayUtil.dp2px(getContext(), 3),
                (int) mFloatingBackgroundRect.height() + DisplayUtil.dp2px(getContext(), 3), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//        drawFloating(canvas, new PointF(canvas.getWidth() / 2f, canvas.getHeight() / 2f), getFloatingCircleRadius());
        return bitmap;
    }

    protected void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            bindTarget(mTargetView);
        }
    }

    @Override
    public void hide(boolean animate) {
       /* if (animate && mActivityRoot != null) {
            initRowFloatingCenter();
            animateHide(mRowFloatingCenter);
        } else {
            setFloatingNumber(0);
        }*/
    }


    @Override
    public Floating setExactMode(boolean isExact) {
        mExact = isExact;
        return this;
    }

    @Override
    public boolean isExactMode() {
        return mExact;
    }

    @Override
    public Floating setShowShadow(boolean showShadow) {
        mShowShadow = showShadow;
        invalidate();
        return this;
    }

    @Override
    public boolean isShowShadow() {
        return mShowShadow;
    }

    @Override
    public Floating setFloatingBackgroundColor(int color) {
       /* mColorBackground = color;
        if (mColorBackground == Color.TRANSPARENT) {
            mFloatingTextPaint.setXfermode(null);
        } else {
            mFloatingTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
        invalidate();*/
        return this;
    }

    @Override
    public Floating stroke(int color, float width, boolean isDpValue) {
//        mColorBackgroundBorder = color;
        mBackgroundBorderWidth = isDpValue ? DisplayUtil.dp2px(getContext(), width) : width;
        invalidate();
        return this;
    }

    @Override
    public int getFloatingBackgroundColor() {
        return 0;
    }

    @Override
    public Floating setFloatingBackground(Drawable drawable) {
        return setFloatingBackground(drawable, false);
    }

    @Override
    public Floating setFloatingBackground(Drawable drawable, boolean clip) {
        mDrawableBackgroundClip = clip;
        mDrawableBackground = drawable;
//        createClipLayer();
        invalidate();
        return this;
    }

    @Override
    public Drawable getFloatingBackground() {
        return mDrawableBackground;
    }


    @Override
    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM ,
     *                Gravity.CENTER , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ,
     *                Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END
     */
    @Override
    public Floating setFloatingGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER) ||
                gravity == (Gravity.CENTER | Gravity.TOP) ||
                gravity == (Gravity.CENTER | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER | Gravity.START) ||
                gravity == (Gravity.CENTER | Gravity.END)) {
            mFloatingGravity = gravity;
            invalidate();
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER" +
                    " , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ," +
                    "Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END");
        }
        return this;
    }

    @Override
    public int getFloatingGravity() {
        return mFloatingGravity;
    }

    @Override
    public Floating setGravityOffset(float offset, boolean isDpValue) {
        return setGravityOffset(offset, offset, isDpValue);
    }

    @Override
    public Floating setGravityOffset(float offsetX, float offsetY, boolean isDpValue) {
        mGravityOffsetX = isDpValue ? DisplayUtil.dp2px(getContext(), offsetX) : offsetX;
        mGravityOffsetY = isDpValue ? DisplayUtil.dp2px(getContext(), offsetY) : offsetY;
        invalidate();
        return this;
    }

    @Override
    public float getGravityOffsetX(boolean isDpValue) {
        return isDpValue ? DisplayUtil.px2dp(getContext(), mGravityOffsetX) : mGravityOffsetX;
    }

    @Override
    public float getGravityOffsetY(boolean isDpValue) {
        return isDpValue ? DisplayUtil.px2dp(getContext(), mGravityOffsetY) : mGravityOffsetY;
    }


    private void updataListener(int state) {
        if (mDragStateChangedListener != null)
            mDragStateChangedListener.onDragStateChanged(state, this, mTargetView);
    }

    @Override
    public Floating setOnDragStateChangedListener(Floating.OnDragStateChangedListener l) {
        mDraggable = l != null;
        mDragStateChangedListener = l;
        return this;
    }

    @Override
    public PointF getDragCenter() {
        if (mDraggable && mDragging) return mDragCenter;
        return null;
    }

    @Override
    public Floating bindTarget(View view) {
        return null;
    }

    @Override
    public View getTargetView() {
        return null;
    }
}
