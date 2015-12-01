package fm.jiecao.jcvideoplayer_lib;

import android.app.Application;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * 统一管理MediaPlayer,管理视频的暂停播放进度全屏的功能
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaPlayer implements MediaPlayer.OnPreparedListener {

    public MediaPlayer mediaPlayer;
    private static JCMediaPlayer jcMediaPlayer;
    private static Application context;

    public static JCMediaPlayer init(Application cont) {
        if (jcMediaPlayer == null || context == null) {
            jcMediaPlayer = new JCMediaPlayer();
            context = cont;
        }
        return jcMediaPlayer;
    }

    public static JCMediaPlayer intance() {
        return jcMediaPlayer;
    }

    public JCMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareToPlay(String url) {
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_PREPARED));
    }

}
