package ru.dinter.magiccave.view;

import java.util.Random;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.model.CandleGraph;
import ru.dinter.magiccave.model.CandleGraphBuilder;
import ru.dinter.magiccave.model.CandleGraphBuilderException;
import ru.dinter.magiccave.model.CandleModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

public class CandleGraphView {

	private static final String TAG = "CandleGraphView";

	private static final int MIN_CANDLES_NUM = 6;
	private static final int MAX_CANDLES_NUM = 15;
	
	private final CandleGraph candlesTree;
	private final CandleView[] candles;

	private static final Random RND = new Random();

	public CandleGraphView(ResourceLoader rl, int screenWidth, int screenHeight, Point candleSize)
			throws CandleGraphBuilderException {
		int candleNum = RND.nextInt(MAX_CANDLES_NUM - MIN_CANDLES_NUM + 1) + MIN_CANDLES_NUM;
		
		Bitmap candleBitmap = rl.getCandleBitmap(new Point(candleSize.x, candleSize.y / 2));
		Bitmap[] fireBitmaps = rl.getFireBitmaps(new Point(candleSize.x, candleSize.y / 2));
		
		candleSize.x = Math.max(candleBitmap.getWidth(), fireBitmaps[0].getWidth());
		candleSize.y = candleBitmap.getHeight() + fireBitmaps[0].getHeight();
		
		Log.d(TAG, "Number of generated candles: " + candleNum);
		Log.d(TAG, String.format("Grid size: (%d, %d)", screenWidth / candleSize.x, screenHeight / candleSize.y));
		candlesTree = CandleGraphBuilder.createRandom(screenWidth / candleSize.x, screenHeight / candleSize.y,
				candleNum);
		candlesTree.shuffle();
		CandleModel[] candleModels = candlesTree.getCandles();
		candles = new CandleView[candleModels.length];

		for (int i = 0; i < candles.length; ++i) {
			candles[i] = new CandleView(candleModels[i], candleBitmap, fireBitmaps, true, screenWidth, screenHeight);
		}
	}

	public void clickXY(float x, float y) {
		for (CandleView candleView : candles) {
			if (candleView.isTouched((int)x, (int)y)) {
				candleView.changeState();
				return;
			}
		}
	}
	
	public void draw(Canvas canvas) {
		for (CandleView candle : candles) {
			candle.draw(canvas);
		}
	}
}
