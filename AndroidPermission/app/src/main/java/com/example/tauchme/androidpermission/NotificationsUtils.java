package com.example.tauchme.androidpermission;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by TauchMe on 2018/1/15.
 */

public class NotificationsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {
        //获取系统服务
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        //ApplicationInfo为AndroidManifest.xml里的<application>标签里的各个属性值,即当前应用所取得的所有应用权限
        ApplicationInfo appInfo = context.getApplicationInfo();
        //获取包名
        String pkg = context.getApplicationContext().getPackageName();

        int uid = appInfo.uid;

        Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

        try {
            //通过映射字节码对象返回类对象
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            // getMethod第一个参数是方法名，第二个参数是该方法的参数类型，
            //获取APP的通知权限
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            //checkOpNoThrowMethod.invoke      获取当前应用通知权限返回的值
            // AppOpsManager.MODE_ALLOWED==0    权限开启时返回的值
            //AppOpsManager.MODE_IGNORED==1     权限未开启时返回的值
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
