package gameObjets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import gameConcepts.SuperActor;
import world.Assets;

import java.util.Random;

import static gameUi.WorldInputProcessor.palierZoom;
import static gameUi.WorldInputProcessor.worldZoom;


public class Soleil extends SuperActor {
    private Sprite sprite;
    private float localzoom=0;
    static private float scaling=0.015f;
    private int background;

    static public Soleil soleilGenerator(Vector2 position, Assets assetmanager){
        Random rand = new Random();
        Sprite sprite = new Sprite();
        return new Soleil(sprite,position.x,position.y, assetmanager.getSoleilTexture(rand .nextInt(assetmanager.getNbSoleilTexture())),rand.nextInt(25));
    }

    private Soleil(Sprite sprite,float x,float y,Texture close,int background){
        this.background=background;
        this.position= new Vector2(x,y);
        this.sprite=sprite;
        sprite.setRegion(close);
        sprite.setBounds(x-sprite.getTexture().getWidth()/2,y-sprite.getTexture().getHeight()/2,sprite.getTexture().getWidth(),sprite.getTexture().getHeight());
        this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
    }

    public int getBackground(){
        return this.background;
    }

    private void sizeSwitch() {
        if(worldZoom>palierZoom[2]*200){
            float scale = palierZoom[2]*200 + (float) Math.sqrt(worldZoom-palierZoom[2]*200);
            sprite.setBounds(this.position.x-sprite.getTexture().getWidth()*scale/2*scaling,this.position.y-sprite.getTexture().getHeight()*scale/2*scaling,sprite.getTexture().getWidth()*scale*scaling,sprite.getTexture().getHeight()*scale*scaling);
            this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
        }
        else if (worldZoom>palierZoom[2]){
            sprite.setBounds(this.position.x-sprite.getTexture().getWidth()*worldZoom/2*scaling,this.position.y-sprite.getTexture().getHeight()*worldZoom/2*scaling,sprite.getTexture().getWidth()*worldZoom*scaling,sprite.getTexture().getHeight()*worldZoom*scaling);
            this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
        }
        else{
            sprite.setBounds(this.position.x-sprite.getTexture().getWidth()/2,this.position.y-sprite.getTexture().getHeight()/2,sprite.getTexture().getWidth(),sprite.getTexture().getHeight());
            this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(),sprite.getHeight());
        }
    }

    @Override
    public float getX() {
        return this.position.x;
    }

    @Override
    public float getY() {
        return this.position.y;
    }



    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(localzoom!=worldZoom){
            localzoom=worldZoom;
            this.sizeSwitch();
        }
        this.sprite.draw(batch);

    }
}
