package wargame;

public abstract class Soldat extends Element implements ISoldat{
	private final int POINT_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointDeVie;
	private Position pos;
	private Carte carte;
	private int tour = 1; // 1 = joueur ; 0 = IA
	
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINT_DE_VIE_MAX = pointDeVie = pts;
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

	public void setTour() {
		if (this.tour == 1) {
			this.tour = 0;
		}
		this.tour = 1;
	}
	
	public int getTour() {
		return this.tour;
	}

	public int getPoints() {
		return this.POINT_DE_VIE_MAX;
	}

	public int getPointsActuels() {
		return this.pointDeVie;
	}
	
	public int getPortee() {
		return this.PORTEE_VISUELLE;
	}
	
}
