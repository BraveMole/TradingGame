package commerce;

import com.badlogic.gdx.utils.Array;
import gameConcepts.MapIndex;
import gameConcepts.Ressource;
import gameObjets.Trader;

import static commerce.MarketPlace.getXindex;
import static commerce.MarketPlace.getYindex;

public class Besoin implements Comparable<Besoin> {
	public final MapIndex indexpos;
	private int ressourceId;
	private float quantity;
	private float prixmax;
	private Array<Float> souvenirBesoin;
	private Trader t;
	public Besoin (int ressourceId, float quantity, int prixmax, Trader t) {
		this.ressourceId=ressourceId;
		this.quantity=quantity;
		this.prixmax=prixmax;
		this.souvenirBesoin = new Array<>();
		this.t=t;
		this.indexpos = new MapIndex(getXindex(this.t.getX()),getYindex(this.t.getY()));
	}
	public int getRessourceId() {
		return this.ressourceId;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
		this.souvenirBesoin.add(quantity);
	}

	public void addQuantity(float quantity){
	    this.quantity+=quantity;
    }

	public void scalePrixMax(float scalefactor){
		this.prixmax*=scalefactor;
	}
	public float getQuantity() {
		return this.quantity;
	}
	public float getPrixmax() {
		return this.prixmax;
	}
	public Trader getTrader() {
		return this.t;
	}
	public String toString() {
		String label="";
		label+=Ressource.getName(this.ressourceId);
		label+=" q= "+this.quantity;
		label+=" p= "+this.prixmax;
		return label;
	}

    @Override
    public int compareTo(Besoin o) {
        if (o.getPrixmax()-this.prixmax>0){
        	return 1;
		}
        else if (o.getPrixmax()-this.prixmax<0){
        	return -1;
		}
        return 0;
    }
}
