package com.hxy.skynet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxy.skynet.R;
import com.hxy.skynet.bean.WarnMsgbean;

import java.util.List;


/**
 * Created by Sudao on 2016/8/19.
 */
public class WarnAdapter extends BaseAdapter{
    public List<WarnMsgbean> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public WarnAdapter(List<WarnMsgbean> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       WarnMsgbean warnMsgbean = data.get(position);
        ViewHolder viewHolder = null;
        convertView = layoutInflater.inflate(R.layout.warnlistitem, null);
        String a[]=warnMsgbean.getDate().split("-");
        viewHolder = new ViewHolder();
        viewHolder.txid = (TextView) convertView.findViewById(R.id.tw_id);
        viewHolder.txwarn = (TextView) convertView.findViewById(R.id.tw_con);
        viewHolder.txtime = (TextView) convertView.findViewById(R.id.tw_time);
        viewHolder.txdate = (TextView) convertView.findViewById(R.id.tw_date);
        viewHolder.txid.setText(position + "");
       /* if(warnMsgbean.type==0){
            viewHolder.txwarn.setText("压力过高");
        }
        switch (warnMsgbean.getType()){
            case 1:
        }
        else {viewHolder.txwarn.setText("电导率超标");}*/
        viewHolder.txwarn.setText(warnMsgbean.getType());
        viewHolder.txtime.setText(warnMsgbean.getTime());
        viewHolder.txdate.setText(a[1]+"-"+a[2]);
        return convertView;
    }

    static class ViewHolder {
        public TextView txid, txwarn, txtime, txdate;

    }
}
