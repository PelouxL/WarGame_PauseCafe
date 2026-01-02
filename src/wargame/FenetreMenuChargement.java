package wargame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Fenêtre du chargement des sauvegardes.
 * <p>
 * Cette classe affiche le menu des sauvegardes disponible
 * et permet de charger une sauvegardes existante.
 */
public class FenetreMenuChargement extends JFrame{
	
	 /**
     * Constructeur de la fenêtre du menu des chargements.
     * <p>
     * Initialise les boutons, leur disposition et les actions associées.
     * @param permet l'accès a la fenetre dans les boutons d'action
     */
	public FenetreMenuChargement(FenetreCarte fenetre) {
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
    				if(fenetre == null) {
    					FenetreCarte fenetreJ = new FenetreCarte(new Carte());
    					
    					fenetreJ.chargerSlot(slot);
    					dispose();
    				}
        		}
        	});
        add(bouton);
   
        setVisible(true);
	}
	}
}
