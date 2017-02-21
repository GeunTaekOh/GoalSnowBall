package com.taek_aaa.goalsnowball.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
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
import static com.taek_aaa.goalsnowball.data.CommonData.headColor;
import static com.taek_aaa.goalsnowball.data.CommonData.listViewPosition;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;

/**
 * Created by taek_aaa on 2017. 2. 10..
 */

public class showListActivity extends Activity {

    private ArrayList<Item> m_arr;
    private List_Adapter adapter;

    TextView listtv, countListtv;
    public static int amountShowList = 10;
    ListView lv;
    private static int amountOfEvery;
    private static int amountOfDraw;
    Boolean toastFlag = true;
    private static int lastPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlist);
        dbManagerInstance = DBManager.getInstance(getBaseContext());
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(headColor);
        }
        listtv = (TextView) findViewById(R.id.listTopTv);
        countListtv = (TextView) findViewById(R.id.countListtv);
        if (amountShowList > dbManagerInstance.getLastPosition()) {
            amountShowList = dbManagerInstance.getLastPosition();
        }
        lv = (ListView) findViewById(R.id.listView1);

        if (dbManagerInstance.isEmptyDB()) {
            lv.setBackgroundResource(R.drawable.empty2);
        }
        countListtv.setText("" + amountShowList + " / " + "" + dbManagerInstance.getLastPosition());
        amountOfEvery = dbManagerInstance.getLastPosition();


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

                int threshold = 1;
                int count = lv.getCount();
                if (i == SCROLL_STATE_IDLE) {
                    if (lv.getLastVisiblePosition() >= count - threshold) {
                        if (m_arr.size() == amountOfEvery) {
                            if (toastFlag) {
                                Toast.makeText(showListActivity.this, "마지막 데이터입니다.", Toast.LENGTH_SHORT).show();
                                toastFlag = false;
                            }
                        } else {
                            setListMore();
                        }
                    }
                }
            }


            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


        if (amountOfDraw <= amountOfEvery) {
            setList();
        }


    }


    private void setList() {
        m_arr = new ArrayList<Item>();
        lv = (ListView) findViewById(R.id.listView1);

        listViewPosition = dbManagerInstance.getLastPosition();

        for (int i = 0; i < amountShowList; i++) {
            if (listViewPosition <= 0) {
                Toast.makeText(this, "" + dbManagerInstance.getLastPosition() + "개의 데이터가 존재합니다.", Toast.LENGTH_SHORT).show();
                break;
            } else {
                Item item = convertData(dbManagerInstance.getPreviousListViewData(listViewPosition));
                m_arr.add(item);
            }
            listViewPosition--;
        }
        adapter = new List_Adapter(showListActivity.this, m_arr);
        lv.setAdapter(adapter);
        lv.setDividerHeight(5);
        amountOfDraw = m_arr.size();
        lastPos = amountShowList-6;
    }

    private void setListMore() {
        for (int i = 0; i < amountShowList; i++) {
            if (listViewPosition <= 0) {
                Toast.makeText(this, "데이터를 더 가져옵니다.", Toast.LENGTH_SHORT).show();
                break;
            } else {
                if (m_arr.size() <= amountOfEvery) {
                    Item item = convertData(dbManagerInstance.getPreviousListViewData(listViewPosition));
                    m_arr.add(item);
                    amountShowList++;
                } else {
                    break;
                }
            }
            listViewPosition--;
        }
        adapter = new List_Adapter(showListActivity.this, m_arr);
        lv.setAdapter(adapter);
        lv.setDividerHeight(5);
        lv.setSelection(lastPos);
        amountOfDraw = m_arr.size();
        lastPos += amountShowList;
        countListtv.setText("" + amountShowList + " / " + "" + dbManagerInstance.getLastPosition());
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
        amountOfEvery=0;
        amountOfDraw=0;
        lastPos=0;
        toastFlag=true;
        amountShowList=10;
        finish();
    }

}
