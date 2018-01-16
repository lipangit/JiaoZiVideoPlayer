package cn.jzvd.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by pc on 2018/1/17.
 */

public class JZVideoPlayerStandardVolumeAfterFullscreen extends JZVideoPlayerStandard {
    public JZVideoPlayerStandardVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public JZVideoPlayerStandardVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            JZMediaManager.instance().jzMediaInterface.setVolume(1f, 1f);
        } else {
            JZMediaManager.instance().jzMediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        JZMediaManager.instance().jzMediaInterface.setVolume(1f, 1f);
    }

    /**
     * 退出全屏模式的时候开启静音模式
     */
    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        JZMediaManager.instance().jzMediaInterface.setVolume(0f, 0f);
    }
}
