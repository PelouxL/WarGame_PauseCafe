package wargame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Fenêtre du chargement des sauvegardes.
 * <p>
 * Cette classe affiche le menu des sauvegardes disponibles
 * et permet de charger une sauvegarde existante.
 */
public class FenetreMenuChargement extends JFrame{
	
	 /**
     * Constructeur de la fenêtre du menu des chargements.
     * <p>
     * Initialise les boutons, leur disposition et les actions associées.
     * @param fenetre qui permet l'accès à la fenêtre dans les boutons d'action
     * 
     */
	public FenetreMenuChargement(FenetreCarte fenetre) {
		super("Menu des sauvegardes");		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        
        setLocationRelativeTo(null); // centre la fenêtre
        setLayout(new GridLayout(3, 1, 10, 10));
        getContentPane().setBackground(new Color(40, 20, 20));
        for(int i = 1; i <= 3; i++) {
        	int slot = i;
        	JButton bouton = creerBoutonMenu("slot"+slot);
        	bouton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				if(fenetre == null) {
    					FenetreCarte fenetreJ = new FenetreCarte(new Carte());
    					
    					fenetreJ.chargerSlot(slot);
    					dispose();
    				}else {
    					FenetreCarte fenetreJ = new FenetreCarte(new Carte());
    					fenetreJ.chargerSlot(slot);
    					dispose();
    					fenetre.dispose();
    				}
        		}
        	});
        add(bouton);
   
        setVisible(true);
        }
	}
	
	private JButton creerBoutonMenu(String texte) {
	    JButton bouton = new JButton(texte);

	    bouton.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 20));
	    bouton.setPreferredSize(new Dimension(280, 60));
	    bouton.setMaximumSize(new Dimension(280, 60));

	    bouton.setForeground(Color.WHITE);
	    bouton.setBackground(new Color(80, 30, 30)); // rouge sombre
	    bouton.setFocusPainted(false);
	    bouton.setBorderPainted(false);
	    bouton.setOpaque(true);

	    // Effet hover
	    bouton.addMouseListener(new MouseAdapter() {
	        public void mouseEntered(MouseEvent e) {
	            bouton.setBackground(new Color(120, 50, 50));
	        }
	        public void mouseExited(MouseEvent e) {
	            bouton.setBackground(new Color(80, 30, 30));
	        }
	    });

	    return bouton;
	}
}
