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
 * Created by zhangxin on 2015/9/7.
 */
public class EntrustPrintAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public ArrayList<String> entrustPrintIds;
    public boolean selectAll;

    public EntrustPrintAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        entrustPrintIds = new ArrayList<>();
        selectAll=false;
    }

    public final class EntrustPrintList{
        public CheckBox entrustPrint_ck;
        public TextView entrustPrint_id;
        public TextView entrustPrint_time;
        public TextView entrustPrint_state;
        public TextView entrustPrint_filename;
        public TextView entrustPrint_clint;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        EntrustPrintList entrustPrintList=null;
        if (convertView==null){
            entrustPrintList=new EntrustPrintList();
            convertView=layoutInflater.inflate(R.layout.item_entpri_layout,null);
            entrustPrintList.entrustPrint_ck=(CheckBox)convertView.findViewById(R.id.entpri_choice);
            entrustPrintList.entrustPrint_id=(TextView)convertView.findViewById(R.id.entpri_id);
            entrustPrintList.entrustPrint_time=(TextView)convertView.findViewById(R.id.entpri_time);
            entrustPrintList.entrustPrint_filename=(TextView)convertView.findViewById(R.id.entpri_filename);
            entrustPrintList.entrustPrint_clint=(TextView)convertView.findViewById(R.id.entpri_clint);
            entrustPrintList.entrustPrint_state=(TextView)convertView.findViewById(R.id.entpri_state);
            convertView.setTag(entrustPrintList);
        }else{
            entrustPrintList=(EntrustPrintList)convertView.getTag();
        }

        entrustPrintList.entrustPrint_ck.setTag(data.get(position).get("EntrustPrint_ck").toString());
        entrustPrintList.entrustPrint_id.setText((String) data.get(position).get("EntrustPrint_id"));
        entrustPrintList.entrustPrint_time.setText((String) data.get(position).get("EntrustPrint_time"));
        entrustPrintList.entrustPrint_filename.setText((String) data.get(position).get("EntrustPrint_filename"));
        entrustPrintList.entrustPrint_state.setText((String)data.get(position).get("EntrustPrint_state"));
        entrustPrintList.entrustPrint_clint.setText((String) data.get(position).get("EntrustPrint_clint"));

        entrustPrintList.entrustPrint_ck.setChecked(selectAll);

        entrustPrintList.entrustPrint_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    entrustPrintIds.add(compoundButton.getTag().toString());
                } else {
                    ArrayList<String> out = new ArrayList<String>();
                    for (String tmp : entrustPrintIds
                            ) {
                        if (!tmp.equals(compoundButton.getTag().toString())) {
                            out.add(tmp);
                        }
                    }
                    entrustPrintIds = out;
                }
            }
        });
//        entrustPrintList.entrustPrint_ck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox checkBox = (CheckBox) view;
//                if (checkBox.isChecked()) {
//                    entrustPrintIds.add(checkBox.getTag().toString());
//                } else {
//                    List<String> out = new ArrayList<String>();
//                    for (String tmp : entrustPrintIds
//                            ) {
//                        if (!tmp.equals(checkBox.getTag().toString())) {
//                            out.add(tmp);
//                        }
//                    }
//                    entrustPrintIds = out;
//                }
//            }
//        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox checkBox = (CheckBox) view.findViewById(R.id.entpri_choice);
//                if (checkBox.isChecked()){
//                    checkBox.setChecked(false);
//                }else {
//                    checkBox.setChecked(true);
//                }
//            }
//        });

        return convertView;
    }
}
