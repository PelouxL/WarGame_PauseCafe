package wargame;

import wargame.ISoldat.TypesH;

public class Heros extends Soldat{
	private final TypesH TYPE;
	private final String NOM;
	private Carte carte;
	private Position pos;
	private boolean joue;
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
	}
	
	public int getTour() {
		if (tour) {
			return 1;
		}
		return 0;
	}
	
	public int getPoints() {
		return getPoints();
	}
	
	public int getPortee() {
		return this.PORTEE_VISUELLE;
	}
	
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.setX(newPos.getX());
			this.setY(newPos.getY());
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}
	
    public void combat(Soldat soldat) { // doit definir ces fonctions pour enlever erreurs
     
    }

 
    public void joueTour(int tour) { // doit definir ces fonctions pour enlever erreurs
    
    }
    
    
	
}
