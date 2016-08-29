package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class RecyclerViewVideoAdapter extends RecyclerView.Adapter<RecyclerViewVideoAdapter.MyViewHolder> {

    String[] videoUrls = {"http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4",
            "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4"};
    String[] videoThumbs = {"http://img4.jiecaojingxuan.com/2016/5/1/3430ec64-e6a7-4d8e-b044-9d408e075b7c.jpg",
            "http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg"};
    String[] videoTitles = {"嫂子坐火车", "嫂子打游戏"};
    int[] videoIndexs = {0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1};
    private Context context;
    public static final String TAG = "RecyclerViewVideoAdapter";
    public RecyclerViewVideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_videoview, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG,"onBindViewHolder ["+holder.jcVideoPlayer.hashCode()+"] position="+position);
        boolean setUp = holder.jcVideoPlayer.setUp(
                videoUrls[videoIndexs[position]], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                videoTitles[videoIndexs[position]]);
        if (setUp) {
            ImageLoader.getInstance().displayImage(videoThumbs[videoIndexs[position]],
                    holder.jcVideoPlayer.thumbImageView);
        }

    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;

        public MyViewHolder(View itemView) {
            super(itemView);
            jcVideoPlayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
        }
    }

}
