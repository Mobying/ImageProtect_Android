package bying.imageprotect.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import bying.imageprotect.R;
import bying.imageprotect.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private EditText nickName,password01,password02;
    private Button btn_register;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //    @ViewInject(id=R.id.et_username)
        nickName = (EditText) findViewById(R.id.et_username);
        //    @ViewInject(id=R.id.et_password)
        password01 = (EditText) findViewById(R.id.et_password);
        //    @ViewInject(id=R.id.et_password_again)
        password02 = (EditText) findViewById(R.id.et_password_again);
        //    @ViewInject(id=R.id.btn_register)
        btn_register = (Button) findViewById(R.id.btn_register);

        back = (ImageView) findViewById(R.id.back_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        onClickRegister();
    }

    private void onClickRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }
    private void register(){
        String name = nickName.getText().toString().trim();
        String password = password01.getText().toString().trim();
        String pwd_again = password02.getText().toString().trim();
        if (name.length() <= 0 || password.length() <= 0 || pwd_again.length() <= 0) {
            ShowToast(R.string.toast_register_all_error);
            return;
        }
        if (password.length() < 6 ) {
            ShowToast(R.string.toast_register_pswlenth_error);
            return;
        }

        if (!password.equals(pwd_again)) {
            ShowToast(R.string.toast_register_psw_unequal_error);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        //        final User bu = new User();
        //        bu.setUsername(name);//昵称
        //        bu.setPassword(password);
        //        //将user和设备id进行绑定aa
        //        bu.setSex(true);
        //        bu.setDeviceType("android");
        //        bu.setInstallId(BmobInstallation.getInstallationId(this));
        //        bu.signUp(RegisterActivity.this, new SaveListener() {
        //            @Override
        //            public void onSuccess() {
        //
        //                progress.dismiss();
        //                ShowToast(R.string.toast_register_success);
        //                // 将设备与username进行绑定
        //                userManager.bindInstallationForRegister(bu.getUsername());
        //                // 启动主页
        //                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        //                startActivity(intent);
        //                finish();
        //            }
        //
        //            @Override
        //            public void onFailure(int arg0, String arg1) {
        //
        //                BmobLog.i(arg1);
        //                ShowToast("注册失败:" + arg1);
        //                progress.dismiss();
        //            }
        //        });
    }
}
