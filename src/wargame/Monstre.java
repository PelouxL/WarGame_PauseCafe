package wargame;

import wargame.ICompetence.TypeCompetence;

/**
 * Représente les monstres contrôlés par l'ordinateur.
 * <p>
 * Chaque monstre possède un nom et un type (défini par {@link TypesM}), ainsi que ses caractéristiques héritées de {@link Soldat}.
 * </p>
 */
public class Monstre extends Soldat {
	private final TypesM TYPE;
	private final String NOM;

	/**
	 * Crée un nouveau monstre avec son type, son nom et sa position sur la carte.
	 *
	 * @param carte la carte sur laquelle le monstre est placé
	 * @param type le type du monstre, définissant ses caractéristiques
	 * @param nom le nom du monstre
	 * @param pos la position initiale du monstre sur la carte
	 */
	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), type.getDeplacement(), pos);
		NOM = nom;
		TYPE = type;
		initialiserCompetence();
	}
	
	/**
	 * Initialise les compétences du monstre selon son type (TROLL, ORC...).
	 */
	public void initialiserCompetence() {
		ajouterCompetence(new Competence(TypeCompetence.COUP_EPEE));
	}
	
	public TypesM getType() { return this.TYPE; }
	public String getNom() { return this.NOM; }
}
