package com.hxy.skynet.Login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hxy.skynet.MainActivity;
import com.hxy.skynet.R;
import com.hxy.skynet.bean.Person;
import com.hxy.skynet.until.JsonUtils;
import com.hxy.skynet.until.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements OnClickListener {

    private Button mBtnRegister;
    private Button mBtnLogin;
    public boolean islogin;
    private EditText tv_user, tv_password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;
    public ProgressDialog mDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0011) {
                gotoMain();
            }
            if (msg.what == 0022) {
                toast("账号或密码错误！");
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.login);

        initView();
    }

    public void initView() {

        mBtnRegister = (Button) findViewById(R.id.register);
        mBtnRegister.setOnClickListener(this);

        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);
        mDialog = new ProgressDialog(LoginActivity.this);
        tv_user = (EditText) findViewById(R.id.username);
        tv_password = (EditText) findViewById(R.id.password);
        bt_username_clear = (Button)findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button)findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button)findViewById(R.id.bt_pwd_eye);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        initWatcher();
        tv_user.addTextChangedListener(username_watcher);
        tv_password.addTextChangedListener(password_watcher);
    }
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {;
                if(s.toString().length()>0){
                    bt_username_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.register:
                goRegisterActivity();
                break;
            case R.id.login:
                mDialog.setMessage("正在登录中，请稍后...");
                mDialog.show();
                connectToLogin();
                //test();
                break;
            case R.id.bt_username_clear:
                tv_user.setText("");
                tv_password.setText("");
                break;
            case R.id.bt_pwd_clear:
                tv_password.setText("");
                break;
            case R.id.bt_pwd_eye:
                if(tv_password.getInputType() == (InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    bt_pwd_eye.setBackgroundResource(R.drawable.set0);
                    tv_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
                }else{
                    bt_pwd_eye.setBackgroundResource(R.drawable.set1);
                    tv_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                tv_password.setSelection(tv_password.getText().toString().length());
                break;
            default:
                break;
        }
    }

    public void goRegisterActivity() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @SuppressLint("ShowToast")

    private void connectToLogin() {
        String user =  tv_user.getText().toString();
        String password =  tv_password.getText().toString();
        if (user.equals("") || password.equals("")) {
            Toast.makeText(this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences userInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        String url = "http://23.83.240.232:8080/Myser/demo1?" + "name=" + user + "&psd=" + password;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                Log.i("response", response);
                if (response.contains("false")) {
                    handler.sendEmptyMessage(0022);
                } else {
                    islogin = true;
                    Person p = new Person();
                    p = JsonUtils.deserialize(response, Person.class);
                    editor.putString("name", p.getName());
                    editor.putString("phonenum", p.getPhonenum());
                    editor.putString("email", p.getEmail());
                    editor.putString("company", p.getCompany());
                    editor.putString("sheng", p.getSheng());
                    editor.putString("shi", p.getShi());
                    editor.putString("address", p.getAdress());
                    editor.putString("machine", p.getMachine());
                    editor.commit();
                    Log.i("editor", "保存用户信息成功");
                    handler.sendEmptyMessage(0011);
                }
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("load  failure", e.toString());mDialog.dismiss();
                toast("登录失败！");
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    private void test() {
        String user = (String) tv_user.getText().toString();
        String password = (String) tv_password.getText().toString();
        Log.i("shuru", user + "," + password);
        OkHttpUtils.Param p1 = new OkHttpUtils.Param("user", user);
        OkHttpUtils.Param p2 = new OkHttpUtils.Param("psd", password);
        Log.i("param", p1.toString() + "," + p2.toString());
        List<OkHttpUtils.Param> params = new ArrayList<>();
        params.add(p1);
        params.add(p2);
        String url = "http://192.168.42.28:8080/test";
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                Log.i("response", response);
               handler.sendEmptyMessage(0011);
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("load  failure", e.toString());
            }
        };
        OkHttpUtils.post(url, callback, params);
    }

    private void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(isReLogin){
                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                LoginActivity.this.startActivity(mHomeIntent);
            }else{
                LoginActivity.this.finish();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }*/
}
