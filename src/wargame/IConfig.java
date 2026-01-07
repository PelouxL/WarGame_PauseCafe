package wargame;

import java.awt.Color;

/**
 * Interface de configuration du jeu.
 * <p>
 * Contient toutes les constantes de configuration pour le jeu, y compris :
 * - dimensions de la carte et des panneaux,
 * - nombre de héros, monstres et obstacles,
 * - couleurs utilisées pour les terrains et les unités,
 * - positions et tailles des fenêtres et panneaux,
 * 
 * <p>Cette interface sert de référence globale pour toutes les classes du jeu.
 */
public interface IConfig {
	// CONFIGS JEU
	int LARGEUR_CARTE = 35; int HAUTEUR_CARTE = 35; // nb de cases
	int NB_PIX_CASE = 20; // taille des cases
	int NB_HEROS = 6; int NB_MONSTRES = 15; int NB_OBSTACLES = 20;
	int TOUR_HEROS = 0; int TOUR_MONSTRE = 1;
	
	// OFFSET X POUR AFFICHAGE CARTE
	int OFFSET_X = NB_PIX_CASE / 2;
	
	// PANNEAUX
	
	// Largeurs
	int LARGEUR_PANNEAU_L = 200;   // panneaux latéraux (gauche & droite)
	int LARGEUR_PANNEAU_CARTE = LARGEUR_CARTE * NB_PIX_CASE + OFFSET_X;  // 35 * 20 = 700
	int LARGEUR_FENETRE = LARGEUR_PANNEAU_CARTE + LARGEUR_PANNEAU_L; // 700 + 200 = 900
	
	// Hauteurs
	int HAUTEUR_PANNEAU_LOG = 150;
	int HAUTEUR_PANNEAU_CARTE = HAUTEUR_CARTE * (NB_PIX_CASE - 5) + OFFSET_X; // 25 * 20 = 500
	int HAUTEUR_PANNEAU_L = HAUTEUR_PANNEAU_CARTE;
	int HAUTEUR_PANNEAU_BAS = 100;
	int HAUTEUR_PANNEAU_HAUT = 50;
	int HAUTEUR_JMENU = 30;
	int HAUTEUR_FENETRE = HAUTEUR_PANNEAU_HAUT + HAUTEUR_PANNEAU_CARTE + HAUTEUR_PANNEAU_BAS;

	int LARGEUR_PANNEAU_LOG = LARGEUR_PANNEAU_CARTE/2;
	int LARGEUR_PANNEAU_BAS  = LARGEUR_FENETRE;
	int LARGEUR_PANNEAU_HAUT = LARGEUR_FENETRE;

	// Positions
	int POSITION_X = 10; int POSITION_Y = 10; // Position de la fenêtre
	int POSITION_LOG_X = 0; int POSITION_LOG_Y = HAUTEUR_PANNEAU_CARTE - HAUTEUR_PANNEAU_LOG;
	
	// Couleurs
	Color COULEUR_VIDE = Color.white;
	Color COULEUR_INCONNU = Color.lightGray;
	Color COULEUR_TEXTE = Color.black;
	Color COULEUR_PANNEAU_TRANSPARENT = new Color(150,150,150,230);
	Color COULEUR_PLATEAU = Color.decode("#8B4513");
	Color COULEUR_BOUTON_COMP = Color.decode("#260923");
	Color COULEUR_BOUTON_COMP_INDISPONIBLE = Color.decode("#DDCEDE");
	Color COULEUR_PORTEE_COMP = new Color(255, 235 , 56, 150);
	
	// SOLDATS
	Color COULEUR_HEROS = Color.red;
	Color COULEUR_HEROS_DEJA_JOUE = Color.pink;
	Color COULEUR_MONSTRES = Color.black;
	
	// TERRAINS
	int NB_RIVIERE = (int) (Math.random()*2 + 1); // entre 1 et 2
	int NB_FORET = (int) (Math.random()*5 + 5); // entre 5 et 9
	int NB_SABLE = (int) (Math.random()*3 + 1); // entre 1 et 3
	int NB_ROCHER = (int) (Math.random()*2 + 3); // entre 3 et 4
	int NB_FEU = (int) (Math.random()*2 + 1); // entre 1 et 2
	
	Color COULEUR_HERBE = Color.decode("#69C24C"); // vert clair
	Color COULEUR_EAU = Color.BLUE;                // bleu
	Color COULEUR_FORET = Color.decode("#0A5C36"); // vert foncé 
	Color COULEUR_ROCHER = Color.GRAY;             // gris
	Color COULEUR_PONT  = Color.decode("#8B4513"); // marron
	Color COULEUR_FEU = Color.ORANGE;              // orange
	Color COULEUR_ACIDE = Color.decode("#7400D5"); // violet
	Color COULEUR_VILLAGE = Color.YELLOW;          // jaune
	Color COULEUR_SABLE = Color.decode("#E0CDA9"); // beige
	
	// INFOBULLE

	int LARGEUR_INFOBULLE = 200;
	int HAUTEUR_INFOBULLE = 120;
	int X_INFOBULLE = 0;
	int Y_INFOBULLE = HAUTEUR_PANNEAU_L - HAUTEUR_INFOBULLE;
	
	// image du soldat
	int LARGEUR_IMAGE_SOLDAT = 60;
	int HAUTEUR_IMAGE_SOLDAT = 60;
	int X_IMAGE_SOLDAT = X_INFOBULLE + 8;
	int Y_IMAGE_SOLDAT = Y_INFOBULLE + 8;
	
	// barre de vie (seulement rectangle vert/orange/rouge)
	int HAUTEUR_BARRE_DE_VIE = 15;
	int X_BARRE_DE_VIE = X_INFOBULLE + 8;
	int Y_BARRE_DE_VIE = Y_INFOBULLE + 98;
	
	// points de vie (en chiffres)
	int X_PV = X_INFOBULLE + 120;
	int Y_PV = Y_INFOBULLE + 109;
	int PV_DECALAGE_X = -5;
	
	// statistiques (rectangles colorés)
	int LARGEUR_CRAN = 10;
	int HAUTEUR_CRAN = 10;
	int X_STAT = X_INFOBULLE + 80;
	int Y_STAT = Y_INFOBULLE + 15;
	int STAT_DECALAGE_Y = 22;
	
	// nom du perso
	int LARGEUR_NOM = 64;
	int HAUTEUR_NOM = 22;
	int X_NOM = X_INFOBULLE + 6;
	int Y_NOM = Y_INFOBULLE + 72;
	
	// couleurs
	Color COULEUR_PV_HAUT = Color.GREEN;
	Color COULEUR_PV_MOYEN = Color.ORANGE;
	Color COULEUR_PV_BAS = Color.RED;
	Color COULEUR_STAT_PUISSANCE = Color.decode("#FF0000");
	Color COULEUR_STAT_PORTEE_VISUELLE = Color.decode("#0080FF");
	Color COULEUR_STAT_DEPLACEMENT = Color.decode("#FFEE00");
	Color COULEUR_STAT_PORTEE_DE_TIR = Color.decode("#BB00FF");
}
