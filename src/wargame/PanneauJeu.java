package wargame;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

public class PanneauJeu extends JFrame implements IConfig {
	
	public  PanneauJeu() {
		int[][] matrice = new int[25][25];
		
		
		JPanel panneau = new JPanel(new BorderLayout());
		
		
		Object[][] donnees = new Object[25][25];  // Matrice d'objets
	       for (int i = 0; i < 25; i++) {
	           for (int j = 0; j < 25; j++) {
	               donnees[i][j] = 1;  // Remplir la matrice d'objets avec les valeurs de int
	           }
	    }
	       
	       String[] colonnes = new String[25];
	        for (int i = 0; i < 25; i++) {
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
				
				 if (value instanceof Integer) {
	                    int val = (Integer) value;
	                    if (val == 1) {
	                        c.setBackground(COULEUR_FORET);
	                        setText(""); // vide si tu veux juste la couleur
	                    } else {
	                        c.setBackground(Color.WHITE);
	                        setText(""); // vide aussi
	                    }
	             } else {
	                    c.setBackground(Color.WHITE);
	                }
			
			
			
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
