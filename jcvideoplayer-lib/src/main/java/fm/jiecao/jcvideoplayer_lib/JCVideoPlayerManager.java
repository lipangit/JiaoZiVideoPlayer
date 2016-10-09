package fm.jiecao.jcvideoplayer_lib;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Put JCVideoPlayer into layout
 * From a JCVideoPlayer to another JCVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JCVideoPlayerManager {

    public static WeakReference<JCMediaPlayerListener> CURRENT_SCROLL_LISTENER;
    public static LinkedList<WeakReference<JCMediaPlayerListener>> LISTENERLIST = new LinkedList<>();

    public static void putScrollListener(JCMediaPlayerListener listener) {
        if (listener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_TINY ||
                listener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN) return;
        CURRENT_SCROLL_LISTENER = new WeakReference<>(listener);//每次setUp的时候都应该add
    }

    public static void putListener(JCMediaPlayerListener listener) {
        LISTENERLIST.push(new WeakReference<>(listener));
    }
    public static void checkAndPutListener(JCMediaPlayerListener listener) {
        if (listener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_TINY ||
                listener.getScreenType() == JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN) return;
        int location = -1;
        for (int i = 1; i < LISTENERLIST.size(); i++) {
            JCMediaPlayerListener jcMediaPlayerListener = LISTENERLIST.get(i).get();
            if (listener.getUrl().equals(jcMediaPlayerListener.getUrl())) {
                location = i;
            }
        }
        if (location != -1) {
            LISTENERLIST.remove(location);
            if (LISTENERLIST.size() <= location) {
                LISTENERLIST.addLast(new WeakReference<>(listener));
            } else {
                LISTENERLIST.set(location, new WeakReference<>(listener));

            }
        }
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
