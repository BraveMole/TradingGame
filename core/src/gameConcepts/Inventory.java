package gameConcepts;


public class Inventory {
	private float maxsize;
	private float sizeavailable;
	private float[] cargo;
	
	public Inventory() {
		this(10000000);
	}
	
	public Inventory(float maxsize) {
		this(maxsize, new float[Ressource.diverse]);
	}
	
	private Inventory(float maxsize, float[] cargo) {
		this.maxsize=maxsize;
		this.cargo=cargo;	
		this.checkSizeAvailable();
	}
	
	private void checkSizeAvailable() {
		sizeavailable=maxsize;
		for (int i=0;i<Ressource.diverse;i++) {
			sizeavailable-=cargo[i]*Ressource.getRoomTaken(i);
		}
		if (sizeavailable<0) {
			System.out.println("Erreur Inventory (Size available <0)");
		}
	}
	
	public float getSizeAvailable() {
		return this.sizeavailable;
	}
	
	public float getRessource(int ressourceId) {//Renvoie combien il y a de ressource
		try { 
			return cargo[ressourceId]; 			
		}
		catch(Exception e) {
			System.out.println("Erreur inventory(get ressource): out of bounds");
			return -1;
		}
	}

	public void setRessource(int ressourceId,float quantity) {
		cargo[ressourceId]=quantity;
	}
	
	public boolean addRessource(int ressourceId,float quantity) {
		try {
			if (((cargo[ressourceId]+quantity)>=0)&&(quantity*Ressource.getRoomTaken(ressourceId)<=sizeavailable)) {
				cargo[ressourceId]+=quantity;
				this.sizeavailable-=quantity*Ressource.getRoomTaken(ressourceId);
				return true;
			}
			else {
				System.out.println("Erreur inventory(add Ressource): not enough material or room issues");
				return false;
			}
		}
		catch(Exception e) {
			System.out.println("Erreur inventory(add Ressource): out of bounds");
			return false;
		}
	}
	
	public String toString() {
		StringBuilder description= new StringBuilder();
		for (int i=0;i<Ressource.diverse;i++) {
			if (cargo[i] !=0) {
				description.append(Ressource.getName(i)).append(" ").append(cargo[i]).append(" ");
			}
		}
		return description.toString();
	}

}
