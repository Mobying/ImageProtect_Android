package bying.imageprotect.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import bying.imageprotect.R;
import bying.imageprotect.base.LeftMenuBaseActivity;

public class DownloadActivity extends LeftMenuBaseActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView nav;
//    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        toolbar = (Toolbar) findViewById(R.id.tl_custom);//toolbar可以共用
        mDrawerLayout = (DrawerLayout) findViewById(R.id.download_draw);
        nav = (NavigationView) findViewById(R.id.nav_view);
//        login = (TextView) findViewById(R.id.login);
        loginListener(nav);
        navigationListener(nav,3);
        initToolbar();//注意和侧滑的顺序
        initLeftSlip(toolbar,mDrawerLayout);

    }
    private void initToolbar(){
        toolbar.setTitle(R.string.download);//设置Toolbar标题
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
