package wargame;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.Dimension;


public class FenetreJeu {

	public static void main(String[] args) {
		Carte carte = new Carte();
		
		// Creation de la frame
		JFrame frame = new JFrame("Wargame le 2: La pause café, l'affrontement final");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(IConfig.POSITION_X, IConfig.POSITION_Y );
		
		// Ajout du JPanel
		PanneauJeu PanneauCarte = new PanneauJeu(carte);
		
		PanneauCarte.setPreferredSize(new Dimension(IConfig.LARGEUR_FENETRE, IConfig.HAUTEUR_FENETRE));
		frame.add(PanneauCarte, BorderLayout.CENTER);
		
		// Visibilité de la Jframe
		frame.pack();
		frame.setVisible(true);
		
		System.out.println("Frame = " + frame.getWidth() + "x" + frame.getHeight());
		System.out.println("Carte = " + PanneauCarte.getWidth() + "x" + PanneauCarte.getHeight());

		
	}

}
