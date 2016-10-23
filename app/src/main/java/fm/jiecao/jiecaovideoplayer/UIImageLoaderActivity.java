package fm.jiecao.jiecaovideoplayer;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jiecaovideoplayer.CustomView.JCVideoPlayerStandardFresco;

/**
 * Created by Nathen on 16/7/31.
 */
public class UIImageLoaderActivity extends AppCompatActivity {
    JCVideoPlayerStandard videoController1, videoController2, videoController3, videoController4;
    JCVideoPlayerStandardFresco videoController5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_loadimage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("LoadImageDemo");

        videoController1 = (JCVideoPlayerStandard) findViewById(R.id.videocontroller1);
        videoController1.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                "嫂子抓住");
        ImageLoader.getInstance().displayImage("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg",
                videoController1.thumbImageView);

        videoController2 = (JCVideoPlayerStandard) findViewById(R.id.videocontroller2);
        videoController2.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                "嫂子别晃");
        Glide.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(videoController2.thumbImageView);

        videoController3 = (JCVideoPlayerStandard) findViewById(R.id.videocontroller3);
        videoController3.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                "嫂子别躲");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(videoController3.thumbImageView);

        videoController4 = (JCVideoPlayerStandard) findViewById(R.id.videocontroller4);
        videoController4.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                "嫂子别忘了");
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        com.android.volley.toolbox.ImageLoader imageLoader = new com.android.volley.toolbox.ImageLoader(mQueue, new BitmapCache());
        com.android.volley.toolbox.ImageLoader.ImageListener listener =
                com.android.volley.toolbox.ImageLoader.getImageListener(videoController4.thumbImageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg", listener);

        videoController5 = (JCVideoPlayerStandardFresco) findViewById(R.id.videocontroller5);
        videoController5.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                "嫂子打电话");
        Uri uri = Uri.parse("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg");
        videoController5.thumbImageView.setImageURI(uri);

        View vv = findViewById(R.id.scroll);
        vv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                videoController1.onScrollChange();
            }
        });
    }

    public class BitmapCache implements com.android.volley.toolbox.ImageLoader.ImageCache {
        private LruCache<String, Bitmap> cache;

        public BitmapCache() {
            cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
