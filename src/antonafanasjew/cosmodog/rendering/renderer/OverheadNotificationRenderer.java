package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction.OverheadNotificationTransition;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;

public class OverheadNotificationRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		//The whole screen
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();

		
		OverheadNotificationAction action = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
		
		if (action == null) {
			return;
		}
		
		//Holds the information of renderable texts, their location and over which heads they should appear.
		OverheadNotificationTransition transition = action.getTransition();
		
		if (transition == null) {
			return;
		}
		
		
		for (int i = 0; i < transition.texts.size(); i++) {

			String text = transition.texts.get(i);
			
			float completion = transition.completions.get(i);
			Actor actor = transition.actors.get(i);
			
			Cam cam = cosmodogGame.getCam();
			
			//Tile size in pixels, probably 16 for both width and height.
			int tileWidth = map.getTileWidth();
			int tileHeight = map.getTileHeight();
	
			//Tile size in pixels considering zooming.
			int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
			int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());
	
			//Camera position in pixel.
			int camX = (int) cam.viewCopy().x();
			int camY = (int) cam.viewCopy().y();
	
			//Camera offset which is 0 if the camera location matches tile sizes.
			//If the tile width is 16 and camera starts horizontally at 35, then the offset is -3.
			int x = -(int) ((camX % scaledTileWidth));
			int y = -(int) ((camY % scaledTileHeight));
	
			//Camera position in tiles.
			int tileNoX = camX / scaledTileWidth;
			int tileNoY = camY / scaledTileHeight;
	
			//Actors move smoothly between tiles. This offset represents this.
			float movementOffsetX = 0;
			float movementOffsetY = 0;
			
			//The information about the actor's movement is taken from its registered transition, if any.
			ActorTransition actorTransition = cosmodogGame.getActorTransitionRegistry().get(actor);
			
			if (actorTransition != null) {
				movementOffsetX = tileWidth * actorTransition.getTransitionalOffsetX();
				movementOffsetY = tileHeight * actorTransition.getTransitionalOffsetY();
			}
			
			//Going to the beginning of the tile where camera top left corner is located.
			//The coordinates could be negative.
			graphics.translate(x, y);
			
			//Actor's X position in tiles related to the camera's left corner.
			int actorTileNoXOnVisibleMap = actor.getPositionX() - tileNoX;
			
			//Actor's X position in pixels corresponding to the tile position calculated above.
			int actorPosXOnVisibleMap = actorTileNoXOnVisibleMap * tileWidth;
			
			//X Position in pixels considering the movement of the actor.
			float actorPosXOnVisibleMapInclMovement = actorPosXOnVisibleMap + movementOffsetX;
			
			//X Position in pixels considering the scale factor.
			float actorPosXOnVisibleMapInclMovementInclScale = actorPosXOnVisibleMapInclMovement * cam.getZoomFactor();
			
			//The same for Y position.
			int actorTileNoYOnVisibleMap = actor.getPositionY() - tileNoY;
			int actorPosYOnVisibleMap = actorTileNoYOnVisibleMap * tileHeight;
			float actorPosYOnVisibleMapInclMovement = actorPosYOnVisibleMap + movementOffsetY;
			float actorPosYOnVisibleMapInclMovementInclScale = actorPosYOnVisibleMapInclMovement * cam.getZoomFactor();
			
			float textWidth = 500;
			
			//If the scaled tile is 32 pixels wide and the text line is 50 pixels wide, then the x offset is (32 - 50) / 2 = -9
			float textOffsetX = (scaledTileWidth - textWidth) / 2;
			
			//Actor's movement offset is added to keep the text centered over their head.
			float textPosX = actorPosXOnVisibleMapInclMovementInclScale + textOffsetX;
			
			//The starting Y point of an overhead message is just constant number of pixels over the actor.
			float overheadMessageStartingY = actorPosYOnVisibleMapInclMovementInclScale - 50;
			
			//The current Y point is in the interval [-50;-150] pixels over the player, depending on completion.
			float overheadMessageCurrentY = overheadMessageStartingY - 100 * completion;
			
			//Drawing context for the text.
			DrawingContext dc = new SimpleDrawingContext(sceneDrawingContext, textPosX, overheadMessageCurrentY, textWidth, 50);
			
			//Drawing the text line.
			FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOverhead();
			Book textBook = TextPageConstraints.fromDc(dc).textToBook(text, fontRefToFontTypeMap);
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook, true);
			
			//Returning back to the camera's left top corner.
			graphics.translate(-x, -y);
		}
		
		//Returning back to the left top corner of the screen.
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
