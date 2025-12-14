package wargame;

public class ListeEffets {
	private int[][] listeEffets;
	private final int NB_EFFETS_MAX = 10;
	private int nbEffets = 0;
	
	private int PORTEE = 0;
	private int VIE = 1;
	private int DEPLACEMENT = 2;
	
	public ListeEffets() {
		this.listeEffets = new int[NB_EFFETS_MAX][3];
	}
	
	// EFFET
	public int getType(int i) { return listeEffets[i][0]; }
	public void setType(int i, int type) { listeEffets[i][0] = type; }
	
	public int getValeur(int i) { return listeEffets[i][1]; }
	public void setValeur(int i, int valeur) { listeEffets[i][1] = valeur; }
	
	public int getNbTours(int i) { return listeEffets[i][2]; }
	public void setNbTours(int i, int nbTours) { listeEffets[i][2] = nbTours; }
	// EFFET
	
	// LISTEEFFETS
	public int[] getEffet(int i) {
		return listeEffets[i];
	}
	
	public void setEffet(int i, int type, int valeur, int nbTours) {
		setType(i, type);
		setValeur(i, valeur);
		setNbTours(i, nbTours);
	}
	
	public void ajouterEffet(int type, int valeur, int nbTours) {
		setType(nbEffets, type);
		setValeur(nbEffets, valeur);
		setNbTours(nbEffets, nbTours);
	}
	
	public void retirerEffet(int i) {
		for (int j=i; j < nbEffets-1; j++) {
			setEffet(j, getType(j+1), getValeur(j+1), getNbTours(j+1));
		}
		nbEffets--;
	}
	
	public void MajEffets() {
		for (int i=0; i < nbEffets; i++) {
			setNbTours(i, getNbTours(i)-1);
		}
	}
	
	public String toString() {
		String s = "Liste des effets : \n";
		for (int i=0; i < nbEffets; i++) {
			s += getValeur(i)+" "+getType(i)+" pendant "+getNbTours(i)+" tours\n";
		}
		s += "\n";
		return s;
	}
	// LISTEEFFETS
}
