package wargame;

import java.util.ArrayList;

public class ListeEffets {
	private Effet effetTerrain = null;
	private ArrayList<Effet> listeEffets;
	
	public ListeEffets() {
		this.listeEffets = new ArrayList<Effet>();
	}
	
	// EFFET TERRAIN
	public Effet getEffetTerrain() { return effetTerrain; }
	public void setEffetTerrain(Effet effetTerrain) { this.effetTerrain = effetTerrain; }
	// EFFET TERRAIN
	
	// AUTRES EFFETS
	public Effet getEffet(int i) {
		if (i < listeEffets.size()) return listeEffets.get(i);
		else return null;
	}
	
	public void ajouterEffet(Effet e) { listeEffets.add(e); }
	public void retirerEffet(int i) { if (i < listeEffets.size()) listeEffets.remove(i); }
	
	public void majEffets() {
		for (int i=0; i < listeEffets.size(); i++) {
			Effet e = listeEffets.get(i);
			e.setDureeRestante(e.getDureeRestante()-1);
			if (e.getDureeRestante() <= 0) retirerEffet(i--);
		}
	}
	// AUTRES EFFETS
	
	public int sommeEffets(int caracAffect) {
		int somme = 0;
		String s = "sommeEffets :"; // DEBUG
		
		if (effetTerrain != null && effetTerrain.getType().getCaracAffect() == caracAffect) {
			somme += effetTerrain.getType().getValeur();
			s += effetTerrain.getType().getValeur()+" + "; // DEBUG
		}
		
		for (Effet e : listeEffets) {
			if (e.getType().getCaracAffect() == caracAffect) {
				somme += e.getType().getValeur();
				s += e.getType().getValeur()+" + "; // DEBUG
			}
		}
		
		System.out.println(s+" = "+somme);
		
		return somme;
	}
	
	public String toString() {
		String s = "Liste des effets : \n";
		
		if (effetTerrain != null) {
			s += " - "+effetTerrain.toString();
		}
		
		for (Effet e : listeEffets) {
			s += " - "+e.toString();
		}
		s += "\n";
		return s;
	}
}
