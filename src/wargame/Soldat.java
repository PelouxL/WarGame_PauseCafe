package wargame;

public abstract class Soldat extends Element implements ISoldat{
	private final int POINT_DE_VIE, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointsDeVie;
	private Position pos;
	private Carte carte;
	private int tour = 1; // 1 = joueur ; 0 = IA
	
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINT_DE_VIE = pointsDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
	}
	
	// POSITION
	public Position getPos() {
		return this.pos;
	}
	
	public void setPos(Position pos) {
		this.pos.setX(pos.getX());
		this.pos.setY(pos.getY());
	}
	// POSITION
	
	// TOUR
	public int getTour() {
		return this.tour;
	}

	public void setTour() {
		if (this.tour == 1) {
			this.tour = 0;
		}
		this.tour = 1;
	}
	
	public void joueTour(int tour) { // on sait pas ce que ça fait ???
    	
    }
	// TOUR

	// POINTS DE VIE
	public int getPoints() {
		return this.POINT_DE_VIE;
	}

	public int getPointsActuels() {
		return this.pointsDeVie;
	}
	
	public void setPointsActuels(int nouveauPoints) {
		this.pointsDeVie = nouveauPoints;
	}
	// POINTS DE VIE
	
	// PORTEE
	public int getPortee() {
		return this.PORTEE_VISUELLE;
	}
	// PORTEE
	
	// PUISSANCE
	public int getPuissance() {
		return PUISSANCE;
	}
	// PUISSANCE
	
	// TIR
	public int getTir() {
		return TIR;
	}
	//TIR
	
	// COMBAT
	public void combat(Soldat soldat) {
		int degats_joueur = this.getPuissance();
	    int pv_ennemi = soldat.getPointsActuels();
	    pv_ennemi -= degats_joueur;
	    soldat.setPointsActuels(pv_ennemi);
    }
	// COMBAT

	// DEPLACEMENT
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.pos.setX(newPos.getX());
			this.pos.setY(newPos.getY());
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}
	// DEPLACEMENT
	
}
