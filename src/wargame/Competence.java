package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * Représente les compétences utilisables par les Soldats.
 * <p>
 * Une compétence peut infliger des dégâts, soigner ou appliquer des effets
 * selon son type. Chaque compétence possède :
 * <ul>
 * 		<li>une portée</li>
 * 		<li>une zone d'effet</li>
 * 		<li>un temps de rechargement</li>
 * </ul>
 * </p>
 */
public class Competence implements ICompetence, Serializable {

	private final TypeCompetence type;
	private int tempsRestantCompetence;
	private transient Image imageCompetence;
	
	/**
	 * Crée une nouvelle compétence à partir de son type.
	 * La compétence est initialement disponible.
	 *
	 * @param type le type de la compétence
	 */
	public Competence(TypeCompetence type) {
		this.type = type;
		tempsRestantCompetence = 0;
		this.imageCompetence = new ImageIcon(trouverImg()).getImage();
	}
	
	/**
	 * Indique si la compétence peut être utilisée.
	 *
	 * @return true si la compétence est disponible, false sinon
	 */
	public boolean peutUtiliser() { return (tempsRestantCompetence == 0); }
	
	/**
	 * Tente d'utiliser la compétence sur une position donnée.
	 * Vérifie la disponibilité, la portée et la zone d'effet.
	 *
	 * @param lanceur le soldat lançant la compétence
	 * @param receveur la position ciblée
	 * @param carte la carte du jeu
	 */
	public void utiliserCompetence(Soldat lanceur, Position receveur, Carte carte) {
		if (!peutUtiliser()) {
			//System.out.println("La competence " + type.getNom() + " n'est pas encore disponible !");
		} else if (lanceur.getAction() < type.getCoutAction()) {
			//System.out.println("Vous n'avez pas assez de points de competence !");
		} else {
			if (lanceur.getPos().distance(receveur) <= type.getDistance()
			   && this.zoneAttaque(lanceur.getPos(), carte).contient(receveur)) {
				
				this.appliquerCompetence(lanceur.getPos(), receveur, carte);
				lanceur.setAction(lanceur.getAction() - type.getCoutAction());
				tempsRestantCompetence = type.getTempsRechargement();
			}
		}
	}
	
