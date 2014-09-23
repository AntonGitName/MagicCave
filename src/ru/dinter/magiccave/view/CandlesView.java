package ru.dinter.magiccave.view;

import ru.dinter.magiccave.model.CandleModel;
import ru.dinter.magiccave.model.CandlesTree;
import ru.dinter.magiccave.model.CandlesTreeBuilder;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CandlesView {

    private static final int DEFAULT_CANDLES_NUM = 15;
    
    private final CandlesTree candlesTree;
    
    public CandlesView() {
        candlesTree = CandlesTreeBuilder.createRandom(DEFAULT_CANDLES_NUM);
        candlesTree.shuffle();
    }
  
    private static final float DEFAULT_RADIUS = 13;
    
    public void draw(Canvas canvas, Paint candlePaint, Paint linePaint, int w, int h) {
        for (CandleModel candle : candlesTree.getCandles()) {
            float x1 = w * candle.getX();
            float y1 = h * candle.getY();
            
            canvas.drawCircle(x1, y1, DEFAULT_RADIUS, candlePaint);
           
            float x2;
            float y2;
            for (CandleModel neighbour : candle.getNeighbours()) {
                x2 = w * neighbour.getX();
                y2 = h * neighbour.getY();
                canvas.drawLine(x1, y1, x2, y2, linePaint);
            }
        }
    }
}
