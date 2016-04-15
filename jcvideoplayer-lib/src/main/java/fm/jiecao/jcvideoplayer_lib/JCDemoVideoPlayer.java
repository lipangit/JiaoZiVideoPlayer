package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Nathen
 * On 2016/04/10 15:45
 */
public class JCDemoVideoPlayer extends JCAbstractVideoPlayer {
    public JCDemoVideoPlayer(Context context) {
        super(context);
    }

    public JCDemoVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        //init my video

    }

    @Override
    public void onClick(View v) {
        //this should before super.onClick()


        super.onClick(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_base_demo_layout;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
