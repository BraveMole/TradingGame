package commerce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import gameConcepts.MapIndex;
import gameObjets.Implantation;
import gameObjets.Trader;
import gameObjets.Vaisseau;
import world.Application;

import java.util.HashMap;
import java.util.Random;

import static gameConcepts.Ressource.diverse;

public class MarketPlace {
	static private float timeElapsed=21f;

	static private float[] gridindex;
	static private Array<MapIndex> listeIndexProspected;
	static private HashMap<MapIndex,Array<Array<Offre>>> listeOffres;
	static private HashMap<MapIndex,Array<Array<Besoin>>> listeBesoins;
	static private HashMap<MapIndex,Array<Trader>> listeTrader;
	static private HashMap<MapIndex, Array<HashmapMegaTrade>> listeClientVaisseau;
	static private HashMap<MapIndex,Array<Vaisseau>> listeVaisseauDisponible;
	static private Array<MapIndex> listeIndexPossible;
	static private MapIndex currentIndex;
	static private int numerocycle=0;
	private static int nbTrade;
    private static int bestbesoinindice;
    private static int bestoffreindice;
	private static float timeElapsed2;

	static public void initialisation() {
		nbTrade=0;
		MapIndex index;
		currentIndex = new MapIndex(0,0);
		listeTrader=new HashMap<>();
		listeOffres=new HashMap<>();
		listeIndexProspected = new Array<>();
		listeClientVaisseau =new HashMap<>();
		listeBesoins = new HashMap<>();
		listeVaisseauDisponible =new HashMap<>();
		listeIndexPossible =new Array<>();
		for (int i = 0; i < gridindex.length; i++) {
			for (int i1 = 0; i1 < gridindex.length; i1++) {
				index = new MapIndex(i,i1);
				listeIndexPossible.add(index);
				listeTrader.put(index,new Array<Trader>());
				listeVaisseauDisponible.put(index,new Array<Vaisseau>());
				listeClientVaisseau.put(index,new Array<HashmapMegaTrade>());
				listeOffres.put(index,new Array<Array<Offre>>());
				listeBesoins.put(index,new Array<Array<Besoin>>());
				for (int i2 = 0; i2 < diverse; i2++) {
					listeOffres.get(index).add(new Array<Offre>());
					listeBesoins.get(index).add(new Array<Besoin>());
				}
			}
		}
	}
	static public void act(){
		timeElapsed+= Gdx.graphics.getDeltaTime();
		timeElapsed2+=Gdx.graphics.getDeltaTime();

		float cycleTime = 5f;
		if (timeElapsed> cycleTime) {
			if (Application.TestWorldSettings.test){
				System.out.println("Cycle numero : "+numerocycle);
				numerocycle++;
			}
            for (int i = 0; i < listeIndexPossible.size; i++) {
                for (int i1 = 0; i1 < listeTrader.get(listeIndexPossible.get(i)).size; i1++) {
                    listeTrader.get(listeIndexPossible.get(i)).get(i1).cycle();
                }
            }
            timeElapsed=0f;
            nbTrade=0;
            runTrade();
            System.out.println("nb trade = "+nbTrade);
		}
		float cycleRechercheVaisseau = 1f;
		if (timeElapsed2> cycleRechercheVaisseau) {
			runTransportationAssignment();
			timeElapsed2=0;
		}

	}
	static int getXindex(float x){
		for (int i = 0; i < gridindex.length; i++) {
			if (x<gridindex[i]){
				return i;
			}
		}
		return -1;
	}

	static int getYindex(float y){
		for (int i = 0; i < gridindex.length; i++) {
			if (y<gridindex[i]){
				return i;
			}
		}
		return -1;
	}
	static public void addImplantation(Implantation i) {
		currentIndex.setXY(getXindex(i.getX()),getYindex(i.getY()));
        listeClientVaisseau.get(currentIndex).add(i.getListeMegaTrade());
	}
	static public void addTrader(Trader t) { //Est appele par le constructeur des traders
		currentIndex.setXY(getXindex(t.getX()),getYindex(t.getY()));
		if (t.getOffre()!=null){
			listeOffres.get(currentIndex).get(t.getOffre().getRessourceId()).add(t.getOffre());
		}
        for (Besoin besoin : t.getBesoin()) {
            listeBesoins.get(currentIndex).get(besoin.getRessourceId()).add(besoin);
        }
		listeTrader.get(currentIndex).add(t);
	}
	static public void addVaisseauDisponible(Vaisseau v) {
		currentIndex.setXY(getXindex(v.getX()),getYindex(v.getY()));
		listeVaisseauDisponible.get(currentIndex).add(v);
	}
	static public void removeVaisseauDisponible(Vaisseau v) {
		currentIndex.setXY(getXindex(v.getX()),getYindex(v.getY()));
		listeVaisseauDisponible.get(currentIndex).removeValue(v,false);
	}
	static private void runTrade() {
        for (MapIndex mapIndex : listeIndexPossible) {
            for (int i = 0; i < diverse; i++) {
                listeOffres.get(mapIndex).get(i).sort();
                listeBesoins.get(mapIndex).get(i).sort();
            }
        }
        for (MapIndex mapIndex : listeIndexPossible) {
            setProspectedIndex(mapIndex);
            for (int i = 0; i < diverse; i++) {
                bestbesoinindice=0;
                bestoffreindice=0;
				Array<Offre> concatenateSortedOffreList1 = new Array<>();
				Array<Offre> concatenateSortedOffreList2 = new Array<>();
				for (MapIndex index : listeIndexProspected) {
					if (!mapIndex.isDiagonalTo(index)) {
						concatenateSortedOffreList1 = MarketPlace.generateSortedOffreList(concatenateSortedOffreList1, listeOffres.get(index).get(i));
					}
					else{
						concatenateSortedOffreList2 = MarketPlace.generateSortedOffreList(concatenateSortedOffreList1, listeOffres.get(index).get(i));
					}
				}
				while(bestbesoinindice<listeBesoins.get(mapIndex).get(i).size && bestoffreindice<listeOffres.get(mapIndex).get(i).size){
					makeTrade(listeBesoins.get(mapIndex).get(i).get(bestbesoinindice),listeOffres.get(mapIndex).get(i).get(bestoffreindice));
				}
				bestoffreindice=0;
				while(bestbesoinindice<listeBesoins.get(mapIndex).get(i).size && bestoffreindice< concatenateSortedOffreList1.size){
					makeTrade(listeBesoins.get(mapIndex).get(i).get(bestbesoinindice), concatenateSortedOffreList1.get(bestoffreindice));
				}
				bestoffreindice=0;
				while(bestbesoinindice<listeBesoins.get(mapIndex).get(i).size && bestoffreindice< concatenateSortedOffreList2.size){
					makeTrade(listeBesoins.get(mapIndex).get(i).get(bestbesoinindice), concatenateSortedOffreList2.get(bestoffreindice));
				}
            }
        }
	}

