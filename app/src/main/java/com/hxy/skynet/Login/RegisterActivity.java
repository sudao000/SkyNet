package com.hxy.skynet.Login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hxy.skynet.R;
import com.hxy.skynet.bean.User;
import com.hxy.skynet.until.JsonUtils;
import com.hxy.skynet.until.OkHttpUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends Activity implements OnClickListener {


    private Button bt_check, bt_clear, bt_regist, bt_back;
    private EditText et_user, et_psd, et_psdt, et_company, et_phone, et_email, et_sheng, et_shi, et_add;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0001) {
                toast("用户名可用！");
                et_psd.setFocusable(true);
                et_psd.setFocusableInTouchMode(true);
                et_psd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et_psd.setText("");
                bt_regist.setClickable(true);
                bt_regist.setBackgroundColor(Color.rgb(91,148,85));
            }
            if(msg.what==0010){
                initetpsd();
                et_psd.setText("用户名不可用！");
            }
            if (msg.what == 0002) {
                String result=msg.getData().getString("result");
                toast(result);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.regist);
        initView();
    }

    public void initView() {

        bt_check = (Button) findViewById(R.id.bt_check);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_regist = (Button) findViewById(R.id.bt_regist);
        bt_back = (Button) findViewById(R.id.bt_back);
        et_user = (EditText) findViewById(R.id.et_user);
        et_psd = (EditText) findViewById(R.id.et_psd);
        et_psdt = (EditText) findViewById(R.id.et_psdt);
        et_company = (EditText) findViewById(R.id.et_company);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_sheng = (EditText) findViewById(R.id.et_sheng);
        et_shi = (EditText) findViewById(R.id.et_shi);
        et_add = (EditText) findViewById(R.id.et_add);
        bt_check.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_regist.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        initetpsd();
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_back:
                back();
                break;
            case R.id.bt_clear:
                clear();
                break;
            case R.id.bt_check:
                check();
                break;
            case R.id.bt_regist:
                regist();
                break;
            default:
                break;
        }
    }

    private void back() {
        finish();
    }

    private void clear() {
        et_user.setText("");
        et_psd.setText("");
        et_psdt.setText("");
        et_company.setText("");
        et_email.setText("");
        et_phone.setText("");
        et_sheng.setText("");
        et_shi.setText("");
        et_add.setText("");
        initetpsd();

    }
private void initetpsd(){
    et_psd.setFocusable(false);
    et_psd.setFocusableInTouchMode(false);
    et_psd.setInputType(InputType.TYPE_CLASS_TEXT);
    et_psd.setText("请检测用户名");
}
    private void check() {
        String user = et_user.getText().toString();
        String url = "http://23.83.240.232:8080/Myser/demo1?" + "name=" + user;
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                if (response.contains("false")) {
                   handler.sendEmptyMessage(0001);
                }
                else handler.sendEmptyMessage(0010);
            }
            @Override
            public void onFailure(Exception e) {
                Log.i("load  failure", e.toString());
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }
    private void toast(String s) {
        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void regist() {
        bt_regist.setClickable(false);
        bt_regist.setBackgroundColor(Color.rgb(80,80,80));
        String user = et_user.getText().toString();
        String psd = et_psd.getText().toString();
        String psdt = et_psdt.getText().toString();
        String company = et_company.getText().toString();
        String phone = et_phone.getText().toString();
        String email = et_email.getText().toString();
        String sheng = et_sheng.getText().toString();
        String shi = et_shi.getText().toString();
        String add = et_add.getText().toString();
        boolean isempty = user.equals("") || psd.equals("") || psdt.equals("") || company.equals("") || phone.equals("")
                || email.equals("") || sheng.equals("") || shi.equals("") || add.equals("");
        if (isempty) {
            toast("请完善个人信息！");
            return;
        }
        if (!psd.equals(psdt)) {
            toast("两次密码输入不一致！");
            return;
        }
        String url = "http://23.83.240.232:8080/Myser/demo2";
        User use = new User(user, psd, company, phone, email, sheng, shi, add);
        String useinfo = JsonUtils.serialize(use);
        Log.i("useinfo", useinfo);
        Mythread sendThread=new Mythread(useinfo);
        sendThread.start();

    }
    class Mythread extends Thread {
        private String content;
        public Mythread(String s){content=s;}
        @Override
        public void run() {
            try {
                URL url = new URL("http://23.83.240.232:8080/Myser/demo2");
                System.out.println(url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(content.getBytes());
                os.close();
                String result = getResult(conn.getInputStream());
                Log.i("result", result);
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                msg.what = 0002;
                bundle.putString("result", result);
                msg.setData(bundle);
                handler.sendMessage(msg);
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private String getResult(InputStream inputStream) {
            String result = "";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] data = new byte[1024];
            try {
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}


