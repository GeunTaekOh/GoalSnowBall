package com.taek_aaa.goalsnowball.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.data.DBData;
import com.taek_aaa.goalsnowball.data.GoalDataSet;
import com.taek_aaa.goalsnowball.dialog.MonthGoalDialog;
import com.taek_aaa.goalsnowball.PictureController;
import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.dialog.TodayGoalDialog;
import com.taek_aaa.goalsnowball.dialog.WeekGoalDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int PICK_FROM_ALBUM = 101;
    Bitmap photo;
    public static LayoutInflater inflater;
    ImageView imageView;
    int viewHeight = 3336;        //원하는 뷰의 높이
    Boolean isPicture = false;
    TextView todaytv, weektv, monthtv, dDayWeektv, dDayMonthtv, mainGoldtv;
    public static GoalDataSet goalDataSet;
    public static LinkedList<DBData> llDBData = new LinkedList<DBData>();
    TodayGoalDialog todayGoalDialog;
    WeekGoalDialog weekGoalDialog;
    MonthGoalDialog monthGoalDialog;
    public static String[] categoryPhysicalArrays = {"개", "쪽", "권", ""};
    public static String[] categoryTimeArrays = {"이상", "이하"};
    public static float defaultHeight, defaultWidth;
    Calendar today;
    int cYear, hMonth, cMonth, cdate, dayOfWeekIndex;
    public static String[] dayOfWeekArray = {"","일","월","화","수","목","금","토"};
    public static int[] endOfMonth={31,28,31,30,31,30,31,31,30,31,30,31};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        goalDataSet = new GoalDataSet();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        goalDataSet.setCurrentAmountToday(0);   //나중에 디비로구현하면 삭제하기
        goalDataSet.setCurrentMinuteToday(0);   //나중에 디비로구현하면 삭제하기
        goalDataSet.setTotalGold(10);
        mainGoldtv = (TextView)findViewById(R.id.mainGoldtv);
        mainGoldtv.setText(""+goalDataSet.getTotalGold()+"Gold");

        drawDDay();
        drawMainImage();
        drawGoal();
    }
    /** 뒤로가기 눌렀을 때 **/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    /** 네비게이션 바에서 메뉴 선택 시 **/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todayGoalSetting) {
            startActivity(new Intent(this, TodayGoalDoingActivity.class));
        } else if (id == R.id.nav_weekGoalSetting) {


        } else if (id == R.id.nav_monthGoaslSetting) {

        } else if (id == R.id.nav_totalGoalSetting) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /** 이미지 뷰 클릭 했을 때 **/
    public void onClickMainImage(View v) {
        if (isPicture == false) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_FROM_ALBUM);
        } else {
            PictureController pictureController = new PictureController();
            Bitmap rotatedPicture;
            rotatedPicture = pictureController.rotate(photo, 90);
            imageView = (ImageView) findViewById(R.id.mainImageView);
            imageView.setImageBitmap(rotatedPicture);
        }
    }

    /** intent 결과 처리 **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                pictureSetToImageView(data);
                break;
        }
    }
    /** 골라온 이미지를 이미지뷰에 입힘 **/
    protected void pictureSetToImageView(Intent data) {
        try {
            PictureController pictureController = new PictureController();
            Uri uri = data.getData();
            String uriPath = uri.getPath();
            Log.e("test", uriPath);
            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            inflater = getLayoutInflater();

            Bitmap rotatedPhoto;
            ExifInterface exif = new ExifInterface(uriPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = pictureController.exifOrientationToDegrees(exifOrientation);
            Log.e("test", "" + exifOrientation);
            Log.e("test", "" + exifDegree);
            rotatedPhoto = pictureController.rotate(photo, exifDegree);

            Bitmap sizedPhoto = setSizedImage(rotatedPhoto);

            imageView = (ImageView) findViewById(R.id.mainImageView);
            imageView.setImageBitmap(sizedPhoto);
            Toast.makeText(getBaseContext(), "사진을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = true;
        } catch (Exception e) {
            e.getStackTrace();
            isPicture = false;
        }

    }
    /** 목표 부분들을 누르면 다이알러그 보여줌 **/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainTodayGoalTv:
                todayGoalDialog.show();
                break;
            case R.id.mainWeekGoalTv:
                weekGoalDialog.show();
                break;
            case R.id.mainMonthGoalTv:
                monthGoalDialog.show();
                break;
        }
    }
    /** 오늘의 목표를 텍스트뷰에 출력 **/
    public void drawTodayGoal() {
        if (goalDataSet.isTodayGoal == true) {
            todaytv.setText(goalDataSet.getTodayGoal());
            todaytv.setGravity(Gravity.CENTER);
            Log.e("test", goalDataSet.getTodayGoal());
        } else {
            todaytv.setText("");
        }
    }
    /** 이번주 목표를 텍스트뷰에 출력 **/
    public void drawWeekGoal() {
        if (goalDataSet.isWeekGoal == true) {
            weektv.setText(goalDataSet.getWeekGoal());
            weektv.setGravity(Gravity.CENTER);
            Log.e("test", goalDataSet.getWeekGoal());
        } else {
            weektv.setText("");
        }

    }
    /** 이번달 목표를 텍스트뷰에 출력 **/
    public void drawMonthGoal() {
        if (goalDataSet.isMonthGoal == true) {
            monthtv.setText(goalDataSet.getMonthGoal());
            monthtv.setGravity(Gravity.CENTER);
            Log.e("test", goalDataSet.getMonthGoal());
        } else {
            monthtv.setText("");
        }

    }
    /** 목표를 다이얼로그에서 설정하고 다이얼로그가 dismiss 되면 목표 출력 **/
    public void drawGoal(){
        todaytv = (TextView) findViewById(R.id.mainTodayGoalTv);
        weektv = (TextView) findViewById(R.id.mainWeekGoalTv);
        monthtv = (TextView) findViewById(R.id.mainMonthGoalTv);

        todayGoalDialog = new TodayGoalDialog(this);
        todayGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawTodayGoal();
            }
        });
        weekGoalDialog = new WeekGoalDialog(this);
        weekGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawWeekGoal();
            }
        });
        monthGoalDialog = new MonthGoalDialog(this);
        monthGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawMonthGoal();
            }
        });
    }
    /** 이미지 선택 안했을 시에 이미지를 이미지뷰에 출력**/
    public void drawMainImage(){
        imageView = (ImageView) findViewById(R.id.mainImageView);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.profile);
        Bitmap bitmapDefault = drawable.getBitmap();

        Bitmap sizedBitmapDefault = setSizedImage(bitmapDefault);

        defaultHeight = sizedBitmapDefault.getHeight();
        defaultWidth = sizedBitmapDefault.getWidth();
        Log.e("length",""+defaultHeight);
        Log.e("length",""+defaultWidth);
        imageView.setImageBitmap(sizedBitmapDefault);

    }
    /** 이미지뷰 사이즈 변환 **/         //근데 안먹히는듯
    public Bitmap setSizedImage(Bitmap bitmap){
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (height > viewHeight) {
            float percente = height / 100;
            float scale = viewHeight / percente;
            width *= scale / 100;
            height *= scale / 100;
        }
        Bitmap sizedBitmapDefault = Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);
        return sizedBitmapDefault;
    }
    /** 오늘쪽 상단의 디데이를 출력 **/
    public void drawDDay(){
        dDayWeektv = (TextView)findViewById(R.id.d_week);
        dDayMonthtv = (TextView)findViewById(R.id.d_month);
        today = Calendar.getInstance();
        cYear = today.get(Calendar.YEAR);
        hMonth = today.get(Calendar.MONTH)+1;
        cMonth = today.get(Calendar.MONTH);
        cdate = today.get(Calendar.DAY_OF_MONTH);
        dayOfWeekIndex = today.get(Calendar.DAY_OF_WEEK);
        Log.e("qq",""+today);
        Log.e("qq",""+cYear);
        Log.e("qq",""+hMonth);
        Log.e("qq",""+cMonth);
        Log.e("qq",""+cdate);
        Log.e("qq",""+dayOfWeekArray[dayOfWeekIndex]);

        switch (dayOfWeekIndex){
            case 1:
                dDayWeektv.setText("이번주   D - 1");
                break;
            case 2:
                dDayWeektv.setText("이번주   D - 7");
                break;
            case 3:
                dDayWeektv.setText("이번주   D - 6");
                break;
            case 4:
                dDayWeektv.setText("이번주   D - 5");
                break;
            case 5:
                dDayWeektv.setText("이번주   D - 4");
                break;
            case 6:
                dDayWeektv.setText("이번주   D - 3");
                break;
            case 7:
                dDayWeektv.setText("이번주   D - 2");
                break;

        }
        switch (cMonth){
            case 0:
                int tmp0;
                tmp0 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp0);
                break;
            case 1:
                boolean tempBoolean;
                int tmp1;
                tmp1 = endOfMonth[cMonth] - cdate;
                tempBoolean = isYoonYear(cYear);
                if(tempBoolean==true){
                    dDayMonthtv.setText("이번달  D - "+""+(tmp1+1));
                    Log.e("ttt",""+tempBoolean);
                }else{
                    dDayMonthtv.setText("이번달  D - "+""+tmp1);
                }
                break;
            case 2:
                int tmp2;
                tmp2 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp2);
                break;
            case 3:
                int tmp3;
                tmp3 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp3);
                break;
            case 4:
                int tmp4;
                tmp4 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp4);
                break;
            case 5:
                int tmp5;
                tmp5 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp5);
                break;
            case 6:
                int tmp6;
                tmp6 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp6);
                break;
            case 7:
                int tmp7;
                tmp7 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp7);
                break;
            case 8:
                int tmp8;
                tmp8 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp8);
                break;
            case 9:
                int tmp9;
                tmp9 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp9);
                break;
            case 10:
                int tmp10;
                tmp10 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp10);
                break;
            case 11:
                int tmp11;
                tmp11 = endOfMonth[cMonth] - cdate;
                dDayMonthtv.setText("이번달  D - "+""+tmp11);
                break;
        }
    }
    /** 2월 윤년 계산 **/
    public Boolean isYoonYear(int year){
        GregorianCalendar gr = new GregorianCalendar();
        return gr.isLeapYear(year);
    }
}