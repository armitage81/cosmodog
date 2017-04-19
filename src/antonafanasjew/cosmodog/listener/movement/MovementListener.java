package antonafanasjew.cosmodog.listener.movement;

import java.io.Serializable;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;

public interface MovementListener extends Serializable {

	void beforeMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext);
	void onLeavingTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext);
	void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext);
	void onInteractingWithTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext);
	void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext);
	
	void beforeWaiting(Actor actor, ApplicationContext applicationContext);
	void afterWaiting(Actor actor, ApplicationContext applicationContext);
	
	void beforeFight(Actor actor, ApplicationContext applicationContext);
	void afterFight(Actor actor, ApplicationContext applicationContext);
	
	void beforeTeleportation(Actor actor, ApplicationContext applicationContext);
	void afterTeleportation(Actor actor, ApplicationContext applicationContext);
	
	
}
