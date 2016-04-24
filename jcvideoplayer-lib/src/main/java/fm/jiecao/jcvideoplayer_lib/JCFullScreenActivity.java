package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;

/**
 * <p>全屏的activity</p>
 * <p>fullscreen activity</p>
 * Created by Nathen
 * On 2015/12/01 11:17
 */
public class JCFullScreenActivity extends Activity {

    static void toActivityFromNormal(Context context, int state, String url, Class videoPlayClass, Object... obj) {
        CURRENT_STATE = state;
        DIRECT_FULLSCREEN = false;
        URL = url;
        VIDEO_PLAYER_CLASS = videoPlayClass;
        OBJECTS = obj;
        Intent intent = new Intent(context, JCFullScreenActivity.class);
        context.startActivity(intent);
    }

    /**
     * <p>直接进入全屏播放</p>
     * <p>Full screen play video derictly</p>
     *
     * @param context        context
     * @param url            video url
     * @param videoPlayClass your videoplayer extends JCAbstraceVideoPlayer
     * @param obj            custom param
     */
    public static void toActivity(Context context, String url, Class videoPlayClass, Object... obj) {
        CURRENT_STATE = JCVideoPlayer.CURRENT_STATE_NORMAL;
        URL = url;
        DIRECT_FULLSCREEN = true;
        VIDEO_PLAYER_CLASS = videoPlayClass;
        OBJECTS = obj;
        Intent intent = new Intent(context, JCFullScreenActivity.class);
        context.startActivity(intent);
    }

    JCVideoPlayer jcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    static int CURRENT_STATE = -1;
    public static String URL;
    static boolean DIRECT_FULLSCREEN = false;//this is should be in videoplayer
    static Class VIDEO_PLAYER_CLASS;
    static Object[] OBJECTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.jc_activity_fullscreen);//or here setJcVideoPlayer derictly

        try {
            Constructor<JCVideoPlayerStandard> constructor = VIDEO_PLAYER_CLASS.getConstructor(Context.class);
            jcVideoPlayer = constructor.newInstance(this);
            setContentView(jcVideoPlayer);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        jcVideoPlayer.IF_CURRENT_IS_FULLSCREEN = true;
        jcVideoPlayer.IF_FULLSCREEN_IS_DIRECTLY = DIRECT_FULLSCREEN;
        jcVideoPlayer.setUp(URL, OBJECTS);
        jcVideoPlayer.addSurfaceView();
        jcVideoPlayer.setStateAndUi(CURRENT_STATE);


        if (jcVideoPlayer.IF_FULLSCREEN_IS_DIRECTLY) {
            jcVideoPlayer.ivStart.performClick();
        } else {
            JCVideoPlayer.IF_RELEASE_WHEN_ON_PAUSE = true;
            JCMediaManager.intance().listener = jcVideoPlayer;
        }
    }

    @Override
    public void onBackPressed() {
        jcVideoPlayer.backFullscreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
//        finish();
    }
}
