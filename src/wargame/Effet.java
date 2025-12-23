package wargame;

public class Effet {
	
	public final static int INFINI = -1;
	
	public enum TCarAff{ // Types de caracteritique pouvant etre affectee
		PORTEE, DEPLACEMENT, VIE, PUISSANCE, ACTION;
	}
		
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
	
	public Effet(TypeEffet type) {
		TYPE = type;
		dureeRestante = type.getDuree();
	}
	
	public TypeEffet getType() { return this.TYPE; }
	public int getDureeRestante() { return this.dureeRestante; }
	public void setDureeRestante(int dureeRestante) { this.dureeRestante = dureeRestante; }
	
	public String toString() {
		return this.TYPE.getValeur()+" "+this.TYPE.getCarAff()+" pour "+this.getDureeRestante()+" tours\n";
	}
}
