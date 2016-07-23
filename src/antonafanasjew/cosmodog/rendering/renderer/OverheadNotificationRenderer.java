package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction.OverheadNotificationTransition;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;

public class OverheadNotificationRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();

		OverheadNotificationAction action = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
		
		if (action == null) {
			return;
		}
		
		OverheadNotificationTransition transition = action.getTransition();
		
		if (transition == null) {
			return;
		}
		
		Actor actor = transition.actor;

		for (int i = 0; i < transition.texts.size(); i++) {

			String text = transition.texts.get(i);
			float completion = transition.completions.get(i);
			
			Cam cam = cosmodogGame.getCam();
			
			int tileWidth = tiledMap.getTileWidth();
			int tileHeight = tiledMap.getTileHeight();
	
			int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
			int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());
	
			int camX = (int) cam.viewCopy().x();
			int camY = (int) cam.viewCopy().y();
	
			int x = -(int) ((camX % scaledTileWidth));
			int y = -(int) ((camY % scaledTileHeight));
	
			int tileNoX = camX / scaledTileWidth;
			int tileNoY = camY / scaledTileHeight;
	
			
			float movementOffsetX = 0;
			float movementOffsetY = 0;
			
			ActorTransition actorTransition = cosmodogGame.getActorTransitionRegistry().get(actor);
			
			if (actorTransition != null) {
				movementOffsetX = tileWidth * actorTransition.getTransitionalOffsetX();
				movementOffsetY = tileHeight * actorTransition.getTransitionalOffsetY();
			}
					
			graphics.translate(x, y); //Just the map  offset
			
			
			//LetterTextRenderer.getInstance().render(gameContainer, g, lifeLabelDrawingContext, );
			
			int actorTileNoXOnVisibleMap = actor.getPositionX() - tileNoX;
			int actorPosXOnVisibleMap = actorTileNoXOnVisibleMap * tileWidth;
			float actorPosXOnVisibleMapInclMovement = actorPosXOnVisibleMap + movementOffsetX;
			float actorPosXOnVisibleMapInclMovementInclScale = actorPosXOnVisibleMapInclMovement * cam.getZoomFactor();
			
			int actorTileNoYOnVisibleMap = actor.getPositionY() - tileNoY;
			int actorPosYOnVisibleMap = actorTileNoYOnVisibleMap * tileHeight;
			float actorPosYOnVisibleMapInclMovement = actorPosYOnVisibleMap + movementOffsetY;
			float actorPosYOnVisibleMapInclMovementInclScale = actorPosYOnVisibleMapInclMovement * cam.getZoomFactor();
			
			Map<Character, Letter> characterLetters = ApplicationContext.instance().getCharacterLetters();
			Letter defaultLetter = characterLetters.get('?');
			List<Letter> textLetters = LetterUtils.lettersForText(text, characterLetters, defaultLetter);
			Rectangle textBounds = LetterUtils.letterListBounds(textLetters, 0);
			
			float textWidth = textBounds.getWidth();
			float textOffsetX = (scaledTileWidth - textWidth) / 2;
			float textPosX = actorPosXOnVisibleMapInclMovementInclScale + textOffsetX;
			if (actorPosXOnVisibleMapInclMovementInclScale + textWidth > drawingContext.w()) {
				//Why is x substracted here? It has to do with thranslation around the drawing method, but what exactly?
				textPosX = drawingContext.w() - textWidth - x;  
			}
			
			float overheadMessageStartingY = actorPosYOnVisibleMapInclMovementInclScale - 25;
			float overheadMessageCurrentY = overheadMessageStartingY - 50 * completion;
			
	//		graphics.setColor(color);
	//		graphics.setFont(Fonts.HOVERTEXT_FONT);
	//		graphics.drawString(text, textPosX, overheadMessageCurrentY);
			
			DrawingContext dc = new SimpleDrawingContext(drawingContext, textPosX, overheadMessageCurrentY, textBounds.getWidth(), textBounds.getHeight());
			
			
			LetterTextRenderer.getInstance().render(gameContainer, graphics, dc, LetterTextRenderingParameter.fromText(text));
			
	
			graphics.translate(-x, -y);
		}
		
	}

}
