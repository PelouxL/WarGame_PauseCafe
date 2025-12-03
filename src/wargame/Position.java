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
		if (x < 0 || x >= LARGEUR_CARTE || y < 0 || y >= HAUTEUR_CARTE) return false; 
		else return true;
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
	
	public boolean estVoisine(Position pos) {
		return ((Math.abs(this.x-pos.x) <= 1) && (Math.abs(this.y-pos.y) <= 1));
	}
	
	public boolean equals(Position pos) {
		return (this.x == pos.x && this.y == pos.y);
	}
	
	public boolean adjacent(Position p) {
		if ((Math.abs(this.x - p.x) == 1 && this.y == p.y)
			|| (Math.abs(this.y - p.y) == 1 && this.x == p.x)) {
			return true;
		}
		return false;
	}
	
	public int distance(Position p) {
		return Math.abs(this.getX() - p.getX()) + Math.abs(this.getY() - p.getY());
	}
}