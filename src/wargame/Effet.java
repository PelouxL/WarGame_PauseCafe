package wargame;

public class Effet {
	
	public enum TCarAff{ // Types de caracteritique pouvant etre affectee
		PORTEE, DEPLACEMENT, VIE, PUISSANCE_ATQ, PUISSANCE_SOIN, ACTION;
	}
		
	public enum TypeEffet {
		FORET_DENSE (TCarAff.PORTEE, -5, -1),
		SABLES_MOUVANTS (TCarAff.DEPLACEMENT, -1, -1),
		POISON (TCarAff.VIE, -5, 3),
		SOUTIEN_POPULAIRE (TCarAff.PUISSANCE_ATQ, 10, 1);
		
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
