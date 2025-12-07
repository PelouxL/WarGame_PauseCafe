package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class Competence {
	private final TypeCompetence type;
	private int tempsRestantCompetence;
	private Image imageCompetence;
	
	public Competence(TypeCompetence type) {
		this.type = type;
		tempsRestantCompetence = 0;
		this.imageCompetence = new ImageIcon("./images/icon_comp_boule_de_feu.png").getImage();
	}
	
	public TypeCompetence getType() {
		return type;
	}
	
	public boolean peutUtiliser() {
		return (tempsRestantCompetence == 0);
	}
	
	public void utiliserCompetence(Soldat lanceur, Soldat receveur) {
		if(!peutUtiliser()) {
			System.out.println("La competence" + type.getNom() + " n'est pas encore disponible !");
		}
		
		appliquerCompetence(lanceur, receveur);
			
		tempsRestantCompetence = type.getTempsRechargement();
	}
	
	public void appliquerCompetence(Soldat lanceur, Soldat receveur) {
		switch(type) {
		case BOULE_DE_FEU:
			//for(int i = 0; i < ) appliquer le sort en zone !!!!! 
			receveur.retirerPv(type.getDegats());
			// appliquer du feu sur le terrain
		}
	}
	
	public void decrementerTempsRestant() {
		if(!peutUtiliser()) {
			tempsRestantCompetence--;
		}
	}
	
	public int getTempsRestant() {
		return tempsRestantCompetence; 
	}

	public void dessinerCompetence(Graphics g) {
		System.out.println("dqsdqd");
		g.setColor(Color.black);
		g.fillRect(8, 8, 55, 55);
		if(imageCompetence != null) {
			g.drawImage(imageCompetence, 10, 10, 50, 50, null);
		}else {
			System.out.println("l'image ne charge pas !");

		}
	}
	
	   public void changerImageCompetence(String cheminImage) {
	        this.imageCompetence = new ImageIcon(cheminImage).getImage();  // Charge une nouvelle image
	    }
}
