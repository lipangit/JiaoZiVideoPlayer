package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2016/04/04 22:13
 */
public interface JCBuriedPoint {

    void POINT_START_ICON(String title, String url);

    void POINT_START_THUMB(String title, String url);

    void POINT_STOP(String title, String url);

    void POINT_STOP_FULLSCREEN(String title, String url);

    void POINT_RESUME(String title, String url);

    void POINT_RESUME_FULLSCREEN(String title, String url);

    void POINT_CLICK_BLANK(String title, String url);

    void POINT_CLICK_BLANK_FULLSCREEN(String title, String url);

    void POINT_CLICK_SEEKBAR(String title, String url);

    void POINT_CLICK_SEEKBAR_FULLSCREEN(String title, String url);

    void POINT_AUTO_COMPLETE(String title, String url);

    void POINT_AUTO_COMPLETE_FULLSCREEN(String title, String url);

    void POINT_ENTER_FULLSCREEN(String title, String url);

    void POINT_QUIT_FULLSCREEN(String title, String url);

}
