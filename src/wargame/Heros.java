package wargame;

/**
 * Représente les héros contrôlés par le joueur.
 * <p>
 * Chaque héros possède un nom et un type (défini par {@link TypesH}), ainsi que ses caractéristiques héritées de {@link Soldat}.
 */
public class Heros extends Soldat{
	private final TypesH TYPE;
	private final String NOM;
	
	/**
	 * Crée un nouveau héros avec son type, son nom et sa position sur la carte.
	 *
	 * @param carte la carte sur laquelle le héros est placé
	 * @param type le type du héros, définissant ses caractéristiques
	 * @param nom le nom du héros
	 * @param pos la position initiale du héros sur la carte
	 */
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
}
