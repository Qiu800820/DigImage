package com.pocoin.basemvp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Sen on 2018/1/5.
 */

public class StatusBarUtil {

    private static final String TAG = "StatusBarUtil";
    private static final String META_KEY = "com.wsd.yjx.theme.LIGHT";

    public static int getStatusBarHeight(Context con){
        int i = con.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return con.getResources().getDimensionPixelSize(i);
    }

    private static boolean setFlymeStatusBarLightMode(Activity activity, boolean isDark){
        boolean result = false;
        try {
            WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(layoutParams);
            if (isDark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(layoutParams, value);
            activity.getWindow().setAttributes(layoutParams);
            result = true;
        } catch (Exception e) {
            Log.w(TAG, "=== setFlymeStatusBarLightMode error===");
        }

        return result;
    }

    private static boolean setMIUIStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        try {
            int darkModeFlag = 0;
            Class clazz = activity.getWindow().getClass();
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                darkModeFlag = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(layoutParams);
            }
            extraFlagField.invoke(activity.getWindow(), darkModeFlag, darkModeFlag);
            result = true;
        } catch (Exception e) {
            Log.w(TAG, "=== setMIUIStatusBarLightMode error===");
        }

        return result;
    }

    private static boolean setNativeStatusBarLightMode(Activity activity, boolean dark){
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int systemUiVisibilityFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (dark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibilityFlag | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibilityFlag & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            result = true;
        }
        return result;
    }

    public static void setStatusBarLightMode(Activity activity, boolean dark){

        boolean customResult = setFlymeStatusBarLightMode(activity, dark) || setMIUIStatusBarLightMode(activity, dark);
        boolean nativeResult = setNativeStatusBarLightMode(activity, dark);

        if(!customResult && !nativeResult && dark){
            // 强制修改状态栏颜色
            setStatusBarColor(activity, Color.BLACK);
        }
    }

    public static void setStatusBarColor(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
