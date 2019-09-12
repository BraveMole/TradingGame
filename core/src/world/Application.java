package world;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import commerce.MarketPlace;
import gameConcepts.Recette;
import gameConcepts.Ressource;
import gameObjets.Planete;
import gameObjets.Vaisseau;

public class Application extends ApplicationAdapter {
	private World world;

	@Override
	public void create () {
		TestWorldSettings.test=true;
		this.ressourceInit();
		this.world = new World();
	}

	@Override
	public void resize(int width, int height) {
		this.world.resize(width, height);
	}

	private void ressourceInit() {
		Ressource.RessourceCreation();
		Recette.RecetteCreation();
	}

	@Override
	public void render () {
		this.world.draw();
		this.world.act();
	}

	@Override
	public void dispose () {

	}

	public static class TestWorldSettings {
		public static boolean test;

		public static Array<Ressource> RessourceCreation(){
			Array<Ressource> listeRessourcePossible = new Array();
			listeRessourcePossible.add(new Ressource("Fer",0,5,10, 0.5f,0.0f,0.5f,true,1));
			listeRessourcePossible.add(new Ressource("Cereale",1,1,10, 0.5f,0.0f,0.5f,true,1));
			listeRessourcePossible.add(new Ressource("Or",2,5,20, 0.5f,0.0f,0.5f,true,1));
			listeRessourcePossible.add(new Ressource("Bijoux",3,5,100, 0,0,0,false,0));
			return listeRessourcePossible;
		}

		public static Array<Recette> RecetteCreation(){
			Array<Recette> listeRecette = new Array<>();
			listeRecette.add(new Recette("Bijoux",3,5,new int[] {1,0,1,0}));
			return listeRecette;
		}

		public static float[] produitConsommePop(){
			return new float[] {0,0.1f,0,0.01f};
		}
		public static int[] prioriteAchatPop(){
			return new int[] {15,0,15,1};
		}

		public static Array<Actor> createTestWorld(Assets assets){
			Array<Actor> listeObjet = new Array<>();
			float tailleTile = 10000f;
			int nbtile = (int)Math.ceil(100000/tailleTile);
			float[] gridindex = new float[nbtile];
			for (int i = 1; i <= nbtile; i++) {
				gridindex[i-1]=tailleTile*(i)-100000/2;
			}
			MarketPlace.setGridindex(gridindex);
			MarketPlace.initialisation();
			Planete p = new Planete(new Sprite((assets.getPlaneteHabitableTexture(0))),400,0,0,assets.getPlaneteFar(),100,true,"Planet Fer et or",true);
			p.addRessource("Fer");
			p.addIndustrie("Fer");
			p.addRessource("Or");
			p.addIndustrie("Or");
			p.addRessource("Cereale");
			p.addIndustrie("Cereale");
			listeObjet.add(p);
			p = new Planete(new Sprite((assets.getPlaneteHabitableTexture(1))),400,2000,2000,assets.getPlaneteFar(),100,true,"Planete bijoux",true);
			p.addIndustrie("Bijoux");
            Vaisseau v = new Vaisseau(1000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),500,0,0);
			listeObjet.add(p);
            listeObjet.add(v);
			return listeObjet;
		}

	}
}
