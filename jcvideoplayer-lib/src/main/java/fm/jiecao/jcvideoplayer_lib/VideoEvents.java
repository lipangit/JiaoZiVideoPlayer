package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2015/11/30 12:19
 */
public class VideoEvents {
    public static final int VE_START = 366001;
    public static final int VE_STOP = 366002;
    public static final int VE_PROGRESSING = 366003;
    public static final int VE_PREPARED = 366004;
    public static final int VE_SURFACEHOLDER_CREATED = 366005;
    public static final int VE_SURFACEHOLDER_FINISH_FULLSCREEN = 366006;
    public static final int VE_MEDIAPLAYER_FINISH_COMPLETE = 366007;
    public static final int VE_MEDIAPLAYER_BUFFERUPDATE = 366008;
    public static final int VE_MEDIAPLAYER_SEEKCOMPLETE = 366009;
    public static final int VE_MEDIAPLAYER_RESIZE = 366010;

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
