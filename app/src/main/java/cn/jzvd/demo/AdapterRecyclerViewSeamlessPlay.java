package cn.jzvd.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by pengan.li on 19/7/4.
 */
public class AdapterRecyclerViewSeamlessPlay extends RecyclerView.Adapter<AdapterRecyclerViewSeamlessPlay.MyViewHolder> {

    public static final String TAG = "AdapterRecyclerView";
    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;
    private OnListListener onListListener;

    public AdapterRecyclerViewSeamlessPlay(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_video_seamless_play, parent,
                false));
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder [" + holder.jzvdStd.hashCode() + "] position=" + position);

        holder.jzvdStd.setUp(
                VideoConstant.videoUrls[0][position],
                VideoConstant.videoTitles[0][position], Jzvd.SCREEN_NORMAL);
        Glide.with(holder.jzvdStd.getContext()).load(VideoConstant.videoThumbs[0][position]).into(holder.jzvdStd.thumbImageView);

        holder.infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onListListener!=null){
                    onListListener.onInfoClick(VideoConstant.videoTitles[0][position],
                            VideoConstant.videoUrls[0][position], VideoConstant.videoThumbs[0][position], position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        JzvdStd jzvdStd;
        LinearLayout infoLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
            infoLayout = itemView.findViewById(R.id.infoLayout);
        }
    }

    public interface OnListListener {
        void onInfoClick(String title, String url, String thumbUrl, int position);
    }

    public void setOnListListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }




}
