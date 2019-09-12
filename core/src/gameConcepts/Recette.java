package gameConcepts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import world.Application;

public class Recette {
	private static Array<Recette> PossibleRecette;
	final private int[] matpremiere;
	final private int coutmanoeuvre;
	final private int idRessourceProduite;
	final private String name;

	public Recette(String name, int idRessourceProduite, int coutmanoeuvre, int[] matpremiere){
		this.name=name;
		this.idRessourceProduite=idRessourceProduite;
		this.coutmanoeuvre=coutmanoeuvre;
		this.matpremiere=matpremiere;
		Ressource.ressourcePossible.get(idRessourceProduite).setRecette(this);
	}

	private Recette(String recette[]) {
		this.name=recette[0];
		this.idRessourceProduite= Integer.parseInt(recette[1]);
		this.coutmanoeuvre= Integer.parseInt(recette[2]);
		matpremiere = new int[Ressource.diverse];
		for (int i=3;i<recette.length;i++){
			if (!recette[i].isEmpty()) {
				matpremiere[i-3] = Integer.parseInt(recette[i]);
			}
		}

	}
	
	public int[] getMatpremiere() {
		return matpremiere;
	}
	public int getCoutmanoeuvre() {
		return coutmanoeuvre;
	}
	public static void RecetteCreation(){
		if (Application.TestWorldSettings.test){
			Recette.PossibleRecette = Application.TestWorldSettings.RecetteCreation();
		}
		else{
			Recette.PossibleRecette = new Array<>();
			String[] wholeFile = Gdx.files.internal("Recette.txt").readString().split("/");
			String[] wholeLine;
			for (int i = 0;i<wholeFile.length-1;i++){
				wholeLine = wholeFile[i].strip().split(";");
				Recette.PossibleRecette.add(new Recette(wholeLine));
			}
		}
	}

}
