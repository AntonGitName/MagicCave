package ru.dinter.magiccave.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class CaveView extends View {

    public CaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Style.FILL);
        candlePaint = new Paint();
        candlePaint.setColor(Color.YELLOW);
        candlePaint.setStyle(Style.FILL);
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Style.FILL);
        linePaint.setStrokeWidth(3);
        candles = new CandlesView();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        
        candles.draw(canvas, candlePaint, linePaint, width, height);
    }

    private Paint backgroundPaint;
    private Paint candlePaint;
    private Paint linePaint;
    private int width;
    private int height;
    private CandlesView candles;
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        
    }

    
    
}
