package wargame;

import java.io.Serializable;

public class Position implements IConfig, Serializable {
	private int x, y;
	
	// Constructeur
	public Position(int x, int y) { 
		this.x = x; 
		this.y = y;
	}
	
	// Accesseurs/Mutateurs
	public int getX() {	return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	
	// Methodes
	public boolean estValide() {
		if (x < 0 || x >= LARGEUR_CARTE*2 || y < 0 || y >= HAUTEUR_CARTE) return false; 
		else {
			if ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1)) return false;
			else return true;
		}
	}
	
	// POSITIONS VOISINES
	public EnsemblePosition voisines() {
		EnsemblePosition voisines = new EnsemblePosition(6);
		
		int[] x = {-2, -1, -1, 1, 1, 2};
		int[] y = {0, 1, -1, 1, -1, 0};
		
		for (int i=0; i < x.length; i++) {
			Position v = new Position(x[i] + this.getX(), y[i] + this.getY());
			if (v.estValide()) {
				voisines.ajouterPos(v);
			}
		}
		
		return voisines;
	}
	
	public EnsemblePosition voisines(int rayon, boolean centre) {
		int nbVoisinesMax = (3 * rayon) * (rayon +1) +1 ;
		EnsemblePosition voisines = new EnsemblePosition(nbVoisinesMax);
		
		if (centre) voisines.ajouterPos(this);
		
		this.voisinesAux(this, this, rayon, voisines);
		
		return voisines;
	}
	
	private void voisinesAux(Position posInit, Position pos, int rayon, EnsemblePosition voisines) {
		if (rayon <= -1 || !pos.estValide()) return;
		if (!voisines.contient(pos) && !posInit.equals(pos)) {
			voisines.ajouterPos(pos);
			
		}

		EnsemblePosition voisinesPos = pos.voisines();
		
		for (int i=0; i < voisinesPos.getNbPos(); i++) {
			Position test = voisinesPos.getPosition(i);
			test.voisinesAux(posInit, test, rayon-1, voisines);
		}
		
	}
	
	private int getFactoriel(int val) {
		int resultat = 1;
		for (int i=2; i <= val; i++) {
			resultat *= i;
		}
		return resultat;
	}
	
	// NE FONCTIONNE PAS , OUT OF BOUNDS
	public EnsemblePosition voisinesCroix(int rayon) {
		EnsemblePosition voisines = new EnsemblePosition(6*rayon);
		
		int[] x = {-2, -1, -1, 1, 1, 2};
		int[] y = {0, 1, -1, 1, -1, 0};
		
		for (int i=1; i <= rayon; i++) {
			for (int j=0; j < x.length; j++) {
				Position pos = new Position(x[j]*i + this.getX(), y[j]*i + this.getY());
				if (pos.estValide()) { voisines.ajouterPos(pos); }
			}
		}
		
		return voisines;
	}	
	
	public boolean estVoisine(Position pos) {
		return this.voisines().contient(pos);
	}
	// POSITIONS VOISINES
	
	
	// DISTANCE
	public int distance(Position p) {
		int [] cube1 = this.cube();
		int [] cube2 = p.cube();
		return (Math.abs(cube1[0] - cube2[0]) +
				Math.abs(cube1[1] - cube2[1]) +
				Math.abs(cube1[2] - cube2[2])) / 2;
	}
	
	private int[] cube() {
		int q = (this.getX() - this.getY()) / 2;
		int r = this.getY();
		int [] resultat = {q, r, -q-r};
		return resultat;
	}
	
	// pas utilisée, sauf si j'arrive à faire mes clics sur hexagone
	public int[] coord(int q, int r, int s) {
		int col = 2 * q + r;
		int ligne = r;
		int [] resultat = {col, ligne};
		return resultat;
	}
	// DISTANCE
	
	
	public boolean equals(Position pos) {
		return (this.x == pos.x && this.y == pos.y);
	}
	
	
	public String toString() {
		return "("+x+","+y+")";
	}
}