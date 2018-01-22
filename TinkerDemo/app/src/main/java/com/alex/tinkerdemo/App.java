package com.alex.tinkerdemo;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.alex.tinkerdemo.tinker.Log.MyLogImp;
import com.alex.tinkerdemo.tinker.util.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;


/**
 *  Application 实现类
 * Created by alex_mahao on 2016/8/3.
 */

@SuppressWarnings("unused")
@DefaultLifeCycle(  application = "com.alex.tinkerdemo.TinkerApp",// 自定义生成
                    flags = ShareConstants.TINKER_ENABLE_ALL,
                    loadVerifyFlag = false)
public class App extends DefaultApplicationLike {


    public static App sApp;


    public App(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
               long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent,
               Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
    }




    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;

        /*JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplication());

        // 友盟统计日志加密
        AnalyticsConfig.enableEncrypt(true);*/


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);

        // 其原理是分包架构，所以在加载初要加载其余的分包
        MultiDex.install(base);

        // Tinker管理类，保存当前对象
        TinkerManager.setTinkerApplicationLike(this);
        // 崩溃保护
        TinkerManager.initFastCrashProtect();
        // 是否重试
        TinkerManager.setUpgradeRetryEnable(true);

        //Log 实现，打印加载补丁的信息
        TinkerInstaller.setLogIml(new MyLogImp());

        // 运行Tinker ，通过Tinker添加一些基本配置
        TinkerManager.installTinker(this);


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        // 生命周期，默认配置
        getApplication().registerActivityLifecycleCallbacks(callback);
    }



    public static App getApp(){
        return sApp;
    }


}
