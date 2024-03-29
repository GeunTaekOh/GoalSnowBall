package com.taek_aaa.goalsnowball.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.taek_aaa.goalsnowball.R;
import com.taek_aaa.goalsnowball.Service.CurrentTimeService;
//import com.taek_aaa.goalsnowball.Service.NotificationService;
import com.taek_aaa.goalsnowball.activity.AchievementRate.MonthAchievementRateActivity;
import com.taek_aaa.goalsnowball.activity.AchievementRate.TodayAchievementRateActivity;
import com.taek_aaa.goalsnowball.activity.AchievementRate.WeekAchievementRateActivity;
import com.taek_aaa.goalsnowball.activity.GoalDoing.MonthGoalDoingActivity;
import com.taek_aaa.goalsnowball.activity.GoalDoing.TodayGoalDoingActivity;
import com.taek_aaa.goalsnowball.activity.GoalDoing.WeekGoalDoingActivity;
import com.taek_aaa.goalsnowball.controller.DataController;
import com.taek_aaa.goalsnowball.controller.PictureController;
import com.taek_aaa.goalsnowball.controller.PicturePermission;
import com.taek_aaa.goalsnowball.data.CalendarDatas;
import com.taek_aaa.goalsnowball.data.DBManager;
import com.taek_aaa.goalsnowball.data.UserDBManager;
import com.taek_aaa.goalsnowball.dialog.ContactUsDialog;
import com.taek_aaa.goalsnowball.dialog.FailDialog;
import com.taek_aaa.goalsnowball.dialog.GoalDialog.MonthGoalDialog;
import com.taek_aaa.goalsnowball.dialog.GoalDialog.TodayGoalDialog;
import com.taek_aaa.goalsnowball.dialog.GoalDialog.WeekGoalDialog;
import com.taek_aaa.goalsnowball.dialog.GradeDialog;
import com.taek_aaa.goalsnowball.dialog.LevelUpDialog;
import com.taek_aaa.goalsnowball.dialog.UserNameDialog;

import java.io.File;

