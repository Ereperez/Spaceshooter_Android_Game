package com.ereperez.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class Utils {
    static final Matrix matrix = new Matrix();

    public static Bitmap flipBitmap(final Bitmap src, final boolean horizontal){
        matrix.reset();
        final int cx = src.getWidth()/2;
        final int cy = src.getHeight()/2;
        if(horizontal){
            matrix.postScale(1, -1, cx, cy);
        }else{
            matrix.postScale(-1, 1, cx, cy);
        }
        return Bitmap.createBitmap(src,0,0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap scaleToTargetHeight(final Bitmap src, final int targetHeight) {
        float ratio = targetHeight / (float) src.getHeight();
        int newH = (int) (src.getHeight() * ratio);
        int newW = (int) (src.getWidth() * ratio);
        return Bitmap.createScaledBitmap(src, newW, newH, true);
    }

    public static float wrap(float val, final float min, final float max){
        if(val < min){
            val = max;
        }else if (val > max){
            val = min;
        }
        return val;
    }

    public static float clamp(float val, final float min, final float max){
        if (val > max){
            val = max;
        } else if (val < min){
            val = min;
        }
        return val;
    }

    public static Bitmap colorBitmap(final Bitmap src, final int color){
        Bitmap resultBitmap = src.copy(src.getConfig(),true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }
}
