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
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
/**
 *
 * @author huert
 */
public class BattleshipServer {
    
    public static void main(String args[]){
        try{
            InetAddress dir = InetAddress.getByName(Properties.SERVER_IP);
            DatagramSocket s = new DatagramSocket(Properties.PORT);
            s.setReuseAddress(true);
            System.out.println("Servidor de datagrama iniciado en el puerto "+s.getLocalPort() + " Esperando jugadores...");
            Ship[] ships = new Ship[7];
            for(;;){
                DatagramPacket packet = new DatagramPacket(new byte[65535],65535);
                s.receive(packet);
                String username = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Hello user: " + username + " , match starts");
                s.connect(packet.getSocketAddress());
                byte[] buff = new String("start").getBytes();
                packet = new DatagramPacket(buff, buff.length);
                s.send(packet);
                setShipsCoordenates(ships);
                packet = new DatagramPacket(new byte[65535],65535);
                s.receive(packet);
                if(new String(packet.getData(), 0, packet.getLength()).equals("ready")){
                    for(Ship ship: ships){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(ship);
                        oos.flush();
                        buff = baos.toByteArray();
                        packet = new DatagramPacket(buff, buff.length);
                        s.send(packet);
                    }
                } else {
                    throw new Exception("Client not ready");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static void setShipsCoordenates(Ship[] ships) {
        Random random = new Random();
        
        int x,y,op;
        
        boolean flag = false, vertical = false;
        Ship tmp = null;
        List <Ship> valid = new ArrayList<>();
        for (int i = 0; i < ships.length; i++) {
            do {
                try {
                   x = random.nextInt(10);
                   y = random.nextInt(10);
                   op = random.nextInt(2);
                   if(op==0)
                    vertical = false;
                   else
                       vertical=true;
                    
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
                    if(!Ship.isValidPosition(valid, tmp)){
                        throw new Exception("Invalid position: overlapped");
                    }
                    flag = false;
                } catch (Exception ex) {
                    //ex.printStackTrace();
                    flag = true;
                }
            } while (flag);
            ships[i] = tmp;
            valid.add(tmp);
        }
        valid.clear();
        
         for (int i = 0; i < ships.length; i++) {
            System.out.println(ships[i]);
         }
    }
}
