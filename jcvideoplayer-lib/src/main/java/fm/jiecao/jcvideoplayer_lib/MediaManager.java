package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.graphics.Point;

/**
 * Created by tysheng
 * Date: 2017/4/17 20:37.
 * Email: tyshengsx@gmail.com
 */

public interface MediaManager {
    void release();

    void pause();

    Point getVideoSize();

    void start();

    void seekTo(int seekTimePosition);

    void initTextureView(Context context);

    void setCurrentVideoWH(int i, int i1);

    void pauseIfPlaying();

    int getCurrentPositionWhenPlaying(int currentState);

    int getDuration();

    void onPrepared();

    void onCompletion();

    void onBufferingUpdate(int i);

    void onSeekComplete();

    boolean onError(int i, int i1);

    boolean onInfo(int i, int i1);

    void onVideoSizeChanged(int i, int i1);

    void releaseMediaPlayer();

    void releaseAllVideos();

    boolean backPress();

    void onVideoSizeChanged();
}
