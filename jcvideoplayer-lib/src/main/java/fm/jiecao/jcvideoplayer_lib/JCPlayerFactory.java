package fm.jiecao.jcvideoplayer_lib;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import fm.jiecao.jcvideoplayer_lib.player.AbstractPlayer;
import fm.jiecao.jcvideoplayer_lib.player.JCIjkMediaPlayer;
import fm.jiecao.jcvideoplayer_lib.player.JCMediaPlayer;

/**
 *  默认使用原生播放器
 *  可自主选择使用原生MediaPlayer、IjkMediaPlayer
 * （使用Ijk需要使用者自行依赖ijk库以及so,视频sdk不提供,不会影响原有视频sdk大小以及功能）
 *
 *  Created by lidongyang on 2017/8/10.
 */
public class JCPlayerFactory {
    /**
     * android原生MediaPlayer
     */
    public static final int TYPE_MEDIA_PLAYER = 1;
    /**
     * IjkMediaPlayer
     */
    public static final int TYPE_IJK_MEDIA_PLAYER = 2;

    private static @PlayerType int sPlayerType = TYPE_MEDIA_PLAYER;

    public static void init(@PlayerType int playerType) {
        sPlayerType = playerType;
    }

    public static @NonNull
    AbstractPlayer buildMediaPlayer() {
        @PlayerType int playerType = sPlayerType;
        if (TYPE_MEDIA_PLAYER == playerType) {
            return new JCMediaPlayer();
        } else if (TYPE_IJK_MEDIA_PLAYER == playerType) {
            return new JCIjkMediaPlayer();
        } else {
            return new JCMediaPlayer();
        }
    }

    @IntDef({TYPE_MEDIA_PLAYER, TYPE_IJK_MEDIA_PLAYER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerType {
    }

}
