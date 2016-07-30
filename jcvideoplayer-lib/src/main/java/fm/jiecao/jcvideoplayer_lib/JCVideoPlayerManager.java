package fm.jiecao.jcvideoplayer_lib;

import java.lang.ref.WeakReference;

/**
 * Put JCVideoPlayer into layout
 * From a JCVideoPlayer to another JCVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class JCVideoPlayerManager {

//    public static void changeJCVideoPlayer(JCVideoPlayer from, JCVideoPlayer to) {
//        //pass state screen
//
//    }
//
//    public static void createJCVideoPlayer(ViewGroup viewGroup, ViewGroup.LayoutParams params
//            , JCVideoPlayer jcVideoPlayer) {
//
//    }

    private static WeakReference<JCMediaPlayerListener> LISTENER;
    private static WeakReference<JCMediaPlayerListener> LAST_LISTENER;

    public static JCMediaPlayerListener listener() {
        if (LISTENER == null)
            return null;
        return LISTENER.get();
    }

    public static JCMediaPlayerListener lastListener() {
        if (LAST_LISTENER == null)
            return null;
        return LAST_LISTENER.get();
    }

    public static void setListener(JCMediaPlayerListener listener) {
        if (listener == null)
            LISTENER = null;
        else
            LISTENER = new WeakReference<>(listener);
    }

    public static void setLastListener(JCMediaPlayerListener lastListener) {
        if (lastListener == null)
            LAST_LISTENER = null;
        else
            LAST_LISTENER = new WeakReference<>(lastListener);
    }
}
