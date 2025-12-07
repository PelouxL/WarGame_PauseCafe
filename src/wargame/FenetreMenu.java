package wargame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.awt.Color;
import java.awt.Component;

public class FenetreMenu extends JFrame implements IConfig{
	
		public FenetreMenu () {
			super("Wargame 2: l'avenement du cafe, le retour");

			
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
					FileInputStream fichier;
						try {
							fichier = new FileInputStream("Carte.ser");
							ObjectInputStream ois = new ObjectInputStream(fichier);
							new FenetreCarte((Carte) ois.readObject());
							dispose();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}	
			});
			
			add(panneauPrincipal);
			setVisible(true);
			pack();
		}
	
	
}
