package ru.dinter.magiccave.view;

import java.util.Timer;
import java.util.TimerTask;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.model.CandleModel;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BigCandleView extends View {

    private static final String TAG = "BigCandleView";
    private static final float CANDLE_RATE = 0.4f; 
    
    private boolean isResourcesSet = false;
    private CandleView candle;
    private ResourceLoader rl = null;
    private Point size;
    private Timer timer;
    
    public BigCandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        size = new Point();
        setWillNotDraw(false);
    }

    public void setResources(ResourceLoader rl) {
        Log.d(TAG, "Setting resources");
        this.rl = rl;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isResourcesSet) {
            candle.draw(canvas, size.x, size.y);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (rl != null) {
            isResourcesSet = true;
            size.x = (int) (w * CANDLE_RATE);
            size.y = (int) (h * CANDLE_RATE);
            candle = new CandleView(new CandleModel(0.5f, 0.5f), rl.getCandleBitmap(size), rl.getFireBitmaps(size));
        }
        size.x = w;
        size.y = h;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        final Handler handler = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }, 0, MainActivity.UPDATE_RATE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
        timer.purge();
    }
    
    
}
