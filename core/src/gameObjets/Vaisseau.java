package gameObjets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import commerce.HashmapMegaTrade;
import commerce.MarketPlace;
import commerce.Trade;
import gameConcepts.Inventory;
import gameConcepts.SuperActor;
import gameUi.InterfaceVaisseau;
import world.Application;
import world.World;

import static gameUi.WorldInputProcessor.palierZoom;
import static gameUi.WorldInputProcessor.worldZoom;

public class Vaisseau extends SuperActor implements HasInventory {
	private InterfaceVaisseau interfaceVaisseau;
	private float accelmax;
	private float scaleSpriteClose = 1f;
	private Inventory cargo;
	private Sprite sprite;
	private Texture close;
	private Texture far;
	private Array<Trade> listeTrade;
	private float tempsVoyageRestant;
	private float tempsVoyageTotal;
	private Vector2 destination;
	private Vector2 depart;
	private int id;


	private Vaisseau(float accelmax, Texture close, Texture far, Inventory cargo, float x, float y) {
		this.accelmax=accelmax;
		this.sprite=new Sprite(close);
		this.close=close;
		this.far=far;
		this.sprite.setScale(scaleSpriteClose);
		this.sprite.setBounds(x, y, this.sprite.getWidth()*scaleSpriteClose, this.sprite.getHeight()*scaleSpriteClose);
		this.cargo=cargo;
		this.depart = new Vector2(x,y);
		this.position = new Vector2(x,y);
		this.destination = new Vector2(x,y);
		this.listeTrade= new Array<>();

		this.id= World.nbVaisseauCree;
		World.nbVaisseauCree++;
		MarketPlace.addVaisseauDisponible(this);
	}

	public Vaisseau (float speed, Texture close, Texture far,int maxsize,float x, float y) {
		this(speed,close,far,new Inventory(maxsize),x,y);
	}

	private void setDestination(float x, float y) {
		this.tempsVoyageTotal =(float) Math.sqrt(4* Math.sqrt(Math.pow(this.position.x-x,2)+ Math.pow(this.position.y-y,2))/this.accelmax);
		this.tempsVoyageRestant=tempsVoyageTotal;
		this.depart.set(position);
		this.destination.set(x,y);
	}

	public void setInterfaceVaisseau(InterfaceVaisseau interfaceVaisseau){
		this.interfaceVaisseau=interfaceVaisseau;
		if (!this.listeTrade.isEmpty()){
			if (this.listeTrade.get(0).getAcheteur().getImplatation().getPosition().epsilonEquals(this.destination)){
				this.interfaceVaisseau.setDestination(this.listeTrade.get(0).getAcheteur().getImplatation());
			}
			else{
				this.interfaceVaisseau.setDestination(this.listeTrade.get(0).getVendeur().getImplatation());
			}
		}
	}

	private void setDestination(Vector2 destination) {
		this.setDestination(destination.x, destination.y);
	}

	@Override
	public boolean receivingCargo(int ressourceId, float quantity) {
		return cargo.addRessource(ressourceId, quantity);
	}

	@Override
	public boolean startingCargoExchange(int ressourceId, float quantity, HasInventory t) {
		if (t.receivingCargo(ressourceId, quantity)) { //La partie adverse peut faire la transfaction
			if(!this.receivingCargo(ressourceId, -quantity)) { //Si celui qui initie la transaction ne peut pas la faire, on l'annule
				t.receivingCargo(ressourceId, -quantity);
				return false;
			}
			return true;
		}
		return false;
	}

