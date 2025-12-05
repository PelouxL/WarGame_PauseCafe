package wargame;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	// action et boolean
	private boolean deplacePerso = false;
	private boolean dragPerso = false;
	private boolean afficheLog = false;
	
	// information du panneauInfo
	private String infoTexte ="";
	private String infoTexte2 ="";
	
	// different sections
	private JPanel panneauCarte;
	private JPanel panneauInfos;
	private JPanel panneauLog;
	private JPanel panneauDroit;
	private JPanel panneauHaut;
	private JPanel panneauTrans;
	private JTextArea logArea;

	// bouton
	private JButton boutonFin;
	private JButton boutonRetour;
	private JButton boutonAffiche;
	
	public PanneauJeu(Carte c) {
		this.carte = c;
		setLayout( new BorderLayout());
		
		// ------------------------ creation d'un layer ------------------------ //		
		JLayeredPane layers = new JLayeredPane();
		layers.setPreferredSize(new Dimension(LARGEUR_PANNEAU_CARTE, HAUTEUR_PANNEAU_CARTE));
		layers.setLayout(null);
		
		// ------------------------ creation de la Carte ----------------------- //		
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
		
		panneauCarte.setBounds(0, 0, LARGEUR_PANNEAU_CARTE, HAUTEUR_PANNEAU_CARTE);
		layers.add(panneauCarte, Integer.valueOf(JLayeredPane.DEFAULT_LAYER));

		// -------------------- Creation du Panneau log --------------------------------------- //
		// ----- creation du textArea ------- //
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setOpaque(false);
		logArea.setForeground(COULEUR_TEXTE);
		
		
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		// ------ creation du JPanel ------- //
		panneauTrans = new JPanel(new BorderLayout()) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

					g.setColor(COULEUR_PANNEAU_TRANSPARENT); 
					g.fillRect(0, 0, LARGEUR_PANNEAU_LOG, HAUTEUR_PANNEAU_LOG);
					g.setColor(Color.black);
					g.drawRect(0, 0, LARGEUR_PANNEAU_LOG, HAUTEUR_PANNEAU_LOG);
				
			}
		};
		
		// ----- SECTION : mini panneau pour bouton ------- //
		JPanel panneauBouton = new JPanel(new BorderLayout());
		panneauBouton.setOpaque(false);
		panneauTrans.setVisible(afficheLog);
		panneauTrans.add(panneauBouton,BorderLayout.NORTH);
		
		// ----- bouton affiche/desaffiche -- //
		JButton boutonAfficheLog = new JButton("");
		// initialisation si on affiche log au debut ou non
		if(afficheLog) {
			panneauBouton.add(boutonAfficheLog, BorderLayout.EAST);
		}else {
			layers.add(boutonAfficheLog, Integer.valueOf(JLayeredPane.DRAG_LAYER));
			boutonAfficheLog.setBounds(0, HAUTEUR_PANNEAU_CARTE - 10, 25, 10);
		}
		
		// ----- action d'affichage/désaffichage ---- //
		boutonAfficheLog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				afficheLog = !afficheLog;
				
				if(afficheLog) {
					panneauTrans.setVisible(true);
					panneauBouton.add(boutonAfficheLog, BorderLayout.EAST);
					
					panneauTrans.revalidate();
					panneauTrans.repaint();
				}else {
					// on cache et supprime les panneaux
					panneauTrans.setVisible(false);
					panneauBouton.remove(boutonAfficheLog);
					
					// on ajout notre bouton au dessus de nos layer et definit où il se place
					layers.add(boutonAfficheLog, Integer.valueOf(JLayeredPane.DRAG_LAYER));				
					boutonAfficheLog.setBounds(0, HAUTEUR_PANNEAU_CARTE - 10, 25, 10);
				}
				// on repaint uniquement notre layer
				layers.repaint();
			}
		});
		
		panneauTrans.setPreferredSize(new Dimension(100, 50));
		panneauTrans.setOpaque(false);
		panneauTrans.add(scrollPane, BorderLayout.CENTER);
		panneauTrans.setBounds(POSITION_LOG_X, POSITION_LOG_Y, LARGEUR_PANNEAU_LOG, HAUTEUR_PANNEAU_LOG);
		layers.add(panneauTrans, Integer.valueOf(JLayeredPane.PALETTE_LAYER));
		
		
		// ----------------------- creation du panneau d'info ----------------------- //
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
	
		// --------------------------- Creation du panneau droit -------------------- //		
		panneauDroit = new JPanel();
		
		panneauDroit.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_L));	
		panneauDroit.setBackground(Color.decode("#8B4513"));
		panneauDroit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		// --------------------------- Creation de panneau haut ---------------------//	
		JPanel panneauHaut = new JPanel();
			
        panneauHaut.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_PANNEAU_HAUT));
		panneauHaut.setBackground(Color.decode("#8B4513"));
		panneauHaut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		// ---------- Creation des boutons de la carte ---------- //
		boutonFin = new JButton("Fin de tour");
		boutonRetour = new JButton("Retour arrière");
		panneauHaut.add(boutonRetour);
		panneauHaut.add(boutonFin);
		
		boutonFin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carte.jouerSoldats();
				panneauCarte.repaint();
				System.out.println("Termine-moi !");
			}
		});
		
		boutonRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ajouter des vrai méthodes 
				System.out.println("Retourne moi !");
			}
		});
		
		
		// ------------------------ Mises en place des layout ----------------------//
		//add(panneauLog, BorderLayout.WEST);
		add(panneauInfos, BorderLayout.SOUTH);
		add(panneauDroit, BorderLayout.EAST);
		add(panneauHaut, BorderLayout.NORTH);
		add(layers, BorderLayout.CENTER);
		
		// ------------------------- Taile du panneau principal ------------------- //		
		setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMinimumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMaximumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        
		// ------------------------- Ajout des ecouteur -------------------------- //
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
	
	public void updaterCombatLogPostChargement() {
		updateCombatLog();
		if (afficheLog) panneauLog.repaint();
	}
	
}    
