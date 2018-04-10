package com.zlx.verticaltablayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.badgeview.Badge;
import com.clj.badgeview.DisplayUtil;
import com.zlx.verticaltablayout.R;

/**
 * Created by zlx on 2017/7/10.
 */
public class QTabView extends TabView {
    private Context mContext;
    private TextView mTitle;
    private Badge mBadgeView;
    private TabIcon mTabIcon;
    private TabTitle mTabTitle;
    private TabBadge mTabBadge;
    private boolean mChecked;
    private boolean mVisiable;
    private Drawable mDefaultBackground;
    private int mWidth;

    public QTabView(Context context) {
        super(context);
        mContext = context;
        mTabIcon = new TabIcon.Builder().build();
        mTabTitle = new TabTitle.Builder().build();
        mTabBadge = new TabBadge.Builder().build();
        initView();
        int[] attrs;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
        } else {
            attrs = new int[]{android.R.attr.selectableItemBackground};
        }
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs);
        mDefaultBackground = a.getDrawable(0);
        a.recycle();
        setDefaultBackground();
    }

    private void initView() {
        setMinimumHeight(DisplayUtil.dp2px(mContext, 25));

//        initTitleView();
//        initIconView();
        initBadge();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setPaddingRelative(@Px int start, @Px int top, @Px int end, @Px int bottom) {
        mTitle.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
        mTitle.setPadding(left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    private void initBadge() {
        mBadgeView = TabBadgeView.bindTab(this);
        mBadgeView.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                switch (dragState) {
                    case STATE_DOWNACTION:
                        if (mVisiable) {
                            mTitle.setVisibility(GONE);
                            mVisiable = false;
                        } else {
                            mVisiable = true;
                            PointF pointF = badge.getDragCenter();
                            int gravity = badge.getBadgeGravity();
                            initTitleView(pointF, gravity);
                        }
                        break;
                    case STATE_DRAGGING:
                    case STATE_SUCCEED:
                        if (mTitle != null) {
                            mVisiable = false;
                            mTitle.setVisibility(GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        if (mTabBadge.getBackgroundColor() != 0xFFE84E40) {
            mBadgeView.setBadgeBackgroundColor(mTabBadge.getBackgroundColor());
        }
        if (mTabBadge.getBadgeTextColor() != 0xFFFFFFFF) {
            mBadgeView.setBadgeTextColor(mTabBadge.getBadgeTextColor());
        }
        if (mTabBadge.getStrokeColor() != Color.TRANSPARENT || mTabBadge.getStrokeWidth() != 0) {
            mBadgeView.stroke(mTabBadge.getStrokeColor(), mTabBadge.getStrokeWidth(), true);
        }
        if (mTabBadge.getDrawableBackground() != null || mTabBadge.isDrawableBackgroundClip()) {
            mBadgeView.setBadgeBackground(mTabBadge.getDrawableBackground(), mTabBadge.isDrawableBackgroundClip());
        }
        if (mTabBadge.getBadgeTextSize() != 11) {
            mBadgeView.setBadgeTextSize(mTabBadge.getBadgeTextSize(), true);
        }
        if (mTabBadge.getBadgePadding() != 5) {
            mBadgeView.setBadgePadding(mTabBadge.getBadgePadding(), true);
        }
        if (mTabBadge.getBadgeNumber() != 0) {
            mBadgeView.setBadgeNumber(mTabBadge.getBadgeNumber());
        }
        if (mTabBadge.getBadgeText() != null) {
            mBadgeView.setBadgeText(mTabBadge.getBadgeText());
        }
        if (mTabBadge.getBadgeGravity() != (Gravity.END | Gravity.TOP)) {
            mBadgeView.setBadgeGravity(mTabBadge.getBadgeGravity());
        }
        if (mTabBadge.getGravityOffsetX() != 5 || mTabBadge.getGravityOffsetY() != 5) {
            mBadgeView.setGravityOffset(mTabBadge.getGravityOffsetX(), mTabBadge.getGravityOffsetY(), true);
        }
        if (mTabBadge.isExactMode()) {
            mBadgeView.setExactMode(mTabBadge.isExactMode());
        }
        if (!mTabBadge.isShowShadow()) {
            mBadgeView.setShowShadow(mTabBadge.isShowShadow());
        }
        if (mTabBadge.getOnDragStateChangedListener() != null) {
            mBadgeView.setOnDragStateChangedListener(mTabBadge.getOnDragStateChangedListener());
        }
    }

    private void initTitleView(PointF pointF, int mBadgeGravity) {
        if (mTitle == null) {
            mTitle = new TextView(mContext);
            this.addView(mTitle);
        }
        mTitle.setTextColor(isChecked() ? mTabTitle.getColorSelected() : mTabTitle.getColorNormal());
        mTitle.setTextSize(mTabTitle.getTitleTextSize());
        mTitle.setText(mTabTitle.getContent());
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        switch (mBadgeGravity) {
            case Gravity.END:
                int gX = (int) pointF.x;
                int marginX = mWidth - gX;
                mTitle.setGravity(Gravity.END);
                params.setMargins(0, (int) pointF.y, marginX + DisplayUtil.dp2px(getContext(), 25f), 0);
                break;
            case Gravity.START:
            case Gravity.CENTER | Gravity.START:
                mTitle.setGravity(Gravity.START);
                params.setMargins((int) pointF.x + DisplayUtil.dp2px(getContext(), 25f), (int) pointF.y, 0, 0);
                break;
        }
        mTitle.setLayoutParams(params);
        mTitle.setVisibility(VISIBLE);
        refreshDrawablePadding();
    }

    private void initIconView() {
        int iconResid = mChecked ? mTabIcon.getSelectedIcon() : mTabIcon.getNormalIcon();
        Drawable drawable = null;
        if (iconResid != 0) {
            drawable = mContext.getResources().getDrawable(iconResid);
            int r = mTabIcon.getIconWidth() != -1 ? mTabIcon.getIconWidth() : drawable.getIntrinsicWidth();
            int b = mTabIcon.getIconHeight() != -1 ? mTabIcon.getIconHeight() : drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, r, b);
        }
        switch (mTabIcon.getIconGravity()) {
            case Gravity.START:
                mTitle.setCompoundDrawables(drawable, null, null, null);
                break;
            case Gravity.TOP:
                mTitle.setCompoundDrawables(null, drawable, null, null);
                break;
            case Gravity.END:
                mTitle.setCompoundDrawables(null, null, drawable, null);
                break;
            case Gravity.BOTTOM:
                mTitle.setCompoundDrawables(null, null, null, drawable);
                break;
        }
        refreshDrawablePadding();
    }

    private void refreshDrawablePadding() {
        int iconResid = mChecked ? mTabIcon.getSelectedIcon() : mTabIcon.getNormalIcon();
        if (iconResid != 0) {
            if (!TextUtils.isEmpty(mTabTitle.getContent()) && mTitle.getCompoundDrawablePadding() != mTabIcon.getMargin()) {
                mTitle.setCompoundDrawablePadding(mTabIcon.getMargin());
            } else if (TextUtils.isEmpty(mTabTitle.getContent())) {
                mTitle.setCompoundDrawablePadding(0);
            }
        } else {
            mTitle.setCompoundDrawablePadding(0);
        }
    }

    @Override
    public QTabView setBadge(TabBadge badge) {
        if (badge != null) {
            mTabBadge = badge;
        }
        initBadge();
        return this;
    }

    @Override
    public QTabView setIcon(TabIcon icon) {
        if (icon != null) {
            mTabIcon = icon;
        }
        initIconView();
        return this;
    }

    @Override
    public QTabView setTitle(TabTitle title) {
        if (title != null) {
            mTabTitle = title;
        }
//        initTitleView();
        return this;
    }

    /**
     * @param resId The Drawable res to use as the background, if less than 0 will to remove the
     *              background
     */
    @Override
    public QTabView setBackground(int resId) {
        if (resId == 0) {
            setDefaultBackground();
        } else if (resId <= 0) {
            setBackground(null);
        } else {
            super.setBackgroundResource(resId);
        }
        return this;
    }

    @Override
    public TabBadge getBadge() {
        return mTabBadge;
    }

    @Override
    public TabIcon getIcon() {
        return mTabIcon;
    }

    @Override
    public TabTitle getTitle() {
        return mTabTitle;
    }

    @Override
    @Deprecated
    public ImageView getIconView() {
        return null;
    }

    @Override
    public TextView getTitleView() {
        return mTitle;
    }

    @Override
    public Badge getBadgeView() {
        return mBadgeView;
    }

    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        } else {
            super.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundResource(int resid) {
        setBackground(resid);
    }

    private void setDefaultBackground() {
        if (getBackground() != mDefaultBackground) {
            setBackground(mDefaultBackground);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setSelected(checked);
        refreshDrawableState();
        mTitle.setTextColor(checked ? mTabTitle.getColorSelected() : mTabTitle.getColorNormal());
        initIconView();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
