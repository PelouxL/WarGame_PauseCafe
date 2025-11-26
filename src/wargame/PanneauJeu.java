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
import wargame.position.*;
import wargame.soldat.Heros;
import wargame.soldat.Monstre;
import wargame.soldat.ISoldat.TypesH;
import wargame.soldat.ISoldat.TypesM;
import wargame.soldat.*;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class PanneauJeu extends JPanel implements IConfig {
	private Carte carte;
	private Position caseSurvolee;
	private Position caseCliquee;
	
	public PanneauJeu(Carte c) {
		this.carte = new Carte();
		
		// Ajout ecouteur
		this.addMouseMotionListener(new MouseMotionAdapter() {
			
			// Effet au deplacement de la souris
			public void mouseMoved(MouseEvent e) {
				int x = e.getX()/NB_PIX_CASE;
				int y = e.getY()/NB_PIX_CASE;
				caseSurvolee = new Position(x, y);
				// System.out.println(caseSurvolee.getX()+","+caseSurvolee.getY());
				if (caseSurvolee.estValide()) {
					// PanneauInfo pi = new PanneauInfo();
					// pi.setCaseSurvoleePI(caseSurvolee);
					// add(pi, BorderLayout.SOUTH);
					//repaint();
				}
				repaint();
			}
		});
		
		// Ecouteur clic souris
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX()/NB_PIX_CASE;
				int y = e.getY()/NB_PIX_CASE;
				Element elem;
				caseCliquee = new Position(x, y);
				elem = carte.getElement(caseCliquee);
				// System.out.println(caseCliquee.getX()+","+caseCliquee.getY());
				if (caseCliquee.estValide() && elem instanceof Heros) {
					//repaint();
				} else {
					caseCliquee = null;
					//repaint();
				}
				repaint();
			}
			
			public void mouseReleased(MouseEvent e) {
			}
		});
		
		setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE, (HAUTEUR_CARTE+1)*NB_PIX_CASE ));
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
			
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				
				Position pos = new Position(i, j);
				Element e = carte.getElement(pos);
				Color couleur = COULEUR_VIDE;
				
				if(e instanceof Heros) {
					couleur = COULEUR_HEROS;
				}else if(e instanceof Monstre) {
					couleur = COULEUR_MONSTRES;
				}else if(e instanceof Obstacle) {
					switch(((Obstacle)e).getType()) {
					case ROCHER:
						couleur = COULEUR_ROCHER;
						break;
					case EAU:
						couleur = COULEUR_EAU;
						break;
					case FORET:
						couleur = COULEUR_FORET;
						break;
					}
				}
				
				// /!\ IMPORTANT POUR LES TESTS /!\
				// Décommenter le && en-dessous si on veut tester la carte en voyant tout
				if (carte.getVisibilite(pos) == 0
					&& carte.getVisibilite(pos) == 1
					) {
					couleur = COULEUR_INCONNU;
				}
				
				dessineCase(g, couleur, pos);
				
			}
		}
		
		// Ajout de la zone des deplacements de la case survolee si cest un soldat
		if (caseSurvolee != null 
			&& caseCliquee == null
			&& caseSurvolee.estValide() 
			&& carte.getElement(caseSurvolee) instanceof Soldat) {
			
			// System.out.println("Coor x : "+x+", y : "+y);
			Soldat soldat = (Soldat)carte.getElement(caseSurvolee);
			
			this.dessineZoneDeplacement(g, soldat);
			this.afficheInfos(g, soldat);
		}
		
		// Ajout de la case cliquée
		if (caseCliquee != null
			&& caseCliquee.estValide()
			&& carte.getElement(caseCliquee) instanceof Heros) {
			
			Soldat soldatClic = (Soldat) carte.getElement(caseCliquee);
			
			this.dessineZoneDeplacement(g, soldatClic);
			this.dessineCaseCliquee(g, caseCliquee);
			this.afficheInfos(g, soldatClic);
		}
	}
	
	// Accesseur
	public Position getCaseSurvolee() {
		return this.caseSurvolee;
	}	
	
	// Fonctions graphiques
	public void dessineCase(Graphics g, Color couleur, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		
		g.setColor(couleur);
		g.fillRect(x*NB_PIX_CASE, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
		g.setColor(Color.BLACK);
		g.drawRect(x*NB_PIX_CASE, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	}
	
	public void dessineZoneDeplacement(Graphics g, Soldat soldat) {
		EnsemblePosition ePos = soldat.zoneDeplacement();
		
		for (int i = 0; i < ePos.getNbPos(); i++) {
			this.dessineCase(g, Color.PINK, ePos.getPosition(i));
		}
	}
	
	public void afficheInfos(Graphics g, Soldat soldat) {
		String s = "(" + soldat.getClass().getSimpleName() + ") " + soldat.getPos() + " " + soldat.getPointsActuels() + "/" + soldat.getPoints();
		g.drawString(s, 5, (HAUTEUR_CARTE+1)*NB_PIX_CASE-5);
	}
	
	public void dessineCaseCliquee(Graphics g, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		Color couleur = new Color(0,0,0,100); // Noir mais faible opacité
		g.setColor(couleur);
		// Obligé de faire un +1 quand opacité pas au max ???
		g.fillRect(x*NB_PIX_CASE + 1, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	}
}    
