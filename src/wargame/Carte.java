package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

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
		
		// Remplissage de la matrice par de l'herbe
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				this.carte[i][j] = new Terrain(Terrain.TypeTerrain.HERBE); // Herbe par defaut
				visibilite[i][j] = 0;
			}
		}
		
		// OBSTACLES
		zoneBiome(NB_SABLE, Terrain.TypeTerrain.SABLE, 2);
		zoneBiome(NB_FORET, Terrain.TypeTerrain.FORET, 4);
		
		// Placement des terrains aléatoires
		for(int i = 0; i < NB_OBSTACLES; i++) {
			Position p = trouvePositionVide();
			this.carte[p.getX()][p.getY()] = new Terrain(Terrain.TypeTerrain.getTerrainAlea());
			
		}
		
		riviere(NB_RIVIERE);
		// OBSTACLES
		
		// HEROS
	    for(int i = 0; i < NB_HEROS; i++) {
			Position p = trouvePositionVide();
			Heros heros = new Heros(this, TypesH.getTypeHAlea(), "Goat"+i, p);
			this.listeHeros[nbHeros++] = heros;
			this.carte[p.getX()][p.getY()].occuper(heros);
			this.visibilite = heros.setCasesVisibles(this.visibilite);
		}
	    // HEROS
	    
	    // MONSTRES
		for(int i = 0; i < NB_MONSTRES; i++) {
			Position p = trouvePositionVide();
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
	 
	
	// OBSTACLES
	// RIVIERE
	private void riviere(int nb_riviere) {
		for (int i=0; i < nb_riviere; i++) {
			Position pos = trouvePositionVide();
			int r = (int) (Math.random() * 2);
			switch(r) {
				case 0: riviereV(pos); break;
				case 1: riviereH(pos); break;
			}
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
		if (y_pont1 % 2 == pos.getY() % 2) {
			x_pont1 = pos.getX();
		} else {
			if (pos.getX() % 2 == 0) x_pont1 = pos.getX() + 1;
			else x_pont1 = pos.getX() - 1;
		}
		if (y_pont2 % 2 == pos.getY() % 2) {
			x_pont2 = pos.getX();
		} else {
			if (pos.getX() % 2 == 0) x_pont2 = pos.getX() + 1;
			else x_pont2 = pos.getX() - 1;
		}
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
	
	private void zoneBiome(int nb, Terrain.TypeTerrain type, int rayMax) {
		for (int i=0; i < nb; i++) {
			Position pos = trouvePositionVide();
			EnsemblePosition foret = pos.voisines((int) (Math.random() * rayMax-1 + 1), true);
			for (int j=0; j < foret.getNbPos(); j++) {
				Position posArbre = foret.getPosition(j);
				this.carte[posArbre.getX()][posArbre.getY()] = new Terrain(type);
			}
		}
	}
	// OBSTACLES
	
	
	// ELEMENT
	public Terrain getCase(Position pos) { // try catch 
		
		int x = pos.getX();
		int y = pos.getY();
		
		if (pos.estValide()) { return this.carte[x][y]; }
		
		System.out.println("Erreur getCase() : x = "+x+", y = "+y);
		return null;
	}
	
	public Soldat getSoldat(Position pos) {	
		return this.getCase(pos).getOccupant();
	}
	
	// FIN DU JEU
	public int verifierFinJeu() {
		if (this.nbHeros == 0) { // IA a gagné
			return -1;
		} else {
			if (this.nbMonstre == 0) { // joueur a gagné
				return 1;
			}
		}
		return 0; // pas fini
	}
	
	// VISIBILITE
	public int getVisibilite(Position pos) {
		if (pos.estValide()) {
			return this.visibilite[pos.getX()][pos.getY()];
		}
		System.out.println("Erreur : getVisibilite :  0 <= x < " + LARGEUR_CARTE + " | 0 <= y < " + HAUTEUR_CARTE);
		return -1;
	}
	
	public void setVisibilite() {
		for (int i = 0 ; i < LARGEUR_CARTE*2 ; i++) {
			for (int j = 0 ; j < HAUTEUR_CARTE ; j++) {
				if (i+j % 2 == 1) {
					continue;
				}
				visibilite[i][j] = 0; // on reset tout
			}
		}
		for (Heros heros : this.listeHeros) {
			this.visibilite = heros.setCasesVisibles(this.visibilite);
		}
	}
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
	
	public void jouerSoldats() {
		// tour des heros vient de finir
		this.nbTours++;
		this.tourActuel = TOUR_MONSTRE;
		
		// j'ai changé le for pour que ça marche (au lieu de Monstre monstre : this.listeMonstres)
		for (int i = 0 ; i < this.nbMonstre ; i++) {
			Monstre monstre = listeMonstres[i];
			Heros heros = listeHeros[0];
			EnsemblePosition chemin = this.plusCourtChemin(monstre.getPos(), heros.getPos());
			int distanceHeros = chemin.getNbPos() - 1;
			System.out.println(" -> Debut du tour");
			
			// Le monstre cherche le heros le plus proche
			for (int j=0; j < this.nbHeros; j++) {
				Heros test = listeHeros[j];
				chemin = this.plusCourtChemin(monstre.getPos(), test.getPos());
				int distanceTest = chemin.getNbPos() - 1;
				if (distanceTest < distanceHeros) {
					heros = test;
					distanceHeros = distanceTest;
				}
			}
			
			System.out.println(monstre.getNom()+" veut attaquer "+heros.getNom());
			
			// Tant qu'il reste des actions au monstre il regarde s'il peut attaquer, sinon il avance
			while(monstre.getAction() > 0 && !monstre.estMort()) {
				//System.out.println(" -> PA = "+monstre.getAction()+"actions");
				//System.out.println("Portee du monstre : " + monstre.getPortee());
				
				/*
				 * 2 cas : 
				 * - Le monstre peut attaquer le heros
				 * - Le monstre ne peut pas attaquer le heros
				 * Amelioration possible : le monstre evalue s'il devrait l'attaquer plutot en melee ou a distance
				 */
				
				Position posHeros = heros.getPos();
				Position posMonstre = monstre.getPos();
				boolean peutAttaquer = false;
				
				EnsemblePosition newChemin = this.plusCourtChemin(posMonstre, posHeros);
				distanceHeros = newChemin.getNbPos() - 1;
				
				// Verifie la distance d'attaque
				if (distanceHeros <= monstre.getTir() || distanceHeros == 1) {
					System.out.println(" - il peut attaquer, distance = "+distanceHeros+", portee = "+monstre.getTir());
					peutAttaquer = true;
				}
				
				if (peutAttaquer && !heros.estMort()) { // 1er cas : le heros est a portee du monstre
					System.out.println(" - Combat");
					peutAttaquer = monstre.combat(heros);
					
					if (!peutAttaquer) System.out.println(" - n'a aps pu combattre : puis = "+monstre.getPuissance()+", tir = "+monstre.getTir());
				} 
				
				if (!peutAttaquer || heros.estMort()){ // 2eme cas : le monstre doit se rapprocher de sa cible
					System.out.println("Je me rapproche");
					
					// si on ne vient pas de tuer le heros courant, et si on a bien trouvé un heros (si non alors distanceHeros = -1)
					if (!heros.estMort() && distanceHeros > 0) {
						Position newPos;
						if (distanceHeros > monstre.getDeplacement()) {
							newPos = newChemin.getPosition(monstre.getDeplacement() - 1);
						} else {
							newPos = newChemin.getPosition(newChemin.getNbPos() - 2);
						}
						this.deplaceSoldat(newPos, monstre);
						monstre.seDeplace(newPos);
					} else { // si le monstre vient de tuer, il a fini son tour, peut-être temporaire mais idée quand même
						monstre.setAction(0);
					}
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
		this.soignerHeros();
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
	
	// FIN TOUR
	public void finTour() {
		
		
	}
	// FIN TOUR
	
	public void resetActionsHeros() {
		int i;
		for (i = 0 ; i < nbHeros ; i++) {
			listeHeros[i].setAction(2); // Remplacer par NB_ACTION_INITIAL
		}
	}
	
	public void soignerHeros() {
		int i;
		for (i = 0 ; i < nbHeros ; i++) {
			listeHeros[i].ajouterPv(5*listeHeros[i].getAction());
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
	// ACTION SOLDAT
	
	
	// MORT
	public void mort(Soldat soldat) {
		System.out.println("je suis appelé avec " + soldat.getClass().getSimpleName());
		System.out.println("MES PV : " + soldat.getPointsActuels());
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
						//listeHeros[i+1] = null;
					}
				}
			} else {
				this.nbMonstre--;
				boolean trouve = false;
				for (int i=0; i < nbMonstre; i++) {
					System.out.println("JE MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEURS");
					if (listeMonstres[i].getPos().equals(soldat.getPos())) {
						trouve = true;
					}
					if (trouve) {
						listeMonstres[i] = listeMonstres[i+1];
						//listeMonstres[i+1] = null;
					}
				}
			}	
			
			this.carte[soldat.getPos().getX()][soldat.getPos().getY()].liberer();
		}
	}
	// MORT
	
	
	// COORTOPOS
	public Position coorToPos(int cx, int cy) {
		Position test = coorToPosRect(cx, cy);
		// on teste si en décalant vers le haut d'1/4 d'hexa ça reste à la même pos
		Position test2 = coorToPosRect(cx, cy-NB_PIX_CASE/4);
		if (test.equals(test2)) {
			return test;
		} else {
			int i;
			int x = test.getX(),
				y = test.getY();
			// pour les coords des triangles je pars des côtés, puis centre, puis bas
			int [] t1_x = {x*NB_PIX_CASE/2, x*NB_PIX_CASE/2+NB_PIX_CASE/2, x*NB_PIX_CASE/2};
			int [] t1_y = {y*NB_PIX_CASE*3/4, y*NB_PIX_CASE*3/4, y*NB_PIX_CASE*3/4+NB_PIX_CASE/4};
			int [] t2_x = {x*NB_PIX_CASE/2+NB_PIX_CASE, x*NB_PIX_CASE/2+NB_PIX_CASE/2, x*NB_PIX_CASE/2+NB_PIX_CASE};
			int [] t2_y = {y*NB_PIX_CASE*3/4, y*NB_PIX_CASE*3/4, y*NB_PIX_CASE*3/4+NB_PIX_CASE/4};
			// petit décalage vers le haut, ça correspond mieux à là où on clique
			for (i = 0 ; i < 3 ; i++) {
				t1_y[i]--;
				t2_y[i]--;
			}
			// verif dans triangle 1, puis si dedans alors x-=1 et y-=1
			if (estDansTriangle(t1_x, t1_y, cx, cy)) {
				test.setX(test.getX() - 1);
				test.setY(test.getY() - 1);
			}
			// verif dans triangle 2, puis si dedans alors x+=1 et y-=1
			if (estDansTriangle(t2_x, t2_y, cx, cy)) {
				test.setX(test.getX() + 1);
				test.setY(test.getY() - 1);
			}
			// si dans aucun alors on change pas
			return test;
		}
	}
	
	private Position coorToPosRect(int x, int y) {
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
	
	private boolean estDansTriangle(int [] tx, int [] ty, int x, int y) {
		double A = aire(tx[0], ty[0], tx[1], ty[1], tx[2], ty[2]);
		double A1 = aire(x, y, tx[1], ty[1], tx[2], ty[2]);
		double A2 = aire(tx[0], ty[0], x, y, tx[2], ty[2]);
		double A3 = aire(tx[0], ty[0], tx[1], ty[1], x, y);
		return (A == A1+A2+A3);
	}
	
	private double aire (int x1, int y1, int x2, int y2, int x3, int y3) {
		return Math.abs( ( x1 * (y2-y3)
				 		 + x2 * (y3-y1)
				 		 + x3 * (y1-y2)) / 2.0 );
	}
	// COORTOPOS
	
	
	// DESSIN
	public void toutDessiner(Graphics g, Position caseSurvolee, Position caseCliquee, Competence choisiComp) {
		
		setVisibilite(); // permet de mettre à jour les cases visibles des Heros dès qu'on refresh
		
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				if ((i+j) % 2 == 1) {
					continue;
				}
				
				Position pos = new Position(i, j);
				Color couleur = COULEUR_VIDE;
				
				if (getSoldat(pos) == null) {
					couleur = carte[i][j].getType().getCouleur();
				} else {
					Soldat soldat = carte[i][j].getOccupant();
					if (soldat instanceof Heros) couleur = COULEUR_HEROS;
					if (soldat instanceof Monstre) couleur = COULEUR_MONSTRES;
				}
				
				dessineCase(g, couleur, pos);
				
				
				
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
				
		}
		
	}
	
			
	public void dessineZoneDeplacement(Graphics g, Soldat soldat) {
		EnsemblePosition ePos = soldat.zoneDeplacement();
		if (getVisibilite(soldat.getPos()) == 1) { // pour les Monstres, on affiche sa zone seulement si le Monstre est visible
			for (int i = 0; i < ePos.getNbPos(); i++) {
				Position caseVisible = ePos.getPosition(i);
				// on dessine la case seulement si elle est visible
				// /!\ à voir si on change pour faire en sorte qu'on ne puisse pas se déplacer dans une case invisible
				if (getVisibilite(caseVisible) == 1) {				
					this.dessineCase(g, Color.PINK, caseVisible);
				}
			}
		}
	}
	
	public void dessinePorteeCompetence(Graphics g, Competence competence, Soldat lanceur, Position caseSurvolee, Position caseCliquee) {
		EnsemblePosition ePos = lanceur.getPos().voisinesCroix(competence.getType().getDistance());
		EnsemblePosition zoneAtt = caseSurvolee.voisines(competence.getType().getDegatsZone(), false);
		
		for (int i = 0; i < ePos.getNbPos(); i++) {
			Soldat soldat = (getSoldat(ePos.getPosition(i)));
			if(soldat != null) {
				if(soldat instanceof Heros) {
					this.dessineCase(g, Color.decode("#6B1818"), ePos.getPosition(i));
				}else {
					this.dessineCase(g, Color.decode("#403939"), ePos.getPosition(i));
				}
			}else {
				// penser a changer la couleur en fonction d'un spell degat / soin
				this.dessineCase(g, COULEUR_PORTEE_COMP, ePos.getPosition(i));
			}
		}
		
		if(caseSurvolee.estValide() && ePos.contient(caseSurvolee)){
			for(int j = 0; j < zoneAtt.getNbPos(); j++) {
				this.dessineCase(g, competence.typeCouleurAttaque(caseSurvolee) , zoneAtt.getPosition(j));
				
			}
		}
		
	}
			
			
	public void dessineCaseCliquee(Graphics g, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		Color couleur = new Color(100,0,0,20); // gestion de l'oppacité
		g.setColor(couleur);
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
		
		if (getVisibilite(pos) == 1) {
			g.setColor(couleur);
		} else {
			g.setColor(COULEUR_INCONNU);
			g.setColor(couleur); //VISIBILITE DECOMMENTER POUR TESTS
		}
		this.dessineInterieurHexagone(g, x, y);
		g.setColor(Color.BLACK);
		this.dessineContourHexagone(g, x, y);
		
		// Ajout des numeros 
		Soldat soldat = getSoldat(pos);
		g.setColor(Color.WHITE);
		if(soldat instanceof Monstre && getVisibilite(pos) == 1) {
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
	
	private void dessineBrouillard(Graphics g, Position pos) {
		int x = pos.getX(),
			y = pos.getY();
		x = x/2;
		Color couleur_base = g.getColor();
		g.setColor(COULEUR_INCONNU);
		dessineInterieurHexagone(g, x, y);
		g.setColor(couleur_base); // juste pour remettre la couleur qu'on avait avant l'appel
	}
	
	public void dessineInfosBas(Graphics g) {
		int i = 0;
		// heros
		for (int j = 0 ; j < this.nbHeros ; j++) {
			Heros heros = listeHeros[j];
			if (!heros.estMort()) {				
				double pv_max = heros.getPoints();
				double pv_act = heros.getPointsActuels();
				double ratio = (pv_act / pv_max) * 100;
				double taille = (pv_act / pv_max) * 50 + 1;
				
				if (ratio >= 50) {
					g.setColor(Color.GREEN);
				} else if (ratio < 15) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.ORANGE);
				}
				g.fillRect(51+i, 16, (int) taille, 12);
				g.drawString("" + heros.getNum(), 35+i, 25);
								
				Image soldat;
				if (heros.getType() == TypesH.ELF) {
					soldat = new ImageIcon("./images/persos/elfe_map.png").getImage();
				} else if (heros.getType() == TypesH.NAIN){
					soldat = new ImageIcon("./images/persos/nain_map.png").getImage();
				} else if (heros.getType() == TypesH.HUMAIN){
					soldat = new ImageIcon("./images/persos/humain_map.png").getImage();
				} else if (heros.getType() == TypesH.HOBBIT){
					soldat = new ImageIcon("./images/persos/hobbit_map.png").getImage();
				} else {
					soldat = new ImageIcon("./images/eau.png").getImage();
				}
				Image barre = new ImageIcon("./images/barre_de_vie_bas.png").getImage();
				g.drawImage(soldat, 10+i, 10, 20, 20, null); // à changer pour verif quel soldat c'est (et adapter l'image)
				g.drawImage(barre, 45+i, 10, 62, 24, null);
				
				i += 110; // décalage vers la droite
			}
		}
		// monstre (pas fonctionnel à 100%, il faudrait une scrollbar
		/*
		i = 0;
		System.out.println("NB MONSTRE = " + this.nbMonstre);
		for (int j = 0 ; j < this.nbMonstre ; j++) {
			Monstre monstre = listeMonstres[j];
			if (!monstre.estMort()) {
				Image soldat = new ImageIcon("./images/elfe_1.png").getImage();
				Image barre = new ImageIcon("./images/barre_de_vie_bas.png").getImage();
				g.drawImage(soldat, 10+i, 60, 20, 20, null);
				g.drawImage(barre, 35+i, 60, 54, 20, null);
				
				double pv_max = monstre.getPoints();
				double pv_act = monstre.getPointsActuels();
				double ratio = (pv_act / pv_max) * 100;
				double taille = (pv_act / pv_max) * 46;
				
				if (ratio >= 50) {
					g.setColor(Color.GREEN);
				} else if (ratio < 15) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.ORANGE);
				}
				g.fillRect(39+i, 64, (int) taille, 12);
				g.drawString("" + monstre.getNum(), 100+i, 60);
				i += 100;
			}
		}
		*/
	}
	// DESSIN
	
	
}


