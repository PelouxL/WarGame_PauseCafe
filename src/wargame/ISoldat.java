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
        HUMAIN (50, 7, 20, 5, 3),
        NAIN (150, 7, 30, 0, 2),
        ELF (50, 10, 5, 20, 5),
        HOBBIT (50, 8, 10, 5, 4),
        ANGE (80, 5, 10, 5, 2),
      //  POUBELLEMALICIEUSE (1, 3, 100, 0, 5),
    	MAGICIEN(50, 6, 0, 0, 5);

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
    }

    /**
     * Enumération des types de monstres disponibles.
     * Chaque type définit ses points de vie, portée visuelle, puissance, tir et déplacement.
     */
    public static enum TypesM {
        TROLL (150, 3, 50, 0, 2),
        ORC (40, 2, 10, 3, 3),
        GOBELIN (30, 5, 10, 5, 5),
        DEMON (50, 4, 30, 15, 4);
    	
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
    }

    // Méthodes que tout soldat doit implémenter

    int getPoints(); 
    int getTour(); 
    int getPortee(); 
    int getDeplacement(); // vraiment utile? elles sont définies dans les enum

    /**
     * Joue le tour du soldat.
     * @param tour numéro du tour en cours
     */
    void joueTour(int tour);

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
