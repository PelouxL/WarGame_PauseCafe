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
	private Position dragPersoFin = null;
	private Position dragPersoInit = null;
	
	// action et deplacement
	private boolean deplacePerso = false;
	private boolean dragPerso = false;
	
	// information du panneauInfo
	private String infoTexte ="";
	private String infoTexte2 ="";
	
	// different sections
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
				c.toutDessiner(g, caseSurvolee, caseCliquee);
				if(dragPerso == true && dragPersoFin != null && dragPersoFin.estValide()) {
					g.setColor(new Color(100,0,0,40));
					g.fillRect(dragPersoFin.getX()*NB_PIX_CASE, dragPersoFin.getY()*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
				}

				
			}
		};
		
		panneauCarte.setPreferredSize(new Dimension(LARGEUR_PANNEAU_CARTE, HAUTEUR_PANNEAU_CARTE));
		
		// ------ creation du panneau log -------- //
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBackground(Color.decode("#8B4513"));
		logArea.setForeground(Color.WHITE);
		logArea.setBorder(BorderFactory.createLineBorder(Color.decode("#663300"), 2));
		
		panneauLog = new JPanel(new BorderLayout());
		panneauLog.add(new JScrollPane(logArea), BorderLayout.CENTER);
		panneauLog.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_L));
		panneauLog.setBackground(Color.decode("#8B4513"));
		
	
		// ------ creation du panneau d'info ----- //
	    panneauInfos = new JPanel() {
	    	protected void paintComponent(Graphics g) {
	    		super.paintComponent(g);
	    		g.setColor(Color.WHITE);
	    		if(infoTexte2 != "" && !(infoTexte2.equals(infoTexte))) {
	    			g.drawString(infoTexte2,10, 15);
	    			g.drawString(infoTexte,10, 30);
	    		}else {
	    			g.drawString(infoTexte,10, 15);
	    		}
	    	}
	    	
	    };
			 
	    panneauInfos.setPreferredSize(new Dimension(LARGEUR_PANNEAU_BAS, HAUTEUR_PANNEAU_BAS));
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
				int x = e.getX();
				int y = e.getY();
				caseSurvolee = carte.coorToPos(x, y);
				
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
			// pensez a dessiner le drag //
			public void mouseDragged(MouseEvent e) {
				if(dragPerso) {
					int x = e.getX();
			        int y = e.getY();
					
			        Position essaie = carte.coorToPos(x, y);
			        
			        Soldat s =(Soldat)carte.getElement(dragPersoInit);
			        
			        if(!s.zoneDeplacement().contient(essaie) && dragPersoInit.equals(dragPersoFin)) {
			        	return;
			        	// gerer exeption
			        }
					dragPersoFin.setX(x);
					dragPersoFin.setY(y);
					deplacePerso = false;
					repaint();
				}
			}
		});
		
		// Ecouteur clic souris
		panneauCarte.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				Element elem = carte.getElement(carte.coorToPos(x, y));
				//System.out.println("x = " + carte.coorToPos(x, y).getX() + " / y = " + carte.coorToPos(x, y).getY());
				// si on est sur le point de deplacé un Heros
				if(deplacePerso) {
					caseAction = carte.coorToPos(x, y);
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
					caseCliquee = carte.coorToPos(x, y);		
					// on initalise le deplacement
					if (caseCliquee.estValide() && elem instanceof Soldat && dragPerso == false) {
						deplacePerso = true;
						infoTexte2 = elem.toString();
						dragPerso = true;
						dragPersoInit = new Position(caseCliquee.getX(), caseCliquee.getY());
						dragPersoFin = new Position(caseCliquee.getX(), caseCliquee.getY());

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
				if(dragPerso && dragPersoFin != null) {
					if(!(dragPersoFin.estValide())){
						dragPerso = false;
						return;
						// surement gerer l'exeptionnelle 
					}
					c.deplaceSoldat(dragPersoFin, ((Soldat)c.getElement(dragPersoInit)));	
					
				}		
				dragPerso = false;
				
			}
		});
		
		setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE, (HAUTEUR_CARTE)*NB_PIX_CASE ));	
	}
	
	
	// Accesseur
	public Position getCaseSurvolee() {
		return this.caseSurvolee;
	}	
	
	// tiens le journal a jour
	private void updateCombatLog() {
		List<String> log = carte.getCombatLog();
		logArea.setText("");
		for(String s : log) {
			logArea.append(s + "\n");
		}
	}
	
	
}    
