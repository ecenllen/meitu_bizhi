package com.duoduoapp.meitu;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;


import com.duoduoapp.brothers.UIApplication;

import com.duoduoapp.meitu.itf.IData;
import com.duoduoapp.meitu.utils.AppConfig;
import com.duoduoapp.meitu.utils.ConfigConstants;
import com.duoduoapp.meitu.utils.CrashHandler;
import com.duoduoapp.meitu.utils.DownLoaderAPK;
import com.duoduoapp.meitu.utils.PlayerFileUtil;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;





/**
 * 代表的当前的应用程序，维护的是应用程序全部的状态信息。 一个应用程序只会实例化一个application类 单一实例 单态模式 singletons
 *
 * <p>
 * <b>注意:一定接的在清单文件配置</b>
 * </p>
 *
 * @author Administrator
 *
 */
public class MobileGuardAppliation extends UIApplication  {
    private static Context sContext;
    public static Context getAppContext() {
        if (sContext == null) {
            return null;
        }
        return sContext;
    }
    /**
     * 应用程序进程在第一次被创建的时候调用的方法，在任何其他对象创建之前执行的逻辑
     */

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
//        createFolder();
//        initAppConfig();
        initFresco();
        initDownloaderAPK();
//        initCrashHandler();
        initLeak();
    }

    private void initLeak() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        try {
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) { // 60
                ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
    }

    private void initCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    private void initDownloaderAPK() {
        DownLoaderAPK.getInstance();
    }

    private void initFresco() {

        Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(sContext));
    }

    private void initAppConfig() {
        ApplicationInfo appInfo;
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            AppConfig.versioncode=GetVersionCode(this);
            AppConfig.APPKEY = appInfo.metaData.getString("UMENG_APPKEY");
            AppConfig.Channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        AppConfig.youkulibPath = getCacheDir() + File.separator + "videoparse.jar";// 初始化引擎存放位置
        AppConfig.GZHPath = IData.DEFAULT_GZH_CACHE ;// 公众号的目录不能用缓存目录
        AppConfig.InitLocal(this);
    }

    private void createFolder() {
        PlayerFileUtil.creatFolder(IData.DEFAULT_DISK_CACHE);
        PlayerFileUtil.creatFolder(IData.DEFAULT_DOWNLOADE_FOLDER);
        PlayerFileUtil.creatFolder(IData.DEFAULT_FILES_CACHE);
        PlayerFileUtil.creatFolder(IData.DEFAULT_GZH_CACHE);
    }

    public  String GetVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(info.versionCode); //获取版本cood
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


}
