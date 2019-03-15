package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.JzvdStd;

public class JzvdStdList extends JzvdStd {
    public JzvdStdList(Context context) {
        super(context);
    }

    public JzvdStdList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, JZMediaInterface jzMediaInterface) {
        super.setUp(jzDataSource, screen, jzMediaInterface);


    }
}
