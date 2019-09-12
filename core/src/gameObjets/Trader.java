package gameObjets;

import com.badlogic.gdx.utils.Array;
import commerce.Besoin;
import commerce.Offre;

public interface Trader extends HasInventory {
	void cycle();
	Offre getOffre();
	float getTresorie();
	void addTresorie(float argent);
	void setTresorie(float argent);
	Array<Besoin> getBesoin();
	float getX();
	float getY();
	double distance(Trader t);
	Implantation getImplatation();
	void addObjetVendu(float nbobjetvendu);
	String toString();

}
