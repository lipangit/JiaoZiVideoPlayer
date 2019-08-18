package cn.jzvd.demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.jzvd.Jzvd;

/**
 * Created by Nathen on 2017/6/9.
 */
public class FragmentDemo extends Fragment {

    ListView listView;
    int index;

    public FragmentDemo setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInastanceState) {
        listView = (ListView) inflater.inflate(R.layout.layout_list, container, false);
        listView.setAdapter(new AdapterListView(getActivity(),
                VideoConstant.videoUrls[index],
                VideoConstant.videoTitles[index],
                VideoConstant.videoThumbs[index]));
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Jzvd.onScrollReleaseAllVideos(view, firstVisibleItem, visibleItemCount, totalItemCount);

                if (Jzvd.CURRENT_JZVD == null) return;
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                int currentPlayPosition = Jzvd.CURRENT_JZVD.positionInList;
//                Log.e(TAG, "onScrollReleaseAllVideos: " +
//                        currentPlayPosition + " " + firstVisibleItem + " " + currentPlayPosition + " " + lastVisibleItem);
                if (currentPlayPosition >= 0) {
                    if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                        if (Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos();//为什么最后一个视频横屏会调用这个，其他地方不会
                        }
                    }
                }
            }
        });
        return listView;
    }
}
