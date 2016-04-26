package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2016/04/04 22:13
 */
public interface JCBuriedPoint {

    void POINT_START_ICON(String url, Object... objects);

    void POINT_START_ERROR(String url, Object... objects);

    void POINT_STOP(String url, Object... objects);

    void POINT_STOP_FULLSCREEN(String url, Object... objects);

    void POINT_RESUME(String url, Object... objects);

    void POINT_RESUME_FULLSCREEN(String url, Object... objects);

    void POINT_CLICK_SEEKBAR(String url, Object... objects);

    void POINT_CLICK_SEEKBAR_FULLSCREEN(String url, Object... objects);

    void POINT_AUTO_COMPLETE(String url, Object... objects);

    void POINT_AUTO_COMPLETE_FULLSCREEN(String url, Object... objects);

    void POINT_ENTER_FULLSCREEN(String url, Object... objects);

    void POINT_QUIT_FULLSCREEN(String url, Object... objects);

}
