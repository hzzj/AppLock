package com.example.administrator.applock;

import com.example.administrator.applock.base.BaseActivity;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class LockApplication extends LitePalApplication {
    private static LockApplication application; //Application 静态实例
    private static List<BaseActivity> activityList;//管理 Activity

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        activityList = new ArrayList<>();
    }

    /**
     * 单例模式获取 Application 静态实例
     * @return
     */
    public static LockApplication getInstance(){
        return application;
    }

    /**
     * 添加 Activity
     * @param activity
     */
    public void doForCreate(BaseActivity activity){
        activityList.add(activity);
    }

    /**
     * 移除 Activity
     * @param activity
     */
    public void doForFinish(BaseActivity activity){
        activityList.remove(activity);
    }

    /**
     * 清除所有 Activity
     * activity 不为 null 且不为解锁 Activity 则 finish
     */
    public void clearAllActivity(){
        for(BaseActivity activity:activityList){
            if(activity != null && !clearAllWhiteList(activity)){
                activity.finish();
            }
        }
        activityList.clear();
    }

    /**
     * 判断是不是解锁 Activity
     * @param activity
     * @return
     */
    private boolean clearAllWhiteList(BaseActivity activity){
        return false;
    }
}