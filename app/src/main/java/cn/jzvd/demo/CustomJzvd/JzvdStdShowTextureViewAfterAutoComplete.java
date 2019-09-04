package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JzvdStd;

/**
 * ////TODO  这个还有重要的逻辑问题，reset的时候是否设置普通屏幕的方向，普通屏幕的方向应该在什么地方设置
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
