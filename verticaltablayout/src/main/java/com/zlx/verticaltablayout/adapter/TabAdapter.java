package com.zlx.verticaltablayout.adapter;

import com.zlx.verticaltablayout.widget.TabView;

/**
 * Created by zlx on 2017/7/10.
 */

public interface TabAdapter {

    int getCount();

    TabView.TabBadge getBadge(int position);

    TabView.TabIcon getIcon(int position);

    TabView.TabTitle getTitle(int position);

    int getBackground(int position);

}
