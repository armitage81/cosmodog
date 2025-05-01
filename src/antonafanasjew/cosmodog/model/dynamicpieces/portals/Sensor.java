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
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.ActivatableHolder;
import antonafanasjew.cosmodog.model.portals.interfaces.PresenceDetector;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;

public class Sensor extends DynamicPiece implements ActivatableHolder, PresenceDetector {

    private final List<Activatable> activatables = new ArrayList<>();
    private boolean presencePresent;

    public static Sensor create(Position position) {
        Sensor sensor = new Sensor();
        sensor.setPosition(position);
        return sensor;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return true;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "";
    }

    @Override
    public void addActivatable(Activatable activatable) {
        this.activatables.add(activatable);
    }

    @Override
    public List<Activatable> getActivatables() {
        return activatables;
    }

    @Override
    public void presenceDetected(CosmodogGame game, Actor presence) {
        this.presencePresent = true;
        for (Activatable activatable : activatables) {
            if (activatable.canActivate(game)) {
                if (activatable instanceof Bollard bollard) {
                    if (!bollard.isActive()) {
                        AsyncAction action;
                        if (bollard.isOpen()) {
                            action = new RaisingBollardAction(RaisingBollardAction.DURATION, bollard);
                        } else {
                            action = new SinkingBollardAction(SinkingBollardAction.DURATION, bollard);
                        }
                        ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                } else if (activatable instanceof OneWayBollard oneWayBollard) {
                    if (!oneWayBollard.isActive()) {
                        AsyncAction action = new SwitchingOneWayBollardAction(500, oneWayBollard);
                        ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                }
            }
        }
    }

    @Override
    public void presenceLost(CosmodogGame game) {
        this.presencePresent = false;
        for (Activatable activatable : activatables) {
            if (activatable.canDeactivate(game)) {
                if (activatable instanceof Bollard bollard) {
                    if (bollard.isActive()) {
                        AsyncAction action;
                        if (bollard.isOpen()) {
                            action = new RaisingBollardAction(RaisingBollardAction.DURATION, bollard);
                        } else {
                            action = new SinkingBollardAction(SinkingBollardAction.DURATION, bollard);
                        }
                        ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                } else if (activatable instanceof OneWayBollard oneWayBollard) {
                    if (oneWayBollard.isActive()) {
                        AsyncAction action = new SwitchingOneWayBollardAction(500, oneWayBollard);
                        ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                }
            }
        }
    }

    public boolean isPresencePresent() {
        return presencePresent;
    }

    //There can be cubes over sensors. In this case, sensors should be rendered first, since they are on the ground.
    @Override
    public int renderingPriority() {
        return 1;
    }
}
