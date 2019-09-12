package gameObjets;

import com.badlogic.gdx.utils.Array;
import commerce.Besoin;
import commerce.MarketPlace;
import commerce.Offre;
import gameConcepts.Inventory;
import gameConcepts.Recette;
import gameConcepts.Ressource;
import gameUi.InterfaceIndustrie;
import world.Application;

import java.util.Random;

import static gameConcepts.Ressource.diverse;

public class Industrie implements Trader{

	private Recette recette;
	private Inventory inventory;

	private Array<Besoin> listeBesoin;
	private Offre offre;
	private int nbobjetsproduits;
	private int nbobjetvendu;
	private Implantation implatation;
	private int id;
	private InterfaceIndustrie inter;
	private Ressource ressourceProduite;
	private float tresorie;
	private double puissanceIndustrielle;
	private double difficulteProduction;
	private int nbobjetvenducycle;
	private double[] ratioVente;
	private int nbcycle;
	private double moyenneRatio;

	private static final float EnoughSaleCoef = 1;
	private static final float maxCostOfMats=0.7f;
	private static final float LittleRaise =1.1f;
	private static final float NotEnoughSaleCoef = 4f;
	private static final float LittleReduction =0.9f;
	private static final float ReserveMatierePremiere = 5f;
	private static int nbIndustrie=0;

	Industrie(Ressource ressourceProduite, double puissanceindustrielle, Implantation implantation) {
		this.tresorie=100000;
		this.nbcycle++;
		this.nbobjetvenducycle=0;
		moyenneRatio=0;
		this.ratioVente= new double[]{0,0,0,0,0};
		Random rand = new Random();
		this.ressourceProduite = ressourceProduite;
		this.id= Industrie.nbIndustrie;
		Industrie.nbIndustrie++;
		this.inventory=new Inventory();
		this.implatation=implantation;
		this.listeBesoin= new Array<>();
		this.recette= ressourceProduite.getRecette();
		this.puissanceIndustrielle=puissanceindustrielle;
		if (recette!=null){
			this.difficulteProduction=recette.getCoutmanoeuvre();
			this.setNbobjetsproduits();
			for (int i=0;i<diverse;i++) {
				if (recette.getMatpremiere()[i]!=0){
					listeBesoin.add(new Besoin(i,recette.getMatpremiere()[i]*nbobjetsproduits*Industrie.ReserveMatierePremiere, Ressource.getBasicPrice(i), this));
				}
			}
		}
		else{
			this.difficulteProduction= ressourceProduite.getFacilitExtraction();
			this.setNbobjetsproduits();
		}
		this.offre=new Offre(ressourceProduite.getId(),0,this.ressourceProduite.getBasicprice()+(0.9f+rand.nextFloat()*0.2f),this);
		MarketPlace.addTrader(this);
		this.nbobjetvendu=0;
	}

	private void setNbobjetsproduits(){
		this.nbobjetsproduits=(int)(this.puissanceIndustrielle/this.difficulteProduction);
	}

	public int getId() {
		return this.id;
	}

	private void printBesoins(){
		for (Besoin besoin : this.listeBesoin) {
			System.out.println(Ressource.ressourcePossible.get(besoin.getRessourceId()).getName()+" "+besoin.getQuantity()+" "+besoin.getPrixmax());
		}
	}

	private void updateBesoin() {
		if (recette!=null) {
			float besointot = 0;
			float prixmatiere = 0;
			for (Besoin besoin : listeBesoin) {
				besointot += besoin.getQuantity() / recette.getMatpremiere()[besoin.getRessourceId()];
				prixmatiere += besoin.getPrixmax() * recette.getMatpremiere()[besoin.getRessourceId()];
			}
			float scale = Industrie.maxCostOfMats * this.offre.getPrix() / prixmatiere;
			for (Besoin besoin : listeBesoin) {
				if (besoin.getQuantity()!=0) {
					besoin.scalePrixMax(scale * besoin.getQuantity() / recette.getMatpremiere()[besoin.getRessourceId()] / besointot * listeBesoin.size);
				}
			}
		}
	}

	public void addObjetVendu(float nbobjetvendu) {
		this.nbobjetvendu+=nbobjetvendu;
		if (nbobjetvendu>0) {
			this.nbobjetvenducycle += nbobjetvendu;
		}
	}

	@Override
	public String toString() {
		return this.ressourceProduite.getName() +"  " +this.id;
	}

	private void adjustementProduction(double scale){
		this.puissanceIndustrielle*=scale;
		this.setNbobjetsproduits();
	}

