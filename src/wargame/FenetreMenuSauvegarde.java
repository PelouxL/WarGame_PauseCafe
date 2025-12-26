package wargame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FenetreMenuSauvegarde extends JFrame{
	private Carte carte;
	
	public FenetreMenuSauvegarde(FenetreCarte fenetre) {
		super("Menu des sauvegardes");		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // centre la fenêtre
        setLayout(new GridLayout(3, 1, 10, 10));
        
        for(int i = 1; i <= 3; i++) {
        	int slot = i;
        	JButton bouton = new JButton("slot"+slot);
        	bouton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				fenetre.sauvegarderSlot(slot);
        		}
    		
        	});
        add(bouton);
   
        setVisible(true);
	}
   
		    	
    }
       
}
