package com.imperium;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.imperium.Database.System;

public class SystemsListViewAdapter extends ArrayAdapter {

    private Context mContext;
    private int id;
    private List<System> items;

    public SystemsListViewAdapter(Context context, int textViewResourceId , List<System> list)
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        if(items.get(position) != null )
        {
            System system = items.get(position);

            text.setText(String.format("%s - %s:%s - STATUS: %s",
                    system.getName(), system.getIpAddress(), system.getPort(), system.getStatus()));
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);

            if(system.getStatus().equals("UP")){
                text.setTextColor(Color.GREEN);
            }
            else if(system.getStatus().equals("DOWN")){
                text.setTextColor(Color.RED);
            }
            else{
                text.setTextColor(Color.YELLOW);
            }
        }

        return mView;
    }
}