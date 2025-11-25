package wargame;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;

import wargame.position.*;

public class PanneauInfo extends JPanel implements IConfig {
	private Position caseSurvoleePI;
	
	public PanneauInfo() {
		this.setPreferredSize(new Dimension(LARGEUR_CARTE*NB_PIX_CASE, NB_PIX_CASE));
	}
	
	public void paintComponent(Graphics g) {
	}
	
	public void setCaseSurvoleePI(Position pos) {
		this.caseSurvoleePI = pos;
	}
}
