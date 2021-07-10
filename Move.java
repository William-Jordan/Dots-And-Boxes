package game;

/**
 * @author William Jordan
 * @version 1.0
 */
public class Move {

    final Edge edge;
    final int[] boxList;

    public Move(Edge edge) {
        this.edge = edge;
        boxList = null;
    }

    public Move(Edge edge, int a) {
        this.edge = edge;
        boxList = new int[]{a};
    }

    public Move(Edge edge, int a, int b) {
        this.edge = edge;
        boxList = new int[]{a, b};
    }

    public Edge getEdge() {
        return edge;
    }

    public int[] getBoxList() {
        return boxList;
    }

    public boolean madeBox(){
        return boxList != null;
    }
}
