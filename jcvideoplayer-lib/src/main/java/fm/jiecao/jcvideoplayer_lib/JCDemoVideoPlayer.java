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
        //this should before super.onClick()
        int i = v.getId();
        if (i == surfaceId || i == R.id.parentview) {
            if (CURRENT_STATE == CURRENT_STATE_ERROR) {
                ivStart.performClick();
            } else {
                onClickBlank();
            }
        } else {

        }

        super.onClick(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_base_demo_layout;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //to prepareing state
        llTopContainer.setVisibility(View.INVISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onPlay() {
        super.onPlay();

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

    protected void onClickBlank() {
        if (llBottomControl.getVisibility() == View.VISIBLE) {
            llTopContainer.setVisibility(View.INVISIBLE);
            llBottomControl.setVisibility(View.INVISIBLE);
            ivStart.setVisibility(View.INVISIBLE);
        } else {
            llTopContainer.setVisibility(View.VISIBLE);
            llBottomControl.setVisibility(View.VISIBLE);
            ivStart.setVisibility(View.VISIBLE);
            updateStartImage();
        }
    }

    private void updateStartImage() {
        if (CURRENT_STATE == CURRENT_STATE_PLAYING) {
            ivStart.setImageResource(R.drawable.click_video_pause_selector);
        } else if (CURRENT_STATE == CURRENT_STATE_ERROR) {
            ivStart.setImageResource(R.drawable.click_video_error_selector);
        } else {
            ivStart.setImageResource(R.drawable.click_video_play_selector);
        }
    }
}
