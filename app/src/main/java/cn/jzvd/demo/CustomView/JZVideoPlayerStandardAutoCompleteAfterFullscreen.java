package cn.jzvd.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 2016/11/26.
 */

public class JZVideoPlayerStandardAutoCompleteAfterFullscreen extends JZVideoPlayerStandard {
    public JZVideoPlayerStandardAutoCompleteAfterFullscreen(Context context) {
        super(context);
    }

    public JZVideoPlayerStandardAutoCompleteAfterFullscreen(Context context, AttributeSet attrs) {
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
