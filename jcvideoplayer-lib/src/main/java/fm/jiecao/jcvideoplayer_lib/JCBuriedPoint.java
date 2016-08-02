package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2016/04/04 22:13
 */
public interface JCBuriedPoint {

    int ON_CLICK_START_ICON          = 0;
    int ON_CLICK_START_ERROR         = 1;
    int ON_CLICK_START_AUTO_COMPLETE = 2;

    int ON_CLICK_PAUSE   = 3;
    int ON_CLICK_RESUME  = 4;
    int ON_SEEK_POSITION = 5;
    int ON_AUTO_COMPLETE = 6;

    int ON_ENTER_FULLSCREEN = 7;
    int ON_QUIT_FULLSCREEN  = 8;
    int ON_ENTER_TINYSCREEN = 9;
    int ON_QUIT_TINYSCREEN  = 10;


    int ON_TOUCH_SCREEN_SEEK_VOLUME   = 11;
    int ON_TOUCH_SCREEN_SEEK_POSITION = 12;

    void onEvent(int type, String url, int screen, Object... objects);

}
