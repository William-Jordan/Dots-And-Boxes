package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author William Jordan
 * @version 1.0
 */
public class GameBoard extends JPanel {
    boolean start = false;
    int rows, columns, size, radius, length, colorPointer = 1, players;
    EdgeWaitedGraph graph;
    ArrayList<Color> colors = new ArrayList<>();
    ArrayList<Color> transparentColors = new ArrayList<>();
    ArrayList<Move> lastMove;

    public GameBoard(int c, int r, int size) {
        this.rows = r;
        this.columns = c;
        this.size = size;
        radius = size / 50;
        length = (size - (6 * radius)) / Math.max(rows, columns);
        lastMove = new ArrayList<>();

        //load six basic colors
        colors.add(new Color(0, 0, 0));
        colors.add(new Color(0, 0, 200));
        colors.add(new Color(255, 100, 0));
        colors.add(new Color(0, 250, 70));
        colors.add(new Color(255, 120, 190));
        colors.add(new Color(150, 70, 255));
        colors.add(new Color(250, 250, 0));
        colors.add(new Color(60, 180, 230));
        colors.add(new Color(130, 180, 140));

        transparentColors.add(new Color(0, 0, 0));
        transparentColors.add(new Color(0, 0, 200, 100));
        transparentColors.add(new Color(255, 100, 0, 100));
        transparentColors.add(new Color(0, 250, 70, 100));
        transparentColors.add(new Color(255, 120, 190, 100));
        transparentColors.add(new Color(150, 70, 255, 100));
        transparentColors.add(new Color(250, 250, 0, 100));
        transparentColors.add(new Color(60, 180, 230, 100));
        transparentColors.add(new Color(130, 180, 140, 100));

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (Math.min(e.getX(), e.getY()) >= 3 * radius) { //in bounds check
                    int x = (e.getX() - (3 * radius));
                    int y = (e.getY() - (3 * radius));
                    if (!(x > length * (rows - 1) + radius || y > length * (columns - 1) + radius)) { //in bounds check
                        if (x % length <= radius) { // vertical
                            if ((y - radius) % (length) <= length - radius) { // not on dot
                                x /= length; //simplify to rows
                                y /= length; //simplify to cols
                                int a = x + (rows * y) + 1; //calculate node number
                                if (graph.addEdge(new Edge(a, x + (rows * y) + rows + 1, colorPointer))) { // try to add edge
                                    int boxes = 0;
                                    if (graph.checkBox(a, rows)) { //check bottom box
                                        graph.setVert(a, colorPointer);
                                        boxes += 1;
                                    }
                                    if (graph.checkBox(a - 1, rows)) { //check upper box
                                        graph.setVert(a - 1, colorPointer);
                                        boxes += 2;
                                    }
                                    if (boxes == 0) { // log move
                                        updateColorPointer(1);
                                        lastMove.add(new Move(new Edge(a, x + (rows * y) + rows + 1, colorPointer)));
                                    } else if (boxes == 1) {
                                        lastMove.add(new Move(new Edge(a, x + (rows * y) + rows + 1, colorPointer), a));
                                    } else if (boxes == 2) {
                                        lastMove.add(new Move(new Edge(a, x + (rows * y) + rows + 1, colorPointer), a - 1));
                                    } else {
                                        lastMove.add(new Move(new Edge(a, x + (rows * y) + rows + 1, colorPointer), a, a - 1));
                                    }
                                }
                            }
                        } else if (y % length <= radius) { // horizontal
                            if ((x - radius) % length <= length - radius) {
                                x /= length; //simplify to rows
                                y /= length; //simplify to cols
                                int a = (y * rows) + x + 1; //calculate node number
                                if (graph.addEdge(new Edge(a, (y * rows) + x + 2, colorPointer))) { //try to add edge
                                    int boxes = 0;
                                    if (graph.checkBox(a, rows)) { //check right box
                                        graph.setVert(a, colorPointer);
                                        boxes += 1;
                                    }
                                    if (graph.checkBox(a - rows, rows)) { //check left box
                                        graph.setVert(a - rows, colorPointer);
                                        boxes += 2;
                                    }
                                    if (boxes == 0) { //save move
                                        updateColorPointer(1);
                                        lastMove.add(new Move(new Edge(a, (y * rows) + x + 2, colorPointer)));
                                    } else if (boxes == 1) {
                                        lastMove.add(new Move(new Edge(a, (y * rows) + x + 2, colorPointer), a));
                                    } else if (boxes == 2) {
                                        lastMove.add(new Move(new Edge(a, (y * rows) + x + 2, colorPointer), a - rows));
                                    } else {
                                        lastMove.add(new Move(new Edge(a, (y * rows) + x + 2, colorPointer), a, a - rows));
                                    }
                                }
                            }
                        }
                    }
                }
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        length = (size - (6 * radius)) / Math.max(rows, columns);

        //prepare Graphics2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //clear canvas
        g2d.setColor(new Color(238, 238, 238));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (start) {
            //draw boxes
            for (int i = 0; i < graph.getVertList().length; i++)
                if (graph.getVert(i) != 0) {
                    g2d.setColor(transparentColors.get(graph.getVert(i)));
                    g2d.fillRect((((i - 1) % rows) * length) + (int) (3.5 * radius),
                            (((i - 1) / rows) * length) + (int) (3.5 * radius),
                            length, length);
                }

            //draw lines
            g2d.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            ArrayList<Edge>[] list = graph.getAdjList();
            for (ArrayList<Edge> l : list)
                for (Edge e : l) {
                    g2d.setColor(colors.get(e.getW()));
                    g2d.drawLine((((e.getA() - 1) % rows) * length) + (int) (3.5 * radius),
                            (((e.getA() - 1) / rows) * length) + (int) (3.5 * radius),
                            (((e.getB() - 1) % rows) * length) + (int) (3.5 * radius),
                            (((e.getB() - 1) / rows) * length) + (int) (3.5 * radius));
                }
            g2d.setColor(colors.get(colorPointer));
            g2d.setFont(new Font("Dialog", Font.BOLD, 30)); //todo relative distances and sizes
            g2d.drawString("Turn",2,30);
        }

        // draw dots
        g2d.setColor(new Color(160, 160, 160)); // todo wrong color
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < columns; c++)
                g2d.fillOval(r * length + 3 * radius, c * length + 3 * radius, radius, radius);

