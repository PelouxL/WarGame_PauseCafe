package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Competence implements ICompetence, Serializable{
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
	
	public void utiliserCompetence(Soldat lanceur, Position receveur, Carte carte) {
		if(!peutUtiliser()) {
			System.out.println("La competence" + type.getNom() + " n'est pas encore disponible !");
		}else {
		
			// peut-être faire une surchage de la methode pour ne pas viser de hero
			if(lanceur.getPos().distance(receveur) <= type.getDistance()){
				this.appliquerCompetence(lanceur.getPos(), receveur, carte);
				lanceur.setAction(lanceur.getAction() - type.getCoutAction());
			}
			tempsRestantCompetence = type.getTempsRechargement();
		}
	}
	
	public void appliquerCompetence(Position lanceur, Position receveur, Carte carte) {
		Soldat soldat = carte.getSoldat(receveur);
		switch(type) {
		case BOULE_DE_FEU:	
				EnsemblePosition zoneAttaque = receveur.voisines(type.getDegatsZone());
				for(int i = 0; i < zoneAttaque.getNbPos(); i++) {
					Soldat soldats = carte.getSoldat(zoneAttaque.getPosition(i));
					if(soldats != null) {
						System.out.println("Bam on la toucher ! "+ soldats);
						soldats.retirerPv(type.getDegats());
						// faire bruler le sol 
					}
				}
				System.out.println("Boule de feu lancer !");
			break;
			// appliquer du feu sur le terrain
		case COUP_EPEE:
			if(soldat != null) {
				soldat.retirerPv(type.getDegats() + soldat.getPuissance());
			}else {
				System.out.println("Wooaw ! le Vent tremble devant votre puissance !");
			}	
			break;
		case SOIN:
			if(soldat != null) {
				soldat.ajouterPv(type.getDegats() + soldat.getPuissance());
			}else {
				System.out.println("Le vent vous remercie pour votre generosité.");
			}
			break;
		case SOIN_DE_ZONE:
			EnsemblePosition zoneSoin = receveur.voisines(type.getDegatsZone());
			for(int i = 0; i < zoneSoin.getNbPos(); i++) {
				Soldat soldats = carte.getSoldat(zoneSoin.getPosition(i));
				if(soldats != null) {
					soldats.ajouterPv(type.getDegats());
					// faire bruler le sol 
				}
			System.out.println("Boule de feu lancer !");
			}
			break;
		case TIR_A_PORTER:
			if(soldat != null) {
				soldat.retirerPv(type.getDegats() + soldat.getPuissance());
		    }else{
		    	System.out.println("Vous avez réussi a transpercer le sol ! Bravo.");
		    }
			break;
		}
		
	}
	
	// A CHANGER + TARD
	public EnsemblePosition porteeCompetence(Soldat lanceur) {
		int nbPosMax = (int) Math.pow(6, type.getDistance()+1); // TEMPORAIRE FAIRE VRAI CALCUL
		EnsemblePosition ePos = new EnsemblePosition(nbPosMax);
		
		porteeCompetenceAux(lanceur, lanceur.getPos(), type.getDistance(), ePos);
		
		return ePos;
	}
	
	private void porteeCompetenceAux(Soldat lanceur, Position pos, int portee, EnsemblePosition ePos) {
		
		if (!(pos.estValide())
			|| portee <= -1 
			|| lanceur.getCarte().getCase(pos).getType().getAccessible() == false
			) {
			return;
		}

		if (!(ePos.contient(pos))) { ePos.ajouterPos(pos); }
		
		int x = pos.getX();
		int y = pos.getY();
		
		// Droite
		this.porteeCompetenceAux(lanceur, new Position(x+2, y), portee-1, ePos);
		// Gauche
		this.porteeCompetenceAux(lanceur, new Position(x-2, y), portee-1, ePos);
		// Bas Gauche
		this.porteeCompetenceAux(lanceur, new Position(x-1, y+1), portee-1, ePos);
		// Bas Droite
		this.porteeCompetenceAux(lanceur, new Position(x+1, y+1), portee-1, ePos);
		// Haut Gauche
		this.porteeCompetenceAux(lanceur, new Position(x-1, y-1), portee-1, ePos);
		// Haut Droite
		this.porteeCompetenceAux(lanceur, new Position(x+1, y-1), portee-1, ePos);
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
	   
	public EnsemblePosition zoneAttaque(Position pos) {
		EnsemblePosition ePos;
		switch(type.getZoneLancer()) {
		case "ligne":
			ePos = pos.voisinesCroix(type.getDistance());
			break;
		case "libre":
			ePos = pos.voisines(type.getDistance());
			break;
		default:
			ePos = null;
			break;
		}
		
		return ePos;
	}
	
	public Color typeCouleurAttaque(Position pos){
		EnsemblePosition ePos;
		ePos = pos.voisines(type.getDegatsZone());
		switch(type.getClasseCompetence()) {
		case ATTAQUE:
			return Color.RED;
		case SOINS:
			return Color.GREEN;
		case DEBUFF:
			return Color.magenta;
		case BUFF:
			return Color.YELLOW;
		default:
			return Color.PINK;
		}		
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