import static android.media.AudioManager.STREAM_MUSIC;
import static com.taek_aaa.goalsnowball.data.CalendarDatas.TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.DOING_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.FAIL_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;
import static com.taek_aaa.goalsnowball.data.CommonData.SUCCESS_STATUS;
import static com.taek_aaa.goalsnowball.data.CommonData.defaultHeight;
import static com.taek_aaa.goalsnowball.data.CommonData.defaultWidth;
import static com.taek_aaa.goalsnowball.data.CommonData.headColor;
import static com.taek_aaa.goalsnowball.data.CommonData.inflater;
import static com.taek_aaa.goalsnowball.data.CommonData.myBlack;
import static com.taek_aaa.goalsnowball.data.CommonData.myGreen;
import static com.taek_aaa.goalsnowball.data.CommonData.myRed;
import static com.taek_aaa.goalsnowball.data.CommonData.setFailStatus;
import static com.taek_aaa.goalsnowball.data.CommonData.whatGradeTo;
import static com.taek_aaa.goalsnowball.data.DBManager.dbManagerInstance;
import static com.taek_aaa.goalsnowball.data.UserDBManager.userDBManagerInstance;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    final int PICK_FROM_ALBUM = 101;
    Bitmap photo;
    ImageView todayBulb, weekBulb, monthBulb;
    CircularImageView imageView;
    Boolean isPicture = false;
    TextView todaytv, weektv, monthtv, dDayWeektv, dDayMonthtv, mainGoldtv, percentToday, percentWeek, percentMonth, userNametv, userIdtv, mainGradetv;
    TodayGoalDialog todayGoalDialog;
    WeekGoalDialog weekGoalDialog;
    MonthGoalDialog monthGoalDialog;
    UserNameDialog userNameDialog;
    PictureController pictureController;
    CalendarDatas today;
    DataController dataController;
    FailDialog failDialog;
    SoundPool soundPool;
    ShowcaseView showcaseView;
    int tune;
    private Target t1, t2, t3, t4, t5, t6, t7, t8;
    private int contador = 0;
    LevelUpDialog levelUpDialog;
    GradeDialog gradeDialog;
    Context context;
    long lastTimeBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getBaseContext();

        doDrawerLayout(toolbar);
        doNavigationView();

        //사진 권한
        PicturePermission.verifyStoragePermissions(this);


        init();

        myRed = getResources().getColor(R.color.myRed);
        myGreen = getResources().getColor(R.color.myGreen);
        headColor = getResources().getColor(R.color.headColor);
        myBlack = getResources().getColor(R.color.myBlack);



        // draw 함수에 drawMainImage, drawImage 포함 시키지 않는 것이 더 효율적
        draw();
        drawImage();

        if (dataController.getPreferencesFailFlag(context) == 1) {
            failDialog = new FailDialog(this);
            failDialog.show();
        }

        //Intent notificationIntent = new Intent(MainActivity.this, NotificationService.class);       //알람 서비스 실행
        //startService(notificationIntent);

        //현재 시간 받는 서비스 실행
        Intent timerIntent = new Intent(MainActivity.this, CurrentTimeService.class);
        startService(timerIntent);

        if (dbManagerInstance.isNotWorkFailToday()) {
            dbManagerInstance.setDBFailToday(dbManagerInstance.getFirstTodayDoingIndex());
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(headColor);
        }
    }

    private void doDrawerLayout(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void doNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 가려젔다가 다시 시작되었을때 값들 업데이트
     **/
    @Override
    protected void onStart() {
        super.onStart();
        checkFailStatus();
        checkLevelUpStatus();
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
        } else if (dataController.getPreferencesIsFirstOpenApp(context) == 1) {
            showcaseView.hide();
            dataController.setPreferencesIsFirstOpenApp(context, 0);
            contador = 0;
        } else {
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
                finish();
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                return;
            }
            Toast.makeText(MainActivity.this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
            lastTimeBackPressed = System.currentTimeMillis();
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
        switch (item.getItemId()) {
            case R.id.action_list:
                startActivity(new Intent(this, showListActivity.class));
                break;
            case R.id.action_howtouse:
                dataController.setPreferencesIsFirstOpenApp(context, 1);
                guide();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    /** 네비게이션 바에서 메뉴 선택 시 **/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_todayGoalSetting:
                startActivity(new Intent(this, TodayGoalDoingActivity.class));
                break;
            case R.id.nav_weekGoalSetting:
                startActivity(new Intent(this, WeekGoalDoingActivity.class));
                break;
            case R.id.nav_monthGoalSetting:
                startActivity(new Intent(this, MonthGoalDoingActivity.class));
                break;
            case R.id.nav_listGoalItem:
                startActivity(new Intent(this, showListActivity.class));
                break;
            case R.id.nav_contacUsItem:
                ContactUsDialog contactUsDialog = new ContactUsDialog(this);
                contactUsDialog.show();
                break;
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
    private void pictureSetToImageView(Intent data) {
        try {
            Uri uri = data.getData();
            File imageFile = new File(getRealPathFromURI(uri));
            userDBManagerInstance.setPicturePath(imageFile.toString());
            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            inflater = getLayoutInflater();
            Bitmap sizedPhoto = pictureController.setSizedImage(photo);
            imageView.setImageBitmap(sizedPhoto);
            Toast.makeText(getBaseContext(), "사진을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = true;
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "사진을 입력하는 과정에서 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = false;
        }
    }

    /**
     * 사진 임시 경로가 아닌 실제 경로를 가져옴
     **/
    protected String getRealPathFromURI(Uri contentURI) {
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


    /**
     * 이미지가 저장되어 있는 경우에 이미지를 그려줌
     **/
    protected void drawImage() {
        int iter = 0;
        Bitmap rotatedPhoto;
        if (userDBManagerInstance.getPicturePath().equals("null") || userDBManagerInstance.getPicturePath()==null) {
            isPicture = false;
            drawMainImage();
        } else {
            String picturePath = userDBManagerInstance.getPicturePath();
            File imgFile = new File("" + picturePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap sizedPhoto = pictureController.setSizedImage(myBitmap);
                photo = sizedPhoto;
                try {
                    iter = userDBManagerInstance.getRotationIter();
                } catch (Exception e) {
                    iter = 0;
                }
                rotatedPhoto = pictureController.rotate(photo, iter * 90);
                photo = rotatedPhoto;
                imageView.setImageBitmap(rotatedPhoto);
                isPicture = true;
            } else {
                Toast.makeText(this, "경로에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                isPicture = false;
            }
        }
    }

    private void drawUserStatus() {
        mainGoldtv.setText("" + userDBManagerInstance.getGold() + "Gold");
        userIdtv.setText("" + userDBManagerInstance.getName());
        mainGradetv.setText("" + userDBManagerInstance.getGrade());
    }

    /**
     * 목표 부분들을 누르면 다이알러그 보여줌
     **/
    public void onClickGoal(View v) {
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
     * 인자로 받은 날의 목표를 텍스트뷰에 출력     isSuccess가 0이면 없는것 //  1이면 성공 // 2이면 하는중 // 3이면 실패
     **/
    private void drawGoalWhen(int fromDateType) {
        ImageView imageView = null;
        TextView textView = null;

        switch (fromDateType) {
            case FROM_TODAY:
                imageView = todayBulb;
                textView = todaytv;
                break;
            case FROM_WEEK:
                imageView = weekBulb;
                textView = weektv;
                break;
            case FROM_MONTH:
                imageView = monthBulb;
                textView = monthtv;
                break;
        }

        switch (dbManagerInstance.getIsSuccess(fromDateType)) {
            case SUCCESS_STATUS:
                imageView.setImageResource(R.drawable.bulbsuccess);
                break;
            case DOING_STATUS:
                imageView.setImageResource(R.drawable.bulbdoing);
                break;
            case FAIL_STATUS:
                imageView.setImageResource(R.drawable.bulbfail);
                break;
            default:
                imageView.setImageResource(0);
                break;
        }
        textView.setText(dbManagerInstance.getGoal(fromDateType));
        textView.setGravity(Gravity.CENTER);
    }

    private void drawGoal() {
        drawGoalWhen(FROM_TODAY);
        drawGoalWhen(FROM_WEEK);
        drawGoalWhen(FROM_MONTH);
    }

    /**
     * 목표를 다이얼로그에서 설정하고 다이얼로그가 dismiss 되면 목표 출력
     **/
    private void drawGoalWhenDismiss() {
        todayGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawGoalWhen(FROM_TODAY);
            }
        });

        weekGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawGoalWhen(FROM_WEEK);
            }
        });

        monthGoalDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                drawGoalWhen(FROM_MONTH);
            }
        });
    }

    /**
     * 이미지 선택 안했을 시에 이미지를 이미지뷰에 출력
     **/
    private void drawMainImage() {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.profile2);
        Bitmap bitmapDefault = drawable.getBitmap();
        Bitmap sizedBitmapDefault = pictureController.setSizedImage(bitmapDefault);
        defaultHeight = sizedBitmapDefault.getHeight();
        defaultWidth = sizedBitmapDefault.getWidth();
        imageView.setImageBitmap(sizedBitmapDefault);
    }


    /**
     * 오늘쪽 상단의 디데이를 출력
     **/
    private void drawDDay() {
        CalendarDatas calendarData = new CalendarDatas(TODAY);
        int endDate;
        dDayWeektv = (TextView) findViewById(R.id.d_week);
        dDayMonthtv = (TextView) findViewById(R.id.d_month);
        int countWeek = calendarData.getDdayWeek(calendarData.dayOfWeekIndex);
        dDayWeektv.setText("이번주   D - " + "" + countWeek);
        endDate = calendarData.getEndOfMonth(calendarData.cYear, calendarData.cMonth);
        dDayMonthtv.setText("이번달  D - " + "" + (endDate - calendarData.cdate + 1));
    }

    /**
     * 주어진 상태의 목표 달성률 출력
     **/
    private void drawWhenPercent(int fromDateType) {
        TextView tv = null;
        double result;
        int goal = dbManagerInstance.getGoalAmount(fromDateType,TODAY);
        int current;

        switch (fromDateType) {
            case FROM_TODAY:
                tv = percentToday;
                break;
            case FROM_WEEK:
                tv = percentWeek;
                break;
            case FROM_MONTH:
                tv = percentMonth;
                break;
        }

        if ((dbManagerInstance.getType(fromDateType).toString().equals("물리적양")) || (dbManagerInstance.getType(fromDateType).toString().equals("시간적양"))) {
            current = dbManagerInstance.getCurrentAmount(fromDateType,TODAY);
        } else {
            current = 0;
            goal = 10;
        }
        result = dataController.makePercent(current, goal);
        if (result == 100) {
            if (dbManagerInstance.getType(fromDateType).equals("시간적양") && dbManagerInstance.getUnit(fromDateType).equals("이하")) {
                tv.setTextColor(myRed);
            } else {
                tv.setTextColor(myGreen);
            }
        } else {
            tv.setTextColor(myBlack);
        }
        tv.setText("" + result + "%");
    }


    /**
     * init
     **/
    private void init() {
        userDBManagerInstance = UserDBManager.getInstance(getBaseContext());
        dbManagerInstance = DBManager.getInstance(getBaseContext());

        initView();
        initDialog();
        initSetText();
        initRegistContextMenu();

        dataController = new DataController();
        pictureController = new PictureController();
        today = new CalendarDatas(TODAY);
        //처음 어플 실행시켰을때 가이드 화면을 띄워줌
        if (dataController.getPreferencesIsFirstOpenApp(context) == 1) {
            guide();
        }
    }

    private void initView() {
        imageView = (CircularImageView) findViewById(R.id.mainImageView);
        mainGradetv = (TextView) findViewById(R.id.mainGradetv);
        mainGoldtv = (TextView) findViewById(R.id.mainGoldtv);
        percentToday = (TextView) findViewById(R.id.percentToday);
        percentWeek = (TextView) findViewById(R.id.percentWeek);
        percentMonth = (TextView) findViewById(R.id.percentMonth);
        userNametv = (TextView) findViewById(R.id.userIdtv);
        userIdtv = (TextView) findViewById(R.id.userIdtv);
        todaytv = (TextView) findViewById(R.id.mainTodayGoalTv);
        weektv = (TextView) findViewById(R.id.mainWeekGoalTv);
        monthtv = (TextView) findViewById(R.id.mainMonthGoalTv);
        todayBulb = (ImageView) findViewById(R.id.todayBulb);
        weekBulb = (ImageView) findViewById(R.id.weekBulb);
        monthBulb = (ImageView) findViewById(R.id.monthBulb);
    }

    private void initDialog() {
        userNameDialog = new UserNameDialog(this);
        todayGoalDialog = new TodayGoalDialog(this);
        weekGoalDialog = new WeekGoalDialog(this);
        monthGoalDialog = new MonthGoalDialog(this);
        gradeDialog = new GradeDialog(this);
    }

    private void initSetText() {
        mainGoldtv.setText("" + userDBManagerInstance.getGold() + "Gold");
        mainGradetv.setText("" + userDBManagerInstance.getGrade());
        userIdtv.setText("" + userDBManagerInstance.getName());
    }

    private void initRegistContextMenu() {
        registerForContextMenu(imageView);
        registerForContextMenu(todaytv);
        registerForContextMenu(weektv);
        registerForContextMenu(monthtv);
    }

    /**
     * 메인이미지를 롱클릭이 아닌 그냥 클릭으로도 롱클릭 효과를 줌
     **/
    public void onClickMainImage(View v) {
        registerForContextMenu(imageView);
        openContextMenu(imageView);
    }

    /**
     * 컨텍스트 메뉴
     **/
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("어떤 작업을 수행하시겠습니까?");
        if (v == imageView) {
            String[] currencyUnit = {"사진 추가", "사진 회전", "사진 삭제"};
            for (int i = 1; i <= 3; i++) {
                menu.add(0, i, 100, currencyUnit[i - 1]);
            }
            if (!isPicture) {
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
            } else {
                menu.getItem(0).setVisible(false);
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
        CalendarDatas calendarDatas = new CalendarDatas(TODAY);
        switch (item.getItemId()) {
            case 1:// 사진추가
                if (isPicture) {
                    Toast.makeText(this, "사진을 삭제하시고 추가하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                    isPicture = true;
                }
                break;
            case 2:// 사진 회전
                if (isPicture) {
                    Bitmap rotatedPicture;
                    rotatedPicture = pictureController.rotate(photo, 90);
                    photo = rotatedPicture;
                    imageView.setImageBitmap(rotatedPicture);
                    userDBManagerInstance.addRotationIter();
                } else {
                    Toast.makeText(this, "아직 이미지를 설정하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3://사진삭제
                if (userDBManagerInstance.getPicturePath().equals("null")) {
                    Toast.makeText(this, "아직 이미지를 설정하지 않았습니다. ", Toast.LENGTH_SHORT).show();
                } else {
                    drawMainImage();
                    isPicture = false;
                    userDBManagerInstance.setPicturePath("null");
                    userDBManagerInstance.setRotationIter(0);
                    Toast.makeText(this, "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:     //오늘 일정 삭제
                deleteGoal(FROM_TODAY);
                break;
            case 5:     //이번주 일정 삭제
                deleteGoal(FROM_WEEK);
                break;
            case 6:     //이번달 일정 삭제
                deleteGoal(FROM_MONTH);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 주어진 상태의 일정 삭제
     **/
    private void deleteGoal(int fromDateType) {
        CalendarDatas calendarDatas = new CalendarDatas(TODAY);
        String msg = "";
        Boolean bool = false;

        switch (fromDateType) {
            case FROM_TODAY:
                msg = "오늘의 목표를 끝까지 도전 해보는건 어떨까요?";
                bool = calendarDatas.hour > 18;
                break;
            case FROM_WEEK:
                msg = "이번주의 목표를 조금만 더 도전해볼까요?";
                bool = calendarDatas.dayOfWeekIndex > 5;
                break;
            case FROM_MONTH:
                msg = "이번달의 목표를 끝까지 응원합니다!";
                bool = calendarDatas.cdate > 15;
                break;
        }


        if (dbManagerInstance.getIsSuccess(fromDateType) == SUCCESS_STATUS) {
            Toast.makeText(this, "이미 성공하여서 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else if (dbManagerInstance.getIsSuccess(fromDateType) == FAIL_STATUS) {
            Toast.makeText(this, "이미 실패하여서 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else if (bool) {
            Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        } else {
            dbManagerInstance.delete(fromDateType);
            onStart();
        }
    }

    /**
     * 이름을 입력받는 창 출력
     **/
    public void onClickName(View v) {
        userNameDialog.show();
        userNameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                userIdtv.setText(userDBManagerInstance.getName());
            }
        });
    }

    /**
     * 다음 등급을 확인하는 창 출력
     **/
    public void onClickGrade(View v) {
        gradeDialog.show();
    }

    /**
     * 화면에 무언가를 그려주는 메서드들
     **/
    private void draw() {
        drawGoal();
        drawGoalWhenDismiss();
        drawWhenPercent(FROM_TODAY);
        drawWhenPercent(FROM_WEEK);
        drawWhenPercent(FROM_MONTH);
        mainGoldtv.setText("" + userDBManagerInstance.getGold() + "Gold");
        drawDDay();
        drawUserStatus();

    }

    /**
     * 목표 달성 실패 여부 확인 후 실패시 실패 화면 띄움
     **/
    private void checkFailStatus() {
        if (dataController.getPreferencesFailFlag(context) == 1) {
            failDialog = new FailDialog(this);
            failDialog.show();

            setFailStatus(false);

            failDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    AudioManager mAudioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

                    if ((mAudioManager.getRingerMode() == 2) && userDBManagerInstance.getIsSound() == 1) {
                        soundPool = new SoundPool(1, STREAM_MUSIC, 0);
                        tune = soundPool.load(getBaseContext(), R.raw.failcoin, 1);
                        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                            @Override
                            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                                soundPool.play(tune, 1, 1, 0, 0, 1);
                            }
                        });
                    }
                    draw();
                }
            });
        }
    }

    private void checkLevelUpStatus() {
        if (dataController.getPreferencesLevelUpFlag(context) == 1 && dataController.getPreferencesIsGradeChange(context) != whatGradeTo) {
            levelUpDialog = new LevelUpDialog(this);
            levelUpDialog.show();
            drawUserStatus();
            dataController.setPreferencesLevelUpFlag(context, 0);
            dataController.setPreferencesIsGradeChange(context, whatGradeTo);
        }
    }

    /**
     * 안내해주는 showcaseView 오픈소스 출력
     **/
    private void guide() {
        t1 = new ViewTarget(R.id.mainImageView, this);
        t2 = new ViewTarget(R.id.d_week, this);
        t3 = new ViewTarget(R.id.mainGradetv, this);
        t4 = new ViewTarget(R.id.userIdtv, this);
        t5 = new ViewTarget(R.id.mainGoldtv, this);
        t6 = new ViewTarget(R.id.percentToday, this);
        t7 = new ViewTarget(R.id.mainTodayGoalTv, this);
        t8 = new ViewTarget(R.id.justTodayGoaltv, this);

        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle("사용 설명서")
                .setOnClickListener(this)
                .setContentText("GoalSnowBall 사용 방법을 설명합니다.")
                .build();
        showcaseView.setButtonText("안내 시작");
    }

    /**
     * showcaseView 안내 내용
     **/
    @Override
    public void onClick(View v) {
        switch (contador) {
            case 0:
                showcaseView.setShowcase(t1, true);
                showcaseView.setContentTitle("목표 이미지");
                showcaseView.setContentText("클릭하여서 사진 추가를 할 수 있습니다.\n목표에 부합하는 사진을 추가하는 것이 바람직합니다.");
                showcaseView.setButtonText("다음");
                break;

            case 1:
                showcaseView.setShowcase(t2, true);
                showcaseView.setContentTitle("남은 기한");
                showcaseView.setContentText("이번주 목표와 이번달 목표를 남은 기한을 보면서 효율적으로 분배하세요.");
                showcaseView.setButtonText("다음");
                break;

            case 2:
                showcaseView.setShowcase(t3, true);
                showcaseView.setContentTitle("등급");
                showcaseView.setContentText("클릭하여서 등급을 올리기 위해 필요한 조건을 볼 수 있습니다.");
                showcaseView.setButtonText("다음");
                break;
            case 3:
                showcaseView.setShowcase(t4, true);
                showcaseView.setContentTitle("이름");
                showcaseView.setContentText("클릭하여서 사용자의 이름을 입력하세요.");
                showcaseView.setButtonText("다음");
                break;
            case 4:
                showcaseView.setShowcase(t5, true);
                showcaseView.setContentTitle("Gold");
                showcaseView.setContentText("목표 성공과 실패 여부에 따라 베팅한 금액만큼 얻거나 잃습니다.");
                showcaseView.setButtonText("다음");
                break;
            case 5:
                showcaseView.setShowcase(t6, true);
                showcaseView.setContentTitle("목표 달성률");
                showcaseView.setContentText("목표 달성률을 보여주며 클릭시 자세한 정보를 보여줍니다.");
                showcaseView.setButtonText("다음");
                break;
            case 6:
                showcaseView.setShowcase(t7, true);
                showcaseView.setContentTitle("오늘의 목표");
                showcaseView.setContentText("클릭하여서 오늘의 목표를 입력하세요.\n길게 클릭하여서 목표를 삭제할 수 있습니다.");
                showcaseView.setButtonText("다음");
                break;
            case 7:
                showcaseView.setShowcase(t8, true);
                showcaseView.setContentTitle("오늘의 목표 관리");
                showcaseView.setContentText("클릭하여서 목표의 수행량을 입력하세요.");
                showcaseView.setButtonText("시작");
                break;
            case 8:
                showcaseView.hide();
                dataController.setPreferencesIsFirstOpenApp(context, 0);
                contador = 0;
                break;
        }
        contador++;
    }
}