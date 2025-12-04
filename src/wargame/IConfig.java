package wargame;
import java.awt.Color;

public interface IConfig {
	int LARGEUR_CARTE = 35; int HAUTEUR_CARTE = 25; // en nombre de cases
	int NB_PIX_CASE = 20; //taille des cases
	int POSITION_X = 10; int POSITION_Y = 10; // Position de la fen�tre
	int NB_HEROS = 6; int NB_MONSTRES = 15; int NB_OBSTACLES = 20;
	
	// definition des dimensions des diverses panneaus
	/*
	int LARGEUR_PANNEAU_CARTE = NB_PIX_CASE * LARGEUR_CARTE, HAUTEUR_PANNEAU_CARTE = NB_PIX_CASE * HAUTEUR_CARTE;
	int LARGEUR_PANNEAU_L = 300, HAUTEUR_PANNEAU_L = HAUTEUR_PANNEAU_CARTE ;
	int LARGEUR_PANNEAU_BAS = LARGEUR_PANNEAU_L * 2 + LARGEUR_CARTE * NB_PIX_CASE;
	int HAUTEUR_PANNEAU_BAS = 100;
	int LARGEUR_PANNEAU_JMENU = LARGEUR_PANNEAU_BAS, HAUTEUR_PANNEAU_JMENU = 50;
	int LARGEUR_FENETRE = LARGEUR_PANNEAU_L * 2 + LARGEUR_PANNEAU_CARTE, HAUTEUR_FENETRE = HAUTEUR_PANNEAU_L + HAUTEUR_PANNEAU_JMENU + HAUTEUR_PANNEAU_BAS;
	*/
	int LARGEUR_PANNEAU_L = 200;   // panneaux latéraux (gauche & droite)
	int LARGEUR_PANNEAU_CARTE = LARGEUR_CARTE * NB_PIX_CASE;  // 35 * 20 = 700

	// -------- Largeur totale = gauche + carte + droite --------
	int LARGEUR_FENETRE = LARGEUR_PANNEAU_L + LARGEUR_PANNEAU_CARTE + LARGEUR_PANNEAU_L; 
	// soit 200 + 700 + 200 = 1100

	// Hauteurs
	int HAUTEUR_PANNEAU_CARTE = HAUTEUR_CARTE * NB_PIX_CASE; // 25 * 20 = 500
	int HAUTEUR_PANNEAU_L = HAUTEUR_PANNEAU_CARTE;
	int HAUTEUR_PANNEAU_BAS = 100;
	int HAUTEUR_PANNEAU_HAUT = 50;
	int HAUTEUR_JMENU = 30;

	// -------- Hauteur totale --------
	int HAUTEUR_FENETRE = HAUTEUR_PANNEAU_HAUT + HAUTEUR_PANNEAU_CARTE + HAUTEUR_PANNEAU_BAS;

	int LARGEUR_PANNEAU_BAS  = LARGEUR_FENETRE;
	int LARGEUR_PANNEAU_HAUT = LARGEUR_FENETRE;

	
	// COULEURS
	Color COULEUR_VIDE = Color.white;
	Color COULEUR_INCONNU = Color.lightGray;
	Color COULEUR_TEXTE = Color.black;
	
	// Couleur Soldats
	Color COULEUR_HEROS = Color.red;
	Color COULEUR_HEROS_DEJA_JOUE = Color.pink;
	Color COULEUR_MONSTRES = Color.black;
	
	// Couleur Obstacles
	Color COULEUR_EAU = Color.blue;
	Color COULEUR_FORET = Color.decode("#0A5C36"); // vert fonce 
	Color COULEUR_ROCHER = Color.gray;
	
	// Couleur Terrains
	Color COULEUR_PONT = Color.decode("#8B4513"); // marron
	Color COULEUR_HERBE = Color.green; // vert clair
	
	// Chemin des sauvagardes
	//String PATHSAVING = ./
	
}