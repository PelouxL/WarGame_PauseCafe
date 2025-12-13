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
		this.ePos[nbPos++] = pos;
	}
	
	
	public boolean contient(Position pos) {
	    for (int i = 0; i < nbPos; i++) {
	        if (ePos[i].equals(pos)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public Position getPosition(int i) {
		return this.ePos[i];
	}
	
}
