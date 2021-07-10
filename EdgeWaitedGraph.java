package game;

import java.util.ArrayList;

/**
 * @author William Jordan
 * @version 1.0
 */
public class EdgeWaitedGraph {
    final int v;
    int edges;
    ArrayList<Edge>[] adjList;
    int[] vertList;

    public EdgeWaitedGraph(int v) {
        this.v = v + 1;
        adjList = (ArrayList<Edge>[]) new ArrayList[this.v];
        for (int i = 0; i < adjList.length; i++) {
            adjList[i] = new ArrayList<Edge>();
        }
        vertList = new int[v];
    }

    public boolean addEdge(Edge e) {
        for (ArrayList<Edge> l: adjList)
            for (Edge edge: l)
                if (edge.compareTo(e) == 0)
                    return false;
        adjList[e.getA()].add(e);
        edges++;
        return true;
    }

    public String toString() {
        StringBuilder out = new StringBuilder(v + " " + edges + "\n");

        for (int i = 1; i < adjList.length; i++) {
            out.append(i).append(": ");
            for (int j = 0; j < adjList[i].size(); j++) {
                out.append(adjList[i].get(j).toString()).append("\t\t");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public ArrayList<Edge>[] getAdjList() {
        return adjList;
    }

    public boolean checkBox(int n, int rows) {
        if (!edgeExists(n, n+1)) return false;
        if (!edgeExists(n,n+rows)) return false;
        if (!edgeExists(n+rows, n+rows+1)) return false;
        if (!edgeExists(n+1, n+rows+1)) return false;
        return true;
    }

    public boolean edgeExists(int a, int b) {
        for (ArrayList<Edge> l: adjList)
            for (Edge edge: l)
                if ((edge.getA() == a && edge.getB() == b) || (edge.getA() == b && edge.getB() == a))
                    return true;
        return false;
    }

    public void setVert(int index, int v){
        if(vertList[index] == 0)
            vertList[index] = v;
    }

    public void resetVert(int index){
        vertList[index] = 0;
    }

    public int[] getVertList() {
        return vertList;
    }

    public int getVert(int index) {
        return vertList[index];
    }

    public void deleteEdge(Edge edge) {
        for (ArrayList<Edge> l: adjList)
            for (int i = 0; i < l.size(); i++)
                if (l.get(i).compareTo(edge) == 0) {
                    l.remove(i);
                    return;
                }
    }
}
