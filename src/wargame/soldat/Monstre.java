package wargame.soldat;

import wargame.Carte;
import wargame.position.Position;
import wargame.soldat.ISoldat.TypesM;

public class Monstre extends Soldat {
	private final TypesM TYPE;
	private final String NOM;
	private Carte carte;
	private Position pos;
	private boolean joue;

	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
	}

	public void setJoue() {
		if (this.getTour() == 0) {
			this.joue = true;
		} else {
			this.joue = false;
		}
	}

	public boolean getJoue() {
		return this.joue;
	}
}
