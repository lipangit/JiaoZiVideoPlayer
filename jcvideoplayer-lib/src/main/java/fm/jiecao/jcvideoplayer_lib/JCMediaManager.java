package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Map;


/**
 * <p>统一管理MediaPlayer的地方,只有一个mediaPlayer实例，那么不会有多个视频同时播放，也节省资源。</p>
 * <p>Unified management MediaPlayer place, there is only one MediaPlayer instance, then there will be no more video broadcast at the same time, also save resources.</p>
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public class JCMediaManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {
  public static String TAG = "JCMediaManager";

  public MediaPlayer mediaPlayer;
  private static JCMediaManager JCMediaManager;
  public int currentVideoWidth = 0;
  public int currentVideoHeight = 0;
  public JCMediaPlayerListener listener;
  public JCMediaPlayerListener lastListener;
  public int lastState;
  Object mediaAsyncObj = new Object();

  public static JCMediaManager instance() {
    if (JCMediaManager == null) {
      JCMediaManager = new JCMediaManager();
    }
    return JCMediaManager;
  }

  public JCMediaManager() {
    mediaPlayer = new MediaPlayer();
  }

  public void prepareToPlay(final Context context, final String url, final Map<String, String> mapHeadData) {
    if (TextUtils.isEmpty(url)) return;
    PrepareTHread p = new PrepareTHread(context, url, mapHeadData);
    p.start();
  }

  class PrepareTHread extends Thread {
    Context context;
    String url;
    Map<String, String> mapHeadData;

    PrepareTHread(final Context context, final String url, final Map<String, String> mapHeadData) {
      this.context = context;
      this.url = url;
      this.mapHeadData = mapHeadData;
    }

    @Override
    public void run() {
      try {
        synchronized (mediaAsyncObj) {
          currentVideoWidth = 0;
          currentVideoHeight = 0;
          mediaPlayer.release();
          mediaPlayer = new MediaPlayer();
          mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
          mediaPlayer.setDataSource(context, Uri.parse(url), mapHeadData);
          mediaPlayer.setOnPreparedListener(JCMediaManager.this);
          mediaPlayer.setOnCompletionListener(JCMediaManager.this);
          mediaPlayer.setOnBufferingUpdateListener(JCMediaManager.this);
          mediaPlayer.setScreenOnWhilePlaying(true);
          mediaPlayer.setOnSeekCompleteListener(JCMediaManager.this);
          mediaPlayer.setOnErrorListener(JCMediaManager.this);
          mediaPlayer.setOnVideoSizeChangedListener(JCMediaManager.this);
          mediaPlayer.prepareAsync();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static boolean RELEASING_ = false;

  public void releaseMediaPlayer() {
//    if (RELEASING_) return;
    ReleaseTHread r = new ReleaseTHread();
    r.start();
  }

  class ReleaseTHread extends Thread {
    @Override
    public void run() {
      synchronized (mediaAsyncObj) {
        mediaPlayer.release();
      }
    }
  }

  public void setDisplay(SurfaceHolder holder) {
    SetDisplayTHread s = new SetDisplayTHread(holder);
    s.start();
  }

  class SetDisplayTHread extends Thread {
    SurfaceHolder holder;

    SetDisplayTHread(SurfaceHolder holder) {
      this.holder = holder;
    }

    @Override
    public void run() {
      synchronized (mediaAsyncObj) {
        JCMediaManager.instance().mediaPlayer.setDisplay(holder);
      }
    }
  }


  @Override
  public void onPrepared(MediaPlayer mp) {
    if (listener != null) {
      listener.onPrepared();
    }
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    if (listener != null) {
      listener.onAutoCompletion();
    }
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    if (listener != null) {
      listener.onBufferingUpdate(percent);
    }
  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {
    if (listener != null) {
      listener.onSeekComplete();
    }
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    if (listener != null) {
      listener.onError(what, extra);
    }
    return true;
  }

  @Override
  public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    currentVideoWidth = mp.getVideoWidth();
    currentVideoHeight = mp.getVideoHeight();
    if (listener != null) {
      listener.onVideoSizeChanged();
    }
  }

  interface JCMediaPlayerListener {
    void onPrepared();

    void onAutoCompletion();

    void onCompletion();

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onError(int what, int extra);

    void onVideoSizeChanged();

    void onBackFullscreen();
  }
}
