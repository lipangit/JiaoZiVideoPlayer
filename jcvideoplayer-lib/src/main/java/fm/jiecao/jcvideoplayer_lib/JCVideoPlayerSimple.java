package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

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
    public int getLayoutId() {
        return R.layout.jc_layout_base;
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            fullscreenButton.setImageResource(R.drawable.jc_shrink);
        } else {
            fullscreenButton.setImageResource(R.drawable.jc_enlarge);
        }
        fullscreenButton.setVisibility(View.GONE);
    }

    @Override
    public void setUiWitStateAndScreen(int state) {
        super.setUiWitStateAndScreen(state);
        switch (currentState) {
            case CURRENT_STATE_NORMAL:
                startButton.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PREPARING:
                startButton.setVisibility(View.INVISIBLE);
                break;
            case CURRENT_STATE_PLAYING:
                startButton.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PAUSE:
                break;
            case CURRENT_STATE_ERROR:
                break;
        }
        updateStartImage();
    }

    private void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jc_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jc_click_error_selector);
        } else {
            startButton.setImageResource(R.drawable.jc_click_play_selector);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fullscreen && currentState == CURRENT_STATE_NORMAL) {
            Toast.makeText(getContext(), "Play video first", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onClick(v);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (currentState == CURRENT_STATE_NORMAL) {
                Toast.makeText(getContext(), "Play video first", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        super.onProgressChanged(seekBar, progress, fromUser);
    }
}
