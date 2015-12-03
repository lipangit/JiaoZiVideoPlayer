package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;

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

    //这个组件的三个属性
    public String url;
    public String thumb;
    public String title;
    public boolean ifFullScreen = false;

    /**
     * 为了保证全屏和退出全屏之后的状态和之前一样
     */
    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STATE_PREPAREING = 0;
    public static final int CURRENT_STATE_PAUSE = 1;
    public static final int CURRENT_STATE_PLAYING = 2;
    public static final int CURRENT_STATE_OVER = 3;
    public static final int CURRENT_STATE_NORMAL = 4;//刚初始化之后


    public JCVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        ImageLoader.getInstance().displayImage(thumb, ivThumb, getDefaultDisplayImageOption());
        CURRENT_STATE = CURRENT_STATE_NORMAL;
    }

    public void setState(int state) {
        this.CURRENT_STATE = state;
        //全屏或取消全屏时继续原来的状态

    }

    Timer timer = new Timer();

    /**
     * 个人认为详细的判断和重复的设置是有相当必要的
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
                JCMediaPlayer.intance().prepareToPlay(url);
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
            if (ifFullScreen) {
                EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN));
            } else {
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
        System.out.println("onBufferingUpdate " + secProgress + " " + position + " " + duration);
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
        if (videoEvents.type == VideoEvents.VE_PREPARED) {
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
            JCMediaPlayer.intance().mediaPlayer.start();
            pbLoading.setVisibility(View.INVISIBLE);
            CURRENT_STATE = CURRENT_STATE_PLAYING;
        } else if (videoEvents.type == VideoEvents.VE_PROGRESSING) {
            //TODO 正在播放中修改时间显示和进度条

        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
            ivThumb.setVisibility(View.VISIBLE);
            ivStart.setVisibility(View.VISIBLE);
            JCMediaPlayer.intance().mediaPlayer.setDisplay(null);
            //TODO 这里要将背景置黑，
//            surfaceView.setBackgroundColor(R.color.black_a10_color);
            CURRENT_STATE = CURRENT_STATE_NORMAL;
        } else if (videoEvents.type == VideoEvents.VE_MEDIAPLAYER_BUFFERUPDATE) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                int percent = Integer.valueOf(videoEvents.obj.toString());
                setProgressAndTimeFromMediaPlayer(percent);
            }
        }
    }

    public void goOn() {
        //继续播放，把引擎的内容直接显示在这里
        JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        ivStart.setVisibility(View.INVISIBLE);
    }

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
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public static DisplayImageOptions getDefaultDisplayImageOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new FadeInBitmapDisplayer(1000)) // 设置图片渐显的时间
//                .delayBeforeLoading(300)  // 下载前的延迟时间
                .build();
        return options;
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
