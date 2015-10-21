package com.printserver.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.printserver.views.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxin on 2015/9/5.
 */
public class UnTaskListAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public ArrayList<String> untaskIds;
    public boolean selectAll;

    public UnTaskListAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.untaskIds=new ArrayList<>();
        this.selectAll=false;
    }

    public final class UnTaskList {
        public CheckBox unTask_check;
        public TextView unTask_id;
        public TextView unTask_file;
        public TextView unTask_time;
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
        UnTaskList unTaskList=null;
        if (convertView==null){
            unTaskList=new UnTaskList();
            convertView=layoutInflater.inflate(R.layout.item_untask_layout,null);
            unTaskList.unTask_check=(CheckBox)convertView.findViewById(R.id.task_checkbox);
            unTaskList.unTask_id=(TextView)convertView.findViewById(R.id.task_id);
            unTaskList.unTask_file=(TextView)convertView.findViewById(R.id.task_filename);
            unTaskList.unTask_time=(TextView)convertView.findViewById(R.id.task_time);
            convertView.setTag(unTaskList);
        }else{
            unTaskList=(UnTaskList)convertView.getTag();
        }
        unTaskList.unTask_check.setTag(data.get(position).get("Task_check").toString());
        unTaskList.unTask_id.setText(String.valueOf(position+1)); //data.get(position).get("Task_id"));
        unTaskList.unTask_file.setText((String) data.get(position).get("Task_filename"));
        unTaskList.unTask_time.setText((String) data.get(position).get("Task_time"));

        unTaskList.unTask_check.setChecked(selectAll);

        unTaskList.unTask_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    untaskIds.add(buttonView.getTag().toString());
                } else {
                    ArrayList<String> out = new ArrayList<String>();
                    for (String tmp : untaskIds
                            ) {
                        if (!tmp.equals(buttonView.getTag().toString())) {
                            out.add(tmp);
                        }
                    }
                    untaskIds = out;
                }
            }
        });
        return convertView;
    }
}
