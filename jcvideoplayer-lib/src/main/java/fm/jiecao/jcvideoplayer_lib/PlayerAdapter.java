package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by tysheng
 * Date: 2017/4/17 14:21.
 * Email: tyshengsx@gmail.com
 */

public interface PlayerAdapter {
    boolean isPlaying();

    void pause();

    long getCurrentPosition();

    long getDuration();

    void start();

    void release();

    void setListener() throws Exception;

    void seekTo(long var1);
}
