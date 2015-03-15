package edu.amd.spbstu.magiccave.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.List;

import edu.amd.spbstu.magiccave.model.CandleModel;
import edu.amd.spbstu.magiccave.model.CandlePuzzle;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class GameView extends TableLayout {

    public static final int ROWS = 3;
    public static final int COLUMNS = 4;

    private CandlePuzzle mPuzzle;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPuzzle(CandlePuzzle puzzle, CandleView.OnCandleViewClickListener listener) {
        this.mPuzzle = puzzle;
        List<CandleModel> candles = puzzle.getCandles();

        removeAllViews();

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
                    row.addView(new CandleView(getContext(), haveCandleHere, listener));
                } else {
                    row.addView(new View(getContext()));
                }
            }
            addView(row);
        }
    }
}
