package antonafanasjew.cosmodog.actions.weather;

import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class SnowfallChangeAction extends VariableLengthAsyncAction {

    public static SnowfallChangeAction fadingInActionInstance(int duration) {
        SnowfallChangeAction o = new SnowfallChangeAction();
        o.duration = duration;
        o.setFadesInNotOut(true);
        o.setChangeRate(0.0f);
        return o;
    }

    public static SnowfallChangeAction fadingOutActionInstance(int duration) {
        SnowfallChangeAction o = new SnowfallChangeAction();
        o.duration = duration;
        o.setFadesInNotOut(false);
        o.setChangeRate(1.0f);
        return o;
    }

    private int duration;
    private float changeRate;
    private boolean fadesInNotOut;

    private SnowfallChangeAction() {

    }

    @Override
    public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
        int diff = after - before;
        float diffRelatedToDuration = (float)diff/duration;
        if (fadesInNotOut) {
            changeRate += diffRelatedToDuration;
            changeRate = Math.min(1.0f, changeRate);
        } else {
            changeRate -= diffRelatedToDuration;
            changeRate = Math.max(0.0f, changeRate);
        }
    }

    @Override
    public boolean hasFinished() {
        return false;
    }

    public void resetRate() {
        this.changeRate = 0.0f;
        this.fadesInNotOut = false;
    }

    public void setChangeRate(float changeRate) {
        this.changeRate = changeRate;
    }

    public float getChangeRate() {
        return changeRate;
    }

    public void setFadesInNotOut(boolean fadesInNotOut) {
        this.fadesInNotOut = fadesInNotOut;
    }

    public boolean isFadesInNotOut() {
        return fadesInNotOut;
    }
}
