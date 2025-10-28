package wargame;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PanneauJeu extends JFrame implements IConfig {

	public  PanneauJeu() {
		super("Comptez-vous avoir plus de 5 au prochain contrôle de pause café ?");	

		JPanel panneau = new JPanel(new FlowLayout());
		
		File imageFile = new File("src/wargame/panneauAffiche.gif");
		System.out.println("Chemin absolu de l'image : " + imageFile.getAbsolutePath());
		System.out.println("Image existe ? " + imageFile.exists());
		
		
		
	    ImageIcon img = new ImageIcon("src/wargame/panneauAffiche.gif");
	    JLabel jLabelImg = new JLabel(img);
	    panneau.add(jLabelImg);
	    
		
		JLabel labelText = new JLabel("Tu t'es fais Kostiné !");
		panneau.add(labelText);
		
		add(panneau);
		
	    pack();
	    setVisible(true);
	   }
	      

}
