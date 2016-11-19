package com.hxy.skynet.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hxy.skynet.MainActivity;
import com.hxy.skynet.R;


public class WelcomeActivity extends Activity{
    /** Called when the activity is first created. */

	private Handler mHandler;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initView();
    }
    
    public void initView()
    {
		Info.sp=getSharedPreferences("userinfo",MODE_PRIVATE);
    	mHandler = new Handler();
    	if (false)
    	{
    		mHandler.postDelayed(new Runnable() {
    			@Override
    			public void run() {
    				connectToLogin();
    			}
        	}, 2000);
    	}
    	else {
    		mHandler.postDelayed(new Runnable() {
    			@Override
    			public void run() {
    				goLoginActivity();
    			}
    		}, 2000);
    	}
    }
    
    public void goLoginActivity()
    {
    	Intent intent = new Intent();
    	intent.setClass(this, LoginActivity.class);		
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	finish();
    }

    public void connectToLogin(){
		Intent intent=new Intent(this, MainActivity.class);
		startActivity(intent);
    }
    
	@Override
	protected void onRestart() {

		super.onRestart();
		finish();
	}
	
	@Override
	protected void onResume() {

		super.onResume();
	}
	
}