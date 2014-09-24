package ru.dinter.magiccave.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CaveView extends View {

    private Paint backgroundPaint;

    private Paint candlePaint;
    
    private CandleGraphView candles;
    
    private int height;

    private Paint linePaint;
    private int width;
    public CaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }
    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Style.FILL);
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Style.FILL);
        linePaint.setStrokeWidth(3);
        candles = new CandleGraphView();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        
        candles.draw(canvas, linePaint, width, height);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() / width;
        float y = event.getY() / height;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
        candles.clickXY(x, y);
        invalidate();
    return false;
    }

}
