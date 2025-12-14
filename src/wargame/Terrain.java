package wargame;
import java.awt.Color;
import java.io.Serializable;

public class Terrain implements IConfig, Serializable {
	
	public enum TypeTerrain {
		HERBE (COULEUR_HERBE, true), // aucun effet (peut prendre feu?)
		ROCHER (COULEUR_ROCHER, false), // inaccessible sauf si vol
		FORET (COULEUR_FORET, true), // invisible pour les ennemis + portee reduite
		EAU (COULEUR_EAU, false), // inaccessible mais peut etre pousse dedans (mort?)
		PONT (COULEUR_PONT, true), // aucun effet
		FEU (COULEUR_FEU, true), // degats en fin de tour
		ACIDE (COULEUR_ACIDE, true), // dot sur x tour si termine dessus
		VILLAGE (COULEUR_VILLAGE, true), // soin si passe son tour dessus
		SABLE (COULEUR_SABLE, true); // reduit la portee de deplacement

		private final Color COULEUR;
		private final boolean ACCESSIBLE; // Changer pour gerer des unité volante par ex?

		TypeTerrain(Color couleur, boolean accessible) { 
			this.COULEUR = couleur;
			this.ACCESSIBLE = accessible;
		}
		
		public Color getCouleur() { return this.COULEUR; }
		public boolean getAccessible() { return this.ACCESSIBLE; }
		
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
	public TypeTerrain getType() { return this.TYPE; }
	public Soldat getOccupant() { return this.occupant; }
	public boolean estLibre() { return (occupant == null) && (this.TYPE.ACCESSIBLE); }
	public void occuper(Soldat soldat) { this.occupant = soldat; }
	public void liberer() { this.occupant = null; }
	
	public void appliquerEffetTerrain() {
		if (this.occupant != null) {
			ListeEffets listeEffetsOccupant = this.occupant.getListeEffets();
			switch(this.TYPE) {
			case FORET : listeEffetsOccupant.setEffetTerrain(new Effet(Effet.TypeEffet.FORET_DENSE)); break;
			case ACIDE : break;
			case SABLE : listeEffetsOccupant.setEffetTerrain(new Effet(Effet.TypeEffet.SABLES_MOUVANTS)); break;
			default: 
				System.out.println("Aucun effet appliqué");
				listeEffetsOccupant.setEffetTerrain(null); 
			}
		}
	}
	
	// Affichage
	public String toString() { 
		return ""+TYPE;
	}
}