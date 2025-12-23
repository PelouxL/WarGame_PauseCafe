package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;
import wargame.Terrain.TypeTerrain;

public class Carte implements IConfig, ICarte, Serializable {
	private Terrain[][] carte;
	private int[][] visibilite;
	
	private Heros[] listeHeros;
	private int nbHeros = 0;
	private Monstre[] listeMonstres;
	private int nbMonstre = 0;
	private int nbTours = 0;
	private int tourActuel = 0;
	
	private List<String> combatLog = new ArrayList<>();
	private int nbLog = 1;

	public Carte() {
		carte = new Terrain[LARGEUR_CARTE*2][HAUTEUR_CARTE];
		visibilite = new int[LARGEUR_CARTE*2][HAUTEUR_CARTE];
		
		listeHeros = new Heros[NB_HEROS];
		listeMonstres = new Monstre[NB_MONSTRES];
		
		// Remplissage de la matrice
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				this.carte[i][j] = new Terrain(Terrain.TypeTerrain.HERBE); // Herbe par defaut
				visibilite[i][j] = 0;
			}
		}
		
		// OBSTACLES
		// Placement de la riviere
		Position p = trouvePositionVide();
		this.carte[p.getX()][p.getY()] =  new Terrain(Terrain.TypeTerrain.EAU);
		riviere(p);
		
		// Placement des autres obstacles
		for(int i = 0; i < NB_OBSTACLES; i++) {
			p = trouvePositionVide();
			this.carte[p.getX()][p.getY()] = new Terrain(Terrain.TypeTerrain.getTerrainAlea());
			
		}
		// OBSTACLES
		
		// HEROS
	    for(int i = 0; i < NB_HEROS; i++) {
			p = trouvePositionVide();
			Heros heros = new Heros(this, TypesH.getTypeHAlea(), "Goat"+i, p);
			this.listeHeros[nbHeros++] = heros;
			this.carte[p.getX()][p.getY()].occuper(heros);
			this.visibilite = heros.setCasesVisibles(this.visibilite);
		}
	    // HEROS
	    
	    // MONSTRES
		for(int i = 0; i < NB_MONSTRES; i++) {
			p = trouvePositionVide();
			Monstre monstre = new Monstre(this, TypesM.getTypeMAlea(), "Kostine"+i, p);
			this.listeMonstres[nbMonstre++] = monstre;
			this.carte[p.getX()][p.getY()].occuper(monstre);;
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
			this.carte[p.getX()][p.getY()] = new Terrain(Terrain.TypeTerrain.EAU);
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
		this.carte[x_pont1][y_pont1] = new Terrain(TypeTerrain.PONT);
		this.carte[x_pont2][y_pont2] = new Terrain(TypeTerrain.PONT);	
	}
	
	private void riviereH(Position pos) {
		int x_pont1, x_pont2;
		int j = pos.getY() % 2;
		while (j < LARGEUR_CARTE*2) {
			Position p = new Position(j, pos.getY());
			this.carte[p.getX()][p.getY()] = new Terrain(Terrain.TypeTerrain.EAU);
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
		this.carte[x_pont1][pos.getY()] = new Terrain(TypeTerrain.PONT);
		this.carte[x_pont2][pos.getY()] = new Terrain(TypeTerrain.PONT);
	}
	// RIVIERE
	
	
	// ELEMENT
	public Terrain getCase(Position pos) {
		
		int x = pos.getX();
		int y = pos.getY();
		
		  if (x >= 0 && x < carte.length &&
			        y >= 0 && y < carte[0].length) {
			        return carte[x][y];
			    }
		
		System.out.println("Erreur getCase() : x = "+x+", y = "+y);
		return null;
	}
	
	public Soldat getSoldat(Position pos) {	
		Terrain t = this.getCase(pos);
	    if (t == null) {
	        return null;
	    }
	    return t.getOccupant();
	}
	
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
			
			if (y % 2 == 0) x -= x % 2;
			else x += (x+1) % 2;
			
			pos = new Position(x, y);	
			
		} while (!pos.estValide() || !this.getCase(pos).estLibre());
		
		return pos;
	}

	public Position trouvePositionVide(Position pos) { // utile?
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
			if (newp.estValide() && !this.carte[x][y].estLibre()) {
				listePos.ajouterPos(newp);
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
	
	/* public Heros trouveHeros(Position pos) {
		
		/* 
		 * Ameliorations possibles :
		 * - Comparer les pdv des heros trouves pour estimer quelles serait la meilleure cible
		 * - Prioriser un heros que le monstre peut tuer
		 * - Si aucun heros n'est trouve en melee ou a distance alors chercher le heros 
		 *   le plus proche ou qui a le moins de vie
		 * 
		 */
		
		// On regarde d'abord s'il y a des heros adjacents
		// Heros heros = this.trouveHerosMelee(pos);
		
		// Si aucun heros n'est trouvé alors on regarde les heros a distance
		/* if (heros == null) {
			heros = this.trouveHerosDistance(pos);
		} */
		
		// Si toujours aucun heros n'est trouve alors on cherche un heros sur la carte
		/* if (heros == null) {
			heros = this.trouveHeros();
		}
		
		return heros; */
	
	/* private Heros trouveHerosMelee(Position pos) {
		Heros[] HerosMelee = new Heros[6];
		int nbHerosMelee = 0;
		int i;
		int dx, dy;
		int [] coordsx = {-2, -1, -1, 1, 1, 2};
		int [] coordsy = {0, 1, -1, 1, -1, 0};
		
		// On regarde les 6 cases autour de pos
		for (i = 0 ; i < 6 ; i++) {
			dx = coordsx[i];
			dy = coordsy[i];
			Element e = this.carte[pos.getX() + dx][pos.getY() + dy];
			if (e.pos.estValide() && e != null && e instanceof Heros) {
				HerosMelee[nbHerosMelee++] = (Heros) e;
			}
		}
		
		if (nbHerosMelee == 0) { return null; }
		
		return HerosMelee[(int)(Math.random()*nbHerosMelee-1)];
	} */
	
	/*
	private Heros trouveHerosDistance(Position pos, Int portee) {
		int nbHerosDistance = 0;
		Heros[] HerosDistance = new Heros[NB_HEROS-4]; // 6 quand hexagones
		
		// Il faut d'abord savoir gerer les lignes de vue pour pouvoir gerer les heros a distance
		
		return null;
	}
	*/
	
	public int getNbTours() {
		return this.nbTours;
	}
	
	public void jouerSoldats() { // a quoi sert panneau jeu en param?
		// tour des heros vient de finir
		this.nbTours++;
		this.tourActuel = TOUR_MONSTRE;
		
		for (Monstre monstre : this.listeMonstres) {
			Heros heros = listeHeros[0];
			EnsemblePosition chemin = this.plusCourtChemin(monstre.getPos(), heros.getPos());
			int distanceHeros = chemin.getNbPos() - 1;
			System.out.println(" -> Debut du tour");
			
			// Le monstre cherche le heros le plus proche
			for (int i=1; i < this.nbHeros; i++) {
				Heros test = listeHeros[i];
				chemin = this.plusCourtChemin(monstre.getPos(), test.getPos());
				int distanceTest = chemin.getNbPos() - 1;
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
				
				EnsemblePosition newChemin = this.plusCourtChemin(posMonstre, posHeros);
				distanceHeros = newChemin.getNbPos() - 1;
				
				// Verifie la distance d'attaque
				if (distanceHeros <= monstre.getPortee()) {
					System.out.println(" - il peut attaquer, distance = "+distanceHeros+", portee = "+monstre.getPortee());
					peutAttaquer = true;
				}
				
				if (peutAttaquer) { // 1er cas : le heros est a portee du monstre
					System.out.println(" - Combat");
					peutAttaquer = monstre.combat(heros);
					if (!peutAttaquer) System.out.println(" - n'a aps pu combattre : puis = "+monstre.getPuissance()+", tir = "+monstre.getTir());
				} 
				
				if (!peutAttaquer || heros.estMort()){ // 2eme cas : le monstre doit se rapprocher de sa cible
					/*
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
					*/
					//if (monstre.getDeplacement()*i < newChemin.getNbPos()) {
					// si on ne vient pas de tuer le heros courant, et si on a bien trouvé un heros (si non alors distanceHeros = -1)
					if (!heros.estMort() && distanceHeros > 0) {
						System.out.println("SALUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUT");
						Position newPos;
						if (distanceHeros > monstre.getDeplacement()) {
							System.out.println("IFFFFFFFFF " + distanceHeros);
							newPos = newChemin.getPosition(monstre.getDeplacement() - 1);
						} else {
							System.out.println("ELSEEEEEEE " + distanceHeros);
							newPos = newChemin.getPosition(newChemin.getNbPos() - 2);
						}
						this.deplaceSoldat(newPos, monstre);
						monstre.seDeplace(newPos);
					} else { // si le monstre vient de tuer, il a fini son tour, peut-être temporaire mais idée quand même
						monstre.setAction(0);
					}
					//}
				}
			}
			
			System.out.println(" -> Fin du tour");
			
			// Lorsque le tour du monstre est termine on remet ses actions a 2
			monstre.setAction(2);
			
		}
		
		// tour des monstres vient de finir
		this.nbTours++;
		this.tourActuel = TOUR_HEROS;
		// on remet les actions à tous les héros
		this.resetActionsHeros();
	}
	// TOUR DES MONSTRES
	
	
	// ACTION SOLDAT (actionHeros a revoir surement)
	public boolean actionHeros(Position pos, Position pos2) {
		
		if (!pos.estValide() || !pos2.estValide()
			|| getSoldat(pos) == null
			|| !(getSoldat(pos) instanceof Heros)
			|| getSoldat(pos).getAction() <= 0) {
			return false;
		} 
		
		Heros heros = (Heros) (this.carte[pos.getX()][pos.getY()].getOccupant());
		Terrain caseCible = this.carte[pos2.getX()][pos2.getY()];
		
		// Deplacement si case vide
		if (caseCible.estLibre()) {
			this.deplaceSoldat(pos2, heros);
			// heros.ajouterAction(-1);
		}
		
		// Combat
		else if (caseCible.getOccupant() instanceof Monstre && pos.distance(pos2) <= heros.getTir()) {
			Monstre monstre = (Monstre) caseCible.getOccupant();
			heros.combat(monstre);
		}

		return true;
	}
	
	/* 
	 * A modifier si on decide de pouvoir deplacer un soldat via autre chose qu'un deplacement (ex : competence)
	 * Reduction et verif des point d'action a faire en dehors de la fonction si on change
	 */
	public boolean deplaceSoldat(Position pos, Soldat soldat) {
			int x = pos.getX();
			int y = pos.getY();
			EnsemblePosition ePos = soldat.zoneDeplacement();
		
			if(ePos.contient(pos) && carte[x][y].estLibre() && soldat.getAction() > 0) {
				int xSoldat = soldat.getPos().getX();
				int ySoldat = soldat.getPos().getY();
				
				this.carte[xSoldat][ySoldat].liberer();
				this.carte[pos.getX()][pos.getY()].occuper(soldat);
				soldat.seDeplace(pos);
				
				soldat.ajouterAction(-1);
				return true;
			}
			
		return false;
	}
	
	public void resetActionsHeros() {
		int i;
		for (i = 0 ; i < nbHeros ; i++) {
			listeHeros[i].setAction(2);
		}
	}	
	
	private boolean estFranchissable(Position p) {
		return this.getCase(p).getType().getAccessible()/* && !(this.getCase(p).getOccupant() instanceof Monstre)*/;
	}
	
	// algo qui reconstruit le chemin dans le bon sens (peut-être inutile ? Mais plus propre)
	public EnsemblePosition plusCourtChemin(Position debut, Position fin) {
		Position current = fin;
		Position [] path = new Position[500];
		Position [][] cameFrom = plusCourtCheminAux(debut, fin);
		int i = 0;
		if (cameFrom[fin.getY()][fin.getX()] == null) {
	        return new EnsemblePosition(0);
	    }
		while(!current.equals(debut)) {
			path[i++] = current;
			current = cameFrom[current.getY()][current.getX()];
		}
		path[i++] = debut;
		// on y met dans un EnsemblePosition dans le bon sens
		EnsemblePosition chemin = new EnsemblePosition(i);
		for (int j = i-1 ; j >= 0 ; j--) {
			chemin.ajouterPos(path[j]);
		}
		return chemin;
	}
	
	// algo qui trouve le chemin le plus court entre 2 soldats,
	// prend en compte les obstacles, mais pas les couts des terrains
	private Position [][] plusCourtCheminAux(Position debut, Position fin) {
		EnsemblePosition frontier = new EnsemblePosition(500);
		frontier.ajouterPos(debut);
		Position [][] cameFrom = new Position[HAUTEUR_CARTE][LARGEUR_CARTE*2];
		cameFrom[debut.getY()][debut.getX()] = debut;
		boolean continuer = true;
		
		while (!frontier.estVide() && continuer) {
			Position current = frontier.getPosition(0);
			frontier.retirerPremierePos();
			//System.out.println("CURRENT : " + current.getX() + " " + current.getY());
			
			if (current.equals(fin)) {
				continuer = false;
			} else {
				EnsemblePosition voisins = current.voisines();
				for (int i = 0 ; i < voisins.getNbPos() ; i++) {
					Position next = voisins.getPosition(i);
					if (estFranchissable(next)
						&& !(this.getSoldat(next) instanceof Monstre)
						&& (!(this.getSoldat(next) instanceof Heros) || next.equals(fin))
						&& cameFrom[next.getY()][next.getX()] == null) {
						frontier.ajouterPos(next);
						cameFrom[next.getY()][next.getX()] = current;
					}
				}
			}
		}
		return cameFrom;
	}
	
	/*
	private String afficherTesMorts(Position [][] cameFrom) {
		String s = "";
		for (int i = 0 ; i < LARGEUR_CARTE*2 ; i++) {
			for (int j = 0 ; j < HAUTEUR_CARTE ; j++) {
				if ((i+j) % 2 == 1 || cameFrom[i][j] == null) {
					s += "[(  ,  )]";
				} else {
					s += "[(";
					s += cameFrom[i][j].getX();
					s += ",";
					s += cameFrom[i][j].getY();
					s += ")]";
				}
			}
			s += "\n";
		}
		return s;
	}
	*/
	
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
			
			this.carte[soldat.getPos().getX()][soldat.getPos().getY()].liberer();;
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
	
	// DESSIN
	public void toutDessiner(Graphics g, Position caseSurvolee, Position caseCliquee, Competence choisiComp) {
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				if ((i+j) % 2 == 1) {
					continue;
				}
				
				Position pos = new Position(i, j);
				Color couleur = COULEUR_VIDE;
				
				// /!\ IMPORTANT POUR LES TESTS /!\
				// Décommenter le && en-dessous si on veut tester la carte en voyant tout
				/* if (getVisibilite(pos) == 0
					&& getVisibilite(pos) == 1
					) {
					couleur = COULEUR_INCONNU;
				} */
				
				
				dessineCase(g, couleur, pos, false);
			}
		}
			
			// Zone de deplacement quand case survolee
			if (caseSurvolee != null 
				&& caseCliquee == null
				&& caseSurvolee.estValide() 
				&& getSoldat(caseSurvolee) != null) {
					
				dessineZoneDeplacement(g, getSoldat(caseSurvolee));
			}
				
			// Zone de deplacement quand case cliquee
			if (caseCliquee != null
				&& caseCliquee.estValide()
				&& getSoldat(caseCliquee) != null
				&& getSoldat(caseCliquee) instanceof Heros
				&& choisiComp == null) {
					
				dessineZoneDeplacement(g, getSoldat(caseCliquee));
				dessineCaseCliquee(g, caseCliquee);
			}
			
			// Zone des competence
			if (caseCliquee != null
					&& caseCliquee.estValide()
					&& getSoldat(caseCliquee) != null
					&& getSoldat(caseCliquee) instanceof Heros
					&& choisiComp != null) {
					
					dessinePorteeCompetence(g, choisiComp ,getSoldat(caseCliquee), caseSurvolee, caseCliquee);					
					dessineCaseCliquee(g, caseCliquee);
			}
				
		
		
		for(int i = 0; i < listeHeros.length; i++) {
			listeHeros[i].dessinSoldat(g, this);
		}
		
		for(int i = 0; i < listeMonstres.length; i++) {
			listeMonstres[i].dessinSoldat(g, this);
		}
	}
	
	
			
	public void dessineZoneDeplacement(Graphics g, Soldat soldat) {
		EnsemblePosition ePos = soldat.zoneDeplacement();	
	    
		for (int i = 0; i < ePos.getNbPos(); i++) {
			//g.drawImage(imgTerrainDeplacement, ePos.getPosition(i).getX()*NB_PIX_CASE /2, ePos.getPosition(i).getY()*NB_PIX_CASE*3/4, 20, 20, null);			
			this.dessineCase(g, new Color(212, 74, 105, 150), ePos.getPosition(i), true);
		}

	} 
	
	public void dessinePorteeCompetence(Graphics g, Competence competence, Soldat lanceur, Position caseSurvolee, Position caseCliquee) {
		EnsemblePosition ePos = lanceur.getPos().voisinesCroix(competence.getType().getDistance());
		EnsemblePosition zoneAtt = caseSurvolee.voisines(competence.getType().getDegatsZone());
		
		for (int i = 0; i < ePos.getNbPos(); i++) {
			Soldat soldat = (getSoldat(ePos.getPosition(i)));
			if(soldat != null) {
				if(soldat instanceof Heros) {
					this.dessineCase(g, Color.decode("#6B1818"), ePos.getPosition(i), true);
				}else {
					this.dessineCase(g, Color.decode("#403939"), ePos.getPosition(i), true);
				}
			}else {
				// penser a changer la couleur en fonction d'un spell degat / soin
				this.dessineCase(g, COULEUR_PORTEE_COMP, ePos.getPosition(i), true);
			}
		}
		
		if(caseSurvolee.estValide() && ePos.contient(caseSurvolee)){
			for(int j = 0; j < zoneAtt.getNbPos(); j++) {
				this.dessineCase(g, competence.typeCouleurAttaque(caseSurvolee) , zoneAtt.getPosition(j), true);
				
			}
		}
		
	}
			
			
	public void dessineCaseCliquee(Graphics g, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		
		Color couleur = new Color(100,0,0,20); // gestion de l'oppacité
		g.setColor(couleur);
		this.dessineInterieurHexagone(g, x/2, y, pos, null);
	}
	
	// prends un boolean en param pour savoir si c'est une cases en surbrillance ou pas
	public void dessineCase(Graphics g, Color couleur, Position pos, Boolean trans) {
		int x = pos.getX(),
			y = pos.getY();
		int offset_x = 0;
		x = x/2;
		if (y % 2 == 1) {
			offset_x = OFFSET_X;
		}
		
		g.setColor(couleur);
		if(trans == false) {
			this.dessineChoixTerrain(g, x, y, pos);
		}else {
			this.dessineInterieurHexagone(g, x, y, pos, null);
		}
		g.setColor(Color.BLACK);
		this.dessineContourHexagone(g, x, y);
		
		// Ajout des numeros 
		Soldat soldat = getSoldat(pos);
		g.setColor(Color.WHITE);
		if(soldat instanceof Monstre) {
			
			g.drawString("" + soldat.getNum(), x*NB_PIX_CASE + offset_x + NB_PIX_CASE/4, y*NB_PIX_CASE*3/4 + NB_PIX_CASE*3/4);
		}else if(soldat instanceof Heros) {
			char lettre = (char)('A' + soldat.getNum());
			
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
	
	// permet de choixir l'image au terrain associe 
	public void dessineChoixTerrain(Graphics g, int x, int y, Position pos) {
		if (carte[pos.getX()][pos.getY()].getType() == TypeTerrain.HERBE) {
			dessineInterieurHexagone( g,  x,  y,  pos,  imgTerrainHerbe);
		}else if  (carte[pos.getX()][pos.getY()].getType() == TypeTerrain.EAU) {
			dessineInterieurHexagone( g,  x,  y,  pos,  imgTerrainEau);
		}else if  (carte[pos.getX()][pos.getY()].getType() == TypeTerrain.FORET) {
			dessineInterieurHexagone( g,  x,  y,  pos,  imgTerrainForet);
		}else if  (carte[pos.getX()][pos.getY()].getType() == TypeTerrain.ROCHER) {
			dessineInterieurHexagone( g,  x,  y,  pos,  imgTerrainRocher);
		}else {
			dessineInterieurHexagone( g,  x,  y,  pos,  null);
		}
	}
	
	private void dessineInterieurHexagone(Graphics g, int x, int y, Position pos, Image imgTerrain) {
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
		
		if(imgTerrain == null) {
			g.fillPolygon(liste_x, liste_y, 6);
		}else {
			g.drawImage(imgTerrain, x*NB_PIX_CASE + offset_x, y*NB_PIX_CASE*3/4, 20, 20, null);

		}

	}

	
	
}


