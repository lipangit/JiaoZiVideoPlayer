package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Manage UI
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public class JCVideoPlayerSimple extends JCVideoPlayer {

    public JCVideoPlayerSimple(Context context) {
        super(context);
    }

    public JCVideoPlayerSimple(Context context, AttributeSet attrs) {
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
        return R.layout.jc_layout_base;
    }

    @Override
    public void setUp(String url, Object... objects) {
        super.setUp(url, objects);
        if (IF_CURRENT_IS_FULLSCREEN) {
            ivFullScreen.setImageResource(R.drawable.jc_shrink);
        } else {
            ivFullScreen.setImageResource(R.drawable.jc_enlarge);
        }
    }

    @Override
    public void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (CURRENT_STATE) {
            case CURRENT_STATE_NORMAL:
                ivStart.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PREPAREING:
                ivStart.setVisibility(View.INVISIBLE);
                break;
            case CURRENT_STATE_PLAYING:
                ivStart.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PAUSE:
                break;
            case CURRENT_STATE_ERROR:
                break;
        }
        updateStartImage();
    }

    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            ivStart.setImageResource(R.drawable.jc_click_pause_selector);
        } else if (CURRENT_STATE == CURRENT_STATE_ERROR) {
            ivStart.setImageResource(R.drawable.jc_click_error_selector);
        } else {
            ivStart.setImageResource(R.drawable.jc_click_play_selector);
        }
    }
}
