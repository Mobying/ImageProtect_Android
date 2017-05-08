package bying.imageprotect.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import bying.imageprotect.R;

public class SplashActivity extends Activity {
    /**
     * Called when the ui is first created.
     */

    private final int SPLASH_DISPLAY_LENGHT = 2500;//显示2.5秒
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);//跳转
                startActivity(intent);
                SplashActivity.this.finish();//关闭
            }
        }, SPLASH_DISPLAY_LENGHT);

    }
}
