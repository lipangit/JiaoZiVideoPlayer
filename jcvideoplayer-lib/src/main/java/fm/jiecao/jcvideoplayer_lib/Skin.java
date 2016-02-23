package fm.jiecao.jcvideoplayer_lib;

/**
 * Created by Nathen
 * On 2016/02/23 15:19
 */
class Skin {
    Skin(int titleColor, int timeColor, int seekDrawable, int bottomControlBackground, int enlargRecId, int shrinkRecId) {
        this.titleColor = titleColor;
        this.timeColor = timeColor;
        this.seekDrawable = seekDrawable;
        this.bottomControlBackground = bottomControlBackground;
        this.enlargRecId = enlargRecId;
        this.shrinkRecId = shrinkRecId;
    }

    int titleColor;
    int timeColor;
    int seekDrawable;
    int bottomControlBackground;
    int enlargRecId;
    int shrinkRecId;
}
