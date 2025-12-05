package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;

public class Carte implements IConfig, ICarte {
	private Element[][] carte;
	private int[][] visibilite;
	// private int[][] terrain;
	
	private Heros[] listeHeros;
	private int nbHeros = 0;
	private Monstre[] listeMonstres;
	private int nbMonstre = 0;
	
	private List<String> combatLog = new ArrayList<>();
	private int nbLog = 1;

	public Carte() {
		carte = new Element[LARGEUR_CARTE*2][HAUTEUR_CARTE];
		visibilite = new int[LARGEUR_CARTE*2][HAUTEUR_CARTE];
		
		listeHeros = new Heros[NB_HEROS];
		listeMonstres = new Monstre[NB_MONSTRES];
		
		// Parcours des matrices
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				carte[i][j] = null;
				visibilite[i][j] = 0;
			}
		}
		
		Position p;
		
		// OBSTACLES
		// Placement de la riviere
		p = trouvePositionVide();
		this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		//riviere(p); // Ajouter des ponts
		System.out.println("x y " + p.getX() + " " + p.getY());
		riviere(p);
		
		// Placement des autres obstacles
		for(int i = 0; i < NB_OBSTACLES; i++) {
			p = trouvePositionVide();
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.getObstacleAlea(), p);
			
		}
		// OBSTACLES
		
		// HEROS
	    for(int i = 0; i < NB_HEROS; i++) {
			p = trouvePositionVide();
			Heros heros = new Heros(this, TypesH.getTypeHAlea(), "Goat"+i, p);
			this.listeHeros[nbHeros++] = heros;
			this.carte[p.getX()][p.getY()] = heros;
			this.visibilite = ((Soldat) this.carte[p.getX()][p.getY()]).setCasesVisibles(this.visibilite);
		}
	    // HEROS
	    
	    // MONSTRES
		for(int i = 0; i < NB_MONSTRES; i++) {
			p = trouvePositionVide();
			Monstre monstre = new Monstre(this, TypesM.getTypeMAlea(), "Kostine"+i, p);
			this.listeMonstres[nbMonstre++] = monstre;
			this.carte[p.getX()][p.getY()] = monstre;
		}
		// MONSTRES
				
	}

	
	// LOG DES COMBATS
	public void addCombatMessage(String msg) { combatLog.add(nbLog + " - " + msg); nbLog++; }
	public List<String> getCombatLog(){ return combatLog; }
	public void clearCombatLog() { combatLog.clear(); }
	// LOG DES COMBATS
	 
	
	// RIVIERE
	private void riviere(Position pos) {
		int r = (int) (Math.random() * 3);
		switch(r) {
			case 0: riviereV(pos); break;
			case 1: riviereH(pos); break;
			case 2: riviereV(pos); riviereH(pos); break;
		}
	}
	
	private void riviereV(Position pos) {
		int x_pont1, x_pont2, y_pont1, y_pont2;
		int debut_x = pos.getX();
		int i = 0;
		if (pos.getX() % 2 == 1) {
			debut_x = pos.getX() - 1;
		}
		while (i < HAUTEUR_CARTE) {
			Position p = new Position(debut_x + i%2, i);
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
			i++;
		}
		// Ponts
		y_pont1 = (int) (Math.random() * pos.getY());
		y_pont2 = (int) (Math.random() * (HAUTEUR_CARTE-pos.getY()) + pos.getY());
		System.out.println(" pont1 pont2 : " + y_pont1 + "   " + y_pont2);
		if (y_pont1 % 2 == pos.getY() % 2) {
			x_pont1 = pos.getX();
		} else {
			if (pos.getX() % 2 == 0) {
				x_pont1 = pos.getX() + 1;
			} else {
				x_pont1 = pos.getX() - 1;
			}
		}
		if (y_pont2 % 2 == pos.getY() % 2) {
			x_pont2 = pos.getX();
		} else {
			if (pos.getX() % 2 == 0) {
				x_pont2 = pos.getX() + 1;
			} else {
				x_pont2 = pos.getX() - 1;
			}
		}
		System.out.println(" pont1 pont2 : " + x_pont1 + "   " + x_pont2);
		this.carte[x_pont1][y_pont1] = null;
		this.carte[x_pont2][y_pont2] = null;	
	}
	
	private void riviereH(Position pos) {
		int x_pont1, x_pont2;
		int j = pos.getY() % 2;
		while (j < LARGEUR_CARTE*2) {
			Position p = new Position(j, pos.getY());
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
			j += 2;
		}
		// Ponts
		x_pont1 = (int) (Math.random() * pos.getX()/2);
		x_pont2 = (int) (Math.random() * (LARGEUR_CARTE*2-pos.getX())/2);
		System.out.println("pont1 pont2 : " + pos.getX() + "   " + pos.getY());
		x_pont1 *= 2;
		x_pont2 *= 2;
		x_pont1 += pos.getY() % 2;
		x_pont2 += pos.getX();
		System.out.println("pont1 pont2 : " + x_pont1 + "   " + x_pont2);
		this.carte[x_pont1][pos.getY()] = null;
		this.carte[x_pont2][pos.getY()] = null;
		/*
		pont1 = (int) (Math.random() * pos.getX() + 1);
		pont2 = (int) (Math.random() * (LARGEUR_CARTE*2-pos.getX()) + pos.getX());
		System.out.println("pont1 pont2 : " + pos.getX() + "   " + pos.getY());
		pont1 = pont1 - (pont1+pos.getY()) % 2;
		pont2 = pont2 - (pont2+pos.getY()) % 2;
		System.out.println("pont1 pont2 : " + pont1 + "   " + pont2);
		this.carte[pont1][pos.getY()] = null;
		this.carte[pont2][pos.getY()] = null;
		*/
	}
	// RIVIERE
	
	
	// ELEMENT
	public Element getElement(Position pos) {	
		int x = pos.getX();
		int y = pos.getY();
		
		if (pos.estValide()) {
			return this.carte[x][y];
		}
		
		System.out.println("Erreur getElement() : x = "+x+", y = "+y);
		return null;
	}
	
	public boolean caseDisponible(Position pos) {
		if (!pos.estValide()
			|| this.getElement(pos) != null
			|| this.getElement(pos) instanceof Soldat
			|| this.getElement(pos) instanceof Obstacle) {
			return false;
		}
		return true;
	}	
	// ELEMENT
	
	
	// VISIBILITE
	public int getVisibilite(Position pos) {
		if (pos.estValide()) {
			return this.visibilite[pos.getX()][pos.getY()];
		}
		System.out.println("Erreur : getVisibilite :  0 <= x < " + LARGEUR_CARTE + " | 0 <= y < " + HAUTEUR_CARTE);
		return -1;
	}
	
	// CETTE FONCTION NE DEVRAIT PAS SERVIR, JE LA LAISSE AU CAS OU C'EST POUR LA VISIBILITE
	// On pourra peut-être enlever le param, et faire en sorte que cette fonction y mette à 1
	// Et à chaque fois avant de mettre à jour on remet toute la matrice à 0
	// Comme ça on a juste à remettre les visibilités actuelles (pas d'ennui à se dire est-ce que ça ça devient invisible)
	// public void setVisibilite(Position pos, int visibilite) {
	//  	this.visibilite[pos.getX()][pos.getY()] = visibilite;
	// }
	// VISIBILITE
	
	
	// POSITION VIDE
	public Position trouvePositionVide() {
		int x, y;
		Position pos;
		
		do {
			x = (int) (Math.random()*LARGEUR_CARTE*2);
			y = (int) (Math.random()*HAUTEUR_CARTE);
			if (y % 2 == 0) {
				x -= x % 2;
			} else {
				x += (x+1) % 2;
			}
			pos = new Position(x, y);			
		} while (!pos.estValide() || this.getElement(pos) != null);
		
		return pos;
	}

	public Position trouvePositionVide(Position pos) {
		EnsemblePosition listePos = new EnsemblePosition(8);
		int i;
		int dx, dy, x, y;
		int [] coordsx = {-2, -1, -1, 1, 1, 2};
		int [] coordsy = {0, 1, -1, 1, -1, 0};
		
		// On regarde les 6 cases autour de pos
		for (i = 0 ; i < 6 ; i++) {
			dx = coordsx[i];
			dy = coordsy[i];
			x = pos.getX() + dx;
			y = pos.getY() + dy;
			// On cree une position et on verifie qu'elle correspond : si oui alors inserer dans liste
			Position newp = new Position(x, y);
			if (newp.estValide() && getElement(newp) == null) {
				listePos.ajouterPos(newp);
			}
		}
		if (listePos.getNbPos() == 0) {
			return null;
		}
		return listePos.getPosition((int)(Math.random()*listePos.getNbPos()-1));
	}
	// POSITION VIDE
	
	
	// HEROS
	public Heros trouveHeros() { return this.listeHeros[(int)(Math.random()*nbHeros-1)]; }

	public Heros trouveHeros(Position pos) {
		Heros[] listeHeros = new Heros[NB_HEROS];
		int nbHeros = 0;
		int i;
		int dx, dy;
		int [] coordsx = {-2, -1, -1, 1, 1, 2};
		int [] coordsy = {0, 1, -1, 1, -1, 0};
		
		// On regarde les 6 cases autour de pos
		for (i = 0 ; i < 6 ; i++) {
			dx = coordsx[i];
			dy = coordsy[i];
			Element e = this.carte[pos.getX() + dx][pos.getY() + dy];
			if (e.pos.estValide()) {
				// on verifie si e est une instanciation de Heros
				if (e instanceof Heros) {
					listeHeros[nbHeros++] = (Heros) e;
				}
			}
		}
		
		return listeHeros[(int) (Math.random()*nbHeros - 1)];
	}
	// HEROS
	
	
	// ACTION SOLDAT (actionHeros a revoir surement)
	public boolean actionHeros(Position pos, Position pos2) {
		
		Element e = this.getElement(pos);
		
		if (!(e instanceof Heros) || ((Heros)e).getAction() <= 0) return false;
		
		Heros heros = (Heros)e;
		Element caseCible = this.getElement(pos2);
		
		// Deplacement si case vide
		if (caseCible == null) {
			this.deplaceSoldat(pos2, heros);
		// Combat
		} else if (caseCible instanceof Monstre && pos.distance(pos2) <= heros.getTir()) { // on regarde que tir
			Monstre monstre = (Monstre)caseCible;
			heros.combat(monstre);
		}
		
		
		return true;
	}
	
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
		
			EnsemblePosition ePos = soldat.zoneDeplacement();
			
			if(ePos.contient(pos)) {
				this.carte[soldat.getPos().getX()][soldat.getPos().getY()] = null;
				this.carte[pos.getX()][pos.getY()] = soldat;
				soldat.seDeplace(pos);
				soldat.ajouterAction(-1);
				return true;
			}
			
		return false;
	}
	// ACTION SOLDAT
	
	
	// MORT
	public void mort(Soldat perso) {
		if (perso.getPointsActuels() <= 0) {
			this.carte[perso.getPos().getX()][perso.getPos().getY()] = null;
			// nb_heros_restant--;
		}
	}
	// MORT
	
	// Primitif mais ok (gère pas la forme des hexagones)
	public Position coorToPos(int x, int y) {
		int offset_x = 0;
		int px = x/NB_PIX_CASE,
			py = y/(NB_PIX_CASE*3/4);
		if (py % 2 == 1) {
			offset_x = OFFSET_X;
			x += offset_x;
		}
		
		px = x/NB_PIX_CASE;
		px = px * 2 - py % 2;
		return new Position(px, py);
	}
	
	/*
	private boolean estDansHexagone(Position posHexa, Position pos) {
		int hx = posHexa.getX();
		int hy = posHexa.getY();
		int offset_x = 0;
		x = x/2;
		if (y % 2 == 1) {
			offset_x = OFFSET_X;
		}
	}*/
	
	
	// TOUR DES MONSTRES
	public void jouerSoldats(PanneauJeu pj) {}
	
	// TOUR DES MONSTRES
	
	
	// DESSIN
	public void toutDessiner(Graphics g, Position caseSurvolee, Position caseCliquee) {
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				if ((i+j) % 2 == 1) {
					continue;
				}
				
				Position pos = new Position(i, j);
				Element e = getElement(pos);
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
				if (getVisibilite(pos) == 0
					&& getVisibilite(pos) == 1
					) {
					couleur = COULEUR_INCONNU;
				}
				
				dessineCase(g, couleur, pos);
			}
			
			if (caseSurvolee != null 
				&& caseCliquee == null
				&& caseSurvolee.estValide() 
				&& getElement(caseSurvolee) instanceof Soldat) {
					
				// System.out.println("Coor x : "+x+", y : "+y);
				Soldat soldat = (Soldat)getElement(caseSurvolee);
					
				dessineZoneDeplacement(g, soldat);
			}
				
			// Ajout de la case cliquée
			if (caseCliquee != null
				&& caseCliquee.estValide()
				&& getElement(caseCliquee) instanceof Heros) {
					
				Soldat soldatClic = (Soldat) getElement(caseCliquee);
					
				dessineZoneDeplacement(g, soldatClic);
				dessineCaseCliquee(g, caseCliquee);
			}
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
		//System.out.println("bonjour je passe normalement 1 seule fois, voici mes valeurs : " + x + "   " + y);
		Color couleur = new Color(100,0,0,20); // gestion de l'oppacité
		g.setColor(couleur);
		// Obligé de faire un +1 quand opacité pas au max ???
		this.dessineInterieurHexagone(g, x/2, y);
	}
	
	public void dessineCase(Graphics g, Color couleur, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		int offset_x = 0;
		x = x/2;
		if (y % 2 == 1) {
			offset_x = OFFSET_X;
		}
		
		g.setColor(couleur);
		this.dessineInterieurHexagone(g, x, y);
		g.setColor(Color.BLACK);
		this.dessineContourHexagone(g, x, y);
		
		// Ajout des numeros 
		Element elem = getElement(pos);
		g.setColor(Color.WHITE);
		if(elem instanceof Monstre) {
			g.drawString("" + ((Soldat)elem).getNum(), x*NB_PIX_CASE + offset_x + NB_PIX_CASE/4, y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4);
		}else if(elem instanceof Heros) {
			char lettre = (char)('A' + ((Soldat)elem).getNum());
			g.drawString("" + lettre, x*NB_PIX_CASE + offset_x + NB_PIX_CASE/4, y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4);
		}
	}
	
	private void dessineContourHexagone(Graphics g, int x, int y) {
		int offset_x = 0;
		if (y % 2 == 1) {
			offset_x = OFFSET_X;
		}
		int [] liste_x = {x*NB_PIX_CASE + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE/2 + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE/2 + offset_x,
				  		  x*NB_PIX_CASE + offset_x};
		int [] liste_y = {y*NB_PIX_CASE*3/4 + NB_PIX_CASE/4,
				  		  y*NB_PIX_CASE*3/4,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE/4,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4};
		g.drawPolygon(liste_x, liste_y, 6);
	}
	
	private void dessineInterieurHexagone(Graphics g, int x, int y) {
		int offset_x = 0;
		if (y % 2 == 1) {
			offset_x = NB_PIX_CASE / 2;
		}
		int [] liste_x = {x*NB_PIX_CASE + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE/2 + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE + offset_x,
				  		  x*NB_PIX_CASE + NB_PIX_CASE/2 + offset_x,
				  		  x*NB_PIX_CASE + offset_x};
		int [] liste_y = {y*NB_PIX_CASE*3/4 + NB_PIX_CASE/4,
				  		  y*NB_PIX_CASE*3/4,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE/4,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE,
						  y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4};
		g.fillPolygon(liste_x, liste_y, 6);
	}
	
	// DESSIN
	
	
}


