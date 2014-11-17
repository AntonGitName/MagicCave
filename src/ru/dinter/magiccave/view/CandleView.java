package ru.dinter.magiccave.view;

import java.util.Random;

import ru.dinter.magiccave.model.CandleModel;
import ru.dinter.magiccave.model.CandleModel.CandleState;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

public class CandleView {

	private static final String TAG = "CandleView";

	private static final Random RND = new Random();
	private final CandleModel model;
	private int animationTimer;
	private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final int MAX_ANIMATION = 50;
	private static final float MAX_ANGLE = 15.0f;

	private final Bitmap candleBitmap;
	private final Bitmap[] fireBitmaps;

	public CandleView(CandleModel model, Bitmap candleBitmap, Bitmap[] fireBitmaps, boolean randomFlag) {
		super();
		this.model = model;
		this.animationTimer = RND.nextInt(MAX_ANIMATION);
		this.fireBitmaps = fireBitmaps;
		if (randomFlag) {
			this.candleBitmap = randomRotatedCandle(candleBitmap);
		} else {
			this.candleBitmap = candleBitmap;
		}

		paint.setStyle(Style.STROKE);
	}

	private Bitmap randomRotatedCandle(Bitmap source) {
		Matrix matrix = new Matrix();
		float angle = (RND.nextFloat() - 0.5f) * MAX_ANGLE * 2;
		Log.d(TAG, "Rotated by: " + angle);
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public void draw(Canvas canvas, int w, int h) {
		if (++animationTimer == MAX_ANIMATION) {
			animationTimer = 0;
		}
		float x = w * model.getX() - candleBitmap.getWidth() / 2;
		float y = h * model.getY() - candleBitmap.getHeight() / 2 + fireBitmaps[0].getHeight() / 2;
		canvas.drawBitmap(candleBitmap, x, y, paint);
		// canvas.drawRect(x, y, candleBitmap.getWidth() + x,
		// candleBitmap.getHeight() + y, paint);
		if (model.getState() == CandleState.ON) {
			x += (candleBitmap.getWidth() - fireBitmaps[0].getWidth()) / 2;
			y -= fireBitmaps[0].getHeight();
			canvas.drawBitmap(fireBitmaps[animationTimer * fireBitmaps.length / MAX_ANIMATION], x, y, paint);
			// canvas.drawRect(x, y, fireBitmaps[0].getWidth() + x,
			// fireBitmaps[0].getHeight() + y, paint);
		}
	}

	public CandleModel getModel() {
		return model;
	}
}
