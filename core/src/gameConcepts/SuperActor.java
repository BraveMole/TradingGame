package gameConcepts;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SuperActor extends Actor {

    protected int zoomlevel=0;
    protected int texturezoomlevel=0;
    protected Vector2 position;

        public void setZoomlevel(int zoomlevel){
        this.zoomlevel=zoomlevel;
    }
    public Vector2 getPosition(){
        return this.position;
    }
    public boolean positionEqual(Vector3 camposition){
        return Math.abs(camposition.x - this.position.x)<50 &&  Math.abs(camposition.y - this.position.y)<50;
    }


}
