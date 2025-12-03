package wargame;


public class Position implements IConfig {
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
	
	// Plus besoin pour les hexagones normalement
	/*
	public boolean adjacent(Position p) {
		if ((Math.abs(this.x - p.x) == 1 && this.y == p.y)
			|| (Math.abs(this.y - p.y) == 1 && this.x == p.x)) {
			return true;
		}
		return false;
	}
	*/
	
	public int distance(Position p) {
		int [] cube1 = this.cube();
		int [] cube2 = p.cube();
		return (Math.abs(cube1[0] - cube2[0]) +
				Math.abs(cube1[1] - cube2[1]) +
				Math.abs(cube1[2] - cube2[2])) / 2;
	}
	
	private int[] cube() {
		int x, y, z;
		int col = this.getX() / 2;
		x = col - (this.getY() - (this.getY() % 2)) / 2;
		z = this.getY();
		y = -x - z;
		int [] resultat = {x, y, z};
		return resultat;
	}
	
	/*
	public int distance(Position p) {
		int dist = 0;
		int i = this.getX();
		Position dep = new Position(this.getX(), this.getY());
		while (!dep.equals(p) && i >= 0) {
			dep.setX(dist);
			dep.verifDiagonales(p);
			i--;
		}
		while (!dep.equals(p) && i < LARGEUR_CARTE*2) {
			dep.setX(dist);
			dep.verifDiagonales(p);
			i += 2;
		}
	}
	
	private boolean verifDiagonales(Position p) {
		
	}
	*/
	/*
	public int distance(Position p) {
		double x1 = Math.divideExact((int) this.getX(), 2); 
		double y1 = this.getY();
		double x2 = Math.divideExact((int) p.getX(), 2);
		double y2 = p.getY();
		double du = x2 - x1;
		double dv = (y2 + Math.divideExact((int) x2, 2)) - (y1 + Math.divideExact((int) x1, 2));
		if ((du >= 0 && dv >= 0) || (du < 0 && dv < 0)) {
			return (int) (Math.max(Math.abs(du), Math.abs(dv)));
		} else {
			return (int) (Math.abs(du) + Math.abs(dv));
		}
	}
	*/
	/*
	public int distance(Position p) {
		int dist = 0;
		int i;
		int dx, dy;
		int [] coordsx = {-2, -1, -1, 1, 1, 2};
		int [] coordsy = {0, 1, -1, 1, -1, 0};
		boolean continuer = true;
		EnsemblePosition ensp = new EnsemblePosition(LARGEUR_CARTE * HAUTEUR_CARTE);
		while (continuer) {
			for (i = 0 ; i < 6 ; i++) {
				dx = coordsx[i];
				dy = coordsy[i];
				Position p2 = new Position(this.getX() + dx, this.getY() + dy);
				if (this.estVoisine(p2)) {
					continuer = false;
				}
				if (coordsx[i] < 0) {
					coordsx[i] -= 2;
				}
				if (coordsx[i] > 0) {
					coordsx[i] += 2;
				}
			}
			dist++;
		}
		return dist;
	}

	private boolean distanceAux(Position p, EnsemblePosition dejaVisite) {
		int i;
		int dx, dy;
		int [] coordsx = {-2, -1, -1, 1, 1, 2};
		int [] coordsy = {0, 1, -1, 1, -1, 0};
		boolean trouve = false;
		for (i = 0 ; i < 6 ; i++) {
			dx = coordsx[i];
			dy = coordsy[i];
			Position p2 = new Position(p.getX() + dx, p.getY() + dy);
			if (!dejaVisite.contient(p2)) {
				dejaVisite.ajouterPos(p2);
			}
			if (p.equals(p2)) {
				trouve = true;
			}
		}
		return trouve;
	}
	*/
}