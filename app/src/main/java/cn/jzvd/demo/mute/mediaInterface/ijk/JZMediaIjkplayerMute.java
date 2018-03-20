package cn.jzvd.demo.mute.mediaInterface.ijk;

import cn.jzvd.demo.CustomMediaPlayer.JZMediaIjkplayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Admin on 2018/3/20.
 * 视频控件 ijk引擎
 * <br/>
 * 基于JZMediaIjkplayer 增加静音播放
 */
public class JZMediaIjkplayerMute extends JZMediaIjkplayer {


    private static JZMediaIjkplayerMute feedJZMediaSystemInterface;

    private JZMediaIjkplayerMute() {

    }

    public static JZMediaIjkplayerMute getInstance() {

        if (feedJZMediaSystemInterface == null) {
            feedJZMediaSystemInterface = new JZMediaIjkplayerMute();
        }
        return feedJZMediaSystemInterface;
    }

    @Override
    public void start() {
        super.start();

        setMute(true);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        super.onPrepared(iMediaPlayer);

        setMute(true);
    }

    public void setMute(boolean isMute) {
        if (isMute) {
            if (ijkMediaPlayer != null)
                ijkMediaPlayer.setVolume(0.0f, 0.0f);
        } else {
            if (ijkMediaPlayer != null)
                ijkMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }
}
