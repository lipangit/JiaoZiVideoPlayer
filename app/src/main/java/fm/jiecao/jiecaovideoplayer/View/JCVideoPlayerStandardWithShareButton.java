package fm.jiecao.jiecaovideoplayer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jiecaovideoplayer.R;

/**
 * Created by Nathen
 * On 2016/04/22 00:54
 */
public class JCVideoPlayerStandardWithShareButton extends JCVideoPlayerStandard {


    ImageView btnShare;

    public JCVideoPlayerStandardWithShareButton(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardWithShareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        btnShare = (ImageView) findViewById(R.id.share);
        btnShare.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_layout_standard_with_share_button;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.share) {
            Toast.makeText(getContext(), "Whatever the icon means", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setStateAndUi(int state) {
        super.setStateAndUi(state);
        if (IF_CURRENT_IS_FULLSCREEN) {
            btnShare.setVisibility(View.VISIBLE);
        } else {
            btnShare.setVisibility(View.INVISIBLE);
        }
    }
}
