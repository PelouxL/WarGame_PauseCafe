package wargame;

/**
 * Représente les héros contrôlés par le joueur.
 * <p>
 * Chaque héros possède un nom et un type (défini par {@link TypesH}), ainsi que ses caractéristiques héritées de {@link Soldat}.
 * </p>
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
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), type.getDeplacement(), pos);
		NOM = nom;
		TYPE = type;	
		initialiserCompetence();
	}
	
	/**
	 * Initialise les compétences du héros selon son type (HOBBIT, ELF...).
	 */
	public void initialiserCompetence() {
		switch(TYPE) {
		case HOBBIT:
			ajouterCompetence(new Competence(TypeCompetence.LANCE_PIERRE));
			break;
		case ELF:
			ajouterCompetence(new Competence(TypeCompetence.TIR_A_PORTER));
			break;
		case HUMAIN:
			ajouterCompetence(new Competence(TypeCompetence.COUP_EPEE));
			ajouterCompetence(new Competence(TypeCompetence.TIR_A_PORTER));
			break;
		case ANGE:
			ajouterCompetence(new Competence(TypeCompetence.SOIN));
			ajouterCompetence(new Competence(TypeCompetence.SOIN_DE_ZONE));
			break;
		case NAIN:
			ajouterCompetence(new Competence(TypeCompetence.COUP_EPEE));
			break;
		case MAGE:
			ajouterCompetence(new Competence(TypeCompetence.BOULE_DE_FEU));
			ajouterCompetence(new Competence(TypeCompetence.COUP_DE_BATON));
		default:
			break;
		}
	
	}
	
	public TypesH getType() { return this.TYPE; }
	public String getNom() { return this.NOM; }
}
