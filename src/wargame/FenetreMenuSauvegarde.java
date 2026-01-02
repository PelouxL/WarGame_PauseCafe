package wargame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;import javax.swing.JFrame;


/**
 * Fenêtre de la sauvegarde de l'état du jeu.
 * <p>
 * Cette classe affiche le menu des sauvegardes disponible
 * et permet d'enregistrer sa partie.
 */
public class FenetreMenuSauvegarde extends JFrame{
	private Carte carte;
	
	/**
	 * Constructeur de la fenêtre du menu des chargements.
	 * <p>
	 * Initialise les boutons, leur disposition et les actions associées.
	 */
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
