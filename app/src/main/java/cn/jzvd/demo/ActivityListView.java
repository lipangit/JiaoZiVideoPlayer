package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Nathen on 16/7/31.
 */
public class ActivityListView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("ListView");
        setContentView(R.layout.activity_listview);
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

    public void clickNormal(View view) {
        startActivity(new Intent(ActivityListView.this, ActivityListViewNormal.class));

    }

    public void clickListViewFragmentViewpager(View view) {
        startActivity(new Intent(ActivityListView.this, ActivityListViewFragmentViewPager.class));

    }

    public void clickMultiHolder(View view) {
        startActivity(new Intent(ActivityListView.this, ActivityListViewMultiHolder.class));

    }

    public void clickRecyclerView(View view) {
        startActivity(new Intent(ActivityListView.this, ActivityListViewRecyclerView.class));
    }
}
