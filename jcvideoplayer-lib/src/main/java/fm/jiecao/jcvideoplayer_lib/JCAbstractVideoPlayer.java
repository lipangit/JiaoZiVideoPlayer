package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manage MediaPlayer
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public abstract class JCAbstractVideoPlayer extends FrameLayout implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, JCMediaManager.JCMediaPlayerListener {

    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STATE_PREPAREING = 0;
    public static final int CURRENT_STATE_PAUSE = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_OVER = 3;
    public static final int CURRENT_STATE_NORMAL = 4;
    public static final int CURRENT_STATE_ERROR = 5;
    private boolean touchingProgressBar = false;

    ImageView ivStart;
    SeekBar skProgress;
    ImageView ivFullScreen;
    TextView tvTimeCurrent, tvTimeTotal;
    ViewGroup rlParent;

    ViewGroup llTopContainer, llBottomControl;

    ResizeSurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    int surfaceId;// for onClick()

    String url;

    private static Timer mUpdateProgressTimer;

    public JCAbstractVideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public JCAbstractVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        ivStart = (ImageView) findViewById(R.id.start);
        ivFullScreen = (ImageView) findViewById(R.id.fullscreen);
        skProgress = (SeekBar) findViewById(R.id.progress);
        tvTimeCurrent = (TextView) findViewById(R.id.current);
        tvTimeTotal = (TextView) findViewById(R.id.total);
        llBottomControl = (LinearLayout) findViewById(R.id.bottom_control);
        rlParent = (RelativeLayout) findViewById(R.id.parentview);
        llTopContainer = (LinearLayout) findViewById(R.id.title_container);

        ivStart.setOnClickListener(this);
        ivFullScreen.setOnClickListener(this);
        skProgress.setOnSeekBarChangeListener(this);
        llBottomControl.setOnClickListener(this);
        rlParent.setOnClickListener(this);
        skProgress.setOnTouchListener(this);
    }

    public abstract int getLayoutId();

    public void setUp(String url) {
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        this.url = url;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), "No url", Toast.LENGTH_SHORT).show();
                return;
            }
            if (CURRENT_STATE == CURRENT_STATE_NORMAL || CURRENT_STATE == CURRENT_STATE_ERROR) {
                onStart();
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                onPause();
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                onResume();
            }
        }
    }

    protected void onStart() {
        CURRENT_STATE = CURRENT_STATE_PREPAREING;
        if (JCMediaManager.intance().listener != null) {
            JCMediaManager.intance().listener.onCompletion();
        }
        JCMediaManager.intance().listener = this;
        addSurfaceView();
        JCMediaManager.intance().prepareToPlay(getContext(), url);
    }

    protected void onPlay() {
        if (CURRENT_STATE != CURRENT_STATE_PREPAREING) return;
        CURRENT_STATE = CURRENT_STATE_PLAYING;
        JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
        JCMediaManager.intance().mediaPlayer.start();
        startProgressTimer();
    }

    protected void onPause() {
        CURRENT_STATE = CURRENT_STATE_PAUSE;
        JCMediaManager.intance().mediaPlayer.pause();
    }

    protected void onResume() {
        CURRENT_STATE = CURRENT_STATE_PLAYING;
        JCMediaManager.intance().mediaPlayer.start();
        Log.i("JCVideoPlayer", "go on video");
    }

    private void addSurfaceView() {
        if (rlParent.getChildAt(0) instanceof ResizeSurfaceView) {
            rlParent.removeViewAt(0);
        }
        surfaceView = new ResizeSurfaceView(getContext());
        surfaceId = surfaceView.getId();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceView.setOnClickListener(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlParent.addView(surfaceView, 0, layoutParams);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchingProgressBar = true;
                cancelProgressTimer();
                break;
            case MotionEvent.ACTION_UP:
                touchingProgressBar = false;
                startProgressTimer();
                break;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress * JCMediaManager.intance().mediaPlayer.getDuration() / 100;
            JCMediaManager.intance().mediaPlayer.seekTo(time);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared() {
        onPlay();

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (CURRENT_STATE != CURRENT_STATE_NORMAL && CURRENT_STATE != CURRENT_STATE_PREPAREING) {
            setTextAndProgress(percent);
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {

    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = JCMediaManager.intance().currentVideoWidth;
        int mVideoHeight = JCMediaManager.intance().currentVideoHeight;
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            surfaceView.requestLayout();
        }
    }

    @Override
    public void onBackFullscreen() {

    }

    protected void startProgressTimer() {
        cancelProgressTimer();
        mUpdateProgressTimer = new Timer();
        mUpdateProgressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                                setTextAndProgress(0);
                            }
                        }
                    });
                }
            }
        }, 0, 300);
    }

    protected void cancelProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
        }
    }

    protected void setTextAndProgress(int secProgress) {
        int position = JCMediaManager.intance().mediaPlayer.getCurrentPosition();
        int duration = JCMediaManager.intance().mediaPlayer.getDuration();
        // if duration == 0 (e.g. in HLS streams) avoids ArithmeticException
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, secProgress, position, duration);
    }

    private void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        if (!touchingProgressBar) {
            if (progress != 0) skProgress.setProgress(progress);
        }
        if (secProgress != 0) skProgress.setSecondaryProgress(secProgress);
        tvTimeCurrent.setText(Utils.stringForTime(currentTime));
        tvTimeTotal.setText(Utils.stringForTime(totalTime));
    }
}
