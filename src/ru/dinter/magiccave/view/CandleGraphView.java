package ru.dinter.magiccave.view;

import java.util.Random;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.model.CandleGraph;
import ru.dinter.magiccave.model.CandleGraphBuilder;
import ru.dinter.magiccave.model.CandleGraphBuilderException;
import ru.dinter.magiccave.model.CandleModel;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class CandleGraphView {

	private static final String TAG = "CandleGraphView";

	private static final int DEFAULT_CANDLES_NUM = 15;
	private static final int MIN_CANDLES_NUM = 6;
	private static final int MAX_CANDLES_NUM = 15;

	private static final float NEAR_DISTANCE = 0.002f;
	
	private final CandleGraph candlesTree;
	private final CandleView[] candles;

	private static final Random RND = new Random();

	public CandleGraphView(ResourceLoader rl) {
		candlesTree = CandleGraphBuilder.createRandom(DEFAULT_CANDLES_NUM);
		candlesTree.shuffle();
		CandleModel[] candleModels = candlesTree.getCandles();
		candles = new CandleView[candleModels.length];

		for (int i = 0; i < candles.length; ++i) {
			candles[i] = new CandleView(candleModels[i], rl.getCandleBitmap(new Point(30, 40)),
					rl.getFireBitmaps(new Point(20, 40)));
		}
	}

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
			candles[i] = new CandleView(candleModels[i], candleBitmap, fireBitmaps);
		}
	}

	public void clickXY(float x, float y) {
		CandleModel[] candles = candlesTree.getCandles();
		CandleModel bestCandle = candles[0];
		float bestDist = bestCandle.distanceTo(x, y);
		float tempDist;
		for (int i = 0; i < candles.length; ++i) {
			tempDist = candles[i].distanceTo(x, y);
			if (tempDist < bestDist) {
				bestDist = tempDist;
				bestCandle = candles[i];
			}
		}
		if (bestDist <= NEAR_DISTANCE) {
			bestCandle.forceInvertState();
		}
	}

	public void draw(Canvas canvas, Paint linePaint, int w, int h) {
		for (CandleView candle : candles) {

			candle.draw(canvas, w, h);

			CandleModel m = candle.getModel();
			float x1 = w * m.getX();
			float y1 = h * m.getY();
			float x2;
			float y2;
			for (CandleModel neighbour : m.getNeighbours()) {
				x2 = w * neighbour.getX();
				y2 = h * neighbour.getY();
				canvas.drawLine(x1, y1, x2, y2, linePaint);
			}

		}
	}

	public void draw(Canvas canvas, int w, int h) {
		for (CandleView candle : candles) {
			candle.draw(canvas, w, h);
		}
	}

}
