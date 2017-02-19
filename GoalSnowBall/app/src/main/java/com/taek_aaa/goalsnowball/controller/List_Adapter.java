package com.taek_aaa.goalsnowball.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.data.Item;

import java.util.ArrayList;

/**
 * Created by taek_aaa on 2017. 2. 10..
 */
public class List_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity m_activity;
    private ArrayList<Item> arr;

    public List_Adapter(Activity act, ArrayList<Item> arr_item) {
        this.m_activity = act;
        arr = arr_item;
        mInflater = (LayoutInflater)m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            int res = 0;
            res = R.layout.list_item;
            convertView = mInflater.inflate(res, parent, false);
        }
        ImageView imView = (ImageView)convertView.findViewById(R.id.vi_image);
        TextView goaltv = (TextView)convertView.findViewById(R.id.vi_goal);
        TextView datetv = (TextView)convertView.findViewById(R.id.vi_date);
        TextView goldStatus = (TextView)convertView.findViewById(R.id.goldGetOrLoose);
        TextView goldContent = (TextView)convertView.findViewById(R.id.goldContents);
        TextView whatDatetv =(TextView)convertView.findViewById(R.id.vi_dateStatus);
        TextView currentAmount = (TextView)convertView.findViewById(R.id.listCurrentdata);
        TextView goalAmount = (TextView)convertView.findViewById(R.id.listGoaldata);
        TextView just_gold = (TextView)convertView.findViewById(R.id.list_gold);

        //ScrollView layout_view =  (ScrollView) convertView.findViewById(R.id.vi_view);
        int resId=  m_activity.getResources().getIdentifier(arr.get(position).bulbImageItem, "drawable", m_activity.getPackageName());

        imView.setBackgroundResource(resId);
        goaltv.setText(arr.get(position).goalItem);
        datetv.setText(arr.get(position).dateItem);
        goldStatus.setText(arr.get(position).goldItem);
        goldContent.setText(""+arr.get(position).goldContent);
        whatDatetv.setText(arr.get(position).whatdatetype);
        currentAmount.setText(""+arr.get(position).currentAmountItem);
        goalAmount.setText(""+arr.get(position).goalAmounItem);

        if((arr.get(position).bulbImageItem).equals("@drawable/bulbfail")){
            goldStatus.setTextColor(Color.parseColor("#FF1C00"));
            goldContent.setTextColor(Color.parseColor("#FF1C00"));
            just_gold.setTextColor(Color.parseColor("#FF1C00"));
        }else if((arr.get(position).bulbImageItem).equals("@drawable/bulbsuccess")){
            goldStatus.setTextColor(Color.parseColor("#93C972"));
            goldContent.setTextColor(Color.parseColor("#93C972"));
            just_gold.setTextColor(Color.parseColor("#93C972"));
        }else{
            goldStatus.setTextColor(Color.parseColor("#808080"));
            goldContent.setTextColor(Color.parseColor("#808080"));
            just_gold.setTextColor(Color.parseColor("#808080"));
        }
        return convertView;
    }
}