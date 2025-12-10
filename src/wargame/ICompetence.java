package wargame;

public interface ICompetence{
	
	public final int ATTAQUE = 0;
	public final int DEBUFF = 1;
	public final int SOINS = 2;
	public final int BUFF = 3;
	
	public enum TypeCompetence {
		BOULE_DE_FEU("boule de feu", ATTAQUE, 2, 30, "ligne", 5 ,true, 3, 2),
		SOIN("soin", SOINS, 1, 15, "ligne", 3, false, 4, 0),
		SOIN_DE_ZONE("soin de zone", SOINS,  2, 20, "libre", 4, false, 2, 3),
		COUP_EPEE("coup d'épée", ATTAQUE, 1, 10, "libre", 1, false, 0, 0),
		TIR_A_PORTER("tir a porter", ATTAQUE, 1, 10, "ligne", 19, false, 0, 0);
		
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
