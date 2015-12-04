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
public class JCMediaPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;
    private static JCMediaPlayer jcMediaPlayer;
    public String uuid;//这个是正在播放中的视频控件的uuid，
    private String prev_uuid;

    public static JCMediaPlayer intance() {
        if (jcMediaPlayer == null) {
            jcMediaPlayer = new JCMediaPlayer();
        }
        return jcMediaPlayer;
    }

    public JCMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareToPlay(Context context, String url) {
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_PREPARED));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_BUFFERUPDATE);
        videoEvents.obj = percent;
        EventBus.getDefault().post(videoEvents);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    public void setUuid(String uuid) {
        backUpUuid();
        this.uuid = uuid;
    }

    public void backUpUuid() {
        this.prev_uuid = this.uuid;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE));
    }
}
