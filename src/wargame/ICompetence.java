package wargame;

/**
 * Interface représentant les compétences des personnages.
 * <p>
 * Elle définit les types de compétences et leurs constantes de classe (ATTAQUE, DEBUFF, SOINS, BUFF).
 */
public interface ICompetence{
	
	public enum ClasseCompetence{
		ATTAQUE, DEBUFF, SOINS, BUFF;
	}
	
	/**
	 * Enum représentant les différents types de compétences.
	 * <p>
	 * Chaque compétence possède un nom, une classe (attaque, soin, etc.), un coût en action, des dégâts, 
	 * une zone de lancer, une distance, et d'autres attributs définissant son fonctionnement.
	 */
	public enum TypeCompetence {
		BOULE_DE_FEU("Boule de feu", ClasseCompetence.ATTAQUE, 2, 30, "ligne", 5 ,true, 3, 2),
		SOIN("Soin", ClasseCompetence.SOINS, 1, 15, "libre", 3, false, 0, 1),
		SOIN_DE_ZONE("Soin de zone", ClasseCompetence.SOINS,  2, 20, "ligne", 4, false, 2, 3),
		COUP_EPEE("Coup d'épée", ClasseCompetence.ATTAQUE, 1, 10, "libre", 1, false, 0, 1),
		TIR_A_PORTEE("Tir à portée", ClasseCompetence.ATTAQUE, 1, 10, "ligne", 12, false, 0, 1),
		LANCE_PIERRE("Lance-pierre", ClasseCompetence.DEBUFF, 1, 5, "ligne", 5, false, 0, 0),
		COUP_DE_BATON("Coup de baton", ClasseCompetence.DEBUFF, 1, 5, "ligne", 1, false, 0, 0);

	
		private final String nom;
		private final ClasseCompetence classe;
		private final int coutAction;
		private int degats;
		private final String zoneLancer;
		private final int distance;
		private final boolean donneVisu;
		private final int degatsZone;
		private final int tempsRechargement; 
	
		TypeCompetence(String nom, ClasseCompetence classe, int coutAction, int degats, String zoneLancer, int distance, boolean donneVisu, int degatZone, int tempsRechargement) {
			this.nom = nom;
			this.classe = classe;
			this.coutAction = coutAction;
			this.degats = degats;
			this.zoneLancer = zoneLancer;
			this.distance = distance;
			this.donneVisu = donneVisu;
			this.degatsZone = degatZone;
			this.tempsRechargement = tempsRechargement;
		}
		
	    public String getNom() { return nom; }
	    public ClasseCompetence getClasseCompetence() { return classe; }
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
