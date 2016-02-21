package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import de.greenrobot.event.EventBus;


/**
 * 统一管理MediaPlayer,管理视频的暂停播放进度全屏的功能
 * Created by Nathen
 * On 2015/11/30 15:39
 */
class JCMediaManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;
    private static JCMediaManager jcMediaManager;
    public String uuid = "";//这个是正在播放中的视频控件的uuid，
    private String prev_uuid = "";
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public static JCMediaManager intance() {
        if (jcMediaManager == null) {
            jcMediaManager = new JCMediaManager();
        }
        return jcMediaManager;
    }

    public JCMediaManager() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareToPlay(Context context, String url) {
        if (TextUtils.isEmpty(url)) return;
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
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_UPDATE_BUFFER);
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
        Log.i("uuid::", "0 " + uuid + " prevuuid " + prev_uuid);
//        backUpUuid();
        this.uuid = uuid;
        Log.i("uuid::", "1 " + uuid + " prevuuid " + prev_uuid);
    }

    public void backUpUuid() {
        this.prev_uuid = this.uuid;
    }

    public void revertUuid() {
        this.uuid = this.prev_uuid;
        this.prev_uuid = "";
        Log.i("uuid::", "2 " + uuid + " prevuuid " + prev_uuid);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_RESIZE));
    }

    public void clearWidthAndHeight() {
        currentVideoWidth = 0;
        currentVideoHeight = 0;
    }
}
