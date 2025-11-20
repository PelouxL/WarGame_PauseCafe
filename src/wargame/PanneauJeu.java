package wargame;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Graphics;

import wargame.obstacle.Obstacle;
import wargame.obstacle.Obstacle.TypeObstacle;
import wargame.position.Position;
import wargame.soldat.Heros;
import wargame.soldat.Monstre;
import wargame.soldat.ISoldat.TypesH;
import wargame.soldat.ISoldat.TypesM;

import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

public class PanneauJeu extends JPanel implements IConfig {
	private Carte carte;
	
	
	public PanneauJeu(Carte c) {
		this.carte = new Carte();
		setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE, HAUTEUR_CARTE*NB_PIX_CASE ));
		
	}
	
	public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			for(int i = 0; i < LARGEUR_CARTE; i++) {
				for(int j = 0; j < HAUTEUR_CARTE; j++) {
					
					Element e = carte.getElement(new Position(i,j));
					if(e instanceof Heros) {
						g.setColor(COULEUR_HEROS);
					}else if(e instanceof Monstre) {
						g.setColor(COULEUR_MONSTRES);
					}else if(e instanceof Obstacle) {
						switch(((Obstacle)e).getType()) {
						case ROCHER:
							g.setColor(COULEUR_ROCHER);
							break;
						case EAU:
							g.setColor(COULEUR_EAU);
							break;
						case FORET:
							g.setColor(COULEUR_FORET);
							break;
						}
					}else {
						g.setColor(COULEUR_VIDE);
						
					}
					// /!\ IMPORTANT POUR LES TESTS /!\
					// Décommenter le && en-dessous si on veut tester la carte en voyant tout
					if (carte.getVisibilite(new Position(i,j)) == 0 /*&& carte.getVisibilite(new Position(i,j)) == 1*/) {
						g.setColor(COULEUR_INCONNU);
					}
					g.fillRect(i*NB_PIX_CASE, j*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
					
					// Bord de la case
					g.setColor(Color.BLACK);
					g.drawRect(i*NB_PIX_CASE, j*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
				}
			}

	}
	
		
		
}
	      

