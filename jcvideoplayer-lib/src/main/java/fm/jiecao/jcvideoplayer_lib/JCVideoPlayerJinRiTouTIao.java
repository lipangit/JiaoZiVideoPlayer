package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nathen
 * On 2016/04/18 16:15
 */
public class JCVideoPlayerJinRiTouTIao extends JCAbstractVideoPlayer {

    ImageView ivBack;
    ProgressBar pbBottom, pbLoading;
    TextView tvTitle;
    public ImageView ivThumb;
    ImageView ivCover;

    private static Timer mDismissControlViewTimer;

    public JCVideoPlayerJinRiTouTIao(Context context) {
        super(context);
    }

    public JCVideoPlayerJinRiTouTIao(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        pbBottom = (ProgressBar) findViewById(R.id.bottom_progressbar);
        tvTitle = (TextView) findViewById(R.id.title);
        ivBack = (ImageView) findViewById(R.id.back);
        ivThumb = (ImageView) findViewById(R.id.thumb);
        ivCover = (ImageView) findViewById(R.id.cover);
        pbLoading = (ProgressBar) findViewById(R.id.loading);

        ivThumb.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }

    public void setUp(String url, String title) {
        setUp(url);
        tvTitle.setText(title);
        if (IF_CURRENT_IS_FULLSCREEN) {
            ivFullScreen.setImageResource(R.drawable.shrink_video);
        } else {
            ivFullScreen.setImageResource(R.drawable.enlarge_video);
            ivBack.setVisibility(View.GONE);
        }
        changeUiToNormal();
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_demo_layout_jinritoutiao;
    }

    @Override
    public void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (CURRENT_STATE) {
            case CURRENT_STATE_NORMAL:
                changeUiToNormal();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PREPAREING:
                changeUiToShowUiPrepareing();
                break;
            case CURRENT_STATE_PLAYING:
                changeUiToShowUiPlaying();
                break;
            case CURRENT_STATE_PAUSE:
                changeUiToShowUiPause();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.thumb) {
            if (CURRENT_STATE != CURRENT_STATE_NORMAL) {
                onClickUiToggle();
                return;
            } else if (CURRENT_STATE == CURRENT_STATE_NORMAL) {
                ivStart.performClick();
            }
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
        llTopContainer.setVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        ivThumb.setVisibility(View.VISIBLE);
        ivCover.setVisibility(View.VISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToShowUiPrepareing() {
        llTopContainer.setVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        ivThumb.setVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.VISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
    }

    private void changeUiToClearUiPrepareing() {
//        changeUiToClearUi();
        llTopContainer.setVisibility(View.INVISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        ivThumb.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
//        pbLoading.setVisibility(View.VISIBLE);
        ivCover.setVisibility(View.VISIBLE);
    }

    private void changeUiToShowUiPlaying() {
        llTopContainer.setVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        ivThumb.setVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToClearUiPlaying() {
        changeUiToClearUi();
        pbBottom.setVisibility(View.VISIBLE);
    }

    private void changeUiToShowUiPause() {
        llTopContainer.setVisibility(View.VISIBLE);
        llBottomControl.setVisibility(View.VISIBLE);
        ivStart.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        ivThumb.setVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
        updateStartImage();
    }

    private void changeUiToClearUiPause() {
        changeUiToClearUi();
        pbBottom.setVisibility(View.VISIBLE);
    }

    private void changeUiToClearUi() {
        llTopContainer.setVisibility(View.INVISIBLE);
        llBottomControl.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        pbLoading.setVisibility(View.INVISIBLE);
        ivThumb.setVisibility(View.INVISIBLE);
        ivCover.setVisibility(View.INVISIBLE);
        pbBottom.setVisibility(View.INVISIBLE);
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
                                tvTitle.setVisibility(View.INVISIBLE);
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
}
