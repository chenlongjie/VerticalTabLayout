package com.zlx.verticaltab;

/**
 * Created by zlx on 2017/7/10.
 */

public class MenuBean {


    private int mSelectIcon;

    private int mNormalIcon;

    private String mTitle;


    public int getmSelectIcon() {
        return mSelectIcon;
    }

    public void setmSelectIcon(int mSelectIcon) {
        this.mSelectIcon = mSelectIcon;
    }

    public int getmNormalIcon() {
        return mNormalIcon;
    }

    public void setmNormalIcon(int mNormalIcon) {
        this.mNormalIcon = mNormalIcon;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public MenuBean(int mSelectIcon, int mNormalIcon, String mTitle) {
        this.mSelectIcon = mSelectIcon;
        this.mNormalIcon = mNormalIcon;
        this.mTitle = mTitle;
    }
}
