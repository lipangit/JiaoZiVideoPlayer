package cn.jzvd.demo.mute;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerStandard;
import cn.jzvd.demo.mute.mediaInterface.ijk.JZMediaIjkplayerMute;
import cn.jzvd.demo.mute.mediaInterface.system.JZMediaSystemMute;

/**
 * Created by Admin on 2018/3/20.
 * 静音播放
 */
public class MuteVideoPlayer extends JZVideoPlayerStandard {

    public MuteVideoPlayer(Context context) {
        super(context);
    }

    public MuteVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 进入全屏模式关闭静音模式
     */
    @Override
    public void startWindowFullscreen() {
        JZMediaInterface jzMediaInterface = JZMediaManager.instance().jzMediaInterface;

        if (jzMediaInterface != null) {
            if (jzMediaInterface instanceof JZMediaSystemMute) {
                // 系统引擎
                ((JZMediaSystemMute) jzMediaInterface).setMute(false);
            } else if (jzMediaInterface instanceof JZMediaIjkplayerMute) {
                // ijk 引擎
                ((JZMediaIjkplayerMute) jzMediaInterface).setMute(false);
            }
        }
        super.startWindowFullscreen();
    }

    /**
     * 退出全屏模式开启静音模式
     * 根据需要来设置
     */
    @Override
    public void playOnThisJzvd() {
        JZMediaInterface jzMediaInterface = JZMediaManager.instance().jzMediaInterface;
        if (jzMediaInterface != null) {
            if (jzMediaInterface instanceof JZMediaSystemMute) {// your JZMediaSystem child
                // 系统引擎
                ((JZMediaSystemMute) jzMediaInterface).setMute(true);
            } else if (jzMediaInterface instanceof JZMediaIjkplayerMute) {
                // ijk 引擎
                ((JZMediaIjkplayerMute) jzMediaInterface).setMute(true);
            }
        }
        super.playOnThisJzvd();
    }
}
