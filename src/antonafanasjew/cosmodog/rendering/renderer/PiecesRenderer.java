package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.rendering.renderer.pieces.*;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.PieceRendererPredicate;
import antonafanasjew.cosmodog.util.PiecesUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
		
		pieceRendererMap.put(CollectibleComposed.class.getSimpleName(), new CollectibleComposedRenderer());
		
		pieceRendererMap.put(CollectibleTool.ToolType.antidote.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.axe.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.binoculars.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.boat.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.dynamite.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.geigerzaehler.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.jacket.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.machete.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.archeologistsJournal.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.weaponFirmwareUpgrade.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.nutrients.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.minedetector.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.pick.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.ski.name(), new ToolRenderer());
		pieceRendererMap.put(CollectibleTool.ToolType.supplytracker.name(), new ToolRenderer());
		
		pieceRendererMap.put("pistol", new WeaponRenderer());
		pieceRendererMap.put("shotgun", new WeaponRenderer());
		pieceRendererMap.put("rifle", new WeaponRenderer());
		pieceRendererMap.put("machinegun", new WeaponRenderer());
		pieceRendererMap.put("rpg", new WeaponRenderer());
		pieceRendererMap.put("CollectibleAmmo_pistol", new AmmoRenderer());
		pieceRendererMap.put("CollectibleAmmo_shotgun", new AmmoRenderer());
		pieceRendererMap.put("CollectibleAmmo_rifle", new AmmoRenderer());
		pieceRendererMap.put("CollectibleAmmo_machinegun", new AmmoRenderer());
		pieceRendererMap.put("CollectibleAmmo_rpg", new AmmoRenderer());

		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blackKeyDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blueKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blueKeyDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.brownKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.cyanKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.darkblueKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.greenKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.greenKeyDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.lilaKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.purpleKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.redKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.redKeyDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.whiteKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.yellowKeycardDoor, new KeyRenderer());
		pieceRendererMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.yellowKeyDoor, new KeyRenderer());
		
		
		
		pieceRendererMap.put(CollectibleLog.class.getSimpleName(), new LogRenderer());
		pieceRendererMap.put(Vehicle.class.getSimpleName(), new VehicleRenderer());
		pieceRendererMap.put(Platform.class.getSimpleName(), new PlatformRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.armor.name(), new ArmorRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.soulessence.name(), new SoulEssenceRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.medipack.name(), new MedipackRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.supplies.name(), new SuppliesRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.insight.name(), new InsightRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.cognition.name(), new CognitionRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.software.name(), new SoftwareRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.chart.name(), new ChartRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.infobit.name(), new InfobitRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.infobyte.name(), new InfobyteRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.infobank.name(), new InfobankRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.bottle.name(), new BottleRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.foodcompartment.name(), new FoodCompartmentRenderer());
		pieceRendererMap.put(CollectibleGoodie.GoodieType.fueltank.name(), new FuelTankRenderer());
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
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

		Position tilePosition = Position.fromCoordinates(tileNoX, tileNoY);

		Collection<Piece> mapPieces = map.visibleMapPieces(tilePosition, tilesW, tilesH, 5).values();
		
		
		
		List<Piece> filteredMapPieces = Lists.newArrayList();
		
		for (Piece piece : mapPieces) {
						
			if (renderingPredicate == null || renderingPredicate.pieceShouldBeRendered(piece)) {
				boolean pieceIsNorthFromPlayer = piece.getPosition().getX() < player.getPosition().getY();
				boolean northernPiecesDrawingPhase = northFromPlayer;
				boolean southernPiecesDrawingPhase = southFromPlayer;
				
				boolean pieceIsPlatform = piece instanceof Platform;
				boolean pieceIsNotPlatform = !pieceIsPlatform;
				
				boolean northernPieceForNorthernDrawingPhase = northernPiecesDrawingPhase && pieceIsNorthFromPlayer;
				boolean platformForNorthernDrawingPhase = northernPiecesDrawingPhase && pieceIsPlatform;
				
				if (northernPieceForNorthernDrawingPhase || platformForNorthernDrawingPhase || (!pieceIsNorthFromPlayer && pieceIsNotPlatform && southernPiecesDrawingPhase)) {
					filteredMapPieces.add(piece);
				}
			}
		}
		
		//Sort the remaining pieces so that northern pieces come before the southern ones. This allows proper rendering (so, for instance, the platform does not cover a vehicle which is in front of it.)
		filteredMapPieces = filteredMapPieces.stream().sorted((p1, p2) -> (int)(p1.getPosition().getY() - p2.getPosition().getY())).collect(Collectors.toList());
		
		for (Piece piece : filteredMapPieces) {
			
			Piece element = null;
			String elementType = null;
			
			if (piece instanceof CollectibleComposed) {
				CollectibleComposed cc = (CollectibleComposed)piece;
				List<Collectible> elements = cc.getElements();
				int numberOfElementToRender = (int)((System.currentTimeMillis() / 1000) % elements.size());
				element = elements.get(numberOfElementToRender);
				elementType = PiecesUtils.pieceType(element);
			} else {
				element = piece;
				elementType = PiecesUtils.pieceType(piece);
			}
			
			PieceRenderer pieceRenderer = pieceRendererMap.get(elementType);
			if (pieceRenderer != null) {
				Enemy enemyOnTile = map.enemyAtTile(piece.getPosition());
				boolean enemyIsOnTile = enemyOnTile != null;
				boolean playerIsOnTile = player.getPosition().equals(piece.getPosition());
				boolean shouldRender = (piece instanceof Vehicle) || (piece instanceof Platform) || (!enemyIsOnTile && !playerIsOnTile);
				if (shouldRender) {

					if (!piece.interactive(piece, applicationContext, cosmodogGame, player)) {

					}
					pieceRenderer.renderPiece(applicationContext, tileWidth, tileHeight, tileNoX, tileNoY, element);
				}
			}
			
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
