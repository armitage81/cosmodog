package antonafanasjew.cosmodog.rendering.renderer.pieces;

import java.util.Map;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.TransitionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.MovementAttemptTransition;
import antonafanasjew.cosmodog.view.transitions.impl.PlayerAttackingFightPhaseTransition;

import com.google.common.collect.Maps;

public class VehicleRenderer extends AbstractPieceRenderer {

	private Map<DirectionType, String> vehicleDirection2animationKey = Maps.newHashMap();

	public VehicleRenderer() {
		vehicleDirection2animationKey.put(DirectionType.RIGHT, "vehicleRight");
		vehicleDirection2animationKey.put(DirectionType.DOWN, "vehicleDown");
		vehicleDirection2animationKey.put(DirectionType.LEFT, "vehicleLeft");
		vehicleDirection2animationKey.put(DirectionType.UP, "vehicleUp");
	}

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();
		Cam cam = cosmodogGame.getCam();

		ActorTransition playerTransition = cosmodogGame.getActorTransitionRegistry().get(player);
		
		FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();
		
		MovementAttemptTransition movementAttemptTransition = cosmodogGame.getMovementAttemptTransition();

		boolean playerIsMoving = playerTransition != null;
		boolean playerIsFighting = fightPhaseTransition != null && fightPhaseTransition instanceof PlayerAttackingFightPhaseTransition;
		boolean playerIsAttemptingBlockedPassage = movementAttemptTransition != null;
		boolean playerInPlatform = player.getInventory().hasPlatform();

		float pieceOffsetX = 0;
		float pieceOffsetY = 0;

		if (playerInPlatform && piece instanceof Platform == false && CosmodogMapUtils.isTileOnPlatform(piece.getPositionX(), piece.getPositionY(), player.getPositionX(), player.getPositionY())) {
		
			if (playerIsMoving) {
				pieceOffsetX = tileWidth * playerTransition.getTransitionalOffsetX();
				pieceOffsetY = tileHeight * playerTransition.getTransitionalOffsetY();
			}

			if (playerIsFighting) {

				float completion = fightPhaseTransition.getCompletion();

				float fightOffset = 0.0f;

				if (completion > 0.5f) {
					completion = 1.0f - completion;
				}

				fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;


				if (player.getDirection() == DirectionType.DOWN) {
					pieceOffsetY = fightOffset;
				}

				if (player.getDirection() == DirectionType.UP) {
					pieceOffsetY = -fightOffset;
				}

				if (player.getDirection() == DirectionType.RIGHT) {
					pieceOffsetX = fightOffset;
				}

				if (player.getDirection() == DirectionType.LEFT) {
					pieceOffsetX = -fightOffset;
				}

			}

			if (playerIsAttemptingBlockedPassage) {

				float completion = movementAttemptTransition.completion;

				float movementAttemptOffset = 0.0f;

				if (completion > 0.5f) {
					completion = 1.0f - completion;
				}

				movementAttemptOffset = (tileWidth * cam.getZoomFactor()) / 16.0f * completion;

				if (player.getDirection() == DirectionType.DOWN) {
					pieceOffsetY = movementAttemptOffset;
				}

				if (player.getDirection() == DirectionType.UP) {
					pieceOffsetY = -movementAttemptOffset;
				}

				if (player.getDirection() == DirectionType.RIGHT) {
					pieceOffsetX = movementAttemptOffset;
				}

				if (player.getDirection() == DirectionType.LEFT) {
					pieceOffsetX = -movementAttemptOffset;
				}
			}
		}
		
		Vehicle vehicle = (Vehicle) piece;
		DirectionType direction = vehicle.getDirection();
		String animationKey = vehicleDirection2animationKey.get(direction);
		Animation vehicleAnimation = applicationContext.getAnimations().get(animationKey);
		vehicleAnimation.draw((vehicle.getPositionX() - tileNoX) * tileWidth + pieceOffsetX, (vehicle.getPositionY() - tileNoY) * tileHeight + pieceOffsetY);
	}

}
