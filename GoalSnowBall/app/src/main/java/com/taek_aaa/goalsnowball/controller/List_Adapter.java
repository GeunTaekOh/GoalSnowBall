package com.taek_aaa.goalsnowball.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        //설명 1:
        mInflater =
                (LayoutInflater)m_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    //설명 2:
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
    //설명 3:
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



        LinearLayout layout_view =  (LinearLayout)convertView.findViewById(R.id.vi_view);
        int resId=  m_activity.getResources().getIdentifier(arr.get(position).bulbImageItem, "drawable", m_activity.getPackageName());

        //imView.setImageResource(resId);
        imView.setBackgroundResource(resId);
        goaltv.setText(arr.get(position).goalItem);
        datetv.setText(arr.get(position).dateItem);
        goldStatus.setText(arr.get(position).goldItem);
        goldContent.setText(""+arr.get(position).goldContent);
        whatDatetv.setText(arr.get(position).whatdatetype);


  /*  버튼에 이벤트처리를 하기위해선 setTag를 이용해서 사용할 수 있습니다.
       *   Button btn 가 있다면, btn.setTag(position)을 활용해서 각 버튼들
       *   이벤트처리를 할 수 있습니다.
   */
        layout_view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
              //  GoIntent(position);
            }
        });
        return convertView;
    }

    //설명 4:
   /* public void GoIntent(int a){
        Intent intent = new Intent(m_activity, "가고싶은 클래스".class);
        //putExtra 로 선택한 아이템의 정보를 인텐트로 넘겨 줄 수 있다.
        intent.putExtra("TITLE", arr.get(a).goalItem);
        intent.putExtra("EXPLAIN", arr.get(a).dateItem);
        m_activity.startActivity(intent);
    }*/
}