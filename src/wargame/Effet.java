package wargame;

import java.io.Serializable;

/**
 * Représente un effet appliqué à un personnage ou à une case du wargame.
 * <p>
 * Un effet modifie temporairement ou définitivement une caractéristique
 * (vie, déplacement, portée, etc.) pendant une certaine durée.
 */
public class Effet implements Serializable {
	
	public final static int INFINI = -1;
	
	/**
	 * Enumération des caractéristiques pouvant être affectées par un effet.
	 */
	public enum TCarAff { // Types de caracteritique pouvant etre affectee
		PORTEE, DEPLACEMENT, VIE, PUISSANCE, ACTION;
	}
		
	/**
	 * Enumération des différents types d'effets disponibles dans le jeu.
	 * Chaque effet est défini par la caractéristique affectée, la valeur
	 * de l'effet et sa durée.
	 */
	public enum TypeEffet {
		// PORTEE
		FORET_DENSE (TCarAff.PORTEE, -3, INFINI),
		// DEPLACEMENT
		SABLES_MOUVANTS (TCarAff.DEPLACEMENT, -1, INFINI),
		// VIE
		POISON (TCarAff.VIE, -10, 3),
		MALADIE (TCarAff.VIE, -5, INFINI),
		SOUTIEN_POPULAIRE (TCarAff.VIE, 10, 1),
		SOL_BRULANT (TCarAff.VIE, -15, 1),
		// PUISSANCE
		// ACTION
		FATIGUE (TCarAff.ACTION, -1, 1);
		
		private TCarAff carAff; // caracteristique affectee par l'effet
		private int valeur;     // valeur du malus/bonus
		private int duree;      // duree de l'effet, si -1 : duree infinie
		
		/**
		 * Construit un type d'effet.
		 *
		 * @param carAff caractéristique affectée
		 * @param valeur valeur du bonus ou malus
		 * @param duree durée de l'effet
		 */
		TypeEffet(TCarAff carAff, int valeur, int duree){
			this.carAff = carAff;
			this.valeur = valeur;
			this.duree = duree;
		}
		
		/**
		 * @return la caractéristique affectée par l'effet
		 */
		public TCarAff getCarAff() { return carAff; }
		
		/**
		 * @return la valeur du bonus ou malus
		 */
		public int getValeur() { return valeur; }
		
		/**
		 * @return la durée de l'effet
		 */
		public int getDuree() { return duree; }
	}
	
	private final TypeEffet TYPE;
	private int dureeRestante;
	
	/**
	 * Crée un nouvel effet à partir de son type.
	 * La durée restante est initialisée selon le type d'effet.
	 *
	 * @param type type de l'effet
	 */
	public Effet(TypeEffet type) {
		TYPE = type;
		dureeRestante = type.getDuree();
	}
	
	/**
	 * @return le type de l'effet
	 */
	public TypeEffet getType() { return this.TYPE; }
	
	/**
	 * @return la durée restante de l'effet
	 */
	public int getDureeRestante() { return this.dureeRestante; }
	
	/**
	 * Modifie la durée restante de l'effet.
	 *
	 * @param dureeRestante nouvelle durée
	 */
	public void setDureeRestante(int dureeRestante) { this.dureeRestante = dureeRestante; }
	
	/**
	 * Retourne une représentation textuelle de l'effet.
	 *
	 * @return description de l'effet
	 */
	public String toString() {
		return this.TYPE.getValeur()+" "+this.TYPE.getCarAff()+" pour "+this.getDureeRestante()+" tours\n";
	}
}
