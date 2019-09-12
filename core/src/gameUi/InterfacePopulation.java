package gameUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import gameConcepts.Population;
import gameConcepts.Ressource;

public class InterfacePopulation extends Interface{
    private Population pop;
    private Label nbTravailleur;
    private Label hapinesslevel;
    private Label nomPop;
    private List listInventory;
    private Table ressourceInventory;

    public InterfacePopulation(Skin skin, float width, float height,Population pop) {
        super(skin, width*0.2f, height*0.4f, width*0.4f, height*0.2f, "InterfacePopulation");
        this.pop=pop;
        this.nbTravailleur = new Label("Nombre de travailleur : "+pop.getNbTravailleur(),skin);
        this.hapinesslevel = new Label("Niveau de bonheur : "+pop.getNiveaudebonheur(),skin);
        this.nomPop = new Label(pop.toString(),skin);
        nomPop.setAlignment(Align.center);
        this.table.top();
        this.table.add(nomPop).center().row();
        this.table.add(nbTravailleur).center().row();
        this.table.add(hapinesslevel).center().row();
        this.createInventory();
        this.pop.setInterfacePopulation(this);
    }
    private void createInventory() {
        this.table.row();
        this.table.add(new Label("Inventaire :", skin));
        table.row();
        listInventory = new List(skin);
        this.ressourceInventory = new Table();
        Array<Actor> labelRessource = new Array<Actor>();
        for (int i = 0; i < Ressource.diverse; i++) {
            if (this.pop.getInventory().getRessource(i) > 0) {
                labelRessource.add(new Label(Ressource.getName(i), skin) {
                    @Override
                    public String toString() {
                        return String.valueOf(this.getText());
                    }
                });
                ressourceInventory.add(new Label(Float.toString(this.pop.getInventory().getRessource(i)), skin));
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
            if (this.pop.getInventory().getRessource(i) > 0) {
                System.out.println(this.pop.getInventory().getRessource(i));
                labelRessource.add(new Label(Ressource.getName(i), skin) {
                    @Override
                    public String toString() {
                        return String.valueOf(this.getText());
                    }
                });
                ressourceInventory.add(new Label(Float.toString(this.pop.getInventory().getRessource(i)), skin));
                ressourceInventory.row();
            }
        }
        listInventory.setItems(labelRessource);
    }
}
