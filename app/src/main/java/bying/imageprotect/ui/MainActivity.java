package bying.imageprotect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import bying.imageprotect.R;
import bying.imageprotect.base.LeftMenuBaseActivity;

public class MainActivity extends LeftMenuBaseActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("opencv_java");
        System.loadLibrary("native-lib");
    }

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView nav;
//    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: start");
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_draw);
        nav = (NavigationView) findViewById(R.id.nav_view);
        navigationListener(nav,1);
        initToolbar();
        initLeftSlip(toolbar,mDrawerLayout);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    private void initToolbar(){
        toolbar.setTitle(R.string.app_name);//设置Toolbar标题
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share:
                ShowToast("上传");
                startActivity(new Intent(this, ShareActivity.class));
                finish();
                break;
            case R.id.menu_download:
                ShowToast("下载");
                startActivity(new Intent(this, DownloadActivity.class));
                finish();
                break;
            case R.id.menu_search:
                ShowToast("搜索");
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                break;
            default:
        }
        return true;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

}
