package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.topology.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.death.DyingAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class DyingPlayerRenderer extends AbstractRenderer {

	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		
		DrawingContext bottomContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		
		
		Player player = cosmodogGame.getPlayer();

		DyingAction dyingAction = (DyingAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.DYING);
		boolean deathSequence = dyingAction != null;
		
		if (!player.dead()) {
			return;
		}
		
		//Stop ambient sounds
		ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
		
		graphics.setColor(Color.black);
		graphics.fillRect(gameContainerDrawingContext.x(), gameContainerDrawingContext.y(), gameContainerDrawingContext.w(), gameContainerDrawingContext.h());
		
		if (!deathSequence) {
			return;
		}
			
		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		Vector playerVectorRelatedToCamTilePosition = Cam.positionVectorRelatedToCamTilePosition(player.getPosition(), camTilePosition);

		String animationKey = "playerDying";
		Animation playerAnimation = applicationContext.getAnimations().get(animationKey);
		
		int animationFrameIndex = dyingAction.animationFrameIndex();

		playerAnimation.setCurrentFrame(animationFrameIndex);
		
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		playerAnimation.draw(playerVectorRelatedToCamTilePosition.getX(), playerVectorRelatedToCamTilePosition.getY());
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.LoadingOrGameOverOrTheEnd);
		Book dyingHintBook = TextPageConstraints.fromDc(bottomContainerDrawingContext).textToBook(dyingAction.getDyingHint(), fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, dyingHintBook);

	}

	
}
