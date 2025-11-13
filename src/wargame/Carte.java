package wargame;

import java.awt.Graphics;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;
import wargame.Obstacle.TypeObstacle;

public class Carte implements IConfig, ICarte {
	private Element[][] carte;

	public Carte() {
		carte = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
		// on parcours la table
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				carte[i][j] = null;
			}
		}
				// on place nos heros
	    for(int k = 0; k < NB_HEROS; k++) {
			Position p = trouvePositionVide();
			carte[p.getX()][p.getY()] = new Heros(this, TypesH.getTypeHAlea(), "blabla", p);
		}
		// on place nos heros
		for(int k = 0; k < NB_MONSTRES; k++) {
			Position p = trouvePositionVide();
			carte[p.getX()][p.getY()] = new Monstre(this, TypesM.getTypeMAlea(), "blabla", p);
		}
				
		// on place nos obstacle
		for(int k = 0; k < NB_OBSTACLES; k++) {
			Position p = trouvePositionVide();
			carte[p.getX()][p.getY()] = new Obstacle(TypeObstacle.getObstacleAlea(), p);
		}
	}
		
		
	
	public Element getElement(Position pos) {
		if (pos.estValide()) {
			return carte[pos.getX()][pos.getY()];
		}
		System.out.println("Erreur : getElement :  0 <= x < " + LARGEUR_CARTE + " | 0 <= y < " + HAUTEUR_CARTE);
		return null;
	}

	
	// a Verifier si un typeH/M/Obstacle peut être null 
	public Position trouvePositionVide() {
		int x, y;
		Position pos;
		
		do {
			x = (int) (Math.random()*LARGEUR_CARTE);
			y = (int) (Math.random()*HAUTEUR_CARTE);
			pos = new Position(x, y);
			
		} while (pos.estValide());
		
		return new Position(x, y);
	}


	public Position trouvePositionVide(Position pos) {
		Position[] listePos = new Position[8];
		int indicePos = 0;
		int dx, dy;
		
		// on regarde les 8 cases autour de la position originale
		for(dx = -1 ; dx <= 1 ; dx++) {
			for(dy = -1 ; dy <= 1 ; dy++) {
				// on ne regarde pas la position originale
				if (dx == 0 && dy == 0) {
					continue;
				}
				// on cree une position et verifie qu'elle correspond si oui alors inserer dans liste
				Position p = new Position(pos.getX() + dx, pos.getY() + dy);
				if (p.estValide() && getElement(p) == null) {
					listePos[indicePos] = p;
					indicePos++;
				}
			}
				
		}
		if (indicePos == 0) {
			return null;
		}
		if (indicePos == 1) {
			return listePos[0];
		}		
		return listePos[(int) (Math.random()*indicePos - 1)];
	}
	
	
	public Heros trouveHeros() {
		int indiceHeros = 0;
		Heros[] listeHeros = new Heros[NB_HEROS];
		int x, y;
		
		for(x = 0 ; x < LARGEUR_CARTE ; x++) {
			for(y = 0 ; y < HAUTEUR_CARTE ; y++) {
				Element e = carte[x][y];
				// on verifie si e est une instanciation de Heros
				if (e instanceof Heros) {
					// /!\ ATTENTION
					// GROS GROS DOUTES sur le faite de cast e avec Heros
					listeHeros[indiceHeros] = (Heros) e;
					indiceHeros++;
				}
			}
		}
		return listeHeros[(int)(Math.random()*indiceHeros - 1)];
	}

	public Heros trouveHeros(Position pos) {
		int indiceHeros = 0;
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
						listeHeros[indiceHeros] = (Heros) e;
						indiceHeros++;
					}
				}
			}
		}
		return listeHeros[(int) (Math.random()*indiceHeros - 1)];
	}
	
	
	// Version très légère qui ne gère pas les endroits bloqués par une rivière ou CAYOU
	// en gros on peut traverser les obstacles mais se poser dessus
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
		int diffDeplacement;
		Position posSoldat = soldat.getPos();
		
		if (pos.estValide() && getElement(pos) == null) {
			diffDeplacement = Math.abs(pos.getX() - posSoldat.getX()) + Math.abs(pos.getY() - posSoldat.getY());
			if (diffDeplacement > 8) {
				return false;
			}
			carte[pos.getX()][pos.getY()] = soldat;
			carte[posSoldat.getX()][posSoldat.getY()] = null;
		}
		return true;
		
	}
	// comprends pas trop la methode, je suppose qu'elle met un mort sur la carte
	// celui ci doit compter comme obstacle ???
	public void mort(Soldat perso) {
		/*
		carte[perso.getPos().getX()][perso.getPos().getY()] = mort;
		// avoir une variable global hero restant ? 
		nb_heros_restant--
        */
	}
	
	public boolean actionHeros(Position pos, Position pos2) {
		return true;
	}
	
	public void jouerSoldats(PanneauJeu pj) {}
	public void toutDessiner(Graphics g) {}
	
	
	
}


