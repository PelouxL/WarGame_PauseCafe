package wargame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite représentant un soldat du jeu.
 * <p>
 * Un soldat peut être un héros ou un monstre. Il possède des points de vie,
 * une position sur la carte, des capacités de combat, de déplacement
 * et un ensemble de compétences et d'effets temporaires.
 * <p>
 * Cette classe centralise toute la logique commune aux unités.
 */
public abstract class Soldat implements ISoldat, IConfig,  ICompetence, Serializable{

	private final int POINT_DE_VIE, PUISSANCE, TIR, PORTEE_VISUELLE, DEPLACEMENT, NB_ACTION_MAX;

	private int pointsDeVie;
	private Position pos;
	private Carte carte;
	private int action = 2; // nb d'action possible pour un soldat par tour
	private int tour; // On enleve?
	private ListeEffets listeEffets;
	
	private static int nbHeros = 0;
	private static int nbMonstre = 0;
	private final int NUM;
	
	private List<Competence> Competences = new ArrayList<>();
	
	/**
	 * Construit un soldat avec ses caractéristiques initiales.
	 *
	 * @param carte carte sur laquelle le soldat est placé
	 * @param pts points de vie maximum
	 * @param portee portée visuelle
	 * @param puiss puissance en combat au corps à corps
	 * @param tir puissance de tir à distance
	 * @param dep distance de déplacement
	 * @param pos position initiale du soldat
	 */
	public Soldat(Carte carte, int pts, int portee, int puiss, int tir, int dep, Position pos) {
		POINT_DE_VIE = pointsDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		DEPLACEMENT = dep;
		NB_ACTION_MAX = action;
		listeEffets = new ListeEffets();
		this.carte = carte;
		this.pos = pos;
		
		if(this instanceof Heros) {
			NUM = nbHeros++;
		}else if(this instanceof Monstre) {
			NUM = nbMonstre++;
		}else {
			NUM = -1;
		}
		
		
	}
	
	// Polymorphisme des competences
	protected abstract void initialiserCompetence(); 
	// 
	
	// POINTS DE VIE
	/**
	 * Retourne les points de vie maximum du soldat.
	 *
	 * @return points de vie maximum
	 */
	public int getPoints() { return this.POINT_DE_VIE; }
	
	/**
	 * Retourne les points de vie actuels.
	 *
	 * @return points de vie actuels
	 */
	public int getPointsActuels() { return this.pointsDeVie; }
	
	/**
	 * Modifie les points de vie actuels.
	 *
	 * @param pointsDeVie nouvelle valeur
	 */
	public void setPointsActuels(int pointsDeVie) { this.pointsDeVie = pointsDeVie; }
	
	/**
	 * Retire des points de vie au soldat.
	 *
	 * @param degats nombre de points retirés
	 */
	public void retirerPv(int degats) { this.pointsDeVie -= degats; }
	
	/**
	 * Ajoute des points de vie sans dépasser le maximum.
	 *
	 * @param soin nombre de points ajoutés
	 */
	public void ajouterPv(int soin) {
		this.pointsDeVie += soin; 
		if (this.pointsDeVie > this.POINT_DE_VIE) this.pointsDeVie = this.POINT_DE_VIE;
	}
	
	/**
	 * Indique si le soldat est mort.
	 *
	 * @return true si les points de vie sont inférieurs ou égaux à zéro
	 */
	public boolean estMort() { return this.pointsDeVie <= 0; }
	// POINTS DE VIE
	
	
	// DEGATS
	
	/**
	 * Retourne la puissance totale du soldat en tenant compte des effets.
	 *
	 * @return puissance effective
	 */
	public int getPuissance() { 
		int puissance = this.PUISSANCE + listeEffets.sommeEffets(Effet.TCarAff.PUISSANCE); 
		if (puissance >= 0) return puissance;
		return 0; 
	}
	
	/**
	 * Retourne la valeur de tir du soldat.
	 *
	 * @return puissance de tir
	 */
	public int getTir() { return this.TIR; }
	// DEGATS
	
	
	// NUM
	public int getNum() { return this.NUM; }
	// NUM
	
	
	// POSITION
	/**
	 * Retourne la position actuelle du soldat.
	 *
	 * @return position du soldat
	 */
	public Position getPos() { return this.pos; }
	
	/**
	 * Modifie la position du soldat.
	 *
	 * @param pos nouvelle position
	 */
	public void setPos(Position pos) { this.pos = pos; }
	// POSITION
	
	
	// TOUR/ACTION
	
	/**
	 * Retourne le tour courant du soldat.
	 *
	 * @return numéro du tour
	 */
	public int getTour() { return this.tour; }
	
	/**
	 * Réinitialise le tour du soldat.
	 */
	public void setTour() { this.tour = 0; }
	
	/**
	 * Méthode appelée lors du déroulement d'un tour.
	 *
	 * @param tour numéro du tour
	 */
	public void joueTour(int tour) {} // on sait pas ce que ça fait ???
	
	/**
	 * Retourne le nombre d'actions disponibles en tenant compte des effets.
	 *
	 * @return nombre d'actions
	 */

