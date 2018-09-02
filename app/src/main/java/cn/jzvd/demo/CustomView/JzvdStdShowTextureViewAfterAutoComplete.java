package cn.jzvd.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JzvdStd;

/**
 * Created by Nathen on 2016/11/6.
 */

public class JzvdStdShowTextureViewAfterAutoComplete extends JzvdStd {
    public JzvdStdShowTextureViewAfterAutoComplete(Context context) {
        super(context);
    }

    public JzvdStdShowTextureViewAfterAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        thumbImageView.setVisibility(View.GONE);
    }

}
