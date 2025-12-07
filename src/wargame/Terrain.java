package wargame;
import java.awt.Color;
import java.io.Serializable;

public class Terrain implements IConfig, Serializable {
	
	public enum TypeTerrain {
		HERBE (COULEUR_HERBE, true),
		ROCHER (COULEUR_ROCHER, false),
		FORET (COULEUR_FORET, false), 
		EAU (COULEUR_EAU, false),
		PONT (COULEUR_PONT, true);
		// FEU (COULEUR_FEU, true),
		// ACIDE (COULEUR_ACIDE, true),
		// VILLAGE (COULEUR_VILLAGE, true);

		private final Color COULEUR;
		private final boolean ACCESSIBLE; // Changer pour gerer des unité volante par ex?

		TypeTerrain(Color couleur, boolean accessible) { 
			this.COULEUR = couleur;
			this.ACCESSIBLE = accessible;
		}
		
		public Color getCouleur() {
			return this.COULEUR;
		}
		
		public boolean getAccessible() {
			return this.ACCESSIBLE;
		}
		
		public static TypeTerrain getTerrainAlea() {
			TypeTerrain type;
			do {
				type = values()[(int)(Math.random()*values().length)];
			} while (type == HERBE || type == PONT);
			return type;
		}
	}
	
	// Declarations
	private TypeTerrain TYPE;
	private Soldat occupant = null;
	
	// Constructeur
	public Terrain(TypeTerrain type) { 
		TYPE = type;
	}
	
	// Methodes
	public TypeTerrain getType() {
		return this.TYPE;
	}
	
	public Soldat getOccupant() {
		return this.occupant;
	}
	
	public boolean estLibre() {
		return (occupant == null) && (this.TYPE.ACCESSIBLE);
	}
	
	public void occuper(Soldat soldat) {
		this.occupant = soldat;
	}
	
	public void liberer() {
		this.occupant = null;
	}		
	
	// Affichage
	public String toString() { 
		return ""+TYPE;
	}
}