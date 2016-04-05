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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>节操视频播放器，库的外面所有使用的接口也在这里</p>
 * <p>Jiecao video player，all outside the library interface is here</p>
 *
 * @see <a href="https://github.com/lipangit/jiecaovideoplayer">JiecaoVideoplayer Github</a>
 * Created by Nathen
 * On 2015/11/30 11:59
 */
public class JCVideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, View.OnTouchListener, JCMediaManager.JCMediaPlayerListener {

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

    private String url;
    private String title;
    private boolean ifFullScreen = false;
    private boolean ifShowTitle = false;
    private boolean ifMp3 = false;

    private int enlargRecId = 0;
    private int shrinkRecId = 0;

    private int surfaceId;

    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STATE_PREPAREING = 0;
    public static final int CURRENT_STATE_PAUSE = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_OVER = 3;
    public static final int CURRENT_STATE_NORMAL = 4;
    private OnTouchListener mSeekbarOnTouchListener;
    private static Timer mDismissControlViewTimer;
    private static Timer mUpdateProgressTimer;
    private static long clickfullscreentime;
    private static final int FULL_SCREEN_NORMAL_DELAY = 5000;

    private boolean touchingProgressBar = false;
    public static boolean isClickFullscreen = false;//一会调试一下，看是不是需要这个
    public boolean isFullscreenFromNormal = false;

    private static ImageView.ScaleType speScalType = null;

    private static JCBuriedPoint JC_BURIED_POINT;

