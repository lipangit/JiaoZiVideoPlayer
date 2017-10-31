package cn.jzvd.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Nathen
 * On 2015/12/01 11:29
 */
public class ApplicationDemo extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        //it is public static, you can set this everywhere
        //JZVideoPlayer.TOOL_BAR_EXIST = false;
        //JZVideoPlayer.ACTION_BAR_EXIST = false;
    }
}
