package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.amd.spbstu.magiccave.model.CandleModel;
import edu.amd.spbstu.magiccave.model.CandlePuzzle;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class GameView extends TableLayout implements CandleView.OnAnimationFinishedListener {

    private static final int DEFAULT_LINE_ALPHA = 200;
    private static final float LINE_WIDTH = 4f;
    private static final Random RND = new Random();

    private static final int[] COLORS = {Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLACK, Color.BLUE, Color.YELLOW};

    private int rows;
    private int columns;
    private List<CandleView> candleViews;
    private List<CandleView> candlesToAnimate;
    private List<PathWithPaint> paths;
    private OnHelpAnimationFinishedListener listener;
    private int lastAnimatedView;
    private boolean isLinesVisible;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPuzzle(CandlePuzzle puzzle, CandleView.OnCandleViewClickListener listener) {
        final List<CandleModel> candles = puzzle.getCandles();
        candleViews = new ArrayList<>(candles.size());
        removeAllViews();
        setLinesVisible(false);

        this.setWillNotDraw(false);

        final TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);

        rows = puzzle.getRows();
        columns = puzzle.getColumns();
        paths = null;

        for (int i = 0; i < rows; ++i) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(layoutParams);
            for (int j = 0; j < columns; ++j) {

                CandleModel haveCandleHere = null;
                for (CandleModel candle : candles) {
                    if (candle.getX() == j && candle.getY() == i) {
                        haveCandleHere = candle;
                        break;
                    }
                }
                if (haveCandleHere != null) {
                    final CandleView candleView = new CandleView(getContext(), haveCandleHere, listener);
                    candleViews.add(candleView);
                    row.addView(candleView);
                } else {
                    row.addView(new View(getContext()));
                }
            }
            addView(row);
        }
    }

    public void showHelpAnimation(List<Integer> solution, OnHelpAnimationFinishedListener listener) {
        final int viewsToAnimate = solution.size();
        if (viewsToAnimate == 0) {
            onAnimationFinished();
            return;
        }
        candlesToAnimate = new ArrayList<>(viewsToAnimate);
        for (int i = 0; i < viewsToAnimate; ++i) {
            candlesToAnimate.add(getCandleWithId(solution.get(i)));
        }
        lastAnimatedView = 0;
        this.listener = listener;
        candlesToAnimate.get(0).playChangeAnimation(this);
    }

    private CandleView getCandleWithId(int id) {
        for (CandleView candleView : candleViews) {
            if (candleView.getModel().getId() == id) {
                return candleView;
            }
        }
        throw new IllegalStateException("Internal id mismatch");
    }

    @Override
    public void onAnimationFinished() {
        if (++lastAnimatedView < candlesToAnimate.size()) {
            candlesToAnimate.get(lastAnimatedView).playChangeAnimation(this);
        } else {
            candlesToAnimate = null;
            lastAnimatedView = 0;
            listener.onHelpAnimationFinished();
        }
    }

    public void setEnabledCandles(boolean isEnabled) {
        for (CandleView candle : candleViews) {
            candle.setEnabled(isEnabled);
        }
    }

    public void setLinesVisible(boolean isLinesVisible) {
        this.isLinesVisible = isLinesVisible;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isLinesVisible && !isEmpty()) {
            if (paths == null) {
                paths = createPaths();
            }
            for (PathWithPaint pathWithPaint : paths) {
                canvas.drawPath(pathWithPaint.path, pathWithPaint.paint);
            }
        }
    }

    private List<PathWithPaint> createPaths() {
        final List<PathWithPaint> paths = new ArrayList<>();
        final Set<Point> pairs = new HashSet<>();
        final int h = getMeasuredHeight();
        final int w = getMeasuredWidth();
        final float dx = 0.5f * w / columns;
        final float dy = 0.5f * h / rows;
        int colorI = 0;
        for (CandleView candleView : candleViews) {
            final int fromX = candleView.getModel().getX();
            final int fromY = candleView.getModel().getY();
            for (CandleModel candleModel : candleView.getModel().getNeighbours()) {
                Point pair = new Point(candleView.getModel().getId(), candleModel.getId());
                if (pairs.contains(pair)) {
                    continue;
                }
                pair = new Point(candleModel.getId(), candleView.getModel().getId());
                if (pairs.contains(pair)) {
                    continue;
                }
                pairs.add(pair);

                final int toX = candleModel.getX();
                final int toY = candleModel.getY();
                final float x1 = getX(fromX, w);
                final float y1 = getY(fromY, h);
                final float x4 = getX(toX, w);
                final float y4 = getY(toY, h);
                float x2, x3, y2, y3;
                if (x1 == x4) {
                    y2 = (y1 + y4) * 0.333f;
                    y3 = (y1 + y4) * 0.666f;
                    x3 = x2 = RND.nextBoolean() ? x1 + dx : x1 - dx;
                } else if (y1 == y4) {
                    x2 = (x1 + x4) * 0.333f;
                    x3 = (x1 + x4) * 0.666f;
                    y3 = y2 = RND.nextBoolean() ? y1 + dy : y1 - dy;
                } else {
                    if (RND.nextBoolean()) {
                        y2 = (y1 + y4) * 0.333f;
                        y3 = (y1 + y4) * 0.666f;
                        if (Math.abs(fromX - toX) % 2 == 1) {
                            x3 = x2 = (x1 + x4) * 0.5f + (RND.nextBoolean() ? dx : -dx);
                        } else {
                            x3 = x2 = (x1 + x4) * 0.5f;
                        }
                    } else {
                        x2 = (x1 + x4) * 0.333f;
                        x3 = (x1 + x4) * 0.666f;
                        if (Math.abs(fromY - toY) % 2 == 1) {
                            y3 = y2 = (y1 + y4) * 0.5f + (RND.nextBoolean() ? dy : -dy);
                        } else {
                            y3 = y2 = (y1 + y4) * 0.5f;
                        }
                    }
                }
                final Path path = new Path();
                path.moveTo(x1, y1);
                path.cubicTo(x2, y2, x3, y3, x4, y4);
                paths.add(new PathWithPaint(path, COLORS[colorI]));
                colorI = (colorI + 1) % COLORS.length;
            }
        }
        return paths;
    }

    private float getX(int i, int w) {
        return w * (i + 0.5f) / columns;
    }

    private float getY(int j, int h) {
        return h * (j + 0.5f) / rows;
    }

    private boolean isEmpty() {
        return candleViews == null || candleViews.isEmpty();
    }

    public interface OnHelpAnimationFinishedListener {
        void onHelpAnimationFinished();
    }

    private class PathWithPaint {
        public final Path path;
        public final Paint paint = new Paint();


        public PathWithPaint(Path path, int color) {
            this.path = path;
            paint.setColor(color);
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setAntiAlias(true);
            paint.setAlpha(DEFAULT_LINE_ALPHA);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }
    }
}
