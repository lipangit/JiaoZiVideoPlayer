package fm.jiecao.jiecaovideoplayer.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 2016/11/26.
 */

public class JCVideoPlayerStandardAutoCompleteAfterFullscreen extends JCVideoPlayerStandard {
    public JCVideoPlayerStandardAutoCompleteAfterFullscreen(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardAutoCompleteAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            onStateAutoComplete();
        } else {
            super.onAutoCompletion();
        }

    }
}
