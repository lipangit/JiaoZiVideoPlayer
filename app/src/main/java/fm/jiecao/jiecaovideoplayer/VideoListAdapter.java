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
  String[] videoUrls = {"http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4",
    "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4"};
  String[] videoThumbs = {"http://img4.jiecaojingxuan.com/2016/5/1/3430ec64-e6a7-4d8e-b044-9d408e075b7c.jpg",
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
