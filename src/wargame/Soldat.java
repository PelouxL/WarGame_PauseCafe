package wargame;

public abstract class Soldat implements ISoldat{
	private final int POINT_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
	private int pointDeVie;
	private Position pos;
	private Carte carte;
	
	Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINT_DE_VIE_MAX = pointDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
	}
	
	
	
}
