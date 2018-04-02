垂直竖向的Android TabLayout

![](https://github.com/qstumn/VerticalTabLayout/raw/master/demo.png?raw=true)


#一些特性#


支持自定义Indicator大小

支持自定义Indicator位置

支持Indicator设置圆角

支持Tab设置Badge

支持Adapter的方式创建Tab

多种Tab高度设置模式

Tab支持android:state_selected

很方便的和ViewPager结合使用

很方便的和Fragment结合使用

![](https://github.com/qstumn/VerticalTabLayout/raw/master/demo_gif.gif?raw=true)


**How to use**

#1. gradle#

 `compile 'com.zlx.verticaltablayout:1.2.4`

#2.XML#

    <com.zlx.verticaltablayout.VerticalTabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#EDEDED"
        app:indicator_color="#FFFFFF"
        app:indicator_gravity="fill"
        app:tab_height="50dp"
        app:tab_mode="scrollable" />


#3.属性说明#
1. 
1. xml	code	说明
1. app:indicator_color	setIndicatorColor	指示器颜色
1. app:indicator_width	setIndicatorWidth	指示器宽度
1. app:indicator_gravity	setIndicatorGravity	指示器位置
1. app:indicator_corners	setIndicatorCorners	指示器圆角
1. app:tab_mode	setTabMode	Tab高度模式
1. app:tab_height	setTabHeight	Tab高度
1. app:tab_margin	setTabMargin	Tab间距

# 4.创建Tab的方式 #

- 普通方式创建
- 
    	tablayout.addTab(new QTabView(context))
	tablayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });

- Adapter方式创建
- 



    tablayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public TabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public TabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public TabView.TabTitle getTitle(int position) {
                return null;
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
	     });







   
 按照自己的需要进行返回相应的值即可，不需要的返回0或者null
也可以选择使用SimpleTabAdapter，内部空实现了TabAdapter的所有方法
TabBadge、TabIcon、TabTitle使用build模式创建。

- 结合ViewPager使用

    tablayout.setupWithViewPager(viewpager);

ViewPager的PagerAdapter可选择实现TabAdapter接口

如果您需要使用垂直竖向的ViewPager，推荐您使用：  [https://github.com/youngkaaa/YViewPagerDemo](https://github.com/youngkaaa/YViewPagerDemo)

结合Fragment使用
  `tabLayout.setupWithFragment(FragmentManager manager, int containerResid, List<Fragment> fragments, TabAdapter adapter)`


**- 5. 设置badge**

    int tabPosition = 3;
	int badgeNum = 55;
	tablayout.setTabBadge(tabPosition,badgeNum);
	Badge badge = tablayout.getTabAt(position).getBadgeView();
	
	Badge使用方法请移步https://github.com/qstumn/BadgeView


