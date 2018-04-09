package com.zlx.verticaltab;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zlx.verticaltablayout.VerticalTabLayout;
import com.zlx.verticaltablayout.adapter.TabAdapter;
import com.zlx.verticaltablayout.widget.ITabView;
import com.zlx.verticaltablayout.widget.QTabView;
import com.zlx.verticaltablayout.widget.TabBadgeView;
import com.zlx.verticaltablayout.widget.TabView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clj.badgeview.Badge;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyPagerAdapter());

     /*   initTab0();
        initTab1();
        initTab2();
        initTab3();*/
        initFloating();
    }
    private void initFloating(){
        final Drawable drawable = getResources().getDrawable(R.drawable.floatbtn);
        final QTabView qTabView= new QTabView(this);
        ITabView.TabBadge tabBadge=new TabView.TabBadge.Builder().setBadgeNumber(1).setDrawableBackground(drawable,false)
                .setBadgeTextColor(Color.RED)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if(dragState==STATE_DOWNACTION){
                            Log.d(this.getClass().getName(),"+++++++++++STATE_DOWNACTION++++++++++++=="+dragState);
                            /*final android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(MainActivity.this, (View)qTabView.getBadgeView(), GravityCompat.END);
                            popupMenu.inflate(com.zlx.verticaltablayout.R.menu.group_pop);
                            popupMenu.show();*/
                        }
                    }
                }).build();
        qTabView.setBadge(tabBadge)
                .setBackground(0xff2faae5);
        ViewGroup view= (ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content));
        view.addView(qTabView);


    }

    private void initTab3() {
        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout);
        tablayout.setTabAdapter(new MyTabAdapter());
    }

    private void initTab2() {

        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout2);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabBadge(2, -1);
        tablayout.setTabBadge(8, -1);
        tablayout.getTabAt(3).setBadge(new TabView.TabBadge.Builder().setBadgeGravity(Gravity.START | Gravity.TOP)
                .setBadgeNumber(999)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (dragState == STATE_SUCCEED) {
                            badge.setBadgeNumber(-1).stroke(0xFFFFFFFF, 1, true);
                        }
                    }
                }).build());
    }

    private void initTab1() {
        VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout1);
        tablayout.setupWithViewPager(viewpager);
    }

    private void initTab0() {

        final VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout0);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabBadge(7, 32);
        tablayout.setTabBadge(2, -1);
        tablayout.setTabBadge(3, -1);
        tablayout.setTabBadge(4, -1);
    }


    private class MyTabAdapter implements TabAdapter {

        List<MenuBean> menus;

        public MyTabAdapter() {
            menus = new ArrayList<>();
            Collections.addAll(menus, new MenuBean(R.drawable.man_01_pressed, R.drawable.man_01_none, "汇总")
                    , new MenuBean(R.drawable.man_02_pressed, R.drawable.man_02_none, "图表")
                    , new MenuBean(R.drawable.man_03_pressed, R.drawable.man_03_none, "收藏")
                    , new MenuBean(R.drawable.man_04_pressed, R.drawable.man_04_none, "竞拍"));
        }

        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            return new TabView.TabBadge.Builder().setBadgeNumber(999).setBackgroundColor(0xff2faae5)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        }
                    }).build();
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabIcon.Builder()
                    .setIcon(menu.getmSelectIcon(), menu.getmNormalIcon())
                    .setIconGravity(Gravity.START)
                    .setIconMargin(dp2px(5))
                    .setIconSize(dp2px(20), dp2px(20))
                    .build();
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            MenuBean menu = menus.get(position);
            return new TabView.TabTitle.Builder()
                    .setContent(menu.getmTitle())
                    .setTextColor(0xFF36BC9B, 0xFF757575)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return -1;
        }

    }

    private class MyPagerAdapter extends PagerAdapter implements TabAdapter {
        List<String> titles;

        public MyPagerAdapter() {
            titles = new ArrayList<>();
            Collections.addAll(titles, "Android", "IOS", "Web", "JAVA", "C++",
                    ".NET", "JavaScript", "Swift", "PHP", "Python", "C#", "Groovy", "SQL", "Ruby");
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            if (position == 5) return new TabView.TabBadge.Builder().setBadgeNumber(666)
                    .setExactMode(true)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        }
                    }).build();
            return null;
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public TabView.TabTitle getTitle(int position) {

            return new TabView.TabTitle.Builder()
                    .setContent(titles.get(position))
                    .setTextColor(Color.WHITE, 0xBBFFFFFF)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(MainActivity.this);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setText(titles.get(position));
            tv.setTextSize(18);
            container.addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    protected int dp2px(float dp) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