	private void updateProduction(){
		this.ratioVente[nbcycle%this.ratioVente.length] = ((double)this.nbobjetvenducycle)/this.nbobjetsproduits;
		if (nbcycle%this.ratioVente.length==0 && nbcycle>0){
			moyenneRatio=0;
			for (double i : ratioVente) {
					moyenneRatio+=i;
			}
			moyenneRatio/=ratioVente.length;
			if (moyenneRatio>1.1){
				this.adjustementProduction(moyenneRatio);
				this.offre.scalePrix(1.1f);
			}
			else if (moyenneRatio<0.9){
				this.offre.scalePrix(0.9f);
				this.adjustementProduction(moyenneRatio);
			}
			else{
				this.adjustementProduction(moyenneRatio+0.1);
				this.offre.scalePrix(1.1f);
			}
		}
		this.nbobjetvenducycle=0;
		nbcycle++;
	}

	private void updatePrice(){
		if( this.inter!=null){
			this.inter.refreshPrice(this.offre.getPrix());
		}
	}

	private void updateOffre() {
		offre.setQuantity(this.inventory.getRessource(ressourceProduite.getId())-this.nbobjetvendu);
	}

	public Offre getOffre() {
		return this.offre;
	}

	@Override
	public float getTresorie() {
		return this.tresorie;
	}

	@Override
	public void addTresorie(float argent) {
		this.tresorie+=argent;
	}

	@Override
	public void setTresorie(float argent) {
		this.tresorie=argent;
	}

	public Array<Besoin> getBesoin(){
		return this.listeBesoin;
	}

	public void cycle() {
		this.updatePrice();
		this.updateProduction();
		int maxproduction = this.nbobjetsproduits;
		for (int i=0;i<this.listeBesoin.size;i++) {
			if ((this.inventory.getRessource(this.listeBesoin.get(i).getRessourceId())/recette.getMatpremiere()[this.listeBesoin.get(i).getRessourceId()])<maxproduction) {
				maxproduction=(int)(this.inventory.getRessource(this.listeBesoin.get(i).getRessourceId())/recette.getMatpremiere()[this.listeBesoin.get(i).getRessourceId()]);
			}
		}
		for (int i=0;i<this.listeBesoin.size;i++) {
			this.addRessource(this.listeBesoin.get(i).getRessourceId(), -recette.getMatpremiere()[this.listeBesoin.get(i).getRessourceId()]*maxproduction);
			this.listeBesoin.get(i).addQuantity(recette.getMatpremiere()[this.listeBesoin.get(i).getRessourceId()]*maxproduction);
		}
		this.addRessource(ressourceProduite.getId(),maxproduction);
		this.updateOffre();
		this.updateBesoin();
		if (Application.TestWorldSettings.test){
			System.out.println(this.toString());
			System.out.println("Max Production : "+maxproduction);
			System.out.println("Moyenne Ratio : "+this.moyenneRatio);
			System.out.println("Offre : "+this.offre.getQuantity()+"  "+this.offre.getPrix());
			this.printBesoins();
		}
	}


	private boolean addRessource(int ressourceId, float quantity){
		if (this.inventory.addRessource(ressourceId, quantity)) {
			if (this.inter!=null) {
				this.inter.refreshInventory();
			}
			return true;
		}
		return false;
	}

	public void setInterface(InterfaceIndustrie inter){
		this.inter=inter;
	}

	@Override
	public boolean receivingCargo(int ressourceId, float quantity) {
		if (quantity<0) {
			if(this.addRessource(ressourceId, quantity)) {
				this.addObjetVendu(quantity);
				return true;
			}
			else {
				return false;
			}
		}
		return this.addRessource(ressourceId, quantity);
	}

	@Override
	public boolean startingCargoExchange(int ressourceId, float quantity,HasInventory t) {
		if (t.receivingCargo(ressourceId, quantity)) { //La partie adverse peut faire la transfaction
			if(!this.receivingCargo(ressourceId, -quantity)) { //Si celui qui initie la transaction ne peut pas la faire, on l'annule
				t.receivingCargo(ressourceId, -quantity);
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public float getX() {
		return this.implatation.getX();
	}

	@Override
	public float getY() {
		return this.implatation.getY();
	}

	@Override
	public double distance(Trader t) {
		return Math.sqrt(Math.pow((this.getX()-t.getX()),2)+ Math.pow((this.getY()-t.getY()),2));
	}

	@Override
	public Implantation getImplatation() {
		return this.implatation;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}
}
