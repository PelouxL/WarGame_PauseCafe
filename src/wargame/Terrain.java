package wargame;
import java.awt.Color;
import java.io.Serializable;

public class Terrain implements IConfig, Serializable {
	
	public enum TypeTerrain {
		HERBE (COULEUR_HERBE, true, 1), // aucun effet (peut prendre feu?)
		ROCHER (COULEUR_ROCHER, false, 99999), // inaccessible sauf si vol
		FORET (COULEUR_FORET, true, 3), // invisible pour les ennemis + portee reduite
		EAU (COULEUR_EAU, false, 99999), // inaccessible mais peut etre pousse dedans (mort?)
		PONT (COULEUR_PONT, true, 1), // aucun effet
		FEU (COULEUR_FEU, true, 1), // degats en fin de tour
		// ACIDE (COULEUR_ACIDE, true, 1), // dot sur x tour si termine dessus
		VILLAGE (COULEUR_VILLAGE, true, 2); // soin si passe son tour dessus


		private final Color COULEUR;
		private final boolean ACCESSIBLE; // Changer pour gerer des unité volante par ex?
		private final int COUT;

		TypeTerrain(Color couleur, boolean accessible, int cout) { 
			this.COULEUR = couleur;
			this.ACCESSIBLE = accessible;
			this.COUT = cout;
		}
		
		public Color getCouleur() {
			return this.COULEUR;
		}
		
		public boolean getAccessible() {
			return this.ACCESSIBLE;
		}
		
		public int getCout() {
			return this.COUT;
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