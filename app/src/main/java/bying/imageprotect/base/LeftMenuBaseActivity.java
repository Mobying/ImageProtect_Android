package bying.imageprotect.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import bying.imageprotect.R;
import bying.imageprotect.ui.AboutActivity;
import bying.imageprotect.ui.DownloadActivity;
import bying.imageprotect.ui.FriendsActivity;
import bying.imageprotect.ui.LoginActivity;
import bying.imageprotect.ui.MainActivity;
import bying.imageprotect.ui.SearchActivity;
import bying.imageprotect.ui.SetActivity;
import bying.imageprotect.ui.ShareActivity;

/**
 * Created by Wu_bying on 2017/5/8.
 * DrawerLayout + NavigationView + Login 点击监听的封装
 */

public class LeftMenuBaseActivity extends BaseActivity {
    //声明相关变量
    //    private NavigationView nav;
//    private TextView login, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        login = (TextView) findViewById(R.id.login);
//        userid = (TextView) findViewById(R.id.userid);
//        initLeftSlip();//侧滑
//        initLogin();//登录
//        initClickEvents();//注册事件
    }

        public void loginListener(NavigationView nav) {
            View headview = nav.getHeaderView(0);
            TextView login = (TextView) headview.findViewById(R.id.login);
    //        //        if (userManager.getCurrentUser() == null) {
//            userid.setVisibility(View.GONE);
//            login.setVisibility(View.VISIBLE);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getCurrentActivity(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    //        } else {
    //            userid.setVisibility(View.VISIBLE);
    //            login.setVisibility(View.GONE);
    //            userid.setText(userManager.getCurrentUser().getUsername());
    //        }

    public void initLeftSlip(Toolbar toolbar, DrawerLayout mDrawerLayout) {
        //三行代码绑定DrawerLayout和NavigationView
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getCurrentActivity(), mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //设置菜单列表
        //        simpleAdapter = new LeftMenuAdapter().getAdapter(this);
        //        lvLeftMenu.setAdapter(simpleAdapter);
    }

    //判断侧滑点击的按钮并处理逻辑
    public void navigationListener(NavigationView nav, final int position) {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //在这里处理item的点击事件
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        ShowToast("主页");
                        if (position != 1) {
                            startActivity(new Intent(getCurrentActivity(), MainActivity.class));
                            finish();
                        }
                        break;
                    case R.id.nav_share:
                        ShowToast("上传");
                        if (position != 2) {
                            startActivity(new Intent(getCurrentActivity(), ShareActivity.class));
                            finish();
                        }
                        break;
                    case R.id.nav_download:
                        ShowToast("下载");
                        if (position != 3) {
                            startActivity(new Intent(getCurrentActivity(), DownloadActivity.class));
                            finish();
                        }
                        break;
                    case R.id.nav_search:
                        ShowToast("搜索");
                        if (position != 4) {
                            startActivity(new Intent(getCurrentActivity(), SearchActivity.class));
                            finish();
                        }
                        break;
                    case R.id.nav_friends:
                        ShowToast("好友");
                        if (position != 5) {
                            startActivity(new Intent(getCurrentActivity(), FriendsActivity.class));
                            finish();
                        }
                        break;
                    case R.id.nav_set:
                        ShowToast("设置");
                        if (position != 6) {
                            startActivity(new Intent(getCurrentActivity(), SetActivity.class));
                            finish();
                        }
                        break;
                    case R.id.nav_about:
                        ShowToast("关于");
                        if (position != 7) {
                            startActivity(new Intent(getCurrentActivity(), AboutActivity.class));
                            finish();
                        }
                        break;
                    default:
                }
                return true;
            }
        });
    }

    //    private void initClickEvents() {
    //        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //            @Override
    //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //                ShowLog("position:" + position);
    //                if (position == 0) {
    //                    // Intent intent = new Intent(getCurrentActivity().this, MainActivity.class);
    //                    startActivity(new Intent(getCurrentActivity(), MainActivity.class));
    //                    finish();
    //                }
    //                else if (position == 1) {
    //                    Intent intent = new Intent(getCurrentActivity(), ShareActivity.class);
    //                    startActivity(intent);
    //                    finish();
    //                }
    //                else if (position == 2) {
    //                    Intent intent = new Intent(getCurrentActivity(), DownloadActivity.class);
    //                    startActivity(intent);
    //                    finish();
    //                }
    //                else if (position == 3) {
    //                    Intent intent = new Intent(getCurrentActivity(), SearchActivity.class);
    //                    startActivity(intent);
    //                    finish();
    //                }
    //                else if (position == 4) {
    //                    Intent intent = new Intent(getCurrentActivity(), FriendsActivity.class);
    //                    startActivity(intent);
    //                    finish();
    //                }
    //                else if (position == 5) {
    //                    Intent intent = new Intent(getCurrentActivity(), SetActivity.class);
    //                    startActivity(intent);
    //                    finish();
    //                }
    //            }
    //        });
    //    }

    /*得到当前的Activity*/
    public static Activity getCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}