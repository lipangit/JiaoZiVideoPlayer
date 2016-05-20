package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2016/04/26 20:53
 */
public interface JCBuriedPointStandard extends JCBuriedPoint {

  void onClickStartThumb(String url, Object... objects);

  void onClickBlank(String url, Object... objects);

  void onClickBlankFullscreen(String url, Object... objects);

}
