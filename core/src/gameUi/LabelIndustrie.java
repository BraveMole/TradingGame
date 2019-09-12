package gameUi;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gameObjets.Industrie;

public class LabelIndustrie extends Label {
    private Industrie i;
    public LabelIndustrie(CharSequence text, Skin skin, Industrie i) {
        super(text, skin);
        this.i=i;
    }
    public Industrie getIndustrie(){
        return this.i;
    }
    @Override
    public String toString() {
        return String.valueOf(this.getText());
    }
}
