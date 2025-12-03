package wargame;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

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
	
	// position pour le drag&drop
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
	private JPanel panneauDroit;
	private JPanel panneauHaut;
	private JTextArea logArea;

	// bouton
	private JButton boutonFin;
	
	public PanneauJeu(Carte c) {
		this.carte = c;
		setLayout( new BorderLayout());
		

		// ------ creation de la Carte ---------- //
		
		panneauCarte = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				carte.toutDessiner(g, caseSurvolee, caseCliquee);
				if(dragPerso == true && dragPersoFin != null && dragPersoFin.estValide()) {
					g.setColor(new Color(100,0,0,40));
					g.fillRect(dragPersoFin.getX()*NB_PIX_CASE, dragPersoFin.getY()*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
				}

				
			}
		};
		panneauCarte.setPreferredSize(new Dimension(LARGEUR_CARTE, HAUTEUR_CARTE));
		add(panneauCarte, BorderLayout.CENTER);
		// ------ creation du panneau log -------- //
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		
		logArea.setBackground(Color.decode("#8B4513"));
		
		panneauLog = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		panneauLog.add(scrollPane, BorderLayout.CENTER);
		panneauLog.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		panneauLog.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_L));
		panneauLog.setBackground(Color.decode("#8B4513"));

		add(panneauLog, BorderLayout.WEST);
	
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
		panneauInfos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		add(panneauInfos, BorderLayout.SOUTH);
	
		// ------- Creation du panneau droit --------- //		
		panneauDroit = new JPanel();
		
		panneauDroit.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_L));	
		panneauDroit.setBackground(Color.decode("#8B4513"));
		panneauDroit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		add(panneauDroit, BorderLayout.EAST);
		
		// ------- Creation de panneau haut --------//	
		JPanel panneauHaut = new JPanel();
			
        panneauHaut.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_PANNEAU_HAUT));
		panneauHaut.setBackground(Color.decode("#8B4513"));
		panneauHaut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		
		boutonFin = new JButton("Fin de tour");
		panneauHaut.add(boutonFin);
		add(panneauHaut, BorderLayout.NORTH);
		
		// ------- Mises en place des layout ------//
		add(panneauLog, BorderLayout.WEST);
		add(panneauInfos, BorderLayout.SOUTH);
		add(panneauDroit, BorderLayout.EAST);
		add(panneauHaut, BorderLayout.NORTH);
		add(panneauCarte, BorderLayout.CENTER);
		
		// ------- Taile du panneau principal ---- //
		
		setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMinimumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMaximumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        
		// ------- Ajout des ecouteur ---------- //
		panneauCarte.addMouseMotionListener(new MouseMotionAdapter() {
			
			// Effet au deplacement de la souris
			public void mouseMoved(MouseEvent e) {
				int x = e.getX()/NB_PIX_CASE;
				int y = e.getY()/NB_PIX_CASE;
				caseSurvolee = new Position(x, y);
				
				// affichage des infos des soldats
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
			
			// creation de l'evenement du dragg 
			public void mouseDragged(MouseEvent e) {
				if(dragPerso) {
					int x = e.getX()/NB_PIX_CASE;
			        int y = e.getY()/NB_PIX_CASE;
					
			        Position essaie = new Position(x, y);
			        
			        Soldat s =(Soldat)carte.getElement(dragPersoInit);
			        
			        // permet de ne pas sortir des deplacements
			        if(!s.zoneDeplacement().contient(essaie) && !(essaie.equals(dragPersoInit))) {
			        	return;
			        	// gerer exeption
			        }
			        // mise a niveau des positions
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
				int x = e.getX()/NB_PIX_CASE;
				int y = e.getY()/NB_PIX_CASE;
				
				Element elem = carte.getElement(new Position(x,y));
				
				// si on fait un clique gauche
				if(SwingUtilities.isLeftMouseButton(e)) {
					// si on est sur le point de deplacé un Heros
					if(deplacePerso) {
						caseAction = new Position(x, y);
						carte.actionHeros(caseCliquee, caseAction);
						// si on a lancé un combat
						if(elem instanceof Monstre) {
							updateCombatLog();
						}
						
						// on réenitialise après deplacement
						infoTexte2 ="";
						deplacePerso = false;
						caseCliquee = null;
						caseAction = null;
					// si c'est le premier clique, initialisation deplacement
					}else {
						caseCliquee = new Position(x, y);		
						// on initalise le deplacement
						if (caseCliquee.estValide() && elem instanceof Soldat && dragPerso == false) {
							deplacePerso = true;
							infoTexte2 = elem.toString();
							
							// initie le dragg
							dragPerso = true;
							dragPersoInit = new Position(caseCliquee.getX(), caseCliquee.getY());
							dragPersoFin = new Position(caseCliquee.getX(), caseCliquee.getY());
	
						} else {
							// renitialise une fois clique en dehors 
							caseCliquee = null;
							deplacePerso = false;
							infoTexte2 ="";
							
						}
					}
				}else {
					// annulement deplacement
					caseCliquee = null;
					caseAction = null;
					deplacePerso = false;
					// annulement drag
					dragPerso = false;
					dragPersoInit = null;
					dragPersoFin = null;
				}
				
				panneauInfos.repaint();
				panneauCarte.repaint();
			}
			
			public void mouseReleased(MouseEvent e) {
				// si on est entrain de dragg une unité
				if(dragPerso && dragPersoFin != null) {
					if(!(dragPersoFin.estValide())){
						dragPerso = false;
						return;
						// surement gerer l'exeptionnelle 
					}
					// on pose
					carte.deplaceSoldat(dragPersoFin, ((Soldat)carte.getElement(dragPersoInit)));	
					infoTexte2="";
					infoTexte="";
				}		
				dragPerso = false;
				repaint();
			}
			
		});
		
	}
	
	
	// Accesseur
	public Position getCaseSurvolee() {
		return this.caseSurvolee;
	}	

	public void setCarte(Carte c) {
	    this.carte = c;
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
