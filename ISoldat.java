package wargame;

public interface ISoldat {
    static enum TypesH {
      	HUMAIN (50, 7, 20, 5, 3),
      	NAIN (150, 7, 30, 0, 2),
      	ELF (30, 10, 5, 10, 3),
      	HOBBIT (30, 8, 10, 5, 4),
      	ANGE (80, 5, 30, 15, 2),
      	POUBELLEMALICIEUSE (1, 3, 100, 0, 5);

      	private final int POINTS_DE_VIE, PORTEE_VISUELLE, PUISSANCE, TIR, DEPLACEMENT;

      	TypesH(int points, int portee, int puissance, int tir, int deplacement) {
    	  	POINTS_DE_VIE = points;
    	  	PORTEE_VISUELLE = portee;
    	  	PUISSANCE = puissance;
    	  	TIR = tir;
    	  	DEPLACEMENT = deplacement;
      	}

		public int getPoints() { return POINTS_DE_VIE; }
		public int getPortee() { return PORTEE_VISUELLE; }
		public int getPuissance() { return PUISSANCE; }
		public int getTir() { return TIR; }
		public int getDeplacement() { return DEPLACEMENT; }

		public static TypesH getTypeHAlea() {
         	return values()[(int)(Math.random()*values().length)];
      	}
   	}

   	public static enum TypesM {
      	TROLL (150, 3, 50, 0, 2),
      	ORC (40, 2, 10, 3, 3),
      	GOBELIN (30, 5, 10, 5, 5),
      	DEMON (50, 4, 30, 15, 4),
      	MAITREDUCAFE (300, 1, 100, 0, 2);

      	private final int POINTS_DE_VIE, PORTEE_VISUELLE, PUISSANCE, TIR, DEPLACEMENT;
      	
      	TypesM(int points, int portee, int puissance, int tir, int deplacement) {
    	 	POINTS_DE_VIE = points;
    	 	PORTEE_VISUELLE = portee;
    	  	PUISSANCE = puissance;
    	  	TIR = tir;
    	  	DEPLACEMENT = deplacement;
      	}

		public int getPoints() { return POINTS_DE_VIE; }
		public int getPortee() { return PORTEE_VISUELLE; }
		public int getPuissance() { return PUISSANCE; }
		public int getTir() { return TIR; }
		public int getDeplacement() { return DEPLACEMENT; }

		public static TypesM getTypeMAlea() {
         	return values()[(int)(Math.random()*values().length)];
      	}
   	}
	
	int getPoints(); int getTour(); int getPortee(); int getDeplacement(); // vraiment utile? elles sont definies dans les enum
	void joueTour(int tour);
	boolean combat(Soldat soldat);
	EnsemblePosition zoneDeplacement();
	void seDeplace(Position newPos);

}
