package wargame;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class PanneauJeu extends JFrame implements IConfig {

	public  PanneauJeu() {
		int[][] matrice = new int[25][25];
		
		
		JPanel panneau = new JPanel(new BorderLayout());
		
		for(int i = 0; i < 25; i++) {
			for(int j = 0; j < 25; j++) {
				matrice[i][j] = i*25+j;
			}
		}
		
		Object[][] donnees = new Object[25][25];  // Matrice d'objets
	       for (int i = 0; i < 25; i++) {
	           for (int j = 0; j < 25; j++) {
	               donnees[i][j] = matrice[i][j];  // Remplir la matrice d'objets avec les valeurs de int
	           }
	    }
	       
	       String[] colonnes = new String[25];
	        for (int i = 0; i < 25; i++) {
	            colonnes[i] = ""+(i + 1);
	        }
		
		DefaultTableModel modeleTable = new DefaultTableModel(donnees,colonnes);
		
		JTable table = new JTable(modeleTable);
		
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
