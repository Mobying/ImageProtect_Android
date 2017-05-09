package bying.imageprotect.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bying.imageprotect.R;
import bying.imageprotect.base.BaseActivity;

public class LoginActivity extends BaseActivity {
//    @ViewInject(id=R.id.et_username)
    private TextView username = (TextView) findViewById(R.id.et_username);
//    @ViewInject(id=R.id.et_password)
    private TextView psd = (TextView) findViewById(R.id.et_password);
//    @ViewInject(id=R.id.btn_login)
    private Button btn_login = (Button) findViewById(R.id.btn_login);
//    @ViewInject(id = R.id.btn_register)
    private TextView register = (TextView) findViewById(R.id.login_register);
    private String name,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FinalActivity.initInjectedView(this);//实现IOC注解组件
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
        onClickLogin();
       // register();
    }

    private void onClickLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        name = username.getText().toString();
        password = psd.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ShowToast(R.string.toast_error_username_null);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.toast_error_password_null);
            return;
        }
        final ProgressDialog progress = new ProgressDialog(
                LoginActivity.this);
        progress.setMessage("正在登录...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
//        User user = new User();
//        user.setUsername(name);
//        user.setPassword(password);
//        userManager.login(user, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                //更新用户的地理位置以及好友的资料，可自行到BaseActivity类中查看此方法的具体实现，建议添加
//                //省略其他代码
//                loginToMain();
//            }
//
//            @Override
//            public void onFailure(int errorcode, String arg0) {
//                // TODO Auto-generated method stub
//                ShowToast("用户名或密码错误");
//            }
//        });
    }

//    private void loginToMain() {
//        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
//        LoginActivity.this.finish();
//    }
//
//    private void register(){
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//                startActivity(intent);
//                LoginActivity.this.finish();
//            }
//        });
//    }
}