package gameUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import gameConcepts.Ressource;
import gameObjets.Implantation;
import gameObjets.Vaisseau;

public class InterfaceVaisseau extends Interface {
    private Vaisseau vaisseau;
    private Skin skin;
    private Table ressourceInventory;
    private Label labelVitesse;
    private int indice;
    private Label labelDestination;
    private List listInventory;

    public InterfaceVaisseau(Vaisseau v,Skin skin, float width, float height) {
        super(skin, width*0.2f, height*0.4f, width*0.7f, height*0.2f, "InterfaceVaisseau");
        this.vaisseau=v;
        this.skin=skin;
        this.labelVitesse=new Label("0.0",skin);
        this.labelDestination=new Label("",skin);
        this.vaisseau.setInterfaceVaisseau(this);
        Table tableVitesse = new Table();
        tableVitesse.add(new Label("Vitesse : ",skin)).fillX().left().expandX();
        tableVitesse.add(this.labelVitesse).fillX().center().expandX();
        Table tableDestination = new Table();
        tableDestination.add(new Label("Destination : ",skin)).fillX().left().expandX();
        tableDestination.add(this.labelDestination).expandX().right().expandX();
        this.table.add(tableDestination).row();
        this.table.add(tableVitesse).row();
        this.createInventory();
        this.indice=0;
    }
    public void setVitesse(int vitesseinstantane){
      this.labelVitesse.setText(Integer.toString(vitesseinstantane));
    }
    public void setDestination(Implantation destination){
        this.labelDestination.setText(destination.toString());
    }
    private void createInventory() {
        this.table.row();
        this.table.add(new Label("Inventaire :", skin));
        table.row();
        listInventory = new List(skin);
        this.ressourceInventory = new Table();
        Array<Actor> labelRessource = new Array<Actor>();
        for (int i = 0; i < Ressource.diverse; i++) {
            if (this.vaisseau.getInventory().getRessource(i) > 0) {
                labelRessource.add(new Label(Ressource.getName(i), skin) {
                    @Override
                    public String toString() {
                        return String.valueOf(this.getText());
                    }
                });
                ressourceInventory.add(new Label(Float.toString(this.vaisseau.getInventory().getRessource(i)), skin));
                ressourceInventory.row();
            }
        }
        listInventory.setItems(labelRessource);
        Table listeInventaire = new Table();
        listeInventaire.setWidth(table.getWidth());
        listeInventaire.add(listInventory).left().expandX().fillX();
        listeInventaire.add(ressourceInventory).right();
        this.table.add(listeInventaire).expandX().fillX();
    }
    public void refreshInventory() {
        Array<Actor> labelRessource = new Array<>();
        this.ressourceInventory.clearChildren();
        for (int i = 0; i < Ressource.diverse; i++) {
            if (this.vaisseau.getInventory().getRessource(i) > 0) {
                System.out.println(this.vaisseau.getInventory().getRessource(i));
                labelRessource.add(new Label(Ressource.getName(i), skin) {
                    @Override
                    public String toString() {
                        return String.valueOf(this.getText());
                    }
                });
                ressourceInventory.add(new Label(Float.toString(this.vaisseau.getInventory().getRessource(i)), skin));
                ressourceInventory.row();
            }
        }
        listInventory.setItems(labelRessource);
    }
}
