package gameUi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import gameConcepts.Ressource;
import gameObjets.Planete;

public class InterfacePlanete extends Interface {

	private Planete p;

	InterfacePlanete(Planete p, Skin skin, float width, float height) {
		super(skin,width *0.2f,height*0.9f,width/40f,height*0.05f,"InterfacePlanete");
		this.p=p;
		Label nameLabel = new Label(this.p.toString(), this.skin,"title");
		table.add(nameLabel);
		table.row();
		table.top();
		this.createPopulation();
		this.createListeIndustrie();
		this.createListeRessourceMinable();
	}

	@Override
	public void resize(float scaleX, float scaleY){

	}



	private Planete getPlanete() {
		return this.p;
	}

	public void clear() {
		super.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof InterfacePlanete) {
			InterfacePlanete f = (InterfacePlanete)o;
			return f.getPlanete().getId() == this.p.getId();
		}
		return false;
	}

	private void createListeRessourceMinable(){
		Table listeRessource = new Table(skin);
		for (Ressource ressource : p.getRessourcePrimaireDisponible()) {
			listeRessource.add(new Label(ressource.toString(),skin)).left().fillX().expandX().row();
		}
		ScrollPane scroll = new ScrollPane(listeRessource,skin);
		scroll.setScrollingDisabled(true,false);
		scroll.layout();
		scroll.setTouchable(Touchable.enabled);
		table.add(new Label("Ressource disponibles :",skin)).pad(10f).center().row();
		table.add(scroll).expandX().fillX().height(table.getHeight()/5f);
	}

	private void createPopulation(){
		if (p.getPopulation()!=null){
			table.add(new ButtonPop(skin,p.getPopulation())).pad(15f).row();
		}
	}

	private void createListeIndustrie() {
		ListIndustrie listeIndustrie = new ListIndustrie(skin);
		Array<Label> labelIndustries = new Array<>();
		for (int i = 0; i < this.p.getListeIndustrie().size; i++) {
			labelIndustries.add(new LabelIndustrie(this.p.getListeIndustrie().get(i).toString(),skin,p.getListeIndustrie().get(i)));
		}
		listeIndustrie.setItems(labelIndustries);
		ScrollPaneIndustrie scroll = new ScrollPaneIndustrie(listeIndustrie,skin);
		scroll.setScrollingDisabled(true,false);
		scroll.layout();
		this.table.add(new Label("Industrie :",skin));
		this.table.row();
		this.table.add(scroll).expandX().fillX().height(table.getHeight()/5f).row();
	}

	@Override
	public void draw(Batch batch, float parentalpha){
		this.act(Gdx.graphics.getDeltaTime());
		super.draw(batch, parentalpha);
	}
}
