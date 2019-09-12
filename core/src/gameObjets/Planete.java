package gameObjets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import gameConcepts.Ressource;
import world.Assets;

import java.util.Random;

public class Planete extends Implantation {
	private static int minsize=200;
	private static int solarsystemsize=35000;
	private static int mindistanceSun=8000;
	private static int maxsize = 400;
	private static int maxpop = 200;

	static public Planete planeteGenerator(float xSun, float ySun, Assets assetmanager){
		Random rand = new Random();
		float habitabilite = rand.nextFloat();
		int size = rand.nextInt(maxsize -minsize)+minsize;
        Sprite sprite = new Sprite();
        int textureId;
        if (habitabilite>0.8){
		    textureId = rand.nextInt(assetmanager.getNbHabitableTexture()-1);
		    sprite.setRegion(assetmanager.getPlaneteHabitableTexture(textureId));
        }
		else{
            textureId = rand.nextInt(assetmanager.getNbInhabitableTexture()-1);
            sprite.setRegion(assetmanager.getPlaneteInhabitableTexture(textureId));
        }
        int population = rand.nextInt(Math.max((int) (habitabilite*maxpop),1));
		int distance = rand.nextInt(solarsystemsize-mindistanceSun)+mindistanceSun;
		float angle = rand.nextFloat()*2f*3.14f;
		return new Planete(sprite,size,xSun+(float)(Math.cos(angle)*distance),ySun+(float)(Math.sin(angle)*distance),assetmanager.getPlaneteFar(),population,habitabilite>0.8,assetmanager.getPlanetName());
	}

	public boolean addRessource(String ressourceName){
		for (Ressource ressource : Ressource.ressourcePossible) {
			if (ressource.getName().equals(ressourceName)){
				this.getRessourcePrimaireDisponible().add(ressource);
				return true;
			}
		}
		return false;
	}


	public Planete(Sprite sprite, int size,float x, float y, Texture far, int population, boolean habitable, String name, boolean test){
		super(sprite,size,x,y,sprite.getTexture(),far,population,name,habitable);
	}

	public Planete(Sprite sprite, int size, float x, float y, Texture far, int population, boolean habitable, String name) {
		super(sprite,size,x,y,sprite.getTexture(),far,population,name,habitable);
		generateRessource(this);
		generateIndustries(this);
	}
}
