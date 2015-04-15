package edu.amd.spbstu.magiccave.model;

/**
 * @author iAnton
 * @since 15/04/15
 */
public final class Line {
    private final Point a;
    private final Point b;

    public Line(Point a, Point b) {
        if (a.getX() != b.getX() ? a.getX() > b.getX() : a.getY() > b.getY()) {
            this.a = b;
            this.b = a;
        } else {
            this.a = a;
            this.b = b;
        }
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public boolean intersects(Line other) {
        return Geom.intersects(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return a.equals(line.a) && b.equals(line.b);

    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }
}