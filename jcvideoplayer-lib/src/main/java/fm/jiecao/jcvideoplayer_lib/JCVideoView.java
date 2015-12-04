package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Formatter;
import java.util.Locale;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Nathen
 * On 2015/11/30 11:59
 */
public class JCVideoView extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback {

    ImageView ivStart;
    ProgressBar pbLoading;
    ImageView ivFullScreen;
    SeekBar sbProgress;
    TextView tvTimeCurrent, tvTimeTotal;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    LinearLayout llBottomControl;
    TextView tvTitle;
    ImageView ivThumb;

    //这个组件的四个属性
    public String url;
    public String thumb;
    public String title;
    public boolean ifFullScreen = false;
    public String uuid;//区别相同地址,包括全屏和不全屏，和都不全屏时的相同地址
    /**
     * 为了保证全屏和退出全屏之后的状态和之前一样
     */
    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STATE_PREPAREING = 0;
    public static final int CURRENT_STATE_PAUSE = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_OVER = 3;//这个状态可能不需要，播放完毕就进入normal状态
    public static final int CURRENT_STATE_NORMAL = 4;//刚初始化之后


    public JCVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        uuid = UUID.randomUUID().toString();
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.video_control_view, this);
        ivStart = (ImageView) findViewById(R.id.start);
        pbLoading = (ProgressBar) findViewById(R.id.loading);
        ivFullScreen = (ImageView) findViewById(R.id.fullscreen);
        sbProgress = (SeekBar) findViewById(R.id.progress);
        tvTimeCurrent = (TextView) findViewById(R.id.current);
        tvTimeCurrent = (TextView) findViewById(R.id.current);
        tvTimeTotal = (TextView) findViewById(R.id.total);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        llBottomControl = (LinearLayout) findViewById(R.id.bottom_control);
        tvTitle = (TextView) findViewById(R.id.title);
        ivThumb = (ImageView) findViewById(R.id.thumb);
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
    }

    /**
     * 设置属性
     */
    public void setUp(String url, String thumb, String title, boolean ifFullScreen) {
        this.url = url;
        this.thumb = thumb;
        this.title = title;
        this.ifFullScreen = ifFullScreen;
        if (ifFullScreen) {
            ivFullScreen.setImageResource(R.drawable.biz_video_shrink);
        } else {
            ivFullScreen.setImageResource(R.drawable.biz_video_expand);
        }
        tvTitle.setText(title);
        ImageLoader.getInstance().displayImage(thumb, ivThumb);
        CURRENT_STATE = CURRENT_STATE_NORMAL;
    }

    public void setState(int state) {
        this.CURRENT_STATE = state;
        //全屏或取消全屏时继续原来的状态
        if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
            ivStart.setVisibility(View.INVISIBLE);
            ivThumb.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
            setProgressAndTime(0, 0, 0, 0);
        } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            updateStartImage();
            ivStart.setVisibility(View.VISIBLE);
            llBottomControl.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            ivThumb.setVisibility(View.INVISIBLE);
        } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
            updateStartImage();
            ivStart.setVisibility(View.VISIBLE);
            llBottomControl.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            ivThumb.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 目前认为详细的判断和重复的设置是有相当必要的,也可以包装成方法
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start || i == R.id.thumb) {
            //点击缩略图或播放按钮。1.如果在normal模式准备视频，如果在播放模式就暂停，如果在暂停就播放，如果在prepare下不可能有这情况。
            if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
                //进入准备状态，开始缓冲视频
                CURRENT_STATE = CURRENT_STATE_PREPAREING;
                ivStart.setVisibility(View.INVISIBLE);
                ivThumb.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
                setProgressAndTime(0, 0, 0, 0);
                JCMediaPlayer.intance().prepareToPlay(getContext(), url);
                JCMediaPlayer.intance().setUuid(uuid);
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                CURRENT_STATE = CURRENT_STATE_PAUSE;
                ivThumb.setVisibility(View.INVISIBLE);
                JCMediaPlayer.intance().mediaPlayer.pause();
                updateStartImage();
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                CURRENT_STATE = CURRENT_STATE_PLAYING;
                ivThumb.setVisibility(View.INVISIBLE);
                JCMediaPlayer.intance().mediaPlayer.start();
                updateStartImage();
            }

        } else if (i == R.id.fullscreen) {
            //此时如果是loading，正在播放，暂停
            JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
            if (ifFullScreen) {
                //把uuid指回到
                JCMediaPlayer.intance().backUpUuid();
                VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN);
                videoEvents.obj = CURRENT_STATE;
                EventBus.getDefault().post(videoEvents);
            } else {
                isClickFullscreen = true;
                FullScreenActivity.toActivity(getContext(), CURRENT_STATE, url, thumb, title);
            }
        } else if (i == R.id.surfaceView) {
            if (CURRENT_STATE == CURRENT_STATE_PREPAREING) {
                if (llBottomControl.getVisibility() == View.VISIBLE) {
                    llBottomControl.setVisibility(View.INVISIBLE);
                    tvTitle.setVisibility(View.INVISIBLE);
                } else {
                    llBottomControl.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                }
                ivStart.setVisibility(View.INVISIBLE);
                pbLoading.setVisibility(View.VISIBLE);
            } else if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
                if (llBottomControl.getVisibility() == View.VISIBLE) {
                    llBottomControl.setVisibility(View.INVISIBLE);
                    tvTitle.setVisibility(View.INVISIBLE);
                    ivStart.setVisibility(View.INVISIBLE);
                } else {
                    updateStartImage();
                    ivStart.setVisibility(View.VISIBLE);
                    llBottomControl.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                }
                pbLoading.setVisibility(View.INVISIBLE);
            } else if (CURRENT_STATE == CURRENT_STATE_PAUSE) {
                if (llBottomControl.getVisibility() == View.VISIBLE) {
                    llBottomControl.setVisibility(View.INVISIBLE);
                    tvTitle.setVisibility(View.INVISIBLE);
                    ivStart.setVisibility(View.INVISIBLE);
                } else {
                    updateStartImage();
                    ivStart.setVisibility(View.VISIBLE);
                    llBottomControl.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                }
                pbLoading.setVisibility(View.INVISIBLE);
            }
        } else if (i == R.id.bottom_control) {
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        }
    }

    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            ivStart.setImageResource(R.drawable.click_video_pause_selector);
        } else {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
        }
    }

    //设置进度条和进度时间
    private void setProgressAndTimeFromMediaPlayer(int secProgress) {
        final int position = JCMediaPlayer.intance().mediaPlayer.getCurrentPosition();
        final int duration = JCMediaPlayer.intance().mediaPlayer.getDuration();
        System.out.println("onBufferingUpdate " + secProgress + " " + position + " " + duration + " " + uuid + " " + JCMediaPlayer.intance().uuid);
        int progress = position * 100 / duration;
        setProgressAndTime(progress, secProgress, position, duration);
    }

    private void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        sbProgress.setProgress(progress);
        sbProgress.setSecondaryProgress(secProgress);
        tvTimeCurrent.setText(stringForTime(currentTime));
        tvTimeTotal.setText(stringForTime(totalTime));
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
            if (CURRENT_STATE != CURRENT_STATE_PREPAREING) {
                ivStart.setImageResource(R.drawable.click_video_play_selector);
                ivThumb.setVisibility(View.VISIBLE);
                ivStart.setVisibility(View.VISIBLE);
//                JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
                //TODO 这里要将背景置黑，
//            surfaceView.setBackgroundColor(R.color.black_a10_color);
                CURRENT_STATE = CURRENT_STATE_NORMAL;
            }
        }
        if (!JCMediaPlayer.intance().uuid.equals(uuid)) {
            System.out.println("不是给我发的");
            return;
        }
        if (videoEvents.type == VideoEvents.VE_PREPARED) {
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
            JCMediaPlayer.intance().mediaPlayer.start();
            pbLoading.setVisibility(View.INVISIBLE);
            CURRENT_STATE = CURRENT_STATE_PLAYING;
        } else if (videoEvents.type == VideoEvents.VE_PROGRESSING) {
            //TODO 正在播放中修改时间显示和进度条

        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_BUFFERUPDATE) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL || CURRENT_STATE != CURRENT_STATE_PREPAREING) {
                int percent = Integer.valueOf(videoEvents.obj.toString());
                setProgressAndTimeFromMediaPlayer(percent);
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
                //200ms播放视频,,这里奇怪了一阵子。
//                delaySetdisplay();
                JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
                isFromFullScreenBackHere = false;
            }
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_RESIZE) {

        }
    }

    public void delaySetdisplay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "显示图像 " + CURRENT_STATE + " " + ifFullScreen, Toast.LENGTH_SHORT).show();
                        JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
                    }
                });
            }
        }).start();
    }

    boolean isFromFullScreenBackHere = false;//如果是true表示这个正在不是全屏，并且全屏刚推出，总之进入过全屏
    boolean isClickFullscreen = false;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress * JCMediaPlayer.intance().mediaPlayer.getDuration() / 100;
            JCMediaPlayer.intance().mediaPlayer.seekTo(time);
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
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //TODO MediaPlayer set holder,MediaPlayer prepareToPlay
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_CREATED));

        if (ifFullScreen) {
            Toast.makeText(getContext(), "进入全屏，显示图像" + CURRENT_STATE + " " + ifFullScreen, Toast.LENGTH_SHORT).show();
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
