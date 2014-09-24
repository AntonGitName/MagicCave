package ru.dinter.magiccave.view;

import ru.dinter.magiccave.model.CandleGraph;
import ru.dinter.magiccave.model.CandleGraphBuilder;
import ru.dinter.magiccave.model.CandleModel;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CandleGraphView {

    private static final int DEFAULT_CANDLES_NUM = 15;
    
    private final CandleGraph candlesTree;
    private final CandleView[] candles;
  
    public CandleGraphView() {
        candlesTree = CandleGraphBuilder.createRandom(DEFAULT_CANDLES_NUM);
        candlesTree.shuffle();
        CandleModel[] candleModels = candlesTree.getCandles();
        candles = new CandleView[candleModels.length];
        
        for (int i = 0; i < candles.length; ++i) {
            candles[i] = new CandleView(candleModels[i]);
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
        if (bestDist <= 0.001) {
            bestCandle.forceInvertState();
        }
    }
    
    public void draw(Canvas canvas, Paint linePaint, int w, int h) {
        for (CandleView candle : candles) {
            
            
            candle.draw(canvas, w, h);/*
            CandleModel m = candle.getModel();
            float x1 = w * m.getX();
            float y1 = h * m.getY();
            float x2;
            float y2;
            for (CandleModel neighbour : m.getNeighbours()) {
                x2 = w * neighbour.getX();
                y2 = h * neighbour.getY();
                canvas.drawLine(x1, y1, x2, y2, linePaint);
            }*/
        }
    }
}
