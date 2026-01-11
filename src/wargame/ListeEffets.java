package wargame;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Représente une liste d'effets appliqués à un soldat.
 * <p>
 * Permet de gérer l'ajout, la suppression et la mise à jour des effets, 
 * ainsi que de calculer leur impact cumulé sur certaines caractéristiques.
 */
public class ListeEffets implements Serializable {
	private ArrayList<Effet> liste;

	public ListeEffets() {
		liste = new ArrayList<Effet>();
	}
	
	// ACTIONS SUR LES EFFETS
	
	/**
	 * Retourne la liste des effets.
	 * @return liste des effets
	 */
	public ArrayList<Effet> getListe(){ return liste; }

	/**
	 * Retourne le nombre d'effets dans la liste.
	 * @return taille de la liste
	 */
	public int getTailleListe() { return liste.size(); }

	/**
	 * Retourne l'effet à l'indice donné.
	 * @param i l'indice de l'effet
	 * @return l'effet ou null si l'indice est hors limites
	 */
	public Effet getEffet(int i) {
		if (i < liste.size()) return liste.get(i);
		else return null;
	}

	/**
	 * Ajoute un effet à la liste.
	 * @param e l'effet à ajouter
	 */
	public void ajouterEffet(Effet e) { liste.add(e); }

	/**
	 * Supprime l'effet à l'indice donné.
	 * @param i l'indice de l'effet à supprimer
	 */
	public void retirerEffet(int i) { if (i < liste.size()) liste.remove(i); }

	/**
	 * Supprime le premier effet correspondant au type donné.
	 * @param type le type d'effet à supprimer
	 * @return true si un effet a été supprimé, false sinon
	 */
	public boolean retirerEffet(Effet.TypeEffet type) {
		int i = this.contient(type);	
		if (i == -1) return false;
		liste.remove(i);
		return true;
	}

	/**
	 * Met à jour tous les effets : décrémente leur durée restante et
	 * retire ceux dont la durée est écoulée (sauf les effets infinis).
	 */
	public void majEffets() {
		for (int i=0; i < liste.size(); i++) {
			Effet e = liste.get(i);
			e.setDureeRestante(e.getDureeRestante()-1);
			if (e.getDureeRestante() <= 0 && e.getType().getDuree() != -1) retirerEffet(i--);
		}
	}
	
	// ACTIONS SUR LES EFFETS

	/**
	 * Vérifie si un effet du type donné est présent dans la liste.
	 * @param type le type d'effet recherché
	 * @return indice du premier effet trouvé, ou -1 si absent
	 */
	public int contient(Effet.TypeEffet type) {
		int i=0;
		for (Effet test : liste) {
			if (test.getType() == type) return i;
			i++;
		}
		return -1;
	}

	/**
	 * Calcule la somme des valeurs de tous les effets affectant une caractéristique donnée.
	 * @param carAff la caractéristique concernée
	 * @return somme des effets
	 */
	public int sommeEffets(Effet.TCarAff carAff) {
		int somme = 0;
		for (Effet e : liste) {
			if (e.getType().getCarAff() == carAff) {
				somme += e.getType().getValeur();
			}
		}
		return somme;
	}

	/**
	 * Représentation textuelle de la liste des effets.
	 * @return chaîne décrivant les effets présents
	 */
	public String toString() {
		String s = "Liste des effets : \n";
		for (Effet e : liste) {
			s += " - "+e.toString();
		}
		s += "\n";
		return s;
	}
}
