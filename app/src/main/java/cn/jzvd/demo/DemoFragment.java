package cn.jzvd.demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.jzvd.demo.R;

/**
 * Created by Nathen on 2017/6/9.
 */
public class DemoFragment extends Fragment {

    ListView listView;
    int index;

    public DemoFragment setIndex(int index) {
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
        listView.setAdapter(new VideoListAdapter(getActivity(),
                VideoConstant.videoUrls[index],
                VideoConstant.videoTitles[index],
                VideoConstant.videoThumbs[index]));
        return listView;
    }
}
