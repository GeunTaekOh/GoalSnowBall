package com.taek_aaa.goalsnowball;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final int PICK_FROM_ALBUM = 101;
    Bitmap photo;
    public static LayoutInflater inflater;
    ImageButton imageButton;
    int viewHeight = 700;        //원하는 뷰의 높이
    Boolean isPicture = false;

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

        imageButton = (ImageButton) findViewById(R.id.mainImageView);
        imageButton.setBackgroundResource(R.drawable.addpicture32);
    }

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_todayGoalSetting) {
            (new TodayGoalDialog(MainActivity.this)).show();
        } else if (id == R.id.nav_weekGoalSetting) {

        } else if (id == R.id.nav_monthGoaslSetting) {

        } else if (id == R.id.nav_totalGoalSetting) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
            imageButton = (ImageButton) findViewById(R.id.mainImageView);
            imageButton.setImageBitmap(rotatedPicture);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                pictureSetToImageButton(data);
                break;
        }
    }

    protected void pictureSetToImageButton(Intent data) {
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

            float width = rotatedPhoto.getWidth();
            float height = rotatedPhoto.getHeight();
            if (height > viewHeight) {
                float percente = height / 100;
                float scale = viewHeight / percente;
                width *= scale / 100;
                height *= scale / 100;
            }
            Bitmap sizedPhoto = Bitmap.createScaledBitmap(rotatedPhoto, (int) width, (int) height, true);

            imageButton = (ImageButton) findViewById(R.id.mainImageView);
            imageButton.setImageBitmap(sizedPhoto);
            Toast.makeText(getBaseContext(), "사진을 입력하였습니다.", Toast.LENGTH_SHORT).show();
            isPicture = true;
        } catch (Exception e) {
            e.getStackTrace();
            isPicture = false;
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainTodayGoalTv:
                (new TodayGoalDialog(MainActivity.this)).show();
                break;
            case R.id.mainWeekGoalTv:
                break;
            case R.id.mainMonthGoalTv:
                break;
        }
    }
}
