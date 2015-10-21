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
public class TransferCarrierAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public TransferCarrierAdapter(Context context, List<Map<String, Object>> data){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
    }

    public final class TransferCarrierList{
        public TextView transferCarrier_id;
        public TextView transferCarrier_time;
        public TextView transferCarrier_filename;
        public TextView transferCarrier_responsible;
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
        TransferCarrierList transferCarrierList=null;
        if (convertView==null){
            transferCarrierList=new TransferCarrierList();
            convertView=layoutInflater.inflate(R.layout.item_tracar_layout,null);
            transferCarrierList.transferCarrier_id=(TextView)convertView.findViewById(R.id.tracar_id);
            transferCarrierList.transferCarrier_filename=(TextView)convertView.findViewById(R.id.tracar_carriername);
            transferCarrierList.transferCarrier_time=(TextView)convertView.findViewById(R.id.tracar_time);
            transferCarrierList.transferCarrier_responsible =(TextView)convertView.findViewById(R.id.tracar_responsible);
            convertView.setTag(transferCarrierList);
        }else{
            transferCarrierList=(TransferCarrierList)convertView.getTag();
        }
        transferCarrierList.transferCarrier_id.setText((String) data.get(position).get("TransferCarrier_id"));
        transferCarrierList.transferCarrier_filename.setText((String) data.get(position).get("TransferCarrier_file"));
        transferCarrierList.transferCarrier_time.setText((String) data.get(position).get("TransferCarrier_time"));
        transferCarrierList.transferCarrier_responsible.setText((String) data.get(position).get("TransferCarrier_responsible"));
        return convertView;
    }
}
