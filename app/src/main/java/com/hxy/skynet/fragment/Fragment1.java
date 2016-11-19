package com.hxy.skynet.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hxy.skynet.R;
import com.hxy.skynet.adapter.LvAdapter;
import com.hxy.skynet.bean.Machine;
import com.hxy.skynet.bean.Tnow;
import com.hxy.skynet.until.Globals;
import com.hxy.skynet.until.JsonUtils;
import com.hxy.skynet.until.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sudao on 2016/10/12.
 */
public class Fragment1 extends Fragment {
    private Spinner spMachine;
    private List<String> dataSp, dataLv;
    private ArrayAdapter<String> spAdapter;
    private LvAdapter lvAdapter;
    private TextView tvtset, tvname, tvid, tvadd, tvstate, tvprimary;
    private ImageView iv01, iv02;
    private RelativeLayout rlState;
    private String name;
    private String URL = "http://23.83.240.232:8080/Myser/two?para=2016-10-26&form=1&num=";
    private SharedPreferences useinfo;
    private SharedPreferences machineinfo;
    private Timer mTimer;
    private ListView lvinfo;
    boolean isexist;
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    String result = msg.getData().getString("data");
                    dataLv.clear();
                    doResult(result);
                    //Log.i("循环次数：",".............");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        init(view);
        test();
        return view;
    }

    private void init(View v) {
        useinfo = getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE);
        spMachine = (Spinner) v.findViewById(R.id.sp_machine);
        tvtset = (TextView) v.findViewById(R.id.tv_test);
        iv01 = (ImageView) v.findViewById(R.id.iv01);
        iv02 = (ImageView) v.findViewById(R.id.iv02);
        rlState = (RelativeLayout) v.findViewById(R.id.rl_state);
        tvname = (TextView) v.findViewById(R.id.tv_name);
        tvid = (TextView) v.findViewById(R.id.tv_id);
        tvprimary = (TextView) v.findViewById(R.id.tv_pra);
        tvadd = (TextView) v.findViewById(R.id.tv_add);
        tvstate = (TextView) v.findViewById(R.id.tv_state);
        lvinfo = (ListView) v.findViewById(R.id.lv_info);
        mTimer = new Timer();
        dataLv = new ArrayList<String>();
        dataSp = new ArrayList<String>();
        dataSp.add("sclyj01001");
        dataSp.add("jce01005");
        dataSp.add("qxshy01001");
        dataSp.add("axdca02001");
        spAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_checked_text, dataSp) {
            LayoutInflater mInflater = getActivity().getLayoutInflater();

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {

                View view = mInflater.inflate(R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                ImageView check = (ImageView) view
                        .findViewById(R.id.spinner_item_checked_image);
                label.setText(dataSp.get(position));
                if (spMachine.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(Color.rgb(153, 62, 26));
                    check.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
                } else {
                    view.setBackgroundColor(Color.rgb(61, 150, 200));
                    check.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_000);
                }

                return view;
            }
        };
        lvAdapter = new LvAdapter(dataLv, getActivity());
        spAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spMachine.setAdapter(spAdapter);
        lvinfo.setAdapter(lvAdapter);
        lvinfo.setDivider(null);
        spMachine.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = dataSp.get(position);
                getData(URL + value);
                Globals.machineName=value;
                tvtset.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Mytask mtsak = new Mytask();
        //mTimer.schedule(mtsak,1000,3000);

    }

    private void test() {
        iv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv01.setVisibility(View.INVISIBLE);
                rlState.setVisibility(View.VISIBLE);
            }
        });
        rlState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlState.setVisibility(View.INVISIBLE);
                iv01.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getData(String url) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                Log.i("response", response);
                if (response.contains("summary")) {
                    Machine machine = new Machine();
                    machine = JsonUtils.deserialize(response, Machine.class);

                    tvid.setText(machine.getNum());
                    tvprimary.setText(machine.getSummary());
                    tvname.setText(machine.getName());
                    isexist = true;
                    Globals.machineDdata=machine.getDparm();
                    Globals.machineAdata=machine.getAparm();
                    if (useinfo != null) {
                        String s1 = useinfo.getString("address", "");
                        Log.i("s1", s1);
                        tvadd.setText(s1);
                    }
                } else {
                    reSetTv();
                    isexist = false;
                    toast("该编号设备不存在！");
                }
                test2();
            }

            @Override
            public void onFailure(Exception e) {
                Log.i("load  failure", e.toString());
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);

    }

    private void reSetTv() {
        tvid.setText("0000");
        tvprimary.setText("0000");
        tvname.setText("0000");
        tvadd.setText("0000");
        tvstate.setText("0000");
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

    }

    class Mytask extends TimerTask {
        String URL = "http://23.83.240.232:8080/Myser/two?num=sclyj01001&form=4&para=2016-10-26";

        @Override
        public void run() {
            test2();
            /*OkHttpUtils.ResultCallback<String> loadTnowCallback = new OkHttpUtils.ResultCallback<String>() {

                @Override
                public void onSuccess(String response) {
                    Log.i("response",response);
                    Message msg = doActionHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    msg.what = 1;
                    bundle.putString("data", response);
                    msg.setData(bundle);
                    doActionHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.i("load  failure", e.toString());
                }
            };
            OkHttpUtils.get(URL, loadTnowCallback);*/

        }
    }

    private void test2() {
        String s0 = "{\"astate\":\"2.3,3.6,6.2,63,23,21,53,62,15\",\"dstate\":\"1,0,0,1,0,1,0,1\",\"id\":\"1\"}";
        Message msg = doActionHandler.obtainMessage();
        Bundle bundle = new Bundle();
        msg.what = 1;
        bundle.putString("data", s0);
        msg.setData(bundle);
        doActionHandler.sendMessage(msg);
    }

    private void doResult(String s) {
        Tnow tnow = new Tnow();
        tnow = JsonUtils.deserialize(s, Tnow.class);
        String s2 = tnow.getDstate();
        EventBus.getDefault().post(s2);
        Log.i("Fragment1-s2-->",s2);
        String a[] = new String[20];
        a = s2.split(",");
        if (isexist) {
            if (a[0].equals("1")) {
                tvstate.setText("运行中");
            } else {
                tvstate.setText("停止中");
            }
        } else {
            tvstate.setText("0000");
        }
        String b[] = tnow.getAstate().split(",");
        for (int i = 0; i < b.length; i++) {
            dataLv.add("模拟量" + i + ": " + b[i]);
        }
        lvAdapter.notifyDataSetChanged();
    }
}
