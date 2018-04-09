package com.clj.badgeview;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface Floating {

    Floating setExactMode(boolean isExact);

    boolean isExactMode();

    Floating setShowShadow(boolean showShadow);

    boolean isShowShadow();

    Floating setFloatingBackgroundColor(int color);

    Floating stroke(int color, float width, boolean isDpValue);

    int getFloatingBackgroundColor();

    Floating setFloatingBackground(Drawable drawable);

    Floating setFloatingBackground(Drawable drawable, boolean clip);

    Drawable getFloatingBackground();

    boolean isDraggable();

    Floating setFloatingGravity(int gravity);

    int getFloatingGravity();

    Floating setGravityOffset(float offset, boolean isDpValue);

    Floating setGravityOffset(float offsetX, float offsetY, boolean isDpValue);

    float getGravityOffsetX(boolean isDpValue);

    float getGravityOffsetY(boolean isDpValue);

    Floating setOnDragStateChangedListener(Floating.OnDragStateChangedListener l);

    PointF getDragCenter();

    Floating bindTarget(View view);

    View getTargetView();
    void hide(boolean animate);
    interface OnDragStateChangedListener {
        int STATE_START = 1;
        int STATE_DRAGGING = 2;
        int STATE_DRAGGING_OUT_OF_RANGE = 3;
        int STATE_CANCELED = 4;
        int STATE_SUCCEED = 5;

        void onDragStateChanged(int dragState, Floating floating, View targetView);
    }
}