	public void receiveTrade(HashmapMegaTrade i) {
		this.listeTrade=i.getBestMegaTrade().getBestTrade(this.getInventory().getSizeAvailable());
		if (this.listeTrade.notEmpty()) {
            MarketPlace.removeVaisseauDisponible(this);
            if (this.interfaceVaisseau != null) {
                this.interfaceVaisseau.setDestination(this.listeTrade.get(0).getVendeur().getImplatation());
            }
            if (Application.TestWorldSettings.test){
            	System.out.println(this +" Liste trade");
				for (Trade trade : listeTrade) {
					System.out.println(trade);
				}
			}
            this.setDestination(this.listeTrade.get(0).getVendeur().getX(), this.listeTrade.get(0).getVendeur().getY());
        }
		else{
		    MarketPlace.addVaisseauDisponible(this);
        }
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean TradeToCargo(Trade t, boolean charger) {
		if (charger) {
			return this.startingCargoExchange(t.getRessourceId(), -t.getQuantity(), t.getVendeur());
		}
		else {
			return this.startingCargoExchange(t.getRessourceId(), t.getQuantity(), t.getAcheteur());
		}
	}

	private void AllTradesToCargo(boolean charger) {
		int size = this.listeTrade.size;
		for (int i = 0;i<size;i++) {
			if (charger) {
				if(!this.TradeToCargo(this.listeTrade.get(i),true)) {
					System.out.println("Erreur AllTradesToCargo");
				}
			}
			else {
				if(!this.TradeToCargo(this.listeTrade.pop(),false)) {
					System.out.println("Erreur AllTradesToCargo");
				}
			}
		}
		if (Application.TestWorldSettings.test){
			System.out.println(this +"  TailleCargo : "+this.cargo.getSizeAvailable());
		}
	}
	private void textureSwitch() {
		texturezoomlevel=zoomlevel;
		switch (zoomlevel){
			case(0):
				sprite.setRegion(close);
				break;
			case(1):
			case(2):
			case(3):
				sprite.setRegion(far);
		}

	}

	private void isArrived() {
		if (this.listeTrade.notEmpty()) {
			if (this.listeTrade.get(0).getVendeur().getImplatation().getPosition().epsilonEquals(destination)) {
				this.AllTradesToCargo(true);
				if (this.interfaceVaisseau!=null){
					this.interfaceVaisseau.refreshInventory();
				}
				if (this.interfaceVaisseau!=null){
					this.interfaceVaisseau.setDestination(this.listeTrade.get(0).getAcheteur().getImplatation());
				}
				this.setDestination(this.listeTrade.get(0).getAcheteur().getImplatation().getPosition());
			}
			else if (this.listeTrade.get(0).getAcheteur().getImplatation().getPosition().epsilonEquals(destination)){
				this.AllTradesToCargo(false);
				if (this.interfaceVaisseau!=null){
					this.interfaceVaisseau.refreshInventory();
				}
				MarketPlace.addVaisseauDisponible(this);
			}
		}
	}

	@Override
	public void act(float delta) {
		this.tempsVoyageRestant-=delta;
		float progress = this.tempsVoyageRestant < 0 ? 1 : 1f - tempsVoyageRestant /this.tempsVoyageTotal;
		this.position.set(Interpolation.pow2.apply(this.depart.x,this.destination.x,progress),Interpolation.pow2.apply(this.depart.y,this.destination.y,progress));

		if (this.interfaceVaisseau!=null) {
				if (progress < 0.5) {
					this.interfaceVaisseau.setVitesse((int)Interpolation.linear.apply(0, accelmax * tempsVoyageTotal / 2, progress * 2)/1000);
				} else {
					this.interfaceVaisseau.setVitesse((int)Interpolation.linear.apply(accelmax * tempsVoyageTotal / 2, 0, progress * 2 - 1)/1000);
				}
			}
		if (progress==1){
			this.isArrived();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (zoomlevel!=texturezoomlevel){
			this.textureSwitch();
		}
		if (this.zoomlevel==0) {
			sprite.setBounds(this.position.x-sprite.getRegionWidth()*scaleSpriteClose/2,this.position.y-sprite.getRegionHeight()*scaleSpriteClose/2,sprite.getRegionWidth()*scaleSpriteClose,scaleSpriteClose*sprite.getRegionHeight());
			this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
			sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
		}
		/*else if(worldZoom>palierZoom[0]*200){
			float scale = palierZoom[0]*200 + (float) Math.sqrt(worldZoom-palierZoom[1]*200);
			sprite.setBounds(this.position.x-sprite.getTexture().getWidth()*scale/2*scaling,this.position.y-sprite.getTexture().getHeight()*scale/2*scaling,sprite.getTexture().getWidth()*scale*scaling,sprite.getTexture().getHeight()*scale*scaling);
			this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
		}*/
		else if (worldZoom>palierZoom[0]){
			float scaling = 0.012f;
			sprite.setBounds(this.position.x-sprite.getTexture().getWidth()*worldZoom/2* scaling,this.position.y-sprite.getTexture().getHeight()*worldZoom/2* scaling,sprite.getTexture().getWidth()*worldZoom* scaling,sprite.getTexture().getHeight()*worldZoom* scaling);
			this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
		}
		this.sprite.draw(batch);
	}


	@Override
	public float getX() {
		return this.position.x;
	}

	@Override
	public float getY() {
		return this.position.y;
	}
	public int getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vaisseau)) {
			return false;
		}
		else {
			return ((Vaisseau) o).getId() == this.id;
		}
	}

	public String toString(){
		return "Vaisseau "+this.id;
	}

	@Override
	public Inventory getInventory() {
		return this.cargo;
	}
}
