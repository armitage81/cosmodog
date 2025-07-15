package antonafanasjew.cosmodog.rendering.renderer.dynamicpieces;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.*;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.rendering.renderer.renderingutils.ActorRendererUtils;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
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
import org.newdawn.slick.Image;
import profiling.ProfilerUtils;

public class DynamicPiecesRenderer extends AbstractRenderer {

	public static List<Class<? extends DynamicPiece>> PIECES_FOR_DEFAULT_RENDERING = List.of(
			OneWayBollard.class,
			AutoBollard.class,
			Bollard.class,
			Switch.class,
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
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();

		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

		DynamicPiecesRendererParam dynamicPieceRenderingParam = (DynamicPiecesRendererParam) renderingParameter;

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.mapOfPlayerLocation();
		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();

		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

		AtomicReference<List<DynamicPiece>> dynamicPieces = new AtomicReference<>();

		ProfilerUtils.runWithProfiling("calculateVisibleDynamicPieces", () -> {

			List<DynamicPiece> visiblePieces = map
					.getMapPieces()
					.piecesInArea(e -> e instanceof DynamicPiece, camTilePosition.tileX(), camTilePosition.tileY(), camTilePosition.widthInTiles(), camTilePosition.heightInTiles(), 2)
					.stream()
					.map(e -> (DynamicPiece)e)
					.toList();

			dynamicPieces.set(visiblePieces);
		});


		AtomicReference<List<DynamicPiece>> sortedDynamicPieces = new AtomicReference<>();

		ProfilerUtils.runWithProfiling("sortVisibleDynamicPieces", () -> {
			sortedDynamicPieces.set(dynamicPieces.get().stream().sorted(Comparator.comparingInt(DynamicPiece::renderingPriority)).toList());
		});

		for (DynamicPiece dynamicPiece : sortedDynamicPieces.get()) {

			Vector pieceVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(dynamicPiece.getPosition(), camTilePosition);
			Vector adjacentNorthPieceVectorRelatedToCam = new Vector(pieceVectorRelatedToCam.getX(), pieceVectorRelatedToCam.getY() - tileLength);

			float pieceX = pieceVectorRelatedToCam.getX();
			float pieceY = pieceVectorRelatedToCam.getY();
			float pieceNorthY = adjacentNorthPieceVectorRelatedToCam.getY();
			float topBottomDependentY = dynamicPieceRenderingParam.bottomNotTop() ? pieceY : pieceNorthY;

			String animationId = dynamicPiece.animationId(dynamicPieceRenderingParam.bottomNotTop());

			if (dynamicPiece instanceof Jammer jammer) {
				if (!jammer.isHidden()) {
					applicationContext.getAnimations().get(animationId).draw(pieceX, topBottomDependentY);
				}
			}

			if (dynamicPiece instanceof Sensor sensor) {

				if (((DynamicPiecesRendererParam) renderingParameter).bottomNotTop) {

					animationId = sensor.isPresencePresent() ? "dynamicPieceActiveSensor" : "dynamicPieceInactiveSensor";

					applicationContext.getAnimations().get(animationId).draw(
							pieceX,
							pieceY
					);
				}
			}

			if (dynamicPiece instanceof Cube cube) {

				CrossTileMotion cubeMotion = ActorRendererUtils.actorMotion(cube.asActor());

				boolean usingPortal = cubeMotion != null && cubeMotion.isContainsTeleportation();

				if (usingPortal) {

					Vector cubeCoordinatesRelatedToCam = Cam
							.positionVectorRelatedToCamTilePosition(
									cube.getPosition(),
									camTilePosition
							)
							;

					Vector exitCoordinatesRelatedToCam = Cam
							.positionVectorRelatedToCamTilePosition(
									DirectionType.facedAdjacentPosition(cubeMotion.getExitPortal().position, cubeMotion.getExitPortal().directionType),
									camTilePosition
							);

					String transparencyInfix = cube.isTransparent() ? "Transparent" : "";
					String heightSuffix = dynamicPieceRenderingParam.bottomNotTop ? "Bottom" : "Top";


					//Assuming that all entering and exiting animations have 6 frames.
					int animationFrameCount = 6;
					int enteringAnimationPhase = (int)Math.abs((cubeMotion.getCrossTileOffsetX() + cubeMotion.getCrossTileOffsetY()) * animationFrameCount);

					//Not sure if a case can happen when the offset is 1 and the phase becomes 6 and hence out of bounds, so being defensive here.
					if (enteringAnimationPhase == animationFrameCount) {
						enteringAnimationPhase = animationFrameCount - 1;
					}
					//Exiting animations are entering animations in reverse.
					int exitingAnimationPhase = animationFrameCount - enteringAnimationPhase - 1;

					Animation enteringPortalAnimation;
					if (cubeMotion.getEntrancePortal().directionType == DirectionType.RIGHT) {
						enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeEastFromPortal" + heightSuffix);
					} else if (cubeMotion.getEntrancePortal().directionType == DirectionType.LEFT) {
						enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeWestFromPortal" + heightSuffix);
					} else if (cubeMotion.getEntrancePortal().directionType == DirectionType.UP) {
						enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeNorthFromPortal" + heightSuffix);
					} else {
						enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeSouthFromPortal" + heightSuffix);
					}
					float enterY = dynamicPieceRenderingParam.bottomNotTop ? cubeCoordinatesRelatedToCam.getY() : cubeCoordinatesRelatedToCam.getY() - tileLength;
					enteringPortalAnimation.getImage(enteringAnimationPhase).draw(cubeCoordinatesRelatedToCam.getX(), enterY);

					Animation exitingPortalAnimation;
					if (cubeMotion.getExitPortal().directionType == DirectionType.RIGHT) {
						exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeEastFromPortal" + heightSuffix);
					} else if (cubeMotion.getExitPortal().directionType == DirectionType.LEFT) {
						exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeWestFromPortal" + heightSuffix);
					} else if (cubeMotion.getExitPortal().directionType == DirectionType.DOWN) {
						exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeSouthFromPortal" + heightSuffix);
					} else {
						exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("dynamicPiece" + transparencyInfix + "CubeNorthFromPortal" + heightSuffix);
					}

					//Top part should be drawn one tile to the North
					float exitY = dynamicPieceRenderingParam.bottomNotTop ? exitCoordinatesRelatedToCam.getY() : exitCoordinatesRelatedToCam.getY() - tileLength;
					exitingPortalAnimation.getImage(exitingAnimationPhase).draw(exitCoordinatesRelatedToCam.getX(), exitY);

				} else {
					float pieceOffsetX = 0.0f;
					float pieceOffsetY = 0.0f;

					MovementAction movementAction = (MovementAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class);

					if (movementAction != null) {

						CrossTileMotion moveableMotion = movementAction.getActorMotions().get(cube.asActor());
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
			}

			if (dynamicPiece instanceof Block block) {

				float pieceOffsetX = 0.0f;
				float pieceOffsetY = 0.0f;

				MovementAction movementAction = (MovementAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class);

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

			if (dynamicPiece instanceof Reflector reflector) {
				if (dynamicPieceRenderingParam.bottomNotTop()) {
					Animation animation = applicationContext.getAnimations().get(animationId);
					Image image = animation.getImage(reflector.getVisualState());
					image.draw(pieceX, pieceY);
				}
			}

			if (dynamicPiece instanceof LetterPlate letterPlate) {

				if (dynamicPieceRenderingParam.bottomNotTop()) {

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
				if (dynamicPieceRenderingParam.bottomNotTop()) {
					applicationContext.getAnimations().get(animationId).draw(pieceX, pieceY);
				}
			}

			if (PIECES_FOR_DEFAULT_RENDERING.contains(dynamicPiece.getClass())) {
				final String finalAnimationId = animationId;
				ProfilerUtils.runWithProfiling("drawDefaultDynamicPiece", () -> {
					applicationContext.getAnimations().get(finalAnimationId).draw(pieceX, topBottomDependentY);
				});
			}

		}
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());

		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

	}

}
