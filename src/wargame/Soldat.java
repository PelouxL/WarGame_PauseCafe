package wargame;

public abstract class Soldat extends Element implements ISoldat{
	private final int POINT_DE_VIE, PUISSANCE, TIR, PORTEE_VISUELLE, DEPLACEMENT = 8;
	private int pointsDeVie;
	private Position pos;
	private Carte carte;
	private int action = 2; // nb d'action possible pour un soldat par tour
	private int tour;
	
	
	private static int compteurHeros = 0;
	private static int compteurMonstre = 1;
	private final int NUM;
	
	public Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINT_DE_VIE = pointsDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
		
		if(this instanceof Heros) {
			NUM = compteurHeros++;
			System.out.println("Création d'un Héros - Numéro: " + NUM + " - Compteur Héros: " + compteurHeros);
		}else if( this instanceof Monstre) {
			NUM = compteurMonstre++;
			 System.out.println("Création d'un Monstre - Numéro: " + NUM + " - Compteur Monstre: " + compteurMonstre);
		}else {
			NUM = -1;
		}
	}
	
	public int getNum() {
		return this.NUM;
	}
	
	// POSITION
	public Position getPos() {
		return this.pos;
	}
	
	public void setPos(Position pos) {
		this.pos = pos;
	}
	// POSITION
	
	// TOUR
	public int getTour() {
		return this.tour;
	}

	public void setTour() {
		if (this.tour == 1) {
			this.tour = 0;
		}
		this.tour = 1;
	}
	
	public void joueTour(int tour) { // on sait pas ce que ça fait ???
    	
    }
	// TOUR

	// POINTS DE VIE
	public int getPoints() {
		return this.POINT_DE_VIE;
	}

	public int getPointsActuels() {
		return this.pointsDeVie;
	}
	
	public void setPointsActuels(int nouveauPoints) {
		this.pointsDeVie = nouveauPoints;
	}
	// POINTS DE VIE
	
	// PORTEE
	public int getPortee() {
		return this.PORTEE_VISUELLE;
	}
	
	public int[][] setCasesVisibles(int[][] visibilite) {
		int i, j;
		int x = this.pos.getX();
		int y = this.pos.getY();
		int portee = this.getPortee();
		for (i = -portee ; i <= portee ; i++) {
			for (j = -portee ; j <= portee ; j++) {
				if (i + x >= 0 && i + x < Carte.LARGEUR_CARTE
				  && j + y >= 0 && j + y < Carte.HAUTEUR_CARTE) {
					visibilite[i+x][j+y] = 1;
				}
			}
		}
		return visibilite;
	}
	// PORTEE
	
	// PUISSANCE
	public int getPuissance() {
		return this.PUISSANCE;
	}
	// PUISSANCE
	
	// TIR
	public int getTir() {
		return this.TIR;
	}
	//TIR
	
	// ACTION
	public int getAction() {
		return this.action;
	}
	// ACTION
	public void setAction(int action) {
		this.action = action;
	}
	// COMBAT
	public void combat(Soldat soldat) {
		Carte carte = this.carte;
		
		// Combat en melee : l'adversaire rend les coups
		// Combat a distance : l'adversaire ne rend pas les coups
		if (this.pos.adjacent(soldat.pos)) {
			this.combatMelee(soldat);
		} else {
			this.combatDistance(soldat);
		}
		
		// On retire les soldats de la carte si ils sont morts
		carte.mort(this);
		carte.mort(soldat);
	} 
	
	private void combatMelee(Soldat soldat) {
		Carte carte = this.carte;
		String msgLog = null;
		
		int dgts_atq = this.getPuissance();
		int pv_def = soldat.getPointsActuels();
		soldat.setPointsActuels(pv_def - dgts_atq);
	
		// ------------- GESTION DES LOGS -------------
		msgLog = this.getClass().getSimpleName() + " ";
		if(this instanceof Monstre) {
			 msgLog += this.getNum()  + " : ";
			 
		}else if(this instanceof Heros) {
			char lettre = (char)('A' + this.getNum());
			
			msgLog += lettre + " : ";
		}
		msgLog += " attaque en melee "+ soldat.getClass().getSimpleName() +
				" pour " + dgts_atq + " dégats !\n";
		// ------------- GESTION DES LOGS -------------
		
		
		pv_def = soldat.getPointsActuels();
		if (pv_def > 0) {
			int dgts_def = soldat.getPuissance();
			int pv_atq = this.getPointsActuels();
			this.setPointsActuels(pv_atq - dgts_def);
			
			
			
			// ------------- GESTION DES LOGS -------------
			if(soldat instanceof Monstre) {
				msgLog += soldat.getClass().getSimpleName() + " " + ((Monstre)soldat).getNum()  + " : ";
			}else if(soldat instanceof Heros) {
				char lettre = (char)('A' + (soldat).getNum());
				msgLog += soldat.getClass().getSimpleName() + " " + lettre + " : ";
			}
			msgLog +=  " riposte et inflige " + dgts_def + " dégâts!";
		}else {
			msgLog += " Il a tué(e) sa cible sans riposte !";
		}
		carte.addCombatMessage(msgLog);
		// ------------- GESTION DES LOGS -------------
	}
	
	private void combatDistance(Soldat soldat) {
		Carte carte = this.carte;
		
		int dgts_atq = this.getTir();
		int pv_def = soldat.getPointsActuels();
		soldat.setPointsActuels(pv_def - dgts_atq);
		carte.addCombatMessage(this.getClass().getSimpleName() + " attaque en melee "+ soldat.getClass().getSimpleName() +
				" pour " + dgts_atq + " dégats !");
		
	}
	// COMBAT

	// DEPLACEMENT
	public int getDeplacement() {
		return this.DEPLACEMENT;
	}
	
	public EnsemblePosition zoneDeplacement() {
		int nbPosMax = 2*this.DEPLACEMENT*(this.DEPLACEMENT+1);
		EnsemblePosition ePos = new EnsemblePosition(nbPosMax);
		
		this.zoneDeplacementAux(this.pos, this.pos, this.DEPLACEMENT, ePos);
		// Penser a retirer la pos initiale de la liste
		
		return ePos;
	}

	private void zoneDeplacementAux(Position posInit, Position pos, int deplacement, EnsemblePosition ePos) {
		
		if (!(pos.estValide())) {
			return;
		}
		
		Element e = this.carte.getElement(pos);
		
		if (deplacement <= -1 
			|| e instanceof Obstacle
			|| (this instanceof Heros && e instanceof Monstre)
			|| (this instanceof Monstre && e instanceof Heros)
			) {
			return;
		}

		if (!(ePos.contient(pos)) && this.carte.caseDisponible(pos)) {
			ePos.ajouterPos(pos);
		}
		
		int x = pos.getX();
		int y = pos.getY();
		
		// Droite
		this.zoneDeplacementAux(posInit, new Position(x+1, y), deplacement-1, ePos);
		// Bas
		this.zoneDeplacementAux(posInit, new Position(x, y+1), deplacement-1, ePos);
		// Gauche
		this.zoneDeplacementAux(posInit, new Position(x-1, y), deplacement-1, ePos);
		// Haut
		this.zoneDeplacementAux(posInit, new Position(x, y-1), deplacement-1, ePos);
	}
	
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.pos.setX(newPos.getX());
			this.pos.setY(newPos.getY());
			//this.setCasesVisibles();
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}
	
	public String toString() {
		String chaine = ""+this.getClass().getSimpleName()+": Position :"+pos+",  PV : ("+this.getPoints()+"\\"+this.getPointsActuels()+"),  Capacité de déplacement : "+DEPLACEMENT;
		
		if(action > 0) {
			chaine+=",  action(s) restante : "+action;
			
		}else {
			chaine+=", plus d'actions restante";
		}
		return chaine;
	}
	// DEPLACEMENT
}
