package com.zlx.verticaltab;

import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.clj.badgeview.Badge;
import com.zlx.verticaltablayout.widget.ITabView;
import com.zlx.verticaltablayout.widget.QTabView;
import com.zlx.verticaltablayout.widget.TabView;

public class FloatBadgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();

    }
    private void initView(){
        LinearLayout layout_main = new LinearLayout(this);
        layout_main.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout_main.setOrientation(LinearLayout.VERTICAL);
        ITabView.TabTitle tabTitle=new TabView.TabTitle.Builder()
                .setContent("")
                .setTextColor(0xFF36BC9B, 0xFF757575)
                .build();
        final Drawable drawable = getResources().getDrawable(R.drawable.floatbtn);
        ITabView.TabBadge tabBadge=new TabView.TabBadge.Builder().setBadgeNumber(1).setDrawableBackground(drawable,false)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                       /* final PopupMenu popupMenu = new PopupMenu(FloatBadgeActivity.this, ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0), GravityCompat.END);
                        popupMenu.inflate(R.menu.group_pop);
                        popupMenu.show();*/
                    }
                }).build();
        QTabView qTabView= new QTabView(this).setIcon(null)
                .setTitle(tabTitle).setBadge(tabBadge)
                .setBackground(0xff2faae5);
        layout_main.addView(qTabView);
        setContentView(layout_main);
    }
}
