package com.example.administrator.applock.utils;

/**
 * Created by Administrator on 2017/8/25.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.administrator.applock.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;


/**
 * 状态栏工具类
 * 状态栏两种模式（Android 4.4以上）
 * 1.沉浸式全屏
 * 2.着色
 */
public class SystemBarHelper {
    //状态栏透明度，Android 5.0 以上0.3，Android 5.0 以下 0.2
    private static float DEFAULT_ALPHA= Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 0.2f:0.3f;

    /**
     * Android 4.4 以上状态栏着色
     * @param activity
     * @param statusBarColor
     */
    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor){
        tintStatusBar(activity,statusBarColor,DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的状态栏着色
     * @param activity Activity对象
     * @param statusBarColor 状态栏颜色
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBar(Activity activity, @ColorInt int statusBarColor, @FloatRange(from = 0.0,to = 1.0) float alpha){
        tintStatusBar(activity.getWindow(),statusBarColor,alpha);
    }

    /**
     *  Android4.4以上的状态栏着色
     * @param window 一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param statusColor 状态栏颜色
     */
    public static void tintStatusBar(Window window,@ColorInt int statusColor){
        tintStatusBar(window,statusColor,DEFAULT_ALPHA);
    }

    /**
     * Android4.4以上的状态栏着色
     * @param window  一般都是用于Activity的window,也可以是其他的例如Dialog,DialogFragment
     * @param statusBarColor 状态栏颜色
     * @param alpha 透明栏透明度[0.0-1.0]
     */
    public static void tintStatusBar(Window window,@ColorInt int statusBarColor,@FloatRange(from = 0.0,to = 1.0) float alpha){
        //Android 系统 4.4 以上
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return;
        }
        //Android 5.0 以上透明状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewGroup decorView = (ViewGroup)window.getDecorView();
        ViewGroup contentView = (ViewGroup)decorView.findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        if(rootView != null){
            rootView.setFitsSystemWindows(true);
        }
        setStatusBar(decorView,statusBarColor,true);
        setTranslucentView(decorView,alpha);
    }

    /**
     * 创建假的状态栏 View
     * @param container
     * @param statusBarColor
     * @param visible
     */
    public static void setStatusBar(ViewGroup container,@ColorInt int statusBarColor,boolean visible){
        setStatusBar(container,statusBarColor,visible,false);
    }

    /**
     * 创建假的状态栏 View
     * @param container
     * @param statusBarColor
     * @param visible
     * @param addToFirst
     */
    public static void setStatusBar(ViewGroup container,@ColorInt int statusBarColor,boolean visible,boolean addToFirst){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            View statusBarView = container.findViewById(R.id.statusbar_view);
            if(statusBarView == null){
                statusBarView = new View(container.getContext());
                statusBarView.setId(R.id.statusbar_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(container.getContext()));
                if(addToFirst){
                    container.addView(statusBarView,0,lp);
                }else {
                    container.addView(statusBarView,lp);
                }
            }
            statusBarView.setBackgroundColor(statusBarColor);
            statusBarView.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }

    /**
     * 创建假的透明栏
     * @param container
     * @param alpha
     */
    private static void setTranslucentView(ViewGroup container,@FloatRange(from = 0.0,to = 1.0)float alpha){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            View translucentView = container.findViewById(R.id.translucent_view);
            if(translucentView == null){
                translucentView = new View(container.getContext());
                translucentView.setId(R.id.translucent_view);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(container.getContext()));
                container.addView(translucentView,lp);
            }
            translucentView.setBackgroundColor(Color.argb((int) (alpha * 255),0,0,0));
        }
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        int result = 0;
        //获取资源 Id
        int resId = context.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resId > 0){
            /**
             * getDimension()：返回px 的 float 值，如果单位是 dp 或 sp，乘以 density 后返回，是px 则不乘
             * getDimensionPixelSize()：返回 px 的 int 值，不能为0，如果单位是 dp 或 sp，乘以 density 后返回，是px 则不乘
             *getDimensionPixelOffset()：返回px 的 int 值， dp、sp, 要乘以 density
             */
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    /**
     *  Android4.4以上的沉浸式全屏模式
     *  注:
     * 1.删除fitsSystemWindows属性:Android5.0以上使用该方法如果出现界面展示不正确,删除布局中所有fitsSystemWindows属性
     * 或者调用forceFitsSystemWindows方法
     * 2.不删除fitsSystemWindows属性:也可以区别处理,Android5.0以上使用自己的方式实现,不调用该方法
     * @param activity
     */
    public static void immersiveStatusBar(Activity activity){
        immersiveStatusBar(activity,DEFAULT_ALPHA);
    }

    public static void immersiveStatusBar(Activity activity,@FloatRange(from = 0.0,to = 1.0) float alpha){
        immersiveStatusBar(activity.getWindow(),alpha);
    }

    public static void immersiveStatusBar(Window window){
        immersiveStatusBar(window,DEFAULT_ALPHA);
    }

    public static void immersiveStatusBar(Window window,@FloatRange(from = 0.0,to = 1.0)float alpha){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            systemUiVisibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup contentView = decorView.findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        if(rootView != null){
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            rootView.setFitsSystemWindows(true);
            lp.topMargin = - getStatusBarHeight(window.getContext());
            rootView.setLayoutParams(lp);
        }
        setTranslucentView(decorView,alpha);
    }

    /**
     * 加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     * @param context
     * @param view
     */
    public static void setHeightAndPadding(Context context,View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height += getStatusBarHeight(context);
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop() + getStatusBarHeight(context),view.getPaddingRight(),view.getPaddingBottom());
        }
    }

    /**
     * 增加 View 的 paddingTop ，增加的值为状态栏高度
     * @param context
     * @param view
     */
    public static void setPadding(Context context,View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(context),
                    view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    /**
     * 强制 RootView 下面的子 View 的 FitsSystemWindows为false
     * @param activity
     */
    public static void forceFitsSystemWindows(Activity activity){
        forceFitsSystemWindows(activity.getWindow());
    }

    public static void forceFitsSystemWindows(Window window){
        forceFitsSystemWindows((ViewGroup) window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT));
    }

    public static void forceFitsSystemWindows(ViewGroup viewGroup){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int count = viewGroup.getChildCount();
            for(int i = 0;i < count;i++){
                View view  = viewGroup.getChildAt(i);
                if(view instanceof ViewGroup){
                    forceFitsSystemWindows((ViewGroup) view);
                }else {
                    if(view.getFitsSystemWindows()){
                        view.setFitsSystemWindows(false);
                    }
                }
            }
        }
    }

    /**
     * 判断是否Flyme4以上
     */
    public static boolean isFlyme4Later(){
        return Build.FINGERPRINT.contains("Flyme_OS_4")
                ||Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]",Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find();
    }

    public static boolean isMIUI6Later(){
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method method = clz.getMethod("get",String.class);
            String value = (String)method.invoke(null,"ro.miui.ui.version.name");
            value = value.replace("[vV]","");
            int version = Integer.parseInt(value);
            return version >= 6;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置状态栏darkMode,字体颜色及icon变黑(目前支持MIUI6以上,Flyme4以上,Android M以上)
     * @param activity
     */
    public static void setStatusBarDarkMode(Activity activity){
        setStatusBarDarkMode(activity.getWindow());
    }

    public static void setStatusBarDarkMode(Window window){
        if(isFlyme4Later()){
            setStatusBarDarkModeForFlyme4(window,true);
        }else if(isMIUI6Later()){
            setStatusBarDarkModeForMIUI6(window,true);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            setStatusBarDarkModeForM(window);
        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    public static void setStatusBarDarkModeForM(Window window){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    public static void setStatusBarDarkModeForFlyme4(Window window,boolean dark){
        boolean result = false;
        if(window != null){
            WindowManager.LayoutParams lp = window.getAttributes();
            try {
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlag = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlag.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlag.getInt(lp);
                if(dark){
                    value |= bit;
                }else {
                    value &= ~bit;
                }
                meizuFlag.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStatusBarDarkModeForMIUI6(Window window,boolean darkmode){
        Class<? extends Window> clazz = window.getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


