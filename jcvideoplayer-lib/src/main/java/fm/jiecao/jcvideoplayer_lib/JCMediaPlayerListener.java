package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen on 16/7/26.
 */
public interface JCMediaPlayerListener {
    void onPrepared();

    void onAutoCompletion();

    void onCompletion();

    boolean onBackPress();

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onError(int what, int extra);

    void onInfo(int what, int extra);

    void onVideoSizeChanged();

    void onBackFullscreen();
}
