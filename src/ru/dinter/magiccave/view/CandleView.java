package ru.dinter.magiccave.view;

import java.util.Random;

import ru.dinter.magiccave.model.CandleModel;
import ru.dinter.magiccave.model.CandleModel.CandleState;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
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
	private final Rect heatbox;
	private CandleFireState state;

	private final int screenWidth;
	private final int screenHeight;
	
	private enum CandleFireState {
		INFLAMING, BURNING, FADING
	}

	static {
		CandleView.paint.setStyle(Style.STROKE);
	}
	
	public CandleView(CandleModel model, Bitmap candleBitmap, Bitmap[] fireBitmaps, boolean randomFlag, int screenWidth, int screenHeight) {
		super();
		this.model = model;
		this.animationTimer = RND.nextInt(MAX_ANIMATION);
		this.fireBitmaps = fireBitmaps;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		if (randomFlag) {
			this.candleBitmap = randomRotatedCandle(candleBitmap);
		} else {
			this.candleBitmap = candleBitmap;
		}

		int x = (int) (screenWidth * model.getX());
		int y = (int) (screenHeight * model.getY());
		int width = Math.max(candleBitmap.getWidth(), fireBitmaps[0].getWidth());
		int height = candleBitmap.getHeight() + fireBitmaps[0].getHeight();
		this.heatbox = new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
	}

	private Bitmap randomRotatedCandle(Bitmap source) {
		Matrix matrix = new Matrix();
		float angle = (RND.nextFloat() - 0.5f) * MAX_ANGLE * 2;
		Log.d(TAG, "Rotated by: " + angle);
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	@Deprecated
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

	public void draw(Canvas canvas) {
		if (++animationTimer == MAX_ANIMATION) {
			animationTimer = 0;
		}
		float x = screenWidth * model.getX() - candleBitmap.getWidth() / 2;
		float y = screenHeight * model.getY() - candleBitmap.getHeight() / 2 + fireBitmaps[0].getHeight() / 2;
		canvas.drawBitmap(candleBitmap, x, y, paint);
		// canvas.drawRect(heatbox, paint);
		if (model.getState() == CandleState.ON) {
			x += (candleBitmap.getWidth() - fireBitmaps[0].getWidth()) / 2;
			y -= fireBitmaps[0].getHeight();
			canvas.drawBitmap(fireBitmaps[animationTimer * fireBitmaps.length / MAX_ANIMATION], x, y, paint);

		}
	}
	
	public boolean isTouched(int x, int y) {
		return heatbox.contains(x, y);
	}

	public void changeState() {
		model.forceInvertState();
		switch (model.getState()) {
		case ON:
			state = CandleFireState.INFLAMING;
			break;
		case OFF:
			state = CandleFireState.FADING;
			break;
		}
	}

	public CandleModel getModel() {
		return model;
	}
}
