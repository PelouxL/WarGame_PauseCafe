package wargame;
import java.awt.Color;
import java.util.ArrayList;
import java.io.Serializable;

public class Terrain implements IConfig, Serializable {
	
	public enum TypeMoment {
		FIN_TOUR,
		DEBUT_TOUR,
		TANT_QUE_DESSUS;
	}
	
	public enum TypeTerrain {
		/*
		 * HERBE : aucun effet (peut prendre feu?)
		 * ROCHER : inaccessible sauf si vol
		 * FORET : invisible pour les ennemis + portee reduite
		 * EAU : inaccessible mais peut etre pousse dedans (mort?)
		 *       OU effet de courant?
		 * PONT : pas d'effet
		 * FEU : degats de fin de tour
		 * ACIDE : applique un poison
		 * VILLAGE : soin de fin de tour + buff de degats pour 1 tour
		 * SABLE : reduction de la portee de deplacement
		 */
		
		HERBE   (COULEUR_HERBE,   true,  null, null, 1),
		ROCHER  (COULEUR_ROCHER,  false, null, null, 9999),
		FORET   (COULEUR_FORET,   true,  Effet.TypeEffet.FORET_DENSE, TypeMoment.TANT_QUE_DESSUS, 1),
		EAU     (COULEUR_EAU,     false, null, null, 9999),
		PONT    (COULEUR_PONT,    true,  null, null, 1),
		FEU     (COULEUR_FEU,     true,  Effet.TypeEffet.SOL_BRULANT, TypeMoment.FIN_TOUR, 1),
		ACIDE   (COULEUR_ACIDE,   true,  Effet.TypeEffet.POISON, TypeMoment.FIN_TOUR, 1),
		VILLAGE (COULEUR_VILLAGE, true,  Effet.TypeEffet.SOUTIEN_POPULAIRE, TypeMoment.FIN_TOUR, 1),
		SABLE   (COULEUR_SABLE,   true,  Effet.TypeEffet.SABLES_MOUVANTS, TypeMoment.TANT_QUE_DESSUS, 2);

		private final Color COULEUR;
		private final boolean ACCESSIBLE; // Changer pour gerer des unité volante par ex?
		private final Effet.TypeEffet effet; // Effet appliqué par la case, remplacer par une liste?
		private final TypeMoment moment; // Moment d'application de l'effet
		private final int COUT;

		TypeTerrain(Color couleur, boolean accessible, Effet.TypeEffet effet, TypeMoment moment, int cout) { 
			this.COULEUR = couleur;
			this.ACCESSIBLE = accessible;
			this.effet = effet;
			this.moment = moment;
			this.COUT = cout;
		}
		
		public Color getCouleur() { return this.COULEUR; }
		public boolean getAccessible() { return this.ACCESSIBLE; }
		public Effet.TypeEffet getEffet() { return this.effet; }
		public TypeMoment getMoment() { return this.moment; }

		
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
	public TypeTerrain getType() { return this.TYPE; }
	public Soldat getOccupant() { return this.occupant; }
	public boolean estLibre() { return (occupant == null) && (this.TYPE.ACCESSIBLE); }
	
	// GESTION OCCUPANT
	public void occuper(Soldat soldat) { 
		this.occupant = soldat;
		
		if (this.TYPE.getMoment() == TypeMoment.TANT_QUE_DESSUS) {
			this.appliquerEffetTerrain();
		}
	}
	
	public void liberer() {
		
		// Avant de retirer l'occupant on enleve le bonus/malus associe a la case
		if (this.TYPE.getMoment() == TypeMoment.TANT_QUE_DESSUS && this.occupant != null) {
			ListeEffets listeEffetsOccupant = this.occupant.getListeEffets();
			listeEffetsOccupant.retirerEffet(this.TYPE.getEffet());
		}
		
		this.occupant = null;
	}
	// GESTION OCCUPANT
	
	
	// EFFET DES TERRAINS
	public Effet effetTerrain() { return new Effet (this.TYPE.getEffet()); }
	
	public void appliquerEffetTerrain() {
		if (this.occupant != null && this.TYPE.getEffet() != null) {
			ListeEffets listeEffetsOccupant = this.occupant.getListeEffets();
			
			// Verification si l'effet a deja ete applique
			int i = listeEffetsOccupant.contient(this.TYPE.getEffet());
			
			// S'il l'effet n'existe pas deja : application de l'effet
			if (i == -1) listeEffetsOccupant.ajouterEffet(effetTerrain());
			
			// Sinon on reinitialise la duree restante de l'effet
			else listeEffetsOccupant.getEffet(i).setDureeRestante(this.TYPE.getEffet().getDuree());
		}
	}
	
	// EFFET DES TERRAINS
	
	
	public String toString() {
		String s = TYPE+" : ";
		
		if (this.TYPE.getAccessible()) s += "non ";
		s += "accessible \n";
		
		s += "Effet : "+this.TYPE.getEffet().name();
		
		return s;
	}
}