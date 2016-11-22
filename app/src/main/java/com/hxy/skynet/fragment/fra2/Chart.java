package com.hxy.skynet.fragment.fra2;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hxy.skynet.R;
import com.hxy.skynet.bean.Statebean;
import com.hxy.skynet.depend.MessageEvent;
import com.hxy.skynet.until.Globals;
import com.hxy.skynet.until.JsonUtils;
import com.hxy.skynet.until.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by ASD on 2016/8/4.
 */
public class Chart extends Fragment {
    private LineChart mLinechart;
    private LineData mLinedata;
    private ArrayList<String> xVals;
    private LineDataSet dataSet1;
    private ArrayList<Entry> lineY1;
    private YAxis leftAxis;
    private MessageEvent mevent;
    // private boolean isnow;
    // private List<Statebean> list_sb;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1002) {
                mLinechart.setData(mLinedata);
                mLinechart.fitScreen();
                mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
                mLinechart.moveViewToX(mLinedata.getXValCount() - 1);//设置折线移动
            }
            if (msg.what == 1003) {
                leftAxis.removeAllLimitLines();
                float lim = Float.parseFloat(mevent.limit);
                LimitLine yLimitLine = new LimitLine(lim, "上限:" + lim);
                yLimitLine.setLineColor(Color.RED);
                yLimitLine.setTextColor(Color.RED);
                yLimitLine.setTextSize(8f);
                leftAxis.addLimitLine(yLimitLine);
            }
            mLinechart.notifyDataSetChanged();
            mLinechart.invalidate();

        }
        //接收并显示信息在接收窗口
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.i("onStart","-------------------------------");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.i("onStop","-------------------------------");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.f2_chart, null, false);
        init(view);
        initline();
        return view;
    }

    @Subscribe
    public void Event1(MessageEvent event) {
        mevent = event;
        if (getUserVisibleHint()) {
            if (mevent.islimit && !mevent.limit.equals("")) {
                handler.sendEmptyMessage(1003);
                //Toast.makeText(getActivity(), "限制线修改成功！", Toast.LENGTH_SHORT).show();
            }
            if (mevent.istime && !mevent.time.equals("")) {
                //Log.i("mevent.time---->", mevent.time);
                getState(mevent.time);
                // Toast.makeText(getActivity(), mevent.time + "数据展示！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Subscribe
    public void Event2(Integer position) {
        // Log.i("Event-position-->", position + "");
        getData(position);
    }

    private void init(View view) {
        mLinechart = (LineChart) view.findViewById(R.id.lc_con);
    }

    private void initline() {
        xVals = new ArrayList<String>();
        lineY1 = new ArrayList<Entry>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSet1 = new LineDataSet(lineY1, "模拟量");
        dataSet1.setColor(Color.parseColor("#058E79"));//设置折线颜色
        dataSet1.setLineWidth(1.5f);//设置折线宽度
        dataSet1.setCircleRadius(2f);//设置圆圈大小
        dataSet1.setValueTextSize(8);//设置显示该点值文字大小。
        dataSet1.setCircleColor(Color.parseColor("#F26077"));//设置圆圈颜色
        mLinedata = new LineData(xVals, dataSet1);
        mLinechart.setDescription("模拟量0");
        mLinechart.setDescriptionPosition(200f, 50f);
        mLinechart.setDescriptionTextSize(12);
        mLinechart.animateY(500);// 动画
        mLinechart.setTouchEnabled(true);// 设置是否可以触摸
        mLinechart.setDragEnabled(true);// 是否可以拖拽
        mLinechart.setScaleEnabled(false);// 是否可以缩放
        mLinechart.setPinchZoom(false);// 集双指缩放
        mLinechart.getAxisRight().setEnabled(false);// 隐藏右边的坐标轴
        Legend mLegend = mLinechart.getLegend();
        mLegend.setForm(Legend.LegendForm.SQUARE);// 设置比例图标示// 设置窗体样式
        // mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);设置比例图位置
        mLegend.setFormSize(4f);// 字体
        mLegend.setTextColor(Color.parseColor("#000000"));// 字体颜色
        XAxis xAxis = mLinechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签位于图标下边。
        xAxis.setDrawGridLines(false);//不画网格线
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setAxisLineColor(Color.BLACK);
        //xAxis.setLabelsToSkip(0);//忽略的标签数
        leftAxis = mLinechart.getAxisLeft();// 获得左侧侧坐标轴
        leftAxis.setAxisMaxValue(25f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisLineColor(Color.BLACK);

        LimitLine yLimitLine = new LimitLine(10f, "上限：10.0");
        yLimitLine.setLineColor(Color.RED);
        yLimitLine.setTextColor(Color.RED);
        yLimitLine.setTextSize(8f);
        leftAxis.addLimitLine(yLimitLine);
        getState("2016-11-08");
        mLinechart.setViewPortOffsets(60, 50, 80, 110);
        mLinechart.setBackgroundColor(Color.parseColor("#BE4C7BA4"));
        mLinechart.setVisibleXRangeMaximum(6);//设置X轴最多显示数据7个
        mLinechart.moveViewToX(mLinedata.getXValCount() - 1);//设置折线移动

    }


    public void getState(String date) {

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
                    getData(Globals.dataid);
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

    public String getURL(String machineid, String date) {
        String url = "http://23.83.240.232:8080/Myser/two?form=3&num=" + machineid + "&para=" + date;
        return url;
    }

    public void getData(int i) {
        lineY1.clear();
        xVals.clear();
        if (Globals.list_sb != null && Globals.list_sb.size() != 0) {
            for (int j = 0; j < Globals.list_sb.size(); j++) {
                Statebean statebean = Globals.list_sb.get(j);
                String state[] = statebean.getState().split(",");
                float f = Float.parseFloat(state[i]);
                //Log.i("f--------------->",f+"");
                lineY1.add(new Entry(f, xVals.size()));
                xVals.add(statebean.getTime());

            }
            mLinechart.setDescription("模拟量"+Globals.dataid);
            handler.sendEmptyMessage(1002);
        }
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
