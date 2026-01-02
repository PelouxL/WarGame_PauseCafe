package wargame;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import wargame.RenduCarte;

public final class RenduSoldat implements IConfig {
	private static final Image imgSpritePersoMage = new ImageIcon("./images/sprite_perso/img_sprite_perso_mage.png").getImage();
	private static final Image imgSpritePersoMonstre = new ImageIcon("./images/sprite_perso/img_sprite_perso_monstre.png").getImage();

	
    private RenduSoldat() {} 

    
    
    public static void dessiner(Graphics g, Soldat s) {
        Position pos = s.getPos();

        int x = pos.getX();
        int y = pos.getY();
        int offsetX = (y % 2 == 1) ? OFFSET_X : 0;
        
        Image img;
        if (s instanceof Heros) {
        	Heros h = (Heros) s;
        	img = RenduCarte.imageHeros(h.getType());
        } else {
        	Monstre m = (Monstre) s;
        	img = RenduCarte.imageMonstre(m.getType());
        }

        g.drawImage(img, (x/2) * NB_PIX_CASE + offsetX, y * NB_PIX_CASE * 3/4, 20, 20, null);
    }
}
