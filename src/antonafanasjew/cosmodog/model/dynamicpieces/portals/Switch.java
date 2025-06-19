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
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.interfaces.Pressable;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.model.portals.interfaces.SwitchableHolder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

public class Switch extends DynamicPiece implements Pressable, SwitchableHolder {

    private final Multimap<Integer, Switchable> switchables = ArrayListMultimap.create();

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
        Player player = ApplicationContextUtils.getPlayer();
        DirectionType directionType = PositionUtils.targetDirection(player, this);
        if (directionType == DirectionType.UP) {
            press(game);
        }
    }

    public void addSwitchable(int priority, Switchable switchable) {
        this.switchables.put(priority, switchable);
    }

    public List<Switchable> getSwitchables() {
        Set<Integer> priorities = switchables.keySet();
        List<Switchable> retVal = new ArrayList<>();
        for (Integer priority : priorities) {
            retVal.addAll(switchables.get(priority));
        }
        return retVal;
    }

    @Override
    public void press(CosmodogGame game) {
        for (Switchable switchable : getSwitchables()) {
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
