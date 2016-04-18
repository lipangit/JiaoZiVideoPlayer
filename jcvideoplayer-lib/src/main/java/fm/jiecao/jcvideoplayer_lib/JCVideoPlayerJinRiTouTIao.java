package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Nathen
 * On 2016/04/18 16:15
 */
public class JCVideoPlayerJinRiTouTIao extends JCAbstractVideoPlayer {
    public JCVideoPlayerJinRiTouTIao(Context context) {
        super(context);
    }

    public JCVideoPlayerJinRiTouTIao(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_demo_layout_jinritoutiao;
    }
}
