package com.example.administrator.applock.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/25.
 */

public class LockPrivate extends DataSupport{
    private boolean isRead;
    private long lookDate;
    private String picPath;
    private String packageName;

    public boolean isRead() {
        return isRead;
    }

    public long getLookDate() {
        return lookDate;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setLookDate(long lookDate) {
        this.lookDate = lookDate;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
