package wargame;

public abstract class Soldat extends Element implements ISoldat{
	private final int POINT_DE_VIE, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointDeVie;
	private Position pos;
	private Carte carte;
	private int tour = 1; // 1 = joueur ; 0 = IA
	
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINT_DE_VIE = pointDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
	}
	
	public Position getPos() {
		return this.pos;
	}
	
	public void setPos(Position pos) {
		this.pos.setX(pos.getX());
		this.pos.setY(pos.getY());
	}
	
	public int getTour() {
		return this.tour;
	}

	public void setTour() {
		if (this.tour == 1) {
			this.tour = 0;
		}
		this.tour = 1;
	}

	public int getPoints() {
		return this.POINT_DE_VIE;
	}

	public int getPointsActuels() {
		return this.pointDeVie;
	}
	
	public int getPortee() {
		return this.PORTEE_VISUELLE;
	}
	
	public int getPuissance() {
		return PUISSANCE;
	}
	
	public int getTir() {
		return TIR;
	}
	
	public void joueTour(int tour) {
    	
    }
	
	public void combat(Soldat soldat) {
	     
    }

	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.pos.setX(newPos.getX());
			this.pos.setY(newPos.getY());
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}
	
}
