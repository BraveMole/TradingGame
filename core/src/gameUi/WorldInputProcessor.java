package gameUi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import gameConcepts.SuperActor;
import gameObjets.Planete;
import gameObjets.Soleil;
import gameObjets.Vaisseau;
import world.World;

public class WorldInputProcessor implements InputProcessor{
	public static float worldZoom;
	private SuperActor focused;
	private Image background;
	private Vector3 mousePosition;
	private boolean rightButtonHold=false;
	private Vector3 originMoveCam;
	private Viewport viewport;
	private World worldStage;
	private Stage hudStage;
	private Stage backStage;
	private Skin skin;
	private Array<Interface> listeInterface;
	private boolean pause;
	private OrthographicCamera backCamera;
	private InterfacePlanete ip;
	private InputMultiplexer multi;
	private float panduration= 1f;
	private float timeToCameraPositionTarget=0;
	private float xcameraori;
	private float ycameraori;
	private float xcameradest;
	private float ycameradest;
	private float zoomduration=0.4f;
	private float cameraZoomOrigin=1f;
	private float cameraZoomTarget =1f;
	private int zoomlevel;
	static public int[] palierZoom = new int[]{10,35,300};
	private float timeToCameraZoomTarget=0;
	private AlphaAction fade;


	public WorldInputProcessor(Viewport viewport, World stage) {
		this.zoomlevel=0;
		ClickListener.visualPressedDuration=0.1f;
        skin = new Skin(Gdx.files.internal("shade/skin/uiskin.json"));
		this.listeInterface= new Array<>();
		this.worldStage=stage;
		this.hudStage=new Stage();
		this.backStage=new Stage();
		this.addFpsCounter();
		multi = new InputMultiplexer();
		multi.addProcessor(this.hudStage);
		multi.addProcessor(this);
		Gdx.input.setInputProcessor(multi);
		this.pause=false;
		this.viewport=viewport;
		this.skin.getFont("font-label").getData().setScale(viewport.getWorldHeight()/500f);
		this.skin.getFont("font-title").getData().setScale(viewport.getWorldHeight()/800f);
		this.mousePosition=new Vector3(0,0,0);
		this.originMoveCam=new Vector3(0,0,0);

		this.setBackground();
		this.worldStage.getRoot().setCullingArea(new Rectangle(viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[0].y,
				viewport.getCamera().frustum.planePoints[2].x-viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[2].y-viewport.getCamera().frustum.planePoints[0].y));
	}


	public void act(){
		this.renderZoom();
		this.renderPan();
	    this.hudStage.act();
	    this.backStage.act();
    }

	private void addFpsCounter(){
	    this.hudStage.addActor(new Label("bla",skin){
            @Override
            public void act(float delta) {
                this.setText("Fps :"+(Gdx.graphics.getFramesPerSecond()));
                super.act(delta);
            }
        });
    }

	public void drawBackground() {
		this.backStage.draw();
	}

	private void setBackground(){
		fade = new AlphaAction();
		fade.setDuration(0.3f);
		fade.setAlpha(0.55f);
		this.background= new Image(this.worldStage.getAssetmanager().getBackgroundTexture(0));
		this.background.addAction(fade);
		this.backStage.addActor(background);
		this.backStage.getBatch().setColor(0f,0,0,0.1f);
		this.backCamera=new OrthographicCamera(Gdx.graphics.getWidth()*2,Gdx.graphics.getHeight()*2);
		this.backCamera.translate(background.getWidth()/2,background.getHeight()/2);
		this.backCamera.zoom=0.6f;
		this.backStage.getViewport().setCamera(this.backCamera);
	}

	public void resize() {
		float scaleX= ((float)this.viewport.getScreenWidth()/(float)this.hudStage.getViewport().getScreenWidth());
		float scaleY= ((float)this.viewport.getScreenHeight()/(float)this.hudStage.getViewport().getScreenHeight());
		if (scaleX!=1 || scaleY!=1) {
			this.skin.getFont("font-label").getData().setScale(viewport.getScreenHeight()/500f);
			this.skin.getFont("font-title").getData().setScale(viewport.getScreenHeight()/800f);
			this.multi.removeProcessor(0);
			Stage tampon = this.hudStage;
			this.hudStage = new Stage();
			this.multi.addProcessor(0,this.hudStage);
			for (int i = 0; i < tampon.getActors().size; i++) {
				if (tampon.getActors().get(i) instanceof Interface) {
					((Interface) tampon.getActors().get(i)).resize(scaleX, scaleY);
				}
				this.hudStage.addActor(tampon.getActors().get(i));
			}
		}
	}

	public void draw() {
		this.hudStage.draw();
	}

