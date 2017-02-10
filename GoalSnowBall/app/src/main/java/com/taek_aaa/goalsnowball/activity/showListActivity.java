package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.List_Adapter;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.Item;
import com.taek_aaa.goalsnowball.data.ListViewData;

import java.util.ArrayList;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.listViewPosition;

/**
 * Created by taek_aaa on 2017. 2. 10..
 */

public class showListActivity extends Activity {

    private ArrayList<Item> m_arr;
    private List_Adapter adapter;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist);
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);

        setList();
    }

    private void setList() {
        m_arr = new ArrayList<Item>();
        ListView lv = (ListView) findViewById(R.id.listView1);

        listViewPosition = dbManager.getLastPosition();

        for (int i = 0; i < 7; i++) {
            if (listViewPosition < 0) {
                Toast.makeText(this, "마지막 데이터입니다.",Toast.LENGTH_SHORT).show();
            } else {

                Item item = convertData(dbManager.getPreviousListViewData(listViewPosition));
                m_arr.add(item);
            }

            listViewPosition--;
        }


        //m_arr.add(new Item("@drawable/bulbsuccess", "오늘 줄넘기 400개하기", "2017/02/10", "+", 5, "Today Mission"));

        adapter = new List_Adapter(showListActivity.this, m_arr);
        lv.setAdapter(adapter);
        lv.setDividerHeight(5);
    }

    public void listUpdate() {
        adapter.notifyDataSetChanged(); //​리스트뷰 값들의 변화가 있을때 아이템들을 다시 배치 할 때 사용되는 메소드입니다.
    }


    public Item convertData(ListViewData listViewData){


        String bulbImage="";
        String looseOrGet="";
        String whatDateType="";

        if(listViewData.lvSuccess==1){  //성공
            bulbImage += "@drawable/bulbsuccess";
            looseOrGet="+ ";
        }else if(listViewData.lvSuccess==2){  //하는중
            bulbImage += "@drawable/bulbdoing";
            looseOrGet="미획득 ";
        }else{      // 실패
            bulbImage += "@drawable/bulbfail";
            looseOrGet="- ";
        }

        if(listViewData.lvDateType==FROM_TODAY){
            whatDateType="Today Mission";
        }else if(listViewData.lvDateType==FROM_WEEK){
            whatDateType="Week Mission";
        }else{
            whatDateType="Month Mission";
        }


        Item result = new Item(bulbImage,listViewData.lvgoal,listViewData.lvDate,looseOrGet,listViewData.lvBettingGold,whatDateType);


        return result;
    }

}
