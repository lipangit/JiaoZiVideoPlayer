package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = "JieCaoVideoPlayer";

    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    Context context;
    int pager = -1;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    public VideoListAdapter(Context context, int pager) {
        this.context = context;
        this.pager = pager;
    }

    @Override
    public int getCount() {
        return pager == -1 ? videoIndexs.length : 4;
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
        viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
        if (pager == -1) {
            viewHolder.jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[0][position]);
            Picasso.with(convertView.getContext())
                    .load(VideoConstant.videoThumbs[0][position])
                    .into(viewHolder.jcVideoPlayer.thumbImageView);
        } else {
            viewHolder.jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[pager][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[pager][position]);
            Picasso.with(convertView.getContext())
                    .load(VideoConstant.videoThumbs[pager][position])
                    .into(viewHolder.jcVideoPlayer.thumbImageView);
        }
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
