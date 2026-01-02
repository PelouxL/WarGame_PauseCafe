package wargame;

/**
 * Interface représentant les compétences des personnages.
 * <p>
 * Elle définit les types de compétences et leurs constantes de classe (ATTAQUE, DEBUFF, SOINS, BUFF).
 */
public interface ICompetence{
	
	public final int ATTAQUE = 0;
	public final int DEBUFF = 1;
	public final int SOINS = 2;
	public final int BUFF = 3;
	
	/**
	 * Enum représentant les différents types de compétences.
	 * <p>
	 * Chaque compétence possède un nom, une classe (attaque, soins, etc.), un coût en action, des dégâts, 
	 * une zone de lancer, une distance, et d'autres attributs définissant son fonctionnement.
	 */
	public enum TypeCompetence {
		BOULE_DE_FEU("boule de feu", ATTAQUE, 2, 30, "ligne", 5 ,true, 3, 2),
		SOIN("soin", SOINS, 1, 15, "libre", 3, false, 0, 1),
		SOIN_DE_ZONE("soin de zone", SOINS,  2, 20, "ligne", 4, false, 2, 3),
		COUP_EPEE("coup d'épée", ATTAQUE, 1, 10, "libre", 1, false, 0, 1),
		TIR_A_PORTER("tir a porter", ATTAQUE, 1, 10, "ligne", 19, false, 0, 1);
		
		private final String nom;
		private final int classeCompetence;
		private final int coutAction;
		private int degats;
		private final String zoneLancer;
		private final int distance;
		private final boolean donneVisu;
		private final int degatsZone;
		private final int tempsRechargement; 
		
		TypeCompetence(String nom, int classeCompetence, int coutAction, int degats, String zoneLancer, int distance, boolean donneVisu, int degatZone, int tempsRechargement) {
			this.nom = nom;
			this.classeCompetence = classeCompetence;
			this.coutAction = coutAction;
			this.degats = degats;
			this.zoneLancer = zoneLancer;
			this.distance = distance;
			this.donneVisu = donneVisu;
			this.degatsZone = degatZone;
			this.tempsRechargement = tempsRechargement;
		}
		
	    public String getNom() { return nom; }
	    public int getClasseCompetence() { return classeCompetence; }
	    public int getCoutAction() { return coutAction; }
	    public int getDegats() { return degats; }
	    public void setDegats(int degats) { this.degats = degats; }
	    public String getZoneLancer() { return zoneLancer; }
	    public int getDistance() { return distance; }
	    public boolean isDonneVisu() { return donneVisu; }
	    public int getDegatsZone() { return degatsZone; }
	    public int getTempsRechargement() { return tempsRechargement; };	
	}
}
