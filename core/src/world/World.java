package world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import commerce.MarketPlace;
import gameConcepts.Population;
import gameUi.WorldInputProcessor;

public class World extends Stage implements InputProcessor{

	static public int nbVaisseauCree=0;
	static public int nbImplantationCree=0;


	private WorldInputProcessor inputProcessor;

	public Assets getAssetmanager() {
		return assetmanager;
	}

	private Assets assetmanager;

	World() {
		this.assetmanager = new Assets();
		assetmanager.finishLoading();
		Population.initPopBesoin();
        this.createWorld();
		inputProcessor = new WorldInputProcessor(this.getViewport(),this);
	}

	private void createWorld() {
		if (Application.TestWorldSettings.test){
			Array<Actor> listeObjet = Application.TestWorldSettings.createTestWorld(assetmanager);
			for (Actor actor : listeObjet) {
				this.addActor(actor);
			}
		}
		else {
			GalaxyGenerator galaxyGenerator = new GalaxyGenerator(20000000, 1, 1000, assetmanager, 4000000);
			Array<Actor> listeObjet = galaxyGenerator.getListeObjet();
			for (Actor actor : listeObjet) {
				this.addActor(actor);
			}
		}
	}

	void resize(int width, int height) {
		this.getViewport().setScreenBounds(0, 0, width, height);
		this.getViewport().getCamera().viewportHeight=this.getViewport().getScreenHeight();
		this.getViewport().getCamera().viewportWidth=this.getViewport().getScreenWidth();
		this.inputProcessor.resize();
	}

	public void act() {
		MarketPlace.act();
	    this.inputProcessor.act();
		if (!this.inputProcessor.getPause()) {
			super.act();
		}
	}

	@Override
	public void draw() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
		this.inputProcessor.drawBackground();
		super.draw();
		this.inputProcessor.draw();
	}

}
