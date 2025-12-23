package wargame;

public class EnsemblePosition {
	private Position[] ePos;
	private final int TAILLE_MAX; 
	private int nbPos = 0;
	
	// Constructeur
	public EnsemblePosition(int n) {
		this.ePos = new Position[n];
		this.TAILLE_MAX = n;
	}
	
	// Accesseurs/Mutateurs
	public Position[] getEPos() {
		return ePos;
	}
	
	public int getTailleMax() {
		return this.TAILLE_MAX;
	}
	
	public int getNbPos() {
		return this.nbPos;
	}
	
	// Methodes
	public void ajouterPos(Position pos) {
		//System.out.println("NBPOS " + nbPos);
		this.ePos[nbPos++] = pos;
	}
	
	public void retirerPremierePos() {
		if (nbPos < 1) {
			System.out.println("Erreur retirerPremierePos : ensemble vide");
		} else {
			ePos[0] = null;
			for (int j = 0 ; j < nbPos - 1 ; j++) {
				ePos[j] = ePos[j+1];
			}
			ePos[nbPos-1] = null;
			nbPos--;
		}
	}
	
	public void retirerDernierePos() {
		if (nbPos < 1) {
			System.out.println("Erreur retirerDernierePos : ensemble vide");
		} else {
			ePos[nbPos-1] = null;
			nbPos--;
		}
	}
	
	public void retirerPos(int i) {
		if (i >= nbPos) {
			System.out.println("Erreur retirerPos : i >= nbPos");
		} else {
			ePos[i] = null;
			for (int j = i ; j < nbPos - 1 ; j++) {
				ePos[j] = ePos[j+1];
			}
			ePos[nbPos-1] = null;
			nbPos--;
		}
	}
	
	public boolean contient(Position pos) {
	    for (int i = 0 ; i < nbPos ; i++) {
	        if (ePos[i].equals(pos)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	// renvoie l'indice de la première position égale à pos, sinon -1
	public int indexPosition(Position pos) {
		for (int i = 0 ; i < nbPos ; i++) {
			if (ePos[i].equals(pos)) {
				return i;
			}
		}
		return -1;
	}
	
	public Position getPosition(int i) {
		return this.ePos[i];
	}
	
	public boolean estVide() {
		return nbPos == 0;
	}
	
}