    public JCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        llBottomControl = (LinearLayout) findViewById(R.id.bottom_control);
        tvTitle = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.back);
        ivThumb = (ImageView) findViewById(R.id.thumb);
        rlParent = (RelativeLayout) findViewById(R.id.parentview);
        llTitleContainer = (LinearLayout) findViewById(R.id.title_container);
        ivCover = (ImageView) findViewById(R.id.cover);

        ivStart.setOnClickListener(this);
        ivThumb.setOnClickListener(this);
        ivFullScreen.setOnClickListener(this);
        skProgress.setOnSeekBarChangeListener(this);
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
        this.ifShowTitle = ifShowTitle;
        if ((System.currentTimeMillis() - clickfullscreentime) < FULL_SCREEN_NORMAL_DELAY) return;
        this.url = url;
        this.title = title;
        this.ifFullScreen = false;
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        if (ifFullScreen) {
            ivFullScreen.setImageResource(enlargRecId == 0 ? R.drawable.shrink_video : enlargRecId);
        } else {
            ivFullScreen.setImageResource(shrinkRecId == 0 ? R.drawable.enlarge_video : shrinkRecId);
            ivBack.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(url) && url.contains(".mp3")) {
            ifMp3 = true;
            ivFullScreen.setVisibility(View.GONE);
        }
        tvTitle.setText(title);

        changeUiToNormal();

        if (JCMediaManager.intance().listener == this) {
            JCMediaManager.intance().mediaPlayer.stop();
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
        this.url = url;
        this.title = title;
        ifShowTitle = true;
        ifFullScreen = true;
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        if (ifFullScreen) {
            ivFullScreen.setImageResource(shrinkRecId == 0 ? R.drawable.shrink_video : shrinkRecId);
        } else {
            ivFullScreen.setImageResource(enlargRecId == 0 ? R.drawable.enlarge_video : enlargRecId);
        }
        tvTitle.setText(title);
        if (!TextUtils.isEmpty(url) && url.contains(".mp3")) {
            ifMp3 = true;
        }
        addSurfaceView();

        if (JCMediaManager.intance().listener != null) {
            JCMediaManager.intance().listener.onCompletion();
        }
        JCMediaManager.intance().listener = this;

        changeUiToNormal();
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
            changeUiToNormal();
            cancelDismissControlViewTimer();
            cancelProgressTimer();
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
                addSurfaceView();

                if (JCMediaManager.intance().listener != null) {
                    JCMediaManager.intance().listener.onCompletion();
                }
                JCMediaManager.intance().listener = this;

                JCMediaManager.intance().clearWidthAndHeight();
                CURRENT_STATE = CURRENT_STATE_PREPAREING;
                changeUiToShowUiPrepareing();
                llBottomControl.setVisibility(View.INVISIBLE);
                llTitleContainer.setVisibility(View.INVISIBLE);
                setProgressAndTime(0, 0, 0);
                setProgressBuffered(0);
                JCMediaManager.intance().prepareToPlay(getContext(), url);
                Log.i("JCVideoPlayer", "play video");

                surfaceView.requestLayout();
                setKeepScreenOn(true);

                if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
                    if (i == R.id.start) {
                        JC_BURIED_POINT.POINT_START_ICON(title, url);
                    } else {
                        JC_BURIED_POINT.POINT_START_THUMB(title, url);
                    }
                }
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                CURRENT_STATE = CURRENT_STATE_PAUSE;

                changeUiToShowUiPause();

                JCMediaManager.intance().mediaPlayer.pause();
                Log.i("JCVideoPlayer", "pause video");

                setKeepScreenOn(false);
                cancelDismissControlViewTimer();

                if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
                    if (ifFullScreen) {
                        JC_BURIED_POINT.POINT_STOP_FULLSCREEN(title, url);
                    } else {
                        JC_BURIED_POINT.POINT_STOP(title, url);
                    }
                }
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                CURRENT_STATE = CURRENT_STATE_PLAYING;

                changeUiToShowUiPlaying();
                JCMediaManager.intance().mediaPlayer.start();
                Log.i("JCVideoPlayer", "go on video");

                setKeepScreenOn(true);
                startDismissControlViewTimer();

                if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
                    if (ifFullScreen) {
                        JC_BURIED_POINT.POINT_RESUME_FULLSCREEN(title, url);
                    } else {
                        JC_BURIED_POINT.POINT_RESUME(title, url);
                    }
                }
            }

        } else if (i == R.id.fullscreen) {
            if (ifFullScreen) {
                isClickFullscreen = false;
                quitFullScreen();
            } else {
                JCMediaManager.intance().mediaPlayer.pause();
                JCMediaManager.intance().mediaPlayer.setDisplay(null);
                JCMediaManager.intance().lastListener = this;
                JCMediaManager.intance().listener = null;
                isClickFullscreen = true;
                JCFullScreenActivity.toActivityFromNormal(getContext(), CURRENT_STATE, url, title);

                if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
                    JC_BURIED_POINT.POINT_ENTER_FULLSCREEN(title, url);
                }
            }
            clickfullscreentime = System.currentTimeMillis();
        } else if (i == surfaceId || i == R.id.parentview) {
            onClickUiToggle();
            startDismissControlViewTimer();

            if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
                if (ifFullScreen) {
                    JC_BURIED_POINT.POINT_CLICK_BLANK_FULLSCREEN(title, url);
                } else {
                    JC_BURIED_POINT.POINT_CLICK_BLANK(title, url);
                }
            }
        } else if (i == R.id.bottom_control) {
            //JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        } else if (i == R.id.back) {
            quitFullScreen();
        }
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

    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        mDismissControlViewTimer = new Timer();
        mDismissControlViewTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                                llBottomControl.setVisibility(View.INVISIBLE);
                                pbBottom.setVisibility(View.VISIBLE);
                                setTitleVisibility(View.INVISIBLE);
                                ivStart.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
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
        ivCover.setVisibility(View.VISIBLE);
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
                            if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {
                                setProgressAndTimeFromTimer();
                            }
                        }
                    });
                }
            }
        }, 0, 300);
    }

    private void cancelProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel();
        }
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

    //这两段根本就没过来，列表重置靠的setUp复用
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
////        cancelDismissControlViewTimer();
////        if (uuid.equals(JCMediaManager.intance().uuid)) {
////        if (JCMediaManager.intance().listener == this) {
//        JCMediaManager.intance().mediaPlayer.stop();
////        }
////        }
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//    }

    public void quitFullScreen() {
        JCFullScreenActivity.manualQuit = true;
        clickfullscreentime = System.currentTimeMillis();
        JCMediaManager.intance().mediaPlayer.pause();
        JCMediaManager.intance().mediaPlayer.setDisplay(null);
        //这个view释放了，
        JCMediaManager.intance().listener = JCMediaManager.intance().lastListener;
        JCMediaManager.intance().lastState = CURRENT_STATE;
        JCMediaManager.intance().listener.onBackFullscreen();

        if (getContext() instanceof JCFullScreenActivity) {
            ((JCFullScreenActivity) getContext()).finish();
        }

        if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
            JC_BURIED_POINT.POINT_QUIT_FULLSCREEN(title, url);
        }
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
        if (ifFullScreen) {
            JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
            stopToFullscreenOrQuitFullscreenShowDisplay();
        }
        if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
            startDismissControlViewTimer();
            startProgressTimer();
        }

        if (JCMediaManager.intance().lastListener == this) {
            JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
            stopToFullscreenOrQuitFullscreenShowDisplay();
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
            if (JCMediaManager.intance().listener != null) {
                JCMediaManager.intance().listener.onCompletion();
            }
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

                if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
                    if (ifFullScreen) {
                        JC_BURIED_POINT.POINT_CLICK_SEEKBAR_FULLSCREEN(title, url);
                    } else {
                        JC_BURIED_POINT.POINT_CLICK_SEEKBAR(title, url);
                    }
                }
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
     * In demo is ok, but in other project This will class not access exception,How to solve the problem
     */
    @Deprecated
    public static void toFullscreenActivity(Context context, String url, String title) {
        JCFullScreenActivity.toActivity(context, url, title);
    }

    @Override
    public void onPrepared() {
        if (CURRENT_STATE != CURRENT_STATE_PREPAREING) return;
        JCMediaManager.intance().mediaPlayer.setDisplay(surfaceHolder);
        JCMediaManager.intance().mediaPlayer.start();
        CURRENT_STATE = CURRENT_STATE_PLAYING;

        changeUiToShowUiPlaying();
        ivStart.setVisibility(View.INVISIBLE);

        startDismissControlViewTimer();
        startProgressTimer();
    }

    @Override
    public void onCompletion() {
        CURRENT_STATE = CURRENT_STATE_NORMAL;
        cancelProgressTimer();
        cancelDismissControlViewTimer();
        setKeepScreenOn(false);
        changeUiToNormal();

        if (JC_BURIED_POINT != null && JCMediaManager.intance().listener == this) {
            if (ifFullScreen) {
                JC_BURIED_POINT.POINT_AUTO_COMPLETE_FULLSCREEN(title, url);
            } else {
                JC_BURIED_POINT.POINT_AUTO_COMPLETE(title, url);
            }
        }

        if (getContext() instanceof JCFullScreenActivity) {
            ((JCFullScreenActivity) getContext()).finish();
        }
        if (isFullscreenFromNormal) {//如果在进入全屏后播放完就初始化自己非全屏的控件
            isFullscreenFromNormal = false;
            JCMediaManager.intance().lastListener.onCompletion();
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {
            setProgressBuffered(percent);
        }
    }

    @Override
    public void onSeekComplete() {
        pbLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onError() {
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
        CURRENT_STATE = JCMediaManager.intance().lastState;
        addSurfaceView();
        setState(CURRENT_STATE);
    }

    public static void setJcBuriedPoint(JCBuriedPoint jcBuriedPoint) {
        JC_BURIED_POINT = jcBuriedPoint;
    }
}
