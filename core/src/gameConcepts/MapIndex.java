package gameConcepts;

import java.util.Objects;

public class MapIndex<x,y> {

    private int x;
    private int y;

    public boolean isDiagonalTo(MapIndex b){
        return !(b.getY()-y==0)||(b.getX()-x==0);
    }

    public MapIndex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapIndex<?, ?> mapIndex = (MapIndex<?, ?>) o;
        return x == mapIndex.x &&
                y == mapIndex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
