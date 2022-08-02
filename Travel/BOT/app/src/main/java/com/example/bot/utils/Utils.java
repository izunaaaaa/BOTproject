package com.example.bot.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class Utils {

    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static GradientDrawable getRoundRectangle(int color, int cornerInDP) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(color);
        gd.setCornerRadius(dpToPx(cornerInDP));
        return gd;
    }

    public static GradientDrawable getRoundRectangle(int color, int cornerInDP, float strokeWidthInDP, int strokeColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(color);
        gd.setCornerRadius(dpToPx(cornerInDP));
        if (strokeWidthInDP > 0) gd.setStroke(dpToPx(strokeWidthInDP), strokeColor);
        return gd;
    }

    //Vector 이미지를 bitmap 으로 변환
    @SuppressLint("RestrictedApi")
    public static Bitmap getBitmapFromVector(Context context, int resId, int sizeInDP){
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, resId);
        if (drawable == null) drawable = new ColorDrawable(Color.TRANSPARENT);

        int size = dpToPx(sizeInDP);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
