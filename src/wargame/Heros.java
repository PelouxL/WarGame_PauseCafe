package wargame;

import java.awt.Graphics;

public class Heros extends Soldat{
	private final TypesH TYPE;
	private final String NOM;
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;

	}
	/*
	public void dessinSoldat(Graphics g, Carte c) {
		int x = super.getPos().getX();
		int y = super.getPos().getY();
		int offset_x = 0;
		x = x/2;
		if (y % 2 == 1) {
			offset_x = OFFSET_X;
		}
		
		g.drawImage(imgSpritePersoMage, x*NB_PIX_CASE + offset_x, y*NB_PIX_CASE*3/4 - NB_PIX_CASE*1/4, 20, 20, null);
	}
	*/
	public TypesH getType() {
		return this.TYPE;
	}
	
	public String getNom() {
		return this.NOM;
	}
	
	public String trouverImg() {
		String path = "./images/persos/";
		switch(this.getType()) {
		   case HUMAIN:
			   path += "humain";
			   break;
		   case NAIN:
			   path += "nain";
			   break;
		   case ELF:
			   path += "elfe";
			   break;
		   case HOBBIT:
			   path += "hobbit";
			   break;
		   case ANGE:
			   path += "ange";
			   break;
		   case POUBELLEMALICIEUSE:
			   path += "humain"; // placeholder
			   break;
		}
	    path += "_map.png";
	    return path;
	}
}
