package wargame;
import javax.swing.*;

import wargame.ISoldat.TypesH;
import wargame.ISoldat.TypesM;
import wargame.Obstacle.TypeObstacle;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

public class PanneauJeu extends JFrame implements IConfig {
	
	public PanneauJeu() {
		Carte c = new Carte();
		
		JPanel panneau = new JPanel(new BorderLayout());
		
		
		Object[][] donnees = new Object[LARGEUR_CARTE][HAUTEUR_CARTE];  // Matrice d'objets
	    for (int i = 0; i < LARGEUR_CARTE; i++) {
	    	for (int j = 0; j < HAUTEUR_CARTE; j++) {
	    		Element e = c.getElement(new Position(i,j));
	    		if(e == null) {
	    			donnees[i][j] = null;
	    		}else {
	    			donnees[i][j] = c.getElement(new Position(i,j));  // Remplir la matrice d'objets avec les valeurs de int
	    		}
	    	}
	    }
	       
	    String[] colonnes = new String[HAUTEUR_CARTE];
	    for (int i = 0; i < HAUTEUR_CARTE; i++) {
	        colonnes[i] = "";
	    }
		
		DefaultTableModel modeleTable = new DefaultTableModel(donnees,colonnes);
		
		JTable table = new JTable(modeleTable);
		
		// Désactiver l'édition des cellules
        table.setDefaultEditor(Object.class, null);

        // Désactiver la sélection des cellules
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
		
        table.getTableHeader().setVisible(false);
		
		// couleur pour affichage
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				
				if (value instanceof Heros) {
	                 c.setBackground(COULEUR_FORET);
	            }else if (value instanceof Monstre) {
	                 c.setBackground(COULEUR_MONSTRES);
	            } else if (value instanceof Obstacle){
	            	System.out.println(((Obstacle) value).getType()+" donne le type");
	            	switch (((Obstacle) value).getType() ) {
	            		case ROCHER:
	            			c.setBackground(COULEUR_ROCHER);
	            			break;
	            		case EAU:
	            			c.setBackground(COULEUR_EAU);
	            			break;
	            		case FORET:
	            			c.setBackground(COULEUR_FORET);
	            			break;
	            	}
	            }else {
	            	c.setBackground(COULEUR_VIDE);
	                
	            }
				setText("");
			return c;
			
			}
				
		});
		
		table.setRowHeight(NB_PIX_CASE); // hauteur des cases
        table.setGridColor(Color.GRAY);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		JPanel panelHaut = new JPanel();
	    panelHaut.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton labelBouton = new JButton("Fin de Tour");
		panelHaut.add(labelBouton);
		
		JLabel labelText = new JLabel("Voici le bouton fin de tour");
		panelHaut.add(labelText);
		
		panelHaut.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panneau.add(scrollPane, BorderLayout.CENTER);  // Placer la table au centre
        panneau.add(panelHaut, BorderLayout.NORTH); 
		
		JFrame fenetre = new JFrame("Comptez-vous avoir plus de 5 au prochain contrôle de pause café ?");
		
		
		fenetre.add(panneau);
		fenetre.setMinimumSize(new java.awt.Dimension(1000,600));
		fenetre.pack();
		fenetre.setLocationRelativeTo(null);
		fenetre.setVisible(true);
	   }
	      

}
