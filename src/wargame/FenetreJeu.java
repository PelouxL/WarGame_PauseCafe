package wargame;

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
		
		/* Modification du JPanel par defaut
		frame.getContentPane().setBackground(Color.GRAY);
		frame.getContentPane().setPreferredSize(new Dimension(600, 400)); */
		
		// Creation de la JMenuBar
		JMenuBar GreenMenuBar = new JMenuBar();
		GreenMenuBar.setOpaque(true);
		GreenMenuBar.setBackground(Color.green);
		GreenMenuBar.setPreferredSize(new Dimension(200,100));
		frame.setJMenuBar(GreenMenuBar);
		
		// Ajout du JPanel
		PanneauJeu PanneauCarte = new PanneauJeu(carte);
		frame.add(PanneauCarte);
		
		// Visibilité de la Jframe
		frame.pack();
		frame.setVisible(true);
		
	}

}