        // game over
        if (start && gameOver()) {
            g.setColor(new Color(255,255,255,150));
            g2d.fillRect(0,0,size,size);

            int max = -1;
            int pointer = 1;
            ArrayList<Integer> tieList = new ArrayList<>();
            for (int j = 1; j < players+1; j++) { // count boxes
                int counter = 0;
                for (int i = 0; i < graph.getVertList().length; i++) {
                    if (graph.getVert(i) == j) counter++;
                }
                if (counter == max) tieList.add(j);
                if (counter > max) { // find most
                    max = counter;
                    pointer = j;
                    tieList.clear();
                }
            }
            //game over screen
            g2d.setColor(colors.get(pointer));
            g2d.setFont(new Font("Dialog", Font.BOLD, 75)); //todo relative distances and sizes
            g2d.drawString("Game Over", 150, 300);
            for (int i = 0; i < tieList.size(); i++) {
                g2d.setColor(colors.get(tieList.get(i)));
                g2d.drawString("Tie",310,300 + ((i+1) * 60));
            }
        }
    }

    public void start(int players) {
        //clear board
        start = false;
        repaint();
        colorPointer = 1;

        //prepare new game
        graph = new EdgeWaitedGraph(rows * columns);
        this.players = players;
        start = true;

        //add additional colors if necessary
        for (int i = 0; i < players - 6; i++) {
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);
            colors.add(new Color(r, g, b));
            transparentColors.add(new Color(r, g, b, 100));
        }
    }

    public void updateBoard(int r, int c) {
        this.rows = r;
        this.columns = c;
        start = false;
        colorPointer = 1;
        repaint();
    }

    private void updateColorPointer(int i) {
        colorPointer += i;
        if (colorPointer <= 0) colorPointer = players;
        if (colorPointer >= players + 1) colorPointer = 1;
    }

    public void undo() {
        if (lastMove.size() == 0) {
            updateColorPointer(1);
            repaint();
            return;
        }
        Move m = lastMove.remove(lastMove.size() - 1);
        graph.deleteEdge(m.getEdge());
        if (m.madeBox())
            for (int i : m.getBoxList())
                graph.resetVert(i);
        else updateColorPointer(-1);
        repaint();
    }

    private boolean gameOver(){
        for (int i = 1; i < graph.getVertList().length-rows; i++)
            if (i % (rows) != 0) {
                int n = graph.getVert(i);
                if (n == 0) return false;
            }
        return true;
    }
}
