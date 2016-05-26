package fm.jiecao.jiecaovideoplayer.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/04/27 10:49
 */
public class JCVideoPlayerStandardShowTitleAfterFullscreen extends JCVideoPlayerStandard {
  public JCVideoPlayerStandardShowTitleAfterFullscreen(Context context) {
    super(context);
  }

  public JCVideoPlayerStandardShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean setUp(String url, Object... objects) {
    if (super.setUp(url, objects)) {
      if (mIfCurrentIsFullscreen) {
        titleTextView.setVisibility(View.VISIBLE);
      } else {
        titleTextView.setVisibility(View.INVISIBLE);
      }
      return true;
    }
    return false;
  }
}
