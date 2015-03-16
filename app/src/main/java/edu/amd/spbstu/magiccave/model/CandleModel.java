package edu.amd.spbstu.magiccave.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iAnton
 * @since 09/03/15
 */
public class CandleModel {

    private static int lastId = 0;
    private final List<CandleModel> neighbours = new ArrayList<>();
    private final int mId;
    private final int x;
    private final int y;
    private State mState = State.ON;
    private boolean isInvertedCorrectly = true;

    public CandleModel(int x, int y, int id, boolean isInvertedCorrectly, String state) {
        this.mId = id;
        this.mState = State.valueOf(state);
        this.isInvertedCorrectly = isInvertedCorrectly;
        this.x = x;
        this.y = y;
    }

    public CandleModel(int x, int y) {
        mId = ++lastId;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState() {
        return mState;
    }

    public List<CandleModel> getNeighbours() {
        return neighbours;
    }

    public int getId() {
        return mId;
    }

    public boolean isInvertedCorrectly() {
        return isInvertedCorrectly;
    }

    private void inverse() {
        mState = mState.inverseState();
    }

    public void addNeighbour(CandleModel candle) {
        neighbours.add(candle);
    }

    public void inverseWithNeighbours() {
        isInvertedCorrectly = !isInvertedCorrectly;
        inverse();
        for (CandleModel neighbour : neighbours) {
            neighbour.inverse();
        }
    }

    public enum State {
        ON, OFF;

        public State inverseState() {
            switch (this) {

                case ON:
                    return OFF;
                case OFF:
                    return ON;
            }
            return null;
        }
    }
}
