package game;

/**
 * @author William Jordan
 * @version 1.0
 */
public class Edge implements Comparable {
    final int a;
    final int b;
    final int w;

    public Edge(int a, int b, int w) {
        this.a = a;
        this.b = b;
        this.w = w;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getW() {
        return w;
    }

    @Override
    public String toString() {
        return a + " - " + b + ", " + w;
    }

    @Override
    public int compareTo(Object o) {
        Edge e = (Edge)o;
        if((e.getA() == a && e.getB() == b) || (e.getA() == b && e.getB() == a))
            return 0;
        else
            return -1;
    }
}
