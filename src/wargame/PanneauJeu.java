package wargame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

public class PanneauJeu extends JPanel implements IConfig {
	private Carte carte;
	private Position caseSurvolee;
	private Position caseCliquee;
	private Position caseAction;
	
	private boolean deplacePerso = false;
	
	private String infoTexte ="";
	private String infoTexte2 ="";
	
	private JPanel panneauCarte;
	private JPanel panneauInfos;
	private JPanel panneauLog;
	private JTextArea logArea;
	
	public PanneauJeu(Carte c) {
		this.carte = c;
		setLayout( new BorderLayout());
		
		// ------ creation de la Carte ---------- //
		
		panneauCarte = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				dessinableCarte(g);
			}
		};
		
		panneauCarte.setPreferredSize(new Dimension(LARGEUR_CARTE * NB_PIX_CASE, HAUTEUR_CARTE * NB_PIX_CASE));
		
		// ------ creation du panneau log -------- //
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBackground(Color.decode("#8B4513"));
		logArea.setForeground(Color.WHITE);
		logArea.setBorder(BorderFactory.createLineBorder(Color.decode("#663300"), 2));
		
		panneauLog = new JPanel(new BorderLayout());
		panneauLog.add(new JScrollPane(logArea), BorderLayout.CENTER);
		panneauLog.setPreferredSize(new Dimension(300, HAUTEUR_CARTE * NB_PIX_CASE));
		panneauLog.setBackground(Color.decode("#8B4513"));
		
	
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
			 
	    panneauInfos.setPreferredSize(new Dimension(LARGEUR_CARTE, NB_PIX_CASE*5));
		panneauInfos.setBackground(Color.decode("#8B4513"));
		panneauInfos.setBorder(BorderFactory.createLineBorder(Color.decode("#663300"), 2));
		
		// ------- Mises en place des layout ------//
		add(panneauCarte, BorderLayout.CENTER);
		add(panneauInfos, BorderLayout.SOUTH);
		add(panneauLog, BorderLayout.WEST);
		
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
				
				Element elem = carte.getElement(new Position(x,y));
				//petit bémole, quand on fait un combat , on affiche le deplacement et info sur le monstre cliquer donc bizzarre
				// si on est sur le point de deplacé un Heros
				if(deplacePerso) {
					caseAction = new Position(x, y);
					carte.actionHeros(caseCliquee, caseAction);
					if(elem instanceof Monstre) {
						updateCombatLog();
					}
					infoTexte2 ="";
					deplacePerso = false;
					caseCliquee = null;
					caseAction = null;
				// si c'est le premier clique
				}else {
					caseCliquee = new Position(x, y);		
					// System.out.println(caseCliquee.getX()+","+caseCliquee.getY());
					// on initalise le deplacement
					if (caseCliquee.estValide() && elem instanceof Soldat) {
						deplacePerso = true;
						infoTexte2 = elem.toString();
					} else {
						caseCliquee = null;
						deplacePerso = false;
						infoTexte2 ="";
						
					}
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
		
		// Ajout des numeros 
		Element elem = carte.getElement(pos);
		g.setColor(Color.WHITE);
		if(elem instanceof Monstre) {
			g.drawString(""+((Soldat)elem).getNum(),x * NB_PIX_CASE + 4,y * NB_PIX_CASE + 15);
		}else if(elem instanceof Heros) {
			char lettre = (char)('A' + ((Soldat)elem).getNum());
			g.drawString(""+lettre, x * NB_PIX_CASE + 4,y * NB_PIX_CASE + 15);
		}
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
	
	private void updateCombatLog() {
		List<String> log = carte.getCombatLog();
		logArea.setText("");
		for(String s : log) {
			logArea.append(s + "\n");
		}
	}
	
	
}    