	/**
	 * Applique les effets de la compétence sur la carte.
	 * Les effets dépendent du type de compétence.
	 *
	 * @param lanceur la position du lanceur
	 * @param receveur la position ciblée
	 * @param carte la carte du jeu
	 */
	public void appliquerCompetence(Position lanceur, Position receveur, Carte carte) {
		Soldat soldat = carte.getSoldat(receveur);
		
		String log = "";
		String atq = "";
		String receveurs = "";
		
		Soldat caster = carte.getSoldat(lanceur);
		
		switch(type) {
		case BOULE_DE_FEU:	
			atq += caster.recupIdentite() + " lance une boule de feu en zone !\n";
			EnsemblePosition zoneAttaque = receveur.voisines(type.getDegatsZone(), true);
			//zoneAttaque.ajouterPos(receveur);
			for(int i = 0; i < zoneAttaque.getNbPos(); i++) {
				Soldat soldats = carte.getSoldat(zoneAttaque.getPosition(i));
				if(soldats != null) {
					receveurs += "---- ";
					receveurs += soldats.recupIdentite();
					receveurs +=  " a reçu " + this.type.getDegats() + " points de dégâts !"; 
					soldats.retirerPv(type.getDegats());
					if(soldats.estMort()) {
						carte.mort(soldats);
						receveurs += "Il a succombé !";
					}
					receveurs += "\n";
				}
			}
			break;

		case COUP_EPEE:
			atq += caster.recupIdentite() + " Donne un coup d'épée !\n";
			if(soldat != null) {
				receveurs += soldat.recupIdentite();
				receveurs += " a reçu " + (type.getDegats() + soldat.getPuissance()) + " points de dégâts !";
				soldat.retirerPv(type.getDegats() + soldat.getPuissance());
				if(soldat.estMort()) {
					carte.mort(soldat);
					receveurs += "Il a succombé !";
				}
			}else {
				receveurs += "Wooaw ! le vent tremble devant votre puissance !";
			}
			receveurs += "\n";
			break;

		case SOIN:
			atq += caster.recupIdentite() + " Lance un sort de soin !\n";
			if(soldat != null) {
				receveurs += soldat.recupIdentite();
				receveurs += " a reçu " + (type.getDegats() + soldat.getPuissance()) + " points de vie !";
				soldat.ajouterPv(type.getDegats() + soldat.getPuissance());
			}else {
				receveurs += "Le vent vous remercie pour votre générosité.";
			}
			receveurs += "\n";
			break;

		case SOIN_DE_ZONE:
			atq += caster.recupIdentite() + " Lance un sort de soin de zone !\n";
			EnsemblePosition zoneSoin = receveur.voisines(type.getDegatsZone(), false);
			for(int i = 0; i < zoneSoin.getNbPos(); i++) {				
				Soldat soldats = carte.getSoldat(zoneSoin.getPosition(i));
				if(soldats != null) {
					receveurs += "---- ";
					receveurs += soldats.recupIdentite();
					receveurs +=  " a reçu " + this.type.getDegats() + " points de vie !\n"; 
					soldats.ajouterPv(type.getDegats());
				}
			}
			break;

		case TIR_A_PORTER:
			atq += caster.recupIdentite() + " Décoche une flèche ! !\n";
			if(soldat != null) {	
				receveurs += "---- ";
				receveurs += soldat.recupIdentite();
				receveurs +=  " a reçu " + (this.type.getDegats() + caster.getTir()) + " points de dégâts !";
				soldat.retirerPv(type.getDegats() + caster.getTir());
				if(soldat.estMort()) {
					carte.mort(soldat);
					receveurs += "Il a succombé !";
				}
		    }else{
		    	receveurs += "Vous avez réussi à transpercer le sol ! Bravo.";
		    }
			receveurs += "\n";
			break;
			
		case LANCE_PIERRE:
			atq += caster.recupIdentite() + "Tir un coup de fronde !\n";
			if(soldat != null) {	
				receveurs += "---- ";
				receveurs += soldat.recupIdentite();
				receveurs +=  " a reçu " + this.type.getDegats() + " points de dégâts !";
				soldat.retirerPv(type.getDegats() + soldat.getPuissance());
				receveurs +=  " il a reçu une pierre sur la tête ! il est affaiblit !";
				soldat.setAction(soldat.getAction()-1);
				if(soldat.estMort()) {
					carte.mort(soldat);
					receveurs += "Il a succombé !";
				}
				receveurs += "\n";
		    }else{
		    	receveurs += "La pierre ricoche sur le sol ! Bravo.";
		    }
			break;
			
		case COUP_DE_BATON:
			atq += caster.recupIdentite() + "Met un coup de baton dans les jambes !\n";
			if(soldat != null) {	
				receveurs += "---- ";
				receveurs += soldat.recupIdentite();
				receveurs +=  " a reçu " + this.type.getDegats() + " points de dégâts !";
				soldat.retirerPv(type.getDegats() + soldat.getPuissance());
				receveurs +=  " Il est paralysé !";
				soldat.setAction(soldat.getAction()-2);
				if(soldat.estMort()) {
					carte.mort(soldat);
					receveurs += "Il a succombé !";
				}
		    }else{
		    	receveurs += "Le vent tremble devant votre baton.";
		    }
			receveurs += "\n";
			break;
		}
		
		
		log += atq + receveurs;
		carte.addCombatMessage(log);
	}
	
	/**
	 * Décrémente le temps de rechargement de la compétence.
	 */
	public void decrementerTempsRestant() { if(!peutUtiliser()) tempsRestantCompetence--; }
	
	/**
	 * Dessine la compétence dans l'interface graphique.
	 *
	 * @param g le contexte graphique
	 * @param x la position horizontale
	 * @param y la position verticale
	 */
	public void dessinerCompetence(Graphics g, int x, int y) {
		g.setColor(Color.black);
		g.fillRect(x, y, 55, 55);
		g.drawRect(x, y, 150, 55);
		if(imageCompetence != null) {
			g.drawImage(imageCompetence, x, y, 50, 55, null);
		}
		g.drawString("" + type.getNom(), x+60, y+30);
	}
	
