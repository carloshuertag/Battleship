/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import Game.Properties;
import Models.Ship;
import java.net.*;
import java.io.*;
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
            int max=20;
            InetAddress dir = InetAddress.getByName("127.0.0.1");
            DatagramSocket s = new DatagramSocket(Properties.PORT);
            System.out.println("Servidor de datagrama iniciado en el puerto "+s.getLocalPort() + " Esperando jugadores...");
            Ship[] ships = new Ship[7];
            while(true){
            //InetSocketAddress socketAddress = new InetSocketAddress(s.getInetAddress(), Properties.PORT );
            //s.connect(socketAddress);
            DatagramPacket p = new DatagramPacket(new byte[max],max);
            s.receive(p);
            String username = new String(p.getData());
            System.out.println("Username: " + username + " is ready to play, match starts");
            setShipsCoordenates(ships);
            
            }
            
        }catch(Exception e){
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
