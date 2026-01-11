package wargame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Component;

/**
 * Fenêtre principale du menu du jeu.
 * <p>
 * Cette classe affiche le menu initial du jeu Wargame, 
 * avec des boutons permettant de lancer une nouvelle partie, 
 * charger une partie existante ou quitter le jeu.
 */
public class FenetreMenu extends JFrame implements IConfig{
	
    /**
     * Constructeur de la fenêtre du menu.
     * <p>
     * Initialise les boutons, leur disposition et les actions associées.
     */
		public FenetreMenu () {
			super("Wargame : Les monstres attaquent !");

			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setLocation(POSITION_LOG_X, POSITION_Y);
			
			/* --------------- Fenêtre de gestion des boutons ----------- */
			JPanel panneauPrincipal = new JPanel();
			panneauPrincipal.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE ));
			panneauPrincipal.setBackground(COULEUR_PLATEAU);
			
			panneauPrincipal.setLayout(new BoxLayout(panneauPrincipal, BoxLayout.Y_AXIS));
			panneauPrincipal.setAlignmentX(CENTER_ALIGNMENT);
			
			JButton bouttonJouer = creerBoutonMenu("Lancez une partie");
			JButton bouttonCharger = creerBoutonMenu("Charger une partie");
			JButton bouttonQuitter = creerBoutonMenu("Quitter");
			
			
			/* ------ Création des boutons et de leur disposition --------*/
			bouttonJouer.setAlignmentX(Component.CENTER_ALIGNMENT);
			bouttonCharger.setAlignmentX(Component.CENTER_ALIGNMENT);
			bouttonQuitter.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			panneauPrincipal.add(Box.createVerticalGlue());
			panneauPrincipal.add(bouttonJouer);
			panneauPrincipal.add(Box.createVerticalStrut(100));
			panneauPrincipal.add(bouttonCharger);
			panneauPrincipal.add(Box.createVerticalStrut(100));
			panneauPrincipal.add(bouttonQuitter);
			panneauPrincipal.add(Box.createVerticalGlue());
			
			
			bouttonQuitter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			bouttonJouer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					new FenetreCarte(new Carte());
				}
			});
			
			bouttonCharger.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new FenetreMenuChargement(null);
					dispose();
				}	
			});
			
			add(panneauPrincipal);
			setVisible(true);
			pack();
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
