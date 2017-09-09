package cn.jzvd;

/**
 * Put JZVideoPlayer into layout
 * From a JZVideoPlayer to another JZVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JZVideoPlayerManager {

    public static JZVideoPlayer FIRST_FLOOR_JZVD;
    public static JZVideoPlayer SECOND_FLOOR_JZVD;

    public static JZVideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(JZVideoPlayer jzVideoPlayer) {
        FIRST_FLOOR_JZVD = jzVideoPlayer;
    }

    public static JZVideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(JZVideoPlayer jzVideoPlayer) {
        SECOND_FLOOR_JZVD = jzVideoPlayer;
    }

    public static JZVideoPlayer getCurrentJzvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JZVD != null) {
            SECOND_FLOOR_JZVD.onCompletion();
            SECOND_FLOOR_JZVD = null;
        }
        if (FIRST_FLOOR_JZVD != null) {
            FIRST_FLOOR_JZVD.onCompletion();
            FIRST_FLOOR_JZVD = null;
        }
    }
}