	private static Array<Offre> generateSortedOffreList(Array<Offre> a, Array<Offre> b){
		Random rand = new Random();
        int indicea=0;
        int indiceb=0;
        Array<Offre> newSortedList=new Array<>();
        if (a.size==0){
        	return b;
		}
        if(b.size==0){
        	return a;
		}
        while (indicea < a.size && indiceb<b.size){
            if (a.get(indicea).getPrix()<b.get(indiceb).getPrix()) {
                newSortedList.add(a.get(indicea));
                indicea++;
            }
            else if (a.get(indicea).getPrix()==b.get(indiceb).getPrix()){
            	if (rand.nextBoolean()){
					newSortedList.add(a.get(indicea));
					indicea++;
				}
            	else{
					newSortedList.add(b.get(indiceb));
					indiceb++;
				}
			}
            else{
                newSortedList.add(b.get(indiceb));
                indiceb++;
            }
        }
		for (int i = indicea; i < a.size; i++) {
			newSortedList.add(a.get(i));
		}
		for (int i = indiceb; i < b.size; i++) {
			newSortedList.add(b.get(i));
		}
        return newSortedList;
    }

	private static void setProspectedIndex(MapIndex mapIndex){
        listeIndexProspected.clear();
        int x;
        int y;
        for (int i = -1; i < 2; i++) {
            for (int i1 = -1; i1 < 2; i1++) {
                x=mapIndex.getX()+i;
                y=mapIndex.getY()+i1;
                if (x>=0 && x<gridindex.length && y>=0 && y<gridindex.length && (i1!=0 && i!=0)) {
                    listeIndexProspected.add(listeIndexPossible.get(x*gridindex.length+y));
                }
            }
        }
    }

	public static void setGridindex(float[] gridindex) {
		MarketPlace.gridindex = gridindex;
	}
	
	static private void makeTrade(Besoin besoin, Offre offre) {
    	if (offre.getQuantity()==0){
    		bestoffreindice++;
    		return;
		}
        if (besoin.getPrixmax()<offre.getPrix() || besoin.getQuantity()==0){
            bestbesoinindice++;
        }
        else if(besoin.getQuantity()==offre.getQuantity()){
            nbTrade++;
            offre.getTrader().getImplatation().addTrade(new Trade(offre.getRessourceId(),besoin.getTrader(),offre.getTrader(),besoin.getQuantity(),offre.getPrix()));
            offre.setQuantity(0);bestoffreindice++;
            besoin.setQuantity(0);bestbesoinindice++;
        }
        else if(besoin.getQuantity()<offre.getQuantity()){
            nbTrade++;
            offre.getTrader().getImplatation().addTrade(new Trade(offre.getRessourceId(),besoin.getTrader(),offre.getTrader(),besoin.getQuantity(),offre.getPrix()));
            offre.setQuantity(offre.getQuantity()-besoin.getQuantity());
            besoin.setQuantity(0);bestbesoinindice++;
        }
        else{
            nbTrade++;
            offre.getTrader().getImplatation().addTrade(new Trade(offre.getRessourceId(),besoin.getTrader(),offre.getTrader(),offre.getQuantity(),offre.getPrix()));
            besoin.setQuantity(besoin.getQuantity()-offre.getQuantity());
            offre.setQuantity(0);bestoffreindice++;
        }
	}
	
	static private void runTransportationAssignment() {
		HashmapMegaTrade bestDeal;
		for (MapIndex mapIndex : listeIndexPossible) {
			for (HashmapMegaTrade hashmapMegaTrade : listeClientVaisseau.get(mapIndex)) {
				hashmapMegaTrade.actualizeBestMegaTrade();
			}
			if (!listeClientVaisseau.get(mapIndex).isEmpty()) {
				listeClientVaisseau.get(mapIndex).sort();
				int i = 0;
				for (Vaisseau vaisseau : listeVaisseauDisponible.get(mapIndex)) {
					bestDeal = listeClientVaisseau.get(mapIndex).get(i);
					if (bestDeal.getIndiceprofitabilite() <= 0) {
						break;
					}
					vaisseau.receiveTrade(bestDeal);
					if (i >= listeClientVaisseau.get(mapIndex).size - 1) {
						break;
					}
					i++;
					listeClientVaisseau.get(mapIndex).sort();
				}
			}
		}
	}
}
