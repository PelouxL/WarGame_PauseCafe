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
