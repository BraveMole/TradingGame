package commerce;

import com.badlogic.gdx.utils.Array;
import gameConcepts.Ressource;
import gameObjets.Trader;

import java.util.PriorityQueue;

public class MegaTrade implements Comparable<MegaTrade> {
	private PriorityQueue<Trade> ListeTrade;
	final private Trader acheteur;
	final private double distance;
	private double indiceprofitabilite;
	
	public MegaTrade() {
		this.acheteur=null;
		this.distance=1;
		this.indiceprofitabilite=-1;
		this.ListeTrade=new PriorityQueue<Trade>();
	}
	
	public MegaTrade(Trade t) {
		this.ListeTrade=new PriorityQueue<Trade>();
		this.acheteur=t.getAcheteur();
		this.distance=this.acheteur.distance(t.getVendeur());
		this.addTrade(t);
	}
	
	public void addTrade(Trade t) {
		this.indiceprofitabilite+=(t.getPrice()*t.getQuantity()*10000)/(Ressource.getRoomTaken(t.getRessourceId())*this.distance);
		this.ListeTrade.add(t);
	}
	
	public Array<Trade> getBestTrade(float sizeavailable){
		Array<Trade> listeBestTrade = new Array<Trade>();
		Trade bestTrade;
 		while (sizeavailable>0 && !this.ListeTrade.isEmpty()) {
			bestTrade = this.ListeTrade.remove();
			this.indiceprofitabilite-=(bestTrade.getPrice()*bestTrade.getQuantity()*10000)/(Ressource.getRoomTaken(bestTrade.getRessourceId())*this.distance);
			if (sizeavailable>=bestTrade.getSizetaken()) {
				sizeavailable-=bestTrade.getSizetaken();
				listeBestTrade.add(bestTrade);
			}
			else {
				listeBestTrade.add(new Trade(bestTrade.getRessourceId(), bestTrade.getAcheteur(), bestTrade.getVendeur(), (int)(sizeavailable/ Ressource.getRoomTaken(bestTrade.getRessourceId())), bestTrade.getPrice()));
				this.addTrade(new Trade(bestTrade.getRessourceId(), bestTrade.getAcheteur(), bestTrade.getVendeur(), bestTrade.getQuantity()-(int)(sizeavailable/ Ressource.getRoomTaken(bestTrade.getRessourceId())), bestTrade.getPrice()));
				sizeavailable=0;
			}
		}
		return listeBestTrade;
	}
	
	public double getindiceprofitabilite() {
		return this.indiceprofitabilite;
	}
	@Override
	public int compareTo(MegaTrade o) {
		if (this.indiceprofitabilite>o.getindiceprofitabilite()) {
			return 1;
		}
		else if (this.indiceprofitabilite<o.getindiceprofitabilite()) {
			return -1;
		}
		return 0;
	}

}
