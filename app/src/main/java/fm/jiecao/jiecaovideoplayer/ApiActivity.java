package fm.jiecao.jiecaovideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Nathen on 16/7/31.
 */
public class ApiActivity extends AppCompatActivity implements View.OnClickListener {
    Button mSmallChange, mBigChange, mImageLoader, mOrientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("About UI");
        setContentView(R.layout.activity_ui);

        mSmallChange = (Button) findViewById(R.id.small_change);
        mBigChange = (Button) findViewById(R.id.big_change);
        mImageLoader = (Button) findViewById(R.id.imageloader);
        mOrientation = (Button) findViewById(R.id.orientation);

        mSmallChange.setOnClickListener(this);
        mBigChange.setOnClickListener(this);
        mImageLoader.setOnClickListener(this);
        mOrientation.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.small_change:
                startActivity(new Intent(ApiActivity.this, UISmallChangeActivity.class));
                break;
            case R.id.big_change:
                Toast.makeText(ApiActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(ApiActivity.this, UIBigChangeActivity.class));
                break;
            case R.id.imageloader:
                startActivity(new Intent(ApiActivity.this, UIImageLoaderActivity.class));
                break;
            case R.id.orientation:
                startActivity(new Intent(ApiActivity.this, OrientationActivity.class));
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
