package wargame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

public class PanneauJeu extends JPanel implements IConfig {
	private Carte carte;
	private Position caseSurvolee;
	private Position caseCliquee;
	private Position caseAction;
	
	// Drag&drop
	private Position dragPersoFin = null;
	private Position dragPersoInit = null;
	
	// Action et boolean
	private boolean deplacePerso = false;
	private boolean dragPerso = false;
	private boolean afficheLog = false;
	private Competence choisiComp = null;
	private Heros herosChoisi = null;
	
	// infos panneauInfo
	private String infoTexte ="";
	private String infoTexte2 ="";
	
	// Panneaux
	private JPanel panneauCarte;
	private JPanel panneauInfos;
	private JPanel panneauLog;
	private JPanel panneauDroit;
	private JPanel panneauHaut;
	private JPanel panneauTrans;
	private JTextArea logArea;

	// Boutons
	private JButton boutonFin;
	private JButton boutonRetour;
	private JButton boutonAffiche;
	private JButton boutonRevenirMenu;
	
	// Gestion din du jeu
	private int finJeu = 0;
	private String messageFinJeu = "";
	
	
	public PanneauJeu(Carte c) {
		this.carte = c;
		setLayout(new BorderLayout());
		
		// ------------------------------- Creation Layer ------------------------------- //		
		JLayeredPane layers = new JLayeredPane();
		layers.setPreferredSize(new Dimension(LARGEUR_PANNEAU_CARTE, HAUTEUR_PANNEAU_CARTE));
		layers.setLayout(null);
		
		// ---------------------------- Creation panneauCarte --------------------------- //		
		panneauCarte = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				RenduCarte.dessiner(g, carte, caseSurvolee, caseCliquee, choisiComp);
				if (dragPerso == true && dragPersoFin != null && dragPersoFin.estValide()) {
					RenduCarte.dessinerCaseCliquee(g, dragPersoFin);
				}	
				verifFinJeu();
				if (finJeu != 0) {
					afficherFinJeu(g);
				}
			}
		};
		panneauCarte.setBackground(Color.BLACK);
		panneauCarte.setBounds(0, 0, LARGEUR_PANNEAU_CARTE, HAUTEUR_PANNEAU_CARTE);
		layers.add(panneauCarte, Integer.valueOf(JLayeredPane.DEFAULT_LAYER));

		// ----------------------------- Creation panneauLog ---------------------------- //
		// ------------------- TextArea ------------------- //
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setOpaque(false);
		logArea.setForeground(COULEUR_TEXTE);
		
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		// -------------------- JPanel -------------------- //
		panneauTrans = new JPanel(new BorderLayout()) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

					g.setColor(COULEUR_PANNEAU_TRANSPARENT); 
					g.fillRect(0, 0, LARGEUR_PANNEAU_LOG, HAUTEUR_PANNEAU_LOG);
					g.setColor(Color.black);
					g.drawRect(0, 0, LARGEUR_PANNEAU_LOG, HAUTEUR_PANNEAU_LOG);
				
			}
		};
		
		// ----------- Bouton affiche/desaffiche ---------- //
		JPanel panneauBouton = new JPanel(new BorderLayout());
		JButton boutonAfficheLog = new JButton("");
		
		panneauBouton.setOpaque(false);
		panneauTrans.setVisible(afficheLog);
		panneauTrans.add(panneauBouton,BorderLayout.NORTH);

		// initialisation si on affiche log au debut ou non
		if(afficheLog) panneauBouton.add(boutonAfficheLog, BorderLayout.EAST);
		else {
			layers.add(boutonAfficheLog, Integer.valueOf(JLayeredPane.DRAG_LAYER));
			boutonAfficheLog.setBounds(0, HAUTEUR_PANNEAU_CARTE - 10, 25, 10);
		}
		
		// --------- Action affichage/desaffichage -------- //
		boutonAfficheLog.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (finJeu == 0) {
					
					afficheLog = !afficheLog;
					panneauTrans.setVisible(afficheLog);
				
					if(afficheLog) {
						panneauBouton.add(boutonAfficheLog, BorderLayout.EAST);
						
						panneauTrans.revalidate();
						updateCombatLog();
						panneauTrans.repaint();
					} else {
						// on cache et supprime les panneaux
						panneauBouton.remove(boutonAfficheLog);
						
						// on replace le bouton en bas a gauche
						layers.add(boutonAfficheLog, Integer.valueOf(JLayeredPane.DRAG_LAYER));				
						boutonAfficheLog.setBounds(0, HAUTEUR_PANNEAU_CARTE - 10, 25, 10);
					}
					
					// on repaint uniquement notre layer
					layers.repaint();
				}
			}
		});
		
		panneauTrans.setPreferredSize(new Dimension(100, 50));
		panneauTrans.setOpaque(false);
		panneauTrans.add(scrollPane, BorderLayout.CENTER);
		panneauTrans.setBounds(POSITION_LOG_X, POSITION_LOG_Y, LARGEUR_PANNEAU_LOG, HAUTEUR_PANNEAU_LOG);
		layers.add(panneauTrans, Integer.valueOf(JLayeredPane.PALETTE_LAYER));
		
		// ----------------------- Creation panneauInfos (en bas) ----------------------- //
	    panneauInfos = new JPanel() {
	    	protected void paintComponent(Graphics g) {
	    		super.paintComponent(g);
	    		/*
	    		g.setColor(Color.WHITE);
	    		if(infoTexte2 != "" && !(infoTexte2.equals(infoTexte))) {
	    			g.drawString(infoTexte2,10, 15);
	    			g.drawString(infoTexte,10, 30);
	    		}else {
	    			g.drawString(infoTexte,10, 15);
	    		}
	    		*/
	    		RenduCarte.dessineInfosBas(g, c, caseCliquee);
	    	}
	    	
	    };
			 
	    panneauInfos.setPreferredSize(new Dimension(LARGEUR_PANNEAU_BAS, HAUTEUR_PANNEAU_BAS));
		panneauInfos.setBackground(COULEUR_PLATEAU);
		panneauInfos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	
		// ---------------------------- Creation panneauDroit --------------------------- //		
		// ignorez les trucs dans le paintComponent c'était juste pour tester
		// des trucs (pour afficher les stats du perso courant)
		panneauDroit = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(COULEUR_FORET);
				g.fillRect(LARGEUR_FENETRE-LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_HAUT, 60, 60);
			}
		};
		panneauDroit.setLayout(new BoxLayout(panneauDroit, BoxLayout.Y_AXIS));
		panneauDroit.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_L));	
		panneauDroit.setBackground(COULEUR_PLATEAU);
		panneauDroit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		// ---------------------------- Creation panneauHaut ---------------------------- //
		JPanel panneauHaut = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				g.drawString("Tour " + Integer.toString(carte.getNbTours()), NB_PIX_CASE, HAUTEUR_PANNEAU_HAUT/2);
				verifFinJeu();
				if (finJeu != 0) {
					boutonFin.setVisible(false);
					boutonRetour.setVisible(false);
					boutonRevenirMenu.setVisible(true);
				}
			}
		};
			
        panneauHaut.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_PANNEAU_HAUT));
		panneauHaut.setBackground(COULEUR_PLATEAU);
		panneauHaut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		// --------------- Creation boutons --------------- //
		boutonFin = new JButton("Fin de tour");
		boutonRetour = new JButton("Retour arrière");
		boutonRevenirMenu = new JButton("Revenir au menu");
		panneauHaut.add(boutonRetour);
		panneauHaut.add(boutonFin);
		panneauHaut.add(boutonRevenirMenu);
		
		boutonFin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (finJeu == 0) {
					carte.finTour();
					//verifFinJeu();
					//logArea.repaint();
					panneauCarte.repaint();
					panneauHaut.repaint();
					panneauDroit.repaint();
					panneauInfos.repaint();
					System.out.println("Termine-moi !");
				}
			}
		});
		
		boutonRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (finJeu == 0) {
					// Ajouter des vrai méthodes 
					System.out.println("Retourne moi !");
				}
			}
		});
		
		boutonRevenirMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame fenetre = (JFrame) SwingUtilities.getWindowAncestor(panneauHaut);
				fenetre.dispose();
				new FenetreMenu();
			}
		});
		
		boutonFin.setVisible(true);
		boutonRetour.setVisible(true);
		boutonRevenirMenu.setVisible(true);
		
		// -------------------------- Mises en place des layout ------------------------- //
		add(panneauInfos, BorderLayout.SOUTH);
		add(panneauDroit, BorderLayout.EAST);
		add(panneauHaut, BorderLayout.NORTH);
		add(layers, BorderLayout.CENTER);
		
		// ------------------------- Taille du panneau principal ------------------------ //		
		setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMinimumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMaximumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        
		// ------------------------- Ajout des ecouteurs -------------------------- //
        
        // -------------------- Souris -------------------- //
		panneauCarte.addMouseMotionListener(new MouseMotionAdapter() {
			
			// Mouvement
			public void mouseMoved(MouseEvent e) {
				if (finJeu == 0) {
					int x = e.getX();
					int y = e.getY();
					caseSurvolee = carte.coorToPos(x, y);
					
					// Affichage des infos des soldats
					// /!\ ATTENTION code inutile pour le moment
					if (caseSurvolee.estValide() && carte.getVisibilite(caseSurvolee) == 1) {
						Soldat soldat = carte.getSoldat(caseSurvolee);
						if(soldat instanceof Soldat) infoTexte = soldat.toString();
						else infoTexte = "";
					} else infoTexte = "";
				}
				panneauInfos.repaint();
				panneauCarte.repaint();
			}
			
			// Mouvement en gardant le bouton enfonce
			public void mouseDragged(MouseEvent e) {
				if (finJeu == 0) {
					if(dragPerso) {
						int x = e.getX();
				        int y = e.getY();
						
				        Position essaie = carte.coorToPos(x, y);
				        
				        Soldat s = carte.getSoldat(dragPersoInit);
				        
				        // permet de ne pas sortir des deplacements
				        if(!s.zoneDeplacement().contient(essaie) && !(essaie.equals(dragPersoInit))) {
				        	return;
				        	// gerer exeption
				        }
	
						dragPersoFin.setX(essaie.getX());
						dragPersoFin.setY(essaie.getY());
						deplacePerso = false;
						panneauCarte.repaint();
					}
				}
			}
		});
		
		// Clic
		panneauCarte.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				Soldat soldat = carte.getSoldat(carte.coorToPos(x, y));
				
				// Clic gauche
				if(SwingUtilities.isLeftMouseButton(e)) {
					
					// Cas 1 - Deplacer un soldat
					if(deplacePerso && choisiComp == null && caseCliquee != null) {
						caseAction = carte.coorToPos(x, y);
						carte.actionHeros(caseCliquee, caseAction);
						
						// si on a lancé un combat (Utilite???)
						if(soldat instanceof Monstre) {
							updateCombatLog();
						}
						
						// Reinitialisation apres deplacement
						infoTexte2 ="";
						deplacePerso = false;
						caseCliquee = null;
						caseAction = null;
						nettoyerPanneauDroit();
						
					// Cas 2 - Lancement de competence 
					} else if(choisiComp != null) {
					
						caseAction = carte.coorToPos(x, y);
						choisiComp.utiliserCompetence(carte.getSoldat(caseCliquee), caseAction, carte);
						
						caseCliquee = null;
						caseAction = null;
						choisiComp = null;
						infoTexte2 ="";
						nettoyerPanneauDroit();
						panneauCarte.repaint();
						
					// Cas 3 - Choix du heros a deplacer
					} else {
						caseCliquee = carte.coorToPos(x, y);
						
						if (soldat instanceof Heros) {
							herosChoisi = (Heros) soldat;
						}
						
						// Preparation au deplacement
						if (caseCliquee.estValide() && soldat instanceof Heros && dragPerso == false && choisiComp == null) {
							
							deplacePerso = true;
							mettreAJourPanneauDroit();
							infoTexte2 = soldat.toString();
							
							dragPerso = true;
							dragPersoInit = new Position(caseCliquee.getX(), caseCliquee.getY());
							dragPersoFin = new Position(caseCliquee.getX(), caseCliquee.getY());
							
						// Annulation du deplacement
						} else {
							caseCliquee = null;
							deplacePerso = false;
							infoTexte2 ="";
							// OCTODAMS (remettre ?) caseAction = null;
							choisiComp = null;
							nettoyerPanneauDroit();			
						}
					}
				}else {
					// Annulation deplacement
					caseCliquee = null;
					caseAction = null;
					deplacePerso = false;
					// Annulation drag
					dragPerso = false;
					dragPersoInit = null;
					dragPersoFin = null;
					infoTexte2="";
					choisiComp = null;
					nettoyerPanneauDroit();
				}
				
				// OCTODAMS (enlever ?)
				panneauInfos.repaint();
				panneauCarte.repaint();
				panneauDroit.repaint();
			}
			
			// Relachement du bouton
			public void mouseReleased(MouseEvent e) {
				if (finJeu == 0) {
					// si on est entrain de dragg une unité
					if(dragPerso && dragPersoFin != null) {
						if(!(dragPersoFin.estValide())){
							dragPerso = false;
							return;
							// surement gerer l'exeptionnelle 
						}
						// on pose
						carte.deplaceSoldat(dragPersoFin, ((Soldat)carte.getSoldat(dragPersoInit)));	
						
						infoTexte="";
					}		
					dragPerso = false;
					repaint();
				}
			}
			
		});	
		
		
		// ATTENTION GET NUM FAUX, IL FAUT L'index reel
		panneauCarte.addKeyListener( new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				ArrayList<Heros> listeHeros = carte.getListeHeros();
				
				
				if (finJeu == 0) {
					switch (e.getKeyCode()) {
					
					// Selection de heros
					case KeyEvent.VK_LEFT :
						System.out.println("touche gauche");
						if (herosChoisi == null) herosChoisi =  listeHeros.get(listeHeros.size() -1);
						else herosChoisi = listeHeros.get((herosChoisi.getNum()-1) % listeHeros.size());
						break;
					case KeyEvent.VK_RIGHT :
						if (herosChoisi == null) herosChoisi =  listeHeros.get(0);
						else herosChoisi = listeHeros.get((herosChoisi.getNum()+1) % listeHeros.size());
						break;
					default :
						System.out.println("Touche non-attribuée");
					}
				}
			}
		});
	}

	
	// -------------------COMPETENCE------------------ //
	private void mettreAJourPanneauDroit() {
		panneauDroit.removeAll();
		if(caseCliquee != null) {
			Soldat soldat = carte.getSoldat(caseCliquee);
			if(soldat instanceof Heros) {
				for(Competence c : soldat.getCompetences()) {							
					JButton boutonCompetence = creerBoutonCompetence(c);
					//boutonCompetence.setBorderPainted(false);
					panneauDroit.add(boutonCompetence);
				}
			}
		}
	
		panneauDroit.revalidate();
		panneauDroit.repaint();
	}
	
	private JButton creerBoutonCompetence(Competence competence) {
		String s = competence.getType().getNom();
		JButton boutonCompetence = new JButton(s);
		ImageIcon icon = new ImageIcon(competence.trouverImg());
		
		// Afficher le temps restant
		if(!competence.peutUtiliser()) s +=  "\n "+competence.getTempsRestant()+ " tour(s) restant";
		
		// Initialisation du bouton
		boutonCompetence.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, 50)); // pas pris en compte?
	    boutonCompetence.setIcon(icon); 
	    boutonCompetence.setForeground(Color.white);
	 
	    if(!competence.peutUtiliser()) boutonCompetence.setBackground(COULEUR_BOUTON_COMP_INDISPONIBLE);
	    else boutonCompetence.setBackground(COULEUR_BOUTON_COMP);
	    
	    // Action
	    boutonCompetence.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {	
	    		if(choisiComp == null) {	
	    			choisiComp = competence;
	    			
	    			if(choisiComp.peutUtiliser()) {
	    				changeCurseur(competence.trouverImg(), 16, 16, competence.getType().getNom());
	    			}else if(carte.getSoldat(caseCliquee).getAction() < choisiComp.getType().getCoutAction()){
	    				System.out.println("Vous n'aveez pas les point d'action necessaire ! ");
	    			}else {
	    				System.out.println("La competence n'est pas encore disponible !!! ");
	    				choisiComp = null;
	    			}
	    		}else {
	    			choisiComp = null;
	    		}
	    		panneauCarte.repaint();
	    	}
	    });
			
		return boutonCompetence;
	}
	
	private void nettoyerPanneauDroit() {
		panneauDroit.removeAll();
		setCursor(Cursor.getDefaultCursor());
		
		panneauDroit.revalidate();
		panneauDroit.repaint();
	}
	
	private void changeCurseur(String chemin, int x, int y, String nom) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.getImage(chemin);
		Cursor c = tk.createCustomCursor(image, new java.awt.Point(x,y), nom);
		setCursor(c);
	}
	
	// Accesseur
	public Position getCaseSurvolee() {
		return this.caseSurvolee;
	}	

	public void setCarte(Carte c) {
	    this.carte = c;
	    repaint();
	}
	
	// tiens le journal a jour
	private void updateCombatLog() {
		List<String> log = carte.getCombatLog();
		logArea.setText("");
		for(String s : log) {
			logArea.append(s + "\n");
		}
	}
	
	public void verifFinJeu() {
		int fin = carte.verifierFinJeu();
		if (fin != 0) {
			if (fin == -1) {
				logArea.setText("L'IA a gagné... Fin du jeu");
			} else {
				if (fin == 1) {
					logArea.setText("Vous avez gagné ! Fin du jeu");
				}
			}
		}
		finJeu = fin;
	}
	
	
	public void afficherFinJeu(Graphics g) {
		int x, y;
		x = LARGEUR_PANNEAU_CARTE / 2;
		y = HAUTEUR_PANNEAU_CARTE / 2;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, LARGEUR_PANNEAU_CARTE, HAUTEUR_PANNEAU_CARTE);
		g.setColor(Color.WHITE);
		if (finJeu == 1) {
			g.drawString("Vous avez gagné !", x, y);
		}
		if (finJeu == -1) {
			g.drawString("Vous avez perdu...", x, y);
		}
	}
	

}    
