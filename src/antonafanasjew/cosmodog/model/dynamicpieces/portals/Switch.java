package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.portals.interfaces.Pressable;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.model.portals.interfaces.SwitchableHolder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;

public class Switch extends DynamicPiece implements Pressable, SwitchableHolder {

    private final List<Switchable> switchables = new ArrayList<>();

    public static Switch createInstance(Position position) {
        Switch aSwitch = new Switch();
        aSwitch.setPosition(position);
        return aSwitch;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "";
    }

    @Override
    public void interact() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        press(game);
    }

    public void addSwitchable(Switchable switchable) {
        this.switchables.add(switchable);
    }

    public List<Switchable> getSwitchables() {
        return switchables;
    }

    @Override
    public void press(CosmodogGame game) {
        switchables.forEach(Switchable::switchToNextState);
    }

}
