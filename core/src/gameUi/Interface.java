package gameUi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Interface extends Group implements Comparable<Actor> {

	private float orgx;
	private float orgy;
	private boolean draging;
	private Image im;
	private Image barre;
	private Button closeButton;
	protected Table table;
	private String type;
	private float height;
	private float widht;
	protected Skin skin;
	private int focus;
	static private int maxfocus=0;

	public Interface(Skin skin, float width, float height, float x, float y, String type){
		this.focus=maxfocus;
		maxfocus++;
		this.setX(x);
		this.setY(y);
		this.type=type;
		this.height=height;
		this.widht=width;
		this.skin=skin;
		this.imageFond();
	}

	private void imageFond(){
		switch (type){
			case("InterfacePlanete"):
				im = new Image(new Texture(Gdx.files.internal("InterfacePlanete.jpg")));
				break;
			case("InterfaceIndustrie"):
				im = new Image(new Texture(Gdx.files.internal("InterfaceIndustrie.jpg")));
				break;
			case("InterfaceVaisseau"):
				im = new Image(new Texture(Gdx.files.internal("InterfaceVaisseau.jpg")));
				break;
			case ("InterfacePopulation"):
				im = new Image(new Texture(Gdx.files.internal("InterfaceVaisseau.jpg")));
				break;
		}
		im.setHeight(height);
		im.setWidth(widht);
		im.getColor().a=0.9f;
		im.setTouchable(Touchable.disabled);
		barre = new Image(new Texture(Gdx.files.internal("barmarron.png")));
		barre.setPosition(0, im.getHeight());
		barre.setName("barre");
		closeButton = new Button(skin, "default");
		closeButton.removeListener(closeButton.getClickListener());
		closeButton.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				closeButton.getClickListener().setVisualPressed(true);
				return false;
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				closeButton.getClickListener().setVisualPressed(false);
			}
		});
		closeButton.setName("Close");
		closeButton.setTouchable(Touchable.enabled);
		closeButton.setHeight(closeButton.getHeight()/2f);
		closeButton.setWidth(closeButton.getWidth()*0.5f);
		closeButton.setPosition(widht- closeButton.getWidth()*1.2f,height);
		barre.setHeight(closeButton.getHeight()*1.1f);
		barre.setWidth(im.getWidth());
		table = new Table(this.skin);
		table.setHeight(im.getHeight());
		table.setWidth(im.getWidth()*0.95f);
		table.setPosition(widht*0.025f,0);
		this.addActor(im);
		this.addActor(barre);
		this.addActor(closeButton);
		this.addActor(table);
	}

	public void resize(float scaleX, float scaleY){
		this.setX(this.getX()*scaleX);
		this.setY(this.getY()*scaleY);
		im.setHeight(im.getHeight()*scaleY);
		im.setWidth(im.getWidth()*scaleX);
		barre.setPosition(0, im.getHeight());
		table.setHeight(im.getHeight());
		table.setWidth(im.getWidth());
		closeButton.setHeight(im.getHeight()/30f);
		closeButton.setWidth(im.getWidth()/15f);
		closeButton.setPosition(im.getWidth()- closeButton.getWidth(),im.getHeight());
		barre.setHeight(closeButton.getHeight()*0.5f);
	}

	public void clickGaucheRelease() {
		this.draging=false;
	}

	public void dragged(float x, float y){
		this.orgy=y;this.orgx=x;
		this.draging=true;
	}

	public void drag(float x, float y) {
		if (this.draging) {
			this.setPosition(this.getX()+x-this.orgx,this.getY() + y-this.orgy);
			this.orgx=x;this.orgy=y;
		}
	}

	public void setFocus(){
		this.focus=maxfocus;
		maxfocus++;
	}

	public int getFocus() {
		return this.focus;
	}

	@Override
	public int compareTo(Actor o) {
		if (o instanceof Interface){
			return this.focus-((Interface)o).getFocus();
		}
		return 1;
	}
}
