package fm.jiecao.jcvideoplayer_lib;

/**
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
    public static final int VE_MEDIAPLAYER_BUFFERUPDATE = 366008;
    public static final int VE_MEDIAPLAYER_SEEKCOMPLETE = 366009;
    public static final int VE_MEDIAPLAYER_RESIZE = 366010;

    /**
     * 给统计用的埋点
     */
    public static final int POINT_START_ICON = 367001;
    public static final int POINT_START_THUMB = 367002;
    public static final int POINT_STOP = 367003;
    public static final int POINT_STOP_FULLSCREEN = 367004;
    public static final int POINT_RESUME = 367014;
    public static final int POINT_RESUME_FULLSCREEN = 367005;

    public static final int POINT_CLICK_BLANK = 367006;
    public static final int POINT_CLICK_BLANK_FULLSCREEN = 367007;
    public static final int POINT_CLICK_SEEKBAR = 367008;
    public static final int POINT_CLICK_SEEKBAR_FULLSCREEN = 367009;
    public static final int POINT_AUTO_COMPLETE = 367010;
    public static final int POINT_AUTO_COMPLETE_FULLSCREEN = 367011;

    public static final int POINT_ENTER_FULLSCREEN = 367012;
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
