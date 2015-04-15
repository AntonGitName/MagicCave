package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edu.amd.spbstu.magiccave.fragments.GameFragment.OnCandlesConnectedListener;
import edu.amd.spbstu.magiccave.interfaces.OnAnimationFinishedListener;
import edu.amd.spbstu.magiccave.interfaces.OnCandleViewClickListener;
import edu.amd.spbstu.magiccave.interfaces.OnConnectionFinishedListener;
import edu.amd.spbstu.magiccave.interfaces.OnHelpAnimationFinishedListener;
import edu.amd.spbstu.magiccave.model.CandleModel;
import edu.amd.spbstu.magiccave.model.CandlePuzzle;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class GameView extends TableLayout implements OnAnimationFinishedListener
        , OnCandlesConnectedListener {

    private static final int DEFAULT_LINE_ALPHA = 200;
    private static final float LINE_WIDTH = 4f;
    private static final Random RND = new Random();

    private static final int[] COLORS = {Color.CYAN, Color.MAGENTA, Color.GREEN, Color.BLACK, Color.BLUE, Color.YELLOW};
    private static final int WRONG_PATH_COLOR = Color.RED;
    private static final int WRONG_PATH_MAX_ANIMATION_TIMER = 128;
    private static final int FRAME_UPDATE_RATE = 8;

    private static final DashPathEffect DASH_PATH_EFFECT = new DashPathEffect(new float[]{10, 20}, 0);

    private final Timer timer = new Timer();


    private int rows;
    private int columns;
    private List<CandleView> candleViews;
    private List<CandleView> candlesToAnimate;
    private List<PathWithPaint> paths;
    private OnHelpAnimationFinishedListener listener;
    private int lastAnimatedView;

    private OnConnectionFinishedListener onConnectionFinishedListener;
    private int wrongPathAnimationTimer;
    private PathWithPaint wrongPath;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPuzzle(CandlePuzzle puzzle, OnCandleViewClickListener onCandleViewClickListener, OnConnectionFinishedListener onConnectionFinishedListener) {
        this.setWillNotDraw(false);
        this.onConnectionFinishedListener = onConnectionFinishedListener;

        final List<CandleModel> candles = puzzle.getCandles();
        candleViews = new ArrayList<>(candles.size());
        removeAllViews();
        paths = null;

        final TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);

        rows = puzzle.getRows();
        columns = puzzle.getColumns();

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
                    final CandleView candleView = new CandleView(getContext(), haveCandleHere, onCandleViewClickListener);
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
        for (PathWithPaint pathWithPaint : paths) {
            pathWithPaint.paint.setAlpha(isLinesVisible ? DEFAULT_LINE_ALPHA : 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paths == null) {
            paths = createPaths();
        }
        for (PathWithPaint pathWithPaint : paths) {
            canvas.drawPath(pathWithPaint.path, pathWithPaint.paint);
        }

        if (wrongPath != null) {
            ++wrongPathAnimationTimer;
            wrongPath.paint.setAlpha(DEFAULT_LINE_ALPHA - DEFAULT_LINE_ALPHA * wrongPathAnimationTimer / WRONG_PATH_MAX_ANIMATION_TIMER);
            canvas.drawPath(wrongPath.path, wrongPath.paint);
            if (wrongPathAnimationTimer >= WRONG_PATH_MAX_ANIMATION_TIMER) {
                wrongPath = null;
                onConnectionFinishedListener.onConnectionFinished(false);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        timer.cancel();
        timer.purge();
    }

    private void setupTimer() {
        final Handler handler = new Handler(Looper.getMainLooper());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GameView.this.invalidate();
                    }
                });
            }
        }, FRAME_UPDATE_RATE, FRAME_UPDATE_RATE);
    }

    @Deprecated
    private List<PathWithPaint> createPathsOld() {
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
                paths.add(new PathWithPaint(path, COLORS[colorI], candleView.getModel().getId(), candleModel.getId()));
                colorI = (colorI + 1) % COLORS.length;
            }
        }
        return paths;
    }

    private List<PathWithPaint> createPaths() {
        final List<PathWithPaint> paths = new ArrayList<>();
        final Set<Point> pairs = new HashSet<>();
        final int h = getMeasuredHeight();
        final int w = getMeasuredWidth();
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
                final Path path = new Path();
                path.moveTo(x1, y1);
                path.lineTo(x4, y4);
                paths.add(new PathWithPaint(path, COLORS[colorI], candleView.getModel().getId(), candleModel.getId()));
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

    @Override
    public void OnCandlesConnected(int firstCandleId, int secondCandleId) {
        final CandleView c1 = getCandleWithId(firstCandleId);
        final CandleView c2 = getCandleWithId(secondCandleId);

        c1.setIsSelected(false);
        c2.setIsSelected(false);

        for (PathWithPaint pathWithPaint : paths) {
            if (pathWithPaint.checkIds(firstCandleId, secondCandleId)) {
                pathWithPaint.paint.setAlpha(DEFAULT_LINE_ALPHA);
                onConnectionFinishedListener.onConnectionFinished(true);
                return;
            }
        }
        final Path path = new Path();
        final int h = getMeasuredHeight();
        final int w = getMeasuredWidth();
        path.moveTo(getX(c1.getModel().getX(), w), getY(c1.getModel().getY(), h));
        path.lineTo(getX(c2.getModel().getX(), w), getY(c2.getModel().getY(), h));
        wrongPath = new PathWithPaint(path, WRONG_PATH_COLOR, firstCandleId, secondCandleId);
        wrongPath.paint.setPathEffect(DASH_PATH_EFFECT);
        wrongPathAnimationTimer = 0;
    }

    private class PathWithPaint {
        public final Path path;
        public final Paint paint = new Paint();
        private final int idA;
        private final int idB;

        public PathWithPaint(Path path, int color, int idA, int idB) {
            this.path = path;
            this.idA = idA;
            this.idB = idB;
            paint.setColor(color);
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setAntiAlias(true);
            paint.setAlpha(0);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        public boolean checkIds(int a, int b) {
            return ((idA == a) && (idB == b)) || ((idA == b) && (idB == a));
        }
    }
}
