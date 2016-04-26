package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2016/04/26 20:53
 */
public interface JCBuriedPointStandard extends JCBuriedPoint {
    
    void POINT_START_THUMB(String url, Object... objects);

    void POINT_CLICK_BLANK(String url, Object... objects);

    void POINT_CLICK_BLANK_FULLSCREEN(String url, Object... objects);

}
