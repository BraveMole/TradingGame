package gameUi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import gameConcepts.Population;

public class ButtonPop extends TextButton {

    public Population getP() {
        return p;
    }

    private Population p;
    public ButtonPop(Skin skin, Population p) {
        super("Population : "+p.getNbTravailleur(), skin);
        this.setName("");
        this.getLabel().setTouchable(Touchable.disabled);
        this.setTouchable(Touchable.enabled);
        this.removeListener(this.getClickListener());
        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getClickListener().setVisualPressed(true);
                return false;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getClickListener().setVisualPressed(false);
            }
        });
        this.p=p;
    }

}
