package com.shaweibo.biu.global;

import android.app.Application;
import android.content.Context;

import com.shaweibo.biu.model.PicSize;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by shaw on 2015/7/2.
 */
public class MyApplication  extends Application{

    public static com.shaweibo.biu.global.LruMemoryCache<String, PicSize> picSizeCache  = new com.shaweibo.biu.global.LruMemoryCache<String, PicSize>(100) {

    };



    private static MyApplication mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        initImageLoaderConfig(this);
    }


    public static MyApplication getInstance() {
        return mContext;
    }

   private void initImageLoaderConfig(Context context){

    // Create global configuration and initialize ImageLoader with this configuration
    File cacheDir = StorageUtils.getCacheDirectory(context);
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .diskCache(new UnlimitedDiskCache(cacheDir))// cache
                    // sd
                    // path
            .memoryCacheExtraOptions(1000, 1000) // max width, max
                    // height，即保存的每个缓存文件的最大长宽
            .memoryCache(new LruMemoryCache(30 * 1024 * 1024)) // 缓存到内存的最大数据
                    // .memoryCache(new WeakMemoryCache())
            .denyCacheImageMultipleSizesInMemory()
            .diskCacheSize(500 * 1024 * 1024)
            .diskCacheFileCount(500)
            .threadPoolSize(5) // default
            .threadPriority(Thread.NORM_PRIORITY - 1)// default
            .denyCacheImageMultipleSizesInMemory()
            .diskCacheFileNameGenerator(new Md5FileNameGenerator())
            .tasksProcessingOrder(QueueProcessingType.LIFO)
                    // .writeDebugLogs() // Remove for release app
            .build();
           ImageLoader.getInstance().init(config);

}

}