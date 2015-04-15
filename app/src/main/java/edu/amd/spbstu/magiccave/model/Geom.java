package edu.amd.spbstu.magiccave.model;

/**
 * @author iAnton
 * @since 15/04/15
 */
public final class Geom {

    private Geom() {
        throw new UnsupportedOperationException();
    }

    public static int area(Point a, Point b, Point c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    private static boolean intersectInner(int a, int b, int c, int d) {
        if (a > b) {
            final int tmp = a;
            a = b;
            b = tmp;
        }
        if (c > d) {
            final int tmp = c;
            c = d;
            d = tmp;
        }
        return Math.max(a, c) <= Math.min(b, d);
    }

    private static boolean intersectStrong(int a, int b, int c, int d) {
        if (a > b) {
            final int tmp = a;
            a = b;
            b = tmp;
        }
        if (c > d) {
            final int tmp = c;
            c = d;
            d = tmp;
        }
        return Math.max(a, c) < Math.min(b, d);
    }

    public static boolean intersectsBB(final Line a, final Line b) {
        return intersectStrong(a.getA().getX(), a.getB().getX(), b.getA().getX(), b.getB().getX())
                && intersectStrong(a.getA().getY(), a.getB().getY(), b.getA().getY(), b.getB().getY())
                && (area(a.getA(), a.getB(), b.getA()) * area(a.getA(), a.getB(), b.getB()) <= 0)
                && (area(b.getA(), b.getB(), b.getA()) * area(b.getA(), b.getB(), a.getB()) <= 0);
    }

    public static boolean intersects(final Line a, final Line b) {
        return intersectInner(a.getA().getX(), a.getB().getX(), b.getA().getX(), b.getB().getX())
                && intersectInner(a.getA().getY(), a.getB().getY(), b.getA().getY(), b.getB().getY())
                && (area(a.getA(), a.getB(), b.getA()) * area(a.getA(), a.getB(), b.getB()) <= 0)
                && (area(b.getA(), b.getB(), b.getA()) * area(b.getA(), b.getB(), a.getB()) <= 0);
    }

    public static boolean interlaps(final Line a, final Line b) {
        return intersectsBB(a, b) && (area(a.getA(), a.getB(), b.getA()) == 0);
    }
}
