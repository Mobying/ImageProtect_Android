package bying.imageprotect.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import bying.imageprotect.R;
import bying.imageprotect.ui.LoginActivity;

/**
 * Created by Wu_bying on 2017/5/8.
 * 侧滑菜单
 */

public class LeftMenuBaseActivity extends BaseActivity{
    //声明相关变量
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView login,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLeftSlip();//侧滑
    //    initLogin();//登录
        //initClickEvents();//注册事件
    }

    private void initLeftSlip() {
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        nav = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_left);
        login = (TextView) findViewById(R.id.login);
        userid = (TextView) findViewById(R.id.userid);
//        setSupportActionBar();
//        getSupportActionBar().setDisplayShowTitleEnabled();
        mDrawerToggle = new ActionBarDrawerToggle(getCurrentActivity(), mDrawerLayout, toolbar, R.string.open, R.string.close) {
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
    public static Activity getCurrentActivity () {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
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
    private void initLogin(){
        //        if(userManager.getCurrentUser() == null) {
        //            userid.setVisibility(View.GONE);
        //            login.setVisibility(View.VISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCurrentActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        //        }
        //        else {
        //            userid.setVisibility(View.VISIBLE);
        //            login.setVisibility(View.GONE);
        //            userid.setText(userManager.getCurrentUser().getUsername());
        //        }
    }

}
