package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Collection;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.collect.Multimap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
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
import antonafanasjew.cosmodog.view.transitions.ActorTransition;

public class DynamicPiecesRenderer extends AbstractRenderer {

	public static class DynamicPiecesRendererParam {

		public static DynamicPiecesRendererParam BOTTOM = new DynamicPiecesRendererParam(true);
		public static DynamicPiecesRendererParam TOP = new DynamicPiecesRendererParam(false);

		private boolean bottomNotTop;

		public DynamicPiecesRendererParam(boolean bottomNotTop) {
			this.bottomNotTop = bottomNotTop;
		}

		public boolean isBottomNotTop() {
			return bottomNotTop;
		}

	}

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();

		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

		DynamicPiecesRendererParam dynamicPiecerenderingParam = (DynamicPiecesRendererParam) renderingParameter;

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.getMap();
		Cam cam = cosmodogGame.getCam();

		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;

		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;

		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

		Multimap<Class<?>, DynamicPiece> dynamicPieces = map.visibleDynamicPieces(tileNoX, tileNoY, tilesW, tilesH, 2);

		Collection<DynamicPiece> blocks = dynamicPieces.get(Block.class);

		for (DynamicPiece piece : blocks) {
			
			Block block = (Block) piece;

			String animationIdPrefix = "dynamicPiece";
			String animationIdStil = block.getStil().substring(0, 1).toUpperCase() + block.getStil().substring(1);
			String animationIdPrefixIndex = String.valueOf(block.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = block.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdStil + animationIdPrefixIndex + animationIdInfix + animationSuffix;

			float pieceOffsetX = 0.0f;
			float pieceOffsetY = 0.0f;
			
			ActorTransition moveableTransition = cosmodogGame.getActorTransitionRegistry().get(block.asActor());
			boolean moveableIsMoving = moveableTransition != null;
			if (moveableIsMoving) {
				pieceOffsetX = tileWidth * moveableTransition.getTransitionalOffsetX();
				pieceOffsetY = tileHeight * moveableTransition.getTransitionalOffsetY();
			}
			
			applicationContext.getAnimations().get(animationId).draw(
					((piece.getPositionX() - tileNoX) * tileWidth) + pieceOffsetX, 
					((piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight) + pieceOffsetY
			);
		}
		
		Collection<DynamicPiece> secretDoors = dynamicPieces.get(SecretDoor.class);

		for (DynamicPiece piece : secretDoors) {
			
			SecretDoor door = (SecretDoor) piece;

			String animationIdPrefix = "dynamicPieceSecretDoor";
			String animationIdStil = door.getStil().substring(0, 1).toUpperCase() + door.getStil().substring(1);
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = door.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdStil + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}
		
		Collection<DynamicPiece> guideTerminals = dynamicPieces.get(Terminal.class);

		for (DynamicPiece piece : guideTerminals) {
			if (dynamicPiecerenderingParam.isBottomNotTop()) {
				String animationId = "dynamicPieceGuideTerminal";
				applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);
			}
		}

		Collection<DynamicPiece> stones = dynamicPieces.get(Stone.class);

		for (DynamicPiece piece : stones) {
			Stone stone = (Stone) piece;

			String animationIdPrefix = "dynamicPieceStone";
			String animationIdPrefixIndex = String.valueOf(stone.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = stone.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> hardStones = dynamicPieces.get(HardStone.class);

		for (DynamicPiece piece : hardStones) {
			HardStone hardStone = (HardStone) piece;

			String animationIdPrefix = "dynamicPieceHardStone";
			String animationIdPrefixIndex = String.valueOf(hardStone.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = hardStone.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> trees = dynamicPieces.get(Tree.class);

		for (DynamicPiece piece : trees) {
			Tree tree = (Tree) piece;

			String animationIdPrefix = "dynamicPieceTree";
			String animationIdPrefixIndex = String.valueOf(tree.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = tree.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> bamboos = dynamicPieces.get(Bamboo.class);

		for (DynamicPiece piece : bamboos) {
			Bamboo bamboo = (Bamboo) piece;

			String animationIdPrefix = "dynamicPieceBamboo";
			String animationIdPrefixIndex = String.valueOf(bamboo.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = bamboo.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> binaryIndicators = dynamicPieces.get(BinaryIndicator.class);

		for (DynamicPiece piece : binaryIndicators) {
			BinaryIndicator binaryIndicator = (BinaryIndicator) piece;

			String animationIdPrefix = "dynamicPieceAlienSwitch";
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = binaryIndicator.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> walls = dynamicPieces.get(CrumbledWall.class);

		for (DynamicPiece piece : walls) {
			CrumbledWall wall = (CrumbledWall) piece;

			String animationIdPrefix = "dynamicPieceCrumbledWall";
			String animationIdPrefixIndex = String.valueOf(wall.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = wall.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> gates = dynamicPieces.get(Gate.class);

		for (DynamicPiece piece : gates) {

			Gate gate = (Gate) piece;

			String animationIdPrefix = "dynamicPieceGate";
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = gate.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> crates = dynamicPieces.get(Crate.class);

		for (DynamicPiece piece : crates) {
			Crate crate = (Crate) piece;

			String animationIdPrefix = "dynamicPieceCrate";
			String animationIdPrefixIndex = String.valueOf(crate.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = crate.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> mines = dynamicPieces.get(Mine.class);

		for (DynamicPiece piece : mines) {
			Mine mine = (Mine) piece;

			String animationIdPrefix = "dynamicPieceMine";
			String animationIdPrefixIndex = String.valueOf(mine.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = mine.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> poisons = dynamicPieces.get(Poison.class);

		for (DynamicPiece piece : poisons) {
			Poison poison = (Poison) piece;

			String animationIdPrefix = "dynamicPiecePoison";
			String animationIdPrefixIndex = String.valueOf(poison.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = poison.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> pressureButtons = dynamicPieces.get(PressureButton.class);

		for (DynamicPiece piece : pressureButtons) {
			PressureButton pressureButton = (PressureButton) piece;

			String animationIdPrefix = "dynamicPiecePressureButton";
			String animationIdPrefixIndex = String.valueOf(pressureButton.getShapeNumber());
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = pressureButton.animationSuffixFromState();
			String animationId = animationIdPrefix + animationIdPrefixIndex + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> letterPlates = dynamicPieces.get(LetterPlate.class);

		for (DynamicPiece piece : letterPlates) {

			if (dynamicPiecerenderingParam.isBottomNotTop()) {

				LetterPlate letterPlate = (LetterPlate) piece;
				short shapeNumber = letterPlate.getShapeNumber();

				String letterPlateAnimationId;
				if (letterPlate.isPressed()) {
					letterPlateAnimationId = "dynamicPieceLetterPlatePressed" + shapeNumber;
				} else {
					letterPlateAnimationId = "dynamicPieceLetterPlate" + shapeNumber;
				}
				applicationContext.getAnimations().get(letterPlateAnimationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);

				char character = letterPlate.getCharacter();
				
				if (character != LetterPlate.CONTROL_LETTER_RESET && character != ' ') {
					int offsetFromA = character - 'a';
					
					if (letterPlate.isActive()) {
						String letterAnimationId = "dynamicPieceLetterPlateLetter" + offsetFromA;
						//When button is pressed, we want to visualize it also on the letter's vertical position
						int verticalOffset = letterPlate.isPressed() ? 1 : 0;
						applicationContext.getAnimations().get(letterAnimationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight + verticalOffset);
					}
				}
			}
		}

		Collection<DynamicPiece> doors = dynamicPieces.get(Door.class);

		for (DynamicPiece piece : doors) {
			Door door = (Door) piece;

			String animationIdPrefix = "dynamicPiece" + door.getDoorAppearanceType() + door.getExitDirectionType().getRepresentation();
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = door.isOpened() ? "Open" : "Closed";
			String animationId = animationIdPrefix + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		Collection<DynamicPiece> alienBaseBlockades = dynamicPieces.get(AlienBaseBlockade.class);

		for (DynamicPiece piece : alienBaseBlockades) {
			AlienBaseBlockade alienBaseBlockade = (AlienBaseBlockade) piece;

			String animationIdPrefix = "dynamicPieceAlienBaseBlockade";
			String animationIdInfix = dynamicPiecerenderingParam.isBottomNotTop() ? "Bottom" : "Top";
			String animationSuffix = alienBaseBlockade.isOpened() ? "Open" : "Closed";
			String animationId = animationIdPrefix + animationIdInfix + animationSuffix;
			applicationContext.getAnimations().get(animationId).draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY - (dynamicPiecerenderingParam.isBottomNotTop() ? 0 : 1)) * tileHeight);

		}

		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);

		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

	}

}
