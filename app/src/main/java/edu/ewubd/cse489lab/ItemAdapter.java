package edu.ewubd.cse489lab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;
    private ArrayList<Item> records;
    public ItemAdapter(Context context, ArrayList<Item> records){
        super(context, -1, records);
        this.records = records;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View template = inflater.inflate(R.layout.row_item, parent, false);
        TextView tvSN = template.findViewById(R.id.tvSN);
        TextView tvItemName = template.findViewById(R.id.tvItemName);
        TextView tvDate = template.findViewById(R.id.tvDate);
        TextView tvCost = template.findViewById(R.id.tvCost);

        tvSN.setText(String.valueOf(position+1));
        tvItemName.setText(records.get(position).itemName);
        tvDate.setText(getFormattedDate(records.get(position).date));
        tvCost.setText(String.valueOf(records.get(position).cost));

        return template;
    }
    private String getFormattedDate(long milliseconds){
        Date date = new Date(milliseconds); // code to convert milliseconds to date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String Date = sdf.format(date);
        return Date;
    }
}
