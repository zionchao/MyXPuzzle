package com.example.zhangchao_a.myxpuzzle.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by ZhangChao on 2016/9/4.
 */
public class ScreenUtil {

    /**
     *
     * @param context
     * @return 屏幕宽高
     */
    public static DisplayMetrics getScreenSize(Context context)
    {
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     *
     * @param context
     * @return  屏幕density
     */
    public static float getDeviceDensity(Context context)
    {
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }
}
