package com.tapping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapping.R;
import com.tapping.dto.ItemDto;

import java.util.ArrayList;

public class ReceiptAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ItemDto> sample;

    public ReceiptAdapter(Context context, ArrayList<ItemDto> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ItemDto getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.listitem2, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.itemimage2);
        TextView itemname = (TextView)view.findViewById(R.id.itemname2);
        TextView itemprice = (TextView)view.findViewById(R.id.itemtotalprice2);
        Log.i("zzzzzzzz",sample.get(position).getName());
        imageView.setImageBitmap(sample.get(position).getBm());
        itemname.setText(sample.get(position).getName());
        itemprice.setText(sample.get(position).getPrice()*sample.get(position).getCount()+"원");
        return view;
    }
}