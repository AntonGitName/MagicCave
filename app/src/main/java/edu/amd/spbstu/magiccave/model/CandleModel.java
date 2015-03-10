package edu.amd.spbstu.magiccave.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iAnton on 09/03/15.
 */
public class CandleModel {

    private State mState = State.ON;
    private List<CandleModel> mNeighbourgs = new ArrayList<>();
    private boolean mIsInversedCorrectly = false;
    private final int mId;
    private final int x;
    private final int y;

    private static int lastId = 0;

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

    public List<CandleModel> getNeighbourgs() {
        return mNeighbourgs;
    }

    public int getId() {
        return mId;
    }

    public boolean isInversedCorrectly() {
        return mIsInversedCorrectly;
    }

    public void inverse() {
        mState = mState.inverseState();
    }

    public void addNeighbour(CandleModel candle) {
        mNeighbourgs.add(candle);
    }

    public void inverseWithNeighbours() {
        mIsInversedCorrectly = !mIsInversedCorrectly;
        inverse();
        for (CandleModel neighbour : mNeighbourgs) {
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
