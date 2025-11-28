package wargame;
import java.awt.Color;

public class Terrain extends Element implements IConfig {
	
	public enum TypeTerrain {
		HERBE (COULEUR_HERBE), PONT (COULEUR_PONT);

		private final Color COULEUR;

		TypeTerrain(Color couleur) { COULEUR = couleur; }

		public static TypeTerrain getObstacleAlea() {
			return values()[(int)(Math.random()*values().length)];
		}
	}

	private TypeTerrain TYPE;
	
	public Terrain(TypeTerrain type, Position pos) { 
		TYPE = type;
		this.pos = pos;
	}
	
	public TypeTerrain getType() {
		return this.TYPE;
	}
	
	public String toString() { 
		return ""+TYPE;
	}
}