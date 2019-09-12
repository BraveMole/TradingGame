package world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Assets {
    private AssetManager manager = new AssetManager();
    private Array<String> planetHabitableTexturePath;
    private Array<String> planetInhabitableTexturePath;
    private Array<String> planetName;
    private Array<String> soleilTexturePath;
    Assets(){
        loadBackgroundAssets();
        loadSoleilAssets();
        loadPlanetsAssets();
        loadVaisseauAssets();
    }
    void finishLoading(){
        manager.finishLoading();
    }

    private void loadBackgroundAssets(){
        for (int i = 0; i < 26; i++) {
            manager.load("Background/"+ i +".jpg",Texture.class);
        }
    }

    public Texture getVaisseauTexture(){
        return this.manager.get("ship.png");
    }
    public Texture getVaisseauFarTexture(){
        return this.manager.get("farVaisseau.png");
    };

    private void loadVaisseauAssets(){
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter=Texture.TextureFilter.MipMapLinearLinear;
        param.genMipMaps =true;
        manager.load("ship.png",Texture.class,param);
        manager.load("farVaisseau.png" ,Texture.class,param);
    }

    private void loadSoleilAssets(){
        soleilTexturePath = new Array<>();
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter=Texture.TextureFilter.MipMapLinearLinear;
        param.genMipMaps =true;
        String[] wholeFile = Gdx.files.internal("Sun/ListeSoleil.txt").readString().split(";");
        for (int i = 0; i < wholeFile.length; i++) {
            soleilTexturePath.add("Sun/"+wholeFile[i].strip()+".png");
            manager.load(soleilTexturePath.get(i),Texture.class,param);
        }
        manager.load("EtoileScintillante.png",Texture.class,param);
    }

    private void loadPlanetsAssets(){
        planetHabitableTexturePath = new Array<>();
        planetInhabitableTexturePath = new Array<>();
        manager.load("Planets/far.png",Texture.class);
        planetName=new Array<>();
        String[] wholeFile = Gdx.files.internal("PlanetName.txt").readString().split("\n");
        for (String s : wholeFile) {
            planetName.add(s.strip());
        }
        wholeFile = Gdx.files.internal("Planets/Habitable/ListeNomHabitable.txt").readString().strip().split(";");
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter=Texture.TextureFilter.MipMapLinearLinear;
        param.genMipMaps =true;
        for (int i = 0; i < wholeFile.length; i++) {
            planetHabitableTexturePath.add("Planets/Habitable/"+wholeFile[i].strip()+".png");
            manager.load(planetHabitableTexturePath.get(i),Texture.class,param);
        }
        wholeFile = Gdx.files.internal("Planets/Inhabitable/ListeNomInhabitable.txt").readString().strip().split(";");
        for (int i = 0; i < wholeFile.length; i++) {
            planetInhabitableTexturePath.add("Planets/Inhabitable/"+wholeFile[i].strip()+".png");
            manager.load(planetInhabitableTexturePath.get(i),Texture.class,param);
        }
    }

    public String getPlanetName(){
        Random rand = new Random();
        return this.planetName.get(rand.nextInt(this.planetName.size-1));
    }

    public int getNbInhabitableTexture(){
        return this.planetInhabitableTexturePath.size;
    }

    public int getNbHabitableTexture(){
        return this.planetHabitableTexturePath.size;
    }

    public Texture getPlaneteHabitableTexture(int id){
        return manager.get(planetHabitableTexturePath.get(id));
    }

    public Texture getPlaneteInhabitableTexture(int id){
        return manager.get(planetInhabitableTexturePath.get(id));
    }

    public Texture getPlaneteFar(){
        return manager.get("Planets/far.png");
    }

    public int getNbSoleilTexture(){
        return this.soleilTexturePath.size;
    }

    public Texture getSoleilTexture(int id){
        return manager.get(soleilTexturePath.get(id));
    }
    public Texture getBackgroundTexture(int id){
        return manager.get("Background/"+id+".jpg");
    }
}
