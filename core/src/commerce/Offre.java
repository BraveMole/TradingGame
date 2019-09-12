package commerce;


import com.badlogic.gdx.utils.Array;
import gameConcepts.MapIndex;
import gameConcepts.Ressource;
import gameObjets.Trader;

import static commerce.MarketPlace.getXindex;
import static commerce.MarketPlace.getYindex;

public class Offre implements Comparable<Offre> {
	private final int ressourceId;
	private float quantity;
	private float prix;
	private final Trader t;
	private Array<Float> souvenirVentes;
	public MapIndex indexpos;
	public Offre (int ressourceId, float quantity, float prix, Trader t) {
		this.ressourceId=ressourceId;
		this.quantity=quantity;
		this.prix=prix;
		this.t=t;
		this.indexpos = new MapIndex(getXindex(this.t.getX()),getYindex(this.t.getY()));
		this.souvenirVentes=new Array<>();
	}
	int getRessourceId() {
		return this.ressourceId;
	}
	public float getQuantity() {
		return this.quantity;
	}
	public float getPrix() {
		return this.prix;
	}
	public Trader getTrader() {
		return this.t;
	}

	public void setPrix(int prix) {
		this.prix = prix;
	}
	public void scalePrix(float scale){
		this.prix*=scale;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
		this.souvenirVentes.add(quantity);
	}

	public String toString() {
		String label="";
		label+=Ressource.getName(this.ressourceId);
		label+=" q= "+this.quantity;
		label+=" p= "+this.prix;
		return label;
	}

	@Override
	public int compareTo(Offre o) {
		if (o.getPrix()>this.prix){
			return 1;
		}
		else if(o.getPrix()<this.prix){
			return -1;
		}
		return 0;
	}
}
