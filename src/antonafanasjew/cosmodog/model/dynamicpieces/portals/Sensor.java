package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.camera.CamMovementActionWithConstantSpeed;
import antonafanasjew.cosmodog.actions.mechanism.RaisingBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SinkingBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SwitchingOneWayBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.TurningReflectorClockwiseAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.ActivatableHolder;
import antonafanasjew.cosmodog.model.portals.interfaces.PresenceDetector;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Sensor extends DynamicPiece implements ActivatableHolder, PresenceDetector {

    private final Multimap<Integer, Activatable> activatables = ArrayListMultimap.create();

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
    public void addActivatable(int priority, Activatable activatable) {
        this.activatables.put(priority, activatable);
    }

    public List<Activatable> getActivatables() {
        Set<Integer> priorities = activatables.keySet();
        List<Activatable> retVal = new ArrayList<>();
        for (Integer priority : priorities) {
            retVal.addAll(activatables.get(priority));
        }
        return retVal;
    }

    @Override
    public void presenceDetected(CosmodogGame game, Actor presence) {

        if (this.presencePresent) {
            return;
        }

        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SENSOR_PRESENCE_DETECTED).play();
        Player player = ApplicationContextUtils.getPlayer();
        this.presencePresent = true;

        ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
        for (Activatable activatable : getActivatables()) {

            if (activatable.canActivate(game)) {

                actionRegistry.registerAction(AsyncActionType.MOVEMENT, new CamMovementActionWithConstantSpeed(16*5, PositionUtils.toPixelPosition(activatable.getPosition()), game));

                if (activatable instanceof Bollard bollard) {
                    if (!bollard.isActive()) {
                        AsyncAction action;
                        if (bollard.isOpen()) {
                            action = new RaisingBollardAction(RaisingBollardAction.DURATION, bollard);
                        } else {
                            action = new SinkingBollardAction(SinkingBollardAction.DURATION, bollard);
                        }
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                } else if (activatable instanceof OneWayBollard oneWayBollard) {
                    if (!oneWayBollard.isActive()) {
                        AsyncAction action = new SwitchingOneWayBollardAction(500, oneWayBollard);
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                }
            }
        }

        actionRegistry.registerAction(AsyncActionType.MOVEMENT, new CamMovementActionWithConstantSpeed(16*10, PositionUtils.toPixelPosition(player.getPosition()), game));
    }

    @Override
    public void presenceLost(CosmodogGame game) {

        if (!this.presencePresent) {
            return;
        }

        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SENSOR_PRESENCE_LOST).play();
        Player player = ApplicationContextUtils.getPlayer();
        this.presencePresent = false;

        ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
        for (Activatable activatable : getActivatables()) {

            if (activatable.canDeactivate(game)) {

                actionRegistry.registerAction(AsyncActionType.MOVEMENT, new CamMovementActionWithConstantSpeed(16*5, PositionUtils.toPixelPosition(activatable.getPosition()), game));

                if (activatable instanceof Bollard bollard) {
                    if (bollard.isActive()) {
                        AsyncAction action;
                        if (bollard.isOpen()) {
                            action = new RaisingBollardAction(RaisingBollardAction.DURATION, bollard);
                        } else {
                            action = new SinkingBollardAction(SinkingBollardAction.DURATION, bollard);
                        }

                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                } else if (activatable instanceof OneWayBollard oneWayBollard) {
                    if (oneWayBollard.isActive()) {
                        AsyncAction action = new SwitchingOneWayBollardAction(500, oneWayBollard);
                        actionRegistry.registerAction(AsyncActionType.MOVEMENT, action);
                    }
                }
            }
        }
        actionRegistry.registerAction(AsyncActionType.MOVEMENT, new CamMovementActionWithConstantSpeed(16*10, PositionUtils.toPixelPosition(player.getPosition()), game));
    }

    @Override
    public void reset() {
        presencePresent = false;
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
