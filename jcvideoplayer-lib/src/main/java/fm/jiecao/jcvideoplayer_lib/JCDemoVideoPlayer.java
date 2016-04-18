package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Manage UI
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public class JCDemoVideoPlayer extends JCAbstractVideoPlayer {

    public JCDemoVideoPlayer(Context context) {
        super(context);
    }

    public JCDemoVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        //init my video

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_demo_layout_base;
    }

    @Override
    public void setUp(String url) {
        super.setUp(url);
        if (IF_CURRENT_IS_FULLSCREEN) {
            ivFullScreen.setImageResource(R.drawable.shrink_video);
        } else {
            ivFullScreen.setImageResource(R.drawable.enlarge_video);
        }
    }

    @Override
    protected void onPrepareing() {
        super.onPrepareing();
        ivStart.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onPlay() {
        super.onPlay();
        ivStart.setVisibility(View.VISIBLE);
        updateStartImage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStartImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStartImage();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        updateStartImage();
    }

    @Override
    public void setUpUI() {
        switch (CURRENT_STATE) {
            case CURRENT_STATE_NORMAL:
                break;
            case CURRENT_STATE_PREPAREING:
                ivStart.setVisibility(View.INVISIBLE);
                break;
            case CURRENT_STATE_PLAY:
                ivStart.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PAUSE:
                break;
        }
        updateStartImage();
    }

    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAY) {
            ivStart.setImageResource(R.drawable.click_video_pause_selector);
        } else if (CURRENT_STATE == CURRENT_STATE_ERROR) {
            ivStart.setImageResource(R.drawable.click_video_error_selector);
        } else {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
        }
    }
}
