package wargame;

import javax.swing.JPanel;

import java.awt.Graphics;

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
	
	private String infoTexte ="";
	private String infoTexte2 ="";
	
	private JPanel panneauCarte;
	private JPanel panneauInfos;
	
	public PanneauJeu(Carte c) {
		this.carte = new Carte();
		setLayout( new BorderLayout());
		
		// ------ creation de la Carte ---- //
		
		panneauCarte = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				dessinableCarte(g);
			}
		};
		
		panneauCarte.setPreferredSize(new Dimension(LARGEUR_CARTE * NB_PIX_CASE, HAUTEUR_CARTE * NB_PIX_CASE));
		
		// ------ creation du panneau d'info ----- //
	    panneauInfos = new JPanel() {
	    	protected void paintComponent(Graphics g) {
	    		super.paintComponent(g);
	    		g.setColor(Color.black);
	    		if(infoTexte2 != "" && !(infoTexte2.equals(infoTexte))) {
	    			g.drawString(infoTexte2,10, 15);
	    			g.drawString(infoTexte,10, 30);
	    		}else {
	    			g.drawString(infoTexte,10, 15);
	    		}
	    	}
	    	
	    };
			 
	    panneauInfos.setPreferredSize(new Dimension(LARGEUR_CARTE, NB_PIX_CASE*4));
		panneauInfos.setBackground(Color.white);
		
		// ------- Mises en place des layout ------//
		add(panneauCarte, BorderLayout.CENTER);
		add(panneauInfos, BorderLayout.SOUTH);
		
		
		// ------- Ajout des ecouteur ---------- //
		panneauCarte.addMouseMotionListener(new MouseMotionAdapter() {
			
			// Effet au deplacement de la souris
			public void mouseMoved(MouseEvent e) {
				int x = e.getX()/NB_PIX_CASE;
				int y = e.getY()/NB_PIX_CASE;
				caseSurvolee = new Position(x, y);
				
				if (caseSurvolee.estValide()) {
					Element elem = carte.getElement(caseSurvolee);
					if(elem instanceof Soldat) {
						infoTexte = elem.toString();
					}else {
						infoTexte ="";
					}
				}else {
					infoTexte ="";
				}
				panneauInfos.repaint();
				panneauCarte.repaint();
			}
		});
		
		// Ecouteur clic souris
		panneauCarte.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX()/NB_PIX_CASE;
				int y = e.getY()/NB_PIX_CASE;
				
				caseCliquee = new Position(x, y);
				Element elem = carte.getElement(caseCliquee);
				// System.out.println(caseCliquee.getX()+","+caseCliquee.getY());
				if (caseCliquee.estValide() && elem instanceof Soldat) {
					infoTexte2 = elem.toString();
				} else {
					caseCliquee = null;
					infoTexte2 ="";
					
				}
				panneauInfos.repaint();
				panneauCarte.repaint();
			}
			
			public void mouseReleased(MouseEvent e) {
			}
		});
		
		setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE, (HAUTEUR_CARTE)*NB_PIX_CASE ));
		
	}
	
	public void dessinableCarte(Graphics g){
		
			
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
		}
		
		// Ajout de la case cliquée
		if (caseCliquee != null
			&& caseCliquee.estValide()
			&& carte.getElement(caseCliquee) instanceof Heros) {
			
			Soldat soldatClic = (Soldat) carte.getElement(caseCliquee);
			
			this.dessineZoneDeplacement(g, soldatClic);
			this.dessineCaseCliquee(g, caseCliquee);
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
	
	
	public void dessineCaseCliquee(Graphics g, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		Color couleur = new Color(0,0,0,100); // Noir mais faible opacité
		g.setColor(couleur);
		// Obligé de faire un +1 quand opacité pas au max ???
		g.fillRect(x*NB_PIX_CASE + 1, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	}
}    
