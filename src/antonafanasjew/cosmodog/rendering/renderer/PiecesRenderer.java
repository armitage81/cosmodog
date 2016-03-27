package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PiecesRenderer extends AbstractRenderer {

	private Map<DirectionType, String> vehicleDirection2animationKey = Maps.newHashMap();

	/*
	 * This flags define which pieces should be rendered. 
	 * It is split in north and south to allow correct covering
	 * The player sprite will cover the northern pieces when walking north
	 * and will be covered by pieces if walking in them in south direction 
	 */
	private boolean northFromPlayer;
	private boolean southFromPlayer;
	
	public PiecesRenderer(boolean northFromPlayer, boolean southFromPlayer) {
		
		this.northFromPlayer = northFromPlayer;
		this.southFromPlayer = southFromPlayer;
		
		vehicleDirection2animationKey.put(DirectionType.RIGHT, "vehicleRight");
		vehicleDirection2animationKey.put(DirectionType.DOWN, "vehicleDown");
		vehicleDirection2animationKey.put(DirectionType.LEFT, "vehicleLeft");
		vehicleDirection2animationKey.put(DirectionType.UP, "vehicleUp");
	}
	
	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
		Cam cam = cosmodogGame.getCam();
		Player player = cosmodogGame.getPlayer();
		
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
		
		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		
		Set<Piece> mapPieces = cosmodogMap.visibleMapPieces(tileNoX, tileNoY, tilesW, tilesH, 2);
		
		Set<Piece> filteredMapPieces = Sets.newHashSet();
		
		for (Piece piece : mapPieces) {
			boolean isNorthFromPlayer = piece.getPositionY() < player.getPositionY();
			if ((isNorthFromPlayer && northFromPlayer) || (!isNorthFromPlayer && southFromPlayer)) {
				filteredMapPieces.add(piece);
			}
		}
		
		for (Piece piece : filteredMapPieces) {
			if (piece instanceof Collectible) {
				Collectible collectible = (Collectible) piece;
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_INFOBIT)) {
					Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
						@Override
						public void run() {
							applicationContext.getAnimations().get("infobit").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
						}
					});
				}
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_INSIGHT)) {
					applicationContext.getAnimations().get("insight").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
				}
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_SUPPLIES)) {
					Features.getInstance().featureBoundProcedure(Features.FEATURE_HUNGER, new Runnable() {
						@Override
						public void run() {
							applicationContext.getAnimations().get("supplies").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
						}
					});
				}
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_MEDIPACK)) {
					applicationContext.getAnimations().get("medipack").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
				}
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_ITEM)) {
					applicationContext.getAnimations().get("collectibleItemTool").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);						
				}
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_SOULESSENCE)) {
					Features.getInstance().featureBoundProcedure(Features.FEATURE_SOULESSENCE, new Runnable() {
						@Override
						public void run() {
							applicationContext.getAnimations().get("soulessence").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
						}
					});
				}
				if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_ARMOR)) {
					applicationContext.getAnimations().get("armor").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
				}
				
			} else if (piece instanceof Vehicle) {
				Vehicle vehicle = (Vehicle)piece;
				DirectionType direction = vehicle.getDirection();
				String animationKey = vehicleDirection2animationKey.get(direction);
				Animation vehicleAnimation = applicationContext.getAnimations().get(animationKey);
				vehicleAnimation.draw((vehicle.getPositionX() - tileNoX) * tileWidth, (vehicle.getPositionY() - tileNoY) * tileHeight);
			}
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
	}

}
