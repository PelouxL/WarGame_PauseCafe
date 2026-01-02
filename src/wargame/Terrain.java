package wargame;
import java.awt.Color;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Représente une case de terrain de la carte du jeu.
 * <p>
 * Un terrain possède un type, une couleur, une accessibilité,
 * un éventuel effet appliqué au soldat présent, et peut être occupé
 * par un {@link Soldat}.
 * </p>
 */
public class Terrain implements IConfig, Serializable {
	
	/**
	 * Définit le moment auquel un effet de terrain est appliqué.
	 */
	public enum TypeMoment {
		FIN_TOUR,
		DEBUT_TOUR,
		TANT_QUE_DESSUS;
	}
	
	/**
	 * Enumération des différents types de terrain existants dans le jeu.
	 * <p>
	 * Chaque type définit :
	 * <ul>
	 *   <li>une couleur</li>
	 *   <li>son accessibilité</li>
	 *   <li>un effet éventuel</li>
	 *   <li>le moment d'application de cet effet</li>
	 *   <li>un coût de déplacement</li>
	 * </ul>
	 */

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

		/**
		 * Construit un type de terrain.
		 *
		 * @param couleur couleur d'affichage
		 * @param accessible indique si le terrain est franchissable
		 * @param effet effet appliqué par le terrain
		 * @param moment moment d'application de l'effet
		 * @param cout coût de déplacement
		 */
		TypeTerrain(Color couleur, boolean accessible, Effet.TypeEffet effet, TypeMoment moment, int cout) { 
			this.COULEUR = couleur;
			this.ACCESSIBLE = accessible;
			this.effet = effet;
			this.moment = moment;
			this.COUT = cout;
		}
		/** @return la couleur du terrain */
		public Color getCouleur() { return this.COULEUR; }
		/** @return vrai si le terrain est accessible */
		public boolean getAccessible() { return this.ACCESSIBLE; }
		/** @return l'effet associé au terrain */
		public Effet.TypeEffet getEffet() { return this.effet; }
		/** @return le moment d'application de l'effet */
		public TypeMoment getMoment() { return this.moment; }

		/** @return le coût de déplacement */
		public int getCout() {
			return this.COUT;
		}
		
		/**
		 * Retourne un type de terrain aléatoire (hors herbe et pont).
		 *
		 * @return type de terrain aléatoire
		 */
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
	
	/**
	 * Construit un terrain avec un type donné.
	 *
	 * @param type type de terrain
	 */
	public Terrain(TypeTerrain type) { 
		TYPE = type;
	}
	
	// Methodes
	/** @return le type du terrain */
	public TypeTerrain getType() { return this.TYPE; }
	/** @return le soldat occupant la case */
	public Soldat getOccupant() { return this.occupant; }
	/** @return vrai si la case est libre et accessible */
	public boolean estLibre() { return (occupant == null) && (this.TYPE.ACCESSIBLE); }
	
	// GESTION OCCUPANT
	/**
	 * Place un soldat sur la case et applique l'effet du terrain si nécessaire.
	 *
	 * @param soldat soldat à placer
	 */
	public void occuper(Soldat soldat) { 
		this.occupant = soldat;
		
		if (this.TYPE.getMoment() == TypeMoment.TANT_QUE_DESSUS) {
			this.appliquerEffetTerrain();
		}
	}
	
	/**
	 * Libère la case et retire l'effet du terrain si nécessaire.
	 */
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
	
	/**
	 * Crée un effet correspondant au terrain.
	 *
	 * @return effet du terrain
	 */
	public Effet effetTerrain() { return new Effet (this.TYPE.getEffet()); }
	
	/**
	 * Applique l'effet du terrain au soldat occupant.
	 */
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
	
	/**
	 * Représentation textuelle du terrain.
	 *
	 * @return description du terrain
	 */
	public String toString() {
		String s = TYPE+" : ";
		
		if (this.TYPE.getAccessible()) s += "non ";
		s += "accessible \n";
		
		s += "Effet : "+this.TYPE.getEffet().name();
		
		return s;
	}
}