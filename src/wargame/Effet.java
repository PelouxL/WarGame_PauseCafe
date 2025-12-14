package wargame;

public class Effet {
		
	public enum TypeEffet {
		FORET_DENSE (IConfig.PORTEE, -5, 3),
		SABLES_MOUVANTS (IConfig.DEPLACEMENT, -1, 3),
		POISON (IConfig.VIE, -5, 3);
		
		private int caracAffect; // caracteristique affectee par l'effet
		private int valeur;      // valeur du malus/bonus
		private int duree;       // duree de l'effet
		
		TypeEffet(int caracAffect, int valeur, int duree){
			this.caracAffect = caracAffect;
			this.valeur = valeur;
			this.duree = duree;
		}
		
		public int getCaracAffect() { return caracAffect; }
		public int getValeur() { return valeur; }
		public int getDuree() { return duree; }
	}
	
	private TypeEffet TYPE;
	private int dureeRestante;
	
	public Effet(TypeEffet type) {
		TYPE = type;
		dureeRestante = type.getDuree();
	}
	
	public TypeEffet getType() { return this.TYPE; }
	public int getDureeRestante() { return this.dureeRestante; }
	public void setDureeRestante(int dureeRestante) { this.dureeRestante = dureeRestante; }
	
	public String toString() {
		return this.TYPE.getValeur()+" "+this.TYPE.caracAffect+" pour "+this.getDureeRestante()+" tours\n";
	}
}
