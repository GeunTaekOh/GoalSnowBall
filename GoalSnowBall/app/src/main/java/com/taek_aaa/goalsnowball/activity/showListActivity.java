package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
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
    TextView listtv, countListtv;
    int amountOfShowList = 10;
    ListView lv;
    private static int amountOfEvery;
    private static int amountOfDraw;
    private static int pos;
    Boolean toastFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist);
        dbManager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#99BADD"));
        }
        listtv = (TextView) findViewById(R.id.listTopTv);
        countListtv = (TextView) findViewById(R.id.countListtv);
        if (amountOfShowList > dbManager.getLastPosition()) {
            amountOfShowList = dbManager.getLastPosition();
        }
        lv = (ListView) findViewById(R.id.listView1);

        if (dbManager.isEmptyDB()) {
            lv.setBackgroundResource(R.drawable.empty2);
        }
        countListtv.setText("" + amountOfShowList + " / " + "" + dbManager.getLastPosition());
        amountOfEvery = dbManager.getLastPosition();

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                int threshold = 1;
                int count = lv.getCount();
                if (i == SCROLL_STATE_IDLE) {
                    if (lv.getLastVisiblePosition() >= count - threshold) {
                        if(m_arr.size()==amountOfEvery){
                            if(toastFlag) {
                                Toast.makeText(showListActivity.this, "마지막 데이터입니다.", Toast.LENGTH_SHORT).show();
                                toastFlag=false;
                            }
                        }else {
                            setListMore();
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        if(amountOfDraw<=amountOfEvery) {
            setList();
        }


    }


    private void setList() {
        Log.e("dhrms", "setList에 들어옴");
        m_arr = new ArrayList<Item>();
        lv = (ListView) findViewById(R.id.listView1);

        listViewPosition = dbManager.getLastPosition();

        for (int i = 0; i < amountOfShowList; i++) {
            if (listViewPosition <= 0) {
                Toast.makeText(this, "" + dbManager.getLastPosition() + "개의 데이터가 존재합니다.", Toast.LENGTH_SHORT).show();
                break;
            } else {
                Item item = convertData(dbManager.getPreviousListViewData(listViewPosition));
                m_arr.add(item);
            }
            listViewPosition--;
        }
        adapter = new List_Adapter(showListActivity.this, m_arr);
        lv.setAdapter(adapter);
        lv.setDividerHeight(5);
        amountOfDraw = m_arr.size();
        Log.e("dhrms","m_arr.size"+m_arr.size());
        pos =  lv.getFirstVisiblePosition();
    }

    private void setListMore() {

        Log.e("dhrms", "setListMore에 들어옴");
        for (int i = 0; i < amountOfShowList; i++) {
            if (listViewPosition <= 0) {
                Toast.makeText(this, "데이터를 더 가져옵니다.", Toast.LENGTH_SHORT).show();
                break;
            } else {
                if(m_arr.size()<=amountOfEvery) {
                    Item item = convertData(dbManager.getPreviousListViewData(listViewPosition));
                    m_arr.add(item);
                }else{
                    break;
                }
            }
            listViewPosition--;
        }
        adapter = new List_Adapter(showListActivity.this, m_arr);
        lv.setAdapter(adapter);
        lv.setDividerHeight(5);
        //lv.setSelection(pos);
        View v = lv.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        lv.setSelectionFromTop(pos,top);
        amountOfDraw=m_arr.size();
        Log.e("dhrms","m_arr.size"+m_arr.size());
        pos =  lv.getFirstVisiblePosition();
    }


    private void listUpdate() {
        adapter.notifyDataSetChanged(); //​리스트뷰 값들의 변화가 있을때 아이템들을 다시 배치 할 때 사용되는 메소드입니다.
    }

    private Item convertData(ListViewData listViewData) {
        String bulbImage = "";
        String looseOrGet = "";
        String whatDateType = "";

        if (listViewData.lvSuccess == 1) {  //성공
            bulbImage += "@drawable/bulbsuccess";
            looseOrGet = "+ ";

        } else if (listViewData.lvSuccess == 2) {  //하는중
            bulbImage += "@drawable/bulbdoing";
            looseOrGet = "미획득 ";
        } else {      // 실패
            bulbImage += "@drawable/bulbfail";
            looseOrGet = "- ";
        }

        if (listViewData.lvDateType == FROM_TODAY) {
            whatDateType = "Today Mission";
        } else if (listViewData.lvDateType == FROM_WEEK) {
            whatDateType = "Week Mission";
        } else {
            whatDateType = "Month Mission";
        }

        Item result = new Item(bulbImage, listViewData.lvgoal, listViewData.lvDate, looseOrGet, listViewData.lvBettingGold, whatDateType, listViewData.lvCurrentAmount, listViewData.lvGoalAmount);
        return result;
    }

    public void onClickBackSpace(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

}
