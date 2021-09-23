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

/**
 *
 * @author huert
 */
public class BattleshipClient extends JFrame{
    
    private JPanel mainPanel;
    private JButton buttonsMatrix[][];
    private int valuesMatrix[][];

    public BattleshipClient() {
        initComponents();
        setComponents();
        addComponents();
        
    }
    
    public static void main(String args[]){
        new BattleshipClient();
    }

    private void setComponents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void addComponents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        buttonsMatrix = new JButton[Game.Properties.dimension][Game.Properties.dimension];
        valuesMatrix = new int[Game.Properties.dimension][Game.Properties.dimension];
    }
    
}
