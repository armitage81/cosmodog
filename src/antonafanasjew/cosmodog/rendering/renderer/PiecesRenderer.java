package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.PieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.pieces.AmmoRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.ArmorRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.BottleRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.FoodCompartmentRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.InfobitRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.InsightRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.MedipackRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.PieceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.PlatformRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.SoulEssenceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.SuppliesRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.ToolRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.VehicleRenderer;
import antonafanasjew.cosmodog.rendering.renderer.pieces.WeaponRenderer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PiecesRenderer extends AbstractRenderer {

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
		
	}
	
	private static Map<String, PieceRenderer> pieceRendererMap = Maps.newHashMap();
	
	static {
		pieceRendererMap.put(CollectibleTool.class.getSimpleName(), new ToolRenderer());
		pieceRendererMap.put(CollectibleWeapon.class.getSimpleName(), new WeaponRenderer());
		pieceRendererMap.put(CollectibleAmmo.class.getSimpleName(), new AmmoRenderer());
		pieceRendererMap.put(Vehicle.class.getSimpleName(), new VehicleRenderer());
		pieceRendererMap.put(Platform.class.getSimpleName(), new PlatformRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.armor.name(), new ArmorRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.soulessence.name(), new SoulEssenceRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.medipack.name(), new MedipackRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.supplies.name(), new SuppliesRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.insight.name(), new InsightRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.infobit.name(), new InfobitRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.bottle.name(), new BottleRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.foodcompartment.name(), new FoodCompartmentRenderer());
	}
	
	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		PieceRendererPredicate renderingPredicate = (PieceRendererPredicate)renderingParameter;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.getMap();
		Cam cam = cosmodogGame.getCam();
		Player player = cosmodogGame.getPlayer();
		
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
		
		Set<Piece> mapPieces = map.visibleMapPieces(tileNoX, tileNoY, tilesW, tilesH, 5);
		
		Set<Piece> filteredMapPieces = Sets.newHashSet();
		
		for (Piece piece : mapPieces) {
			if (renderingPredicate == null || renderingPredicate.pieceShouldBeRendered(piece)) {
				boolean isNorthFromPlayer = piece.getPositionY() < player.getPositionY();
				if ((isNorthFromPlayer && northFromPlayer) || (!isNorthFromPlayer && (piece instanceof Platform == false) && southFromPlayer) || (northFromPlayer && piece instanceof Platform)) {
					filteredMapPieces.add(piece);
				}
			}
		}
		
		for (Piece piece : filteredMapPieces) {
			
			String pieceType;
			
			if (piece instanceof Collectible) {

				Collectible collectible = (Collectible) piece;
				Collectible.CollectibleType collectibleType = collectible.getCollectibleType();
				
				if (collectibleType == Collectible.CollectibleType.TOOL) {
					pieceType = CollectibleTool.class.getSimpleName();
				} else if (collectibleType == Collectible.CollectibleType.WEAPON) {
					pieceType = CollectibleWeapon.class.getSimpleName();
				} else if (collectibleType == Collectible.CollectibleType.AMMO) {
					pieceType = CollectibleAmmo.class.getSimpleName();
				}
				else {
					
					CollectibleGoodie goodie = (CollectibleGoodie)collectible;
					
					pieceType = goodie.getGoodieType().name();
				}
				
			} else if (piece instanceof Vehicle) {
				pieceType = Vehicle.class.getSimpleName();
			} else if (piece instanceof Platform) {
				pieceType = Platform.class.getSimpleName();
			} else {
				pieceType = null;
			}
			
			PieceRenderer pieceRenderer = pieceRendererMap.get(pieceType);
			
			if (pieceRenderer != null) {
				pieceRenderer.renderPiece(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, piece);
			}
			
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
	}


}
