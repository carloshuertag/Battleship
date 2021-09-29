/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Game.Properties;
import Models.Ship;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author huert
 */
public class BattleshipClient extends JFrame {

    private static Ship[] ships;
    private JPanel mainPanel, tablesPanel, infoPanel, userTable, pcTable;
    private JButton buttonsMatrix[][];
    private JLabel labelsMatrix[][], ys[], xs[], shipsNames[], shipsInfo[],
            turn, attempts, title, username;
    private Border borders;
    private int valuesMatrix[][];

    public BattleshipClient() {
        initPanels();
        initComponents();
        setComponents();
        addComponents();
        setFrame();
        getShipsCoordenates();
        setShips();
    }

    public static void main(String args[]) {
        new BattleshipClient();
    }

    private void getShipsCoordenates() {
        int x = 0;
        int y = 0;
        boolean flag = false, vertical = false;
        for (int i = 0; i < ships.length; i++) {
            do {
                try {
                    x = Integer.parseInt(JOptionPane.showInputDialog(null,
                            "Enter numeric (1-10) initial coordante for "
                            + Properties.SHIPNAMES[i], "Enter coordenates",
                            JOptionPane.QUESTION_MESSAGE)) - 1;
                    y = JOptionPane.showInputDialog(null,
                            "Enter alphanumeric (A-J) initial coordante for "
                            + Properties.SHIPNAMES[i], "Enter coordenates",
                            JOptionPane.QUESTION_MESSAGE).charAt(0) - 65;
                    vertical = JOptionPane.showConfirmDialog(null,
                            "Confirm vertical alignment, or else it will be horizontal",
                            "Enter alignment", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
                    if(vertical){
                        if (y + Properties.SHIPLENGTHS[i] > 10) {
                            throw new Exception("Invalid postion for 0-10");
                        }
                    } else {
                        if (x + Properties.SHIPLENGTHS[i] > 10) {
                            throw new Exception("Invalid position A-J");
                        }
                    }
                    flag = false;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input, try again", "Oops" + ex.getMessage(),
                            JOptionPane.ERROR_MESSAGE);
                    flag = true;
                }
            } while (flag);
            ships[i] = new Ship(Properties.SHIPNAMES[i], x, y,
                    Properties.SHIPLENGTHS[i], vertical);
            System.out.println(ships[i]);
        }
    }

    private void setShips() {
        int x, y;
        for (int i = 0; i < ships.length; i++) {
            x = ships[i].getX();
            y = ships[i].getY();
            for (int j = 0; j < ships[i].getLength(); j++) {
                labelsMatrix[x][y].setBackground(Color.DARK_GRAY);
                labelsMatrix[x][y].setText(String.valueOf(
                        ships[i].getName().charAt(0)));
                labelsMatrix[x][y].setForeground(Color.CYAN);
                labelsMatrix[x][y].setOpaque(true);
                if(ships[i].getVertical()) {
                    y++;
                } else {
                    x++;
                }
            }
        }
    }

    private void setComponents() {
        setUserTable();
        setPcTable();
    }

    private void addComponents() {
        mainPanel.add(title, BorderLayout.NORTH);
        for (int i = 0; i < 7; i++) {
            infoPanel.add(shipsNames[i]);
            infoPanel.add(shipsInfo[i]);
        }
        infoPanel.add(turn);
        infoPanel.add(attempts);
        mainPanel.add(infoPanel, BorderLayout.WEST);
        tablesPanel.add(pcTable);
        tablesPanel.add(userTable);
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(username, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void setFrame() {
        setTitle("Battleship");
        setResizable(false);
        setSize(1280, 720);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initPanels() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        tablesPanel = new JPanel(new GridLayout(2, 0, 0, 10));
        infoPanel = new JPanel(new GridLayout(16, 0, 0, 5));
        userTable = new JPanel(new GridLayout(Properties.DIMENSION,
                Properties.DIMENSION, 2, 2));
        pcTable = new JPanel(new GridLayout(Properties.DIMENSION + 1,
                Properties.DIMENSION + 1, 2, 2));
    }

    private void initComponents() {
        buttonsMatrix = new JButton[Properties.DIMENSION][Properties.DIMENSION];
        labelsMatrix = new JLabel[Properties.DIMENSION][Properties.DIMENSION];
        xs = new JLabel[Properties.DIMENSION];
        ys = new JLabel[Properties.DIMENSION];
        valuesMatrix = new int[Properties.DIMENSION][Properties.DIMENSION];
        borders = BorderFactory.createLineBorder(Color.BLUE);
        shipsNames = new JLabel[7];
        shipsInfo = new JLabel[7];
        for (int i = 0; i < 7; i++) {
            shipsNames[i] = new JLabel(Properties.SHIPNAMES[i]);
            shipsInfo[i] = new JLabel("Unattacked");
        }
        turn = new JLabel("Turn: Username/PC");
        attempts = new JLabel("Attempts remaining: 1");
        title = new JLabel("Battleship", SwingConstants.CENTER);
        username = new JLabel("Username", SwingConstants.CENTER);
        ships = new Ship[7];
    }

    private void setUserTable() {
        for (int i = 0; i < Properties.DIMENSION; i++) {
            for (int j = 0; j < Properties.DIMENSION; j++) {
                labelsMatrix[i][j] = new JLabel("≈", SwingConstants.CENTER);
                labelsMatrix[i][j].setOpaque(true);
                setCell(labelsMatrix[i][j], Color.CYAN);
                labelsMatrix[i][j].setForeground(Color.BLUE);
                userTable.add(labelsMatrix[i][j]);
            }
        }
    }

    private void setPcTable() {
        JLabel refLabel = new JLabel("X, N");
        setCell(refLabel, Color.GRAY);
        pcTable.add(refLabel);
        for (int i = 0; i < Properties.DIMENSION; i++) {
            xs[i] = new JLabel(String.valueOf(i + 1));
            setCell(xs[i], Color.GRAY);
            xs[i].setOpaque(true);
            xs[i].setForeground(Color.WHITE);
            pcTable.add(xs[i]);
        }
        for (int i = 0; i < Properties.DIMENSION; i++) {
            for (int j = 0; j < Properties.DIMENSION; j++) {
                if (j == 0) {
                    ys[i] = new JLabel(String.valueOf((char) (i + 65)));
                    setCell(ys[i], Color.GRAY);
                    ys[i].setOpaque(true);
                    ys[i].setForeground(Color.WHITE);
                    pcTable.add(ys[i]);
                }
                buttonsMatrix[i][j] = new JButton("≈");
                setCell(buttonsMatrix[i][j], Color.CYAN);
                buttonsMatrix[i][j].setForeground(Color.BLUE);
                pcTable.add(buttonsMatrix[i][j]);
            }
        }
    }

    private void setCell(JComponent cell, Color color) {
        cell.setBackground(color);
        cell.setBorder(borders);
    }

}
