//package fm.jiecao.jcvideoplayer_lib;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.SurfaceTexture;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.MotionEvent;
//import android.view.Surface;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.lang.reflect.Constructor;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Manage MediaPlayer
// * Created by Nathen
// * On 2016/04/10 15:45
// */
//public abstract class BBVideoPlayer extends FrameLayout implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, JCMediaPlayerListener, TextureView.SurfaceTextureListener {
//
//    public static final String TAG = "JieCaoVideoPlayer";
//
//    protected int currentState  = -1;
//    protected int currentScreen = -1;
//
//    public static final int SCREEN_LAYOUT_LIST       = 0;
//    public static final int SCREEN_WINDOW_FULLSCREEN = 1;
//    public static final int SCREEN_WINDOW_TINY       = 2;
//    public static final int SCREEN_LAYOUT_DETAIL     = 3;
//
//    public static final int CURRENT_STATE_NORMAL                  = 0;
//    public static final int CURRENT_STATE_PREPAREING              = 1;
//    public static final int CURRENT_STATE_PLAYING                 = 2;
//    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3;
//    public static final int CURRENT_STATE_PAUSE                   = 5;
//    public static final int CURRENT_STATE_AUTO_COMPLETE           = 6;
//    public static final int CURRENT_STATE_ERROR                   = 7;
//
//    //-----
//    protected static int mBackUpBufferState = -1;
//
//    protected boolean mTouchingProgressBar    = false;
//    protected boolean mIfFullscreenIsDirectly = false;//mIfCurrentIsFullscreen should be true first
//
//    protected static    boolean IF_FULLSCREEN_FROM_NORMAL  = false;//to prevent infinite looping
//    public static       boolean IF_RELEASE_WHEN_ON_PAUSE   = true;
//    protected static    long    CLICK_QUIT_FULLSCREEN_TIME = 0;
//    public static final int     FULL_SCREEN_NORMAL_DELAY   = 2000;
//    //-----
//
//    public ImageView startButton;
//    public SeekBar   progressBar;
//    public ImageView fullscreenButton;
//    public TextView  currentTimeTextView, totalTimeTextView;
//    public ViewGroup textureViewContainer;
//    public ViewGroup topContainer, bottomContainer;
//    public Surface surface;
//    public static boolean             WIFI_TIP_DIALOG_SHOWED = false;
//    protected     String              url                   = null;
//    protected     Object[]            objects               = null;
//    protected     Map<String, String> mapHeadData           = new HashMap<>();
//    protected     boolean             looping               = false;
//    public        int                 seekToInAdvance        = -1;
//
//    public static final int FULLSCREEN_ID = 33797;
//    public static final int TINY_ID       = 33798;
//    protected        int               mScreenWidth;
//    protected        int               mScreenHeight;
//    protected        AudioManager      mAudioManager;
//    protected static Timer             UPDATE_PROGRESS_TIMER;
//    protected static ProgressTimerTask mProgressTimerTask;
//
//    //--
//
//    protected static JCBuriedPoint JC_BURIED_POINT;
//
//    //--
//
//
//    //--
//    protected int THRESHOLD = 80;
//    protected float mDownX;
//    protected float mDownY;
//    protected boolean mChangeVolume   = false;
//    protected boolean mChangePosition = false;
//    protected int mDownPosition;
//    protected int mGestureDownVolume;
//    protected int mSeekTimePosition;//change postion when finger up
////--
//
//
//    protected Handler mHandler = new Handler();
//
//
//    public BBVideoPlayer(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public BBVideoPlayer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    protected void init(Context context) {
//        View.inflate(context, getLayoutId(), this);
//        startButton = (ImageView) findViewById(R.id.start);
//        fullscreenButton = (ImageView) findViewById(R.id.fullscreen);
//        progressBar = (SeekBar) findViewById(R.id.progress);
//        currentTimeTextView = (TextView) findViewById(R.id.current);
//        totalTimeTextView = (TextView) findViewById(R.id.total);
//        bottomContainer = (ViewGroup) findViewById(R.id.layout_bottom);
//        textureViewContainer = (RelativeLayout) findViewById(R.id.surface_container);
//        topContainer = (ViewGroup) findViewById(R.id.layout_top);
//
//        startButton.setOnClickListener(this);
//        fullscreenButton.setOnClickListener(this);
//        progressBar.setOnSeekBarChangeListener(this);
//        bottomContainer.setOnClickListener(this);
//        textureViewContainer.setOnClickListener(this);
//
//        textureViewContainer.setOnTouchListener(this);
//        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
//        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
//        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//    }
//
//    public abstract int getLayoutId();
//
//    protected static void setJcBuriedPoint(JCBuriedPoint jcBuriedPoint) {
//        JC_BURIED_POINT = jcBuriedPoint;
//    }
//
//    public boolean setUp(String url, int screen, Object... objects) {
////        if (isCurrentMediaListener() &&
////                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
////            return false;
//        currentState = CURRENT_STATE_NORMAL;
//        this.url = url;
//        this.objects = objects;
//        this.currentScreen = screen;
//        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
//        return true;
//    }
//
//    public boolean setUp(String url, Map<String, String> mapHeadData, int screen, Object... objects) {
//        if (setUp(url, screen, objects)) {
//            this.mapHeadData.clear();
//            this.mapHeadData.putAll(mapHeadData);
//            return true;
//        }
//        return false;
//    }
//
//    public void setLoop(boolean looping) {
//        this.looping = looping;
//    }
//
//    //set ui
//    public void setUiWitStateAndScreen(int state) {
//        currentState = state;
//        switch (currentState) {
//            case CURRENT_STATE_NORMAL:
//                if (isCurrentMediaListener()) {
//                    cancelProgressTimer();
//                    JCMediaManager.instance().releaseMediaPlayer();
//                }
//                break;
//            case CURRENT_STATE_PREPAREING:
//                resetProgressAndTime();
//                break;
//            case CURRENT_STATE_PLAYING:
//                startProgressTimer();
//                break;
//            case CURRENT_STATE_PAUSE:
//                startProgressTimer();
//                break;
//            case CURRENT_STATE_ERROR:
//                if (isCurrentMediaListener()) {
//                    JCMediaManager.instance().releaseMediaPlayer();
//                }
//                break;
//            case CURRENT_STATE_AUTO_COMPLETE:
//                cancelProgressTimer();
//                progressBar.setProgress(100);
//                currentTimeTextView.setText(totalTimeTextView.getText());
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.start) {
//            Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
//            if (TextUtils.isEmpty(url)) {
//                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR) {
//                if (!url.startsWith("file") && !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
//                    showWifiDialog();
//                    return;
//                }
//                startButtonLogic();
//            } else if (currentState == CURRENT_STATE_PLAYING) {
//                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
//                JCMediaManager.instance().mediaPlayer.pause();
//                setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
//                if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
////                    if (mIfCurrentIsFullscreen) {
////                        JC_BURIED_POINT.onClickStopFullscreen(url, objects);
////                    } else {
////                        JC_BURIED_POINT.onClickStop(url, objects);
////                    }
//                }
//            } else if (currentState == CURRENT_STATE_PAUSE) {
//                if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
////                    if (mIfCurrentIsFullscreen) {
////                        JC_BURIED_POINT.onClickResumeFullscreen(url, objects);
////                    } else {
////                        JC_BURIED_POINT.onClickResume(url, objects);
////                    }
//                }
//                JCMediaManager.instance().mediaPlayer.start();
//                setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
//            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
//                startButtonLogic();
//            }
//        } else if (i == R.id.fullscreen) {
//            Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
//            if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
//            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//                //quit fullscreen
//                backPress();
//            } else {
//                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
//                if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
//                    JC_BURIED_POINT.onEnterFullscreen(url, objects);
//                }
//
//                toWindowFullscreen();
//            }
//        } else if (i == R.id.surface_container && currentState == CURRENT_STATE_ERROR) {
//            Log.i(TAG, "onClick surfaceContainer State=Error [" + this.hashCode() + "] ");
//            if (JC_BURIED_POINT != null) {
//                JC_BURIED_POINT.onClickStartError(url, objects);
//            }
//            prepareVideo();
//        }
//    }
//
//    private void toWindowFullscreen() {
//        ((AppCompatActivity) getContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ((AppCompatActivity) getContext()).getSupportActionBar().setShowHideAnimationEnabled(false);
//        ((AppCompatActivity) getContext()).getSupportActionBar().hide();
//
//        IF_FULLSCREEN_FROM_NORMAL = true;
//        IF_RELEASE_WHEN_ON_PAUSE = false;
//
//        ViewGroup vp = (ViewGroup) ((Activity) getContext()).findViewById(Window.ID_ANDROID_CONTENT);
//        View old = vp.findViewById(FULLSCREEN_ID);
//        if (old != null) {
//            vp.removeView(old);
//        }
//        try {
//            Constructor<BBVideoPlayer> constructor = (Constructor<BBVideoPlayer>) BBVideoPlayer.this.getClass().getConstructor(Context.class);
//            BBVideoPlayer jcVideoPlayer = constructor.newInstance(getContext());
//            jcVideoPlayer.setId(FULLSCREEN_ID);
//            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//            int w = wm.getDefaultDisplay().getWidth();
//            int h = wm.getDefaultDisplay().getHeight();
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(h, w);
//            lp.setMargins((w - h) / 2, -(w - h) / 2, 0, 0);
//            vp.addView(jcVideoPlayer, lp);
//            jcVideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, objects);
//            jcVideoPlayer.setUiWitStateAndScreen(currentState);
//            jcVideoPlayer.addTextureView();
//            jcVideoPlayer.setRotation(90);
//
//            JCVideoPlayerManager.setLastListener(this);
//            JCVideoPlayerManager.setListener(jcVideoPlayer);
//
//
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void toWindowTiny() {
//        ViewGroup vp = (ViewGroup) ((Activity) getContext()).findViewById(Window.ID_ANDROID_CONTENT);
//        View old = vp.findViewById(TINY_ID);
//        if (old != null) {
//            vp.removeView(old);
//        }
//        try {
//            Constructor<BBVideoPlayer> constructor = (Constructor<BBVideoPlayer>) BBVideoPlayer.this.getClass().getConstructor(Context.class);
//            BBVideoPlayer mJcVideoPlayer = constructor.newInstance(getContext());
//            mJcVideoPlayer.setId(TINY_ID);
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 400);
//            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
//            vp.addView(mJcVideoPlayer, lp);
//            mJcVideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_WINDOW_TINY, objects);
//            mJcVideoPlayer.setUiWitStateAndScreen(currentState);
//            mJcVideoPlayer.addTextureView();
//            JCVideoPlayerManager.setLastListener(this);
//            JCVideoPlayerManager.setListener(mJcVideoPlayer);
//
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void showWifiDialog() {
//
//    }
//
//    public static boolean backPress() {
//        if (JCVideoPlayerManager.listener() != null) {
//            return JCVideoPlayerManager.listener().goToOtherListener();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean goToOtherListener() {
//        if (currentScreen == JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN
//                || currentScreen == JCVideoPlayerStandard.SCREEN_WINDOW_TINY) {
//            ViewGroup vp = (ViewGroup) ((Activity) getContext()).findViewById(Window.ID_ANDROID_CONTENT);
//            vp.removeView(this);
//
//            JCVideoPlayerManager.setListener(JCVideoPlayerManager.lastListener());
//            JCVideoPlayerManager.setLastListener(null);
//            JCMediaManager.instance().lastState = currentState;//save state
//            JCVideoPlayerManager.listener().goBackThisListener();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void goBackThisListener() {
//        currentState = JCMediaManager.instance().lastState;
//        setUiWitStateAndScreen(currentState);
//        addTextureView();
//
//        ((AppCompatActivity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ((AppCompatActivity) getContext()).getSupportActionBar().setShowHideAnimationEnabled(false);
//        ((AppCompatActivity) getContext()).getSupportActionBar().show();
//    }
//
//    private void startButtonLogic() {
//        if (JC_BURIED_POINT != null && currentState == CURRENT_STATE_NORMAL) {
//            JC_BURIED_POINT.onClickStartIcon(url, objects);
//        } else if (JC_BURIED_POINT != null) {
//            JC_BURIED_POINT.onClickStartError(url, objects);
//        }
//        prepareVideo();
//    }
//
//    protected void prepareVideo() {
//        Log.d(TAG, "prepareVideo [" + this.hashCode() + "] ");
//        if (JCVideoPlayerManager.listener() != null) {
//            JCVideoPlayerManager.listener().onCompletion();
//        }
//        JCVideoPlayerManager.setListener(this);
//        addTextureView();
//        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        JCMediaManager.instance().prepare(url, mapHeadData, looping);
//        setUiWitStateAndScreen(CURRENT_STATE_PREPAREING);
//
//    }
//
//    private static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            switch (focusChange) {
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    releaseAllVideos();
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
//                        JCMediaManager.instance().mediaPlayer.pause();
//                    }
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    break;
//            }
//        }
//    };
//
//    public void addTextureView() {
//        Log.d(TAG, "addTextureView [" + this.hashCode() + "] ");
//        if (textureViewContainer.getChildCount() > 0) {
//            textureViewContainer.removeAllViews();
//        }
//        JCMediaManager.textureView = null;
//        JCMediaManager.textureView = new JCResizeTextureView(getContext());
//        JCMediaManager.textureView.setSurfaceTextureListener(this);
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        textureViewContainer.addView(JCMediaManager.textureView, layoutParams);
//    }
//
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//        Log.i(TAG, "onSurfaceTextureAvailable [" + this.hashCode() + "] ");
//        surface = new Surface(surface);
//        JCMediaManager.instance().setDisplay(surface);
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        surface.release();
//        return true;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        int id = v.getId();
//        if (id == R.id.surface_container) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    Log.i(TAG, "onTouch surfaceContainer actionDown [" + this.hashCode() + "] ");
//                    mTouchingProgressBar = true;
//
//                    mDownX = x;
//                    mDownY = y;
//                    mChangeVolume = false;
//                    mChangePosition = false;
//                    /////////////////////
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    Log.i(TAG, "onTouch surfaceContainer actionMove [" + this.hashCode() + "] ");
//                    float deltaX = x - mDownX;
//                    float deltaY = y - mDownY;
//                    float absDeltaX = Math.abs(deltaX);
//                    float absDeltaY = Math.abs(deltaY);
//                    if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//                        if (!mChangePosition && !mChangeVolume) {
//                            if (absDeltaX > THRESHOLD || absDeltaY > THRESHOLD) {
//                                cancelProgressTimer();
//                                if (absDeltaX >= THRESHOLD) {
//                                    mChangePosition = true;
//                                    mDownPosition = getCurrentPositionWhenPlaying();
//                                    if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
//                                        JC_BURIED_POINT.onTouchScreenSeekPosition(url, objects);
//                                    }
//                                } else {
//                                    mChangeVolume = true;
//                                    mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                                    if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
//                                        JC_BURIED_POINT.onTouchScreenSeekVolume(url, objects);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (mChangePosition) {
//                        int totalTimeDuration = getDuration();
//                        mSeekTimePosition = (int) (mDownPosition + deltaX * totalTimeDuration / mScreenWidth);
//                        if (mSeekTimePosition > totalTimeDuration)
//                            mSeekTimePosition = totalTimeDuration;
//                        String seekTime = JCUtils.stringForTime(mSeekTimePosition);
//                        String totalTime = JCUtils.stringForTime(totalTimeDuration);
//
//                        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
//                    }
//                    if (mChangeVolume) {
//                        deltaY = -deltaY;
//                        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                        int deltaV = (int) (max * deltaY * 3 / mScreenHeight);
//                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0);
//                        int volumePercent = (int) (mGestureDownVolume * 100 / max + deltaY * 3 * 100 / mScreenHeight);
//
//                        showVolumDialog(-deltaY, volumePercent);
//                    }
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    Log.i(TAG, "onTouch surfaceContainer actionUp [" + this.hashCode() + "] ");
//                    mTouchingProgressBar = false;
//                    dismissProgressDialog();
//                    dismissVolumDialog();
//                    if (mChangePosition) {
//                        JCMediaManager.instance().mediaPlayer.seekTo(mSeekTimePosition);
//                        int duration = getDuration();
//                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
//                        progressBar.setProgress(progress);
//                    }
//                    /////////////////////
//                    startProgressTimer();
//                    if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
////                        if (mIfCurrentIsFullscreen) {
////                            JC_BURIED_POINT.onClickSeekbarFullscreen(url, objects);
////                        } else {
////                            JC_BURIED_POINT.onClickSeekbar(url, objects);
////                        }
//                    }
//                    break;
//            }
//        }
//        return false;
//    }
//
//    protected void showProgressDialog(float deltaX,
//                                      String seekTime, int seekTimePosition,
//                                      String totalTime, int totalTimeDuration) {
//
//    }
//
//    protected void dismissProgressDialog() {
//
//    }
//
//    protected void showVolumDialog(float deltaY, int volumePercent) {
//
//    }
//
//    protected void dismissVolumDialog() {
//
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        Log.i(TAG, "bottomProgress onStartTrackingTouch [" + this.hashCode() + "] ");
//        cancelProgressTimer();
//        ViewParent vpdown = getParent();
//        while (vpdown != null) {
//            vpdown.requestDisallowInterceptTouchEvent(true);
//            vpdown = vpdown.getParent();
//        }
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        Log.i(TAG, "bottomProgress onStopTrackingTouch [" + this.hashCode() + "] ");
//        startProgressTimer();
//        ViewParent vpup = getParent();
//        while (vpup != null) {
//            vpup.requestDisallowInterceptTouchEvent(false);
//            vpup = vpup.getParent();
//        }
//        if (currentState != CURRENT_STATE_PLAYING &&
//                currentState != CURRENT_STATE_PAUSE) return;
//        int time = seekBar.getProgress() * getDuration() / 100;
//        JCMediaManager.instance().mediaPlayer.seekTo(time);
//        Log.i(TAG, "seekTo " + time + " [" + this.hashCode() + "] ");
//    }
//
//    @Override
//    public void onPrepared() {
//        if (currentState != CURRENT_STATE_PREPAREING) return;
//        JCMediaManager.instance().mediaPlayer.start();
//        if (seekToInAdvance != -1) {
//            JCMediaManager.instance().mediaPlayer.seekTo(seekToInAdvance);
//            seekToInAdvance = -1;
//        }
//        startProgressTimer();
//        setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
//    }
//
//    @Override
//    public void onAutoCompletion() {
//        //make me normal first
//        if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
////            if (mIfCurrentIsFullscreen) {
////                JC_BURIED_POINT.onAutoCompleteFullscreen(url, objects);
////            } else {
////                JC_BURIED_POINT.onAutoComplete(url, objects);
////            }
//        }
//        setUiWitStateAndScreen(CURRENT_STATE_AUTO_COMPLETE);
//        if (textureViewContainer.getChildCount() > 0) {
//            textureViewContainer.removeAllViews();
//        }
//        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
//            IF_FULLSCREEN_FROM_NORMAL = false;
//            JCVideoPlayerManager.lastListener().onAutoCompletion();
//        }
//        JCVideoPlayerManager.setLastListener(null);
//        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
//        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        ViewGroup vp = (ViewGroup) ((Activity) getContext()).findViewById(Window.ID_ANDROID_CONTENT);
//        View oldF = vp.findViewById(FULLSCREEN_ID);
//        View oldT = vp.findViewById(TINY_ID);
//        if (oldF != null) {
//            vp.removeView(oldF);
//        }
//        if (oldT != null) {
//            vp.removeView(oldT);
//        }
//    }
//
//    @Override
//    public void onCompletion() {
//        //make me normal first
//        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
//        if (textureViewContainer.getChildCount() > 0) {
//            textureViewContainer.removeAllViews();
//        }
//        //if fullscreen finish activity what ever the activity is directly or click fullscreen
//
//        if (IF_FULLSCREEN_FROM_NORMAL) {//如果在进入全屏后播放完就初始化自己非全屏的控件
//            IF_FULLSCREEN_FROM_NORMAL = false;
////            JCMediaManager.instance().lastListener().onCompletion();
//        }
//        JCVideoPlayerManager.setListener(null);
//        JCVideoPlayerManager.setLastListener(null);
//        JCMediaManager.instance().currentVideoWidth = 0;
//        JCMediaManager.instance().currentVideoHeight = 0;
//
//        AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
//        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//    }
//
//    @Override
//    public void onBufferingUpdate(int percent) {
//        if (currentState != CURRENT_STATE_NORMAL && currentState != CURRENT_STATE_PREPAREING) {
//            Log.v(TAG, "onBufferingUpdate " + percent + " [" + this.hashCode() + "] ");
//            setTextAndProgress(percent);
//        }
//    }
//
//    @Override
//    public void onSeekComplete() {
//
//    }
//
//    @Override
//    public void onError(int what, int extra) {
//        Log.e(TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ");
//        if (what != 38 && what != -38) {
//            setUiWitStateAndScreen(CURRENT_STATE_ERROR);
//        }
//    }
//
//    @Override
//    public void onInfo(int what, int extra) {
//        Log.d(TAG, "onInfo what - " + what + " extra - " + extra);
//        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//            mBackUpBufferState = currentState;
//            setUiWitStateAndScreen(CURRENT_STATE_PLAYING_BUFFERING_START);
//            Log.d(TAG, "MEDIA_INFO_BUFFERING_START");
//        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//            if (mBackUpBufferState != -1) {
//                setUiWitStateAndScreen(mBackUpBufferState);
//                mBackUpBufferState = -1;
//            }
//            Log.d(TAG, "MEDIA_INFO_BUFFERING_END");
//        }
//    }
//
//    @Override
//    public void onVideoSizeChanged() {
//        int mVideoWidth = JCMediaManager.instance().currentVideoWidth;
//        int mVideoHeight = JCMediaManager.instance().currentVideoHeight;
//        if (mVideoWidth != 0 && mVideoHeight != 0) {
//            JCMediaManager.textureView.requestLayout();
//        }
//    }
//
//    protected void startProgressTimer() {
//        cancelProgressTimer();
//        UPDATE_PROGRESS_TIMER = new Timer();
//        mProgressTimerTask = new ProgressTimerTask();
//        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 300);
//    }
//
//    protected void cancelProgressTimer() {
//        if (UPDATE_PROGRESS_TIMER != null) {
//            UPDATE_PROGRESS_TIMER.cancel();
//        }
//        if (mProgressTimerTask != null) {
//            mProgressTimerTask.cancel();
//        }
//    }
//
//    protected class ProgressTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            if (currentState == CURRENT_STATE_PLAYING || currentState == CURRENT_STATE_PAUSE) {
//                int position = getCurrentPositionWhenPlaying();
//                int duration = getDuration();
//                Log.v(TAG, "onProgressUpdate " + position + "/" + duration + " [" + this.hashCode() + "] ");
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        setTextAndProgress(0);
//                    }
//                });
//            }
//        }
//    }
//
//    protected int getCurrentPositionWhenPlaying() {
//        int position = 0;
//        if (currentState == CURRENT_STATE_PLAYING || currentState == CURRENT_STATE_PAUSE) {
//            try {
//                position = (int) JCMediaManager.instance().mediaPlayer.getCurrentPosition();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//                return position;
//            }
//        }
//        return position;
//    }
//
//    protected int getDuration() {
//        int duration = 0;
//        try {
//            duration = (int) JCMediaManager.instance().mediaPlayer.getDuration();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            return duration;
//        }
//        return duration;
//    }
//
//    protected void setTextAndProgress(int secProgress) {
//        int position = getCurrentPositionWhenPlaying();
//        int duration = getDuration();
//        // if duration == 0 (e.g. in HLS streams) avoids ArithmeticException
//        int progress = position * 100 / (duration == 0 ? 1 : duration);
//        setProgressAndTime(progress, secProgress, position, duration);
//    }
//
//    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
//        if (!mTouchingProgressBar) {
//            if (progress != 0) progressBar.setProgress(progress);
//        }
//        if (secProgress > 95) secProgress = 100;
//        if (secProgress != 0) progressBar.setSecondaryProgress(secProgress);
//        currentTimeTextView.setText(JCUtils.stringForTime(currentTime));
//        totalTimeTextView.setText(JCUtils.stringForTime(totalTime));
//    }
//
//    protected void resetProgressAndTime() {
//        progressBar.setProgress(0);
//        progressBar.setSecondaryProgress(0);
//        currentTimeTextView.setText(JCUtils.stringForTime(0));
//        totalTimeTextView.setText(JCUtils.stringForTime(0));
//    }
//
//    protected void quitFullScreenGoToNormal() {
//        Log.d(TAG, "quitFullScreenGoToNormal [" + this.hashCode() + "] ");
//        if (JC_BURIED_POINT != null && isCurrentMediaListener()) {
//            JC_BURIED_POINT.onQuitFullscreen(url, objects);
//        }
//        JCMediaManager.instance().setDisplay(null);
//        JCVideoPlayerManager.setListener(JCVideoPlayerManager.lastListener());
//        JCVideoPlayerManager.setLastListener(null);
//        JCMediaManager.instance().lastState = currentState;//save state
//        JCVideoPlayerManager.listener().goBackThisListener();
//        if (currentState == CURRENT_STATE_PAUSE) {
//            JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getCurrentPosition());
//        }
//    }
//
//    public static void releaseAllVideos() {
//        if (IF_RELEASE_WHEN_ON_PAUSE) {
//            Log.d(TAG, "releaseAllVideos");
//            if (JCVideoPlayerManager.listener() != null) {
//                JCVideoPlayerManager.listener().onCompletion();
//            }
//            JCMediaManager.instance().releaseMediaPlayer();
//        } else {
//            IF_RELEASE_WHEN_ON_PAUSE = true;
//        }
//    }
//
//    /**
//     * if I am playing release me
//     */
//    public void release() {
//        if (isCurrentMediaListener() &&
//                (System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
//            Log.d(TAG, "release [" + this.hashCode() + "]");
//            releaseAllVideos();
//        }
//    }
//
//    protected boolean isCurrentMediaListener() {
//        return JCVideoPlayerManager.listener() != null
//                && JCVideoPlayerManager.listener() == this;
//    }
//}
