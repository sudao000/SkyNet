package com.hxy.skynet.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hxy.skynet.R;
import com.hxy.skynet.adapter.TabAdapter;
import com.hxy.skynet.depend.DatePickerFragment;
import com.hxy.skynet.depend.MessageEvent;
import com.hxy.skynet.depend.StaticViewPager;
import com.hxy.skynet.fragment.fra2.Chart;
import com.hxy.skynet.fragment.fra2.Digital;
import com.hxy.skynet.fragment.fra2.Form;
import com.hxy.skynet.until.Globals;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudao on 2016/10/12.
 */
public class Fragment2 extends Fragment implements View.OnClickListener{
    private RelativeLayout rl;
    private int mYear, mMonth, mDay;
    private List<String> dataSp;
    private ArrayAdapter<String> spAdapter;
    private EditText et_limit,et_time;
    private Button bt_limit,bt_time;
    private TabLayout tabtitle;
    private StaticViewPager vpcontent;
    private TabAdapter fAdapter;
    private List<Fragment> list_fs;
    private List<String> list_title;
    private Spinner sp_machine;
    private Form f2;private Chart f1;private Digital f3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        initControls(view);
        return view;
    }

    public void initControls(View view){
        rl=(RelativeLayout)view.findViewById(R.id.rl000);
        et_limit=(EditText)view.findViewById(R.id.et_limit0);
        et_time=(EditText)view.findViewById(R.id.et_time0);
        bt_limit=(Button)view.findViewById(R.id.bt_limit0);
        bt_time=(Button)view.findViewById(R.id.bt_time0);
        tabtitle=(TabLayout)view.findViewById(R.id.cv_title);
        vpcontent=(StaticViewPager)view.findViewById(R.id.vp_data);
        sp_machine=(Spinner)view.findViewById(R.id.sp_machine);
        f1=new Chart();f2=new Form();f3=new Digital();
        list_fs=new ArrayList<>();
        list_fs.add(f1); list_fs.add(f2);list_fs.add(f3);
        list_title=new ArrayList<>();
        list_title.add("折线图");list_title.add("数据表");list_title.add("状态量");
        //设置TabLayout模式
        tabtitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        fAdapter=new TabAdapter(getChildFragmentManager(),list_fs);
        fAdapter.setTabTitle(list_title);
        vpcontent.setAdapter(fAdapter);//为viewpager添加适配器
        tabtitle.setupWithViewPager(vpcontent);
        //Tablayout关联viewpager
        vpcontent.setOffscreenPageLimit(4);
        bt_limit.setOnClickListener(this);
        bt_time.setOnClickListener(this);
        et_time.setOnClickListener(this);
        dataSp=new ArrayList<>();
        for(int i=0;i<8;i++){
            dataSp.add("模拟量"+i);
        }
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
                if (sp_machine.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(Color.rgb(153, 62, 26));
                    check.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_015);
                } else {
                    view.setBackgroundColor(Color.rgb(61, 150, 200));
                    check.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_000);
                }

                return view;
            }
        };
        spAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        sp_machine.setAdapter(spAdapter);
        sp_machine.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer inl=position;
                Globals.dataid=position;
                EventBus.getDefault().post(inl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_limit0:
               limit();
                break;
            case R.id.bt_time0:
                time();
                break;
            case R.id.et_time0:
                ettime();
                break;
        }
    }
    private void limit(){
        MessageEvent me=new MessageEvent();
        me.limit=et_limit.getText().toString();
        me.islimit=true;
        me.istime=false;
        EventBus.getDefault().post(me);
    }
    private void time(){
        MessageEvent me=new MessageEvent();
        me.time=et_time.getText().toString();
        me.islimit=false;
        me.istime=true;
        EventBus.getDefault().post(me);
    }
    private void ettime(){
        DatePickerFragment dateDialog = new DatePickerFragment();
        dateDialog.setOstm(new DatePickerFragment.OnSetTime() {
            @Override
            public void acceptTime(int a, int b, int c) {
                String day="0";
                mYear = a;
                mMonth = b;
                mDay = c;
                if(mDay<10){day="0"+mDay;}else {day=mDay+"";}
                et_time.setText(new StringBuilder().append(mYear).append("-")
                        .append(mMonth).append("-").append(day));
                //Log.i("found:",mYear+"-"+mMonth+"-"+mDay);
            }
        });
        dateDialog.show(getFragmentManager(), "dateDialog");
    }
}
