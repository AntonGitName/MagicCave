package ru.dinter.magiccave.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CandleModel {

    public enum CandleState {
        ON, OFF;
        
        public CandleState inverted() {
            return (this.equals(ON)) ? OFF : ON;
        }
    }
    
    private final float x;
    private final float y;
    
    private List<CandleModel> neighbours = new ArrayList<>();
    private CandleState state;
    
    private static final Random RND = new Random();
    
    public CandleModel() {
        this.state = CandleState.ON;
        x = RND.nextFloat();
        y = RND.nextFloat();
    }

    public CandleState getState() {
        return state;
    }

    public void invertState() {
        state = state.inverted();
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

    public double distanceTo(CandleModel other) {
        return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
    }
    
    public void connect(CandleModel other) {
        neighbours.add(other);
        other.neighbours.add(this);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    
}
