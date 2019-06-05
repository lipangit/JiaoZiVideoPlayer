package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JzvdStd;

/**
 * Created by pc on 2018/1/17.
 */

public class JzvdStdVolumeAfterFullscreen extends JzvdStd {
    public JzvdStdVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (screen == SCREEN_FULLSCREEN) {
            mediaInterface.setVolume(1f, 1f);
        } else {
            mediaInterface.setVolume(0f, 0f);
        }
    }

    /**
     * 进入全屏模式的时候关闭静音模式
     */
    @Override
    public void gotoScreenFullscreen() {
        super.gotoScreenFullscreen();
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (mediaInterface != null)
            mediaInterface.setVolume(1f, 1f);
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        if (mediaInterface != null)
            mediaInterface.setVolume(0f, 0f);
    }
}
