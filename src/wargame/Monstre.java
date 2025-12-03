package wargame;


public class Monstre extends Soldat {
	private final TypesM TYPE;
	private final String NOM;

	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
	}
	
	public TypesM getType() {
		return this.TYPE;
	}
	
	public String getNom() {
		return this.NOM;
	}
}
