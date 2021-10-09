/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Game.Properties;
import Models.Ship;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

/**
 *
 * @author huert
 */
public class BattleshipServer {

    private static final Random RANDOM = new Random();
    private static boolean serverTurn, end;
    private static int shipsLeft = 7, clientAttempts = 0;
    private static byte[] buff;
    private static DatagramPacket packet;
    private static DatagramSocket server;
    private static Ship[] ships;

    public static void main(String args[]) {
        String username;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        List<String> prevShoots;
        String shoot;
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        int x, y;
        try {
            server = new DatagramSocket(Properties.PORT, InetAddress.getByName(Properties.SERVER_IP));
            server.setReuseAddress(true);
            System.out.println("Battleship server at port: " + server.getLocalPort() + " Waiting for players...");
            ships = new Ship[7];
            oos = new ObjectOutputStream(baos);
            for (;;) {
                packet = new DatagramPacket(new byte[65535], 65535);
                server.receive(packet);
                username = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Hello user: " + username + " , match starts");
                server.connect(packet.getSocketAddress());
                buff = "start".getBytes();
                packet = new DatagramPacket(buff, buff.length);
                server.send(packet);
                setShipsCoordenates(ships);
                packet = new DatagramPacket(new byte[65535], 65535);
                server.receive(packet);
                if (new String(packet.getData(), 0, packet.getLength()).equals("ready")) {
                    System.out.println("User is ready");
                    for (Ship ship : ships) {
                        oos.writeObject(ship);
                        oos.flush();
                        buff = baos.toByteArray();
                        baos.flush();
                        packet = new DatagramPacket(buff, buff.length);
                        server.send(packet);
                    }
                    serverTurn = RANDOM.nextBoolean();
                    System.out.println((serverTurn) ? "Server's turn" : "Client's turn");
                    buff = String.valueOf(serverTurn).getBytes();
                    packet = new DatagramPacket(buff, buff.length);
                    server.send(packet);
                    prevShoots = new ArrayList<>();
                    while(!end){
                        if(serverTurn){
                            do {
                                x = RANDOM.nextInt(10);
                                y = RANDOM.nextInt(10);
                                sb.append(x);
                                sb.append(',');
                                sb.append(y);
                                shoot = sb.toString();
                                sb.setLength(0);
                                if(!prevShoots.contains(shoot)) {
                                    prevShoots.add(shoot);
                                } else {
                                    flag = true;
                                }
                            } while(flag);
                            buff = shoot.getBytes();
                            packet = new DatagramPacket(buff, buff.length);
                            server.send(packet);
                            System.out.println("Server's shoot " + shoot);
                            buff = new byte[65535];
                            packet = new DatagramPacket(buff, buff.length);
                            server.receive(packet);
                            serverTurn = Boolean.parseBoolean(new String(packet.getData(), 0, packet.getLength()));
                            buff = new byte[65535];
                            packet = new DatagramPacket(buff, buff.length);
                            server.receive(packet);
                            end = Boolean.parseBoolean(new String(packet.getData(), 0, packet.getLength()));
                            System.out.println((end) ? "Game over, server wins": "Ships left: " + shipsLeft);
                        } else {
                            clientTurn();
                        }
                    }
                } else {
                    throw new Exception("Client not ready");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void clientTurn() throws Exception{
        String clientShoot;
        int x, y;
        buff = new byte[65535];
        packet = new DatagramPacket(buff, 65535);
        server.receive(packet);
        clientShoot = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Client's shoot "+clientShoot);
        if (clientShoot.substring(1, 2).equals(",")) {
            x = Integer.parseInt(clientShoot.substring(0, 1));
            y = Integer.parseInt(clientShoot.substring(2, 3));
            for(Ship ship: ships){
                if(Ship.isDamaged(ship, x, y)){
                    serverTurn = false;
                    if(ship.getLife() == 0){
                        shipsLeft--;
                    }
                    break;
                } else {
                    serverTurn = true;
                }
            }
            buff = String.valueOf(serverTurn).getBytes();
            packet = new DatagramPacket(buff, buff.length);
            server.send(packet);
            buff = String.valueOf(shipsLeft).getBytes();
            packet = new DatagramPacket(buff, buff.length);
            server.send(packet);
            if(shipsLeft == 0){
                System.out.println("Game over, client wins");
                end = true;
            }
            if(++clientAttempts == 3){
                clientAttempts = 0;
                serverTurn = true;
            }
        } else {
            throw new Exception("Client shoot failed");
        }
    }

    private static void setShipsCoordenates(Ship[] ships) {
        int x, y;
        boolean flag = false, vertical = false;
        Ship tmp = null;
        List<Ship> valid = new ArrayList<>();
        for (int i = 0; i < ships.length; i++) {
            do {
                x = RANDOM.nextInt(10);
                y = RANDOM.nextInt(10);
                vertical = RANDOM.nextBoolean();
                try {
                    if (vertical) {
                        if (y + Properties.SHIPLENGTHS[i] > 10) {
                            throw new Exception("Invalid postion for 0-9");
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
                    flag = true;
                }
            } while (flag);
            ships[i] = tmp;
            valid.add(tmp);
        }
        valid.clear();
    }
}
