package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Competence {
	
	public enum TypeCompetence {
		BOULE_DE_FEU("boule de feu", 2, 30, 10, true, 3, 2),
		SOIN("soin", 1, -15, 4, false, 1, 0),
		SOIN_DE_ZONE("soin de zone", 2, -20, 6, false, 4, 3),
		COUP_EPEE("coup d'épée", 1, 10, 1, false, 1, 0),
		TIR_A_PORTER("tir a porter", 1, 10, 10, false, 1, 0);
		
		private final String nom;
		private final int coutAction;
		private int degats;
		private final int distance;
		private final boolean donneVisu;
		private final int degatsZone;
		private final int tempsRechargement; 
		
		TypeCompetence(String nom, int coutAction, int degats, int distance, boolean donneVisu, int degatZone, int tempsRechargement) {
			this.nom = nom;
			this.coutAction = coutAction;
			this.degats = degats;
			this.distance = distance;
			this.donneVisu = donneVisu;
			this.degatsZone = degatZone;
			this.tempsRechargement = tempsRechargement;
		}
		
	    public String getNom() { return nom; }
	    public int getCoutAction() { return coutAction; }
	    public int getDegats() { return degats; }
	    public void setDegats(int degats) { this.degats = degats; }
	    public int getDistance() { return distance; }
	    public boolean isDonneVisu() { return donneVisu; }
	    public int getDegatsZone() { return degatsZone; }
	    public int getTempsRechargement() { return tempsRechargement; };	
	}
	
	private final TypeCompetence type;
	private int tempsRestantCompetence;
	private Image imageCompetence;
	
	public Competence(TypeCompetence type) {
		this.type = type;
		tempsRestantCompetence = 0;
		

		this.imageCompetence = new ImageIcon(trouverImg()).getImage();
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
	
	
	public enum TypeCompetence {
		BOULE_DE_FEU("boule de feu", 2, 30, 10, true, 3, 2),
		SOIN("soin", 1, -15, 4, false, 1, 0),
		SOIN_DE_ZONE("soin de zone", 2, -20, 6, false, 4, 3),
		COUP_EPEE("coup d'épée", 1, 10, 1, false, 1, 0),
		TIR_A_PORTER("tir a porter", 1, 10, 10, false, 1, 0);
		
		private final String nom;
		private final int coutAction;
		private int degats;
		private final int distance;
		private final boolean donneVisu;
		private final int degatsZone;
		private final int tempsRechargement; 
		
		TypeCompetence(String nom, int coutAction, int degats, int distance, boolean donneVisu, int degatZone, int tempsRechargement) {
			this.nom = nom;
			this.coutAction = coutAction;
			this.degats = degats;
			this.distance = distance;
			this.donneVisu = donneVisu;
			this.degatsZone = degatZone;
			this.tempsRechargement = tempsRechargement;
		}
		
	    public String getNom() { return nom; }
	    public int getCoutAction() { return coutAction; }
	    public int getDegats() { return degats; }
	    public void setDegats(int degats) { this.degats = degats; }
	    public int getDistance() { return distance; }
	    public boolean isDonneVisu() { return donneVisu; }
	    public int getDegatsZone() { return degatsZone; }
	    public int getTempsRechargement() { return tempsRechargement; };	
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

	public void dessinerCompetence(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.fillRect(x, y, 55, 55);
		g.drawRect(x, y, 150, 55);
		if(imageCompetence != null) {
			g.drawImage(imageCompetence, x, y, 50, 55, null);
		}else {
			System.out.println("l'image ne charge pas !");
		}
		g.drawString("" + type.getNom(), x+60, y+30);
	}
	
	public void changerImageCompetence(String cheminImage) {
       this.imageCompetence = new ImageIcon(cheminImage).getImage();  // Charge une nouvelle image
	}
	   
	   
	   public String trouverImg() {
		   String path = "./images/comp/icon_comp_";
		   switch(type.getNom()) {
		   case "boule de feu":
			   path +="boule_de_feu";
			   break;
		   case "soin":
			   path += "soin";
			   break;
		   case "soin de zone":
			   path += "soin_de_zone";
			   break;
		   case "coup d'épée":
			   path += "coup_epee";
			   break;
		   case "tir a porter":
			   path += "tir_a_porter";
			   break;
		   case "default":
			   path += "default";
			   break;
		   }
		   
	   path += ".png";
	   return path;
	   }
	   
}
