package fm.jiecao.jiecaovideoplayer;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Nathen
 * On 2015/12/01 11:29
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        //it is public static, you can set this everywhere
        //JCVideoPlayer.TOOL_BAR_EXIST = false;
        //JCVideoPlayer.ACTION_BAR_EXIST = false;
    }
}
