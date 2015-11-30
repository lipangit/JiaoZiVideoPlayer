package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import de.greenrobot.event.EventBus;


/**
 * Created by Nathen
 * On 2015/11/30 11:59
 */
public class VideoController extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    ImageView ivStart;
    ImageView ivFullScreen;
    SeekBar sbProgress;
    TextView tvTimeCurrent, tvTimeTotal;

    public VideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.video_control_view, this);
        ivStart = (ImageView) findViewById(R.id.start);
        ivFullScreen = (ImageView) findViewById(R.id.fullscreen);
        sbProgress = (SeekBar) findViewById(R.id.progress);
        tvTimeCurrent = (TextView) findViewById(R.id.current);
        tvTimeCurrent = (TextView) findViewById(R.id.current);
        tvTimeTotal = (TextView) findViewById(R.id.total);
        ivStart.setOnClickListener(this);
        ivFullScreen.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.start) {

        } else if (i == R.id.fullscreen) {

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

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_PROGRESS) {
            //TODO 正在播放中修改时间显示和进度条
        }
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

}
