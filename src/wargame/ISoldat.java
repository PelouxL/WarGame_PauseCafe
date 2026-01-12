package wargame;

/**
 * Interface représentant les caractéristiques et comportements d'un soldat.
 * <p>
 * Contient les définitions des types de héros et de monstres, ainsi que les
 * signatures des méthodes que tout soldat doit implémenter, telles que :
 * - jouer son tour,
 * - combattre un autre soldat,
 * - déterminer sa zone de déplacement,
 * - se déplacer vers une nouvelle position.
 */
public interface ISoldat {

    /**
     * Enumération des types de héros disponibles.
     * Chaque type définit ses points de vie, portée visuelle, puissance, tir et déplacement.
     */
    static enum TypesH {
      	HUMAIN  (90, 6, 20,  5, 3),
      	NAIN   (120, 3, 30,  0, 2),
      	ELF     (50, 8, 10, 10, 2),
      	HOBBIT  (30, 7, 10,  2, 5),
      	ANGE    (80, 5, 10,  6, 2),
        MAGE    (50, 6,  0,  0, 3);

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

        /**
         * Retourne un type de héros aléatoire.
         * @return un TypesH choisi aléatoirement
         */
        public static TypesH getTypeHAlea() {
            return values()[(int)(Math.random() * values().length)];
        }
        
        /**
         * Retourne un type de héros à l'indice i.
         * @param i l'indice du type de héros voulu
         * @return le TypesH correspondant à i
         */
        public static TypesH getTypeHIndice(int i) {
        	return values()[i];
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

        /**
         * Retourne un type de monstre aléatoire.
         * @return un TypesM choisi aléatoirement
         */
        public static TypesM getTypeMAlea() {
            return values()[(int)(Math.random() * values().length)];
        }
        
        /**
         * Retourne un type de monstre à l'indice i.
         * @param i l'indice du type de monstre voulu
         * @return le TypesM correspondant à i
         */
        public static TypesM getTypeMIndice(int i) {
        	return values()[i];
        }
    }

    // Méthodes que tout soldat doit implémenter

    int getPoints();
    int getPortee(); 
    int getDeplacement(); // vraiment utile? elles sont définies dans les enum

    /**
     * Effectue un combat contre un autre soldat.
     * @param soldat l'autre soldat
     * @return true si le combat est remporté, false sinon
     */
    boolean combat(Soldat soldat);

    /**
     * Retourne la zone de déplacement possible du soldat.
     * @return ensemble des positions accessibles
     */
    EnsemblePosition zoneDeplacement();

    /**
     * Déplace le soldat vers une nouvelle position.
     * @param newPos la nouvelle position
     */
    void seDeplace(Position newPos);
}
