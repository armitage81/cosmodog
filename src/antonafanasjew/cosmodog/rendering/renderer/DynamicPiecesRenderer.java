package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.collect.Multimap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.dynamicpieces.AlienBaseBlockade;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.BinaryIndicator;
import antonafanasjew.cosmodog.model.dynamicpieces.Block;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.CrumbledWall;
import antonafanasjew.cosmodog.model.dynamicpieces.Door;
import antonafanasjew.cosmodog.model.dynamicpieces.Gate;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.LetterPlate;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Terminal;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;

public class DynamicPiecesRenderer extends AbstractRenderer {

	public static List<Class<? extends DynamicPiece>> PIECES_FOR_DEFAULT_RENDERING = List.of(
			SecretDoor.class,
			Stone.class,
			HardStone.class,
			Tree.class,
			Bamboo.class,
			BinaryIndicator.class,
			CrumbledWall.class,
			Gate.class,
			Crate.class,
			Mine.class,
			Poison.class,
			PressureButton.class,
			Door.class,
			AlienBaseBlockade.class
	);

	public record DynamicPiecesRendererParam(boolean bottomNotTop) {
			public static DynamicPiecesRendererParam BOTTOM = new DynamicPiecesRendererParam(true);
			public static DynamicPiecesRendererParam TOP = new DynamicPiecesRendererParam(false);
	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();

		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

		DynamicPiecesRendererParam dynamicPiecerenderingParam = (DynamicPiecesRendererParam) renderingParameter;

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.mapOfPlayerLocation();

		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();

		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

		Multimap<Class<?>, DynamicPiece> dynamicPieces = map.visibleDynamicPieces(
				Position.fromCoordinates(
						camTilePosition.tileX(),
						camTilePosition.tileY(),
						map.getMapType()
				),
				camTilePosition.widthInTiles(),
				camTilePosition.heightInTiles(),
				2
		);

		for (DynamicPiece dynamicPiece : dynamicPieces.values()) {

			Vector pieceVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(dynamicPiece.getPosition(), camTilePosition);
			Vector adjacentNorthPieceVectorRelatedToCam = new Vector(pieceVectorRelatedToCam.getX(), pieceVectorRelatedToCam.getY() - tileLength);

			float pieceX = pieceVectorRelatedToCam.getX();
			float pieceY = pieceVectorRelatedToCam.getY();
			float pieceNorthY = adjacentNorthPieceVectorRelatedToCam.getY();
			float topBottomDependentY = dynamicPiecerenderingParam.bottomNotTop() ? pieceY : pieceNorthY;

			String animationId = dynamicPiece.animationId(dynamicPiecerenderingParam.bottomNotTop());

			if (dynamicPiece instanceof Block block) {

				float pieceOffsetX = 0.0f;
				float pieceOffsetY = 0.0f;

				MovementAction movementAction = (MovementAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT);

				if (movementAction != null) {

					CrossTileMotion moveableMotion = movementAction.getActorMotions().get(block.asActor());
					boolean moveableIsMoving = moveableMotion != null;
					if (moveableIsMoving) {
						pieceOffsetX = tileLength * moveableMotion.getCrossTileOffsetX();
						pieceOffsetY = tileLength * moveableMotion.getCrossTileOffsetY();
					}

				}

				applicationContext.getAnimations().get(animationId).draw(
						pieceX + pieceOffsetX,
						topBottomDependentY + pieceOffsetY
				);
			}

			if (dynamicPiece instanceof LetterPlate letterPlate) {

				if (dynamicPiecerenderingParam.bottomNotTop()) {

					applicationContext.getAnimations().get(animationId).draw(pieceX, pieceY);

					char character = letterPlate.getCharacter();

					if (character != LetterPlate.CONTROL_LETTER_RESET && character != ' ') {
						int offsetFromA = character - 'a';

						if (letterPlate.isActive()) {
							String letterAnimationId = "dynamicPieceLetterPlateLetter" + offsetFromA;
							//When button is pressed, we want to visualize it also on the letter's vertical position
							int verticalOffset = letterPlate.isPressed() ? 1 : 0;
							applicationContext.getAnimations().get(letterAnimationId).draw(pieceX, pieceY + verticalOffset);
						}
					}
				}
			}

			if (dynamicPiece instanceof Terminal terminal) {
				if (dynamicPiecerenderingParam.bottomNotTop()) {
					applicationContext.getAnimations().get(animationId).draw(pieceX, pieceY);
				}
			}

			if (PIECES_FOR_DEFAULT_RENDERING.contains(dynamicPiece.getClass())) {
				applicationContext.getAnimations().get(animationId).draw(pieceX, topBottomDependentY);
			}

		}
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());

		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

	}

}
