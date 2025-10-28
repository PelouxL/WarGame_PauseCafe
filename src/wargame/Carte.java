package wargame;

public abstract class Carte implements IConfig, ICarte{
	private Element[][] carte = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
	
	
	public Element getElement(Position pos) {
		if(pos.estValide()) {
			return carte[pos.getX()][pos.getY()];
		}
		System.out.println("Erreur : getElement :  0 <= x < "+LARGEUR_CARTE+" | 0 <= y < "+HAUTEUR_CARTE);
		return null;
	}
	
	// a Verifier si un typeH/M/Obstacle peux être null 
	public Position trouverPositionVide() {
		int x, y;
		
		do {
			x = (int)Math.random()*LARGEUR_CARTE - 1;
			y = (int)Math.random()*HAUTEUR_CARTE - 1;
			
			
		}while (carte[x][y] != null);
		
		return new Position(x, y);
	}

	public Position trouvePositionVide(Position pos) {
		Position[] listePos = new Position[8];
		int indicePos = 0;
		
		// on regarde les 8 cases autour de la position original
		for(int dx = -1; dx <= 1; dx++) {
			for(int dy = -1; dy <= 1; dy++) {
				// on ne regarde pas la osition original
				if(dx == 0 && dy == 0) {
					continue;
				}
				// on cree une position et veirfie qu'elle correspond si oui alors inserer dans liste
				Position p = new Position(pos.getX() + dx, pos.getY() + dy);
				if( p.estValide() && getElement(p) == null) {
					listePos[indicePos] = p;
					indicePos++;
				}
			}
				
		}
		if(indicePos == 0) {
			return null;
		}
		if(indicePos == 1) {
			return listePos[0];
		}		
		return listePos[(int)(Math.random()*indicePos - 1)];

	}
	
	
	public Heros trouverHeros() {
		int indiceHeros = 0;
		Heros[] listeHeros = new Heros[NB_HEROS];
		
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int y = 0; y < HAUTEUR_CARTE; y++) {
				
				Element e = carte[i][y];
				// on verifie si e est une instanciation de Heros
				if(e instanceof Heros) {
					// /!\ ATTENTION
					// GROS GROS DOUTES sur le faite de cast e avec Heros
					listeHeros[indiceHeros] = (Heros) e;
					indiceHeros++;
				}
			}
		}
		return listeHeros[(int)(Math.random()*indiceHeros - 1)];
	}

	public Heros trouverHeros(Position pos) {
		int indiceHeros = 0;
		Heros[] listeHeros = new Heros[NB_HEROS];
		
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int y = 0; y < HAUTEUR_CARTE; y++) {
				
				Element e = carte[i][y];
				// on verifie si e est une instanciation de Heros
				if(e instanceof Heros) {
					// /!\ ATTENTION
					// GROS GROS DOUTES sur le faite de cast e avec Heros
					listeHeros[indiceHeros] = (Heros) e;
					indiceHeros++;
				}
			}
		}
		return listeHeros[(int)(Math.random()*indiceHeros - 1)];
	}
	
	
}