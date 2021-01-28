package com.wiem.pdfreadwrite;

import android.content.Context;

import java.io.File;

public class Common {
    public static String getAppPath(Context context){
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
        + File.separator
                + context.getResources().getString(R.string.app_name)
                + File.separator
        );
        if(!dir.exists())
            dir.mkdirs();

            return dir.getPath() + File.separator  ;
    }
}
