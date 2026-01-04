package wargame;

public interface ISoldat {
    static enum TypesH {
      	HUMAIN (90, 6, 20,  5, 3),
      	NAIN  (120, 3, 30,  0, 2),
      	ELF    (50, 8, 10, 10, 2),
      	HOBBIT (30, 7, 10,  2, 5),
      	ANGE   (80, 5, 10,  6, 2);

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
      	TROLL  (150, 2, 40, 0, 2),
      	ORC     (80, 3, 15, 3, 3),
      	GOBELIN (30, 6, 10, 6, 5),
      	DEMON   (50, 4, 30, 5, 4);

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
