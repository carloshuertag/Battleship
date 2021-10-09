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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
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

    private Ship[] ships, serverShips;
    private JPanel mainPanel, tablesPanel, infoPanel, userTable, pcTable;
    private JButton buttonsMatrix[][];
    private JLabel labelsMatrix[][], ys[][], xs[][], shipsNames[], shipsInfo[],
            turn, attempts, title, usernameLabel, refLabel[];
    private Border borders;
    private InetAddress serverAddrs;
    private DatagramSocket client;
    private String username;
    private DatagramPacket packet;
    private ByteArrayInputStream bais;
    private ObjectInputStream ois;
    private byte[] buffer;
    private boolean trn;
    private int serverShipsLeft, clientShipsLeft, attmpts;

    public BattleshipClient() {
        initPanels();
        initComponents();
        setComponents();
        addComponents();
        setFrame();
        connectWithServer();
        getShipsCoordenates();
        setShips(ships, labelsMatrix);
        setReady();
        getServerShips();
        play();
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
        borders = BorderFactory.createLineBorder(Color.BLUE);
        shipsNames = new JLabel[7];
        shipsInfo = new JLabel[7];
        IntStream.range(0, shipsNames.length).forEach(i -> {
            shipsNames[i] = new JLabel(Properties.SHIPNAMES[i]);
            shipsInfo[i] = new JLabel(String.valueOf(Properties.SHIPLENGTHS[i]));
        });
        turn = new JLabel("Turn: Username/PC");
        attmpts = 1;
        attempts = new JLabel("Attempts remaining: " + attmpts);
        title = new JLabel("Battleship", SwingConstants.CENTER);
        usernameLabel = new JLabel("Username", SwingConstants.CENTER);
        ships = new Ship[7];
        serverShips = new Ship[7];
    }

    private void setComponents() {
        setPcTable();
        setUserTable();
    }

    private void addComponents() {
        mainPanel.add(title, BorderLayout.NORTH);
        IntStream.range(0, shipsNames.length).forEach(i -> {
            infoPanel.add(shipsNames[i]);
            infoPanel.add(shipsInfo[i]);
        });
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
        IntStream.range(0, Properties.DIMENSION).forEach(i -> {
            xs[i][1] = new JLabel(String.valueOf(i + 1));
            setCell(xs[i][1], Color.GRAY, Color.WHITE);
            userTable.add(xs[i][1]);
        });
        IntStream.range(0, Properties.DIMENSION).forEach(i -> {
            IntStream.range(0, Properties.DIMENSION).forEach(j -> {
                if (j == 0) {
                    ys[i][1] = new JLabel(String.valueOf((char) (i + 65)));
                    setCell(ys[i][1], Color.GRAY, Color.WHITE);
                    userTable.add(ys[i][1]);
                }
                labelsMatrix[i][j] = new JLabel("≈", SwingConstants.CENTER);
                setCell(labelsMatrix[i][j], Color.CYAN, Color.BLUE);
                userTable.add(labelsMatrix[i][j]);
            });
        });
    }

    private void setPcTable() {
        refLabel[0] = new JLabel("X, N");
        setCell(refLabel[0], Color.GRAY, Color.WHITE);
        pcTable.add(refLabel[0]);
        IntStream.range(0, Properties.DIMENSION).forEach(i -> {
            xs[i][0] = new JLabel(String.valueOf(i + 1));
            setCell(xs[i][0], Color.GRAY, Color.WHITE);
            pcTable.add(xs[i][0]);
        });
        IntStream.range(0, Properties.DIMENSION).forEach(i -> {
            IntStream.range(0, Properties.DIMENSION).forEach(j -> {
                if (j == 0) {
                    ys[i][0] = new JLabel(String.valueOf((char) (i + 65)));
                    setCell(ys[i][0], Color.GRAY, Color.WHITE);
                    pcTable.add(ys[i][0]);
                }
                buttonsMatrix[i][j] = new JButton("≈");
                setCell(buttonsMatrix[i][j], Color.CYAN, Color.BLUE);
                pcTable.add(buttonsMatrix[i][j]);
            });
        });
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

    private void connectWithServer() {
        boolean flag = false;
        do {
            try {
                hello();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Cannot connect to server", "Oops" + ex.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
                flag = true;
            }
        } while (flag);
    }

    private void hello() throws UnknownHostException, SocketException,
            IOException, Exception {
        boolean flag;
        do {
            username = JOptionPane.showInputDialog(null, "Enter your name",
                    "Welcome", JOptionPane.QUESTION_MESSAGE);
            flag = username.equals("");
        } while (flag);
        serverAddrs = InetAddress.getByName(Properties.SERVER_IP);
        buffer = username.getBytes();
        client = new DatagramSocket();
        client.setReuseAddress(true);
        packet = new DatagramPacket(buffer, buffer.length, serverAddrs,
                Properties.PORT);
        client.send(packet);
        buffer = new byte[65535];
        packet = new DatagramPacket(buffer, buffer.length, serverAddrs,
                Properties.PORT);
        client.receive(packet);
        if (!(new String(packet.getData(), 0, packet.getLength()).equals("start"))) {
            throw new Exception("Cannot connect to server, try again");
        }
    }

    private void getShipsCoordenates() {
        int x = 0;
        int y = 0;
        boolean flag = false, vertical = false;
        Ship tmp = null;
        List<Ship> valid = new ArrayList<>();
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
                            JOptionPane.QUESTION_MESSAGE).toUpperCase().charAt(0) - 65;
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
                    if (!Ship.isValidPosition(valid, tmp)) {
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

    private void setShips(Ship[] ships, JComponent[][] matrix) {
        int x, y;
        for (int i = 0; i < ships.length; i++) {
            x = ships[i].getX();
            y = ships[i].getY();
            for (int j = 0; j < ships[i].getLength(); j++) {
                matrix[y][x].setBackground(Color.DARK_GRAY);
                if (matrix[y][x] instanceof JLabel) {
                    ((JLabel) (matrix[y][x])).setText(String.valueOf(
                            ships[i].getName().charAt(0)));
                } else if (matrix[y][x] instanceof JButton) {
                    ((JButton) (matrix[y][x])).setText(String.valueOf(
                            ships[i].getName().charAt(0)));
                }
                matrix[y][x].setForeground(Color.CYAN);
                matrix[y][x].setOpaque(true);
                if (ships[i].isVertical()) {
                    y++;
                } else {
                    x++;
                }
            }
        }
    }

    private void setReady() {
        buffer = "ready".getBytes();
        packet = new DatagramPacket(buffer, buffer.length, serverAddrs,
                Properties.PORT);
        try {
            client.send(packet);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to server", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(1);
        }
    }

    private void getServerShips() {
        buffer = new byte[65535];
        packet = new DatagramPacket(buffer, 65535);
        try {
            for (int i = 0; i < Properties.SHIPNAMES.length; i++) {
                client.receive(packet);
                bais = new ByteArrayInputStream(packet.getData());
                ois = new ObjectInputStream(bais);
                serverShips[i] = (Ship) ois.readObject();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to server", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(1);
        }
    }

    private void serverTurn() throws Exception {
        String serverShoot;
        int x, y;
        buffer = new byte[65535];
        packet = new DatagramPacket(buffer, 65535);
        client.receive(packet);
        serverShoot = new String(packet.getData(), 0, packet.getLength());
        if (serverShoot.substring(1, 2).equals(",")) {
            x = Integer.parseInt(serverShoot.substring(0, 1));
            y = Integer.parseInt(serverShoot.substring(2, 3));
            for (Ship ship : ships) {
                if (Ship.isDamaged(ship, y, x)) {
                    trn = true;
                    setCell(labelsMatrix[y][x], Color.RED, Color.BLACK);
                    if (ship.getLife() == 0) {
                        clientShipsLeft--;
                        JOptionPane.showMessageDialog(null, ship.getName()
                                + " is down", "Ship down",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (clientShipsLeft == 0) {
                            JOptionPane.showMessageDialog(null, "You loose",
                                    "Game over", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            System.exit(0);
                        }
                    }
                } else {
                    setCell(labelsMatrix[y][x], Color.BLUE, Color.WHITE);
                    trn = false;
                }
            }
            labelsMatrix[y][x].setText("x");
        } else {
            throw new Exception("Server shoot failed");
        }
        buffer = String.valueOf(trn).getBytes();
        packet = new DatagramPacket(buffer, buffer.length, serverAddrs,
                Properties.PORT);
        client.send(packet);
        buffer = String.valueOf(clientShipsLeft == 0).getBytes();
        packet = new DatagramPacket(buffer, buffer.length, serverAddrs,
                Properties.PORT);
        client.send(packet);
    }

    private void play() {
        IntStream.range(0, Properties.DIMENSION).forEach(i -> {
            IntStream.range(0, Properties.DIMENSION).forEach(j -> {
                buttonsMatrix[i][j].addActionListener(e -> buttonHandler(i, j));
            });
        });
        clientShipsLeft = serverShipsLeft = 7;
        buffer = new byte[65535];
        packet = new DatagramPacket(buffer, 65535);
        try {
            client.receive(packet);
            trn = Boolean.parseBoolean(new String(packet.getData(), 0, packet.getLength()));
            if (trn) {
                serverTurn();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Fatal error at connection", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(1);
        }

    }

    private void buttonHandler(int x, int y) {
        StringBuilder shoot = new StringBuilder();
        shoot.append(x);
        shoot.append(',');
        shoot.append(y);
        buffer = shoot.toString().getBytes();
        packet = new DatagramPacket(buffer, buffer.length, serverAddrs,
                Properties.PORT);
        try {
            client.send(packet);
            buffer = new byte[65535];
            packet = new DatagramPacket(buffer, 65535);
            client.receive(packet);
            trn = Boolean.parseBoolean(new String(packet.getData(), 0, packet.getLength()));
            buffer = new byte[65535];
            packet = new DatagramPacket(buffer, 65535);
            client.receive(packet);
            serverShipsLeft = Integer.parseInt(new String(packet.getData(), 0, packet.getLength()));
            if (!trn) {
                attmpts++;
                setCell(buttonsMatrix[x][y], Color.RED, Color.BLACK);
            } else {
                setCell(buttonsMatrix[x][y], Color.BLUE, Color.WHITE);
            }
            buttonsMatrix[x][y].setText("x");
            buttonsMatrix[x][y].setEnabled(false);
            if (serverShipsLeft == 0) {
                JOptionPane.showMessageDialog(null, "You win", "Game over",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                System.exit(0);
            }
            if(attmpts == 3){
                attmpts = 1;
                trn = true;
            }
            if (trn) {
                serverTurn();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Fatal error at connection", "Oops" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(1);
        }
    }

}
