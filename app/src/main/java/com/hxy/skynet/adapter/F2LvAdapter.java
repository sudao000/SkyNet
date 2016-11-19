package com.hxy.skynet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxy.skynet.R;
import com.hxy.skynet.bean.Statebean;
import com.hxy.skynet.until.Globals;

import java.util.List;

/**
 * Created by ASD on 2016/8/4.
 */
public class F2LvAdapter extends BaseAdapter {
    public List<String> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public F2LvAdapter(List<String> data, Context context) {
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
        String dvalue = data.get(position);
        ViewHolder viewHolder = null;
        Statebean statebean=new Statebean();
        if (position % 2 == 0) {
            convertView = layoutInflater.inflate(R.layout.lvitem, null);
        } else {
            convertView = layoutInflater.inflate(R.layout.lvitem2, null);
        }
        if(Globals.list_sb.size()>0){statebean= Globals.list_sb.get(position);}
        //String a[]=tbyhis.mdate.split("-");
        viewHolder = new ViewHolder();
        viewHolder.txid = (TextView) convertView.findViewById(R.id.tw_id);
        viewHolder.txcon = (TextView) convertView.findViewById(R.id.tw_con);
        viewHolder.txtime = (TextView) convertView.findViewById(R.id.tw_time);
        viewHolder.txdate = (TextView) convertView.findViewById(R.id.tw_date);
        viewHolder.txid.setText(position + "");
        viewHolder.txcon.setText(dvalue);
        viewHolder.txtime.setText(statebean.getTime());
        viewHolder.txdate.setText(statebean.getDate());
        return convertView;
    }

    static class ViewHolder {
        public TextView txid, txcon, txtime, txdate;

    }
}
