package com.awaitu.allen.wallpagerpor.bean;


import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * Created by Administrator on 2015/10/20.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 整个程序 只需要 一个 ，从这里初始化
         * 需要 Context
         * 尤其 第三方
         */
        /**
         *
         *
         */
       
        initImageLoader(getApplicationContext());
        /**
         * 视频编辑初始化
         */
    	// sdk初始



    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
      
    }

   }