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

/**
 * Représente la carte du wargame.
 * <p>
 * Elle gère les terrains, les soldats (héros et monstres),
 * la visibilité ainsi que le déroulement des tours.
 */

public class Carte implements IConfig, ICarte, Serializable {
	private Terrain[][] carte;
	private int[][] visibilite;
	
	private ArrayList<Heros> listeHeros;
	private ArrayList<Monstre> listeMonstres;
	
	private int nbTours = 0;
	private int tourActuel = 0;
	
	private ArrayList<String> combatLog = new ArrayList<String>();
	private int nbLog = 1;

	/** Initialise la carte du jeu.
	 * <p>
	 * La carte est remplie de terrains par défaut, des obstacles
	 * et des biomes sont générés aléatoirement.
	 * Les héros et les monstres sont également placés
	 * sur des positions libres.
	 */
	public Carte() {
		carte = new Terrain[LARGEUR_CARTE*2][HAUTEUR_CARTE];
		visibilite = new int[LARGEUR_CARTE*2][HAUTEUR_CARTE];
		
		listeHeros = new ArrayList<Heros>();
		listeMonstres = new ArrayList<Monstre>();
		
		// Remplissage de la matrice
		for(int i = 0; i < LARGEUR_CARTE*2; i++) {
			for(int j = 0; j < HAUTEUR_CARTE; j++) {
				this.carte[i][j] = new Terrain(Terrain.TypeTerrain.HERBE); // Herbe par defaut
				visibilite[i][j] = 0;
			}
		}
		
		// OBSTACLES
		zoneBiome(NB_SABLE, Terrain.TypeTerrain.SABLE, 2, 2);
		zoneBiome(NB_FORET, Terrain.TypeTerrain.FORET, 2, 4);
		zoneBiome(NB_FEU, Terrain.TypeTerrain.FEU, 2, 2);
		
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
			this.listeHeros.add(heros);
			this.carte[p.getX()][p.getY()].occuper(heros);
			this.visibilite = heros.setCasesVisibles(this.visibilite);
		}
	    // HEROS
	    
