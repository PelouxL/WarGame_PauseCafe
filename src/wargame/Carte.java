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
		carte = new Element[LARGEUR_CARTE][HAUTEUR_CARTE];
		visibilite = new int[LARGEUR_CARTE][HAUTEUR_CARTE];
		
		listeHeros = new Heros[NB_HEROS];
		listeMonstres = new Monstre[NB_MONSTRES];
		
		// Parcours des matrices
		for(int i = 0; i < LARGEUR_CARTE; i++) {
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
		riviere(p); // Ajouter des ponts
		
		//Placement des autres obstacles
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
		int r = (int)(Math.random()*3);
		switch(r) {
		case 0: riviereV(pos); break;
		case 1: riviereH(pos); break;
		case 2: riviereV(pos); riviereH(pos); break;
		}
	}
	
	private void riviereV(Position pos) {
		for (int i = 0; i < HAUTEUR_CARTE; i++) {
			Position p = new Position(pos.getX(), i);
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		}
		// Ponts
		this.carte[pos.getX()][(int)(Math.random()*pos.getY())] = null;
		this.carte[pos.getX()][(int)(Math.random() * (HAUTEUR_CARTE - pos.getY()) + pos.getY())] = null;
	}
	
	private void riviereH(Position pos) {	
		for (int i = 0; i < LARGEUR_CARTE; i++) {
			Position p = new Position(i, pos.getY());
			this.carte[p.getX()][p.getY()] = new Obstacle(Obstacle.TypeObstacle.EAU, p);
		}
		// Ponts
		this.carte[(int)(Math.random()*pos.getX())][pos.getY()] = null;
		this.carte[(int)(Math.random() * (LARGEUR_CARTE - pos.getX()) + pos.getX())][pos.getY()] = null;
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
			x = (int) (Math.random()*LARGEUR_CARTE);
			y = (int) (Math.random()*HAUTEUR_CARTE);
			pos = new Position(x, y);			
		} while (!pos.estValide() || this.getElement(pos) != null);
		
		return pos;
	}

	public Position trouvePositionVide(Position pos) {
		EnsemblePosition listePos = new EnsemblePosition(8);
		int dx, dy;
		
		// On regarde les 8 cases autour pos
		for(dx = -1 ; dx <= 1 ; dx++) {
			for(dy = -1 ; dy <= 1 ; dy++) {
				// On passe pos
				if (dx == 0 && dy == 0) {
					continue;
				}
				
				// on cree une position et verifie qu'elle correspond si oui alors inserer dans liste
				int x = pos.getX() + dx;
				int y = pos.getY() + dy;
				Position newp = new Position(x, y);
				if (newp.estValide() && getElement(newp) == null) {
					listePos.ajouterPos(newp);
				}
			}
				
		}
		if (listePos.getNbPos() == 0) {
			return null;
		}
		return listePos.getPosition((int)(Math.random()*listePos.getNbPos()-1));
	}
	// POSITION VIDE
	
	
	// TOUR DES MONSTRES
	public Heros trouveHeros() { return this.listeHeros[(int)(Math.random()*nbHeros-1)]; }
	
	public Heros trouveHeros(Position pos) {
		
		/* 
		 * Ameliorations possibles :
		 * - Comparer les pdv des heros trouves pour estimer quelles serait la meilleure cible
		 * - Prioriser un heros que le monstre peut tuer
		 * - Si aucun heros n'est trouve en melee ou a distance alors chercher le heros 
		 *   le plus proche ou qui a le moins de vie
		 * 
		 */
		
		// On regarde d'abord s'il y a des heros adjacents
		Heros heros = this.trouveHerosMelee(pos);
		
		// Si aucun heros n'est trouvé alors on regarde les heros a distance
		/* if (heros == null) {
			heros = this.trouveHerosDistance(pos);
		} */
		
		// Si toujours aucun heros n'est trouve alors on cherche un heros sur la carte
		if (heros == null) {
			heros = this.trouveHeros();
		}
		
		return heros;
	}
	
	private Heros trouveHerosMelee(Position pos) {
		int nbHerosMelee = 0;
		Heros[] HerosMelee = new Heros[4]; // 6 quand hexagones
		
		for (int dx = -1; dx <= 1; dx += 2) {
			for (int dy = -1; dy <= 1; dy += 2) {
				
				Element e = this.carte[pos.getX() + dx][pos.getY() + dy];
				if (e.pos.estValide() && e != null && e instanceof Heros) {
					HerosMelee[nbHerosMelee++] = (Heros) e;
				}
			}
		}
		
		if (nbHerosMelee == 0) { return null; }
		
		return HerosMelee[(int)(Math.random()*nbHerosMelee-1)];
	}
	
	/*
	private Heros trouveHerosDistance(Position pos, Int portee) {
		int nbHerosDistance = 0;
		Heros[] HerosDistance = new Heros[NB_HEROS-4]; // 6 quand hexagones
		
		// Il faut d'abord savoir gerer les lignes de vue pour pouvoir gerer les heros a distance
		
		return null;
	}
	*/
	
	public void jouerSoldats() { // a quoi sert panneau jeu en param?
		for (Monstre monstre : this.listeMonstres) {
			Heros heros = listeHeros[0];
			int distanceHeros = monstre.getPos().distance(heros.getPos());
			System.out.println(" -> Debut du tour");
			
			// Le monstre cherche le heros le plus proche
			for (int i=1; i < this.nbHeros; i++) {
				Heros test = listeHeros[i];
				int distanceTest = monstre.getPos().distance(test.getPos());
				if (distanceTest < distanceHeros) {
					heros = test;
					distanceHeros = distanceTest;
				}
			}
			
			System.out.println(monstre.getNom()+" veut attaquer "+heros.getNom());
			
			// Tant qu'il reste des actions au monstre il regarde s'il peut attaquer, sinon il avance
			while(monstre.getAction() > 0) {
				System.out.println(" -> PA = "+monstre.getAction()+"actions");
				
				/*
				 * 2 cas : 
				 * - Le monstre peut attaquer le heros
				 * - Le monstre ne peut pas attaquer le heros
				 * Amelioration possible : le monstre evalu s'il devrait l'attaquer plutot en melee ou a distance
				 */
				
				Position posHeros = heros.getPos();
				Position posMonstre = monstre.getPos();
				boolean peutAttaquer = false;
				
				// Verifie la distance d'attaque
				if (posMonstre.distance(posHeros) <= monstre.getPortee()) {
					System.out.println(" - il peut attaquer, distance = "+posMonstre.distance(posHeros)+", portee = "+monstre.getPortee());
					peutAttaquer = true;
				}
				
				if (peutAttaquer) { // 1er cas : le heros est a portee du monstre
					System.out.println(" - Combat");
					peutAttaquer = monstre.combat(heros);
					if (!peutAttaquer) System.out.println(" - n'a aps pu combattre : puis = "+monstre.getPuissance()+", tir = "+monstre.getTir());
				} 
				
				if (!peutAttaquer){ // 2eme cas : le monstre doit se rapprocher de sa cible
					System.out.println(" - Decide de se rapprocher");
					
					// Recuperation la liste des cases accessibles par le monstre
					EnsemblePosition ePos = monstre.zoneDeplacement();
					Position plusProche = posMonstre;
					
					for (int i=0; i < ePos.getNbPos(); i++) {
						Position test = ePos.getPosition(i);
						if (test.distance(posHeros) < plusProche.distance(posHeros) && !(test.equals(posHeros))) {
							plusProche = test; 
						}
					}
					
					System.out.println(" - pos la plus proche : "+plusProche.toString()+", pos monstre : "+posMonstre.toString());
					
					if (plusProche.equals(posMonstre)) monstre.setAction(0);
					else {
						System.out.println(" - se deplace de "+posMonstre.distance(plusProche)+" cases");
						this.deplaceSoldat(plusProche, monstre);
						monstre.seDeplace(plusProche);
					}
					
					System.out.println(" -> reste PA = "+monstre.getAction());
					
				}
			}
			
			System.out.println(" -> Fin du tour");
			
			// Lorsque le tour du monstre est termine on remet ses actions a 2
			monstre.setAction(2);
			
		}
	}
	// TOUR DES MONSTRES
	
	
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
	public void mort(Soldat soldat) {
		if (soldat.getPointsActuels() <= 0) {
			if (soldat instanceof Heros) {
				this.nbHeros--;
				boolean trouve = false;
				for (int i=0; i < nbHeros; i++) {
					if (listeHeros[i].getPos().equals(soldat.getPos())) {
						trouve = true;
					}
					if (trouve) {
						listeHeros[i] = listeHeros[i+1];
					}
				}
			} else {
				this.nbMonstre--;
				boolean trouve = false;
				for (int i=0; i < nbMonstre; i++) {
					if (listeMonstres[i].getPos().equals(soldat.getPos())) {
						trouve = true;
					}
					if (trouve) {
						listeMonstres[i] = listeMonstres[i+1];
					}
				}
			}	
			
			this.carte[soldat.getPos().getX()][soldat.getPos().getY()] = null;
		}
	}
	// MORT
	
	public Position coorToPos(int x, int y) {
		int px = x/NB_PIX_CASE,
			py = y/NB_PIX_CASE ;
		return new Position(px, py);
	}
	
	// DESSIN
	public void toutDessiner(Graphics g, Position caseSurvolee, Position caseCliquee) {
		for(int i = 0; i < LARGEUR_CARTE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				
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
		Color couleur = new Color(100,0,0,20); // gestion de l'oppacité
		g.setColor(couleur);
		// Obligé de faire un +1 quand opacité pas au max ???
		g.fillRect(x*NB_PIX_CASE + 1, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
	}
	
	public void dessineCase(Graphics g, Color couleur, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		
		g.setColor(couleur);
		g.fillRect(x*NB_PIX_CASE, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
		g.setColor(Color.BLACK);
		g.drawRect(x*NB_PIX_CASE, y*NB_PIX_CASE, NB_PIX_CASE, NB_PIX_CASE);
		
		// Ajout des numeros 
		Element elem = getElement(pos);
		g.setColor(Color.WHITE);
		if(elem instanceof Monstre) {
			g.drawString(""+((Soldat)elem).getNum(),x * NB_PIX_CASE + 4,y * NB_PIX_CASE + 15);
		}else if(elem instanceof Heros) {
			char lettre = (char)('A' + ((Soldat)elem).getNum());
			g.drawString(""+lettre, x * NB_PIX_CASE + 4,y * NB_PIX_CASE + 15);
		}
	}
	// DESSIN
	
	
}


