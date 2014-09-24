package ru.dinter.magiccave.model;

import java.util.Random;

public final class CandleGraph {
    
    private static final Random RND = new Random();

    private static final int SHUFFLE_RATE = 1000;
    private final CandleModel candles[];
    
    private final boolean solution[];
    public CandleGraph(CandleModel candles[]) {
        super();
        this.candles = candles;
        solution = new boolean[candles.length];
    }
    
    public CandleModel[] getCandles() {
        return candles;
    }
    
    public void shuffle() {
        int n;
        for (int i = 0; i < SHUFFLE_RATE; ++i) {
            n = RND.nextInt(candles.length);
            candles[n].forceInvertState();
            solution[n] ^= true;
        }
    }

    public boolean[] solve() {
        return solution.clone();
    }
    
}
