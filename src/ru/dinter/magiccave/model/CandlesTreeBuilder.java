package ru.dinter.magiccave.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class CandlesTreeBuilder {

    private static final Random RND = new Random();

    private static void connect(Set<CandleModel> setA, Set<CandleModel> setB) {
        CandleModel bestCandleA = setA.iterator().next();
        CandleModel bestCandleB = setB.iterator().next();
        double bestDist = bestCandleA.distanceTo(bestCandleB);
        for (CandleModel candleA : setA) {
            for (CandleModel candleB : setB) {
                double tempDist = candleA.distanceTo(candleB);
                if (bestDist > tempDist) {
                    bestDist = tempDist;
                    bestCandleA = candleA;
                    bestCandleB = candleB;
                }
            }
        }
        bestCandleA.connect(bestCandleB);
    }
    
    public static CandlesTree createRandom(int n) {
        List<Set<CandleModel>> sets = new ArrayList<>();
        CandleModel candles[] = new CandleModel[n];
        for (int i = 0; i < n; ++i) {
            candles[i] = new CandleModel();
            Set<CandleModel> set = new HashSet<>();
            set.add(candles[i]);
            sets.add(set);
        }
        int k = sets.size();
        while (k != 1) {
            int a = RND.nextInt(k);
            int b = RND.nextInt(k);
            while (b == a) {
                b = RND.nextInt(k);
            }
            
            Set<CandleModel> setA = sets.get(a);
            Set<CandleModel> setB = sets.get(b);
            connect(setA, setB);
            --k;
        }
        
        return new CandlesTree(candles);
    }
    
}
