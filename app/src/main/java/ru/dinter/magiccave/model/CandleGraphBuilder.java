package ru.dinter.magiccave.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public final class CandleGraphBuilder {

    private static final int MAX_NEIGHBOUR_RAND = 4;

    private static final Random RND = new Random();

    private static void connect(CandleModel[] candles, int toAdd) {
        int bestI = 0;
        int bestJ = toAdd;
        double bestDist = candles[bestI].distanceTo(candles[bestJ]);
        double tempDist;
        for (int i = 0; i < toAdd; ++i) {
            for (int j = toAdd; j < candles.length; ++j) {
                tempDist = candles[i].distanceTo(candles[j]);
                if (tempDist < bestDist) {
                    bestDist = tempDist;
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        candles[bestI].connect(candles[bestJ]);
        CandleModel tmp = candles[bestJ];
        candles[bestJ] = candles[toAdd];
        candles[toAdd] = tmp;
    }

    public static CandleGraph createRandom(int w, int h, int n) throws CandleGraphBuilderException {
    	
    	if (w * h < n) {
    		throw new CandleGraphBuilderException("Too small grid for this random CandleGraph");
    	}
    	
    	CandleModel[] candles = new CandleModel[n];
    	
    	Set<Integer> rects = new TreeSet<>();
        while (rects.size() < n) {
            int k = RND.nextInt(w * h);
            rects.add(k);
        }
        
        float x;
        float y;
        int i = 0;
        for (int k : rects) {
        	x = (k % w) / (float) w + 1.0f / (2.0f * w);
            y = (k / w) / (float) h + 1.0f / (2.0f * h);
            candles[i++] = new CandleModel(x, y);
        }
        
        for (i = 1; i < n; ++i) {
            connect(candles, i);
        }

        for (i = 0; i < n; ++i) {
            List<CandleModel> nearestCandles = new ArrayList<>();
            for (int j = i + 1; j < n; ++j) {
                nearestCandles.add(candles[j]);
            }
            final CandleModel curCandle = candles[i];
            Collections.sort(nearestCandles, new Comparator<CandleModel>() {

                @Override
                public int compare(CandleModel arg0, CandleModel arg1) {
                    return Double.compare(curCandle.distanceTo(arg0), curCandle.distanceTo(arg1));
                }
            });
            int toAdd = RND.nextInt(MAX_NEIGHBOUR_RAND);
            for (int j = 0; j < toAdd && j < nearestCandles.size(); ++j) {
                if (!curCandle.getNeighbours().contains(nearestCandles.get(j))) {
                    curCandle.connect(nearestCandles.get(j));
                }
            }
        }

        return new CandleGraph(candles);
    }
    
    public static CandleGraph createRandom(int n) {
        CandleModel[] candles = new CandleModel[n];

        Set<Integer> rects = new TreeSet<>();
        while (rects.size() < n) {
            int k = RND.nextInt(n * n);
            if (k % n == 0 || k / n == 0 || k % n == n - 1 || k / n == n - 1) {
                continue;
            }
            rects.add(k);
        }
        float x;
        float y;
        Iterator<Integer> it = rects.iterator();
        for (int i = 0; i < n; ++i) {
            int k = it.next();
            x = (k / n) / (float) n + (RND.nextFloat() - 0.5f) / n;
            y = (k % n) / (float) n + (RND.nextFloat() - 0.5f) / n;
            candles[i] = new CandleModel(x, y);
        }
        for (int i = 1; i < n; ++i) {
            connect(candles, i);
        }

        for (int i = 0; i < n; ++i) {
            List<CandleModel> nearestCandles = new ArrayList<>();
            for (int j = i + 1; j < n; ++j) {
                nearestCandles.add(candles[j]);
            }
            final CandleModel curCandle = candles[i];
            Collections.sort(nearestCandles, new Comparator<CandleModel>() {

                @Override
                public int compare(CandleModel arg0, CandleModel arg1) {
                    return Double.compare(curCandle.distanceTo(arg0), curCandle.distanceTo(arg1));
                }
            });
            int toAdd = RND.nextInt(MAX_NEIGHBOUR_RAND);
            for (int j = 0; j < toAdd && j < nearestCandles.size(); ++j) {
                if (!curCandle.getNeighbours().contains(nearestCandles.get(j))) {
                    curCandle.connect(nearestCandles.get(j));
                }
            }
        }

        return new CandleGraph(candles);
    }

}
