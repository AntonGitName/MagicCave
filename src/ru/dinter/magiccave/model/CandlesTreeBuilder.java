package ru.dinter.magiccave.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CandlesTreeBuilder {

    private static void connect(List<CandleModel> list, CandleModel candle) {
        CandleModel bestCandle = list.get(new Random().nextInt(list.size()));
        double bestDist = bestCandle.distanceTo(candle);
        double tempDist;
        for (CandleModel candleA : list) {
            tempDist = candleA.distanceTo(candle);
            if (bestDist > tempDist) {
                bestDist = tempDist;
                bestCandle = candleA;
            }
        }
        bestCandle.connect(candle);
        list.add(candle);
    }
    
    public static CandlesTree createRandom(int n) {
        List<CandleModel> addedCandles = new ArrayList<>();
        CandleModel candles[] = new CandleModel[n];
        candles[0] = new CandleModel();
        addedCandles.add(candles[0]);
        for (int i = 1; i < n; ++i) {
            candles[i] = new CandleModel();
            connect(addedCandles, candles[i]);
        }
        
        return new CandlesTree(candles);
    }
    
}
