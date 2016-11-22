package com.hxy.skynet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hxy.skynet.Login.LoginActivity;


public class SplashActivity extends AppCompatActivity {
    private SharedPreferences userInfo;
    private String name;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
           /* if(msg.what==1){
                if(name==null){
                    Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }*/
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userInfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        name=userInfo.getString("name",null);
        Log.i("name-->",name+"为空");
        new Thread(new Mythread()).start();

    }
    class  Mythread implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(1);

        }
    }

}
