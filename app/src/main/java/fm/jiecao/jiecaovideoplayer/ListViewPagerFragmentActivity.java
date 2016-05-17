package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathen
 * On 2016/05/17 22:13
 */
public class ListViewPagerFragmentActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_viewpager_fragment);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(true);
    getSupportActionBar().setDisplayUseLogoEnabled(false);
    getSupportActionBar().setTitle("ListViewPagerFragmentDemo");

    List<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(new VideoFragment());
    fragmentList.add(new VideoFragment());
    fragmentList.add(new VideoFragment());
    fragmentList.add(new VideoFragment());
    MyAdapter myAdapter = new MyAdapter(fragmentList);
    ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
    viewPager.setAdapter(myAdapter);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  public static class VideoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

    }
  }

  public class MyAdapter extends PagerAdapter {

    List<Fragment> fragmentLists;

    public MyAdapter(List<Fragment> lists) {
      fragmentLists = lists;
    }

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return fragmentLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      // TODO Auto-generated method stub
      return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View view, int position) {
      return fragmentLists.get(position);
    }
  }
}
