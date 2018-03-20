package cn.jzvd.demo.mute.mediaInterface.system;

import android.media.MediaPlayer;

import cn.jzvd.JZMediaSystem;

/**
 * Created by Admin on 2018/3/20.
 * 消费圈视频播放器 mediaPlayer 引擎
 * <br/>
 * 基于 JZMediaSystem 增加静音播放
 */
public class JZMediaSystemMute extends JZMediaSystem {


    private static JZMediaSystemMute JZMediaSystemMute;

    private JZMediaSystemMute() {

    }

    public static JZMediaSystemMute getInstance() {

        if (JZMediaSystemMute == null) {
            JZMediaSystemMute = new JZMediaSystemMute();
        }
        return JZMediaSystemMute;
    }

    @Override
    public void start() {
        super.start();
        setMute(true);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        super.onPrepared(mediaPlayer);
        setMute(true);
    }

    public void setMute(boolean isMute) {

        if (isMute) {
            if (mediaPlayer != null)
                mediaPlayer.setVolume(0.0f, 0.0f);
        } else {
            if (mediaPlayer != null)
                mediaPlayer.setVolume(1.0f, 1.0f);
        }
    }
}
