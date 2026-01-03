package wargame;

import java.awt.Graphics;

import wargame.ICompetence.TypeCompetence;
import wargame.ISoldat.TypesH;

/**
 * Représente les monstres contrôlés par l'ordinateur.
 * <p>
 * Chaque monstre possède un nom et un type (défini par {@link TypesM}), ainsi que ses caractéristiques héritées de {@link Soldat}.
 */
public class Monstre extends Soldat {
	private final TypesM TYPE;
	private final String NOM;

	/**
	 * Crée un nouveau monstre avec son type, son nom et sa position sur la carte.
	 *
	 * @param carte la carte sur laquelle le monstre est placé
	 * @param type le type du monstre, définissant ses caractéristiques
	 * @param nom le nom du monstre
	 * @param pos la position initiale du monstre sur la carte
	 */
	public Monstre(Carte carte, TypesM type, String nom, Position pos) {
		super(carte, type.getPoints(), type.getPortee(), type.getPuissance(), type.getTir(), pos);
		NOM = nom;
		TYPE = type;
		
		initialiserCompetence();
	}
	
	public TypesM getType() {
		return this.TYPE;
	}
	
	public String getNom() {
		return this.NOM;
	}
	
	public void initialiserCompetence() {
		ajouterCompetence(new Competence(TypeCompetence.COUP_EPEE));

	}
	
	public String trouverImg() {
		String path = "./images/persos/";
		switch(this.getType()) {
		   case TROLL:
			   path += "troll";
			   break;
		   case ORC:
			   path += "orc";
			   break;
		   case GOBELIN:
			   path += "gobelin";
			   break;
		   case DEMON:
			   path += "demon";
			   break;
		}
	    path += "_map.png";
	    return path;
	}
}
