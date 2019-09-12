package gameConcepts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import commerce.Besoin;
import commerce.MarketPlace;
import commerce.Offre;
import gameObjets.HasInventory;
import gameObjets.Implantation;
import gameObjets.Trader;
import gameUi.InterfacePopulation;
import world.Application;

public class Population implements Trader{
	private static float [] produitConsomme;
	private static int [] prioriteAchat;
	private static int nbBesoin=0;
	private int nbTravailleur;
	private Inventory inventory;
	private float tresorie;
	private Array<Besoin> listeBesoin;
	private InterfacePopulation interfacePopulation;

	public int getNiveaudebonheur() {
		return niveaudebonheur;
	}

	private Implantation implantation;
	private static int fullstock=10;
	private int niveaudebonheur;

	public static void initPopBesoin(){
		if (Application.TestWorldSettings.test){
			produitConsomme= Application.TestWorldSettings.produitConsommePop();
			prioriteAchat= Application.TestWorldSettings.prioriteAchatPop();
			for (float v : produitConsomme) {
				if (v>0) {
					nbBesoin++;
				}
			}
		}
		else {
			produitConsomme = new float[Ressource.ressourcePossible.size];
			prioriteAchat = new int[Ressource.ressourcePossible.size];
			String[] wholeFile = Gdx.files.internal("BesoinPop.txt").readString().split(";");
			int i = 0;
			String[] s2;
			for (String s : wholeFile) {
				if (!s.isEmpty()) {
					s2 = s.split("-");
					produitConsomme[i] = Float.parseFloat(s2[0]) / 100f;
					prioriteAchat[i] = Integer.parseInt(s2[1].strip());
					nbBesoin++;
				}
				i++;
			}
		}
	}

	private void besoinInit(){
		this.listeBesoin.setSize(nbBesoin);
		for (int i = 0; i < produitConsomme.length; i++) {
			if (produitConsomme[i]!=0) {
				this.listeBesoin.set(prioriteAchat[i], new Besoin(i, produitConsomme[i]*nbTravailleur*fullstock, Ressource.getBasicPrice(i)*2, this));
			}
		}
	}

	public Population (int nbTravailleur, Implantation implantation) {
		this.tresorie=100000;
		this.niveaudebonheur=0;
		this.nbTravailleur=nbTravailleur;
		this.inventory= new Inventory();
		this.implantation=implantation;
		this.listeBesoin= new Array<>();
		this.besoinInit();
		MarketPlace.addTrader(this);
	}

	@Override
	public boolean receivingCargo(int ressourceId, float quantity) {
		if (quantity<0) {
			if(this.inventory.addRessource(ressourceId, quantity)) {
				this.addObjetVendu(quantity);
				if (this.interfacePopulation!=null) {
					this.interfacePopulation.refreshInventory();
				}
				return true;
			}
			else {
				return false;
			}
		}
		if(this.inventory.addRessource(ressourceId, quantity)){
			if (this.interfacePopulation!=null) {
				this.interfacePopulation.refreshInventory();
			}
			return true;
		}
		return false;
	}

	public void setInterfacePopulation(InterfacePopulation ip){
		this.interfacePopulation=ip;
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


	private void updateBesoin() {
		for (int i = 0; i <= niveaudebonheur; i++) {
			int stocktresbas = 6;
			int stockbas = 2;
			if (this.listeBesoin.get(i).getQuantity()==0){
				this.listeBesoin.get(i).scalePrixMax(0.9f);
			}
			else if (this.listeBesoin.get(i).getQuantity()>produitConsomme[listeBesoin.get(i).getRessourceId()]*nbTravailleur* stocktresbas){
				this.listeBesoin.get(i).scalePrixMax(1.25f);
			}
			else if (this.listeBesoin.get(i).getQuantity()>produitConsomme[listeBesoin.get(i).getRessourceId()]*nbTravailleur* stockbas){
				this.listeBesoin.get(i).scalePrixMax(1.1f);
			}
		}
		if (niveaudebonheur<listeBesoin.size-1) {
			for (int i = niveaudebonheur + 1; i < listeBesoin.size; i++) {
				listeBesoin.get(i).setQuantity(0);
			}
		}
	}

	private void printBesoins(){
		for (int i = 0; i <= niveaudebonheur; i++) {
			System.out.println(Ressource.ressourcePossible.get(this.listeBesoin.get(i).getRessourceId()).getName()+" "+this.listeBesoin.get(i).getQuantity()+" "+this.listeBesoin.get(i).getPrixmax());
		}
	}

	public int getNbTravailleur(){
		return this.nbTravailleur;
	}

	@Override
	public void cycle() {
		this.updateBesoin();
		if (Application.TestWorldSettings.test){
			System.out.println(this);
			this.printBesoins();
		}
		this.consommation();
		if (this.interfacePopulation!=null){
			this.interfacePopulation.refreshInventory();
		}
	}

	private void consommation(){
		int id;
		for (int i = 0; i <= niveaudebonheur; i++) {
			id =this.listeBesoin.get(i).getRessourceId();
			if (this.inventory.getRessource(id)>=nbTravailleur*produitConsomme[id]){
				this.inventory.addRessource(id,-nbTravailleur*produitConsomme[id]);
				this.listeBesoin.get(i).addQuantity(nbTravailleur*produitConsomme[id]);
			}
			else{
				this.inventory.setRessource(id,0);
				this.listeBesoin.get(i).setQuantity(produitConsomme[id]*nbTravailleur*fullstock);
				niveaudebonheur=i;
				if (Application.TestWorldSettings.test){
					System.out.println("Manque. Niveau de bonheur : "+niveaudebonheur);
				}
				return;
			}
		}
		niveaudebonheur++;
		if (niveaudebonheur>=listeBesoin.size){
			niveaudebonheur=listeBesoin.size-1;
		}
		System.out.println("Besoin fullfilled : Niveau de bonheur : "+niveaudebonheur);
	}

	@Override
	public Offre getOffre() {
		return null;
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

	@Override
	public Array<Besoin> getBesoin() {
		return this.listeBesoin;
	}
	@Override
	public float getX() {
		return this.implantation.getX();
	}

	@Override
	public float getY() {
		return this.implantation.getY();
	}
	@Override
	public double distance(Trader t) {
		return Math.sqrt(Math.pow((this.getX()-t.getX()),2)+ Math.pow((this.getY()-t.getY()),2));
	}
	@Override
	public Implantation getImplatation() {
		return this.implantation;
	}
	@Override
	public void addObjetVendu(float nbobjetvendu) {

	}

	@Override
	public String toString() {
		return "Population de : "+this.implantation;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}


}
