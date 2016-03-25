package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * <p>节操视频播放器，库的外面所有使用的接口也在这里</p>
 * <p>Jiecao video player，all outside the library interface is here</p>
 *
 * @see <a href="https://github.com/lipangit/jiecaovideoplayer">JiecaoVideoplayer Github</a>
 * Created by Nathen
 * On 2015/11/30 11:59
 */
public class JCVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, View.OnTouchListener {

    //控件
    public ImageView ivStart;
    ProgressBar pbLoading, pbBottom;
    ImageView ivFullScreen;
    SeekBar skProgress;
    TextView tvTimeCurrent, tvTimeTotal;
    ResizeSurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    TextView tvTitle;
    ImageView ivBack;
    public ImageView ivThumb;
    RelativeLayout rlParent;
    LinearLayout llTitleContainer, llBottomControl;
    ImageView ivCover;

    //属性
    private String url;
    private String title;
    private boolean ifFullScreen = false;
    public String uuid;//区别相同地址,包括全屏和不全屏，和都不全屏时的相同地址
    public boolean ifShowTitle = false;
    private boolean ifMp3 = false;

    private int enlargRecId = 0;
    private int shrinkRecId = 0;

    public static Skin globleSkin;
    private Skin skin;

    // 为了保证全屏和退出全屏之后的状态和之前一样,需要记录状态
    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STATE_PREPAREING = 0;
    public static final int CURRENT_STATE_PAUSE = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_OVER = 3;//这个状态可能不需要，播放完毕就进入normal状态
    public static final int CURRENT_STATE_NORMAL = 4;//刚初始化之后
    private OnTouchListener mSeekbarOnTouchListener;
    private static Timer mDismissControlViewTimer;
    private static Timer mUpdateProgressTimer;
    private static long clickfullscreentime;
    private static final int FULL_SCREEN_NORMAL_DELAY = 5000;

    // 一些临时表示状态的变量
    private boolean touchingProgressBar = false;
    private static boolean isFromFullScreenBackHere = false;//如果是true表示这个正在不是全屏，并且全屏刚推出，总之进入过全屏
    static boolean isClickFullscreen = false;

    private static ImageView.ScaleType speScalType = null;

    public JCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        uuid = UUID.randomUUID().toString();
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.video_control_view, this);
        ivStart = (ImageView) findViewById(R.id.start);
        pbLoading = (ProgressBar) findViewById(R.id.loading);
        pbBottom = (ProgressBar) findViewById(R.id.bottom_progressbar);
        ivFullScreen = (ImageView) findViewById(R.id.fullscreen);
        skProgress = (SeekBar) findViewById(R.id.progress);
        tvTimeCurrent = (TextView) findViewById(R.id.current);
        tvTimeTotal = (TextView) findViewById(R.id.total);
        surfaceView = (ResizeSurfaceView) findViewById(R.id.surfaceView);
        llBottomControl = (LinearLayout) findViewById(R.id.bottom_control);
        tvTitle = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.back);
        ivThumb = (ImageView) findViewById(R.id.thumb);
        rlParent = (RelativeLayout) findViewById(R.id.parentview);
        llTitleContainer = (LinearLayout) findViewById(R.id.title_container);
        ivCover = (ImageView) findViewById(R.id.cover);

