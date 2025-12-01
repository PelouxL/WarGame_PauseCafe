package wargame;


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
}
