package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.List_Adapter;
import com.taek_aaa.goalsnowball.data.Item;

import java.util.ArrayList;

/**
 * Created by taek_aaa on 2017. 2. 10..
 */

public class showListActivity extends Activity {

    private ArrayList<Item> m_arr;
    private List_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist);

        setList();
    }
    private void setList(){
        m_arr = new ArrayList<Item>();
        ListView lv = (ListView)findViewById(R.id.listView1);
        m_arr.add(new Item("@drawable/bulbsuccess","오늘 줄넘기 400개하기","2017/02/10", "+", 5,"Today Mission"));

        adapter = new List_Adapter(showListActivity.this, m_arr);
        lv.setAdapter(adapter);
        //lv.setDivider(null); 구분선을 없에고 싶으면 null 값을 set합니다.
        //lv.setDividerHeight(5); 구분선의 굵기를 좀 더 크게 하고싶으면 숫자로 높이 지정
    }
    public void listUpdate(){
        adapter.notifyDataSetChanged(); //​리스트뷰 값들의 변화가 있을때 아이템들을 다시 배치 할 때 사용되는 메소드입니다.
    }


}
