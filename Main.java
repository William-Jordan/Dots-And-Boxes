/*
Players as counter
scores at the end, number in color

 */
package game;

import javax.swing.*;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author William Jordan
 * @version 1.0
 */
public class Main implements ActionListener{
    JFrame frame;
    JPanel rowsButtons, columnButtons, playerButtons;
    JLabel rows, columns, players;
    Button rowsUp, rowsDown, columnsUp, columnsDown, addPlayer, addBot, removePlayer, start, square, undo;
    GameBoard gameBoard;
    int row = 5, column = 5, numOfPlayers;
    boolean isSquare = true;
    StringBuilder playerList = new StringBuilder();

    public Main () {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Look and Feel not set");
        }
        //frame setup
        frame = new JFrame();
        frame.setLayout(null);
        frame.setPreferredSize(new Dimension(700, 800));
        frame.setTitle("Dots and Boxes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Rows
        rows = new JLabel(" Rows: " + row);
        rows.setFont(new Font("Dialog", Font.BOLD, 18));
        rows.setBounds(0,0,100,100);

        rowsButtons = new JPanel(new GridLayout(2,1));
        rowsButtons.setBounds(100,0,50,100);

        rowsUp = new Button("+");
        rowsUp.setName("rowsUp");
        rowsUp.addActionListener(this);
        rowsButtons.add(rowsUp);
        rowsDown = new Button("-");
        rowsDown.setName("rowsDown");
        rowsDown.addActionListener(this);
        rowsButtons.add(rowsDown);

        //Columns
        columns = new JLabel(" Columns: " + column);
        columns.setFont(new Font("Dialog", Font.BOLD, 18));
        columns.setBounds(150,0,130,100);

        columnButtons = new JPanel(new GridLayout(2,1));
        columnButtons.setBounds(280,0,50,100);

        columnsUp = new Button("+");
        columnsUp.setName("columnsUp");
        columnsUp.addActionListener(this);
        columnButtons.add(columnsUp);

        columnsDown = new Button("-");
        columnsDown.setName("columnsDown");
        columnsDown.addActionListener(this);
        columnButtons.add(columnsDown);

        //Player Buttons
        players = new JLabel("Players: ");
        players.setBounds(330,50,250,50);
        players.setFont(new Font("Dialog", Font.BOLD, 18));

        playerButtons = new JPanel(new GridLayout(1,3));
        playerButtons.setBounds(330,0,250,50);

        addPlayer = new Button("+ P");
        addPlayer.setName("addPlayer");
        addPlayer.addActionListener(this);
        playerButtons.add(addPlayer);

        //added for later use
        addBot = new Button("+ B");
        addBot.setName("addBot");
        addBot.addActionListener(this);
        playerButtons.add(addBot);

        removePlayer = new Button("- P/B");
        removePlayer.setName("removePlayer");
        removePlayer.addActionListener(this);
        removePlayer.setEnabled(false);
        playerButtons.add(removePlayer);

        //Square
        square = new Button("■");//▢□■
        square.setFont(new Font("Dialog", Font.PLAIN, 26));
        System.out.println(square.getFont());
//        square.setFont();
        square.setName("square");
        square.addActionListener(this);
        square.setForeground(Color.green);
        playerButtons.add(square);

        //Undo
        undo = new Button("undo");
        undo.setName("undo");
        undo.addActionListener(this);
        playerButtons.add(undo);

        //Start
        start = new Button("Start");
        start.setName("start");
        start.addActionListener(this);
        start.setBounds(580, 0,100,100);

        //Game Board
        gameBoard = new GameBoard(column,row,700);
        gameBoard.setBounds(0,100,700,800);

        //final frame setup
        frame.add(rows);
        frame.add(rowsButtons);
        frame.add(columns);
        frame.add(columnButtons);
        frame.add(playerButtons);
        frame.add(players);
        frame.add(start);
        frame.add(gameBoard);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e+"";
        s=s.substring(s.indexOf(" on ")+4);
        switch (s) {
            case "rowsUp" -> {
                if (isSquare) updateBoth(1);
                else row++;
                rows.setText(" Rows: " + row);
                rowsDown.setEnabled(true);
                gameBoard.updateBoard(row, column);
            }
            case "rowsDown" -> {
                if (isSquare) updateBoth(-1);
                else row--;
                rows.setText(" Rows: " + row);
                if (row == 2) rowsDown.setEnabled(false);
                gameBoard.updateBoard(row, column);
            }
            case "columnsUp" -> {
                if (isSquare) updateBoth(1);
                else column++;
                columns.setText(" Columns: " + column);
                columnsDown.setEnabled(true);
                gameBoard.updateBoard(row, column);
            }
            case "columnsDown" -> {
                if (isSquare) updateBoth(-1);
                else column--;
                columns.setText(" Columns: " + column);
                if (column == 2) columnsDown.setEnabled(false);
                gameBoard.updateBoard(row, column);
            }
            case "addPlayer" -> {
                numOfPlayers++;
                if (playerList.length() != 0) players.setText(players.getText() + ", ");
                playerList.append("P");
                players.setText(players.getText() + "P");
                removePlayer.setEnabled(true);
                gameBoard.updateBoard(row, column);
            }
            case "addBot" -> {
                numOfPlayers++;
                if (playerList.length() != 0) players.setText(players.getText() + ", ");
                playerList.append("B");
                players.setText(players.getText() + "B");
                removePlayer.setEnabled(true);
                gameBoard.updateBoard(row, column);
            }
            case "removePlayer" -> {
                numOfPlayers--;
                playerList.deleteCharAt(playerList.length()-1);
                if (playerList.length() == 0) {
                    removePlayer.setEnabled(false);
                    players.setText(players.getText() + "  ");
                }
                players.setText(players.getText().substring(0, players.getText().length()-3));
                gameBoard.updateBoard(row, column);
            }
            case "start" -> {
                if (playerList.length() != 0) gameBoard.start(numOfPlayers);
            }

            case "square" -> {
                if (isSquare) {
                    isSquare = false;
                    square.setForeground(Color.red);
                }
                else {
                    isSquare = true;
                    square.setForeground(Color.green);
                }
            }

            case "undo" -> gameBoard.undo();
        }
    }

    private void updateBoth(int n) {
        if (row != column) { // equalize with square
            if (n > 0) row = Math.max(row, column);
            else row = Math.min(row, column);
            column = row;
        }

        //update values
        row+=n;
        column+=n;
        columns.setText(" Columns: " + column);
        rows.setText(" Rows: " + row);

        //set enabled
        if (column == 2 || row == 2) {
            columnsDown.setEnabled(false);
            rowsDown.setEnabled(false);
        }
        else {
            columnsDown.setEnabled(true);
            rowsDown.setEnabled(true);
        }
    }

    public static void main(String[] args) { new Main(); }
}
