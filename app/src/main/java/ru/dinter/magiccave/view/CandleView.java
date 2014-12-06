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
import android.nfc.cardemulation.OffHostApduService;
import android.util.Log;

public class CandleView {

	private static final String TAG = "CandleView";

	private static final Random RND = new Random();

	private final CandleModel model;
	private int animationTimer;
	private int subAnimationTimer;

	private static final Paint candlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final int MAX_ANIMATION = 50;
	private static final float MAX_ANGLE = 15.0f;

	private final Bitmap candleBitmap;
	private final Bitmap[] fireBitmaps;
	private final Rect heatbox;
	private CandleFireState fireState;

	private final int screenWidth;
	private final int screenHeight;
	private final Paint firePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private enum CandleFireState {
		INFLAMING, BURNING, FADING, OFF;
		public CandleFireState nextState() {
			switch (this) {
			case INFLAMING:
				return BURNING;
			case BURNING:
				return FADING;
			case FADING:
				return OFF;
			case OFF:
				return INFLAMING;
			}
			return OFF;
		}
		
		public CandleFireState anotherState() {
			switch (this) {
			case INFLAMING:
			case BURNING:
				return FADING;
			case FADING:
			case OFF:
				return INFLAMING;
			}
			return OFF;
		}
	}

	static {
		CandleView.candlePaint.setStyle(Style.STROKE);
	}

	public CandleView(CandleModel model, Bitmap candleBitmap, Bitmap[] fireBitmaps, boolean randomFlag,
			int screenWidth, int screenHeight) {
		super();
		this.model = model;
		this.animationTimer = RND.nextInt(MAX_ANIMATION);
		this.fireBitmaps = fireBitmaps;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.firePaint.setAlpha(255);
		
		if (randomFlag) {
			this.candleBitmap = randomRotatedCandle(candleBitmap);
		} else {
			this.candleBitmap = candleBitmap;
			
		}
		switch (model.getState()) {
		case OFF:
			this.fireState = CandleFireState.OFF;
			this.subAnimationTimer = 0;
			break;
		case ON:
			this.fireState = CandleFireState.BURNING;
			this.subAnimationTimer = MAX_ANIMATION;
			break;
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

	public void draw(Canvas canvas) {
		if (++animationTimer == MAX_ANIMATION) {
			animationTimer = 0;
		}
		float x = screenWidth * model.getX() - candleBitmap.getWidth() / 2;
		float y = screenHeight * model.getY() - candleBitmap.getHeight() / 2 + fireBitmaps[0].getHeight() / 2;
		canvas.drawBitmap(candleBitmap, x, y, candlePaint);
		// canvas.drawRect(heatbox, paint);
		switch (fireState) {
		case FADING:
			--subAnimationTimer;
			firePaint.setAlpha(255 * subAnimationTimer / MAX_ANIMATION);
			if (subAnimationTimer <= 0) {
				fireState = fireState.nextState();
				subAnimationTimer = MAX_ANIMATION;
			}
			break;
		case INFLAMING:
			++subAnimationTimer;
			firePaint.setAlpha(255 * subAnimationTimer / MAX_ANIMATION);
			if (subAnimationTimer >= MAX_ANIMATION) {
				fireState = fireState.nextState();
				subAnimationTimer = 0;
			}
			break;
		case OFF:
			subAnimationTimer = 0;
			firePaint.setAlpha(0);
			break;
		case BURNING:
			subAnimationTimer = MAX_ANIMATION;
			firePaint.setAlpha(255);
			break;
		}
		
		if (!checkState()) {
			fireState = fireState.anotherState();
		}
		
		if (fireState != CandleFireState.OFF) {
			x += (candleBitmap.getWidth() - fireBitmaps[0].getWidth()) / 2;
			y -= fireBitmaps[0].getHeight();
			canvas.drawBitmap(fireBitmaps[animationTimer * fireBitmaps.length / MAX_ANIMATION], x, y, firePaint);
		}
	}

	public boolean isTouched(int x, int y) {
		return heatbox.contains(x, y);
	}

	public void changeState() {
		model.forceInvertState();
		switch (model.getState()) {
		case ON:
			fireState = CandleFireState.INFLAMING;
			break;
		case OFF:
			fireState = CandleFireState.FADING;
			break;
		}
	}

	public CandleModel getModel() {
		return model;
	}
	
	private boolean checkState() {
		if (model.getState() == CandleState.ON) {
			return fireState == CandleFireState.BURNING || fireState == CandleFireState.INFLAMING;
		} else {
			return fireState == CandleFireState.OFF || fireState == CandleFireState.FADING;
		}
	}
}
