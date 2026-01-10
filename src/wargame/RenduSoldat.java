package wargame;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import wargame.RenduCarte;
import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;

/**
 * Classe utilitaire responsable du rendu graphique des soldats sur la carte.
 * <p>
 * Cette classe ne contient que des méthodes statiques et ne doit pas être instanciée.
 * Elle dessine les soldats (héros ou monstres) à leur position actuelle
 * en utilisant les sprites correspondants.
 */
public final class RenduSoldat implements IConfig {
	// corps héros
    private static final Image imgCorpsElf     = new ImageIcon("./images/corps_perso/corps_elf.png").getImage();
    private static final Image imgCorpsHumain  = new ImageIcon("./images/corps_perso/corps_humain.png").getImage();
    private static final Image imgCorpsNain    = new ImageIcon("./images/corps_perso/corps_nain.png").getImage();
    private static final Image imgCorpsHobbit  = new ImageIcon("./images/corps_perso/corps_hobbit.png").getImage();
    private static final Image imgCorpsAnge    = new ImageIcon("./images/corps_perso/corps_ange.png").getImage();
    private static final Image imgCorpsMage    = new ImageIcon("./images/corps_perso/corps_mage.png").getImage();
    
    // corps monstres
    private static final Image imgCorpsTroll   = new ImageIcon("./images/corps_perso/corps_troll.png").getImage();
    private static final Image imgCorpsOrc     = new ImageIcon("./images/corps_perso/corps_orc.png").getImage();
    private static final Image imgCorpsGobelin = new ImageIcon("./images/corps_perso/corps_gobelin.png").getImage();
    private static final Image imgCorpsDemon   = new ImageIcon("./images/corps_perso/corps_demon.png").getImage();
    
    // gif corps héros
    private static final Image gifCorpsElf     = new ImageIcon("./images/gif_perso/gif_elf.gif").getImage();
    private static final Image gifCorpsHumain  = new ImageIcon("./images/gif_perso/gif_humain.gif").getImage();
    private static final Image gifCorpsNain    = new ImageIcon("./images/gif_perso/gif_nain.gif").getImage();
    private static final Image gifCorpsHobbit  = new ImageIcon("./images/gif_perso/gif_hobbit.gif").getImage();
    private static final Image gifCorpsAnge    = new ImageIcon("./images/gif_perso/gif_ange.gif").getImage();
    private static final Image gifCorpsMage    = new ImageIcon("./images/gif_perso/gif_mage.gif").getImage();
    
    // gif corps monstres
    private static final Image gifCorpsTroll   = new ImageIcon("./images/gif_perso/gif_troll.gif").getImage();
    private static final Image gifCorpsOrc     = new ImageIcon("./images/gif_perso/gif_orc.gif").getImage();
    private static final Image gifCorpsGobelin = new ImageIcon("./images/gif_perso/gif_gobelin.gif").getImage();
    private static final Image gifCorpsDemon   = new ImageIcon("./images/gif_perso/gif_demon.gif").getImage();

	/**
	 * Constructeur privé empêchant l'instanciation de la classe.
	 */
    private RenduSoldat() {} 
    
    
    public static Image imageHeros(TypesH type) {
        switch (type) {
            case HUMAIN: return imgCorpsHumain;
            case NAIN:   return imgCorpsNain;
            case ELF:    return imgCorpsElf;
            case HOBBIT: return imgCorpsHobbit;
            case ANGE: 	 return imgCorpsAnge;
            case MAGE:   return imgCorpsMage;
            default:     return null;
        }
    }
    
    public static Image imageMonstre(TypesM type) {
        switch (type) {
            case TROLL:   return imgCorpsTroll;
            case ORC:     return imgCorpsOrc;
            case GOBELIN: return imgCorpsGobelin;
            case DEMON:   return imgCorpsDemon;
            default:      return null;
        }
    }
    
    public static Image gifHeros(TypesH type) {
        switch (type) {
            case HUMAIN: return gifCorpsHumain;
            case NAIN:   return gifCorpsNain;
            case ELF:    return gifCorpsElf;
            case HOBBIT: return gifCorpsHobbit;
            case ANGE: 	 return gifCorpsAnge;
            case MAGE:   return gifCorpsMage;
            default:     return null;
        }
    }
    
    public static Image gifMonstre(TypesM type) {
        switch (type) {
            case TROLL:   return gifCorpsTroll;
            case ORC:     return gifCorpsOrc;
            case GOBELIN: return gifCorpsGobelin;
            case DEMON:   return gifCorpsDemon;
            default:      return null;
        }
    }

    
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
    public static void dessiner(Graphics g, Soldat s, boolean herosClique) {
        Position pos = s.getPos();

        int x = pos.getX();
        int y = pos.getY();
        int offsetX = (y % 2 == 1) ? OFFSET_X : 0;
        int mage = 0; // le mage est un peu plus grand
        
        Image img;
        if (s instanceof Heros) {
        	Heros h = (Heros) s;
        	if (herosClique) { // si le héros est sélectionné, alors on affiche le GIF
        		img = gifHeros(h.getType());
        	} else {
        		img = imageHeros(h.getType());
        	}
        	if (h.getType() == TypesH.MAGE) {
        		mage += HAUTEUR_SOLDAT/6;
        	}
        } else {
        	Monstre m = (Monstre) s;
        	img = imageMonstre(m.getType());
        }

        g.drawImage(img, (x/2) * NB_PIX_CASE + offsetX, y * NB_PIX_CASE * 3/4 - NB_PIX_CASE*3/5 - mage, LARGEUR_SOLDAT, HAUTEUR_SOLDAT+mage, null);
    }
}
