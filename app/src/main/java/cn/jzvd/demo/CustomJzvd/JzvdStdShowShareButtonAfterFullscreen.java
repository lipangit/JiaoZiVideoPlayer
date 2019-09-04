package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

/**
 * Created by Nathen
 * On 2016/04/22 00:54
 */
public class JzvdStdShowShareButtonAfterFullscreen extends JzvdStd {

    public ImageView shareButton;

    public JzvdStdShowShareButtonAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdShowShareButtonAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_std_with_share_button;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.share) {
            Toast.makeText(getContext(), "Whatever the icon means", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        shareButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        shareButton.setVisibility(View.VISIBLE);
    }
}
