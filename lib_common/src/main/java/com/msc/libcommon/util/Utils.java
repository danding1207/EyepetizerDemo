package com.msc.libcommon.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * <p>Utils初始化相关 </p>
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 全局获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * View获取Activity的工具
     *
     * @param view view
     * @return Activity
     */
    public static
    @NonNull
    Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    /**
     * 全局获取String的方法
     *
     * @param id 资源Id
     * @return String
     */
    public static String getString(@StringRes int id) {
        return context.getResources().getString(id);
    }

    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug() {
        if (StringUtils.isSpace(context.getPackageName())) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }


    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    private static String GUID;


    public static String getUDID() {
        if (context == null)
            throw new NullPointerException("Context is null, please init frist!");
        if (GUID == null) {
            synchronized (Utils.class) {
                if (GUID == null) {

                    FileInputStream fis = null;
                    StringBuilder id = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    String GUIDFILE = "guid_file";
                    try {
                        fis = context.openFileInput(GUIDFILE);
                        int ch = 0;
                        while ((ch = fis.read(buffer)) != -1) {
                            id.append(new String(buffer));
                        }
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!TextUtils.isEmpty(id.toString())) {
                        // Use the ids previously computed and stored in the prefs file
                        GUID = id.toString();
                    } else {
                        GUID = UUID.randomUUID().toString();
                        FileOutputStream fos;
                        try {
                            fos = context.openFileOutput(GUIDFILE, Context.MODE_PRIVATE);
                            fos.write(GUID.getBytes());
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return GUID;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return String.valueOf(android.os.Build.VERSION.SDK_INT);
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }


}