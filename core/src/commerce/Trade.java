package commerce;

import gameConcepts.Ressource;
import gameObjets.Trader;

public class Trade implements Comparable<Trade> {
	final private int ressourceId;
	final private Trader acheteur;
	final private Trader vendeur;
	final private float quantity;
	final private float sizetaken;
	final private float price;
	final private double indiceprofitabilite;
	Trade(int ressourceId, Trader acheteur, Trader vendeur, float quantity, float price) {
		this.ressourceId=ressourceId;this.acheteur=acheteur;this.vendeur=vendeur;this.quantity=quantity;this.price=price;
		this.sizetaken=this.quantity* Ressource.getRoomTaken(this.ressourceId);
		this.moneyExchange();
		this.indiceprofitabilite = this.price/(this.sizetaken);
	}
	private void moneyExchange(){
		this.acheteur.addTresorie(-quantity*price);
		this.vendeur.addTresorie(-quantity*price);
	}
	public int getRessourceId() {
		return ressourceId;
	}
	public Trader getAcheteur() {
		return acheteur;
	}
	public Trader getVendeur() {
		return vendeur;
	}
	public float getQuantity() {
		return quantity;
	}
	float getPrice() {
		return price;
	}
	private double getIndiceprofitabilite() {
		return indiceprofitabilite;
	}
	@Override
	public int compareTo(Trade o) {
		if (this.indiceprofitabilite>o.getIndiceprofitabilite()) {
			return 1;
		}
		else if(this.indiceprofitabilite<o.getIndiceprofitabilite()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString(){
		return "Trade : Vendeur : " + this.getVendeur().toString()+" Acheteur : "+this.getAcheteur().toString()+" Quantity : "+this.getQuantity()+" Prix : "+this.getPrice();
	}
	float getSizetaken() {
		return sizetaken;
	}
}
