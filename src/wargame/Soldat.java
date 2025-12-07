package wargame;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Soldat implements ISoldat, Serializable{
	private final int POINT_DE_VIE, PUISSANCE, TIR, PORTEE_VISUELLE, DEPLACEMENT = 3;

	private int pointsDeVie;
	private Position pos;
	private Carte carte;
	private int action = 2; // nb d'action possible pour un soldat par tour
	private int tour; // On enleve?
	
	private static int nbHeros = 0;
	private static int nbMonstre = 0;
	private final int NUM;
	
	private List<Competence> Competences = new ArrayList<>();
	
	public Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
		POINT_DE_VIE = pointsDeVie = pts;
		PORTEE_VISUELLE = portee;
		PUISSANCE = puiss;
		TIR = tir;
		this.carte = carte;
		this.pos = pos;
		
		if(this instanceof Heros) {
			NUM = nbHeros++;
		}else if(this instanceof Monstre) {
			NUM = nbMonstre++;
		}else {
			NUM = -1;
		}
		
		initialiserCompetence();
	}
	
	
	// POINTS DE VIE
	public int getPoints() { return this.POINT_DE_VIE; }
	public int getPointsActuels() { return this.pointsDeVie; }
	public void setPointsActuels(int pointsDeVie) { this.pointsDeVie = pointsDeVie; }
	public void retirerPv(int degats) { this.pointsDeVie -= degats; }
	public void ajouterPv(int soin) {
		this.pointsDeVie += soin; 
		if (this.pointsDeVie > this.POINT_DE_VIE) this.pointsDeVie = this.POINT_DE_VIE;
	}
	public boolean estMort() { return this.pointsDeVie <= 0; }
	// POINTS DE VIE
	
	
	// DEGATS
	public int getPuissance() { return this.PUISSANCE; }
	public int getTir() { return this.TIR; }
	// DEGATS
	
	
	// NUM
	public int getNum() { return this.NUM; }
	// NUM
	
	
	// POSITION
	public Position getPos() { return this.pos; }
	public void setPos(Position pos) { this.pos = pos; }
	// POSITION
	
	
	// TOUR/ACTION
	public int getTour() { return this.tour; }
	public void setTour() { this.tour = 1; }
	public void joueTour(int tour) {} // on sait pas ce que ça fait ???
	
	public int getAction() { return this.action; }
	public void setAction(int action) { this.action = action; }
	public void ajouterAction(int nbAction) { this.action += nbAction; }
	// TOUR/ACTION
	
	
	// PORTEE VISUELLE
	public int getPortee() { return this.PORTEE_VISUELLE; }
	
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
	public void initialiserCompetence() {
		if(this instanceof Heros) {
			ajouterCompetence(new Competence(Competence.TypeCompetence.BOULE_DE_FEU));
			ajouterCompetence(new Competence(Competence.TypeCompetence.SOIN));
			ajouterCompetence(new Competence(Competence.TypeCompetence.SOIN_DE_ZONE));
			ajouterCompetence(new Competence(Competence.TypeCompetence.TIR_A_PORTER));
			ajouterCompetence(new Competence(Competence.TypeCompetence.COUP_EPEE));
		}else if(this instanceof Monstre){
			ajouterCompetence(new Competence(Competence.TypeCompetence.COUP_EPEE));
		}
	}
	
	public void ajouterCompetence(Competence competence) {
		this.Competences.add(competence);
	}
	
	
	public void decrementerTempsRecharge() {
		for(Competence c : Competences) {
			c.decrementerTempsRestant();
		}
	}
	
	public List<Competence> getCompetences() {
		return Competences;
	}
	
	
	// COMBAT
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
	public int getDeplacement() { return this.DEPLACEMENT; }
	
	public EnsemblePosition zoneDeplacement() {
		int nbPosMax = (int) Math.pow(6, this.DEPLACEMENT); // TEMPORAIRE FAIRE VRAI CALCUL
		EnsemblePosition ePos = new EnsemblePosition(nbPosMax);
		
		this.zoneDeplacementAux(this.pos, this.pos, this.DEPLACEMENT, ePos);
		
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
	
	public void seDeplace(Position newPos) {
		if (newPos.estValide()) {
			this.setPos(newPos);
			//this.setCasesVisibles();
		} else {
			System.out.println("Erreur seDeplace : position invalide.");
		}
	}
	// DEPLACEMENT
	
	
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
		s += " - Deplacement : "+this.DEPLACEMENT+"\n";
		s += " - Action(s) restante : "+this.action;
			
		return s;
	}
}