	public int getAction() { 
		int action = this.action + listeEffets.sommeEffets(Effet.TCarAff.ACTION);
		if (action >= 0 ) return action;
		return 0;
	}
	
	/**
	 * Modifie le nombre d'actions restantes.
	 *
	 * @param action nouvelle valeur
	 */
	public void setAction(int action) { this.action = action; }
	
	/**
	 * Ajoute ou retire des actions.
	 *
	 * @param nbAction variation du nombre d'actions
	 */
	public void ajouterAction(int nbAction) { this.action += nbAction; }
	// TOUR/ACTION
	
	// EFFETS
	/**
	 * Retourne la liste des effets actifs.
	 *
	 * @return liste d'effets
	 */
	public ListeEffets getListeEffets() { return this.listeEffets; }
	// EFFETS
	
	// PORTEE VISUELLE
	
	/**
	 * Retourne la portée visuelle effective du soldat.
	 *
	 * @return portée visuelle
	 */
	public int getPortee() {
		int portee = this.PORTEE_VISUELLE + listeEffets.sommeEffets(Effet.TCarAff.PORTEE);
		if (portee >= 1) return portee;
		return 1;
	}
	
	/**
	 * Met à jour les cases visibles sur la carte.
	 *
	 * @param visibilite matrice de visibilité
	 * @return matrice mise à jour
	 */
	public int[][] setCasesVisibles(int[][] visibilite) { // Penser a gerer les lignes de vue
		int i, j;
		int x = this.pos.getX();
		int y = this.pos.getY();
		int portee = this.getPortee();
		for (i = -portee*2 ; i <= portee*2 ; i++) {
			for (j = -portee*2 ; j <= portee*2 ; j++) {
				if (i + x >= 0 && i + x < Carte.LARGEUR_CARTE*2
					&& j + y >= 0 && j + y < Carte.HAUTEUR_CARTE) {
					if (this.pos.distance(new Position(i+x, j+y)) <= portee) {
						visibilite[i+x][j+y] = 1;
					}
				}
			}
		}
		return visibilite;
	}
	// PORTEE VISUELLE
	
	
	// COMPETENCE
	/**
	 * Initialise les compétences du soldat selon son type.
	 */
	/*
	public void initialiserCompetence() {
		if(this instanceof Heros) {
			switch()
			ajouterCompetence(new Competence(TypeCompetence.BOULE_DE_FEU));
			ajouterCompetence(new Competence(TypeCompetence.SOIN));
			ajouterCompetence(new Competence(TypeCompetence.SOIN_DE_ZONE));
			ajouterCompetence(new Competence(TypeCompetence.TIR_A_PORTER));
			ajouterCompetence(new Competence(TypeCompetence.COUP_EPEE));
			ajouterCompetence(new Competence(TypeCompetence.LANCE_PIERRE));
			ajouterCompetence(new Competence(TypeCompetence.COUP_DE_BATON));

		}else if(this instanceof Monstre){
			ajouterCompetence(new Competence(TypeCompetence.COUP_EPEE));
		}
	} */
	
	/**
	 * Ajoute une compétence au soldat.
	 *
	 * @param competence compétence à ajouter
	 */
	public void ajouterCompetence(Competence competence) {
		this.Competences.add(competence);
	}
	
	/**
	 * Décrémente le temps de recharge de toutes les compétences.
	 */
	public void decrementerTempsRecharge() {
		for(Competence c : Competences) {
			c.decrementerTempsRestant();
		}
	}
	
	/**
	 * Retourne la liste des compétences du soldat.
	 *
	 * @return liste des compétences
	 */
	public List<Competence> getCompetences() {
		return Competences;
	}
	
	
	// COMBAT
	/**
	 * Engage un combat contre un autre soldat.
	 *
	 * @param soldat adversaire
	 * @return true si le combat a eu lieu
	 */
	public boolean combat(Soldat soldat) {
		Carte carte = this.carte;
		boolean combat = false;
		
		// Combat en melee : l'adversaire rend les coups, melee = case collee
		// Combat a distance : l'adversaire ne rend pas les coups
		if (this.pos.estVoisine(soldat.pos)) {
			combat = this.combatMelee(soldat);
		} else {
			combat = this.combatDistance(soldat);
		}
		
		if (!combat) {
			System.out.println("LE HEROS N'A PAS COMBATTU");
			return false;
		}
		
		this.ajouterAction(-1);
		
		// On retire les soldats de la carte si ils sont morts
		carte.mort(this);
		carte.mort(soldat);
		
		return true;
	} 
	
	private boolean combatMelee(Soldat soldat) {
		Carte carte = this.carte;
		String msgLog = "";
		
		if (this.getPuissance() <= 0) {
			System.out.println("Cette unite ne peu pas combattre en melee");
			return false;
		}
		
		soldat.retirerPv(this.getPuissance());
		msgLog = this.logCombat(soldat, false);

		if (!soldat.estMort()) {
			this.retirerPv(soldat.getPuissance());
			msgLog += soldat.logCombat(this, true);
		}
		
		carte.addCombatMessage(msgLog);
		return true;
	}
	
