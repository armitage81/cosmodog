package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;

/**
 * Wraps another async action with cam focus. The lifecycle is as following:
 * 
 * The camera loses focus on the player and moves towards a position.
 * The underlying action is executed.
 * The camera moves back to the player and focuses on him.
 * 
 * The speed of the camera movement is defined as time duration. That is, the camera will move faster if the target position
 * is more remote from the player.
 * 
 * The camera movement to the focus and the movement back are themselves async actions with fixed length. Since they are parts of this action
 * they are registered in a local action phase registry. The same happens with the underlying action.
 *
 */
public class CamCenteringDecoratorAction  extends VariableLengthAsyncAction {

	private static final long serialVersionUID = 5171259029091809078L;

	private int camMovementDuration;
	private float camCenteringX;
	private float camCenteringY;
	private AsyncAction underlyingAsyncAction;
	private CosmodogGame cosmodogGame;
	
	
	private ActionRegistry actionPhaseRegistry = new ActionRegistry();
	
	public CamCenteringDecoratorAction(int camMovementDuration, int positionX, int positionY, AsyncAction underlyingAsyncAction, CosmodogGame cosmodogGame) {
		this.camMovementDuration = camMovementDuration;
		float tileWidth = cosmodogGame.getMap().getTileWidth();
		float tileHeight = cosmodogGame.getMap().getTileHeight();
		this.camCenteringX = positionX * tileWidth + tileWidth / 2;
		this.camCenteringY = positionY * tileHeight + tileHeight / 2;
		this.underlyingAsyncAction = underlyingAsyncAction;
		this.cosmodogGame = cosmodogGame;
	}
	
	public CamCenteringDecoratorAction(int camMovementDuration, Piece piece, AsyncAction underlyingAsyncAction, CosmodogGame cosmodogGame) {
		this(camMovementDuration, piece.getPositionX(), piece.getPositionY(), underlyingAsyncAction, cosmodogGame);
		
	}
	
	public CamCenteringDecoratorAction(int camMovementDuration, float camCenteringX, float camCenteringY, AsyncAction underlyingAsyncAction, CosmodogGame cosmodogGame) {
		this.camMovementDuration = camMovementDuration;
		this.camCenteringX = camCenteringX;
		this.camCenteringY = camCenteringY;
		this.underlyingAsyncAction = underlyingAsyncAction;
		this.cosmodogGame = cosmodogGame;
		
	}
	
	@Override
	public void onTrigger() {
		float initialCamX = cosmodogGame.getCam().viewCopy().centerX() / cosmodogGame.getCam().getZoomFactor();
		float initialCamY = cosmodogGame.getCam().viewCopy().centerY() / cosmodogGame.getCam().getZoomFactor();
		
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new CamMovementAction(camMovementDuration, camCenteringX, camCenteringY, cosmodogGame));
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, underlyingAsyncAction);
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new CamMovementAction(camMovementDuration, initialCamX, initialCamY, cosmodogGame));
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}
	
	@Override
	public void onEnd() {
		cosmodogGame.getCam().focusOnPiece(cosmodogGame.getMap(), 0, 0, cosmodogGame.getPlayer());
	}
	
	@Override
	public boolean hasFinished() {
		return !actionPhaseRegistry.isActionRegistered(AsyncActionType.CUTSCENE);
	}
	
}
