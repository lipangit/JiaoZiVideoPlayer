package fm.jiecao.jcvideoplayer_lib;

import android.util.Log;

/**
 * Put JCVideoPlayer into layout
 * From a JCVideoPlayer to another JCVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JCVideoPlayerManager {

    public static JCVideoPlayer FIRST_FLOOR_JCVD;
    public static JCVideoPlayer SECOND_FLOOR_JCVD;

    public static void setFirstFloor(JCVideoPlayer jcVideoPlayer) {
        FIRST_FLOOR_JCVD = jcVideoPlayer;
    }

    public static void setSecondFloor(JCVideoPlayer jcVideoPlayer) {
        SECOND_FLOOR_JCVD = jcVideoPlayer;
    }

    public static JCVideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JCVD;
    }

    public static JCVideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JCVD;
    }

    public static JCVideoPlayer getCurrentJcvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JCVD != null) {
            SECOND_FLOOR_JCVD.onCompletion();
            SECOND_FLOOR_JCVD = null;
        }
        if (FIRST_FLOOR_JCVD != null) {
            FIRST_FLOOR_JCVD.onCompletion();
            FIRST_FLOOR_JCVD = null;
        }
    }
}
