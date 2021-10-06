/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Game.CellActionListener;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Game.Properties;
import Models.Ship;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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

    private Ship[] ships;
    private JPanel mainPanel, tablesPanel, infoPanel, userTable, pcTable;
    private JButton buttonsMatrix[][];
    private JLabel labelsMatrix[][], ys[][], xs[][], shipsNames[], shipsInfo[],
            turn, attempts, title, usernameLabel, refLabel[];
    private Border borders;
    private InetAddress serverAddrs;
    private DatagramSocket client;
    private String username;
    private int valuesMatrix[][];

    public BattleshipClient() {
        initPanels();
        initComponents();
        setComponents();
        addComponents();
        setFrame();
        /*try {
            hello();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to server", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
        }*/
        getShipsCoordenates();
        setShips();
    }

    public static void main(String args[]) {
        new BattleshipClient();
    }

    private void initPanels() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        tablesPanel = new JPanel(new GridLayout(2, 0, 0, 10));
        infoPanel = new JPanel(new GridLayout(10, 0, 0, 5));
        userTable = new JPanel(new GridLayout(Properties.DIMENSION + 1,
                Properties.DIMENSION + 1, 2, 2));
        pcTable = new JPanel(new GridLayout(Properties.DIMENSION + 1,
                Properties.DIMENSION + 1, 2, 2));
    }

    private void initComponents() {
        buttonsMatrix = new JButton[Properties.DIMENSION][Properties.DIMENSION];
        labelsMatrix = new JLabel[Properties.DIMENSION][Properties.DIMENSION];
        xs = new JLabel[Properties.DIMENSION][2];
        ys = new JLabel[Properties.DIMENSION][2];
        refLabel = new JLabel[2];
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
        usernameLabel = new JLabel("Username", SwingConstants.CENTER);
        ships = new Ship[7];
    }

    private void setComponents() {
        setPcTable();
        setUserTable();
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
        mainPanel.add(usernameLabel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void setUserTable() {
        refLabel[1] = new JLabel("X, N");
        setCell(refLabel[1], Color.GRAY, Color.WHITE);
        userTable.add(refLabel[1]);
        for (int i = 0; i < Properties.DIMENSION; i++) {
            xs[i][1] = new JLabel(String.valueOf(i + 1));
            setCell(xs[i][1], Color.GRAY, Color.WHITE);
            userTable.add(xs[i][1]);
        }
        for (int i = 0; i < Properties.DIMENSION; i++) {
            for (int j = 0; j < Properties.DIMENSION; j++) {
                if (j == 0) {
                    ys[i][1] = new JLabel(String.valueOf((char) (i + 65)));
                    setCell(ys[i][1], Color.GRAY, Color.WHITE);
                    userTable.add(ys[i][1]);
                }
                labelsMatrix[i][j] = new JLabel("≈", SwingConstants.CENTER);
                setCell(labelsMatrix[i][j], Color.CYAN, Color.BLUE);
                userTable.add(labelsMatrix[i][j]);
            }
        }
    }

    private void setPcTable() {
        refLabel[0] = new JLabel("X, N");
        setCell(refLabel[0], Color.GRAY, Color.WHITE);
        pcTable.add(refLabel[0]);
        for (int i = 0; i < Properties.DIMENSION; i++) {
            xs[i][0] = new JLabel(String.valueOf(i + 1));
            setCell(xs[i][0], Color.GRAY, Color.WHITE);
            pcTable.add(xs[i][0]);
        }
        for (int i = 0; i < Properties.DIMENSION; i++) {
            for (int j = 0; j < Properties.DIMENSION; j++) {
                if (j == 0) {
                    ys[i][0] = new JLabel(String.valueOf((char) (i + 65)));
                    setCell(ys[i][0], Color.GRAY, Color.WHITE);
                    pcTable.add(ys[i][0]);
                }
                buttonsMatrix[i][j] = new JButton("≈");
                setCell(buttonsMatrix[i][j], Color.CYAN, Color.BLUE);
                buttonsMatrix[i][j].addActionListener(
                        new CellActionListener(i, j));
                pcTable.add(buttonsMatrix[i][j]);
            }
        }
    }

    private void setCell(JComponent cell, Color bgColor, Color fgColor) {
        cell.setBackground(bgColor);
        cell.setBorder(borders);
        cell.setOpaque(true);
        cell.setForeground(fgColor);
    }

    private void setFrame() {
        setTitle("Battleship");
        setResizable(false);
        setSize(1280, 720);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void hello() throws UnknownHostException, SocketException,
            IOException {
        boolean flag = false;
        do {
            username = JOptionPane.showInputDialog(null, "Enter your name",
                    "Welcome", JOptionPane.QUESTION_MESSAGE);
            flag = username.equals("");
        } while (flag);
        serverAddrs = InetAddress.getByName(Properties.SERVER_IP);
        byte[] buffer = username.getBytes();
        client = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                serverAddrs, Properties.PORT);
        client.send(packet);
        client.close();
    }

    private void getShipsCoordenates() {
        int x = 0;
        int y = 0;
        boolean flag = false, vertical = false;
        Ship tmp = null;
        List <Ship> valid = new ArrayList<>();
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
                    if (vertical) {
                        if (y + Properties.SHIPLENGTHS[i] > 10) {
                            throw new Exception("Invalid postion for 0-10");
                        }
                    } else {
                        if (x + Properties.SHIPLENGTHS[i] > 10) {
                            throw new Exception("Invalid position A-J");
                        }
                    }
                    tmp = new Ship(Properties.SHIPNAMES[i], x, y,
                                    Properties.SHIPLENGTHS[i], vertical);
                    if(!Ship.isValidPosition(valid, tmp)){
                        throw new Exception("Invalid position: overlapped");
                    }
                    flag = false;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input, try again", "Oops! " + ex.getMessage(),
                            JOptionPane.ERROR_MESSAGE);
                    flag = true;
                }
            } while (flag);
            ships[i] = tmp;
            valid.add(tmp);
        }
        valid.clear();
    }

    private void setShips() {
        int x, y;
        for (int i = 0; i < ships.length; i++) {
            System.out.println(ships[i]);
            x = ships[i].getX();
            y = ships[i].getY();
            for (int j = 0; j < ships[i].getLength(); j++) {
                labelsMatrix[y][x].setBackground(Color.DARK_GRAY);
                labelsMatrix[y][x].setText(String.valueOf(
                        ships[i].getName().charAt(0)));
                labelsMatrix[y][x].setForeground(Color.CYAN);
                labelsMatrix[y][x].setOpaque(true);
                if (ships[i].getVertical()) {
                    y++;
                } else {
                    x++;
                }
            }
        }
    }

}
