package wargame;

import java.io.Serializable;
import java.util.ArrayList;

public class ListeEffets implements Serializable{
	private ArrayList<Effet> liste;
	
	public ListeEffets() {
		liste = new ArrayList<Effet>();
	}
	
	// ACTIONS SUR LES EFFETS
	public ArrayList<Effet> getListe(){ return liste; }
	public int getTailleListe() { return liste.size(); }
	
	public Effet getEffet(int i) {
		if (i < liste.size()) return liste.get(i);
		else return null;
	}
	
	public void ajouterEffet(Effet e) { liste.add(e); }
	public void retirerEffet(int i) { if (i < liste.size()) liste.remove(i); }
	
	public boolean retirerEffet(Effet.TypeEffet type) {
		int i = this.contient(type);	
		if (i == -1) return false;
		liste.remove(i);
		return true;
	}
	
	public void majEffets() {
		for (int i=0; i < liste.size(); i++) {
			Effet e = liste.get(i);
			e.setDureeRestante(e.getDureeRestante()-1);
			if (e.getDureeRestante() <= 0 && e.getType().getDuree() != -1) retirerEffet(i--);
		}
	}
	// ACTIONS SUR LES EFFETS
	
	public int contient(Effet.TypeEffet type) {
		int i=0;
		for (Effet test : liste) {
			if (test.getType() == type) return i;
			i++;
		}
		return -1;
	}
	
	public int sommeEffets(Effet.TCarAff carAff) {
		int somme = 0;
		
		for (Effet e : liste) {
			if (e.getType().getCarAff() == carAff) {
				somme += e.getType().getValeur();
			}
		}
		
		return somme;
	}
	
	public String toString() {
		String s = "Liste des effets : \n";
		for (Effet e : liste) {
			s += " - "+e.toString();
		}
		s += "\n";
		return s;
	}
}
