package wargame.obstacle;
import java.awt.Color;

import wargame.Element;
import wargame.IConfig;
import wargame.position.Position;

public class Obstacle extends Element implements IConfig {
	
	public enum TypeObstacle {
		ROCHER (COULEUR_ROCHER), FORET (COULEUR_FORET), EAU (COULEUR_EAU);

		private final Color COULEUR;

		TypeObstacle(Color couleur) { COULEUR = couleur; }

		public static TypeObstacle getObstacleAlea() {
			return values()[(int)(Math.random()*values().length)];
		}
	}

	private TypeObstacle TYPE;
	
	public Obstacle(TypeObstacle type, Position pos) { 
		TYPE = type;
		this.pos = pos;
	}
	
	public TypeObstacle getType() {
		return this.TYPE;
	}
	
	public String toString() { 
		return ""+TYPE;
	}
}