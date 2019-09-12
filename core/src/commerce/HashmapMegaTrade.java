package commerce;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class HashmapMegaTrade implements Comparable<HashmapMegaTrade> {
	private HashMap<Integer,MegaTrade> listeMegaTrade;
	private MegaTrade bestMegaTrade;
	private Array<Integer> listeKey;
	private double indiceprofitabilite;

	public HashmapMegaTrade() {
		this.listeKey= new Array<>();
		this.listeMegaTrade = new HashMap<>();
		this.bestMegaTrade = new MegaTrade();
	}
	
	public void addTrade(Trade t) {
		if (this.listeMegaTrade.containsKey(t.getAcheteur().getImplatation().getId())) {
			this.listeMegaTrade.get(t.getAcheteur().getImplatation().getId()).addTrade(t);
			if (this.listeMegaTrade.get(t.getAcheteur().getImplatation().getId()).getindiceprofitabilite()>this.bestMegaTrade.getindiceprofitabilite()) {
				this.bestMegaTrade=this.listeMegaTrade.get(t.getAcheteur().getImplatation().getId());
			}
		}
		else {
			this.listeMegaTrade.put(t.getAcheteur().getImplatation().getId(), new MegaTrade(t));
			this.listeKey.add(t.getAcheteur().getImplatation().getId());
			if (this.listeMegaTrade.get(t.getAcheteur().getImplatation().getId()).getindiceprofitabilite()>this.bestMegaTrade.getindiceprofitabilite()) {
				this.bestMegaTrade=this.listeMegaTrade.get(t.getAcheteur().getImplatation().getId());
			}
		}
	}
	
	public void actualizeBestMegaTrade() {
		for (int i =0;i<this.listeKey.size;i++) {
			if (this.listeMegaTrade.get(this.listeKey.get(i)).getindiceprofitabilite()>this.bestMegaTrade.getindiceprofitabilite()) {
				this.bestMegaTrade=this.listeMegaTrade.get(this.listeKey.get(i));
			}
		}
		this.indiceprofitabilite=this.bestMegaTrade.getindiceprofitabilite();
	}
	
	public MegaTrade getBestMegaTrade() {
		actualizeBestMegaTrade();
		return this.bestMegaTrade;
	}

	@Override
	public int compareTo(HashmapMegaTrade o) {
		if (o.getIndiceprofitabilite()>this.getIndiceprofitabilite()){
			return 1;
		}
		else if (o.getIndiceprofitabilite()<this.getIndiceprofitabilite()){
			return -1;
		}
		else{
			return 0;
		}
	}

	public double getIndiceprofitabilite() {
		return indiceprofitabilite;
	}
}
