package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class VideoListAdapter extends BaseAdapter {
    String[] videoUrls = {"http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
            "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4"};
    String[] videoThumbs = {"http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
            "http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg"};
    String[] videoTitles = {"嫂子真紧", "嫂子抬腿"};

    int[] videoIndexs = {0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1};
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_list, null);
            viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.jcVideoPlayer.setUp(
                videoUrls[videoIndexs[position]],
                videoTitles[videoIndexs[position]]);
        ImageLoader.getInstance().displayImage(videoThumbs[videoIndexs[position]],
                viewHolder.jcVideoPlayer.ivThumb);
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
