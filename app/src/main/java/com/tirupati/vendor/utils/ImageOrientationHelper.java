package com.tirupati.vendor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class ImageOrientationHelper {

    public static int getOrientation(Context context, Uri photoUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(photoUri);
            ExifInterface exifInterface;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                exifInterface = new ExifInterface(inputStream);
            } else {
                exifInterface = new ExifInterface(photoUri.getPath());
            }
            return exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED
            );
        } catch (IOException e) {
            Log.d("ImageOrientationHelper", "catch::");
            e.printStackTrace();
            return ExifInterface.ORIENTATION_UNDEFINED;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}

