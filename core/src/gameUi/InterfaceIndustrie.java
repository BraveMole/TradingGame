package gameUi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import gameConcepts.Ressource;
import gameObjets.Industrie;

public class InterfaceIndustrie extends Interface {

    private Industrie industrie;
    private Table ressourceInventory;
    private Label labelprixvente;
    private Label tresorie;
    private List listInventory;

    public InterfaceIndustrie(Industrie i, Skin skin, float width, float height) {
        super(skin, width * 0.2f, height * 0.5f, width / 4f, height / 40f, "InterfaceIndustrie");
        this.industrie = i;
        this.industrie.setInterface(this);
        Label nameLabel = new Label(this.industrie.toString(), this.skin, "title");
        nameLabel.setAlignment(Align.center);
        table.add(nameLabel).pad(table.getHeight() / 40f).padBottom(table.getHeight() / 30f).center().expandX().fillX();
        this.table.top();
        tresorie = new Label("Tresorie : "+this.industrie.getTresorie(),skin);
        this.table.add(tresorie).row();
        this.createPrixVente();
        this.createInventory();
    }

    private void createPrixVente(){
        Table prixvente = new Table();
        labelprixvente = new Label(Float.toString(industrie.getOffre().getPrix()),skin);
        prixvente.add(new Label("Prix vente :  ", skin)).left();
        prixvente.add(labelprixvente).fillX().right();
        this.table.row();
        this.table.add(prixvente).padBottom(10f);
    }

    private void createInventory() {
        this.table.row();
        this.table.add(new Label("Inventaire :", skin));
        table.row();
        listInventory = new List(skin);
        this.ressourceInventory = new Table();
        Array<Actor> labelRessource = new Array<Actor>();
        for (int i = 0; i < Ressource.diverse; i++) {
            if (this.industrie.getInventory().getRessource(i) > 0) {
                labelRessource.add(new Label(Ressource.getName(i), skin) {
                    @Override
                    public String toString() {
                        return String.valueOf(this.getText());
                    }
                });
                ressourceInventory.add(new Label(Float.toString(this.industrie.getInventory().getRessource(i)), skin));
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


    public Industrie getIndustrie() {
        return this.industrie;
    }

    @Override
    public void clear() {

    }

    public void refreshPrice(float price){
        this.labelprixvente.setText(Float.toString(price));
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof InterfaceIndustrie) {
            InterfaceIndustrie f = (InterfaceIndustrie) o;
            return (f.getIndustrie().getId() == this.getIndustrie().getId());
        }
        return false;

    }

    public void refreshInventory() {
        Array<Actor> labelRessource = new Array<>();
        this.ressourceInventory.clearChildren();
        for (int i = 0; i < Ressource.diverse; i++) {
            if (this.industrie.getInventory().getRessource(i) > 0) {
                labelRessource.add(new Label(Ressource.getName(i), skin) {
                    @Override
                    public String toString() {
                        return String.valueOf(this.getText());
                    }
                });
                ressourceInventory.add(new Label(Float.toString(this.industrie.getInventory().getRessource(i)), skin));
                ressourceInventory.row();
            }
        }
        listInventory.setItems(labelRessource);

    }
}
