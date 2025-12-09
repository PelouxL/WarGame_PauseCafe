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
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	// Methodes
	public boolean estValide() {
		if (x < 0 || x >= LARGEUR_CARTE*2 || y < 0 || y >= HAUTEUR_CARTE) {
			return false; 
		} else {
			if ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
	
	public boolean estVoisine(Position pos) {
		int i;
		int dx, dy, x, y;
		int [] coordsx = {-2, -1, -1, 1, 1, 2};
		int [] coordsy = {0, 1, -1, 1, -1, 0};
		boolean voisine = false;
		
		// On regarde les 6 cases autour de pos
		for (i = 0 ; i < 6 ; i++) {
			dx = coordsx[i];
			dy = coordsy[i];
			x = this.getX() + dx;
			y = this.getY() + dy;
			if (this.equals(pos)) {
				voisine = true;
			}
		}
		return voisine;
	}
	
	public boolean equals(Position pos) {
		return (this.x == pos.x && this.y == pos.y);
	}
	
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
	
	
	/* ANCIENNE VERSION
	public int[] cube() {
		int x, y, z;
		int col = this.getX() / 2;
		x = col - (this.getY() - (this.getY() % 2)) / 2;
		z = this.getY();
		y = -x - z;
		int [] resultat = {x, y, z};
		return resultat;
	}
	
	public int[] coord(int x, int y, int z) {
		int ligne = z;
		int col = x + (ligne - (ligne % 2)) / 2;
		int offset_x = ligne % 2;
		col = col * 2 + offset_x;
		int [] resultat = {ligne, col};
		return resultat;
	}
	*/
}