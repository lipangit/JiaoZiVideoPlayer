package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by yujunkui
 * On 2016/08/30
 * 正在开发中 暂时不要使用
 */
public class JCVideoPlayerListAutoWindowTiny extends JCVideoPlayerStandard {


    public String position="-1";//记录播放的position

    public JCVideoPlayerListAutoWindowTiny(Context context) {
        super(context);
    }

    public JCVideoPlayerListAutoWindowTiny(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public   boolean setUp(String url, int screen, Object... objects) {
        Log.i(TAG, "setUp [" + this.hashCode() + "]isCurrentMediaListener()=" + isCurrentMediaListener() + " this.currentState == CURRENT_STATE_PLAYING=" + (this.currentState == CURRENT_STATE_PLAYING) + " screen!=SCREEN_WINDOW_FULLSCREEN" + (screen != SCREEN_WINDOW_FULLSCREEN));
        JCVideoPlayerListAutoWindowTiny jcVideoPlayer = null;
        boolean isPlayingTiny=false;//当前是否在播放
        if (JCVideoPlayerManager.listener() != null) {
            jcVideoPlayer = (JCVideoPlayerListAutoWindowTiny) JCVideoPlayerManager.listener();
            isPlayingTiny=jcVideoPlayer.currentScreen==SCREEN_WINDOW_TINY?true:false;
//            Log.i(TAG, "setUp [" + this.hashCode() + "] jcVideoPlayer.objects[1]="+jcVideoPlayer.objects[1]);
        }
        if (isCurrentMediaListener() //当前播放监听是否是自己
                && this.currentState == CURRENT_STATE_PLAYING //正在播放
                && screen != SCREEN_WINDOW_FULLSCREEN //是否在全屏
                && !isFullSwitchList//是否全屏切到listview
                &&!isPlayingTiny) {//在播放
            //启动小屏
            startWindowTiny();
        } else if (jcVideoPlayer != null
                && objects.length>1
//                && jcVideoPlayer.objects[1] != null && objects[1] != null
                && position.equals(objects[1])//position是否一致
                && jcVideoPlayer.currentState == CURRENT_STATE_PLAYING//是否在播放
                && jcVideoPlayer.currentScreen == SCREEN_WINDOW_TINY) {//是否小屏
            Log.i(TAG, "setUp Tiny To List [" + this.hashCode() + "] position="+position+" objects[1]="+objects[1]);
            JCVideoPlayerManager.listener().goToOtherListener();//返回到item播放
        } else {
            isFullSwitchList = false;
        }
        boolean superReturn = super.setUp(url, screen, objects);
        if (currentScreen == SCREEN_WINDOW_TINY) {
            tinyBackImageView.setVisibility(View.INVISIBLE);//设置在小屏下隐藏back
        }
        return superReturn;
    }

    public boolean isFullSwitchList = false;//是否从全屏切换到小屏

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.surface_container && currentScreen == SCREEN_WINDOW_TINY) {
            //回调小屏下小屏被点击事件
            Toast.makeText(getContext(),"小屏被点击", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.fullscreen) {
            if (currentScreen != SCREEN_WINDOW_FULLSCREEN) {
                isFullSwitchList = true;//全屏时候做个标识  以便在setup的时候不会切换成小屏
            }
        }else if (i == R.id.thumb||i==R.id.start) {
            position=objects[1].toString();
        }
    }

    public static boolean backPress() {//这个方法如果你没看过源码 基本可能看不懂
        Log.i(TAG, "backPress");
        if (JCVideoPlayerManager.listener() != null) {
            JCVideoPlayerListAutoWindowTiny jcVideoPlayer = (JCVideoPlayerListAutoWindowTiny) JCVideoPlayerManager.listener();
            if (jcVideoPlayer.currentScreen != SCREEN_WINDOW_TINY) {
                if (jcVideoPlayer.currentScreen == SCREEN_WINDOW_FULLSCREEN) {//如果当前是全屏则在切换完后对小屏的isFullSwitchList进行操作
                    boolean b = JCVideoPlayerManager.listener().goToOtherListener();
                    JCVideoPlayerListAutoWindowTiny currentJcVideoPlayer = (JCVideoPlayerListAutoWindowTiny) JCVideoPlayerManager.listener();
                    currentJcVideoPlayer.isFullSwitchList = true;//back标识
                    return b;
                }
                return JCVideoPlayerManager.listener().goToOtherListener();
            }
        }
        return false;
    }

}
