package wargame;

/**
 * Représente un ensemble de positions.
 * <p>
 * Cette classe permet de stocker, ajouter, retirer et parcourir
 * des objets {@link Position}. L'ensemble possède une taille maximale
 * définie à la création.
 */
public class EnsemblePosition {

	private Position[] ePos;
	private final int TAILLE_MAX; 
	private int nbPos = 0;
	
	/**
	 * Construit un ensemble de positions avec une taille maximale donnée.
	 *
	 * @param n le nombre maximal de positions pouvant être stockées
	 */
	public EnsemblePosition(int n) {
		this.ePos = new Position[n];
		this.TAILLE_MAX = n;
	}
	
	/**
	 * Ajoute une position à l'ensemble.
	 *
	 * @param pos la position à ajouter
	 */
	public void ajouterPos(Position pos) {
		this.ePos[nbPos] = pos;
		nbPos++;
	}
	
	/**
	 * Retire la première position de l'ensemble.
	 * Affiche un message d'erreur si l'ensemble est vide.
	 */
	public void retirerPremierePos() {
		if (nbPos < 1) {
			System.out.println("Erreur retirerPremierePos : ensemble vide");
		} else {
			ePos[0] = null;
			for (int j = 0 ; j < nbPos - 1 ; j++) {
				ePos[j] = ePos[j+1];
			}
			ePos[nbPos-1] = null;
			nbPos--;
		}
	}
	
	/**
	 * Retire la dernière position de l'ensemble.
	 * Affiche un message d'erreur si l'ensemble est vide.
	 */
	public void retirerDernierePos() {
		if (nbPos < 1) {
			System.out.println("Erreur retirerDernierePos : ensemble vide");
		} else {
			ePos[nbPos-1] = null;
			nbPos--;
		}
	}
	
	/**
	 * Retire la position située à l'indice donné.
	 * Affiche un message d'erreyr si l'indice est trop grand.
	 *
	 * @param i l'indice de la position à retirer
	 */
	public void retirerPos(int i) {
		if (i >= nbPos) {
			System.out.println("Erreur retirerPos : i >= nbPos");
		} else {
			ePos[i] = null;
			for (int j = i ; j < nbPos - 1 ; j++) {
				ePos[j] = ePos[j+1];
			}
			ePos[nbPos-1] = null;
			nbPos--;
		}
	}
	
	/**
	 * Indique si l'ensemble contient une position donnée.
	 *
	 * @param pos la position recherchée
	 * @return true si la position est présente, false sinon
	 */
	public boolean contient(Position pos) {
	    for (int i = 0 ; i < nbPos ; i++) {
	        if (ePos[i].equals(pos)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Renvoie l'indice de la première position égale à celle donnée.
	 *
	 * @param pos la position recherchée
	 * @return indice de la position ou -1 si absente
	 */
	public int indexPosition(Position pos) {
		for (int i = 0 ; i < nbPos ; i++) {
			if (ePos[i].equals(pos)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Indique si l'ensemble est vide.
	 *
	 * @return true si aucune position n'est stockée, false sinon
	 */
	public boolean estVide() { return nbPos == 0; }
	
	public Position[] getEPos() { return ePos; }	
	public int getTailleMax() { return this.TAILLE_MAX; }
	public int getNbPos() { return this.nbPos; }
	public Position getPosition(int i) { return this.ePos[i]; }
}
