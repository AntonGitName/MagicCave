package ru.dinter.magiccave.view;

import java.util.Timer;
import java.util.TimerTask;

import ru.dinter.magiccave.MainActivity;
import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.model.CandleGraphBuilderException;
import ru.dinter.magiccave.model.CandleModel;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CaveView extends View {

	private static final String TAG = "CaveView";
	private static final float CANDLE_RATE = 0.2f;
	
	private CandleGraphView candles;
    private ResourceLoader rl = null;
    private boolean isResourcesSet = false;
    private Point size;
    private Timer timer;

	public CaveView(Context context, AttributeSet attrs) {
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
        	candles.draw(canvas, size.x, size.y);
        }
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (rl != null) {
            isResourcesSet = true;
            size.x = (int) (w * CANDLE_RATE);
            size.y = (int) (h * CANDLE_RATE);
            try {
				candles = new CandleGraphView(rl, w, h, size);
			} catch (CandleGraphBuilderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        size.x = w;
        size.y = h;
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isResourcesSet) {
			float x = event.getX() / size.x;
			float y = event.getY() / size.y;
			candles.clickXY(x, y);
			invalidate();
		}
		return false;
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
