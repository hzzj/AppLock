package com.example.administrator.applock.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/25.
 */

public class FaviterInfo extends DataSupport {
    public String packageName;


    public FaviterInfo() {
    }

    public FaviterInfo(String packageName) {
        this.packageName = packageName;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
