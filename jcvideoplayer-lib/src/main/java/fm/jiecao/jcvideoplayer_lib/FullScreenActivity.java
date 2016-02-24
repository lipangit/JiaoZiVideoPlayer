package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import de.greenrobot.event.EventBus;

/**
 * 可能会有直接全屏显示的需求
 * Created by Nathen
 * On 2015/12/01 11:17
 */
public class FullScreenActivity extends Activity {

    public static void toActivity(Context context, int state, String url, String thumb, String title) {
        STATE = state;
        URL = url;
        THUMB = thumb;
        TITLE = title;
        Intent intent = new Intent(context, FullScreenActivity.class);
        context.startActivity(intent);
    }

    JCVideoPlayer jcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    public static int STATE = -1;
    public static String URL;
    public static String TITLE;
    public static String THUMB;
    public static boolean manualQuit = false;
    protected static Skin skin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_fullscreen);
        jcVideoPlayer = (JCVideoPlayer) findViewById(R.id.jcvideoplayer);
        if (skin != null) {
            jcVideoPlayer.setSkin(skin.titleColor, skin.timeColor, skin.seekDrawable, skin.bottomControlBackground,
                    skin.enlargRecId, skin.shrinkRecId);
        }
        jcVideoPlayer.setUpForFullscreen(URL, THUMB, TITLE);
        jcVideoPlayer.setState(STATE);
        JCMediaManager.intance().setUuid(jcVideoPlayer.uuid);
        manualQuit = false;
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN || videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        jcVideoPlayer.quitFullScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!manualQuit) {
            jcVideoPlayer.isClickFullscreen = false;
            JCVideoPlayer.releaseAllVideos();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
