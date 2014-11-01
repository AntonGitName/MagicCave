package ru.dinter.magiccave.model;

import java.util.ArrayList;
import java.util.List;

public final class CandleModel {

    public enum CandleState {
        OFF, ON;

        public CandleState inverted() {
            return (this.equals(ON)) ? OFF : ON;
        }
    }

    private List<CandleModel> neighbours = new ArrayList<>();

    private CandleState state;
    private final float x;
    private final float y;

    public CandleModel(float x, float y) {
        this.state = CandleState.ON;
        this.x = x;
        this.y = y;
    }

    public void connect(CandleModel other) {
        neighbours.add(other);
        other.neighbours.add(this);
    }

    public float distanceTo(CandleModel other) {
        return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
    }

    public float distanceTo(float x, float y) {
        return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y);
    }

    public void forceInvertState() {
        state = state.inverted();
        for (CandleModel candle : neighbours) {
            candle.invertState();
        }
    }

    public List<CandleModel> getNeighbours() {
        return neighbours;
    }

    public CandleState getState() {
        return state;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void invertState() {
        state = state.inverted();
    }

}
