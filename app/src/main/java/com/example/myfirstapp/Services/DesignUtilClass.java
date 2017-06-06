package com.example.myfirstapp.Services;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Created by shubhajain on 5/27/2017.
 */

public class DesignUtilClass {
    public static ShapeDrawable drawCircle(Context context, int width, int height, int color){
        ShapeDrawable oval = new ShapeDrawable(new OvalShape());
        oval.setIntrinsicHeight(height);
        oval.setIntrinsicHeight(width);
        oval.getPaint().setColor(color);
        return oval;
    }
}
