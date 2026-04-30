package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.OverheadPixelTextRenderer.OverheadPixelTextParam;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.util.TileUtils;

public class OverheadNotificationRenderer extends AbstractRenderer {

	private static final float SCALE_FACTOR = 2.0f;
	private static final float MAX_WIDTH = 500f;

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		OverheadNotificationAction action = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);

		if (action == null) {
			return;
		}

		OverheadNotificationAction.OverheadNotificationsState state = action.getProperty("state");

		if (state == null) {
			return;
		}

		Map<Character, Letter> letters = applicationContext.getOverheadCharacterLetters();
		Letter defaultLetter = letters.get('?');

		for (int i = 0; i < state.texts.size(); i++) {

			String rawText = state.texts.get(i);
			float completion = state.completions.get(i);
			Actor actor = state.actors.get(i);

			Cam cam = cosmodogGame.getCam();
			Cam.CamTilePosition camTilePosition = cam.camTilePosition();

			float movementOffsetX = 0;
			float movementOffsetY = 0;

			MovementAction movementAction = (MovementAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class);
			CrossTileMotion actorMotion = null;
			if (movementAction != null) {
				actorMotion = movementAction.getActorMotions().get(actor);
			}

			if (actorMotion != null) {
				movementOffsetX = tileLength * actorMotion.getCrossTileOffsetX();
				movementOffsetY = tileLength * actorMotion.getCrossTileOffsetY();
			}

			graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());

			int actorTileNoXOnVisibleMap = (int) (actor.getPosition().getX() - camTilePosition.tileX());
			int actorPosXOnVisibleMap = actorTileNoXOnVisibleMap * tileLength;
			float actorPosXOnVisibleMapInclMovement = actorPosXOnVisibleMap + movementOffsetX;
			float actorPosXOnVisibleMapInclMovementInclScale = actorPosXOnVisibleMapInclMovement * cam.getZoomFactor();

			int actorTileNoYOnVisibleMap = (int) (actor.getPosition().getY() - camTilePosition.tileY());
			int actorPosYOnVisibleMap = actorTileNoYOnVisibleMap * tileLength;
			float actorPosYOnVisibleMapInclMovement = actorPosYOnVisibleMap + movementOffsetY;
			float actorPosYOnVisibleMapInclMovementInclScale = actorPosYOnVisibleMapInclMovement * cam.getZoomFactor();

			boolean critical = rawText.contains("<font:critical>");
			String text = rawText.replace("<font:critical>", "").replace("<font:default>", "");
			Color color = critical ? FontType.OVERHEAD_PIXEL_CRITICAL_COLOR : FontType.OVERHEAD_PIXEL_DEFAULT_COLOR;

			List<Letter> textLetters = LetterUtils.lettersForText(text, letters, defaultLetter);
			Rectangle textBounds = LetterUtils.letterListBounds(textLetters, 0);
			float textPixelWidth = textBounds.getWidth() * SCALE_FACTOR;

			float textPosX = actorPosXOnVisibleMapInclMovementInclScale + (cam.getZoomFactor() * tileLength - textPixelWidth) / 2;

			float overheadMessageStartingY = actorPosYOnVisibleMapInclMovementInclScale - 50;
			float overheadMessageCurrentY = overheadMessageStartingY - 100 * completion;

			OverheadPixelTextParam param = new OverheadPixelTextParam();
			param.text = text;
			param.color = color;
			param.outlineColor = FontType.OVERHEAD_PIXEL_OUTLINE_COLOR;
			param.scaleFactor = SCALE_FACTOR;
			param.x = textPosX;
			param.y = overheadMessageCurrentY;
			param.maxWidth = MAX_WIDTH;

			OverheadPixelTextRenderer.instance().render(gameContainer, graphics, param);

			graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		}

		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
