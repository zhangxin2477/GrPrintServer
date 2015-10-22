package com.printserver.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.printserver.views.R;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/7.
 */
public class DoTaskListAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public DoTaskListAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }

    public final class DoTaskList{
        public TextView doTask_id;
        public TextView doTask_time;
        public TextView doTask_filename;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DoTaskList doTaskList=null;
        if (convertView==null){
            doTaskList=new DoTaskList();
            convertView=layoutInflater.inflate(R.layout.item_task_layout,null);
            doTaskList.doTask_id=(TextView)convertView.findViewById(R.id.task_id);
            doTaskList.doTask_filename=(TextView)convertView.findViewById(R.id.task_filename);
            doTaskList.doTask_time=(TextView)convertView.findViewById(R.id.task_time);
            convertView.setTag(doTaskList);
        }else{
            doTaskList=(DoTaskList)convertView.getTag();
        }
        doTaskList.doTask_id.setText(String.valueOf(position+1)); //data.get(position).get("Task_id"));
        doTaskList.doTask_filename.setText((String) data.get(position).get("Task_filename"));
        doTaskList.doTask_time.setText((String) data.get(position).get("Task_time"));
        return convertView;
    }
}
