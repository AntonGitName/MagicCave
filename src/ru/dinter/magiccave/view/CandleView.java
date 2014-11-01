package ru.dinter.magiccave.view;

import java.util.Random;

import ru.dinter.magiccave.model.CandleModel;
import ru.dinter.magiccave.model.CandleModel.CandleState;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CandleView {

    private static final Random RND = new Random();

    public CandleView(CandleModel model, Bitmap candleBitmap, Bitmap[] fireBitmaps) {
        super();
        this.model = model;
        this.animationTimer = RND.nextInt(MAX_ANIMATION);
        this.candleBitmap = candleBitmap;
        this.fireBitmaps = fireBitmaps;
    }

    private final CandleModel model;
    private int animationTimer;
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int MAX_ANIMATION = 50;

    private final Bitmap candleBitmap;
    private final Bitmap[] fireBitmaps;

    public void draw(Canvas canvas, int w, int h) {
        animationTimer += 1;
        if (animationTimer == MAX_ANIMATION) {
            animationTimer = 0;
        }
        float x = w * model.getX() - candleBitmap.getWidth() / 2;
        float y = h * model.getY() - candleBitmap.getHeight() / 2 + fireBitmaps[0].getHeight() / 2;
        canvas.drawBitmap(candleBitmap, x, y, paint);
        if (model.getState() == CandleState.ON) {
            x += (candleBitmap.getWidth() - fireBitmaps[0].getWidth()) / 2;
            y -= fireBitmaps[0].getHeight();
            canvas.drawBitmap(fireBitmaps[animationTimer * fireBitmaps.length / MAX_ANIMATION], x, y, paint);
        }
    }

    public CandleModel getModel() {
        return model;
    }
}
