package wargame;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Classe utilitaire responsable du rendu graphique des soldats sur la carte.
 * <p>
 * Cette classe ne contient que des méthodes statiques et ne doit pas être instanciée.
 * Elle dessine les soldats (héros ou monstres) à leur position actuelle
 * en utilisant les sprites correspondants.
 */
public final class RenduSoldat implements IConfig {
	private static final Image imgSpritePersoMage = new ImageIcon("./images/sprite_perso/img_sprite_perso_mage.png").getImage();
	private static final Image imgSpritePersoMonstre = new ImageIcon("./images/sprite_perso/img_sprite_perso_monstre.png").getImage();

	/**
	 * Constructeur privé empêchant l'instanciation de la classe.
	 */
    private RenduSoldat() {} 

    
    /**
     * Dessine un soldat sur la carte à sa position actuelle.
     * <p>
     * Le sprite utilisé dépend du type du soldat :
     * <ul>
     *   <li>Héros : sprite de mage</li>
     *   <li>Monstre : sprite de monstre</li>
     * </ul>
     *
     * @param g contexte graphique utilisé pour le dessin
     * @param s soldat à dessiner
     */
    public static void dessiner(Graphics g, Soldat s) {
        Position pos = s.getPos();

        int x = pos.getX();
        int y = pos.getY();
        int offsetX = (y % 2 == 1) ? OFFSET_X : 0;

        Image img = (s instanceof Heros)
            ? imgSpritePersoMage
            : imgSpritePersoMonstre;

        g.drawImage(img, (x / 2) * NB_PIX_CASE + offsetX, y * NB_PIX_CASE * 3 / 4 - NB_PIX_CASE / 4, 20, 20, null);
    }
}
