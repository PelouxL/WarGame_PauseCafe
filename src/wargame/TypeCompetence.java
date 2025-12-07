package wargame;

public enum TypeCompetence {
	BOULE_DE_FEU("boule de feu", 2, 30, 10, true, 3, 2),
	SOIN("soin", 1, -15, 4, false, 1, 0),
	SOIN_DE_ZONE("soin de zone", 2, -20, 6, false, 4, 3),
	COUP_EPEE("coup d'épée", 1, 10, 1, false, 1, 0),
	TIR_A_PORTER("tir a porter", 1, 10, 10, false, 1, 0);
	
	private final String nom;
	private final int coutAction;
	private int degats;
	private final int distance;
	private final boolean donneVisu;
	private final int degatsZone;
	private final int tempsRechargement; 
	
	TypeCompetence(String nom, int coutAction, int degats, int distance, boolean donneVisu, int degatZone, int tempsRechargement) {
		this.nom = nom;
		this.coutAction = coutAction;
		this.degats = degats;
		this.distance = distance;
		this.donneVisu = donneVisu;
		this.degatsZone = degatZone;
		this.tempsRechargement = tempsRechargement;
	}
	
    public String getNom() { return nom; }
    public int getCoutAction() { return coutAction; }
    public int getDegats() { return degats; }
    public void setDegats(int degats) { this.degats = degats; }
    public int getDistance() { return distance; }
    public boolean isDonneVisu() { return donneVisu; }
    public int getDegatsZone() { return degatsZone; }
    public int getTempsRechargement() { return tempsRechargement; };	
}
