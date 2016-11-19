package com.hxy.skynet.fragment.fra2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hxy.skynet.R;
import com.hxy.skynet.adapter.LvAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudao on 2016/11/7.
 */
public class Digital extends Fragment {
    private List<String> dataLv;
    private LvAdapter lvAdapter;
    private ListView lv_digital;
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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f2_digital, null, false);
        init(view);
        return view;
    }
    private void init(View v){
        lv_digital=(ListView)v.findViewById(R.id.lv_digital);
        dataLv=new ArrayList<>();
        dataLv.add("1");
        dataLv.add("2");
        dataLv.add("3");
        dataLv.add("4");
        lvAdapter=new LvAdapter(dataLv,getActivity());
        lv_digital.setAdapter(lvAdapter);
        lvAdapter.notifyDataSetChanged();
    }
    @Subscribe
    public void Event1(String s){
        Log.i("digital-even1","执行。。。。。。。。。。。");
        String a[]=s.split(",");
        dataLv.clear();
        for(int i=0;i<a.length;i++){
            dataLv.add("状态量"+i+":"+a[i]);
        }
        lvAdapter.notifyDataSetChanged();

    }

}
