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
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author huert
 */
public class BattleshipClient extends JFrame {

    private JPanel mainPanel, tablesPanel, infoPanel, userTable, pcTable;
    private JButton buttonsMatrix[][];
    private JLabel labelsMatrix[][], ys[], xs[], shipsNames[], shipsInfo[],
            turn, attempts;
    private Border borders;
    private int valuesMatrix[][];

    public BattleshipClient() {
        initPanels();
        initComponents();
        setComponents();
        addComponents();
        setFrame();
    }

    public static void main(String args[]) {
        new BattleshipClient();
    }

    private void setComponents() {
        setUserTable();
        setPcTable();
    }

    private void addComponents() {
        for(int i = 0; i < 7; i++) {
            infoPanel.add(shipsNames[i]);
            infoPanel.add(shipsInfo[i]);
        }
        infoPanel.add(turn);
        infoPanel.add(attempts);
        mainPanel.add(infoPanel, BorderLayout.WEST);
        tablesPanel.add(pcTable);
        tablesPanel.add(userTable);
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void setFrame() {
        setTitle("Battleship");
        setResizable(false);
        setSize(1280, 720);
        setVisible(true);
    }
    
    private void initPanels() {
        mainPanel = new JPanel(new BorderLayout());
        tablesPanel = new JPanel(new GridLayout(2, 0, 0, 4));
        infoPanel = new JPanel(new GridLayout(16, 0, 0, 4));
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
        shipsNames[0] = new JLabel("Submarine");
        shipsNames[1] = new JLabel("Battleship");
        shipsNames[2] = new JLabel("Cruiser Alpha");
        shipsNames[3] = new JLabel("Cruiser Beta");
        shipsNames[4] = new JLabel("Destroyer Alpha");
        shipsNames[5] = new JLabel("Destroyer Beta");
        shipsNames[6] = new JLabel("Destroyer Charlie");
        for(int i = 0; i < 7; i++) {
            shipsInfo[i] = new JLabel("Unattacked");
        }
        turn = new JLabel("Turn: Username/PC");
        attempts = new JLabel("Attempts remaining: 1");
    }

    private void setUserTable() {
        for (int i = 0; i < Properties.DIMENSION; i++) {
            for (int j = 0; j < Properties.DIMENSION; j++) {
                labelsMatrix[i][j] = new JLabel();
                setCell(labelsMatrix[i][j], Color.GRAY);
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
            pcTable.add(xs[i]);
        }
        for (int i = 0; i < Properties.DIMENSION; i++) {
            for (int j = 0; j < Properties.DIMENSION; j++) {
                if (j == 0) {
                    ys[i] = new JLabel(String.valueOf((char) (i + 65)));
                    setCell(ys[i], Color.GRAY);
                    pcTable.add(ys[i]);
                }
                buttonsMatrix[i][j] = new JButton();
                setCell(buttonsMatrix[i][j], Color.CYAN);
                pcTable.add(buttonsMatrix[i][j]);
            }
        }
    }

    private void setCell(JComponent cell, Color color) {
        cell.setBackground(color);
        cell.setBorder(borders);
    }

}
