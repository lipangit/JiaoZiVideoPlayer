package fm.jiecao.jiecaovideoplayer.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jiecaovideoplayer.R;

//import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Just replace thumb from ImageView to SimpleDraweeView
 * Created by Nathen
 * On 2016/05/01 22:59
 */
public class JCVideoPlayerStandardFresco extends JCVideoPlayerStandard {
    //    public SimpleDraweeView thumbImageView;

    public JCVideoPlayerStandardFresco(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardFresco(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progress);
        titleTextView = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.back);
//        thumbImageView = (SimpleDraweeView) findViewById(R.id.thumb);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        tinyBackImageView = (ImageView) findViewById(R.id.back_tiny);

//        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        tinyBackImageView.setOnClickListener(this);

    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (objects.length == 0) return;
        titleTextView.setText(objects[0].toString());
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            fullscreenButton.setImageResource(R.drawable.jc_shrink);
            backButton.setVisibility(View.VISIBLE);
            tinyBackImageView.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_LAYOUT_LIST) {
            fullscreenButton.setImageResource(R.drawable.jc_enlarge);
            backButton.setVisibility(View.GONE);
            tinyBackImageView.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            tinyBackImageView.setVisibility(View.VISIBLE);
            setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_standard_fresco;
    }


}
