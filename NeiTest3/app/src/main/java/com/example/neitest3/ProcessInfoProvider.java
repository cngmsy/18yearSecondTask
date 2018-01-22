package com.example.neitest3;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵辉 on 2018/1/19.
 */

public class ProcessInfoProvider {
    public static ArrayList<ProcessInfo> getProcessInfo(Context ctx){
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = ctx.getPackageManager();
        List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        ArrayList<ProcessInfo> plist = new ArrayList<>();
        for(RunningAppProcessInfo runningProcess : runningAppProcesses){
            ProcessInfo processInfo = new ProcessInfo();
            String packageName = runningProcess.processName;
            processInfo.name = packageName;
            int pid = runningProcess.pid;
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{pid});
            long memory = memoryInfo[0].getTotalPrivateDirty() * 1024;
            processInfo.memory = memory;
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
                Drawable icon = appInfo.loadIcon(pm);
                String lable = appInfo.loadLabel(pm).toString();
                processInfo.icon = icon;
                processInfo.name = lable;

                int flags = appInfo.flags;
                if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    processInfo.isUseApp = false;
                } else {
                    processInfo.isUseApp = true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                processInfo.name = packageName;
                processInfo.icon = ctx.getResources().getDrawable(R.drawable.ic_launcher);
                processInfo.isUseApp = false;
                e.printStackTrace();
            }
            if (processInfo.isUseApp) {
                plist.add(processInfo);
            }
        }
        return plist;
    }
}
