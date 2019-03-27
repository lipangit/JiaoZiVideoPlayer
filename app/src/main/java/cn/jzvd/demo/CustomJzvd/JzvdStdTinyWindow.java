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
        //这个是clone出来的

        //滑出去进入小窗，  当前的url和正在播放的url不一样，
//        if (!jzDataSource.containsTheUrl(CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
//            //进入小窗
//            gotoScreenTiny();
//        }
        //滑进来，退出小窗


        ////
        if (CURRENT_JZVD != null) {
            if (this == CURRENT_JZVD && jzDataSource.containsTheUrl(CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                //不理解
            } else if (this == CURRENT_JZVD && !jzDataSource.containsTheUrl(CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                gotoScreenTiny();
            } else if (this != CURRENT_JZVD && jzDataSource.containsTheUrl(CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                if (CURRENT_JZVD.currentScreen == SCREEN_WINDOW_TINY) {
                    //需要退出小窗退到我这里，我这里是第一层级
                    //clear container, goback
                    ViewGroup vp = (ViewGroup) getParent();
                    vp.removeAllViews();
                    ((ViewGroup) CURRENT_JZVD.getParent()).removeAllViews();
                    vp.addView(CURRENT_JZVD);
                    CONTAINER_LIST.pop();
                }
            } else if (this != CURRENT_JZVD && !jzDataSource.containsTheUrl(CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
            }
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
