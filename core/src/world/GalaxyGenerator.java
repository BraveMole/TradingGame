package world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import commerce.MarketPlace;
import gameObjets.Planete;
import gameObjets.Soleil;
import gameObjets.Vaisseau;

import java.util.Random;

public class GalaxyGenerator {
    private final float dispersion;
    private final int nbimplantationtile=100;
    private Random rand = new Random();
    private final float maxangle;
    private final float diametrecara;
    private float[] gridindex;
    private Array<Actor> listeObjet;
    private Vector2 generatePosition(){
        float angle = rand.nextFloat()*maxangle;
        float angledispersion = rand.nextFloat()*2f*3.14f;
        float rdispersion = rand.nextFloat()*this.dispersion;
        int sens;
        if (rand.nextBoolean()){
            sens=1;
        }
        else{
            sens=-1;
        }
        return new Vector2(diametrecara *sens*(float)(Math.sqrt(angle)* Math.cos(angle))+rdispersion*(float)(Math.cos(angledispersion)),
                (sens*diametrecara *(float)(Math.sqrt(angle)* Math.sin(angle)))+rdispersion*(float)(Math.sin(angledispersion)));
    }
    public GalaxyGenerator(float diametrecara, float nbtour,int nbetoile,Assets assets,float dispersion){
        
        this.dispersion = dispersion;
        this.listeObjet = new Array<>();
        this.diametrecara = diametrecara;
        maxangle=nbtour*2*3.14f;
        this.generateGridIndex(nbetoile,2*(dispersion+diametrecara)*((float) Math.sqrt(maxangle)),nbimplantationtile);
        MarketPlace.setGridindex(gridindex);
        MarketPlace.initialisation();
        for (int i = 0; i < nbetoile; i++) {
            this.listeObjet.add(Soleil.soleilGenerator(this.generatePosition(),assets));
            int nbPlanete = rand.nextInt(8);
            for (int i1 = 0; i1 < nbPlanete; i1++) {
                this.listeObjet.add(Planete.planeteGenerator(this.listeObjet.get(this.listeObjet.size-1-i1).getX(),this.listeObjet.get(this.listeObjet.size-1-i1).getY(),assets));
                this.listeObjet.add(new Vaisseau(10000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),1000,this.listeObjet.get(listeObjet.size-1).getX(),this.listeObjet.get(listeObjet.size-1).getY()));
                this.listeObjet.add(new Vaisseau(10000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),1000,this.listeObjet.get(listeObjet.size-1).getX(),this.listeObjet.get(listeObjet.size-1).getY()));
                this.listeObjet.add(new Vaisseau(10000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),1000,this.listeObjet.get(listeObjet.size-1).getX(),this.listeObjet.get(listeObjet.size-1).getY()));
                this.listeObjet.add(new Vaisseau(10000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),1000,this.listeObjet.get(listeObjet.size-1).getX(),this.listeObjet.get(listeObjet.size-1).getY()));
                this.listeObjet.add(new Vaisseau(10000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),1000,this.listeObjet.get(listeObjet.size-1).getX(),this.listeObjet.get(listeObjet.size-1).getY()));
                this.listeObjet.add(new Vaisseau(10000,assets.getVaisseauTexture(),assets.getVaisseauFarTexture(),1000,this.listeObjet.get(listeObjet.size-1).getX(),this.listeObjet.get(listeObjet.size-1).getY()));
            }
        }

    }

    private void generateGridIndex(int nbetoile,float diametremax,int nbimplatationtile){
        float tailleTile = (float)(Math.sqrt(nbimplatationtile*(diametremax*diametremax)/(nbetoile*16)));
        int nbtile = (int) Math.ceil(diametremax/tailleTile);
        gridindex = new float[nbtile];
        for (int i = 1; i <= nbtile; i++) {
            gridindex[i-1]=tailleTile*(i)-diametremax/2;
        }
    }

    public Array<Actor> getListeObjet(){
        return this.listeObjet;
    }
}
