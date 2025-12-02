package wargame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.Dimension;


public class FenetreJeu {

	public static void main(String[] args) {
		Carte carte = new Carte();
		
		// Creation de la frame
		JFrame frame = new JFrame("Wargame le 2: La pause café, l'affrontement final");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(IConfig.LARGEUR_FENETRE, IConfig.HAUTEUR_FENETRE));
		frame.setLocation(IConfig.POSITION_X, IConfig.POSITION_Y );
		
		// Ajout du JPanel
		PanneauJeu PanneauCarte = new PanneauJeu(carte);
		frame.add(PanneauCarte, BorderLayout.CENTER);
		
		// Visibilité de la Jframe
		frame.pack();
		frame.setVisible(true);
		
	}

}
