package edu.amd.spbstu.magiccave.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class CandlePuzzleBuilder {

    private static final Random RND = new Random();
    private static final float EDGE_PROBABILITY = 0.25f;
    private static final float INVERSE_PROBABILITY = 0.5f;

    private static int dist(CandleModel a, CandleModel b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private static void connect(List<CandleModel> candles) {
        final int len = candles.size();
        for (int i = 1; i < len; ++i) {
            final CandleModel toAdd = candles.get(i);
            CandleModel nearest = candles.get(0);
            for (int j = 0; j < i; ++j) {
                final CandleModel tmp = candles.get(j);
                if (dist(nearest, toAdd) > dist(tmp, toAdd)) {
                    nearest = tmp;
                }
            }
            nearest.addNeighbour(toAdd);
            toAdd.addNeighbour(nearest);
        }
    }

    private static int removeEmptyRows(List<CandleModel> candles, int h) {
        Set<Integer> rows = new TreeSet<>();
        for (int i = 0; i < h; ++i) {
            rows.add(i);
        }
        for (CandleModel model : candles) {
            rows.remove(model.getY());
        }
        for (Integer row : rows) {
            for (int i = 0; i < candles.size(); ++i) {
                final CandleModel candle = candles.get(i);
                if (candle.getY() > row) {
                    candles.set(i, new CandleModel(candle.getX(), candle.getY() - 1, candle.getId(), candle.isInvertedCorrectly(), candle.getState().toString()));
                }
            }
        }
        return rows.size();
    }

    private static int removeEmptyColumns(List<CandleModel> candles, int w) {
        Set<Integer> columns = new TreeSet<>();
        for (int i = 0; i < w; ++i) {
            columns.add(i);
        }
        for (CandleModel model : candles) {
            columns.remove(model.getX());
        }
        for (Integer column : columns) {
            for (int i = 0; i < candles.size(); ++i) {
                final CandleModel candle = candles.get(i);
                if (candle.getX() > column) {
                    candles.set(i, new CandleModel(candle.getX() - 1, candle.getY(), candle.getId(), candle.isInvertedCorrectly(), candle.getState().toString()));
                }
            }
        }
        return columns.size();
    }

    public static CandlePuzzle build(long seed) {
        RND.setSeed(seed);
        final int w = 3 + RND.nextInt(4);
        final int h = 2 + RND.nextInt(3);
        final int n = 5 + RND.nextInt(w * h - 5);
        return build(w, h, n);
    }

    public static CandlePuzzle build(int w, int h, int n) {

        if (w * h < n) {
            throw new IllegalArgumentException("Grid is too small");
        }

        final List<CandleModel> candles = new ArrayList<>(n);

        Set<Integer> rects = new TreeSet<>();
        while (rects.size() < n) {
            int k = RND.nextInt(w * h);
            rects.add(k);
        }

        int x;
        int y;
        for (int k : rects) {
            x = (k % w);
            y = (k / w);
            candles.add(new CandleModel(x, y));
        }

        w -= removeEmptyColumns(candles, w);
        h -= removeEmptyRows(candles, h);

        connect(candles);

        for (final CandleModel candle : candles) {
            final List<CandleModel> candidates = new ArrayList<>(candles);
            candidates.remove(candle);
            candidates.removeAll(candle.getNeighbours());

            Collections.sort(candidates, new Comparator<CandleModel>() {
                @Override
                public int compare(CandleModel a, CandleModel b) {
                    return dist(a, candle) - dist(b, candle);
                }
            });

            while (!candidates.isEmpty() && (RND.nextFloat() < EDGE_PROBABILITY)) {
                final CandleModel neighbour = candidates.get(0);
                candidates.remove(neighbour);
                candle.addNeighbour(neighbour);
                neighbour.addNeighbour(candle);
            }
        }

        final CandlePuzzle candlePuzzle = new CandlePuzzle(candles, w, h);

        while (candlePuzzle.isSolved()) {
            for (CandleModel candle : candles) {
                if (RND.nextFloat() < INVERSE_PROBABILITY) {
                    candle.inverseWithNeighbours();
                }
            }
        }
        return candlePuzzle;
    }
}
