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
 * Created by Nathen
 * On 2015/12/01 11:17
 */
public class FullScreenActivity extends Activity {
    /**
     * state是当前的播放状态，全屏之后要继续原来的状态
     */
    public static void toActivity(Context context, int state, String url, String thumb, String title) {
        STATE = state;
        URL = url;
        THUMB = thumb;
        TITLE = title;
        Intent intent = new Intent(context, FullScreenActivity.class);
        context.startActivity(intent);
    }

    JCVideoView jcVideoView;
    /**
     * 刚启动全屏时的播放状态
     */
    public static int STATE = -1;
    public static String URL;
    public static String TITLE;
    public static String THUMB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_fullscreen);
        jcVideoView = (JCVideoView) findViewById(R.id.jcvideoview);
        jcVideoView.setUp(URL, THUMB, TITLE, true);
        jcVideoView.setState(STATE);
//        jcVideoView.delaySetdisplay();
        //TODO 来到全屏之后继续之前的状态，准备，播放，暂停

    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN) {
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
