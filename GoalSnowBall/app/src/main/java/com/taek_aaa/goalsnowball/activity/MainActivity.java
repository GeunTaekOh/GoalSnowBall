package com.taek_aaa.goalsnowball.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.taek_aaa.goalsnowball.Service.CurrentTimeService;
import com.taek_aaa.goalsnowball.Service.NotificationService;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.controller.PictureController;
import com.taek_aaa.goalsnowball.controller.PicturePermission;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;
import com.taek_aaa.goalsnowball.dialog.MonthGoalDialog;
import com.taek_aaa.goalsnowball.dialog.TodayGoalDialog;
import com.taek_aaa.goalsnowball.dialog.UserNameDialog;
import com.taek_aaa.goalsnowball.dialog.WeekGoalDialog;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public final static int FROM_TODAY = 991;
    public final static int FROM_WEEK = 992;
    public final static int FROM_MONTH = 993;
    public final static int NOTIFICATION_TERM = 12 * 1000 * 60 * 60;     //12시간 마다 확인
    final int PICK_FROM_ALBUM = 101;
    Bitmap photo;
    public static LayoutInflater inflater;
    ImageView imageView, todayBulb, weekBulb, monthBulb;
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
    DBManager dbmanager;
    UserDBManager userDBManager;
    CalendarDatas today;
    DataController dataController;
    public static boolean isTodayDueFinish, isWeekDueFinish, isMonthDueFinish;
    public static boolean isFailToday, isFailWeek, isFailMonth;


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
        init();

        pictureController = new PictureController();
        PicturePermission.verifyStoragePermissions(this);

        drawMainImage();
        draw();

        Intent notificationIntent = new Intent(MainActivity.this, NotificationService.class);       //알람 서비스 실행
        startService(notificationIntent);

        Intent timerIntent = new Intent(MainActivity.this, CurrentTimeService.class);
        startService(timerIntent);


    }

    /**
     * 가려젔다가 다시 시작되었을때 값들 업데이트
     **/
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("rmsxor94", "onStart");
        draw();
        checkFailStatus();
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
            File imageFile = new File(getRealPathFromURI(uri));
            userDBManager.setPicturePath(imageFile.toString());
            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            inflater = getLayoutInflater();
            Bitmap sizedPhoto = pictureController.setSizedImage(photo);
            imageView.setImageBitmap(sizedPhoto);
            Toast.makeText(getBaseContext(), "사진을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = true;
        } catch (Exception e) {
            e.getStackTrace();
            isPicture = false;
        }
    }

    /**
     * 이미지가 저장되어 있는 경우에 이미지를 그려줌
     **/
    protected void drawImage() {
        int iter= 0;
        Bitmap rotatedPhoto;
        if (userDBManager.getPicturePath().equals("null")) {
            isPicture = false;
            drawMainImage();
        } else {
            String picturePath = userDBManager.getPicturePath();
            File imgFile = new File("" + picturePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap sizedPhoto = pictureController.setSizedImage(myBitmap);
                photo = sizedPhoto;
                iter = userDBManager.getRotationIter();
                rotatedPhoto = pictureController.rotate(photo, iter*90);
                photo = rotatedPhoto;
                imageView.setImageBitmap(rotatedPhoto);
                isPicture = true;
            } else {
                Toast.makeText(this, "경로에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                isPicture = false;
            }
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
     * 좌측 하단 텍스트뷰를 클릭하면 드로우바에서 목표 관리하는 액티비티를 보여줌
     **/
    public void onClickJustTextView(View v) {
        switch (v.getId()) {
            case R.id.justTodayGoaltv:
                startActivity(new Intent(this, TodayGoalDoingActivity.class));
                break;
            case R.id.justWeekGoaltv:
                startActivity(new Intent(this, WeekGoalDoingActivity.class));
                break;
            case R.id.justMonthGoaltv:
                startActivity(new Intent(this, MonthGoalDoingActivity.class));
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
     * 오늘의 목표를 텍스트뷰에 출력     isSuccess가 0이면 없는것 //  1이면 성공 // 2이면 하는중 // 3이면 실패
     **/
    public void drawTodayGoal() {

        if (dbmanager.getIsSuccess(FROM_TODAY) == 1) {
            todayBulb.setImageResource(R.drawable.bulbsuccess);
        } else if (dbmanager.getIsSuccess(FROM_TODAY) == 2) {
            todayBulb.setImageResource(R.drawable.bulbdoing);
        } else if (dbmanager.getIsSuccess(FROM_TODAY) == 3) {
            todayBulb.setImageResource(R.drawable.bulbfail);
        } else {
            todayBulb.setImageResource(0);
        }
        todaytv.setText(dbmanager.getGoal(FROM_TODAY));
        todaytv.setGravity(Gravity.CENTER);
    }

    /**
     * 이번주 목표를 텍스트뷰에 출력     isSuccess가 0이면 없는것 //  1이면 성공 // 2이면 하는중 // 3이면 실패
     **/
    public void drawWeekGoal() {
        if (dbmanager.getIsSuccess(FROM_WEEK) == 1) {
            weekBulb.setImageResource(R.drawable.bulbsuccess);
        } else if (dbmanager.getIsSuccess(FROM_WEEK) == 2) {
            weekBulb.setImageResource(R.drawable.bulbdoing);
        } else if (dbmanager.getIsSuccess(FROM_WEEK) == 3) {
            weekBulb.setImageResource(R.drawable.bulbfail);
        } else {
            weekBulb.setImageResource(0);
        }
        weektv.setText(dbmanager.getGoal(FROM_WEEK));
        weektv.setGravity(Gravity.CENTER);
    }

    /**
     * 이번달 목표를 텍스트뷰에 출력     isSuccess가 0이면 없는것 //  1이면 성공 // 2이면 하는중 // 3이면 실패
     **/
    public void drawMonthGoal() {
        if (dbmanager.getIsSuccess(FROM_MONTH) == 1) {
            monthBulb.setImageResource(R.drawable.bulbsuccess);
        } else if (dbmanager.getIsSuccess(FROM_MONTH) == 2) {
            monthBulb.setImageResource(R.drawable.bulbdoing);
        } else if (dbmanager.getIsSuccess(FROM_MONTH) == 3) {
            monthBulb.setImageResource(R.drawable.bulbfail);
        } else {
            monthBulb.setImageResource(0);
        }
        monthtv.setText(dbmanager.getGoal(FROM_MONTH));
        monthtv.setGravity(Gravity.CENTER);
    }

    public void drawGoal() {
        drawTodayGoal();
        drawWeekGoal();
        drawMonthGoal();
    }

    /**
     * 목표를 다이얼로그에서 설정하고 다이얼로그가 dismiss 되면 목표 출력
     **/
    public void drawGoalWhenDismiss() {
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

        int endDate;
        dDayWeektv = (TextView) findViewById(R.id.d_week);
        dDayMonthtv = (TextView) findViewById(R.id.d_month);
        int countWeek = calendarData.getDdayWeek(calendarData.dayOfWeekIndex);
        dDayWeektv.setText("이번주   D - " + "" + countWeek);
        endDate = calendarData.getEndOfMonth(calendarData.cYear, calendarData.cMonth);
        dDayMonthtv.setText("이번달  D - " + "" + (endDate - calendarData.cdate + 1));
    }

    /**
     * 오늘 목표 달성률 출력
     **/
    public void drawTodayPercent() {
        double result;
        int goal = dbmanager.getGoalAmount(FROM_TODAY);
        int current;
        if ((dbmanager.getType(FROM_TODAY).toString().equals("물리적양")) || (dbmanager.getType(FROM_TODAY).toString().equals("시간적양"))) {
            current = dbmanager.getCurrentAmount(FROM_TODAY);
        } else {
            current = 0;
            goal = 10;
        }

        result = dataController.makePercent(current, goal);
        if (result == 100) {
            percentToday.setTextColor(Color.GREEN);
        } else {
            percentToday.setTextColor(Color.BLACK);
        }
        percentToday.setText("" + result + "%");

    }

    /**
     * 이번주 목표 달성률 출력
     **/
    public void drawWeekPercent() {
        double result;
        int goal = dbmanager.getGoalAmount(FROM_WEEK);
        int current;
        if ((dbmanager.getType(FROM_WEEK).toString().equals("물리적양")) || (dbmanager.getType(FROM_WEEK).toString().equals("시간적양"))) {
            current = dbmanager.getCurrentAmount(FROM_WEEK);
        } else {
            current = 0;
            goal = 10;
        }
        result = dataController.makePercent(current, goal);
        if (result == 100) {
            percentWeek.setTextColor(Color.GREEN);
        } else {
            percentWeek.setTextColor(Color.BLACK);

        }
        percentWeek.setText("" + result + "%");
    }

    /**
     * 이번달 목표 달성률 출력
     **/
    public void drawMonthPercent() {
        double result;
        int goal = dbmanager.getGoalAmount(FROM_MONTH);
        int current;
        if ((dbmanager.getType(FROM_MONTH).toString().equals("물리적양")) || (dbmanager.getType(FROM_MONTH).toString().equals("시간적양"))) {
            current = dbmanager.getCurrentAmount(FROM_MONTH);
        } else {
            current = 0;
            goal = 10;
        }
        result = dataController.makePercent(current, goal);
        if (result == 100) {
            result = 100;
            percentMonth.setTextColor(Color.GREEN);
        } else {
            percentMonth.setTextColor(Color.BLACK);
        }
        percentMonth.setText("" + result + "%");
    }

    /**
     * init
     **/
    public void init() {
        userDBManager = new UserDBManager(getBaseContext(), "user.db", null, 1);
        dbmanager = new DBManager(getBaseContext(), "goaldb.db", null, 1);
        imageView = (ImageView) findViewById(R.id.mainImageView);
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
        registerForContextMenu(imageView);
        registerForContextMenu(todaytv);
        registerForContextMenu(weektv);
        registerForContextMenu(monthtv);
        dataController = new DataController();
        todayBulb = (ImageView) findViewById(R.id.todayBulb);
        weekBulb = (ImageView) findViewById(R.id.weekBulb);
        monthBulb = (ImageView) findViewById(R.id.monthBulb);
    }

    /**
     * 컨텍스트 메뉴
     **/
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 컨텍스트 메뉴가 최초로 한번만 호출되는 콜백 메서드

        menu.setHeaderTitle("어떤 작업을 수행하시겠습니까?");

        if (v == imageView) {
            String[] currencyUnit = {"사진 추가/수정", "사진 회전", "사진 삭제"};
            for (int i = 1; i <= 3; i++) {
                menu.add(0, i, 100, currencyUnit[i - 1]);
            }
        } else if (v == todaytv) {
            String[] currencyUnit = {"오늘의 일정 삭제"};
            menu.add(0, 4, 100, currencyUnit[0]);
        } else if (v == weektv) {
            String[] currencyUnit = {"이번주 일정 삭제"};
            menu.add(0, 5, 100, currencyUnit[0]);
        } else if (v == monthtv) {
            String[] currencyUnit = {"이번달 일정 삭제"};
            menu.add(0, 6, 100, currencyUnit[0]);
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
                    imageView.setImageBitmap(rotatedPicture);
                    Log.e("rmsxor", "" + photo);
                    userDBManager.addRotationIter();
                } else {
                    Toast.makeText(this, "기본 이미지는 회전을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3://사진삭제
                if (userDBManager.getPicturePath().equals("null")) {
                    Toast.makeText(this, "아직 이미지를 설정하지 않았습니다. ", Toast.LENGTH_SHORT).show();
                } else {
                    drawMainImage();
                    isPicture = false;
                    userDBManager.setPicturePath("null");
                    Toast.makeText(this, "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:     //오늘 일정 삭제
                if (dbmanager.getIsSuccess(FROM_TODAY) == 1) {
                    Toast.makeText(this, "이미 달성하여서 목표를 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    dbmanager.delete(FROM_TODAY);
                    onStart();
                }
                break;
            case 5:     //이번주 일정 삭제
                if (dbmanager.getIsSuccess(FROM_WEEK) == 1) {
                    Toast.makeText(this, "이미 달성하여서 목표를 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    dbmanager.delete(FROM_WEEK);
                    onStart();
                }
                break;
            case 6:     //이번달 일정 삭제
                if (dbmanager.getIsSuccess(FROM_MONTH) == 1) {
                    Toast.makeText(this, "이미 달성하여서 목표를 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    dbmanager.delete(FROM_MONTH);
                    onStart();
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

    public void draw() {
        drawGoal();
        drawGoalWhenDismiss();
        drawTodayPercent();
        drawWeekPercent();
        drawMonthPercent();
        mainGoldtv.setText("" + userDBManager.getGold() + "Gold");
        drawDDay();
        drawImage();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void checkFailStatus() {
        if (isTodayDueFinish) {
            if (dbmanager.getGoalAmount(FROM_TODAY) > dbmanager.getCurrentAmount(FROM_TODAY)) {
                isFailToday=true;
            }
            isTodayDueFinish = false;
        }
        if (isWeekDueFinish) {
            if (dbmanager.getGoalAmount(FROM_WEEK) > dbmanager.getCurrentAmount(FROM_WEEK)) {
                isFailWeek=true;
            }
            isWeekDueFinish = false;
        }
        if (isMonthDueFinish) {
            if(dbmanager.getGoalAmount(FROM_MONTH) > dbmanager.getCurrentAmount(FROM_MONTH)){
                isFailMonth=true;
            }
            isMonthDueFinish = false;
        }
    }

}