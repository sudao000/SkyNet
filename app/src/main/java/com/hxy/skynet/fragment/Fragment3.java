package com.hxy.skynet.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hxy.skynet.R;
import com.hxy.skynet.adapter.WarnAdapter;
import com.hxy.skynet.bean.WarnMsgbean;
import com.hxy.skynet.depend.DatePickerFragment;
import com.hxy.skynet.until.Globals;
import com.hxy.skynet.until.JsonUtils;
import com.hxy.skynet.until.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudao on 2016/10/12.
 */
public class Fragment3 extends Fragment {
    private RelativeLayout rl;
    private int mYear, mMonth, mDay;
    boolean isnow;
    private Button bt_now;
    private ListView lv;
    private EditText et_time;
    private List<WarnMsgbean> data_list;
    private WarnAdapter mWarnAdapter;


    private Handler handler = new Handler() {
        //handler跨线程通讯才用到。用于子线程向主线程通讯。
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0011) {
                mWarnAdapter.notifyDataSetChanged();
                Log.i("mWarnAdapter","");
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
       // EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
       // EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null, false);
        init(view);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rl.requestFocus();
                return false;
            }
        });
        bt_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        et_time.setOnClickListener(new View.OnClickListener() {// 为时间文本框设置单击监听事件
            @Override
            public void onClick(View arg0) {
                DatePickerFragment dateDialog = new DatePickerFragment();
                dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
                    @Override
                    public void acceptTime(int a, int b, int c) {
                        String day="";
                        mYear = a;
                        mMonth = b;
                        mDay = c;
                        if(c<10){
                            day="0"+mDay;
                        }
                        String s=mYear+"-"+mMonth+"-"+day;
                        et_time.setText(s);
                        getState(s);
                    }
                });
                dateDialog.show(getFragmentManager(), "dateDialog");
            }
        });

        return view;
    }

    public void init(View view) {
        isnow = true;
        bt_now = (Button) view.findViewById(R.id.bt_now1);
        lv = (ListView) view.findViewById(R.id.lv_warn);
        et_time = (EditText) view.findViewById(R.id.et_time1);
        rl = (RelativeLayout) view.findViewById(R.id.rl001);
        data_list=new ArrayList<>();
        mWarnAdapter=new WarnAdapter(data_list,getActivity());
        lv.setAdapter(mWarnAdapter);

    }
    private void getState(String date) {
        Globals.list_sb.clear();
        String url = getURL(Globals.machineName, date);
        Log.i("url",url);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                //Log.i("response-------------->", response);
                if (response.contains("type")) {
                    data_list.clear();
                    String rs[] = response.split("\\}");
                    for (int i = 1; i < rs.length; i++) {
                        rs[i] = rs[i] + "}";
                        Log.i("rs------->", rs[i]);
                        WarnMsgbean wbean = JsonUtils.deserialize(rs[i], WarnMsgbean.class);
                        data_list.add(wbean);
                    }
                    handler.sendEmptyMessage(0011);
                } else {
                    toast("数据加载失败！");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("load  failure", e.toString());
            }
        };
        OkHttpUtils.get(url, callback);


    }
    private String getURL(String machineid, String date) {
        String url = "http://23.83.240.232:8080/Myser/two?form=6&num=" + machineid + "&para=" + date;
        return url;
    }
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
   /* @Subscribe
    public void onEvent(Tbwarn tbwarn) {
        warnAdapter2.data.add(tbwarn);
        if(isnow){handler.sendEmptyMessage(0012);}

    }*/
}
