package fm.jiecao.jcvideoplayer_lib;

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

    //这个组件的三个属性
    public String url;
    public String thumb;
    public String title;
    public boolean ifFullScreen = false;

    /**
     * 为了保证全屏和退出全屏之后的状态和之前一样
     */
    public int CURRENT_STATE = -1;//-1相当于null
    public static final int CURRENT_STSTE_PREPAREING = 0;
    public static final int CURRENT_STATE_STOP = 1;
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
        surfaceHolder = surfaceView.getHolder();
        ivStart.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {
            //TODO 准备视频
            ivStart.setVisibility(View.INVISIBLE);
            pbLoading.setVisibility(View.VISIBLE);
            JCMediaPlayer.intance().prepareToPlay(url);
        } else if (i == R.id.fullscreen) {
            if (ifFullScreen) {
                EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN));
            } else {
                FullScreenActivity.toActivity(getContext(), CURRENT_STATE, url, title);
            }
        } else if (i == R.id.surfaceView) {
            if (llBottomControl.getVisibility() == View.VISIBLE) {
                //显示
                llBottomControl.setVisibility(View.INVISIBLE);
                ivStart.setVisibility(View.INVISIBLE);
                tvTitle.setVisibility(View.INVISIBLE);
            } else {
                setStartImage();
                llBottomControl.setVisibility(View.VISIBLE);
                ivStart.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            ivStart.setImageResource(R.drawable.click_video_pause_selector);
        } else {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
        }
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_PREPARED) {
            JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
            JCMediaPlayer.intance().mediaPlayer.start();
            pbLoading.setVisibility(View.INVISIBLE);
        } else if (videoEvents.type == VideoEvents.VE_PROGRESSING) {
            //TODO 正在播放中修改时间显示和进度条

        }
    }

    public void goOn() {
        //继续播放，把引擎的内容直接显示在这里
        JCMediaPlayer.intance().mediaPlayer.setDisplay(surfaceHolder);
        ivStart.setVisibility(View.INVISIBLE);
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
}
