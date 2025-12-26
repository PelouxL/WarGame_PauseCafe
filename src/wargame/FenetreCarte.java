package wargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.JOptionPane;

public class FenetreCarte extends JFrame {
	final Carte[] carteActive = { new Carte() };
    private PanneauJeu panneauCarte;

	public FenetreCarte(Carte carte) {
		super();
		
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
		panneauCarte = new PanneauJeu(carteActive[0]);
		panneauCarte.setPreferredSize(new Dimension(IConfig.LARGEUR_FENETRE, IConfig.HAUTEUR_FENETRE));
		
		// ------------- Action du Menu --------------- //
		menuQuitter.addActionListener(e -> dispose());
		
		menuSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FenetreMenuSauvegarde(FenetreCarte.this);
			}
		});
		
		menuCharger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FenetreMenuChargement(FenetreCarte.this);
				
			}
		});
				
		
		
		add(menuBar, BorderLayout.NORTH);
		add(panneauCarte, BorderLayout.CENTER);
		
		
		// Visibilité de la Jframe
		pack();
		setVisible(true);
	}
	
	  public Carte getCarte() {
	        return carteActive[0];
	    }

	    public PanneauJeu getPanneau() {
	        return panneauCarte;
	    }

	    // Méthode pour sauvegarder un slot
	    public void sauvegarderSlot(int slot) {
	        try {
	            File file = new File("Carte_slot" + slot + ".ser");
	            if(file.exists()) {
	            	// on demande si on ecrase ou non la sauvegarde
	                int rep = JOptionPane.showConfirmDialog(this,
	                        "Le slot " + slot + " existe déjà. Écraser la sauvegarde ?",
	                        "Écraser la sauvegarde",
	                        JOptionPane.YES_NO_OPTION);
	                //on sort si il ne veux pas
	                if(rep != JOptionPane.YES_OPTION) return;
	            }
	            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
	            oos.writeObject(carteActive[0]);
	            oos.close();
	            JOptionPane.showMessageDialog(this, "Sauvegarde réussie !");
	        } catch(Exception e) {
	            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde !");
	        }
	    }

	    // Méthode pour charger un slot
	    public void chargerSlot(int slot) {
	        try {
	            File file = new File("Carte_slot" + slot + ".ser");
	            if(!file.exists()) {
	                JOptionPane.showMessageDialog(this, "Aucune sauvegarde dans ce slot !");
	                return;
	            }
	            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
	            carteActive[0] = (Carte) ois.readObject();
	            ois.close();

	            panneauCarte.setCarte(carteActive[0]);
	            panneauCarte.repaint();

	            JOptionPane.showMessageDialog(this, "Chargement réussi !");
	        } catch(Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Erreur lors du chargement !");
	        }
	    }
}
