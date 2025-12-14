package wargame;

public class ListeEffets {
	private Effet effetTerrain;
	private Effet[] listeEffets;
	private final int NB_EFFETS_MAX = 10;
	private int nbEffets = 0;
	
	public ListeEffets() {
		this.listeEffets = new Effet[NB_EFFETS_MAX];
	}
	
	// EFFET TERRAIN
	public Effet getEffetTerrain() { return effetTerrain; }
	public void setEffetTerrain(Effet effetTerrain) { this.effetTerrain = effetTerrain; }
	// EFFET TERRAIN
	
	// AUTRES EFFETS
	public Effet getEffet(int i) { return listeEffets[i]; }
	public void ajouterEffet(Effet e) { this.listeEffets[nbEffets++] = e; }
	
	public void retirerEffet(int i) {
		for (int j=i; j < nbEffets-1; j++) {
			this.listeEffets[j] = this.listeEffets[j+1];
		}
		nbEffets--;
	}
	
	public void MajEffets() {
		for (int i=0; i < nbEffets; i++) {
			Effet e = listeEffets[i];
			e.setDureeRestante(e.getDureeRestante()-1);
			if (e.getDureeRestante() <= 0) retirerEffet(i);
		}
	}
	// AUTRES EFFETS
	
	public int sommeEffets(int caracAffect) {
		int somme = 0;
		String s = "sommeEffets :";
		
		if (effetTerrain != null && effetTerrain.getType().getCaracAffect() == caracAffect) {
			somme += effetTerrain.getType().getValeur();
			s += effetTerrain.getType().getValeur()+" + ";
		}
		
		for (int i=0; i < nbEffets; i++) {
			Effet effet = listeEffets[i];
			if (effet.getType().getCaracAffect() == caracAffect) {
				somme += effet.getType().getValeur();
				s += effet.getType().getValeur()+" + ";
			}
		}
		
		System.out.println(s+" = "+somme);
		
		return somme;
	}
	
	public String toString() {
		String s = "Liste des effets : \n";
		s += " - "+effetTerrain.toString();
		for (int i=0; i < nbEffets; i++) {
			s += " - "+listeEffets[i].toString();
		}
		s += "\n";
		return s;
	}
}
