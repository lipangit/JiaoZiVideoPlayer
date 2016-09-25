package fm.jiecao.jcvideoplayer_lib;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Put JCVideoPlayer into layout
 * From a JCVideoPlayer to another JCVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JCVideoPlayerManager {

    public static List<WeakReference<JCMediaPlayerListener>>       CURRENT_SCROLL_LISTENER_LIST = new LinkedList<>();
    public static LinkedList<WeakReference<JCMediaPlayerListener>> LISTENERLIST                 = new LinkedList<>();

    public static void putScrollListener(JCMediaPlayerListener listener) {
        if (listener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_TINY ||
                listener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN) return;
//        for (int i = 0; i < CURRENT_SCROLL_LISTENER_LIST.size(); i++) {
//            if (CURRENT_SCROLL_LISTENER_LIST.get(i) == null &&
//                    CURRENT_SCROLL_LISTENER_LIST.get(i).get().getUrl() == listener.getUrl()) {
//                CURRENT_SCROLL_LISTENER_LIST.clear();
//                CURRENT_SCROLL_LISTENER_LIST.add(new WeakReference<>(listener));
//                return;
//            }
//        }
//        for (int i = 0; i < CURRENT_SCROLL_LISTENER_LIST.size(); i++) {
//            if (CURRENT_SCROLL_LISTENER_LIST.get(i) == null &&
//                    CURRENT_SCROLL_LISTENER_LIST.get(i).get() == listener) {
//                CURRENT_SCROLL_LISTENER_LIST.add(i, new WeakReference<>(listener));
//                return;
//            }
//        }
        CURRENT_SCROLL_LISTENER_LIST.clear();
        CURRENT_SCROLL_LISTENER_LIST.add(new WeakReference<>(listener));//每次setUp的时候都应该add
    }

    public static void clearScrollListenerList() {
        CURRENT_SCROLL_LISTENER_LIST.clear();
    }

    public static void putListener(JCMediaPlayerListener listener) {
        LISTENERLIST.push(new WeakReference<>(listener));
    }

    public static JCMediaPlayerListener popListener() {
        if (LISTENERLIST.size() == 0) {
            return null;
        }
        return LISTENERLIST.pop().get();
    }

    public static JCMediaPlayerListener getFirst() {
        if (LISTENERLIST.size() == 0) {
            return null;
        }
        return LISTENERLIST.getFirst().get();
    }

    public static void completeAll() {
        JCMediaPlayerListener ll = popListener();
        while (ll != null) {
            ll.onCompletion();
            ll = popListener();
        }
    }
}
