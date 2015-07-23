package com.dudutech.biu.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by shaw on 2015/7/2.
 */
public class FileUtils {

    public static String getSDCardPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return null;
    }

}
