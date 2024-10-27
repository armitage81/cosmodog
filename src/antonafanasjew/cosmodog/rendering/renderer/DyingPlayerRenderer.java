package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.dying.DyingAction;
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

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		
		DrawingContext bottomContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		
		
		Player player = cosmodogGame.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		
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
		
		int scaledTileWidth = (int) (tileLength * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileLength * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;

		String animationKey = "playerDying";
		Animation playerAnimation = applicationContext.getAnimations().get(animationKey);
		
		int animationFrameIndex = dyingAction.animationFrameIndex();

		playerAnimation.setCurrentFrame(animationFrameIndex);
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		playerAnimation.draw((player.getPosition().getX() - tileNoX) * tileLength, (player.getPosition().getY() - tileNoY) * tileLength);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.LoadingOrGameOverOrTheEnd);
		Book dyingHintBook = TextPageConstraints.fromDc(bottomContainerDrawingContext).textToBook(dyingAction.getDyingHint(), fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, dyingHintBook);
		
		
	}

	
}
