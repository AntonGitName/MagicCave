package ru.dinter.magiccave.model;

import java.util.Random;

public final class CandlesTree {
    
    public CandlesTree(CandleModel candles[]) {
        super();
        this.candles = candles;
        solution = new boolean[candles.length];
    }

    private final CandleModel candles[];
    private final boolean solution[];
    
    private static final int SHUFFLE_RATE = 1000;
    private static final Random RND = new Random();
    
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
