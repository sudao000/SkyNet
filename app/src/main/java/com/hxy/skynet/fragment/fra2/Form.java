package com.hxy.skynet.fragment.fra2;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxy.skynet.R;
import com.hxy.skynet.adapter.F2LvAdapter;
import com.hxy.skynet.bean.Statebean;
import com.hxy.skynet.depend.MessageEvent;
import com.hxy.skynet.until.Globals;
import com.hxy.skynet.until.JsonUtils;
import com.hxy.skynet.until.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASD on 2016/8/4.
 */
public class Form extends Fragment {
    private ListView lv;
    private F2LvAdapter lvAdapter;
    private List<String> listData = new ArrayList<>();
    private TextView tv_value;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0012){
                lvAdapter.notifyDataSetChanged();
            }

        }
        //接收并显示信息在接收窗口
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f2_form, null, false);
       // init(view);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        if(getUserVisibleHint()){
        if(event.istime&&!event.time.equals("")) {
            getState(event.time);
        }
        }
    }
    @Subscribe
    public  void Event1(Integer position){
        getdata(Globals.dataid);
    }
    private void init(View view) {
        lv = (ListView) view.findViewById(R.id.lvdata);
        tv_value=(TextView)view.findViewById(R.id.tw_con);
        getState("2016-11-01");
        lvAdapter= new F2LvAdapter(listData, getActivity());
        lv.setAdapter(lvAdapter);
    }
    private void getState(String date) {

        String url = getURL(Globals.machineName, date);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                //Log.i("response-------------->", response);
                if (response.contains("state")) {
                    Globals.list_sb.clear();
                    String rs[] = response.split("\\}");
                    for (int i = 1; i < rs.length; i++) {
                        rs[i] = rs[i] + "}";
                        Log.i("rs------->", rs[i]);
                        Statebean sbean = JsonUtils.deserialize(rs[i], Statebean.class);
                        Globals.list_sb.add(sbean);
                    }
                    getdata(Globals.dataid);
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
    private void getdata(int i){
        listData.clear();
        if (Globals.list_sb!=null&&Globals.list_sb.size() != 0) {
            for (int j = 0; j < Globals.list_sb.size(); j++) {
                Statebean statebean = Globals.list_sb.get(j);
                String state[] = statebean.getState().split(",");
                Log.i("state--->",state[i]);
               listData.add(state[i]);
            }
            tv_value.setText("模拟量"+Globals.dataid);
            handler.sendEmptyMessage(0012);
        }

    }
    private String getURL(String machineid, String date) {
        String url = "http://23.83.240.232:8080/Myser/two?form=3&num=" + machineid + "&para=" + date;
        return url;
    }
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }



}
