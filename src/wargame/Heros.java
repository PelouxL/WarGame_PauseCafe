package wargame;

import wargame.ISoldat.TypesH;

public class Heros extends Soldat{
	private final TypesH TYPE;
	private final String NOM;
	private Carte carte;
	private Position pos;
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
	}
	
	public Position getPos() {
		return this.pos;
	}
	
	public void setPos(Position pos) {
		this.pos.setX(pos.getX());
		this.pos.setY(pos.getY());
	}
	
	public int getTour() {
		return 1; // utiliser une variable pour tour
	}
	
	public int getPoints() {
		return getPoints();
	}
	
	public int getPortee() {
		return getPortee();
	}
	
	public void seDeplace(Position newPos) {
	
	}
	
    public void combat(Soldat soldat) { // dois definir ces fonction pour enlever erreurs
     
    }

 
    public void joueTour(int tour) { // dois definir ces fonction pour enlever erreurs
    
    }
    
    
	
}
