package com.example.administrator.applock.bean;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/24.
 */

public class CommonLockInfo extends DataSupport implements Parcelable {

    private long id;
    private String packageName;
    private String appName;
    private boolean isLocked;
    private boolean isFavirateApp;
    private ApplicationInfo appInfo;
    private boolean isSysApp;
    private String topTitle;
    private boolean isSetUnLock;

    public CommonLockInfo() {
    }

    public CommonLockInfo(String packageName, boolean isLocked, boolean isFavirateApp) {
        this.packageName = packageName;
        this.isLocked = isLocked;
        this.isFavirateApp = isFavirateApp;
    }

    public long getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isFavirateApp() {
        return isFavirateApp;
    }

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public boolean isSysApp() {
        return isSysApp;
    }

    public String getTopTitle() {
        return topTitle;
    }

    public boolean isSetUnLock() {
        return isSetUnLock;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void setFavirateApp(boolean favirateApp) {
        isFavirateApp = favirateApp;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public void setSysApp(boolean sysApp) {
        isSysApp = sysApp;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }

    public void setSetUnLock(boolean setUnLock) {
        isSetUnLock = setUnLock;
    }

    protected CommonLockInfo(Parcel in) {
        this.id = in.readLong();
        this.packageName = in.readString();
        this.appName = in.readString();
        this.isLocked = in.readByte() != 0;
        this.isFavirateApp = in.readByte() != 0;
        this.appInfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
        this.isSysApp = in.readByte() != 0;
        this.topTitle = in.readString();
        this.isSetUnLock = in.readByte() != 0;
    }

    public static final Creator<CommonLockInfo> CREATOR = new Creator<CommonLockInfo>() {
        @Override
        public CommonLockInfo createFromParcel(Parcel in) {
            return new CommonLockInfo(in);
        }

        @Override
        public CommonLockInfo[] newArray(int size) {
            return new CommonLockInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.packageName);
        parcel.writeString(this.appName);
        parcel.writeByte(this.isLocked ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isFavirateApp ? (byte) 1 : (byte) 0);
        parcel.writeParcelable(this.appInfo, i);
        parcel.writeByte(this.isSysApp ? (byte) 1 : (byte) 0);
        parcel.writeString(this.topTitle);
        parcel.writeByte(this.isSetUnLock ? (byte) 1 : (byte) 0);

    }
}
