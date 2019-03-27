package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;

/**
 * 这个类里详细定义了进入小窗的方式，进入小窗之后小窗的操作等
 */
public class JzvdStdTinyWindow extends JzvdStd {
    public JzvdStdTinyWindow(Context context) {
        super(context);
    }

    public JzvdStdTinyWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, JZMediaInterface jzMediaInterface) {
        //滑动列表的时候
        //如果滑动过快，setUp在scroll之前生效
        //setUp是原来的url  不是原来的url

        //滑出去  快速  慢速
        if (jzDataSource.getCurrentUrl().equals(CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
            //正在播放滑动过快，如果这个正式当前正在播放的url


        }

        super.setUp(jzDataSource, screen, jzMediaInterface);
    }

    @Override
    public void gotoScreenTiny() {
        super.gotoScreenTiny();
        Log.i(TAG, "startWindowTiny " + " [" + this.hashCode() + "] ");
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR || currentState == CURRENT_STATE_AUTO_COMPLETE)
            return;
        ViewGroup vg = (ViewGroup) getParent();
        vg.removeView(this);
        vg.addView(cloneMe());
        CONTAINER_LIST.add(vg);
        ViewGroup vgg = (ViewGroup) (JZUtils.scanForActivity(getContext())).getWindow().getDecorView();//和他也没有关系
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 400);
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        vgg.addView(this, lp);
        setScreenTiny();
    }
}
