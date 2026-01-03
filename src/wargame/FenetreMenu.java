package wargame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
			
			/* --------------- fenetre de gestion des boutons ----------- */
			JPanel panneauPrincipal = new JPanel();
			panneauPrincipal.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE ));
			panneauPrincipal.setBackground(Color.GRAY);
			
			panneauPrincipal.setLayout(new BoxLayout(panneauPrincipal, BoxLayout.Y_AXIS));
			panneauPrincipal.setAlignmentX(CENTER_ALIGNMENT);
			
			JButton bouttonJouer = new JButton("Lancez une partie");
			JButton bouttonCharger = new JButton("Charger une partie");
			JButton bouttonQuitter = new JButton("Quitter");
			
			
			/* ------ creation des boutons et de leur disposition --------*/
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
	
	
}
