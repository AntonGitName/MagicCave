package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import edu.amd.spbstu.magiccave.model.CandleModel;
import edu.amd.spbstu.magiccave.model.CandlePuzzle;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class GameView extends TableLayout implements CandleView.OnAnimationFinishedListener {

    public static final int ROWS = 3;
    public static final int COLUMNS = 4;

    private static final int DEFAULT_LINE_ALPHA = 100;
    private static final float LINE_WIDTH = 4f;
    private final Paint linePaint = new Paint();
    private List<CandleView> candleViews;
    private List<CandleView> candlesToAnimate;
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

        linePaint.setColor(Color.DKGRAY);
        linePaint.setStrokeWidth(LINE_WIDTH);
        linePaint.setAntiAlias(true);
        linePaint.setAlpha(DEFAULT_LINE_ALPHA);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);

        this.setWillNotDraw(false);

        for (int i = 0; i < ROWS; ++i) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            for (int j = 0; j < COLUMNS; ++j) {

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
        final int viewsToAnimate = solution.size() > 2 ? solution.size() - 2 : solution.size();
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
            final int h = getMeasuredHeight();
            final int w = getMeasuredWidth();
            for (CandleView candleView : candleViews) {
                final float fromX = candleView.getModel().getX();
                final float fromY = candleView.getModel().getY();
                for (CandleModel candleModel : candleView.getModel().getNeighbours()) {
                    final float toX = candleModel.getX();
                    final float toY = candleModel.getY();
                    canvas.drawLine(w * (fromX + 0.5f) / COLUMNS, h * (fromY + 0.5f) / ROWS, w * (toX + 0.5f) / COLUMNS, h * (toY + 0.5f) / ROWS, linePaint);
                }
            }
        }
    }

    private boolean isEmpty() {
        return candleViews == null || candleViews.isEmpty();
    }

    public interface OnHelpAnimationFinishedListener {
        void onHelpAnimationFinished();
    }
}
