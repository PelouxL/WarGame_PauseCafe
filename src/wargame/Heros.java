package wargame;


public class Heros extends Soldat{
	private final TypesH TYPE;
	private final String NOM;
	
	public Heros(Carte carte, TypesH type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;

	}
	
	public TypesH getType() {
		return this.TYPE;
	}
	
	public String getNom() {
		return this.NOM;
	}
	
	public String toStrin() { // si je l'appel toString ca remplace lors de l'affichage dans le panneau info
		
		String classe = this.getClass().getSimpleName();
		char num = (char)('A' + this.getNum());
		
		return "("+classe+" "+num+" "+this.TYPE+") "+this.NOM;
	}
}