	/**
	 * Change l'image associée à la compétence.
	 *
	 * @param cheminImage le chemin vers la nouvelle image
	 */
	public void changerImageCompetence(String cheminImage) {
       this.imageCompetence = new ImageIcon(cheminImage).getImage();
	}
	   
	/**
	 * Détermine les cases où la compétence peut être lancée.
	 *
	 * @param pos la position du lanceur
	 * @param carte la carte du jeu
	 * @return ensemble des positions ciblables
	 */
	public EnsemblePosition zoneAttaque(Position pos, Carte carte) {
		EnsemblePosition ePos;
		switch(type.getZoneLancer()) {
		case "ligne": // lancé sur les cases alignées au lanceur, les obstacles genent
			ePos = new EnsemblePosition(6*type.getDistance() + 1);
			
			int[] x = {-2, -1, -1, 1, 1, 2};
			int[] y = {0, 1, -1, 1, -1, 0};
			
			for (int i = 0; i < x.length; i++) { // pour chaque direction
				for (int j = 1; j <= type.getDistance(); j++) { // ligne a tester
					Position posTest = new Position(x[i]*j + pos.getX(), y[i]*j + pos.getY());
					
					// si un rocher est recontré arret de la boucle sinon on ajoute et on continu
					if (posTest.estValide() && carte.getCase(posTest).getType() != Terrain.TypeTerrain.ROCHER) {
						ePos.ajouterPos(posTest);
					} else break;
				}
			}
			break;
			
		case "cercle": // lancé sur les cases autour du lancer dans un certain rayon, les obstacles genent
			ePos = pos.voisines(type.getDistance(), false);
			
			// Test de chaque position
			for (int i = 0; i < ePos.getNbPos(); i++) {
				// Position posTest = ePos.getPosition(i);
				
				// On trace une ligne imaginaire de la pos initial jusqu'a la position de test
				
				// On regarde par quelle cases passe la ligne
				
				// On verifie chaque position, si l'une d'entre elles est consideree comme un obstacle alors
				// on la supprime de ePos
				
			}
			
			
			break;
			
		case "libre": // pas de restriction de lancé
			ePos = pos.voisines(type.getDistance(), false);	
			break;
		default:
			ePos = null;
			break;
		}
		
		// Si c'est une competence de soin le lanceur peut la lancer sur lui-meme
		if (type.getClasseCompetence() == ClasseCompetence.SOINS) {
			ePos.ajouterPos(pos);
		}
		
		return ePos;
	}
	
	/**
	 * Retourne la couleur associée au type de compétence (soin, debuff, dégâts).
	 *
	 * @param pos la position ciblée
	 * @return couleur utilisée pour l'affichage
	 */
	public Color typeCouleurAttaque(Position pos){
		switch(type.getClasseCompetence()) {
		case ClasseCompetence.ATTAQUE: return new Color(255,0,0, 180);
		case ClasseCompetence.SOINS: return new Color(0,255,0, 180);
		case ClasseCompetence.DEBUFF: return new Color(126, 14, 134, 180);
		case ClasseCompetence.BUFF: return new Color(255, 235, 56, 180);
		default: return Color.PINK;
		}		
	}
	   
	/**
	 * Détermine le chemin de l'image associée à la compétence.
	 *
	 * @return chemin du fichier image
	 */
	public String trouverImg() {
		String path = "./images/comp/icon_comp_";
	
		switch(type.getNom()) {
		case "Boule de feu": path +="boule_de_feu"; break;
		case "Soin": path += "soin"; break;
		case "Soin de zone": path += "soin_de_zone"; break;
		case "Coup d'épée": path += "coup_epee"; break;
		case "Tir à portée": path += "tir_a_porter"; break;
		case "Lance-pierre": path += "lance_pierre"; break;
		case "Coup de baton": path += "coup_de_baton"; break;
		case "default": path += "default"; break;
		}
	
		path += ".png";
		return path;
	}
	
	public TypeCompetence getType() { return type; }
	public int getTempsRestant() { return tempsRestantCompetence; }
}
