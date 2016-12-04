package fm.jiecao.jcvideoplayer_lib;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Set;

/**
 * Put JCVideoPlayer into layout
 * From a JCVideoPlayer to another JCVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JCVideoPlayerManager {

    public static HashMap<String, WeakReference<JCMediaPlayerListener>> FIRST_FLOOR_LIST = new HashMap<>();
    public static WeakReference<JCMediaPlayerListener> SECOND_FLOOR;

    //When set up
    public static void putFirstFloor(JCMediaPlayerListener jcMediaPlayerListener) {
        //TODO clear null point
        //TODO 如果url不一样，但是listener一样（列表复用的时候），就要删除那个url的listener，设置新url的listener
        if (jcMediaPlayerListener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN ||
                jcMediaPlayerListener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_TINY)
            return;
        FIRST_FLOOR_LIST.put(jcMediaPlayerListener.getUrl(), new WeakReference<>(jcMediaPlayerListener));
    }

    public static void putSecondFloor(JCMediaPlayerListener jcMediaPlayerListener) {
        if (jcMediaPlayerListener == null) {
            SECOND_FLOOR = null;
        } else {
            SECOND_FLOOR = new WeakReference<>(jcMediaPlayerListener);
        }
    }

    public static JCMediaPlayerListener getCurrentJcvdOnFirtFloor() {
        if (FIRST_FLOOR_LIST.get(JCMediaManager.CURRENT_PLAYING_URL) != null) {
            return FIRST_FLOOR_LIST.get(JCMediaManager.CURRENT_PLAYING_URL).get();
        }
        return null;
    }

    public static JCMediaPlayerListener getCurrentJcvdOnSecondFloor() {
        if (SECOND_FLOOR != null) {
            return SECOND_FLOOR.get();
        }
        return null;
    }

//    public static JCMediaPlayerListener findFirtFloor(){//need this when listview
//        return null;
//    }

    public static void completeAll() {
        if (SECOND_FLOOR != null && SECOND_FLOOR.get() != null) {
            SECOND_FLOOR.get().onCompletion();
            putSecondFloor(null);
        }
        Set<String> set = FIRST_FLOOR_LIST.keySet();
        for (String s : set) {
            if (FIRST_FLOOR_LIST.get(s) != null && FIRST_FLOOR_LIST.get(s).get() != null && FIRST_FLOOR_LIST.get(s).get().getState() != JCVideoPlayer.CURRENT_STATE_NORMAL) {
                FIRST_FLOOR_LIST.get(s).get().onCompletion();
            }
        }
//        if (getCurrentJcvdOnFirtFloor() != null) {
//            getCurrentJcvdOnFirtFloor().onCompletion();
//        }
    }
}
