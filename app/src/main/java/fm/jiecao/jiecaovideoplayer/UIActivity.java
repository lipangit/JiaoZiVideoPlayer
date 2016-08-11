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
public class UIActivity extends AppCompatActivity implements View.OnClickListener {
    Button mSmallChange, mBigChange, mImageLoader;

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

        mSmallChange.setOnClickListener(this);
        mBigChange.setOnClickListener(this);
        mImageLoader.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.small_change:
                startActivity(new Intent(UIActivity.this, UISmallChangeActivity.class));
                break;
            case R.id.big_change:
                Toast.makeText(UIActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(UIActivity.this, UIBigChangeActivity.class));
                break;
            case R.id.imageloader:
                startActivity(new Intent(UIActivity.this, UIImageLoaderActivity.class));
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
