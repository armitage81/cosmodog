package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;

public class MoveableDynamicPiece extends DynamicPiece {

	private Actor moveableAsActor;
	
	public MoveableDynamicPiece() {
		moveableAsActor = new MoveableActor();
	}
	
	public static class MoveableActor extends Player {

		private static final long serialVersionUID = 8952823001282507082L;
		
	}
	
	private static final long serialVersionUID = 7641013857041088738L;

	@Override
	public boolean wrapsCollectible() {
		return false;
	}
	
	//We need an actor to use the collision validator.
	public Actor asActor() {
		moveableAsActor.setPositionX(this.getPositionX());
		moveableAsActor.setPositionY(this.getPositionY());
		return moveableAsActor;
	}
	
}
