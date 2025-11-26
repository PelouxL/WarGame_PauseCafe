package wargame;

import java.awt.Graphics;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;

public class Carte implements IConfig, ICarte {
	private Element[][] carte;
	private int[][] visibilite;

	public Carte() {
		carte = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
		visibilite = new int[LARGEUR_CARTE][HAUTEUR_CARTE];
		Position p;
		
		// Parcours des matrices
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				carte[i][j] = null;
				visibilite[i][j] = 0;
			}
		}
		
		// Placement des obstacles Aleatoirement
		// Placement de la riviere
		p = trouvePositionVide();
		this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		riviere(p); // Ajouter des ponts
		
		//Placement des autres obstacles
		for(int i = 0; i < NB_OBSTACLES; i++) {
			p = trouvePositionVide();
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.getObstacleAlea(), p);
			
		}
		
		// Placement des heros aleatoirement
	    for(int i = 0; i < NB_HEROS; i++) {
			p = trouvePositionVide();
			this.carte[p.getX()][p.getY()] = new Heros(this, TypesH.getTypeHAlea(), "blabla", p);
			this.visibilite = ((Soldat) this.carte[p.getX()][p.getY()]).setCasesVisibles(this.visibilite);
		}
	    
		// Placement des monstres Aleatoirement
		for(int i = 0; i < NB_MONSTRES; i++) {
			p = trouvePositionVide();
			this.carte[p.getX()][p.getY()] = new Monstre(this, TypesM.getTypeMAlea(), "blabla", p);
		}
				
	}

	
	// RIVIERE
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
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		}
		// Ponts
		this.carte[pos.getX()][(int)(Math.random()*pos.getY())] = null;
		this.carte[pos.getX()][(int)(Math.random() * (HAUTEUR_CARTE - pos.getY()) + pos.getY())] = null;
	}
	
	private void riviereH(Position pos) {	
		for (int i = 0; i < LARGEUR_CARTE; i++) {
			Position p = new Position(i, pos.getY());
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		}
		// Ponts
		this.carte[(int)(Math.random()*pos.getX())][pos.getY()] = null;
		this.carte[(int)(Math.random() * (LARGEUR_CARTE - pos.getX()) + pos.getX())][pos.getY()] = null;
	}
	// RIVIERE
	
	
	// ELEMENT
	public Element getElement(Position pos) {
		
		int x = pos.getX();
		int y = pos.getY();
		
		if (pos.estValide()) {
			return this.carte[x][y];
		}
		
		System.out.println("Erreur getElement() : x = "+x+", y = "+y);
		return null;
	}
	// ELEMENT
	
	
	// VISIBILITE
	public int getVisibilite(Position pos) {
		if (pos.estValide()) {
			return this.visibilite[pos.getX()][pos.getY()];
		}
		System.out.println("Erreur : getVisibilite :  0 <= x < " + LARGEUR_CARTE + " | 0 <= y < " + HAUTEUR_CARTE);
		return -1;
	}
	
	// CETTE FONCTION NE DEVRAIT PAS SERVIR, JE LA LAISSE AU CAS OU C'EST POUR LA VISIBILITE
	// On pourra peut-être enlever le param, et faire en sorte que cette fonction y mette à 1
	// Et à chaque fois avant de mettre à jour on remet toute la matrice à 0
	// Comme ça on a juste à remettre les visibilités actuelles (pas d'ennui à se dire est-ce que ça ça devient invisible)
	// public void setVisibilite(Position pos, int visibilite) {
	//  	this.visibilite[pos.getX()][pos.getY()] = visibilite;
	// }
	// VISIBILITE
	
	
	// POSITION VIDE
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
	// POSITION VIDE
	
	
	// TROUVE HEROS
	public Heros trouveHeros() {
		int nbHeros = 0;
		Heros[] listeHeros = new Heros[NB_HEROS];
		int x, y;
		
		for(x = 0 ; x < LARGEUR_CARTE ; x++) {
			for(y = 0 ; y < HAUTEUR_CARTE ; y++) {
				Element e = this.carte[x][y];
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
				
				Element e = this.carte[pos.getX() + dx][pos.getY() + dy];
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
	// TROUVE HEROS
	
	
	// ACTION SOLDAT (actionHeros a revoir surement)
	public boolean actionHeros(Position pos, Position pos2) {
		
		Element e = this.getElement(pos);
		
		if (!(e instanceof Heros) || ((Heros)e).getAction() <= 0) return false;
		
		Heros heros = (Heros)e;
		Element caseCible = this.getElement(pos2);
		
		// Deplacement si case vide
		if (caseCible == null) {
			return this.deplaceSoldat(pos2, heros);
		} 
		
		if (caseCible instanceof Monstre && pos.distance(pos2) <= heros.getTir()) { // on regarde que tir
			Monstre monstre = (Monstre)caseCible;
			heros.combat(monstre);
		}
		
		return true;
	}
	
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
		
			EnsemblePosition ePos = soldat.zoneDeplacement();
			
			if(ePos.contient(pos)) {
				this.carte[soldat.getPos().getX()][soldat.getPos().getY()] = null;
				this.carte[pos.getX()][pos.getY()] = soldat;
				soldat.seDeplace(pos);
				return true;
			}
			
		return false;
	}
	// ACTION SOLDAT
	
	
	// MORT
	public void mort(Soldat perso) {
		if (perso.getPointsActuels() <= 0) {
			this.carte[perso.getPos().getX()][perso.getPos().getY()] = null;
			// nb_heros_restant--;
		}
	}
	// MORT
	
	public Position coorToPos(int x, int y) {
		int px = x/NB_PIX_CASE,
			py = y/NB_PIX_CASE ;
		return new Position(px, py);
	}
	
	public void jouerSoldats(PanneauJeu pj) {}
	public void toutDessiner(Graphics g) {}
	
	
	
}