	    // MONSTRES
		for(int i = 0; i < NB_MONSTRES; i++) {
			Position p = trouvePositionVide();
			Monstre monstre = new Monstre(this, TypesM.getTypeMAlea(), "Kostine"+i, p);
			this.listeMonstres.add(monstre);
			this.carte[p.getX()][p.getY()].occuper(monstre);;
		}
		// MONSTRES			
	}

	
	// LOG DES COMBATS
	/**
	 * permet d'ajouter l'historique d'un combat au log
	 * @param msg a ajouter
	 */
	public void addCombatMessage(String msg) { combatLog.add(nbLog + " - " + msg); nbLog++; }
	public List<String> getCombatLog(){ return combatLog; }
	
	/**
	 * permet de nettoyer les logs en supprimant le contenue 
	 */
	public void clearCombatLog() { combatLog.clear(); }
	// LOG DES COMBATS
	 
	
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
	private void zoneBiome(int nb, Terrain.TypeTerrain type, int rayMin, int rayMax) {
		for (int i=0; i < nb; i++) {
			Position pos = trouvePositionVide();
			EnsemblePosition foret = pos.voisines((int) (Math.random() * (rayMax-rayMin) + rayMin), true);
			for (int j=0; j < foret.getNbPos(); j++) {
				Position posArbre = foret.getPosition(j);
				this.carte[posArbre.getX()][posArbre.getY()] = new Terrain(type);
			}
		}
	}

	
	// ELEMENTS
	/**
	 * permet de réccuperer le terrain correspondant a la position
	 * @param pos position de notre case
	 * @return le terrain a cette le position
	 */
	public Terrain getCase(Position pos) { // try catch 
		
		int x = pos.getX();
		int y = pos.getY();
		
		if (pos.estValide()) { return this.carte[x][y]; }
		
		//System.out.println("Erreur getCase() : x = "+x+", y = "+y);
		return null;
	}
	
	/***
	 * permet de reccuperer un soldat a une position donnée en parametre
	 * @param pos position de notre case
	 * @return renvoie le soldat a la position pos si il existe
	 */
	
	public Soldat getSoldat(Position pos) {	
		Terrain t = this.getCase(pos);
	    if (t == null) {
	        return null;
	    }
	    return t.getOccupant();
	}
	
	public int getNbHeros() { return listeHeros.size(); }
	public int getNbMonstre() { return listeMonstres.size(); }

	public ArrayList<Heros> getListeHeros(){ return listeHeros; }
	public ArrayList<Monstre> getListeMonstres(){ return listeMonstres; }
	
	public int getIndiceHeros(Heros heros) {
		int nbHeros = getNbHeros();
		for (int i = 0 ; i < nbHeros ; i++) {
			if (listeHeros.get(i).getNum() == heros.getNum()) {
				return i;
			}
		}
		return -1; // erreur
	}
	
	public int getIndiceMonstre(Monstre monstre) {
		int nbMonstre = getNbMonstre();
		for (int i = 0 ; i < nbMonstre ; i++) {
			if (listeMonstres.get(i).getNum() == monstre.getNum()) {
				return i;
			}
		}
		return -1; // erreur
	}
	
	// VISIBILITE
	/**
	 * Permet de savoir si le joueur a la vision sur cette positio ou non
	 * @param pos position de la case visible ou non
	 * @return renvoie si il est visible ou -1 sinon
	 */
	public int getVisibilite(Position pos) {
		if (pos.estValide()) {
			return this.visibilite[pos.getX()][pos.getY()];
		}
		//System.out.println("Erreur : getVisibilite :  0 <= x < " + LARGEUR_CARTE + " | 0 <= y < " + HAUTEUR_CARTE);
		return -1;
	}
	
	/**
	 * permet de rendre visible les zones autours des heros
	 */
	public void setVisibilite() {
		int nbHeros = getNbHeros();
		for (int i = 0 ; i < LARGEUR_CARTE*2 ; i++) {
			for (int j = 0 ; j < HAUTEUR_CARTE ; j++) {
				if (i+j % 2 == 1) {
					continue;
				}
				visibilite[i][j] = 0; // on reset tout
			}
		}
		for (int i = 0 ; i < nbHeros ; i++) {
			this.visibilite = listeHeros.get(i).setCasesVisibles(this.visibilite);
		}
	}
	
	
	// POSITION VIDE
	/**
	 * permet de trouver une position vide aléatoirement dans la carte
	 */
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
	/**
	 * permet de trouver une position vide aléatoirement dans la carte
	 * depuis une position donnée en param
	 * @param pos position où il faut regarder ses cases adjacentes
	 */
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
			if (newp.estValide() && !this.carte[x][y].estLibre()) {
				listePos.ajouterPos(newp);
			}
		}
		if (listePos.getNbPos() == 0) {
			return null;
		}
		return listePos.getPosition((int)(Math.random()*listePos.getNbPos())); // modif -1 retire
	}
	
	public int getNbTours() { return this.nbTours; }
	
	/**
	 * Permet de faire jouer les Monstres et de géré leur "IA"
	 */
	public void jouerSoldats() {	
		// tour des heros vient de finir
		this.nbTours++;
		this.tourActuel = TOUR_MONSTRE;
		
		// j'ai changé le for pour que ça marche (au lieu de Monstre monstre : this.listeMonstres)				
		for (int i = 0 ; i < listeMonstres.size() ; i++) {
			// On recupere le chemin le plus court vers le heros 0
			if (listeHeros.isEmpty()) return;
			Heros heros = listeHeros.get(0);
			Monstre monstre = listeMonstres.get(i);
			EnsemblePosition chemin = this.plusCourtChemin(monstre.getPos(), heros.getPos());
			int distanceHeros = chemin.getNbPos() - 1;
			System.out.println(" -> Debut du tour");
			
			// Le monstre cherche le heros le plus proche
			for (int j=0; j < listeHeros.size() ; j++) {
				Heros test = listeHeros.get(j);
				chemin = this.plusCourtChemin(monstre.getPos(), test.getPos());
				int distanceTest = chemin.getNbPos() - 1;
				if (distanceTest < distanceHeros) {
					heros = test;
					distanceHeros = distanceTest;
				}
			}
			
			System.out.println(monstre.getNom()+" "+monstre.getType()+" veut attaquer "+heros.getNom()+" "+heros.getType());
			
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
					//System.out.println("Je me rapproche");
					
					// si on ne vient pas de tuer le heros courant, et si on a bien trouvé un heros (si non alors distanceHeros = -1)
					if (!heros.estMort() && distanceHeros > 0) {
						Position newPos;
						if (distanceHeros > monstre.getDeplacement()) {
							newPos = newChemin.getPosition(monstre.getDeplacement());
						} else {
							newPos = newChemin.getPosition(newChemin.getNbPos() - 2); // -2 car avec -1 il peut se positionner SUR le héros
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
	
	// ACTION SOLDAT (actionHeros a revoir surement)
	
	
	/**
	 * Permet la gestion du deplacement et de l'affrontement
	 * @param pos est la position du heros jouer
	 * @param pos2 est la position où on veux se deplacer/attaquer
	 */
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
	
	/**
	 * permet de déplacer un soldat 
	 * @param pos position où l'on veux se deplacer
	 * @param soldat Le soldat qu'on veux deplacer
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
	/**
	 * Permet de géré tout evenement de fin de tour tel que 
	 * appliquer les effets des terrains
	 * faire jouer les monstres
	 * et reduire le cooldown des competences
	 */
	public void finTour() {
		// Effets appliques a la fin du tour des monstres
		appliquerEffets();
		
		// Tour des monstres
		jouerSoldats();
		decrementerCompSoldat();
	}
	
	private void decrementerCompSoldat() {
		for(Soldat sh : listeHeros) {
			sh.decrementerTempsRecharge();
		}
		for(Soldat sm : listeMonstres) {
			sm.decrementerTempsRecharge();
		}
	}
	
	/**
	 * Gère les effets des differents terrains sur les Soldats 
	 */
	public void appliquerEffets() {
		
		/*
		 * Ordre d'application :
		 * 1 - Ajout des effets des terrains
		 * 2 - Proc de la liste d'effets
		 * 3 - Reduction de la duree des effets
		 */
		
		// Recuperation de la liste de TOUT les soldats
		ArrayList<Soldat> listeSoldats = new ArrayList<Soldat>();
		listeSoldats.addAll(listeMonstres);
		listeSoldats.addAll(listeHeros);
		
		// Applications des effets
		for (Soldat s : listeSoldats) {
			int x = s.getPos().getX();
			int y = s.getPos().getY();
			TypeTerrain terrain = carte[x][y].getType();
			
			// 1 - Ajout des effets FIN_TOUR des terrains a la liste d'effets
			if (terrain.getMoment() == Terrain.TypeMoment.FIN_TOUR) {
				s.getListeEffets().ajouterEffet(new Effet(terrain.getEffet()));
			}
			
			// 2 - Application des effets de degats aux soldats
			s.setPointsActuels(s.getPointsActuels() + s.getListeEffets().sommeEffets(Effet.TCarAff.VIE));
			
			// 3 - Reduction de la duree des effets si vivant
			if (!s.estMort()) s.getListeEffets().majEffets();
			else mort(s);
		}
	}
	
	
	// FIN DU JEU
	/**
	 * Verifie si un camp ou l'autres a gagné
	 * @return renvoie -1 si perdu, 1 si gagné et 0 si ce n'est pas fini
	 */
	public int verifierFinJeu() {
		if (this.listeHeros.isEmpty()) return -1; // Perdu
		if (this.listeMonstres.isEmpty()) return 1; // Gagne
		return 0; // pas fini
	}
		
	
	public void resetActionsHeros() {
		for (Heros h : listeHeros) {
			h.setAction(2); // Remplacer par NB_ACTION_INITIAL
		}
	}
	
	public void soignerHeros() {
		for (Heros h : listeHeros) {
			h.ajouterPv(5*h.getAction());
		}
	}	
	
	private boolean estFranchissable(Position p) {
		return this.getCase(p).getType().getAccessible()/* && !(this.getCase(p).getOccupant() instanceof Monstre)*/;
	}
	
	// algo qui reconstruit le chemin dans le bon sens (peut-être inutile ? Mais plus propre)
	/**
	 * Permet de trouver le chemins le plus court pour l'ia des Monstres
	 * tout en esquivant les obstacle
	 * @param debut position d'où part le monstre
	 * @param fin position où doit arriver le monstre
	 * @return renvoie les positions utilisé pour le plus court chemin
	 */
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
	
	// MORT
	
	/**
	 * Vérifie si un soldat est mort, si c'est le cas alors on le supprime du jeu
	 */
	public void mort(Soldat soldat) {
		if (soldat.getPointsActuels() <= 0) {
			// Suppression du soldat de la liste correspondante
			if (soldat instanceof Heros) listeHeros.remove(soldat);
			else listeMonstres.remove(soldat);
			// Suppression du soldat de la carte
			this.carte[soldat.getPos().getX()][soldat.getPos().getY()].liberer();
		}
	}
	// MORT
	

	// COORTOPOS
	/**
	 * Permet de convertir la position en pixel de la souris en position de matrice x y
	 * @param cx pixel sur l'axe x
	 * @param cy pixel sur l'axe y
	 * @return renvoie la position dans la matrice terrain
	 */
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
}
	



