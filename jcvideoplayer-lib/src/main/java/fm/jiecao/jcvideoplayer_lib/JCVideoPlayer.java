package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
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

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * @see <a href="https://github.com/lipangit/jiecaovideoplayer">节操视频播放器 Github</a>
 * <br>
 * On 2015/11/30 11:59
 */
public class JCVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, View.OnTouchListener {

    //控件
    ImageView ivStart;
    ProgressBar pbLoading, pbBottom;
    ImageView ivFullScreen;
    SeekBar sbProgress;
    TextView tvTimeCurrent, tvTimeTotal;
    ResizeSurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    LinearLayout llBottomControl;
    TextView tvTitle;
    ImageView ivBack;
    ImageView ivThumb;
    RelativeLayout rlParent;
    LinearLayout llTitleContainer;
    ImageView ivCover;

    //属性
    private String url;
    private String thumb;
    private String title;
    private boolean ifFullScreen = false;
    public String uuid;//区别相同地址,包括全屏和不全屏，和都不全屏时的相同地址
    public boolean ifShowTitle = false;

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
    boolean isFromFullScreenBackHere = false;//如果是true表示这个正在不是全屏，并且全屏刚推出，总之进入过全屏
    boolean isClickFullscreen = false;

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
        sbProgress = (SeekBar) findViewById(R.id.progress);
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
        sbProgress.setOnSeekBarChangeListener(this);
        surfaceHolder.addCallback(this);
        surfaceView.setOnClickListener(this);
        llBottomControl.setOnClickListener(this);
        rlParent.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        sbProgress.setOnTouchListener(this);

    }

    /**
     * 配置要播放的内容
     *
     * @param url   视频地址
     * @param thumb 缩略图地址
     * @param title 标题
     */
    public void setUp(String url, String thumb, String title) {
        setUp(url, thumb, title, true);
    }

    /**
     * 配置要播放的内容
     *
     * @param url         视频地址
     * @param thumb       缩略图地址
     * @param title       标题
     * @param ifShowTitle 是否在非全屏下显示标题
     */
    public void setUp(String url, String thumb, String title, boolean ifShowTitle) {
        setIfShowTitle(ifShowTitle);
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) return;
        this.url = url;
        this.thumb = thumb;
        this.title = title;
        this.ifFullScreen = false;
        if (ifFullScreen) {
            ivFullScreen.setImageResource(R.drawable.shrink_video);
        } else {
            ivFullScreen.setImageResource(R.drawable.enlarge_video);
            ivBack.setVisibility(View.GONE);
        }
        tvTitle.setText(title);
        ivThumb.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(thumb, ivThumb, Utils.getDefaultDisplayImageOption());
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        setTitleVisibility(View.VISIBLE);
        if (uuid.equals(JCMediaPlayer.intance().uuid)) {
            JCMediaPlayer.intance().mediaPlayer.stop();
        }
    }

    /**
     * 只在全全屏中调用的方法
     *
     * @param url   视频地址
     * @param thumb 缩略图地址
     * @param title 标题
     */
    public void setUpForFullscreen(String url, String thumb, String title) {
        this.url = url;
        this.thumb = thumb;
        this.title = title;
        this.ifFullScreen = true;
        if (ifFullScreen) {
            ivFullScreen.setImageResource(R.drawable.shrink_video);
        } else {
            ivFullScreen.setImageResource(R.drawable.enlarge_video);
        }
        tvTitle.setText(title);
        ivThumb.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.VISIBLE);
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        setTitleVisibility(View.VISIBLE);
    }

    /**
     * 设置视频的状态
     *
     * @param state int型
     */
    public void setState(int state) {
        this.CURRENT_STATE = state;
        //全屏或取消全屏时继续原来的状态
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            ivStart.setVisibility(View.INVISIBLE);
            ivThumb.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
            ivCover.setVisibility(View.VISIBLE);
            setProgressAndTime(0, 0, 0);
            setProgressBuffered(0);
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            updateStartImage();
            ivStart.setVisibility(View.VISIBLE);
            llBottomControl.setVisibility(View.VISIBLE);
            pbBottom.setVisibility(View.INVISIBLE);
            setTitleVisibility(View.VISIBLE);
            ivThumb.setVisibility(View.INVISIBLE);
            ivCover.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.INVISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            updateStartImage();
            ivStart.setVisibility(View.VISIBLE);
            llBottomControl.setVisibility(View.VISIBLE);
            pbBottom.setVisibility(View.INVISIBLE);
            setTitleVisibility(View.VISIBLE);
            ivThumb.setVisibility(View.INVISIBLE);
            ivCover.setVisibility(View.INVISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
            if (uuid.equals(JCMediaPlayer.intance().uuid)) {
                JCMediaPlayer.intance().mediaPlayer.stop();
            }
            ivStart.setVisibility(View.VISIBLE);
            ivThumb.setVisibility(View.VISIBLE);
            llBottomControl.setVisibility(View.INVISIBLE);
            pbBottom.setVisibility(View.VISIBLE);
            ivCover.setVisibility(View.VISIBLE);
            setTitleVisibility(View.VISIBLE);
            updateStartImage();
            cancelDismissControlViewTimer();
            cancelProgressTimer();
        }
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
//            if (CURRENT_STATE != CURRENT_STATE_PREPAREING) {
            cancelProgressTimer();
            ivStart.setImageResource(R.drawable.click_video_play_selector);
            ivThumb.setVisibility(View.VISIBLE);
            ivStart.setVisibility(View.VISIBLE);
//                JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
            //TODO 这里要将背景置黑，
//            surfaceView.setBackgroundColor(R.color.black_a10_color);
            CURRENT_STATE = CURRENT_STATE_NORMAL;
            setKeepScreenOn(false);
            sendPointEvent(ifFullScreen ? VideoEvents.POINT_AUTO_COMPLETE_FULLSCREEN : VideoEvents.POINT_AUTO_COMPLETE);
        }
        if (!JCMediaPlayer.intance().uuid.equals(uuid)) {
            if (videoEvents.type == VideoEvents.VE_START) {
                if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                    setState(CURRENT_STATE_NORMAL);
                }
            }
            return;
        }
        if (videoEvents.type == VideoEvents.VE_PREPARED) {
            if (CURRENT_STATE != CURRENT_STATE_PREPAREING) return;
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
            JCMediaPlayer.intance().mediaPlayer.start();
            pbLoading.setVisibility(View.INVISIBLE);
            ivCover.setVisibility(View.INVISIBLE);
            llBottomControl.setVisibility(View.VISIBLE);
            pbBottom.setVisibility(View.INVISIBLE);
            CURRENT_STATE = CURRENT_STATE_PLAYING;
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
                JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
                stopToFullscreenOrQuitFullscreenShowDisplay();
                isFromFullScreenBackHere = false;
                startDismissControlViewTimer();
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_RESIZE) {
            int mVideoWidth = JCMediaPlayer.intance().currentVideoWidth;
            int mVideoHeight = JCMediaPlayer.intance().currentVideoHeight;
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
                surfaceView.requestLayout();
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE) {
            pbLoading.setVisibility(View.INVISIBLE);
            Log.i("JCVideoPlayer", "seek compile");
        }
    }

    //*********************所有控件显示隐藏的控制**********************************
    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (uuid.equals(JCMediaPlayer.intance().uuid)) {
                    if (getContext() != null && getContext() instanceof Activity) {
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissControlView();
                            }
                        });
                    }
                }
            }
        }, 2500);
    }

    //只是onClickToggleClear这个方法中逻辑的一部分
    private void dismissControlView() {
        llBottomControl.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.VISIBLE);
        setTitleVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
