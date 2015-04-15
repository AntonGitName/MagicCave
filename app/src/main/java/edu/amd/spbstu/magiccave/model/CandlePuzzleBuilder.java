package edu.amd.spbstu.magiccave.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class CandlePuzzleBuilder {

    private static final Random RND = new Random();
    private static final float EDGE_PROBABILITY = 0.25f;
    private static final float INVERSE_PROBABILITY = 0.5f;
    private static final int MAX_ITERATIONS = 200;
    private static final String TAG = CandlePuzzleBuilder.class.getSimpleName();

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

    @Deprecated
    public static CandlePuzzle buildOld(int w, int h, int n) {

        if (w * h < n) {
            throw new IllegalArgumentException("Grid is too small");
        }

        final List<CandleModel> candles = new ArrayList<>(n);

        final Set<Integer> rects = new TreeSet<>();
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

    public static CandlePuzzle build(int w, int h, int n) {
        final Map<Integer, List<Integer>> graph = new TreeMap<>();
        for (int i = 0; i < n; ++i) {
            final List<Integer> list = new ArrayList<>();
            if (i != 0) {
                list.add(i - 1);
            }
            if (i != n - 1) {
                list.add(i + 1);
            }
            graph.put(i, list);
        }
        for (int i = 0; i < n; ++i) {
            final List<Integer> candidates = new ArrayList<>(n);
            for (int j = 0; j < n; ++j) {
                if (j != i && !graph.get(i).contains(j)) {
                    candidates.add(j);
                }
            }
            int l = 0;
            while (l != candidates.size() && (RND.nextFloat() < EDGE_PROBABILITY)) {
                final int x = candidates.get(l++);
                graph.get(i).add(x);
                graph.get(x).add(i);
            }
        }

        List<Point> bestPoints = null;
        List<Point> points;
        int linesCount = 0;
        int intersections = Integer.MAX_VALUE;
        int minIntersections = Integer.MAX_VALUE;
        for (int i = 0; i < MAX_ITERATIONS && intersections != 0; ++i) {
            intersections = 0;
            points = generatePoints(w, h, n);
            Set<Line> lines = new HashSet<>(n * 2);
            for (int j = 0; j < points.size(); ++j) {
                List<Integer> others = graph.get(j);
                for (int other : others) {
                    lines.add(new Line(points.get(j), points.get(other)));
                }
            }
            for (Line line : lines) {
                for (Line other : lines) {
                    if (line != other && line.intersects(other)) {
                        ++intersections;
                        if (Geom.interlaps(line, other)) {
                            intersections += 100;
                        }
                    }
                }
            }
            if (intersections < minIntersections) {
                bestPoints = points;
                minIntersections = intersections;
                linesCount = lines.size();
            }
        }

        Log.d(TAG, "# of lines :" + linesCount);
        Log.d(TAG, "least # of intersections is :" + minIntersections);

        final List<CandleModel> candles = new ArrayList<>(n);
        for (Point point : bestPoints) {
            candles.add(new CandleModel(point.getX(), point.getY()));
        }

        for (int i = 0; i < n; ++i) {
            for (int j : graph.get(i)) {
                candles.get(i).addNeighbour(candles.get(j));
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

    private static List<Point> generatePoints(int w, int h, int n) {
        final List<Point> result = new ArrayList<>(n);
        final Set<Integer> rects = new TreeSet<>();

        while (rects.size() < n) {
            int k = RND.nextInt(w * h);
            rects.add(k);
        }

        for (int k : rects) {
            result.add(new Point(k % w, k / w));
        }

        return result;
    }

}
