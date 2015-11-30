package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
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

    public static JCMediaPlayer intance() {
        if (jcMediaPlayer == null) {
            jcMediaPlayer = new JCMediaPlayer();
        }
        return jcMediaPlayer;
    }

    public JCMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void setUrl(String url, Context context) {
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_PREPARED));
    }

}
