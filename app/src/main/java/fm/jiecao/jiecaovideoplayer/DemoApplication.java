package fm.jiecao.jiecaovideoplayer;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Nathen
 * On 2015/12/01 11:29
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUniversalImageLoader();

        //这里将会设置所有播放器的皮肤 | Here the player will set all the skin
//        JCVideoPlayer.setGlobleSkin(R.color.colorPrimary, R.color.colorAccent, R.drawable.skin_seek_progress,
//                R.color.bottom_bg, R.drawable.skin_enlarge_video, R.drawable.skin_shrink_video);
        //这里将会改变所有缩略图的ScaleType | Here will change all thumbnails ScaleType
//        JCVideoPlayer.setThumbImageViewScalType(ImageView.ScaleType.FIT_XY);
    }

    private void initUniversalImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}
