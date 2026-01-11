package wargame;

/**
 * Interface représentant les opérations principales sur une carte de jeu.
 * <p>
 * Fournit les méthodes pour gérer les positions, le déplacement des soldats, les actions des héros et la gestion de la mort des personnages.
 */
public interface ICarte {

    /**
     * Trouve une position vide aléatoire sur la carte.
     *
     * @return une position vide disponible sur la carte
     */
    Position trouvePositionVide(); // Trouve aléatoirement une position vide sur la carte

    /**
     * Trouve une position vide proche d'une position donnée.
     *
     * @param pos la position de référence pour chercher une case vide adjacente
     * @return une position vide à proximité de la position donnée
     */
    Position trouvePositionVide(Position pos); // Trouve une position vide choisie

    /**
     * Déplace un soldat vers une position donnée si le déplacement est possible.
     *
     * @param pos la position cible pour le déplacement
     * @param soldat le soldat à déplacer
     * @return true si le déplacement a été effectué, false sinon
     */
    boolean deplaceSoldat(Position pos, Soldat soldat);

    /**
     * Gère la mort d’un soldat et sa suppression de la carte.
     *
     * @param perso le soldat à retirer
     */
    void mort(Soldat perso);

    /**
     * Effectue l’action d’un héros sur une case cible, comme un déplacement ou un combat.
     *
     * @param pos position de départ du héros
     * @param pos2 position cible pour l’action
     * @return true si l’action a été effectuée, false sinon
     */
    boolean actionHeros(Position pos, Position pos2);

    /**
     * Fait jouer tous les monstres (ou autres soldats contrôlés par l’IA) sur la carte.
     */
    void jouerSoldats();
}
