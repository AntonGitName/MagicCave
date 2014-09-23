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
    
    private final double x;
    private final double y;
    
    private List<CandleModel> neighbours = new ArrayList<>();
    private CandleState state;
    
    private static final Random RND = new Random();
    
    public CandleModel() {
        this.state = CandleState.ON;
        x = RND.nextDouble();
        y = RND.nextDouble();
    }

    public CandleState getState() {
        return state;
    }

    public void setState(CandleState state) {
        this.state = state;
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

    public void setNeighbours(List<CandleModel> neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((neighbours == null) ? 0 : neighbours.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double distanceTo(CandleModel other) {
        return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
    }
    
    public void connect(CandleModel other) {
        neighbours.add(other);
        other.neighbours.add(this);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CandleModel other = (CandleModel) obj;
        if (neighbours == null) {
            if (other.neighbours != null)
                return false;
        } else if (!neighbours.equals(other.neighbours))
            return false;
        if (state != other.state)
            return false;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }
    
    
}
