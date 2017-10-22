package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Nathen on 16/7/31.
 */
public class ListViewActivity extends AppCompatActivity implements View.OnClickListener {
    Button mNormal, mNormalAutoTiny, mViewPager, mMultiHolder, mRecyleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("ListView");
        setContentView(R.layout.activity_listview);

        mNormal = (Button) findViewById(R.id.normal);
        mNormalAutoTiny = (Button) findViewById(R.id.normal_auto_tiny);
        mViewPager = (Button) findViewById(R.id.listview_fragment_viewpager);
        mMultiHolder = (Button) findViewById(R.id.multiholder);
        mRecyleView = (Button) findViewById(R.id.recyleview);

        mNormal.setOnClickListener(this);
        mNormalAutoTiny.setOnClickListener(this);
        mViewPager.setOnClickListener(this);
        mMultiHolder.setOnClickListener(this);
        mRecyleView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal:
                startActivity(new Intent(ListViewActivity.this, ListViewNormalActivity.class));
                break;
            case R.id.normal_auto_tiny:
                startActivity(new Intent(ListViewActivity.this, ListViewNormalAutoTinyActivity.class));
                break;
            case R.id.listview_fragment_viewpager:
                startActivity(new Intent(ListViewActivity.this, ListViewFragmentViewpagerActivity.class));
                break;
            case R.id.multiholder:
                startActivity(new Intent(ListViewActivity.this, ListViewMultiHolderActivity.class));
                break;
            case R.id.recyleview:
                startActivity(new Intent(ListViewActivity.this, RecyclerViewNormalActivity.class));
                break;
        }
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

}
