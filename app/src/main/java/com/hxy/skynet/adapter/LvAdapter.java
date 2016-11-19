package com.hxy.skynet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxy.skynet.R;

import java.util.List;

/**
 * Created by ASD on 2016/8/4.
 */
public class LvAdapter extends BaseAdapter {
    public List<String> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public LvAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    public int getCount() {
        int count=data.size()/2;
        if(data.size()%2==0){
            return count;
        }else {
            return count+1;
        }
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String left="";//data.get(position);
        String right="";
        int intl=2*position;
        int intr=intl+1;
        if(data.size()>0){
            left=data.get(intl);
            if(intr<data.size()){
                right=data.get(intr);
            }
        }
      // if(2*(position)+1<data.size()) { right=data.get(2*(position)+1);}
        ViewHolder viewHolder = null;
        convertView = layoutInflater.inflate(R.layout.lvitemf1, null);
        viewHolder = new ViewHolder();
        viewHolder.tvleft= (TextView) convertView.findViewById(R.id.tv_left);
        viewHolder.tvright = (TextView) convertView.findViewById(R.id.tv_right);
        viewHolder.tvleft.setText(left);
        viewHolder.tvright.setText(right);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvleft, tvright;

    }
}
