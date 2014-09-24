package ru.dinter.magiccave.view;

import java.util.Random;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.model.CandleModel;
import ru.dinter.magiccave.model.CandleModel.CandleState;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CandleView {

    private static final Random RND = new Random();
    
    public CandleView(CandleModel model) {
        super();
        this.model = model;
        this.animationTimer = RND.nextInt(MAX_ANIMATION);
    }
    private final CandleModel model;
    private int animationTimer;
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int MAX_ANIMATION = 50;
    
    private static Bitmap candleBitmap;
    private static Bitmap[] fireBitmaps;

    private static final int CANDLE_WIDTH = 20;
    private static final int CANDLE_HEIGHT = 40;
    private static final int FIRE_WIDTH = 20;
    private static final int FIRE_HEIGHT = 40;
    
    public static void loadBitmaps(ResourceLoader rl) {
        Bitmap tmp1 = rl.getCandleBitmap();
        candleBitmap = Bitmap.createScaledBitmap(tmp1, CANDLE_WIDTH, CANDLE_HEIGHT, false);
        Bitmap[] tmp2 = rl.getFireBitmaps();
        fireBitmaps = new Bitmap[tmp2.length];
        for (int i = 0; i < tmp2.length; ++i) {
            fireBitmaps[i] = Bitmap.createScaledBitmap(tmp2[i], FIRE_WIDTH, FIRE_HEIGHT, false);
        }
    }
    
    public void draw(Canvas canvas, int w, int h) {
        animationTimer += 1;
        if (animationTimer == MAX_ANIMATION) {
            animationTimer = 0;
        }
        float x = w * model.getX() - CANDLE_WIDTH / 2;
        float y = h * model.getY() - CANDLE_HEIGHT / 2;
        canvas.drawBitmap(candleBitmap, x, y, paint);
        if (model.getState() == CandleState.ON) {
            x += (CANDLE_WIDTH - FIRE_WIDTH) / 2;
            y -= FIRE_HEIGHT * 3 / 4;
            canvas.drawBitmap(fireBitmaps[animationTimer * fireBitmaps.length / MAX_ANIMATION], x, y, paint);
        }
    }

    public CandleModel getModel() {
        return model;
    }
}