//        surfaceView.setZOrderOnTop(true);
//        surfaceView.setBackgroundColor(R.color.black_a10_color);
        surfaceHolder = surfaceView.getHolder();
        ivStart.setOnClickListener(this);
        ivThumb.setOnClickListener(this);
        ivFullScreen.setOnClickListener(this);
        skProgress.setOnSeekBarChangeListener(this);
        surfaceHolder.addCallback(this);
        surfaceView.setOnClickListener(this);
        llBottomControl.setOnClickListener(this);
        rlParent.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        skProgress.setOnTouchListener(this);
        if (speScalType != null) {
            ivThumb.setScaleType(speScalType);
        }
    }

    /**
     * <p>配置要播放的内容</p>
     * <p>Configuring the Content to Play</p>
     *
     * @param url   视频地址 | Video address
     * @param title 标题 | title
     */
    public void setUp(String url, String title) {
        setUp(url, title, true);
    }

    /**
     * <p>配置要播放的内容</p>
     * <p>Configuring the Content to Play</p>
     *
     * @param url         视频地址 | Video address
     * @param title       标题 | title
     * @param ifShowTitle 是否在非全屏下显示标题 | The title is displayed in full-screen under
     */
    public void setUp(String url, String title, boolean ifShowTitle) {
        setSkin();
        setIfShowTitle(ifShowTitle);
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) return;
        this.url = url;
        this.title = title;
        this.ifFullScreen = false;
        if (ifFullScreen) {
            ivFullScreen.setImageResource(enlargRecId == 0 ? R.drawable.shrink_video : enlargRecId);
        } else {
            ivFullScreen.setImageResource(shrinkRecId == 0 ? R.drawable.enlarge_video : shrinkRecId);
            ivBack.setVisibility(View.GONE);
        }
        tvTitle.setText(title);

        changeUiToNormal();

        CURRENT_STATE = CURRENT_STATE_NORMAL;
        if (uuid.equals(JCMediaManager.intance().uuid)) {
            JCMediaManager.intance().mediaPlayer.stop();
        }
        if (!TextUtils.isEmpty(url) && url.contains(".mp3")) {
            ifMp3 = true;
        }
    }

    /**
     * <p>只在全全屏中调用的方法</p>
     * <p>Only in fullscreen can call this</p>
     *
     * @param url   视频地址 | Video address
     * @param title 标题 | title
     */
    public void setUpForFullscreen(String url, String title) {
        setSkin();
        this.url = url;
        this.title = title;
        setIfShowTitle(true);
        if (ifFullScreen) {
            ivFullScreen.setImageResource(shrinkRecId == 0 ? R.drawable.shrink_video : shrinkRecId);
        } else {
            ivFullScreen.setImageResource(enlargRecId == 0 ? R.drawable.enlarge_video : enlargRecId);
        }
        tvTitle.setText(title);

        changeUiToNormal();

        CURRENT_STATE = CURRENT_STATE_NORMAL;
        setTitleVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(url) && url.contains(".mp3")) {
            ifMp3 = true;
        }
    }

    /**
     * <p>只在全全屏中调用的方法</p>
     * <p>Only in fullscreen can call this</p>
     *
     * @param state int state
     */
    public void setState(int state) {
        this.CURRENT_STATE = state;
        //全屏或取消全屏时继续原来的状态
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            changeUiToShowUiPrepareing();
            setProgressAndTime(0, 0, 0);
            setProgressBuffered(0);
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            changeUiToShowUiPlaying();
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            changeUiToShowUiPause();
        } else if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
            if (uuid.equals(JCMediaManager.intance().uuid)) {
                JCMediaManager.intance().mediaPlayer.stop();
            }
            changeUiToNormal();
            cancelDismissControlViewTimer();
            cancelProgressTimer();
        }
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
            CURRENT_STATE = CURRENT_STATE_NORMAL;
            cancelProgressTimer();
            setKeepScreenOn(false);
            changeUiToNormal();
            if (JCMediaManager.intance().uuid.equals(uuid)) {
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_AUTO_COMPLETE_FULLSCREEN : VideoEvents.POINT_AUTO_COMPLETE);
            }
        }
        if (!JCMediaManager.intance().uuid.equals(uuid)) {
            if (videoEvents.type == VideoEvents.VE_START) {
                if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                    setState(CURRENT_STATE_NORMAL);
                }
            }
            return;
        }
        if (videoEvents.type == VideoEvents.VE_PREPARED) {
            if (CURRENT_STATE != CURRENT_STATE_PREPAREING) return;
            JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
            JCMediaManager.intance().mediaPlayer.start();
            CURRENT_STATE = CURRENT_STATE_PLAYING;

            changeUiToShowUiPlaying();
            ivStart.setVisibility(View.INVISIBLE);

            startDismissControlViewTimer();
            startProgressTimer();
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_UPDATE_BUFFER) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {
                int percent = Integer.valueOf(videoEvents.obj.toString());
                setProgressBuffered(percent);
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_UPDATE_PROGRESS) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {
                setProgressAndTimeFromTimer();
            }
        } else if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN) {
            if (isClickFullscreen) {
                isFromFullScreenBackHere = true;
                isClickFullscreen = false;
                int prev_state = Integer.valueOf(videoEvents.obj.toString());
                setState(prev_state);
            }
        } else if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_CREATED) {
            if (isFromFullScreenBackHere) {
                JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
                stopToFullscreenOrQuitFullscreenShowDisplay();
                isFromFullScreenBackHere = false;
                startDismissControlViewTimer();
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_RESIZE) {
            int mVideoWidth = JCMediaManager.intance().currentVideoWidth;
            int mVideoHeight = JCMediaManager.intance().currentVideoHeight;
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
                surfaceView.requestLayout();
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE) {
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 目前认为详细的判断和重复的设置是有相当必要的,也可以包装成方法
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start || i == R.id.thumb) {
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), "视频地址为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (i == R.id.thumb) {
                if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                    onClickUiToggle();
                    return;
                }
            }
            if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
                JCMediaManager.intance().clearWidthAndHeight();
                CURRENT_STATE = CURRENT_STATE_PREPAREING;
                changeUiToShowUiPrepareing();
                llBottomControl.setVisibility(View.INVISIBLE);
                llTitleContainer.setVisibility(View.INVISIBLE);
                setProgressAndTime(0, 0, 0);
                setProgressBuffered(0);
                JCMediaManager.intance().prepareToPlay(getContext(), url);
                JCMediaManager.intance().setUuid(uuid);
                Log.i("JCVideoPlayer", "play video");

                VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_START);
                videoEvents.obj = uuid;
                EventBus.getDefault().post(videoEvents);
                surfaceView.requestLayout();
                setKeepScreenOn(true);

                sendPointEvent(i == R.id.start ? VideoEvents.POINT_START_ICON : VideoEvents.POINT_START_THUMB);
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                CURRENT_STATE = CURRENT_STATE_PAUSE;

                changeUiToShowUiPause();

                JCMediaManager.intance().mediaPlayer.pause();
                Log.i("JCVideoPlayer", "pause video");

                setKeepScreenOn(false);
                cancelDismissControlViewTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_STOP_FULLSCREEN : VideoEvents.POINT_STOP);
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                CURRENT_STATE = CURRENT_STATE_PLAYING;

                changeUiToShowUiPlaying();
                JCMediaManager.intance().mediaPlayer.start();
                Log.i("JCVideoPlayer", "go on video");

                setKeepScreenOn(true);
                startDismissControlViewTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_RESUME_FULLSCREEN : VideoEvents.POINT_RESUME);
            }

        } else if (i == R.id.fullscreen) {
            if (ifFullScreen) {
                quitFullScreen();
            } else {
                JCFullScreenActivity.skin = skin;
                JCMediaManager.intance().mediaPlayer.pause();
                JCMediaManager.intance().mediaPlayer.setDisplay(null);
                JCMediaManager.intance().backUpUuid();
                isClickFullscreen = true;
                JCFullScreenActivity.toActivityFromNormal(getContext(), CURRENT_STATE, url, title);
                sendPointEvent(VideoEvents.POINT_ENTER_FULLSCREEN);
            }
            clickfullscreentime = System.currentTimeMillis();
        } else if (i == R.id.surfaceView || i == R.id.parentview) {
            onClickUiToggle();
            startDismissControlViewTimer();
            sendPointEvent(ifFullScreen ? VideoEvents.POINT_CLICK_BLANK_FULLSCREEN : VideoEvents.POINT_CLICK_BLANK);
        } else if (i == R.id.bottom_control) {
            //JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        } else if (i == R.id.back) {
            quitFullScreen();
        }
    }

    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (uuid.equals(JCMediaManager.intance().uuid)) {
                    if (getContext() != null && getContext() instanceof Activity) {
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                llBottomControl.setVisibility(View.INVISIBLE);
                                pbBottom.setVisibility(View.VISIBLE);
                                setTitleVisibility(View.INVISIBLE);
                                ivStart.setVisibility(View.INVISIBLE);//TODO check this for ui
                            }
                        });
                    }
                }
            }
        }, 2500);
    }

    private void cancelDismissControlViewTimer() {
        if (mDismissControlViewTimer != null) {
            mDismissControlViewTimer.cancel();
        }
    }

    private void onClickUiToggle() {
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                changeUiToClearUiPrepareing();
            } else {
                changeUiToShowUiPrepareing();
            }
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                changeUiToClearUiPlaying();
            } else {
                changeUiToShowUiPlaying();
            }
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                changeUiToClearUiPause();
            } else {
                changeUiToShowUiPause();
            }
        }
    }

    //Unified management Ui
    private void changeUiToNormal() {
        setTitleVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        setThumbVisibility(View.VISIBLE);
        ivCover.setVisibility(View.VISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToShowUiPrepareing() {
        setTitleVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        setThumbVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.VISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
    }

    private void changeUiToClearUiPrepareing() {
        changeUiToClearUi();
        pbLoading.setVisibility(View.VISIBLE);
    }

    private void changeUiToShowUiPlaying() {
        setTitleVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        setThumbVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToClearUiPlaying() {
        changeUiToClearUi();
        pbBottom.setVisibility(View.VISIBLE);
    }

    private void changeUiToShowUiPause() {
        setTitleVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        setThumbVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToClearUiPause() {
        changeUiToClearUi();
        pbBottom.setVisibility(View.VISIBLE);
    }

    private void changeUiToClearUi() {
        setTitleVisibility(View.INVISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        setThumbVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
    }

    private void startProgressTimer() {
        cancelProgressTimer();
        mUpdateProgressTimer = new Timer();
        mUpdateProgressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_UPDATE_PROGRESS);
                            EventBus.getDefault().post(videoEvents);
                        }
                    });
                }
            }
        }, 0, 300);
    }

    private void cancelProgressTimer() {
        if (uuid.equals(JCMediaManager.intance().uuid)) {
            if (mUpdateProgressTimer != null) {
                mUpdateProgressTimer.cancel();
            }
        }
    }

    public void setIfShowTitle(boolean ifShowTitle) {
        this.ifShowTitle = ifShowTitle;
    }

    //if show title in top level logic
    private void setTitleVisibility(int visable) {
        if (ifShowTitle) {
            llTitleContainer.setVisibility(visable);
        } else {
            if (ifFullScreen) {
                llTitleContainer.setVisibility(visable);
            } else {
                llTitleContainer.setVisibility(View.INVISIBLE);
            }
        }
    }

    //if show thumb in top level logic
    private void setThumbVisibility(int visable) {
        if (ifMp3) {
            ivThumb.setVisibility(View.VISIBLE);
        } else {
            ivThumb.setVisibility(visable);
        }
    }

    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            ivStart.setImageResource(R.drawable.click_video_pause_selector);
        } else {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
        }
    }

    private void setProgressBuffered(int secProgress) {
        if (secProgress >= 0) {
            skProgress.setSecondaryProgress(secProgress);
            pbBottom.setSecondaryProgress(secProgress);
        }
    }

    private void setProgressAndTimeFromTimer() {
        int position = JCMediaManager.intance().mediaPlayer.getCurrentPosition();
        int duration = JCMediaManager.intance().mediaPlayer.getDuration();
        // if duration == 0 (e.g. in HLS streams) avoids ArithmeticException
        int progress = position * 100 / (duration == 0 ? 1 : duration);
        setProgressAndTime(progress, position, duration);
    }

    private void setProgressAndTime(int progress, int currentTime, int totalTime) {
        if (!touchingProgressBar) {
            skProgress.setProgress(progress);
            pbBottom.setProgress(progress);
        }
        tvTimeCurrent.setText(Utils.stringForTime(currentTime));
        tvTimeTotal.setText(Utils.stringForTime(totalTime));
    }

    public void release() {
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) return;
        setState(CURRENT_STATE_NORMAL);
        //回收surfaceview
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress * JCMediaManager.intance().mediaPlayer.getDuration() / 100;
            JCMediaManager.intance().mediaPlayer.seekTo(time);
            pbLoading.setVisibility(View.VISIBLE);
            ivStart.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
