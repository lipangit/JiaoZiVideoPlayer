package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = "JieCaoVideoPlayer";

    String[] videoUrls   = {"http://video.jiecao.fm/8/17/bGQS3BQQWUYrlzP1K4Tg4Q__.mp4",
            "http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4",
            "http://video.jiecao.fm/8/18/%E5%A4%A7%E5%AD%A6.mp4",
            "http://video.jiecao.fm/8/16/%E8%B7%B3%E8%88%9E.mp4",
            "http://video.jiecao.fm/8/16/%E9%B8%AD%E5%AD%90.mp4",
            "http://video.jiecao.fm/8/16/%E9%A9%BC%E8%83%8C.mp4",
            "http://video.jiecao.fm/8/16/%E4%BF%AF%E5%8D%A7%E6%92%91.mp4",
            "http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4",
            "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4",
            "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"};
    String[] videoThumbs = {"http://img4.jiecaojingxuan.com/2016/8/17/bd7ffc84-8407-4037-a078-7d922ce0fb0f.jpg",
            "http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg",
            "http://img4.jiecaojingxuan.com/2016/8/18/ccd86ca1-66c7-4331-9450-a3b7f765424a.png",
            "http://img4.jiecaojingxuan.com/2016/8/16/2adde364-9be1-4864-b4b9-0b0bcc81ef2e.jpg",
            "http://img4.jiecaojingxuan.com/2016/8/16/2a877211-4b68-4e3a-87be-6d2730faef27.png",
            "http://img4.jiecaojingxuan.com/2016/8/16/aaeb5da9-ac50-4712-a28d-863fe40f1fc6.png",
            "http://img4.jiecaojingxuan.com/2016/8/16/e565f9cc-eedc-45f0-99f8-5b0fa3aed567.jpg",
            "http://img4.jiecaojingxuan.com/2016/5/1/3430ec64-e6a7-4d8e-b044-9d408e075b7c.jpg",
            "http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg",
            "http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg"};
    String[] videoTitles = {"嫂子出来", "嫂子溢出", "嫂子我姓王", "嫂子怕好了", "嫂子很渴", "嫂子这样不好", "嫂子别笑", "嫂子坐火车", "嫂子打游戏", "嫂子稳当的"};

    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    Context context;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoIndexs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    int a = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "why you always getview");

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        boolean setUp = viewHolder.jcVideoPlayer.setUp(
                videoUrls[videoIndexs[position]], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                videoTitles[videoIndexs[position]]);
        if (setUp) {
            ImageLoader.getInstance().displayImage(videoThumbs[videoIndexs[position]],
                    viewHolder.jcVideoPlayer.thumbImageView);
        }
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
