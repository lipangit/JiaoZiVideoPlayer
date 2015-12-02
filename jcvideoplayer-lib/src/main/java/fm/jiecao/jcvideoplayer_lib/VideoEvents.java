package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2015/11/30 12:19
 */
public class VideoEvents {
    public static final int VE_START = 0x366001;
    public static final int VE_STOP = 0x366002;
    public static final int VE_PROGRESSING = 0x366003;
    public static final int VE_PREPARED = 0x366004;
    public static final int VE_SURFACEHOLDER_CREATED = 0x366005;
    public static final int VE_SURFACEHOLDER_FINISH_FULLSCREEN = 0x366006;
    public static final int VE_SURFACEHOLDER_FINISH_COMPLETE = 0x366007;

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
