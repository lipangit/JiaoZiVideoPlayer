package fm.jiecao.jcvideoplayer_lib;

/**
 * <p>关于eventbus的使用进行过多次探索，目前认为这么使用是最容易理解的</p>
 * <p>Explore about the use of the eventbus many times, now think so used is the most easy to understand</p>
 * Created by Nathen
 * On 2015/11/30 12:19
 */
public class VideoEvents {
    public static final int VE_START = 366001;
    public static final int VE_STOP = 366002;
    //    public static final int VE_PROGRESSING = 366003;
    public static final int VE_PREPARED = 366004;
    public static final int VE_SURFACEHOLDER_CREATED = 366005;
    public static final int VE_SURFACEHOLDER_FINISH_FULLSCREEN = 366006;
    public static final int VE_MEDIAPLAYER_FINISH_COMPLETE = 366007;
    public static final int VE_MEDIAPLAYER_UPDATE_BUFFER = 366008;
    public static final int VE_MEDIAPLAYER_UPDATE_PROGRESS = 366011;
    public static final int VE_MEDIAPLAYER_SEEKCOMPLETE = 366009;
    public static final int VE_MEDIAPLAYER_RESIZE = 366010;


    // 埋点事件
    // Buried point events
    /**
     * <p>用户点击播放按钮,播放视频</p>
     * <p>Users click the play button, play the video</p>
     */
    public static final int POINT_START_ICON = 367001;
    /**
     * <p>用户点击缩略图,播放视频</p>
     * <p>Users click on the thumbnail, play the video</p>
     */
    public static final int POINT_START_THUMB = 367002;
    /**
     * <p>暂停视频(非全屏)</p>
     * <p>Pause the video (not fullscreen)</p>
     */
    public static final int POINT_STOP = 367003;
    /**
     * <p>暂停视频(全屏)</p>
     * <p>Pause the video (full screen)</p>
     */
    public static final int POINT_STOP_FULLSCREEN = 367004;
    /**
     * <p>用户点击播放按钮，暂停状态下继续播放视频(非全屏)</p>
     * <p>User clicks the play button, pause state continues to play a video (not fullscreen)</p>
     */
    public static final int POINT_RESUME = 367014;
    /**
     * <p>用户点击播放按钮，暂停状态下继续播放视频(全屏)</p>
     * <p>User clicks the play button, pause state continues to play video (full screen)</p>
     */
    public static final int POINT_RESUME_FULLSCREEN = 367005;

    /**
     * <p>用户在播放视频时点击空白区域，显示播放控件和隐藏播放控件(非全屏)</p>
     * <p>When users click on a blank area of the video playback, display the playback controls and hide playback controls (not fullscreen)</p>
     */
    public static final int POINT_CLICK_BLANK = 367006;
    /**
     * <p>用户在播放视频时点击空白区域，显示播放控件和隐藏播放控件(全屏)</p>
     * <p>When users click on a blank area of the video playback, display the playback controls and hide playback controls (full screen)</p>
     */
    public static final int POINT_CLICK_BLANK_FULLSCREEN = 367007;
    /**
     * <p>用户修改播放进度(非全屏)</p>
     * <p>User to modify the playback progress (not fullscreen)</p>
     */
    public static final int POINT_CLICK_SEEKBAR = 367008;
    /**
     * <p>用户修改播放进度(全屏)</p>
     * <p>User to modify the playback progress (full screen)</p>
     */
    public static final int POINT_CLICK_SEEKBAR_FULLSCREEN = 367009;
    /**
     * <p>视频播放完了(非全屏)</p>
     * <p>Video playback over (not fullscreen)</p>
     */
    public static final int POINT_AUTO_COMPLETE = 367010;
    /**
     * <p>视频播放完了(全屏)</p>
     * <p>Video playback over (full screen)</p>
     */
    public static final int POINT_AUTO_COMPLETE_FULLSCREEN = 367011;
    /**
     * <p>进入全屏</p>
     * <p>Enter full screen</p>
     */
    public static final int POINT_ENTER_FULLSCREEN = 367012;
    /**
     * <p>退出全屏</p>
     * <p>Exit Full Screen</p>
     */
    public static final int POINT_QUIT_FULLSCREEN = 367013;

    public int type;
    public Object obj;
    public Object obj1;

    public VideoEvents setType(int type) {
        this.type = type;
        return this;
    }

    public VideoEvents setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    public VideoEvents setObj1(Object obj1) {
        this.obj1 = obj1;
        return this;
    }
}
