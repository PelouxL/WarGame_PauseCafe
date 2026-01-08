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

	private final int POINT_DE_VIE, PUISSANCE, TIR, PORTEE_VISUELLE, DEPLACEMENT;
	private int pointsDeVie;
	private Position pos;
	private Carte carte;
	private int action = 2; // nb d'action possible pour un soldat par tour
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
	
	/*
	 * Initialise les competence des soldats selon leur type.
	 */
	protected abstract void initialiserCompetence(); 
	
	/**
	 * Indique si le soldat est mort.
	 *
	 * @return true si les points de vie sont inférieurs ou égaux à zéro, false sinon
	 */
	public boolean estMort() { return this.pointsDeVie <= 0; }
	
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
	
	/**
	 * Ajoute une compétence au soldat.
	 *
	 * @param competence compétence à ajouter
	 */
	public void ajouterCompetence(Competence competence) { this.Competences.add(competence); }
	
	/**
	 * Décrémente le temps de recharge de toutes les compétences du soldat.
	 */
	public void decrementerTempsRecharge() {
		for(Competence c : Competences) {
			c.decrementerTempsRestant();
		}
	}
	
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
		if (this.pos.estVoisine(soldat.pos)) combat = this.combatMelee(soldat);
		
		// Combat a distance : l'adversaire ne rend pas les coups
		else combat = this.combatDistance(soldat);
		
		// Verification : le combat n'a pas pu etre effectue
		if (!combat) return false;
		
		// LE combat a bien ete effectue
		this.ajouterAction(-1);
		
		// On retire les soldats de la carte s'ils sont morts
		carte.mort(this);
		carte.mort(soldat);
		
		return true;
	} 
	
	private boolean combatMelee(Soldat soldat) {
		Carte carte = this.carte;
		String msgLog = "";
		
		// Verification si l'unite peut bien combattre en melee
		if (this.getPuissance() <= 0) return false;
		
		// Combat
		soldat.retirerPv(this.getPuissance());
		msgLog = this.logCombat(soldat, false);
		
		// Riposte adverse
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
		
		// Verification si l'unite peut bien combattre a distance
		if (this.getTir() <= 0) return false;
		
		// Combat
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
	
	/**
	 * Calcul la zone des deplacements possibles pour un soldat
	 * grace a un algorithme d'inondation.
	 * 
	 * @return L'ensemble des positions accessibles
	 */
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
	
	// Fonction auxiliaire pour calculer le nombre de cases max d'une zone de déplacement
	private int sommeDeplacement(int dep) {
		int somme = 0;
		for (int i = 1 ; i <= dep ; i++) {
			somme += i;
		}
		return somme;
	}
	
	/**
	 * Déplace le soldat vers une nouvelle position.
	 *
	 * @param newPos nouvelle position
	 */
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) this.setPos(newPos);
		else System.out.println("Erreur seDeplace : position invalide.");
	}
	
	/**
	 * Représentation textuelle du soldat.
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
	
	/**
	 * ?????????
	 * @return
	 */
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
	
	public int getPoints() { return this.POINT_DE_VIE; }
	public int getPointsActuels() { return this.pointsDeVie; }
	public void setPointsActuels(int pointsDeVie) { this.pointsDeVie = pointsDeVie; }
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
	 * Retourne la valeur de tir du soldat en tenant compte des effets.
	 *
	 * @return puissance de tir effective
	 */
	public int getTir() {
		int puissance_tir = this.TIR + listeEffets.sommeEffets(Effet.TCarAff.PUISSANCE); 
		if (puissance_tir >= 0) return puissance_tir;
		return 0; 
	}
	
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
	 * Retourne la distance de déplacement en tenant compte des effets.
	 *
	 * @return distance de déplacement
	 */
	public int getDeplacement() { return this.DEPLACEMENT + listeEffets.sommeEffets(Effet.TCarAff.DEPLACEMENT); }
	
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
	
	public void setAction(int action) { this.action = action; }
	public void ajouterAction(int nbAction) { this.action += nbAction; }
	public Position getPos() { return this.pos; }
	public void setPos(Position pos) { this.pos = pos; }
	public int getNum() { return this.NUM; }
	public Carte getCarte() { return this.carte; }
	public ListeEffets getListeEffets() { return this.listeEffets; }
	public List<Competence> getCompetences() { return Competences; }
}
