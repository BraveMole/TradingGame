package gameObjets;

import gameConcepts.Inventory;

public interface HasInventory {
	boolean receivingCargo(int ressourceId, float quantity);
	boolean startingCargoExchange(int ressourceId, float quantity, HasInventory t);
	Inventory getInventory();
}
