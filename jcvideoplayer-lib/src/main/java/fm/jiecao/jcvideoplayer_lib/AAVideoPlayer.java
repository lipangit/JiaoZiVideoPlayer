package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nathen on 16/7/30.
 */
public abstract class AAVideoPlayer extends FrameLayout implements JCMediaPlayerListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    public static final String TAG = "JieCaoVideoPlayer";

    public static final int FULLSCREEN_ID = 33797;
    public static final int TINY_ID       = 33798;

    public static boolean WIFI_TIP_DIALOG_SHOWED = false;


    public static final int SCREEN_LAYOUT_LIST       = 0;
    public static final int SCREEN_WINDOW_FULLSCREEN = 1;
    public static final int SCREEN_WINDOW_TINY       = 2;
    public static final int SCREEN_LAYOUT_DETAIL     = 3;

    public static final int CURRENT_STATE_NORMAL                  = 0;
    public static final int CURRENT_STATE_PREPAREING              = 1;
    public static final int CURRENT_STATE_PLAYING                 = 2;
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3;
    public static final int CURRENT_STATE_PAUSE                   = 5;
    public static final int CURRENT_STATE_AUTO_COMPLETE           = 6;
    public static final int CURRENT_STATE_ERROR                   = 7;

    public int mCurrentState  = -1;
    public int mCurrentScreen = -1;


    public String              mUrl            = null;
    public Object[]            mObjects        = null;
    public boolean             mLooping        = false;
    public Map<String, String> mMapHeadData    = new HashMap<>();
    public int                 seekToInAdvance = -1;

    public ImageView startButton;
    public SeekBar   progressBar;
    public ImageView fullscreenButton;
    public TextView  currentTimeTextView, totalTimeTextView;
    public ViewGroup textureViewContainer;
    public ViewGroup topContainer, bottomContainer;
    public Surface mSurface;

    protected static Timer             UPDATE_PROGRESS_TIMER;
    protected static ProgressTimerTask mProgressTimerTask;

    protected int          mScreenWidth;
    protected int          mScreenHeight;
    protected AudioManager mAudioManager;
    protected Handler      mHandler;

    public AAVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public AAVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        startButton = (ImageView) findViewById(R.id.start);
        fullscreenButton = (ImageView) findViewById(R.id.fullscreen);
        progressBar = (SeekBar) findViewById(R.id.progress);
        currentTimeTextView = (TextView) findViewById(R.id.current);
        totalTimeTextView = (TextView) findViewById(R.id.total);
        bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
        textureViewContainer = (RelativeLayout) findViewById(R.id.surface_container);
        topContainer = (ViewGroup) findViewById(R.id.layout_top);

        startButton.setOnClickListener(this);
        fullscreenButton.setOnClickListener(this);
        progressBar.setOnSeekBarChangeListener(this);
        bottomContainer.setOnClickListener(this);
        textureViewContainer.setOnClickListener(this);

        textureViewContainer.setOnTouchListener(this);
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mHandler = new Handler();
    }

    public boolean setUp(String url, int screen, Object... objects) {
        mCurrentState = CURRENT_STATE_NORMAL;
        mUrl = url;
        mObjects = objects;
        mCurrentScreen = screen;
        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
        return true;
    }

    public boolean setUp(String url, int screen, Map<String, String> mapHeadData, Object... objects) {
        setUp(url, screen, objects);
        mMapHeadData.clear();
        mMapHeadData.putAll(mapHeadData);
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
            if (TextUtils.isEmpty(mUrl)) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR) {
                if (!mUrl.startsWith("file") && !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startButtonLogic();
            } else if (mCurrentState == CURRENT_STATE_PLAYING) {
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                JCMediaManager.instance().mediaPlayer.pause();
                setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
            } else if (mCurrentState == CURRENT_STATE_PAUSE) {
                JCMediaManager.instance().mediaPlayer.start();
                setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
            } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
                startButtonLogic();
            }
        } else if (i == R.id.fullscreen) {
            Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
            if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (mCurrentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                toWindowFullscreen();
            }
        } else if (i == R.id.surface_container && mCurrentState == CURRENT_STATE_ERROR) {
            Log.i(TAG, "onClick surfaceContainer State=Error [" + this.hashCode() + "] ");
            prepareVideo();
        }
    }

    public void setUiWitStateAndScreen(int state) {
        mCurrentState = state;
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                if (isCurrentMediaListener()) {
                    cancelProgressTimer();
                    JCMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_PREPAREING:
                resetProgressAndTime();
                break;
            case CURRENT_STATE_PLAYING:
                startProgressTimer();
                break;
            case CURRENT_STATE_PAUSE:
                startProgressTimer();
                break;
            case CURRENT_STATE_ERROR:
                if (isCurrentMediaListener()) {
                    JCMediaManager.instance().releaseMediaPlayer();
                }
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                cancelProgressTimer();
                progressBar.setProgress(100);
                currentTimeTextView.setText(totalTimeTextView.getText());
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    protected void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (UPDATE_PROGRESS_TIMER != null) {
            UPDATE_PROGRESS_TIMER.cancel();
        }
        if (mProgressTimerTask != null) {
            mProgressTimerTask.cancel();
        }
    }

    protected class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
                int position = getCurrentPositionWhenPlaying();
                int duration = getDuration();
                Log.v(TAG, "onProgressUpdate " + position + "/" + duration + " [" + this.hashCode() + "] ");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setTextAndProgress(0);
                    }
                });
            }
        }
    }

    protected int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE) {
            try {
                position = (int) JCMediaManager.instance().mediaPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    protected int getDuration() {
        int duration = 0;
        try {
            duration = (int) JCMediaManager.instance().mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    protected void setTextAndProgress(int secProgress) {
        int position = getCurrentPositionWhenPlaying();
        int duration = getDuration();
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!mTouchingProgressBar) {
            if (progress != 0) progressBar.setProgress(progress);
        }
        if (secProgress > 95) secProgress = 100;
        if (secProgress != 0) progressBar.setSecondaryProgress(secProgress);
        currentTimeTextView.setText(JCUtils.stringForTime(currentTime));
        totalTimeTextView.setText(JCUtils.stringForTime(totalTime));
    }

    protected void resetProgressAndTime() {
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(0);
        currentTimeTextView.setText(JCUtils.stringForTime(0));
        totalTimeTextView.setText(JCUtils.stringForTime(0));
    }

    public abstract int getLayoutId();

    protected boolean isCurrentMediaListener() {
        return JCVideoPlayerManager.listener() != null
                && JCVideoPlayerManager.listener() == this;
    }
}
