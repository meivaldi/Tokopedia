package com.tokopedia.testproject.problems.androidView.waterJugSimulation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class WaterJugView extends View {

    private int maxWater = 0;
    private int waterFill = 0;

    public WaterJugView(Context context) {
        super(context);
    }

    public WaterJugView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterJugView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaterJugView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMaxWater(int maxWater) {
        this.maxWater = maxWater;
    }

    public void setWaterFill(int waterFill) {
        this.waterFill = waterFill;
    }

    //TODO
    /*
    Based on these variables: maxWater and waterFill, draw the jug with the water

    Example a:
    maxWater = 10
    waterFill = 0

    Result,
    View will draw like below
    |        |
    |        |
    |        |
    |        |
    `--------'

    Example b:
    maxWater = 10
    waterFill = 5

    Result,
    View will draw like below
    |        |
    |        |
    |--------|
    |        |
    `--------'

    Example c:
    maxWater = 10
    waterFill = 8

    Result,
    View will draw like below
    |        |
    |--------|
    |        |
    |        |
    `--------'

    Example d:
    maxWater = 10
    waterFill = 10

    Result,
    View will draw like below
     ________
    |        |
    |        |
    |        |
    |        |
    `--------'
    */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int lineHeight = 20;
        float strokeWidth = 3.0f;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(strokeWidth);

        canvas.drawLine(0, 0, 0, maxWater*lineHeight, paint);
        canvas.drawLine(0, maxWater*lineHeight, (maxWater*lineHeight), maxWater*lineHeight, paint);
        canvas.drawLine(maxWater*lineHeight, maxWater*lineHeight, maxWater*lineHeight, 0, paint);

        paint.setColor(Color.rgb(0, 255,255));
        paint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect((int)strokeWidth, (maxWater*lineHeight) - (waterFill*lineHeight),
                (maxWater*lineHeight) - (int)strokeWidth, maxWater*lineHeight);

        canvas.drawRect(rect, paint);
    }
}
