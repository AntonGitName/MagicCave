package ru.dinter.magiccave.view;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.model.CandleModel;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BigCandleView extends View {

    private static final String TAG = "BigCandleView";
    private static final float CANDLE_RATE = 0.4f; 
    
    private boolean isResourcesSet = false;
    private CandleView candle;
    private ResourceLoader rl;
    private Point size;
    
    public BigCandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        size = new Point();
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
        isResourcesSet = true;
        size.x = (int) (w * CANDLE_RATE);
        size.y = (int) (h * CANDLE_RATE);
        candle = new CandleView(new CandleModel(0.5f, 0.5f), rl.getCandleBitmap(size), rl.getFireBitmaps(size));
        size.x = w;
        size.y = h;
    }
}
