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
		 * HERBE : aucun effet
		 * ROCHER : inaccessible
		 * FORET : portée visuelle réduite
		 * EAU : inaccessible
		 * PONT : aucun effet
		 * FEU : dégâts de fin de tour
		 * ACIDE : applique un poison
		 * VILLAGE : soin de fin de tour
		 * SABLE : distance de déplacement réduite
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
		private final boolean ACCESSIBLE; // Changer pour gérer des unités volantes par ex?
		private final Effet.TypeEffet effet; // Effet appliqué par la case, remplacer par une liste?
		private final TypeMoment moment; // Moment d'application de l'effet
		private final int COUT;

		/**
		 * Construit un type de terrain.
		 *
		 * @param couleur la couleur d'affichage
		 * @param accessible indique si le terrain est franchissable
		 * @param effet l'effet appliqué par le terrain
		 * @param moment le moment d'application de l'effet
		 * @param cout le coût de déplacement
		 */
		TypeTerrain(Color couleur, boolean accessible, Effet.TypeEffet effet, TypeMoment moment, int cout) { 
			this.COULEUR = couleur;
			this.ACCESSIBLE = accessible;
			this.effet = effet;
			this.moment = moment;
			this.COUT = cout;
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
		
		public Color getCouleur() { return this.COULEUR; }
		public boolean getAccessible() { return this.ACCESSIBLE; }
		public Effet.TypeEffet getEffet() { return this.effet; }
		public TypeMoment getMoment() { return this.moment; }
		public int getCout() { return this.COUT; }
	}
	
	private TypeTerrain TYPE;
	private Soldat occupant = null;
	
	/**
	 * Construit un terrain avec un type donné.
	 *
	 * @param type le type de terrain
	 */
	public Terrain(TypeTerrain type) { 
		TYPE = type;
	}
	
	/** 
	 * Indique si une case est libre et accessible.
	 * 
	 * @return vrai si la case est libre et accessible, false sinon */
	public boolean estLibre() { return (occupant == null) && (this.TYPE.ACCESSIBLE); }

	/**
	 * Place un soldat sur la case et applique l'effet associé au terrain
	 * selon son type d'application.
	 *
	 * @param soldat le soldat à placer
	 */
	public void occuper(Soldat soldat) { 
		this.occupant = soldat;
		if (this.TYPE.getMoment() == TypeMoment.TANT_QUE_DESSUS) {
			this.appliquerEffetTerrain();
		}
	}
	
	/**
	 * Libère la case et retire l'effet associé au terrain selon son type d'application.
	 */
	public void liberer() {
		
		// Avant de retirer l'occupant on enleve le bonus/malus associe a la case
		if (this.TYPE.getMoment() == TypeMoment.TANT_QUE_DESSUS && this.occupant != null) {
			ListeEffets listeEffetsOccupant = this.occupant.getListeEffets();
			listeEffetsOccupant.retirerEffet(this.TYPE.getEffet());
		}
		this.occupant = null;
	}
	
	/**
	 * Applique l'effet du terrain au soldat occupant.
	 */
	public void appliquerEffetTerrain() {
		if (this.occupant != null && this.TYPE.getEffet() != null) {
			ListeEffets listeEffetsOccupant = this.occupant.getListeEffets();
			
			// Verification si l'effet a deja ete applique
			int i = listeEffetsOccupant.contient(this.TYPE.getEffet());
			
			// S'il l'effet n'existe pas deja : application de l'effet
			if (i == -1) listeEffetsOccupant.ajouterEffet(new Effet (this.TYPE.getEffet()));
			
			// Sinon on reinitialise la duree restante de l'effet
			else listeEffetsOccupant.getEffet(i).setDureeRestante(this.TYPE.getEffet().getDuree());
		}
	}
	
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
	
	public TypeTerrain getType() { return this.TYPE; }
	public Soldat getOccupant() { return this.occupant; }
}