//        pbLoading.setVisibility(View.INVISIBLE);
    }

    private void cancelDismissControlViewTimer() {
        if (mDismissControlViewTimer != null) {
            mDismissControlViewTimer.cancel();
        }
    }

    private void onClickToggleClear() {
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                llBottomControl.setVisibility(View.INVISIBLE);
                pbBottom.setVisibility(View.VISIBLE);
                setTitleVisibility(View.INVISIBLE);
            } else {
                llBottomControl.setVisibility(View.VISIBLE);
                pbBottom.setVisibility(View.INVISIBLE);
                setTitleVisibility(View.VISIBLE);
            }
            ivStart.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                llBottomControl.setVisibility(View.INVISIBLE);
                pbBottom.setVisibility(View.VISIBLE);
                setTitleVisibility(View.INVISIBLE);
                ivStart.setVisibility(View.INVISIBLE);
            } else {
                updateStartImage();
                ivStart.setVisibility(View.VISIBLE);
                llBottomControl.setVisibility(View.VISIBLE);
                pbBottom.setVisibility(View.INVISIBLE);
                setTitleVisibility(View.VISIBLE);
            }
            pbLoading.setVisibility(View.INVISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                llBottomControl.setVisibility(View.INVISIBLE);
                pbBottom.setVisibility(View.VISIBLE);
                setTitleVisibility(View.INVISIBLE);
                ivStart.setVisibility(View.INVISIBLE);
            } else {
                updateStartImage();
                ivStart.setVisibility(View.VISIBLE);
                llBottomControl.setVisibility(View.VISIBLE);
                pbBottom.setVisibility(View.INVISIBLE);
                setTitleVisibility(View.VISIBLE);
            }
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }
    //*******************************************************

    //***************************加载进度的控制****************************
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
        Log.i("update buffer", "updatebuffer:: start");
    }

    private void cancelProgressTimer() {
        if (uuid.equals(JCMediaPlayer.intance().uuid)) {
            if (mUpdateProgressTimer != null) {
                mUpdateProgressTimer.cancel();
                Log.i("update buffer", "updatebuffer:: cancel");
            }
        }
    }
    //*******************************************************

    //**************************是否显示标题的控制*****************************
    public void setIfShowTitle(boolean ifShowTitle) {
        this.ifShowTitle = ifShowTitle;
    }

    private void setTitleVisibility(int visable) {
        if (ifShowTitle) {//全屏的时候要一直标题的
            llTitleContainer.setVisibility(visable);
        } else {
            if (ifFullScreen) {
                llTitleContainer.setVisibility(visable);
            } else {
                llTitleContainer.setVisibility(View.INVISIBLE);
            }
        }
    }
    //*******************************************************

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
            //点击缩略图或播放按钮。1.如果在normal模式准备视频，如果在播放模式就暂停，如果在暂停就播放，如果在prepare下不可能有这情况。
            if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
                JCMediaPlayer.intance().clearWidthAndHeight();
                //进入准备状态，开始缓冲视频
                CURRENT_STATE = CURRENT_STATE_PREPAREING;
                ivStart.setVisibility(View.INVISIBLE);
                ivThumb.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                ivCover.setVisibility(View.VISIBLE);
                setProgressAndTime(0, 0, 0);
                setProgressBuffered(0);
                JCMediaPlayer.intance().prepareToPlay(getContext(), url);
                JCMediaPlayer.intance().setUuid(uuid);

                VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_START);
                videoEvents.obj = uuid;
                EventBus.getDefault().post(videoEvents);
                surfaceView.requestLayout();
                setKeepScreenOn(true);

                sendPointEvent(i == R.id.start ? VideoEvents.POINT_START_ICON : VideoEvents.POINT_START_THUMB);
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                CURRENT_STATE = CURRENT_STATE_PAUSE;
                ivThumb.setVisibility(View.INVISIBLE);
                ivCover.setVisibility(View.INVISIBLE);
                JCMediaPlayer.intance().mediaPlayer.pause();
                updateStartImage();
                setKeepScreenOn(false);
                cancelDismissControlViewTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_STOP_FULLSCREEN : VideoEvents.POINT_STOP);
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                CURRENT_STATE = CURRENT_STATE_PLAYING;
                ivThumb.setVisibility(View.INVISIBLE);
                ivCover.setVisibility(View.INVISIBLE);
                JCMediaPlayer.intance().mediaPlayer.start();
                updateStartImage();
                setKeepScreenOn(true);
                startDismissControlViewTimer();
                sendPointEvent(ifFullScreen ? VideoEvents.POINT_RESUME_FULLSCREEN : VideoEvents.POINT_RESUME);
            }

        } else if (i == R.id.fullscreen) {
            //此时如果是loading，正在播放，暂停
            if (ifFullScreen) {
                //退出全屏
                quitFullScreen();
            } else {
                JCMediaPlayer.intance().mediaPlayer.pause();
                JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
                JCMediaPlayer.intance().backUpUuid();
                isClickFullscreen = true;
                FullScreenActivity.toActivity(getContext(), CURRENT_STATE, url, thumb, title);
                sendPointEvent(VideoEvents.POINT_ENTER_FULLSCREEN);
            }
            clickfullscreentime = System.currentTimeMillis();
        } else if (i == R.id.surfaceView || i == R.id.parentview) {
            onClickToggleClear();
            startDismissControlViewTimer();
            sendPointEvent(ifFullScreen ? VideoEvents.POINT_CLICK_BLANK_FULLSCREEN : VideoEvents.POINT_CLICK_BLANK);
        } else if (i == R.id.bottom_control) {
//            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        } else if (i == R.id.back) {
            quitFullScreen();
        }
    }


    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            ivStart.setImageResource(R.drawable.click_video_pause_selector);
        } else {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
        }
    }

    //************************进度条的控制*******************************
    private void setProgressBuffered(int secProgress) {
        if (secProgress >= 0) {
            sbProgress.setSecondaryProgress(secProgress);
            pbBottom.setSecondaryProgress(secProgress);
        }
    }

    private void setProgressAndTimeFromTimer() {
        int position = JCMediaPlayer.intance().mediaPlayer.getCurrentPosition();
        int duration = JCMediaPlayer.intance().mediaPlayer.getDuration();
        int progress = position * 100 / duration;
        setProgressAndTime(progress, position, duration);
    }

    private void setProgressAndTime(int progress, int currentTime, int totalTime) {
        if (!touchingProgressBar) {
            sbProgress.setProgress(progress);
            pbBottom.setProgress(progress);
        }
        tvTimeCurrent.setText(Utils.stringForTime(currentTime));
        tvTimeTotal.setText(Utils.stringForTime(totalTime));
    }
    //*******************************************************

    public void release() {
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) return;
        //将状态设为普通
        setState(CURRENT_STATE_NORMAL);
        //回收surfaceview，或画黑板
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress * JCMediaPlayer.intance().mediaPlayer.getDuration() / 100;
            JCMediaPlayer.intance().mediaPlayer.seekTo(time);
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
        if (uuid.equals(JCMediaPlayer.intance().uuid)) {
            JCMediaPlayer.intance().mediaPlayer.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    //************************全屏的控制*******************************
    public void quitFullScreen() {
        FullScreenActivity.manualQuit = true;
        clickfullscreentime = System.currentTimeMillis();
        JCMediaPlayer.intance().mediaPlayer.pause();
        JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
        JCMediaPlayer.intance().revertUuid();
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN);
        videoEvents.obj = CURRENT_STATE;
        EventBus.getDefault().post(videoEvents);
        sendPointEvent(VideoEvents.POINT_QUIT_FULLSCREEN);
    }

    private void stopToFullscreenOrQuitFullscreenShowDisplay() {
        if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            JCMediaPlayer.intance().mediaPlayer.start();
            CURRENT_STATE = CURRENT_STATE_PLAYING;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JCMediaPlayer.intance().mediaPlayer.pause();
                            CURRENT_STATE = CURRENT_STATE_PAUSE;
                        }
                    });
                }
            }).start();
            surfaceView.requestLayout();
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            JCMediaPlayer.intance().mediaPlayer.start();
        }
    }
    //*******************************************************

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //TODO MediaPlayer set holder,MediaPlayer prepareToPlay
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_CREATED));
        if (ifFullScreen) {
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
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
     * 停止所有音频的播放
     */
    public static void releaseAllVideo() {
        JCMediaPlayer.intance().mediaPlayer.stop();
        JCMediaPlayer.intance().setUuid("");
        JCMediaPlayer.intance().setUuid("");
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
    }

    /**
     * 有特殊需要的客户端
     *
     * @param onClickListener 开始按钮点击的回调函数
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
}
