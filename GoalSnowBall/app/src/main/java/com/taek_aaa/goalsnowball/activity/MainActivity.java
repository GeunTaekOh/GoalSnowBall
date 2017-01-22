package com.taek_aaa.goalsnowball.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.controller.PictureController;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;
import com.taek_aaa.goalsnowball.dialog.MonthGoalDialog;
import com.taek_aaa.goalsnowball.dialog.TodayGoalDialog;
import com.taek_aaa.goalsnowball.dialog.UserNameDialog;
import com.taek_aaa.goalsnowball.dialog.WeekGoalDialog;

import static com.taek_aaa.goalsnowball.data.CalendarDatas.dayOfWeekArray;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int PICK_FROM_ALBUM = 101;
    Bitmap photo;
    public static LayoutInflater inflater;
    ImageView imageView;
    public static int viewHeight = 700;        //원하는 뷰의 높이(해상도)
    Boolean isPicture = false;
    TextView todaytv, weektv, monthtv, dDayWeektv, dDayMonthtv, mainGoldtv, percentToday, percentWeek, percentMonth, userNametv, userIdtv;
    TodayGoalDialog todayGoalDialog;
    WeekGoalDialog weekGoalDialog;
    MonthGoalDialog monthGoalDialog;
    UserNameDialog userNameDialog;
    public static String[] categoryPhysicalArrays = {"개", "쪽", "권", ""};
    public static String[] categoryTimeArrays = {"이상", "이하"};
    public static float defaultHeight, defaultWidth;
    PictureController pictureController;
    public static boolean isSuccessToday = false, isSuccessWeek = false, isSuccessMonth = false;
    public final static int FROM_TODAY = 991;
    public final static int FROM_WEEK = 992;
    public final static int FROM_MONTH = 993;
    DBManager dbmanager;
    UserDBManager userDBManager;
    CalendarDatas today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pictureController = new PictureController();

        init();     //나중에 디비로 구현하면 여기서 몇개 제외하기

        drawMainImage();
        Log.e("rmsxor94","onCreate");
        draw();
        registerForContextMenu(imageView);
    }

    /**
     * 가려젔다가 다시 시작되었을때 값들 업데이트
     **/

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("rmsxor94","onStart");
        draw();
    }

    /**
     * 뒤로가기 눌렀을 때
     **/
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
            startActivity(new Intent(this, WeekGoalDoingActivity.class));
        } else if (id == R.id.nav_monthGoaslSetting) {
            startActivity(new Intent(this, MonthGoalDoingActivity.class));
        } else if (id == R.id.nav_totalGoalSetting) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * intent 결과 처리
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                pictureSetToImageView(data);
                break;
        }
    }

    /**
     * 골라온 이미지를 이미지뷰에 입힘
     **/
    protected void pictureSetToImageView(Intent data) {
        try {
            Uri uri = data.getData();
            String uriPath = uri.getPath();
            Log.e("test", uriPath);
            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            inflater = getLayoutInflater();

            Bitmap rotatedPhoto;
            ExifInterface exif = new ExifInterface(uriPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = pictureController.exifOrientationToDegrees(exifOrientation);

            rotatedPhoto = pictureController.rotate(photo, exifDegree);

            Bitmap sizedPhoto = pictureController.setSizedImage(rotatedPhoto);

            imageView = (ImageView) findViewById(R.id.mainImageView);
            imageView.setImageBitmap(sizedPhoto);
            Toast.makeText(getBaseContext(), "사진을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = true;
        } catch (Exception e) {
            e.getStackTrace();
            isPicture = false;
        }

    }

    /**
     * 목표 부분들을 누르면 다이알러그 보여줌
     **/
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

    /**
     * 달성률 클릭 시
     **/
    public void onClickRate(View v) {
        switch (v.getId()) {
            case R.id.percentToday:
                startActivity(new Intent(this, TodayAchievementRateActivity.class));
                break;
            case R.id.percentWeek:
                startActivity(new Intent(this, WeekAchievementRateActivity.class));
                break;
            case R.id.percentMonth:
                startActivity(new Intent(this, MonthAchievementRateActivity.class));
                break;
        }

    }

    /**
     * 오늘의 목표를 텍스트뷰에 출력
     **/
    public void drawTodayGoal() {
        /*if (dbmanager.hasGoal(today.cYear, today.cMonth, today.cdate, FROM_TODAY)) {
            todaytv.setText(dbmanager.getGoal(today.cYear, today.cMonth, today.cdate, FROM_TODAY));
            todaytv.setGravity(Gravity.CENTER);
        } else {
            todaytv.setText("");
        }*/
        todaytv.setText(dbmanager.getGoal(today.cYear, today.cMonth, today.cdate, FROM_TODAY));
        todaytv.setGravity(Gravity.CENTER);
    }

    /**
     * 이번주 목표를 텍스트뷰에 출력
     **/
    public void drawWeekGoal() {
        weektv.setText(dbmanager.getGoal(today.cYear, today.cMonth, today.cdate, FROM_WEEK));
        weektv.setGravity(Gravity.CENTER);
    }

    /**
     * 이번달 목표를 텍스트뷰에 출력
     **/
    public void drawMonthGoal() {
        /*if (dbmanager.hasGoal(today.cYear, today.cMonth, today.cdate, FROM_MONTH)) {
            monthtv.setText(dbmanager.getGoal(today.cYear, today.cMonth, today.cdate, FROM_MONTH));
            monthtv.setGravity(Gravity.CENTER);
        } else {
            monthtv.setText("");
        }*/
        monthtv.setText(dbmanager.getGoal(today.cYear, today.cMonth, today.cdate, FROM_MONTH));
        monthtv.setGravity(Gravity.CENTER);

    }

    /**
     * 목표를 다이얼로그에서 설정하고 다이얼로그가 dismiss 되면 목표 출력
     **/
    public void drawGoal() {


        todayGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawTodayGoal();
            }
        });

        weekGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawWeekGoal();
            }
        });

        monthGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawMonthGoal();
            }
        });
    }

    /**
     * 이미지 선택 안했을 시에 이미지를 이미지뷰에 출력
     **/
    public void drawMainImage() {
        imageView = (ImageView) findViewById(R.id.mainImageView);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.profile);
        Bitmap bitmapDefault = drawable.getBitmap();
        Bitmap sizedBitmapDefault = pictureController.setSizedImage(bitmapDefault);

        defaultHeight = sizedBitmapDefault.getHeight();
        defaultWidth = sizedBitmapDefault.getWidth();
        imageView.setImageBitmap(sizedBitmapDefault);
    }


    /**
     * 오늘쪽 상단의 디데이를 출력
     **/
    public void drawDDay() {
        CalendarDatas calendarData = new CalendarDatas();
        int cYear = calendarData.cYear;
        int cMonth = calendarData.cMonth;
        int cdate = calendarData.cdate;
        int endDate;

        dDayWeektv = (TextView) findViewById(R.id.d_week);
        dDayMonthtv = (TextView) findViewById(R.id.d_month);

        Log.e("qq", "" + calendarData.today);
        Log.e("qq", "" + calendarData.cYear);
        Log.e("qq", "" + calendarData.hMonth);
        Log.e("qq", "" + calendarData.cMonth);
        Log.e("qq", "" + calendarData.cdate);
        Log.e("qq", "" + dayOfWeekArray[calendarData.dayOfWeekIndex]);
        int countWeek = 0;
        switch (calendarData.dayOfWeekIndex) {
            case 1:
                countWeek = 1;
                break;
            case 2:
                countWeek = 7;
                break;
            case 3:
                countWeek = 6;
                break;
            case 4:
                countWeek = 5;
                break;
            case 5:
                countWeek = 4;
                break;
            case 6:
                countWeek = 3;
                break;
            case 7:
                countWeek = 2;
                break;
        }
        dDayWeektv.setText("이번주   D - " + "" + countWeek);
        endDate = calendarData.getEndOfMonth(cYear, cMonth);
        dDayMonthtv.setText("이번달  D - " + "" + (endDate - cdate + 1));
    }

    /**
     * 오늘 목표 달성률 출력
     **/
    public void drawTodayPercent() {
        double result;
        int goal = dbmanager.getGoalAmount(today.cYear, today.cMonth, today.cdate, FROM_TODAY);
        int current;
        if ((dbmanager.getType(today.cYear, today.cMonth, today.cdate, FROM_TODAY).toString().equals("물리적양")) || (dbmanager.getType(today.cYear, today.cMonth, today.cdate, FROM_TODAY).toString().equals("시간적양"))) {
            Log.e("rmsxor94","drawToayPercent");
            current = dbmanager.getCurrentAmount(today.cYear, today.cMonth, today.cdate, FROM_TODAY);
        } else {
            current = 0;
            goal = 10;

        }
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
            percentToday.setText("" + result + "%");
            percentToday.setTextColor(Color.GREEN);
            /*soundPoolMain = new SoundPool(1, STREAM_MUSIC, 0);
            tuneMain = soundPoolMain.load(this, R.raw.coin, 1);
            soundPoolMain.play(tuneMain, 1, 1, 0, 0, 1);*/

        } else {
            percentToday.setText("" + result + "%");
            percentToday.setTextColor(Color.BLACK);
        }

    }

    /**
     * 이번주 목표 달성률 출력
     **/
    public void drawWeekPercent() {
        double result;
        int goal = dbmanager.getGoalAmount(today.cYear,today.cMonth,today.cdate,FROM_WEEK);
        int current;
        if ((dbmanager.getType(today.cYear,today.cMonth,today.cdate,FROM_WEEK).toString().equals("물리적양")) || (dbmanager.getType(today.cYear,today.cMonth,today.cdate,FROM_WEEK).toString().equals("시간적양"))) {
            current = dbmanager.getCurrentAmount(today.cYear,today.cMonth,today.cdate,FROM_WEEK);
        } else {
            current = 0;
            goal = 10;
        }
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
            percentWeek.setText("" + result + "%");
            percentWeek.setTextColor(Color.GREEN);
            /*soundPoolMain = new SoundPool(1, STREAM_MUSIC, 0);
            tuneMain = soundPoolMain.load(this, R.raw.coin, 1);
            soundPoolMain.play(tuneMain, 1, 1, 0, 0, 1);*/
        } else {
            percentWeek.setText("" + result + "%");
            percentWeek.setTextColor(Color.BLACK);

        }
    }
    /**
     * 이번달 목표 달성률 출력
     **/
    public void drawMonthPercent() {
        double result;
        int goal = dbmanager.getGoalAmount(today.cYear,today.cMonth,today.cdate,FROM_MONTH);
        int current;
        if ((dbmanager.getType(today.cYear,today.cMonth,today.cdate,FROM_MONTH).toString().equals("물리적양")) || (dbmanager.getType(today.cYear,today.cMonth,today.cdate,FROM_MONTH).toString().equals("시간적양"))) {
            current = dbmanager.getCurrentAmount(today.cYear,today.cMonth,today.cdate,FROM_MONTH);
        } else {
            current = 0;
            goal = 10;
        }
        result = (double) current / (double) goal * 100;
        result = Double.parseDouble(String.format("%.1f", result));
        if (result >= 100.0) {
            result = 100;
            percentMonth.setText("" + result + "%");
            percentMonth.setTextColor(Color.GREEN);
        } else {
            percentMonth.setText("" + result + "%");
            percentMonth.setTextColor(Color.BLACK);
        }
    }






    /**
     * init
     **/
    public void init() {


        userDBManager = new UserDBManager(getBaseContext(), "user.db", null, 1);
        dbmanager = new DBManager(getBaseContext(), "goaldb.db", null, 1);

        today = new CalendarDatas();
        mainGoldtv = (TextView) findViewById(R.id.mainGoldtv);
        mainGoldtv.setText("" + userDBManager.getGold() + "Gold");
        percentToday = (TextView) findViewById(R.id.percentToday);
        percentWeek = (TextView) findViewById(R.id.percentWeek);
        percentMonth = (TextView) findViewById(R.id.percentMonth);
        userNametv = (TextView) findViewById(R.id.userIdtv);
        userIdtv = (TextView) findViewById(R.id.userIdtv);
        userIdtv.setText("" + userDBManager.getName());
        userNameDialog = new UserNameDialog(this);
        todaytv = (TextView) findViewById(R.id.mainTodayGoalTv);
        weektv = (TextView) findViewById(R.id.mainWeekGoalTv);
        monthtv = (TextView) findViewById(R.id.mainMonthGoalTv);
        todayGoalDialog = new TodayGoalDialog(this);
        weekGoalDialog = new WeekGoalDialog(this);
        monthGoalDialog = new MonthGoalDialog(this);
    }

    /**
     * 컨텍스트 메뉴
     **/
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 컨텍스트 메뉴가 최초로 한번만 호출되는 콜백 메서드
        menu.setHeaderTitle("어떤 작업을 수행하시겠습니까?");
        String[] currencyUnit = {"사진 추가/수정", "사진 회전"};
        for (int i = 1; i <= 2; i++) {
            menu.add(0, i, 100, currencyUnit[i - 1]);
        }
    }

    /**
     * 컨텍스트 메뉴에서 아이템 선택
     **/
    public boolean onContextItemSelected(MenuItem item) {
        // 롱클릭했을 때 나오는 context Menu 의 항목을 선택(클릭) 했을 때 호출
        switch (item.getItemId()) {
            case 1:// 사진추가
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_FROM_ALBUM);
                break;
            case 2:// 사진 회전
                if (isPicture) {
                    Bitmap rotatedPicture;
                    rotatedPicture = pictureController.rotate(photo, 90);
                    photo = rotatedPicture;
                    imageView = (ImageView) findViewById(R.id.mainImageView);
                    imageView.setImageBitmap(rotatedPicture);
                } else {
                    Toast.makeText(this, "기본 이미지는 회전을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void onClickName(View v) {
        userNameDialog.show();
        userNameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                userIdtv.setText(userDBManager.getName());
            }
        });

    }
    public void draw(){
        drawTodayGoal();
        drawWeekGoal();
        drawMonthGoal();
        drawGoal();
        drawTodayPercent();
        drawWeekPercent();
        drawMonthPercent();
        mainGoldtv.setText("" + userDBManager.getGold() + "Gold");
        drawDDay();
    }
}