	private boolean combatDistance(Soldat soldat) {
		Carte carte = this.carte;
		String msgLog = "";
		
		if (this.getTir() <= 0) {
			System.out.println("Cette unite ne peut pas combattre a distance");
			return false;
		}
		
		soldat.retirerPv(this.getTir());
		msgLog = this.logCombat(soldat, false);
		
		carte.addCombatMessage(msgLog);
		return true;
	}
	
	private String logCombat(Soldat soldat, boolean riposte) {
		String log = "";
		String atq = "";
		String def = "";
		String msg_riposte = "";
		
		if (riposte) msg_riposte = " riposte et";
		
		if (this instanceof Monstre) {
			atq += ((Monstre)this).getNom();
			def += ((Heros)soldat).getNom();
		} else {
			atq += ((Heros)this).getNom();
			def += ((Monstre)soldat).getNom();
		}
		
		log += atq+msg_riposte+" inflige "+this.getPuissance()+" degats a "+def+"\n";
		
		if (soldat.estMort()) {
			log += def+" succombe à ses blessures.\n";
		}
		
		return log;
	}
	// COMBAT
	
	
	// DEPLACEMENT
	/**
	 * Retourne la distance de déplacement effective.
	 *
	 * @return distance de déplacement
	 */
	public int getDeplacement() { return this.DEPLACEMENT + listeEffets.sommeEffets(Effet.TCarAff.DEPLACEMENT); }
	
	// Fonction auxiliaire pour calculer le nombre de cases max d'une zone de déplacement
	private int sommeDeplacement(int dep) {
		int somme = 0;
		for (int i = 1 ; i <= dep ; i++) {
			somme += i;
		}
		return somme;
	}
	
	public EnsemblePosition zoneDeplacement() {
		int nbPosMax = (int) 6 * (sommeDeplacement(getDeplacement()));
		EnsemblePosition ePos = new EnsemblePosition(nbPosMax);
		
		this.zoneDeplacementAux(this.pos, this.pos, this.getDeplacement(), ePos);
		
		return ePos;
	}

	private void zoneDeplacementAux(Position posInit, Position pos, int deplacement, EnsemblePosition ePos) {
		
		if (!(pos.estValide())) {
			return;
		}
		
		Soldat soldat = this.carte.getSoldat(pos);
		
		if (deplacement <= -1 
			|| this.carte.getCase(pos).getType().getAccessible() == false
			|| (this instanceof Heros && soldat instanceof Monstre)
			|| (this instanceof Monstre && soldat instanceof Heros)
			) {
			return;
		}

		if (!(ePos.contient(pos)) && this.carte.getCase(pos).estLibre()) {
			ePos.ajouterPos(pos);
		}
		
		int x = pos.getX();
		int y = pos.getY();
		
		// Droite
		this.zoneDeplacementAux(posInit, new Position(x+2, y), deplacement-1, ePos);
		// Gauche
		this.zoneDeplacementAux(posInit, new Position(x-2, y), deplacement-1, ePos);
		// Bas Gauche
		this.zoneDeplacementAux(posInit, new Position(x-1, y+1), deplacement-1, ePos);
		// Bas Droite
		this.zoneDeplacementAux(posInit, new Position(x+1, y+1), deplacement-1, ePos);
		// Haut Gauche
		this.zoneDeplacementAux(posInit, new Position(x-1, y-1), deplacement-1, ePos);
		// Haut Droite
		this.zoneDeplacementAux(posInit, new Position(x+1, y-1), deplacement-1, ePos);
	}
	
	/**
	 * Déplace le soldat vers une nouvelle position.
	 *
	 * @param newPos nouvelle position
	 */
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.setPos(newPos);
			//this.setCasesVisibles();
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}
	// DEPLACEMENT
	
	/**
	 * Retourne une représentation textuelle du soldat.
	 *
	 * @return description du soldat
	 */
	public String toString() {
		
		String s = "";
		
		String nom = "";
		String num = "";
		
		if (this instanceof Monstre) {
			nom += ((Monstre)this).getNom();
			num += this.getNum()+1;
		} else {
			nom += ((Heros)this).getNom();
			num += (char)('A'+this.getNum());
		}
		
		s += "("+this.getClass().getSimpleName()+" "+num+") "+nom+" :\n";
		s += " - Position : "+this.pos+"\n";
		s += " - PV : "+this.getPointsActuels()+"/"+this.getPoints()+"\n";
		s += " - Puissance : "+getPuissance();
		s += " - Tir : "+getTir();
		s += " - Portee : "+getPortee();
		s += " - PorteeReelle"+this.PORTEE_VISUELLE;
		s += " - Deplacement : "+this.getDeplacement()+"\n";
		s += " - Action(s) restante : "+this.action;
		// s += listeEffets.toString();
			
		return s;
	}
	
	public String recupIdentite() {
		 String id = "";
		 
		if (this instanceof Monstre) {
			id += ((Monstre)this).getNom();
			id += this.getNum()+1;
		} else {
			id += ((Heros)this).getNom();
			id += (char)('A'+this.getNum());
		}
		return id;
	}
	
	// TMP
	public Carte getCarte() {
		return this.carte;
	}
}
