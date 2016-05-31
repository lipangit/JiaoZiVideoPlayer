package fm.jiecao.jcvideoplayer_lib;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

  public static final int HANDLER_PREPARE = 0;
  public static final int HANDLER_SETDISPLAY = 1;
  public static final int HANDLER_RELEASE = 2;
  HandlerThread mMediaHandlerThread;
  MediaHandler mMediaHandler;
  Handler mainThreadHandler;

  public static JCMediaManager instance() {
    if (JCMediaManager == null) {
      JCMediaManager = new JCMediaManager();
    }
    return JCMediaManager;
  }

  public JCMediaManager() {
    mediaPlayer = new MediaPlayer();
    mMediaHandlerThread = new HandlerThread(TAG);
    mMediaHandlerThread.start();
    mMediaHandler = new MediaHandler((mMediaHandlerThread.getLooper()));
    mainThreadHandler = new Handler();
  }

  public class MediaHandler extends Handler {
    public MediaHandler(Looper looper) {
      super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case HANDLER_PREPARE:
          try {
            Log.w(TAG, "prepare");
            currentVideoWidth = 0;
            currentVideoHeight = 0;
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//              mediaPlayer.setDataSource(context, Uri.parse(url), mapHeadData);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            method.invoke(mediaPlayer, ((FuckBean) msg.obj).url, ((FuckBean) msg.obj).mapHeadData);
            mediaPlayer.setOnPreparedListener(JCMediaManager.this);
            mediaPlayer.setOnCompletionListener(JCMediaManager.this);
            mediaPlayer.setOnBufferingUpdateListener(JCMediaManager.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(JCMediaManager.this);
            mediaPlayer.setOnErrorListener(JCMediaManager.this);
            mediaPlayer.setOnVideoSizeChangedListener(JCMediaManager.this);
            mediaPlayer.prepareAsync();
          } catch (NoSuchMethodException e) {
            e.printStackTrace();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
          Log.w(TAG, "prepare done");
          break;
        case HANDLER_SETDISPLAY:
          Log.i(TAG, "setdisplay");
          SurfaceHolder holder = (SurfaceHolder) msg.obj;
          if (holder.getSurface() != null && holder.getSurface().isValid()) {
            JCMediaManager.instance().mediaPlayer.setDisplay(holder);
            Log.i(TAG, "setdisplay done");
          }
          break;
        case HANDLER_RELEASE:
          Log.e(TAG, "release");
          mediaPlayer.release();
          Log.e(TAG, "release done");
          break;
      }
    }
  }


  public void prepareToPlay(final String url, final Map<String, String> mapHeadData) {
    if (TextUtils.isEmpty(url)) return;
    Message msg = new Message();
    msg.what = HANDLER_PREPARE;
    FuckBean fb = new FuckBean(url, mapHeadData);
    msg.obj = fb;
    mMediaHandler.sendMessage(msg);
  }

  public void releaseMediaPlayer() {
    Message msg = new Message();
    msg.what = HANDLER_RELEASE;
    mMediaHandler.sendMessage(msg);
  }

  public void setDisplay(SurfaceHolder holder) {
    Message msg = new Message();
    msg.what = HANDLER_SETDISPLAY;
    msg.obj = holder;
    mMediaHandler.sendMessage(msg);
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    if (listener != null) {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onPrepared();
        }
      });
    }
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    if (listener != null) {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onAutoCompletion();
        }
      });
    }
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, final int percent) {
    if (listener != null) {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onBufferingUpdate(percent);
        }
      });
    }
  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {
    if (listener != null) {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onSeekComplete();
        }
      });
    }
  }

  @Override
  public boolean onError(MediaPlayer mp, final int what, final int extra) {
    if (listener != null) {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onError(what, extra);
        }
      });
    }
    return true;
  }

  @Override
  public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    currentVideoWidth = mp.getVideoWidth();
    currentVideoHeight = mp.getVideoHeight();
    if (listener != null) {
      mainThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          listener.onVideoSizeChanged();
        }
      });
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

  private class FuckBean {
    String url;
    Map<String, String> mapHeadData;

    FuckBean(String url, Map<String, String> mapHeadData) {
      this.url = url;
      this.mapHeadData = mapHeadData;
    }
  }
}
