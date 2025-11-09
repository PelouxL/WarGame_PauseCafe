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

	// peut-être à mettre dans Soldat aussi
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.pos.setX(newPos.getX());
			this.pos.setY(newPos.getY());
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}

	public void setJoue() {
		if (this.getTour() == 1) {
			this.joue = true;
		} else {
			this.joue = false;
		}
	}

	public boolean getJoue() {
		return this.joue;
	}
	
    public void combat(Soldat soldat) { // doit definir ces fonctions pour enlever erreurs
     
    }

 
    public void joueTour(int tour) { // doit definir ces fonctions pour enlever erreurs
    
    }
    
    
	
}
