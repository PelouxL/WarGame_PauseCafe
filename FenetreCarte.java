package wargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class FenetreCarte extends JFrame {
	final Carte[] carteActive = { new Carte() };
	
	public FenetreCarte(Carte carte) {
		super("Wargame : Le combat final, kostine a l'attaque");
		
		carteActive[0] = carte;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(IConfig.POSITION_X, IConfig.POSITION_Y );
		
		// ------------ JMenuBar ------------- //
		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(IConfig.LARGEUR_FENETRE, IConfig.HAUTEUR_JMENU));
		menuBar.setBackground(Color.WHITE);
		
		// ------------ Onglet Fichier ------- //
		JMenu menuFichier = new JMenu("Fichier");
		menuBar.add(menuFichier);
		
		JMenuItem menuCharger = new JMenuItem("Charger");
		JMenuItem menuSauvegarder = new JMenuItem("sauvegarder");
		JMenuItem menuExporter = new JMenuItem("Exporter");
		JMenuItem menuImporter = new JMenuItem("Importer");
		JMenuItem menuQuitter = new JMenuItem("Quitter");
		menuFichier.add(menuCharger);
		menuFichier.add(menuSauvegarder);
		menuFichier.add(menuExporter);
		menuFichier.add(menuImporter);
		menuFichier.add(menuQuitter);
		
		// ------------- Creation PanneauJeu ----------- //
		PanneauJeu panneauCarte = new PanneauJeu(carteActive[0]);
		panneauCarte.setPreferredSize(new Dimension(IConfig.LARGEUR_FENETRE, IConfig.HAUTEUR_FENETRE));
		
		// ------------- Action du Menu --------------- //
		menuQuitter.addActionListener(e -> dispose());
		
		menuSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileOutputStream fichier = new FileOutputStream("Carte.ser");
					ObjectOutputStream oos = new ObjectOutputStream(fichier);
					oos.writeObject(carteActive[0]);
					oos.flush();
					oos.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		menuCharger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileInputStream fichier;
				try {
	
					fichier = new FileInputStream("Carte.ser");
					ObjectInputStream ois = new ObjectInputStream(fichier);
					carteActive[0] = (Carte) ois.readObject();
					panneauCarte.setCarte(carteActive[0]);
					
					panneauCarte.repaint();
					
					
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
				
		
		
		add(menuBar, BorderLayout.NORTH);
		add(panneauCarte, BorderLayout.CENTER);
		
		
		// Visibilité de la Jframe
		pack();
		setVisible(true);
	}
}
