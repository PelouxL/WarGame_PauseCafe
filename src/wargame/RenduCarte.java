package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import wargame.ISoldat.TypesH;
import wargame.Terrain.TypeTerrain;

public class RenduCarte implements IConfig {

    /* =======================
       IMAGES (VUE UNIQUEMENT)
       ======================= */

    private static final Image imgTerrainHerbe   = new ImageIcon("./images/terrain/img_terrain_herbe.png").getImage();
    private static final Image imgTerrainEau     = new ImageIcon("./images/terrain/img_terrain_eau.png").getImage();
    private static final Image imgTerrainForet   = new ImageIcon("./images/terrain/img_terrain_foret.png").getImage();
    private static final Image imgTerrainRocher  = new ImageIcon("./images/terrain/img_terrain_rocher.png").getImage();
    private static final Image imgTerrainVillage = new ImageIcon("./images/terrain/img_terrain_village.png").getImage();
    private static final Image imgTerrainFeu     = new ImageIcon("./images/terrain/img_terrain_feu.png").getImage();
    private static final Image imgTerrainAcide   = new ImageIcon("./images/terrain/img_terrain_acide.png").getImage();
    private static final Image imgTerrainSable   = new ImageIcon("./images/terrain/img_terrain_sable.png").getImage();
    private static final Image imgTerrainPont    = new ImageIcon("./images/terrain/img_terrain_pont.png").getImage();

    
    /* =======================
       MÉTHODE PRINCIPALE
       ======================= */

    public static void dessiner(
            Graphics g,
            Carte carte,
            Position caseSurvolee,
            Position caseCliquee,
            Competence competenceChoisie) {

        carte.setVisibilite();

        for (int i = 0; i < LARGEUR_CARTE * 2; i++) {
            for (int j = 0; j < HAUTEUR_CARTE; j++) {

                if ((i + j) % 2 == 1) continue;

                Position pos = new Position(i, j);
                dessinerCase(g, carte, pos, false);
            }
        }

        if (caseSurvolee != null && caseCliquee == null && carte.getSoldat(caseSurvolee) != null) {
            dessinerZoneDeplacement(g, carte, carte.getSoldat(caseSurvolee));
        }

        if (caseCliquee != null && carte.getSoldat(caseCliquee) instanceof Heros && competenceChoisie == null) {
            dessinerZoneDeplacement(g, carte, carte.getSoldat(caseCliquee));
            dessinerCaseCliquee(g, caseCliquee);
        }

        if (caseCliquee != null && competenceChoisie != null && carte.getSoldat(caseCliquee) instanceof Heros) {
            dessinerPorteeCompetence(g, carte, competenceChoisie, carte.getSoldat(caseCliquee), caseSurvolee);
            dessinerCaseCliquee(g, caseCliquee);
        }

        
        for (Heros h : carte.getListeHeros()) {
            RenduSoldat.dessiner(g, h);
        }

        for (Monstre m : carte.getListeMonstres()) {
            if (carte.getVisibilite(m.getPos()) == 1) {
                RenduSoldat.dessiner(g, m);
            }
        }
    }

    /* =======================
       DESSIN DES CASES
       ======================= */

    private static void dessinerCase(Graphics g, Carte carte, Position pos, boolean transparent) {

        int x = pos.getX() / 2;
        int y = pos.getY();
        int offsetX = (y % 2 == 1) ? OFFSET_X : 0;

        if (carte.getVisibilite(pos) == 0) {
            g.setColor(COULEUR_INCONNU);
            dessinerInterieurHexagone(g, x, y, null);
        } else {
            Image img = imageTerrain(carte.getCase(pos).getType());
            dessinerInterieurHexagone(g, x, y, img);
        }

        g.setColor(Color.BLACK);
        dessinerContourHexagone(g, x, y);
    }

    private static Image imageTerrain(TypeTerrain type) {
        switch (type) {
            case HERBE:   return imgTerrainHerbe;
            case EAU:     return imgTerrainEau;
            case FORET:   return imgTerrainForet;
            case ROCHER:  return imgTerrainRocher;
            case VILLAGE: return imgTerrainVillage;
            case FEU:     return imgTerrainFeu;
            case ACIDE:   return imgTerrainAcide;
            case SABLE:   return imgTerrainSable;
            case PONT:    return imgTerrainPont;
            default:      return null;
        }
    }

    /* =======================
       HEXAGONES
       ======================= */

    private static void dessinerInterieurHexagone(Graphics g, int x, int y, Image img) {

        int offsetX = (y % 2 == 1) ? NB_PIX_CASE / 2 : 0;

        if (img == null) {
            g.fillPolygon(hexX(x, y, offsetX), hexY(y), 6);
        } else {
            g.drawImage(img, x * NB_PIX_CASE + offsetX, y * NB_PIX_CASE * 3 / 4,
                        NB_PIX_CASE, NB_PIX_CASE, null);
        }
    }

