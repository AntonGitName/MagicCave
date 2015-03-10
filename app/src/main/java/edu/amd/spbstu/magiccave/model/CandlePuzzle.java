package edu.amd.spbstu.magiccave.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iAnton on 09/03/15.
 */
public class CandlePuzzle {

    private final List<CandleModel> candles;
    private final int gridW;
    private final int gridH;

    public CandlePuzzle(List<CandleModel> candles, int w, int h) {
        this.candles = candles;
        gridW = w;
        gridH = h;
    }

    public int getGridW() {
        return gridW;
    }

    public int getGridH() {
        return gridH;
    }

    public List<Integer> getSolution() {
        List<Integer> solution = new ArrayList<>();
        for (CandleModel candle : candles) {
            if (!candle.isInversedCorrectly()) {
                solution.add(candle.getId());
            }
        }
        return solution;
    }

    public boolean isSolved() {
        for (CandleModel candle : candles) {
            if (candle.getState() == CandleModel.State.OFF) {
                return false;
            }
        }
        return true;
    }

    public List<CandleModel> getCandles() {
        return candles;
    }
}
