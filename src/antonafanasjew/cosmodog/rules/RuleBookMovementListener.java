package antonafanasjew.cosmodog.rules;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.listener.movement.MovementListenerAdapter;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.rules.events.GameEventTeleported;
import antonafanasjew.cosmodog.util.GameEventUtils;

public class RuleBookMovementListener extends MovementListenerAdapter {

	private static final long serialVersionUID = -8999782338441869506L;

	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		GameEventUtils.throwEvent(new GameEventChangedPosition());
	}
	
	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		GameEventUtils.throwEvent(new GameEventEndedTurn());
	}
	
	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		GameEventUtils.throwEvent(new GameEventTeleported());
	}
	
}
