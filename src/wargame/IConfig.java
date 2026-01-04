package wargame;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

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
	int POSITION_X = 10; int POSITION_Y = 10; // Position de la fen�tre
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
	int NB_RIVIERE = (int) (Math.random()*2 + 1); // 1 ou 2 riviere
	int NB_FORET = 10;
	int NB_SABLE = 5;
	int NB_FEU = 1;
	
	Color COULEUR_HERBE = Color.decode("#69C24C"); // vert clair
	Color COULEUR_EAU = Color.BLUE;                // bleu
	Color COULEUR_FORET = Color.decode("#0A5C36"); // vert fonce 
	Color COULEUR_ROCHER = Color.GRAY;             // gris
	Color COULEUR_PONT  = Color.decode("#8B4513"); // marron
	Color COULEUR_FEU = Color.ORANGE;              // orange
	Color COULEUR_ACIDE = Color.decode("#7400D5"); // violet
	Color COULEUR_VILLAGE = Color.YELLOW;          // jaune
	Color COULEUR_SABLE = Color.decode("#E0CDA9"); // beige
	
	// INFORMATIONS
	Color COULEUR_PV_HAUT = Color.GREEN;
	Color COULEUR_PV_MOYEN = Color.ORANGE;
	Color COULEUR_PV_BAS = Color.RED;
	Color COULEUR_PUISSANCE = Color.decode("FF0000FF");
	Color COULEUR_PORTEE_VISUELLE = Color.decode("0080FFFF");
	Color COULEUR_DEPLACEMENT = Color.decode("FFEE00FF");
	Color COULEUR_PORTEE_DE_TIR = Color.decode("BB00FFFF");
	
	// IMAGES
	
	// image terrain
/*	Image imgTerrainEau = new ImageIcon("./images/terrain/img_terrain_eau.png").getImage();
	Image imgTerrainHerbe = new ImageIcon("./images/terrain/img_terrain_herbe.png").getImage();
	Image imgTerrainForet = new ImageIcon("./images/terrain/img_terrain_foret.png").getImage();
	Image imgTerrainRocher = new ImageIcon("./images/terrain/img_terrain_rocher.png").getImage();
	Image imgTerrainVillage = new ImageIcon("./images/terrain/img_terrain_village.png").getImage();
	Image imgTerrainFeu = new ImageIcon("./images/terrain/img_terrain_feu.png").getImage();
	Image imgTerrainAcide = new ImageIcon("./images/terrain/img_terrain_acide.png").getImage();
	Image imgTerrainSable = new ImageIcon("./images/terrain/img_terrain_sable.png").getImage();
	Image imgTerrainPont = new ImageIcon("./images/terrain/img_terrain_pont.png").getImage();
	Image imgTerrainDeplacement = new ImageIcon("./images/terrain/img_terrain_deplacement.png").getImage();

	// image personnage
	Image  imgSpritePersoMage = new ImageIcon("./images/sprite_perso/img_sprite_perso_mage.png").getImage();
	Image imgSpritePersoMonstre = new ImageIcon("./images/sprite_perso/img_sprite_perso_monstre.png").getImage();
*/
	

}