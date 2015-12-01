package fm.jiecao.jiecaovideoplayer;

import android.app.Application;

/**
 * Created by Nathen
 * On 2015/12/01 11:29
 */
public class DemoApplication extends Application {
    private static DemoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static DemoApplication instance() {
        return application;
    }
}