    private static void dessinerContourHexagone(Graphics g, int x, int y) {
        int offsetX = (y % 2 == 1) ? OFFSET_X : 0;
        g.drawPolygon(hexX(x, y, offsetX), hexY(y), 6);
    }

    private static int[] hexX(int x, int y, int offset) {
        return new int[]{
                x * NB_PIX_CASE + offset,
                x * NB_PIX_CASE + NB_PIX_CASE / 2 + offset,
                x * NB_PIX_CASE + NB_PIX_CASE + offset,
                x * NB_PIX_CASE + NB_PIX_CASE + offset,
                x * NB_PIX_CASE + NB_PIX_CASE / 2 + offset,
                x * NB_PIX_CASE + offset
        };
    }

    private static int[] hexY(int y) {
        return new int[]{
                y * NB_PIX_CASE * 3 / 4 + NB_PIX_CASE / 4,
                y * NB_PIX_CASE * 3 / 4,
                y * NB_PIX_CASE * 3 / 4 + NB_PIX_CASE / 4,
                y * NB_PIX_CASE * 3 / 4 + NB_PIX_CASE * 3 / 4,
                y * NB_PIX_CASE * 3 / 4 + NB_PIX_CASE,
                y * NB_PIX_CASE * 3 / 4 + NB_PIX_CASE * 3 / 4
        };
    }

    /* =======================
       ZONES & COMPÉTENCES
       ======================= */

    private static void dessinerZoneDeplacement(Graphics g, Carte carte, Soldat soldat) {
        EnsemblePosition zone = soldat.zoneDeplacement();
        for (int i = 0; i < zone.getNbPos(); i++) {
            Position p = zone.getPosition(i);
            if (carte.getVisibilite(p) == 1) {
                g.setColor(new Color(212, 74, 105, 150));
                dessinerInterieurHexagone(g, p.getX() / 2, p.getY(), null);
            }
        }
    }

    public static void dessinerCaseCliquee(Graphics g, Position pos) {
        g.setColor(new Color(100, 0, 0, 40));
        dessinerInterieurHexagone(g, pos.getX() / 2, pos.getY(), null);
    }

    
    public static void dessinerPorteeCompetence(Graphics g, Carte carte, Competence competence, Soldat lanceur, Position caseSurvolee) {

        if (caseSurvolee == null) return;

        // Zone de portée "croix" autour du lanceur
        EnsemblePosition ePos = lanceur.getPos().voisinesCroix(competence.getType().getDistance());

        // Zone d'effet autour de la case survolée
        EnsemblePosition zoneAtt = caseSurvolee.voisines(competence.getType().getDegatsZone(), true);

        // Dessiner la zone de portée du sort
        for (int i = 0; i < ePos.getNbPos(); i++) {
            Position pos = ePos.getPosition(i);
            Soldat soldat = carte.getSoldat(pos);

            if (soldat != null) {
                if (soldat instanceof Heros) {
                    dessinerCaseSurvolee(g, pos, new Color(107, 24, 24, 128)); // rouge sombre
                } else {
                    dessinerCaseSurvolee(g, pos, new Color(64, 57, 57, 128)); // gris sombre
                }
            } else {
                dessinerCaseSurvolee(g, pos, COULEUR_PORTEE_COMP); // couleur classique pour les cases vides
            }
        }

        // Dessiner la zone d'effet autour de la case survolée
        if(caseSurvolee.estValide() && ePos.contient(caseSurvolee)){
	        for (int i = 0; i < zoneAtt.getNbPos(); i++) {
	            Position pos = zoneAtt.getPosition(i);
	            dessinerCaseSurvolee(g, pos, new Color(255, 0, 0, 128)); // rouge semi-transparent
	        }
        }
    }
    // Méthode pour dessiner une case survolée ou zone d'effet
    private static void dessinerCaseSurvolee(Graphics g, Position pos, Color couleur) {
        g.setColor(couleur);
        int x = pos.getX() / 2;
        int y = pos.getY();
        int offsetX = (y % 2 == 1) ? OFFSET_X : 0;
        dessinerInterieurHexagone(g, x, y, null); // null = remplissage avec la couleur définie
    }


		/*
    public static void dessinerPorteeCompetence(
            Graphics g,
            Carte carte,
            Competence competence,
            Soldat lanceur,
            Position survolee) {

        if (survolee == null) return;

        EnsemblePosition zone = survolee.voisines(
                competence.getType().getDistance(), true);

        for (int i = 0; i < zone.getNbPos(); i++) {
            Position p = zone.getPosition(i);
            g.setColor(competence.typeCouleurAttaque(survolee));
            dessinerInterieurHexagone(g, p.getX() / 2, p.getY(), null);
        }
    }
    */
    
	public static void dessineInfosBas(Graphics g, Carte c) {
		int i = 0;
		// heros
		for (Heros heros : c.getListeHeros()) {
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
	}
}
