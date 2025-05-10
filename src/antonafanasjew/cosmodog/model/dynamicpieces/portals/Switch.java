package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.mechanism.RaisingBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SinkingBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SwitchingOneWayBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.TurningReflectorClockwiseAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.portals.interfaces.Pressable;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.model.portals.interfaces.SwitchableHolder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

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
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return bottomNotTop ? "dynamicPieceSwitchBottom" : "dynamicPieceSwitchTop";
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
        for (Switchable switchable : switchables) {
            if (switchable instanceof  Bollard bollard) {
                boolean open = bollard.isOpen();
                AsyncAction action;
                if (open) {
                    action = new RaisingBollardAction(RaisingBollardAction.DURATION, bollard);
                } else {
                    action = new SinkingBollardAction(SinkingBollardAction.DURATION, bollard);
                }
                ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
            } else if (switchable instanceof Reflector reflector) {
                AsyncAction action = new TurningReflectorClockwiseAction(500, reflector);
                ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
            } else if (switchable instanceof OneWayBollard oneWayBollard) {
                AsyncAction action = new SwitchingOneWayBollardAction(500, oneWayBollard);
                ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
            }
        }

    }

}