//        cancelDismissControlViewTimer();
        if (uuid.equals(JCMediaManager.intance().uuid)) {
            JCMediaManager.intance().mediaPlayer.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void quitFullScreen() {
        JCFullScreenActivity.manualQuit = true;
        clickfullscreentime = System.currentTimeMillis();
        JCMediaManager.intance().mediaPlayer.pause();
        JCMediaManager.intance().mediaPlayer.setDisplay(null);
        JCMediaManager.intance().revertUuid();
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN);
        videoEvents.obj = CURRENT_STATE;
        EventBus.getDefault().post(videoEvents);
        sendPointEvent(VideoEvents.POINT_QUIT_FULLSCREEN);
    }

    private void stopToFullscreenOrQuitFullscreenShowDisplay() {
        if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            JCMediaManager.intance().mediaPlayer.start();
            CURRENT_STATE = CURRENT_STATE_PLAYING;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JCMediaManager.intance().mediaPlayer.pause();
                            CURRENT_STATE = CURRENT_STATE_PAUSE;
                        }
                    });
                }
            }).start();
            surfaceView.requestLayout();
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            JCMediaManager.intance().mediaPlayer.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //TODO MediaPlayer set holder,MediaPlayer prepareToPlay
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_CREATED));
        if (ifFullScreen) {
            JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
            stopToFullscreenOrQuitFullscreenShowDisplay();
        }
        if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
            startDismissControlViewTimer();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * <p>停止所有音频的播放</p>
     * <p>release all videos</p>
     */
    public static void releaseAllVideos() {
        if (!isClickFullscreen) {
            JCMediaManager.intance().mediaPlayer.stop();
            JCMediaManager.intance().setUuid("");
            JCMediaManager.intance().setUuid("");
            EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
            if (mUpdateProgressTimer != null) {
                mUpdateProgressTimer.cancel();
            }
        }
    }

    /**
     * <p>有特殊需要的客户端</p>
     * <p>Clients with special needs</p>
     *
     * @param onClickListener 开始按钮点击的回调函数 | Click the Start button callback function
     */
    @Deprecated
    public void setStartListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            ivStart.setOnClickListener(onClickListener);
            ivThumb.setOnClickListener(onClickListener);
        } else {
            ivStart.setOnClickListener(this);
            ivThumb.setOnClickListener(this);
        }
    }

    private void sendPointEvent(int type) {
        VideoEvents videoEvents = new VideoEvents();
        videoEvents.setType(type);
        videoEvents.obj = title;
        videoEvents.obj1 = url;
        EventBus.getDefault().post(videoEvents);
    }

    public void setSeekbarOnTouchListener(OnTouchListener listener) {
        mSeekbarOnTouchListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchingProgressBar = true;
                cancelDismissControlViewTimer();
                cancelProgressTimer();
                break;
            case MotionEvent.ACTION_UP:
                touchingProgressBar = false;
                startDismissControlViewTimer();
                startProgressTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_CLICK_SEEKBAR_FULLSCREEN : VideoEvents.POINT_CLICK_SEEKBAR);
                break;
        }

        if (mSeekbarOnTouchListener != null) {
            mSeekbarOnTouchListener.onTouch(v, event);
        }
        return false;
    }

    /**
     * <p>默认的缩略图的scaleType是fitCenter，这时候图片如果和屏幕尺寸不同的话左右或上下会有黑边，可以根据客户端需要改成fitXY或这其他模式</p>
     * <p>The default thumbnail scaleType is fitCenter, and this time the picture if different screen sizes up and down or left and right, then there will be black bars, or it may need to change fitXY other modes based on the client</p>
     *
     * @param thumbScaleType 缩略图的scalType | Thumbnail scaleType
     */
    public static void setThumbImageViewScalType(ImageView.ScaleType thumbScaleType) {
        speScalType = thumbScaleType;
    }

    /**
     * <p>只设置这一个播放器的皮肤<br>
     * 这个需要在setUp播放器的属性之前调用，因为enlarge图标的原因<br>
     * 所有参数如果不需要修改的设为0</p>
     * <p>This setting only one player skin<br>
     * This requires the player before setUp property called, because of the enlarge icon<br>
     * If you do not modify all parameters can be set to 0</p>
     *
     * @param titleColor              标题颜色 | title color
     * @param timeColor               时间颜色 | time color
     * @param seekDrawable            滑动条颜色 | seekbar color
     * @param bottomControlBackground 低栏背景 | background color
     * @param enlargRecId             全屏背景 | fullscreen background
     * @param shrinkRecId             退出全屏背景 | quit fullscreen background quit fullscreen
     */
    public void setSkin(int titleColor, int timeColor, int seekDrawable, int bottomControlBackground,
                        int enlargRecId, int shrinkRecId) {
        skin = new Skin(titleColor, timeColor, seekDrawable, bottomControlBackground,
                enlargRecId, shrinkRecId);
    }

    /**
     * <p>设置应用内所有播放器的皮肤</p>
     * <p>Apply all settings within the player skin</p>
     */
    public static void setGlobleSkin(int titleColor, int timeColor, int seekDrawable, int bottomControlBackground,
                                     int enlargRecId, int shrinkRecId) {
        globleSkin = new Skin(titleColor, timeColor, seekDrawable, bottomControlBackground,
                enlargRecId, shrinkRecId);
    }

    /**
     * In demo is ok, but in other project This will class not access exception,How to solve the problem
     */
    @Deprecated
    public static void toFullscreenActivity(Context context, String url, String title) {
        JCFullScreenActivity.toActivity(context, url, title);
    }

    private void setSkin() {
        if (skin != null) {
            setSkin(skin);
        } else {
            if (globleSkin != null) {
                setSkin(globleSkin);
            }
        }
    }

    private void setSkin(Skin skin) {
        Resources resource = getContext().getResources();
        if (skin.titleColor != 0) {
            ColorStateList titleCsl = resource.getColorStateList(skin.titleColor);
            if (titleCsl != null) {
                tvTitle.setTextColor(titleCsl);
            }
        }
        if (skin.timeColor != 0) {
            ColorStateList timeCsl = resource.getColorStateList(skin.timeColor);
            if (timeCsl != null) {
                tvTimeCurrent.setTextColor(timeCsl);
                tvTimeTotal.setTextColor(timeCsl);
            }
        }
        if (skin.seekDrawable != 0) {
            Drawable bg = resource.getDrawable(skin.seekDrawable);
            Rect bounds = skProgress.getProgressDrawable().getBounds();
            skProgress.setProgressDrawable(bg);
            skProgress.getProgressDrawable().setBounds(bounds);
            pbBottom.setProgressDrawable(resource.getDrawable(skin.seekDrawable));
        }
        if (skin.bottomControlBackground != 0) {
            llBottomControl.setBackgroundColor(resource.getColor(skin.bottomControlBackground));
        }
        this.enlargRecId = skin.enlargRecId;
        this.shrinkRecId = skin.shrinkRecId;
    }
}
