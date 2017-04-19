package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;

import java.util.Map;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CLICK_QUIT_FULLSCREEN_TIME;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_NORMAL;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PAUSE;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.FULL_SCREEN_NORMAL_DELAY;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.SCREEN_WINDOW_TINY;

/**
 * <p>统一管理MediaPlayer的地方,只有一个mediaPlayer实例，那么不会有多个视频同时播放，也节省资源。</p>
 * <p>Unified management MediaPlayer place, there is only one MediaPlayer instance, then there will be no more video broadcast at the same time, also save resources.</p>
 * Created by Nathen
 * On 2015/11/30 15:39
 */
public  class JCMediaManager implements TextureView.SurfaceTextureListener, MediaManager {
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;
    public static final int MEDIA = 0, IJK = 1;
    public static String TAG = "JieCaoVideoPlayer";
    public static JCResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static String CURRENT_PLAYING_URL;
    public static boolean CURRENT_PLING_LOOP;
    public static Map<String, String> MAP_HEADER_DATA;
    public static int whichPlayer;
    private static fm.jiecao.jcvideoplayer_lib.JCMediaManager JCMediaManager;

    private AbstractPlayerControllerWrapper mWrapper;

    public JCMediaManager() {
        setWrapper(new AbstractPlayerControllerWrapper());
    }

    public static fm.jiecao.jcvideoplayer_lib.JCMediaManager instance() {
        if (JCMediaManager == null) {
            JCMediaManager = new JCMediaManager();
        }
        return JCMediaManager;
    }

    public static JCResizeTextureView getTextureView() {
        return textureView;
    }

    public static void setTextureView(JCResizeTextureView resizeTextureView) {
        textureView = resizeTextureView;
    }

    public void onVideoSizeChanged() {
        textureView.setVideoSize(getVideoSize());
    }

    public boolean backPress() {
        Log.i(TAG, "backPress");
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) < FULL_SCREEN_NORMAL_DELAY)
            return false;
        if (JCVideoPlayerManager.getSecondFloor() != null) {
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            JCVideoPlayer jcVideoPlayer = JCVideoPlayerManager.getSecondFloor();
            jcVideoPlayer.onEvent(jcVideoPlayer.currentScreen == SCREEN_WINDOW_FULLSCREEN ?
                    JCUserAction.ON_QUIT_FULLSCREEN :
                    JCUserAction.ON_QUIT_TINYSCREEN);
            JCVideoPlayerManager.getFirstFloor().playOnThisJcvd();
            return true;
        } else if (JCVideoPlayerManager.getFirstFloor() != null &&
                (JCVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_FULLSCREEN ||
                        JCVideoPlayerManager.getFirstFloor().currentScreen == SCREEN_WINDOW_TINY)) {//以前我总想把这两个判断写到一起，这分明是两个独立是逻辑
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
            //直接退出全屏和小窗
            JCVideoPlayerManager.getCurrentJcvd().currentState = CURRENT_STATE_NORMAL;
            JCVideoPlayerManager.getFirstFloor().clearFloatScreen();
            releaseMediaPlayer();
            JCVideoPlayerManager.setFirstFloor(null);
            return true;
        }
        return false;
    }

    public void releaseAllVideos() {
        if ((System.currentTimeMillis() - CLICK_QUIT_FULLSCREEN_TIME) > FULL_SCREEN_NORMAL_DELAY) {
            Log.d(TAG, "releaseAllVideos");
            JCVideoPlayerManager.completeAll();
            releaseMediaPlayer();
        }
    }

    public AbstractPlayerControllerWrapper getWrapper() {
        return mWrapper;
    }

    public void setWrapper(AbstractPlayerControllerWrapper wrapper) {
        mWrapper = wrapper;
    }

    private void removeTextureView() {
        savedSurfaceTexture = null;
        if (textureView != null && textureView.getParent() != null) {
            ((ViewGroup) textureView.getParent()).removeView(textureView);
        }
    }

    public void initTextureView(Context context) {
        removeTextureView();
        textureView = new JCResizeTextureView(context);
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void setCurrentVideoWH(int width, int height) {
        getWrapper().setCurrentVideoWH(width, height);
    }

    @Override
    public void pauseIfPlaying() {
        try {
            if (getWrapper().getMediaPlayer() != null &&
                    getWrapper().getMediaPlayer().isPlaying()) {
                getWrapper().getMediaPlayer().pause();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCurrentPositionWhenPlaying(int currentState) {
        int position = 0;
        if (getWrapper().getMediaPlayer() == null) return position;//这行代码不应该在这，如果代码和逻辑万无一失的话，心头之恨呐
        if (currentState == CURRENT_STATE_PLAYING ||
                currentState == CURRENT_STATE_PAUSE ||
                currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            try {
                position = (int) getWrapper().getMediaPlayer().getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return position;
            }
        }
        return position;
    }

    @Override
    public int getDuration() {
        int duration = 0;
        if (getWrapper().getMediaPlayer() == null) return duration;
        try {
            duration = (int) getWrapper().getMediaPlayer().getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    @Override
    public Point getVideoSize() {
        return getWrapper().getVideoSize();
    }

    @Override
    public void start() {
        getWrapper().start();
    }

    @Override
    public void release() {
        if (getWrapper() != null) {
            getWrapper().release();
        }
    }

    @Override
    public void pause() {
        getWrapper().pause();
    }

    @Override
    public void seekTo(int var1) {
        getWrapper().seekTo(var1);
    }

    //    player listener callback
    @Override
    public void onCompletion() {
        getWrapper().onCompletion();
    }

    @Override
    public void onBufferingUpdate(final int percent) {
        getWrapper().onBufferingUpdate(percent);
    }

    @Override
    public void onSeekComplete() {
        getWrapper().onSeekComplete();
    }

    @Override
    public boolean onError(final int what, final int extra) {
        return getWrapper().onError(what, extra);
    }

    @Override
    public boolean onInfo(final int what, final int extra) {
        return getWrapper().onInfo(what, extra);
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        getWrapper().onVideoSizeChanged(width, height);
    }

    @Override
    public void onPrepared() {
        getWrapper().onPrepared();
    }

    //handler callback
    private void prepare() {
        getWrapper().prepare();
    }

    @Override
    public void releaseMediaPlayer() {
        getWrapper().releaseMediaPlayer();
    }

    //texture callback
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + this.hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // 如果SurfaceTexture还没有更新Image，则记录SizeChanged事件，否则忽略
        Log.i(TAG, "onSurfaceTextureSizeChanged [" + this.hashCode() + "] ");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }
}