	public boolean getPause() {
		return this.pause;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.SPACE) {
			this.pause=!this.pause;
		}
		return false;
	}

	private void renderZoom(){
		if (timeToCameraZoomTarget >= 0){
			this.worldStage.getRoot().getCullingArea().set(viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[0].y,
					viewport.getCamera().frustum.planePoints[2].x-viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[2].y-viewport.getCamera().frustum.planePoints[0].y);
			timeToCameraZoomTarget -= Gdx.graphics.getDeltaTime();
			float progress = timeToCameraZoomTarget < 0 ? 1 : 1f - timeToCameraZoomTarget /zoomduration;
			((OrthographicCamera)this.viewport.getCamera()).zoom = Interpolation.pow3Out.apply(cameraZoomOrigin, cameraZoomTarget, progress);
			worldZoom=((OrthographicCamera)this.viewport.getCamera()).zoom;
		}
		this.setZoomlevel();
	}

	private void zoomBy(float zoomAmount){
		this.zoomTo(cameraZoomTarget*(zoomAmount));
	}
	private void zoomTo(float zoomAmount){
		cameraZoomOrigin=((OrthographicCamera)this.viewport.getCamera()).zoom;
		worldZoom=cameraZoomOrigin;
		cameraZoomTarget = zoomAmount;
		timeToCameraZoomTarget = zoomduration;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	private boolean checkZoomlevel(){
		float zoom=((OrthographicCamera)this.worldStage.getCamera()).zoom;
		int newzoomlevel;
		if (zoom < palierZoom[0]){
			newzoomlevel=0;
		}
		else if (zoom < palierZoom[1]){
			newzoomlevel=1;
		}
		else if (zoom < palierZoom[2]){
			newzoomlevel=2;
		}
		else {
			newzoomlevel=3;
		}
		if (newzoomlevel!=zoomlevel){
			if (newzoomlevel==3){
				this.zoomTo(20000f);
			}
			zoomlevel=newzoomlevel;
			return true;
		}
		return false;
	}

	private void setZoomlevel(){
		if (this.checkZoomlevel()){
			for (Actor actor : this.worldStage.getActors()) {
				if (actor instanceof SuperActor) {
					((SuperActor)actor).setZoomlevel(this.zoomlevel);
				}
			}
			fade.reset();
			fade.setDuration(0.3f);
			switch (zoomlevel){
				case(0):
				case(1):
					fade.setAlpha(0.7f);
					this.background.addAction(fade);
					break;
				case(2):
					fade.setAlpha(0.3f);
					this.background.addAction(fade);
					break;
				case(3):
					fade.setAlpha(0f);
					this.background.addAction(fade);
			}
		}
	}


	@Override
	public boolean scrolled(int amount) {
		if (this.hudStage.hit(Gdx.input.getX(),Gdx.input.getY(),false)==null) {
			this.zoomBy( 1 + amount * 0.2f);
			this.viewport.getCamera().update();
			this.worldStage.getRoot().getCullingArea().set(viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[0].y,
					viewport.getCamera().frustum.planePoints[2].x-viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[2].y-viewport.getCamera().frustum.planePoints[0].y);
			return true;
		}
		return false;
	}

	private void renderPan(){
		if(this.timeToCameraPositionTarget>0){
			this.worldStage.getRoot().getCullingArea().set(viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[0].y,
					viewport.getCamera().frustum.planePoints[2].x-viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[2].y-viewport.getCamera().frustum.planePoints[0].y);
			this.timeToCameraPositionTarget-=Gdx.graphics.getDeltaTime();
			float progress = timeToCameraPositionTarget < 0 ? 1 : 1f - timeToCameraPositionTarget/panduration;
			float x = Interpolation.pow3Out.apply(xcameraori,xcameradest, progress);
			float y = Interpolation.pow3Out.apply(ycameraori,ycameradest, progress);
			this.backCamera.translate((x-this.worldStage.getCamera().position.x)*0.002f,(y-this.worldStage.getCamera().position.y)*0.002f);
			this.worldStage.getCamera().position.set(x,y,0);
			this.worldStage.getCamera().update();
		}
	}

	private void panTo(float x, float y){
		this.timeToCameraPositionTarget=this.panduration;
		this.xcameradest=x;this.ycameradest=y;
		this.xcameraori=this.worldStage.getCamera().position.x;
		this.ycameraori=this.worldStage.getCamera().position.y;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		switch (button){
			case Input.Buttons.RIGHT:
				this.rightButtonHold=true;
				originMoveCam.set(screenX,screenY,0);
				break;
			case Input.Buttons.LEFT:
				Actor hit=this.hudStage.hit(screenX,this.viewport.getScreenHeight()-screenY,true);
				if (hit!=null) {
					focused=null;
					Actor parent = hit.getParent();
					while(!(parent instanceof Interface) && parent.hasParent()){
						parent=parent.getParent();
					}
					if (parent instanceof Interface){
						((Interface)parent).setFocus();
					}
					if (hit instanceof Image){
						if (hit.getName().equals("barre")){
							assert parent instanceof Interface;
							((Interface)parent).dragged(screenX,this.viewport.getScreenHeight()-screenY);
						}
					}
					else if (hit instanceof ListIndustrie){
						if (((ListIndustrie<Object>)hit).getSelected()instanceof LabelIndustrie){
							this.addInterface(new InterfaceIndustrie(((LabelIndustrie)(((ListIndustrie)hit).getSelected())).getIndustrie(), skin, this.viewport.getScreenWidth(), this.viewport.getScreenHeight()));
						}
					}
					else if (hit instanceof ButtonPop){
						this.addInterface(new InterfacePopulation(skin,this.viewport.getScreenWidth(), this.viewport.getScreenHeight(),((ButtonPop)hit).getP()));
					}
					this.hudStage.getActors().sort();
				}
				else {
					this.viewport.getCamera().unproject(mousePosition.set(screenX, screenY, 0));
					SuperActor hit2 =(SuperActor)this.worldStage.hit(this.mousePosition.x, this.mousePosition.y, true);
					if ( hit2!= null) {
						if (hit2.equals(focused)){
							if (hit2.positionEqual(this.worldStage.getCamera().position)){
								if (hit2 instanceof Planete){
									this.zoomTo(5f);
								}
								else if(hit2 instanceof Soleil){
									this.zoomTo(190f);
									this.backStage.getActors().removeValue(background,true);
									this.background=new Image(this.worldStage.getAssetmanager().getBackgroundTexture(((Soleil)hit2).getBackground()));
									this.background.setSize(Gdx.graphics.getWidth()*2,Gdx.graphics.getHeight() *2);
									fade.reset();
									fade.setDuration(0);
									fade.setAlpha(0.3f);
									this.background.addAction(fade);
									this.backStage.addActor(background);
									this.background.layout();
									this.backCamera.setToOrtho(false,Gdx.graphics.getWidth()*2,Gdx.graphics.getHeight()*2);
									this.backCamera.translate(background.getWidth()/6,background.getHeight()/4);
									this.backCamera.zoom=0.6f;
								}
							}
							else {
								this.panTo(hit2.getX(), hit2.getY());
							}
						}
						else{
							focused=hit2;
						}
						if (hit2 instanceof Planete) {
							this.addInterface(new InterfacePlanete((Planete)hit2, skin, this.viewport.getScreenWidth(), this.viewport.getScreenHeight()));
						}
						if (hit2 instanceof Vaisseau){
							this.addInterface(new InterfaceVaisseau((Vaisseau)hit2,skin,this.viewport.getScreenWidth(), this.viewport.getScreenHeight()));
						}
					}
					else{
						focused=null;
					}
				}
				break;
		}
		return false;
	}

	private void addInterface(Interface inter){
		if (!this.hudStage.getActors().contains(inter,false)) {
			if (inter instanceof InterfacePlanete){
				if (!inter.equals(ip)) {
					this.listeInterface.removeValue(ip,false);
					this.hudStage.getActors().removeValue(ip,false);
					ip= (InterfacePlanete) inter;
				}
			}
			this.listeInterface.add(inter);
			this.hudStage.addActor(inter);
		}
	}


	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		switch (button){
			case Input.Buttons.RIGHT:
				this.rightButtonHold=false;
				break;
			case Input.Buttons.LEFT:
				Actor hit=this.hudStage.hit(screenX,this.viewport.getScreenHeight()-screenY,true);
				if (hit!=null) {
					if (hit instanceof Button) {
						if (hit.getName().equals("Close")) {
							this.listeInterface.removeValue((Interface) hit.getParent(), false);
							this.hudStage.getActors().removeValue(hit.getParent(), false);
						}
					}
				}
				for (int i=0;i<this.listeInterface.size;i++){
					this.listeInterface.get(i).clickGaucheRelease();
				}
				break;
		}
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY,int pointer) {
		if (this.rightButtonHold) {
			float xtranslate = this.originMoveCam.sub(screenX, screenY,0).scl(((OrthographicCamera)this.viewport.getCamera()).zoom).x;
			float ytranslate = -this.originMoveCam.y*0.5f*this.viewport.getScreenWidth()/this.viewport.getScreenHeight();
			((OrthographicCamera)this.viewport.getCamera()).translate(xtranslate,ytranslate);
			this.worldStage.getRoot().getCullingArea().set(viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[0].y,
					viewport.getCamera().frustum.planePoints[2].x-viewport.getCamera().frustum.planePoints[0].x,viewport.getCamera().frustum.planePoints[2].y-viewport.getCamera().frustum.planePoints[0].y);
			this.backCamera.translate(xtranslate*0.002f,ytranslate*0.002f);
			this.originMoveCam.set(screenX,screenY,0);
		}
		else{
			for (int i=0;i<this.listeInterface.size;i++){
				this.listeInterface.get(i).drag(screenX,this.viewport.getScreenHeight()-screenY);
			}
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
}
