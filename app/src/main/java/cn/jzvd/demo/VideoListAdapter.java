package cn.jzvd.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = "JiaoZiVideoPlayer";

    Context context;

    String[] videoUrls;
    String[] videoTitles;
    String[] videoThumbs;

    public VideoListAdapter(Context context, String[] videoUrls, String[] videoTitles, String[] videoThumbs) {
        this.context = context;
        this.videoUrls = videoUrls;
        this.videoTitles = videoTitles;
        this.videoThumbs = videoThumbs;
    }

    @Override
    public int getCount() {
        return videoUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.jzVideoPlayer = (JZVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
        viewHolder.jzVideoPlayer.setUp(
                videoUrls[position], JZVideoPlayer.SCREEN_LAYOUT_LIST,
                videoTitles[position]);
        Picasso.with(convertView.getContext())
                .load(videoThumbs[position])
                .into(viewHolder.jzVideoPlayer.thumbImageView);
        return convertView;
    }

    class ViewHolder {
        JZVideoPlayerStandard jzVideoPlayer;
    }
}
