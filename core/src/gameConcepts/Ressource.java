package gameConcepts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import world.Application;

public class Ressource {
	static public Array<Ressource> ressourcePossible;
	static public int diverse=0; //Number of different ressources
	static public int nbprimaire=0;//nb RessourcePrimaire;
	private final float roomtaken;
	private final int basicprice;
	private final String name;
	private final float profusionnonhabitable;
    private final float profusionhabitable;
	private final boolean primaire;
	private final float facilitExtraction;
	private final int id;
	private Recette recette;

	static public void RessourceCreation(){
		if (Application.TestWorldSettings.test){
			Ressource.ressourcePossible  = Application.TestWorldSettings.RessourceCreation();
			for (Ressource ressource : ressourcePossible) {
				if (ressource.isPrimaire()) {
					nbprimaire++;
				}
			}
			diverse = ressourcePossible.size;
		}
		else {
			Ressource.ressourcePossible = new Array();
			String[] wholeFile = Gdx.files.internal("Ressource.txt").readString().split("/");
			String[] wholeLine;
			for (int i = 0; i < wholeFile.length; i++) {
				wholeLine = wholeFile[i].strip().split(";");
				diverse++;
				if (!Boolean.parseBoolean(wholeLine[7])) {
					Ressource.ressourcePossible.add(new Ressource(wholeLine[0], wholeLine[1], wholeLine[2], wholeLine[3], wholeLine[4], wholeLine[5], wholeLine[6], false));
				} else {
					Ressource.ressourcePossible.add(new Ressource(wholeLine[0], wholeLine[1], wholeLine[2], wholeLine[3], wholeLine[4], wholeLine[5], wholeLine[6], true, wholeLine[8]));
				}
			}
			for (Ressource ressource : ressourcePossible) {
				if (ressource.isPrimaire()) {
					nbprimaire++;
				}
			}
		}
	}
	static public String getName(int id) {
		return Ressource.ressourcePossible.get(id).getName();
	}
	static public int getBasicPrice(int id) {
		return Ressource.ressourcePossible.get(id).getBasicprice();
	}
	static public float getRoomTaken(int id) {
		return Ressource.ressourcePossible.get(id).getRoomtaken();
	}

	public Ressource(String name, int id, float roomtaken, int basicprice, float Profusionnonhabitable, float Profusiongeantegaseuse, float Profusionhabitable, boolean Primaire, float facilitExtraction) {
		this.facilitExtraction=facilitExtraction;
		this.roomtaken=roomtaken;this.basicprice=basicprice;this.name=name;
		profusionnonhabitable = Profusionnonhabitable;
        profusionhabitable = Profusionhabitable;
		primaire = Primaire;
		this.id=id;
	}

	public Ressource(String name, String id, String roomtaken, String basicprice, String Profusionnonhabitable, String Profusiongeantegaseuse, String Profusionhabitable, boolean Primaire){
		this(name.strip(), Integer.parseInt(id), Float.parseFloat(roomtaken), Integer.parseInt(basicprice), Float.parseFloat(Profusionnonhabitable), Float.parseFloat(Profusiongeantegaseuse), Float.parseFloat(Profusionhabitable),Primaire,0);
	}
	public Ressource(String name, String id, String roomtaken, String basicprice, String Profusionnonhabitable, String Profusiongeantegaseuse, String Profusionhabitable, boolean Primaire, String faciliteExtraction){
		this(name, Integer.parseInt(id), Float.parseFloat(roomtaken), Integer.parseInt(basicprice), Float.parseFloat(Profusionnonhabitable), Float.parseFloat(Profusiongeantegaseuse), Float.parseFloat(Profusionhabitable),Primaire, Float.parseFloat(faciliteExtraction)/5);
	}

	public String getName() {
		return name;
	}
	public int getBasicprice() {
		return basicprice;
	}
	private float getRoomtaken() {
		return roomtaken;
	}

	public Recette getRecette(){
		return this.recette;
	}

	public float getProfusionnonhabitable() {
		return profusionnonhabitable;
	}

    public float getProfusionhabitable() {
		return profusionhabitable;
	}

	public boolean isPrimaire() {
		return primaire;
	}

	void setRecette(Recette recette) {
		this.recette = recette;
	}

	public float getFacilitExtraction() {
		return facilitExtraction;
	}

	public int getId() {
		return id;
	}
	public String toString(){
		return this.name;
	}
}
