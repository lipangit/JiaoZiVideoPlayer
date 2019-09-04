package cn.jzvd.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class AdapterListView extends BaseAdapter {

    public static final String TAG = "JZVD";

    Context context;

    String[] videoUrls;
    String[] videoTitles;
    String[] videoThumbs;

    public AdapterListView(Context context, String[] videoUrls, String[] videoTitles, String[] videoThumbs) {
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
        viewHolder.jzvdStd = convertView.findViewById(R.id.videoplayer);
        viewHolder.jzvdStd.setUp(
                videoUrls[position],
                videoTitles[position], Jzvd.SCREEN_NORMAL);
        Glide.with(convertView.getContext())
                .load(videoThumbs[position])
                .into(viewHolder.jzvdStd.thumbImageView);
        viewHolder.jzvdStd.positionInList = position;
        return convertView;
    }

    class ViewHolder {
        JzvdStd jzvdStd;
    }
}
