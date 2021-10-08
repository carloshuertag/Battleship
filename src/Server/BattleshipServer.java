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
    private static boolean serverTurn;

    public static void main(String args[]) {
        byte[] buff;
        DatagramPacket packet;
        String username;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        boolean end = false;
        try {
            InetAddress dir = InetAddress.getByName(Properties.SERVER_IP);
            DatagramSocket server = new DatagramSocket(Properties.PORT);
            server.setReuseAddress(true);
            System.out.println("Battleship server at port: " + server.getLocalPort() + " Waiting for players...");
            Ship[] ships = new Ship[7];
            oos = new ObjectOutputStream(baos);
            for (;;) {
                packet = new DatagramPacket(new byte[65535], 65535);
                server.receive(packet);
                username = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Hello user: " + username + " , match starts");
                server.connect(packet.getSocketAddress());
                buff = new String("start").getBytes();
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
                    while(!end){
                        if(serverTurn){
                            
                        } else {
                            
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
