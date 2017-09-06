package cn.jzvd.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 2016/11/6.
 */

public class JZVideoPlayerStandardShowTextureViewAfterAutoComplete extends JZVideoPlayerStandard {
    public JZVideoPlayerStandardShowTextureViewAfterAutoComplete(Context context) {
        super(context);
    }

    public JZVideoPlayerStandardShowTextureViewAfterAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        thumbImageView.setVisibility(View.GONE);
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            thumbImageView.setVisibility(View.GONE);
        }
    }
}
