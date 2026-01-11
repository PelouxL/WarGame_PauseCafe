package wargame;

import java.io.Serializable;

/**
 * Représente un effet appliqué à un personnage ou à une case du Wargame.
 * <p>
 * Un effet modifie temporairement ou définitivement une caractéristique
 * (vie, déplacement, portée, etc.).
 * </p>
 */
public class Effet implements Serializable {
	
	public final static int INFINI = -1;
	
	/**
	 * Enumération des caractéristiques pouvant être affectées par un effet.
	 */
	public enum TCarAff { // Types de caractéristique pouvant être affectée
		PORTEE, DEPLACEMENT, VIE, PUISSANCE, ACTION;
	}
		
	/**
	 * Enumération des différents types d'effets disponibles dans le jeu.
	 * Chaque effet est défini par :
	 * <ul>
	 * 		<li>la caractéristique affectée</li>
	 * 		<li>sa valeur</li>
	 * 		<li>sa durée</li>
	 * </ul>
	 */
	public enum TypeEffet {	
		FORET_DENSE (TCarAff.PORTEE, -3, INFINI),
		SABLES_MOUVANTS (TCarAff.DEPLACEMENT, -1, INFINI),
		POISON (TCarAff.VIE, -10, 3),
		MALADIE (TCarAff.VIE, -5, INFINI),
		SOUTIEN_POPULAIRE (TCarAff.VIE, 10, 1),
		SOL_BRULANT (TCarAff.VIE, -15, 1),
		FATIGUE (TCarAff.ACTION, -1, 1);
		
		private TCarAff carAff;
		private int valeur;
		private int duree;
		
		/**
		 * Construit un type d'effet.
		 *
		 * @param carAff la caractéristique affectée
		 * @param valeur la valeur du bonus ou malus
		 * @param duree la durée de l'effet
		 */
		TypeEffet(TCarAff carAff, int valeur, int duree){
			this.carAff = carAff;
			this.valeur = valeur;
			this.duree = duree;
		}
		
		public TCarAff getCarAff() { return carAff; }
		public int getValeur() { return valeur; }
		public int getDuree() { return duree; }
	}
	
	private final TypeEffet TYPE;
	private int dureeRestante;
	
	/**
	 * Crée un nouvel effet à partir de son type.
	 * La durée restante est initialisée selon le type d'effet.
	 *
	 * @param type le type de l'effet
	 */
	public Effet(TypeEffet type) {
		TYPE = type;
		dureeRestante = type.getDuree();
	}
	
	/**
	 * Modifie la durée restante de l'effet.
	 *
	 * @param dureeRestante la nouvelle durée
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
	
	public TypeEffet getType() { return this.TYPE; }
	public int getDureeRestante() { return this.dureeRestante; }
}
