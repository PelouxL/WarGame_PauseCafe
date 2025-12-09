package wargame;

import javax.swing.JButton;
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
	private Competence choisiComp = null;
	
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
				carte.toutDessiner(g, caseSurvolee, caseCliquee, choisiComp);
				if(dragPerso == true && dragPersoFin != null && dragPersoFin.estValide()) {

					carte.dessineCaseCliquee(g, dragPersoFin);
				}			
			}
		};
		panneauCarte.setBackground(Color.BLACK);
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
					updateCombatLog();
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
		panneauInfos.setBackground(COULEUR_PLATEAU);
		panneauInfos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	
		// --------------------------- Creation du panneau droit -------------------- //		
		panneauDroit = new JPanel();
		panneauDroit.setLayout(new BoxLayout(panneauDroit, BoxLayout.Y_AXIS));
		panneauDroit.setPreferredSize(new Dimension(LARGEUR_PANNEAU_L, HAUTEUR_PANNEAU_L));	
		panneauDroit.setBackground(COULEUR_PLATEAU);
		panneauDroit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		// --------------------------- Creation de panneau haut ---------------------//	
		JPanel panneauHaut = new JPanel();
		JTextArea tourActuel = new JTextArea();
			
        panneauHaut.setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_PANNEAU_HAUT));
		panneauHaut.setBackground(COULEUR_PLATEAU);
		panneauHaut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		panneauHaut.add(tourActuel);
		tourActuel.setText(Integer.toString(carte.getNbTours()));

		// ---------- Creation des boutons de la carte ---------- //
		boutonFin = new JButton("Fin de tour");
		boutonRetour = new JButton("Retour arrière");
		panneauHaut.add(boutonRetour);
		panneauHaut.add(boutonFin);
		
		boutonFin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carte.jouerSoldats();
				tourActuel.setText(Integer.toString(carte.getNbTours()));
				tourActuel.repaint();
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
		
		// ------------------------- Taille du panneau principal ------------------- //		
		setPreferredSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMinimumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        setMaximumSize(new Dimension(LARGEUR_FENETRE, HAUTEUR_FENETRE));
        
		// ------------------------- Ajout des ecouteurs -------------------------- //
		panneauCarte.addMouseMotionListener(new MouseMotionAdapter() {
			
			// Effet au deplacement de la souris
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				caseSurvolee = carte.coorToPos(x, y);
				
				// affichage des infos des soldats
				if (caseSurvolee.estValide()) {
					Soldat soldat = carte.getSoldat(caseSurvolee);
					if(soldat instanceof Soldat) {
						infoTexte = soldat.toString();
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
		});
		
		// Ecouteur clic souris
		panneauCarte.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				Soldat soldat = carte.getSoldat(carte.coorToPos(x, y));
				
				// si on fait un clique gauche
				if(SwingUtilities.isLeftMouseButton(e)) {
					// si on est sur le point de deplacé un Heros
					if(deplacePerso && choisiComp == null && caseCliquee != null) {
						caseAction = carte.coorToPos(x, y);
						carte.actionHeros(caseCliquee, caseAction);
						// si on a lancé un combat
						if(soldat instanceof Monstre) {
							updateCombatLog();
						}
						
						// on réenitialise après deplacement
						infoTexte2 ="";
						deplacePerso = false;
						caseCliquee = null;
						caseAction = null;
						
						// le cas où une competence est lancer 
					}else if(choisiComp != null) {
					
						caseAction = carte.coorToPos(x, y);
						choisiComp.utiliserCompetence(carte.getSoldat(caseCliquee), caseAction, carte);
						
						// reccuperer le clic
						// verifier qu'on clique bien sur une case disponbiel
						// si le clic est bon que faire ?? forcement sur un Monstre ou sur une case vode ? 
						caseCliquee = null;
						caseAction = null;
						choisiComp = null;
						infoTexte2 ="";
						nettoyerPanneauDroit();
						
						
					// si c'est le premier clique, initialisation deplacement
					}else {
						caseCliquee = carte.coorToPos(x, y);		
						// on initalise le deplacement
						if (caseCliquee.estValide() && soldat instanceof Soldat && dragPerso == false && choisiComp == null) {

							deplacePerso = true;
							mettreAJourPanneauDroit();
							System.out.println("bah tu affiche la ? ");
							infoTexte2 = soldat.toString();
							System.out.println("bah tu affiche la2 ? ");
							// initie le dragg
							dragPerso = true;
							dragPersoInit = new Position(caseCliquee.getX(), caseCliquee.getY());
							dragPersoFin = new Position(caseCliquee.getX(), caseCliquee.getY());
						} else {
							// renitialise une fois clique en dehors 
							
							caseCliquee = null;
							deplacePerso = false;
							infoTexte2 ="";
							
							choisiComp = null;
							nettoyerPanneauDroit();
							
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
					infoTexte2="";
					choisiComp = null;
					nettoyerPanneauDroit();
				}
				
				panneauInfos.repaint();
				panneauCarte.repaint();
				panneauDroit.repaint();
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
					carte.deplaceSoldat(dragPersoFin, ((Soldat)carte.getSoldat(dragPersoInit)));	
					
					infoTexte="";
				}		
				dragPerso = false;
				repaint();
			}
			
		});
		
	}

	
	// -------------------COMPETENCE------------------
	private void mettreAJourPanneauDroit() {
		panneauDroit.removeAll();
		if(caseCliquee != null) {
			Soldat soldat = carte.getSoldat(caseCliquee);
			if(soldat instanceof Heros) {
				for(Competence c : soldat.getCompetences()) {							
					JButton boutonCompetence = creeBoutonCompetence(c);
					//boutonCompetence.setBorderPainted(false);
					panneauDroit.add(boutonCompetence);
				}
			}
		}
	
		panneauDroit.revalidate();
		panneauDroit.repaint();
	}
	
	private JButton creeBoutonCompetence(Competence competence) {
		JButton boutonCompetence = new JButton(competence.getType().getNom());
			
		ImageIcon icon = new ImageIcon(competence.trouverImg()); 
	    boutonCompetence.setIcon(icon); 
	    boutonCompetence.setForeground(Color.white);
	    boutonCompetence.setBackground(COULEUR_BOUTON_COMP);
	 
	    boutonCompetence.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		changeCurseur(competence.trouverImg(), 16, 16, competence.getType().getNom());
	    		if(choisiComp == null) {		
	    			choisiComp = competence;
	    			// carte.dessinePorteeCompetence(getGraphics(), competence, carte.getSoldat(caseCliquee));
	    				
	    			// caseCliquee = null;
	    			// dessiner les cases atteignable sur la carte avec la competence
	    				
	    		} else {
	    			choisiComp = null;
	    			
	    			// utiliserCompetence(); // Appeler la fonction qui utilise la compétence
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
