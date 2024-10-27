package antonafanasjew.cosmodog.rules;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.listener.movement.MovementListenerAdapter;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.rules.events.GameEventTeleported;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.GameEventUtils;

import java.io.Serial;

public class RuleBookMovementListener extends MovementListenerAdapter {

	@Serial
	private static final long serialVersionUID = -8999782338441869506L;

	@Override
	public void onEnteringTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		GameEventUtils.throwEvent(new GameEventChangedPosition());
	}
	
	@Override
	public void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		GameEventUtils.throwEvent(new GameEventEndedTurn());
	}
	
	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		GameEventUtils.throwEvent(new GameEventTeleported());
	}

}
