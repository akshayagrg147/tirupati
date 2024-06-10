package com.tirupati.vendor.views;

import android.os.Parcel;
import android.os.Parcelable;

public class ToolbarConfig implements Parcelable {

    public static final Creator<ToolbarConfig> CREATOR = new Creator<ToolbarConfig>() {
        @Override
        public ToolbarConfig createFromParcel(Parcel in) {
            return new ToolbarConfig(in);
        }

        @Override
        public ToolbarConfig[] newArray(int size) {
            return new ToolbarConfig[size];
        }
    };
    private String title;
    private boolean showBackBtn;
    private boolean hideNotificationBtn;
    private boolean hideCartBtn;
    private boolean showMenuBtn;
    private boolean showSearchView;
    private boolean hideSearchView;
    private boolean showSearchBtn;
    private boolean showLogo;

    protected ToolbarConfig(Parcel in) {
        title = in.readString();
        showBackBtn = in.readByte() != 0;
        showLogo = in.readByte() != 0;
        hideNotificationBtn = in.readByte() != 0;
        hideCartBtn = in.readByte() != 0;
        showMenuBtn = in.readByte() != 0;
        showSearchView = in.readByte() != 0;
        showSearchBtn = in.readByte() != 0;
    }
    public ToolbarConfig() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (showBackBtn ? 1 : 0));
        dest.writeByte((byte) (showLogo ? 1 : 0));
        dest.writeByte((byte) (hideNotificationBtn ? 1 : 0));
        dest.writeByte((byte) (hideCartBtn ? 1 : 0));
        dest.writeByte((byte) (showMenuBtn ? 1 : 0));
        dest.writeByte((byte) (showSearchView ? 1 : 0));
        dest.writeByte((byte) (showSearchBtn ? 1 : 0));
    }
    @Override
    public int describeContents() {
        return 0;
    }

    //set title of the page
    public String getTitle() {
        return title;
    }
    public ToolbarConfig setTitle(String title) {
        this.title = title;
        return this;
    }
    //hide or show backbutton on the toolbar
    public boolean isShowBackBtn() {
        return showBackBtn;
    }
    public ToolbarConfig setShowBackBtn(boolean showBackBtn) {
        this.showBackBtn = showBackBtn;
        return this;
    }
    public boolean isShowLogo() {
        return showLogo;
    }
    public ToolbarConfig setShowLogo(boolean showLogo) {
        this.showLogo = showLogo;
        return this;
    }

    //to show wishlist icon or hide
    public boolean isHideNotificationBtn() {
        return hideNotificationBtn;
    }
    public ToolbarConfig setHideNotificationBtn(boolean hideNotificationBtn) {
        this.hideNotificationBtn = hideNotificationBtn;
        return this;
    }

    //show or hide cart button
     public boolean istHideCartBtn() {
        return hideCartBtn;
    }
    public ToolbarConfig setHideCartBtn(boolean hideCartBtn) {
        this.hideCartBtn = hideCartBtn;
        return this;
    }


    // show and hide drawer menu icon
    public boolean isShowMenuBtn() {
        return showMenuBtn;
    }
    public ToolbarConfig setShowMenuBtn(boolean showMenuBtn) {
        this.showMenuBtn = showMenuBtn;
        return this;
    }

    ///show right icon tray
    public boolean isShowSearchView(boolean b) {
        return showSearchView;
    }
    public ToolbarConfig setShowSearchView(boolean showSearchView) {
        this.showSearchView = showSearchView;
        return this;
    }
    //hide the top right icon tray
    public boolean isHideSearchView() {
        return hideSearchView;
    }
    public ToolbarConfig setHideSearchView(boolean hideSearchView) {
        this.hideSearchView = hideSearchView;
        return this;
    }


//    public ToolbarConfig setShowMenuBtn(boolean showMenuBtn) {
//        this.showMenuBtn = showMenuBtn;
//        return this;
//    }
//    public boolean isShowSearchView(boolean b) {
//        return showSearchView;
//    }

}
