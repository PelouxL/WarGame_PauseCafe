package wargame;

import java.awt.Graphics;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;
import wargame.Obstacle.TypeObstacle;

public class Carte implements IConfig, ICarte {
	private Element[][] carte;

	public Carte() {
		carte = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
		Position p;
		
		// Parcours de la matrice d'elements
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				carte[i][j] = null;
			}
		}
		
		// Placement des obstacles Aleatoirement
		// Placement de la riviere
		p = trouvePositionVide();
		carte[p.getX()][p.getY()] = new Obstacle(TypeObstacle.EAU, p);
		riviere(p); // Ajouter des ponts
		
		//Placement des autres obstacles
		for(int i = 0; i < NB_OBSTACLES; i++) {
			p = trouvePositionVide();
			carte[p.getX()][p.getY()] = new Obstacle(TypeObstacle.getObstacleAlea(), p);
			
		}
		
		// Placement des heros aleatoirement
	    for(int i = 0; i < NB_HEROS; i++) {
			p = trouvePositionVide();
			carte[p.getX()][p.getY()] = new Heros(this, TypesH.getTypeHAlea(), "blabla", p);
		}
	    
		// Placement des monstres Aleatoirement
		for(int i = 0; i < NB_MONSTRES; i++) {
			p = trouvePositionVide();
			carte[p.getX()][p.getY()] = new Monstre(this, TypesM.getTypeMAlea(), "blabla", p);
		}
				
	}

	
	// Riviere
	private void riviere(Position pos) {
		int r = (int)(Math.random()*3);
		switch(r) {
		case 0: riviereV(pos); break;
		case 1: riviereH(pos); break;
		case 2: riviereV(pos); riviereH(pos); break;
		}
	}
	
	private void riviereV(Position pos) {
		for (int i = 0; i < HAUTEUR_CARTE; i++) {
			Position p = new Position(pos.getX(), i);
			carte[p.getX()][p.getY()] = new Obstacle(TypeObstacle.EAU, p);
		}
		// Ponts
		carte[pos.getX()][(int)(Math.random()*pos.getY())] = null;
		carte[pos.getX()][(int)(Math.random() * (HAUTEUR_CARTE - pos.getY()) + pos.getY())] = null;
	}
	
	private void riviereH(Position pos) {	
		for (int i = 0; i < LARGEUR_CARTE; i++) {
			Position p = new Position(i, pos.getY());
			carte[p.getX()][p.getY()] = new Obstacle(TypeObstacle.EAU, p);
		}
		// Ponts
		carte[(int)(Math.random()*pos.getX())][pos.getY()] = null;
		carte[(int)(Math.random() * (LARGEUR_CARTE - pos.getX()) + pos.getX())][pos.getY()] = null;
	}
	// Riviere
	
	public Element getElement(Position pos) {
		if (pos.estValide()) {
			return carte[pos.getX()][pos.getY()];
		}
		System.out.println("Erreur : getElement :  0 <= x < " + LARGEUR_CARTE + " | 0 <= y < " + HAUTEUR_CARTE);
		return null;
	}

	
	// positionVide
	// a Verifier si un typeH/M/Obstacle peut être null 
	public Position trouvePositionVide() {
		int x, y;
		Position pos;
		
		do {
			x = (int) (Math.random()*LARGEUR_CARTE);
			y = (int) (Math.random()*HAUTEUR_CARTE);
			pos = new Position(x, y);			
		} while (!pos.estValide() || this.getElement(pos) != null);
		
		return pos;
	}


	public Position trouvePositionVide(Position pos) {
		EnsemblePosition listePos = new EnsemblePosition(8);
		int dx, dy;
		
		// On regarde les 8 cases autour pos
		for(dx = -1 ; dx <= 1 ; dx++) {
			for(dy = -1 ; dy <= 1 ; dy++) {
				// On passe pos
				if (dx == 0 && dy == 0) {
					continue;
				}
				
				// on cree une position et verifie qu'elle correspond si oui alors inserer dans liste
				int x = pos.getX() + dx;
				int y = pos.getY() + dy;
				Position newp = new Position(x, y);
				if (newp.estValide() && getElement(newp) == null) {
					listePos.ajouterPos(newp);
				}
			}
				
		}
		if (listePos.getNbPos() == 0) {
			return null;
		}
		return listePos.getPosition((int)(Math.random()*listePos.getNbPos()-1));
	}
	
	// trouveHeros
	public Heros trouveHeros() {
		int nbHeros = 0;
		Heros[] listeHeros = new Heros[NB_HEROS];
		int x, y;
		
		for(x = 0 ; x < LARGEUR_CARTE ; x++) {
			for(y = 0 ; y < HAUTEUR_CARTE ; y++) {
				Element e = carte[x][y];
				// on verifie si e est une instanciation de Heros
				if (e instanceof Heros) {
					// /!\ ATTENTION
					// GROS GROS DOUTES sur le faite de cast e avec Heros
					listeHeros[nbHeros] = (Heros) e;
					nbHeros++;
				}
			}
		}
		return listeHeros[(int)(Math.random()*nbHeros - 1)];
	}

	public Heros trouveHeros(Position pos) {
		int nbHeros = 0;
		int dx, dy;
		Heros[] listeHeros = new Heros[NB_HEROS];
		
		for(dx = -1 ; dx < 1 ; dx++) {
			for(dy = -1 ; dy < 1 ; dy++) {
				// on ignore la position actuelle
				if (dy == 0 && dx == 0) {
					continue;
				}
				
				Element e = carte[pos.getX() + dx][pos.getY() + dy];
				if (e.pos.estValide()) {
					// on verifie si e est une instanciation de Heros
					if (e instanceof Heros) {
						// /!\ ATTENTION
						// GROS GROS DOUTES sur le faite de cast e avec Heros
						listeHeros[nbHeros++] = (Heros) e;
					}
				}
			}
		}
		return listeHeros[(int) (Math.random()*nbHeros - 1)];
	}
	// trouveHeros
	
	// deplaceSoldat
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
			EnsemblePosition ep = soldat.zoneDeplacement();
			
			if(ep.contient(pos)) {
				this.carte[soldat.getPos().getX()][soldat.getPos().getY()] = null;
				this.carte[pos.getX()][pos.getY()] = soldat;
				soldat.seDeplace(pos);
				return true;
			}
		return false;
	}
	// deplaceSoldat
	
	// mort
	// comprends pas trop la methode, je suppose qu'elle met un mort sur la carte
	// celui ci doit compter comme obstacle ???
	public void mort(Soldat perso) {
		/*
		carte[perso.getPos().getX()][perso.getPos().getY()] = mort;
		// avoir une variable global hero restant ? 
		nb_heros_restant--
        */
	}
	// mort
	
	// actionHeros
	public boolean actionHeros(Position pos, Position pos2) {
		
		if(!(getElement(pos) instanceof Heros)) return false;
		
		return true;
	}
	// actionHeros
	
	public void jouerSoldats(PanneauJeu pj) {}
	public void toutDessiner(Graphics g) {}
	
	
	
}


