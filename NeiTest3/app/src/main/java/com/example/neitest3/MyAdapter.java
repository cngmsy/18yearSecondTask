package com.example.neitest3;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 赵辉 on 2018/1/21.
 */

public class MyAdapter extends BaseAdapter {
    Context context;
    List<ProcessInfo> tempList;

    public MyAdapter(Context context, List<ProcessInfo> tempList) {
        this.context = context;
        this.tempList = tempList;
    }

    @Override
    public int getCount() {
        return tempList.size();
    }

    @Override
    public Object getItem(int i) {
        return tempList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_processinfo, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_name.setText(tempList.get(i).name);
        holder.iv_icon.setImageDrawable(tempList.get(i).icon);
        holder.tv_memory.setText(Formatter.formatFileSize(
                context, tempList.get(i).memory));
        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv_memory;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_icon = (ImageView) rootView.findViewById(R.id.iv_icon);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_memory = (TextView) rootView.findViewById(R.id.tv_memory);
        }

    }
}
