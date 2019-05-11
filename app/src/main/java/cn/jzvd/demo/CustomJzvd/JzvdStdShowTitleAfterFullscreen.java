package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

/**
 * Created by Nathen
 * On 2016/04/27 10:49
 */
public class JzvdStdShowTitleAfterFullscreen extends JzvdStd {
    public JzvdStdShowTitleAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        if (this.screen == SCREEN_FULLSCREEN) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
        }
    }
}
