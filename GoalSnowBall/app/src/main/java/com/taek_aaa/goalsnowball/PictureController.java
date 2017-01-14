package com.taek_aaa.goalsnowball;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

/**
 * Created by taek_aaa on 2017. 1. 7..
 */

public class PictureController {      //최신폰은 버그로 exif 제대로 안되는듯 다이알로그만들어서 직접 수동 회전 하고 선택 할 수있게 하기
    public int exifOrientationToDegrees(int exifOrientation) {

        Log.e("test", "" + exifOrientation);
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }

    //회전하는 거 그냥 뭐 Matrix.rotate이런거찾아보기 이미 구현된건 없으려나 사이즈가 너무 이상하게바뀜
    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환
            }
        }
        return bitmap;
    }
}
