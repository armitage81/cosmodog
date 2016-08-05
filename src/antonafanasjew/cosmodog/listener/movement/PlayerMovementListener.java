package antonafanasjew.cosmodog.listener.movement;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.collision.WaterValidator;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.listener.pieceinteraction.ComposedPieceInteractionListener;
import antonafanasjew.cosmodog.listener.pieceinteraction.PieceInteractionListener;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.FuelTankInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.NarrativeSequenceUtils;
import antonafanasjew.cosmodog.util.NotificationUtils;

import com.google.common.collect.Lists;

/**
 * Note: this class is not thread-save
 */
public class PlayerMovementListener extends MovementListenerAdapter {

	private static final long serialVersionUID = -1789226092040648128L;

	private List<PieceInteractionListener> pieceInteractionListeners = Lists.newArrayList();
	private ComposedPieceInteractionListener composedPieceInteractionListener = new ComposedPieceInteractionListener(getPieceInteractionListeners());
	
	
	//This is used to compare players values before and after modification.
	private int oldWater = -1;
	
	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Player player = (Player)actor;
		oldWater = player.getWater();
		applyTime(player, x1, y1, x2, y2, applicationContext);
	}
	
	private void applyTime(Player player, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		int timePassed = cosmodog.getTravelTimeCalculator().calculateTravelTime(applicationContext, player, x2, y2);
		PlanetaryCalendar calendar = cosmodogGame.getPlanetaryCalendar();
		calendar.addMinutes(timePassed);
		
	}
	
	@Override
	public void onInteractingWithTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		refillWater(applicationContext);
		refillFuel(applicationContext);
	}
	
	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		checkStarvation(applicationContext);
		checkDehydration(applicationContext);
		checkTemperature(applicationContext);
		checkRadiation(applicationContext);
	}
	
	private void collectCollectibles(ApplicationContext applicationContext) {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		CosmodogMap cosmodogMap = cosmodog.getCosmodogGame().getMap();
		
		Set<Piece> pieces = cosmodogMap.getMapPieces();
		Iterator<Piece> it = pieces.iterator();

		while (it.hasNext()) {
			Piece piece = it.next();
			
			if (piece.getPositionX() == player.getPositionX() && piece.getPositionY() == player.getPositionY()) {
				
				composedPieceInteractionListener.beforeInteraction(piece);
				
				String pieceType;
				
				if (piece instanceof Collectible) {
					
					Collectible collectible = (Collectible) piece;
					
					if (collectible instanceof CollectibleTool) {
						CollectibleTool collectibleTool = (CollectibleTool)collectible;
						pieceType = collectibleTool.getToolType().name();
					} else if (collectible instanceof CollectibleWeapon) {
						pieceType = CollectibleWeapon.class.getSimpleName();
					} else if (collectible instanceof CollectibleAmmo) {
						pieceType = CollectibleAmmo.class.getSimpleName();
					} else {
						CollectibleGoodie collectibleGoodie = (CollectibleGoodie)collectible;
						pieceType = collectibleGoodie.getGoodieType().name();
					} 
					
				} else if (piece instanceof Vehicle) {
					pieceType = Vehicle.class.getSimpleName();
				} else {
					pieceType = null;
				}
				
				PieceInteraction pieceInteraction = cosmodog.getPieceInteractionMap().get(pieceType);
				
				if (pieceInteraction != null) {
					pieceInteraction.interactWithPiece(piece, applicationContext, cosmodogGame, player);
				}
				
				it.remove();
				composedPieceInteractionListener.afterInteraction(piece);
			}
		}
	}


	private void refillWater(ApplicationContext applicationContext) {
		
		CosmodogGame cosmodogGame = applicationContext.getCosmodog().getCosmodogGame();
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_THIRST, new Runnable() {

			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();
				CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
				
				WaterValidator waterValidator = cosmodog.getWaterValidator();
				
				boolean hasWaterAccess = waterValidator.waterInReach(player, tiledMap, player.getPositionX(), player.getPositionY());
				
				if (hasWaterAccess) {
					if (oldWater < player.getCurrentMaxWater()) {
						cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundWater()), true, false);
						applicationContext.getSoundResources().get(SoundResources.SOUND_DRUNK).play();
						OverheadNotificationAction.registerOverheadNotification(player, "You drink the water");
					}
					player.setWater(player.getCurrentMaxWater());
					player.setLifeLentForThirst(0);
				}
			}
			
		});
	}
	

	/*
	 * Take care. Fuel meta data is stored on the 'collectibles' layer, but it is not a 
	 * collectible. It is more a region, like a water place.
	 */
	private void refillFuel(ApplicationContext applicationContext) {
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_FUEL, new Runnable() {

			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();
				CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
				int collectiblesLayerTileId = tiledMap.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_COLLECTIBLES);
				if (TileType.FUEL.getTileId() == collectiblesLayerTileId) {
					if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
						VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
						Vehicle vehicle = vehicleItem.getVehicle();
						vehicle.setFuel(Vehicle.MAX_FUEL);
					} else {
						if (player.getInventory().get(InventoryItemType.FUEL_TANK) == null) {
							player.getInventory().put(InventoryItemType.FUEL_TANK, new FuelTankInventoryItem());
						}
					}
				}
			}
			
		});
		
	}
	
	/*
	 * Player will never really starve. He stays alive with one life point in the worst case.
	 */
	private void checkStarvation(ApplicationContext applicationContext) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_HUNGER, new Runnable() {
			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();				
				if (player.starving()) {
					OverheadNotificationAction.registerOverheadNotification(player, "You are hungry");
					if (player.getActualLife() > 1) {
						player.increaseLifeLentForHunger(1);
					}
				}
			}
		});
		
	}
	
	/*
	 * Player will never really dehydrate. He stays alive with one life point in the worst case.
	 */
	private void checkDehydration(ApplicationContext applicationContext) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_THIRST, new Runnable() {
			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();
				if (player.dehydrating()) {
					OverheadNotificationAction.registerOverheadNotification(player, "You are thirsty");
					if (player.getActualLife() > 1) {
						player.increaseLifeLentForThirst(1);
					}
				}
			}
		});		
	}

	private void checkTemperature(ApplicationContext applicationContext) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_TEMPERATURE, new Runnable() {
			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();
				CustomTiledMap customTiledMap = applicationContext.getCustomTiledMap();
				int tileId = customTiledMap.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_TEMPERATURE);
				
				boolean coldTile = TileType.getByLayerAndTileId(Layers.LAYER_META_TEMPERATURE, tileId) == TileType.META_TEMPERATURE_COLD;
				boolean inCar = player.getInventory().get(InventoryItemType.VEHICLE) != null;
				boolean hasJacket = player.getInventory().get(InventoryItemType.JACKET) != null;
				
				if (coldTile && !inCar && !hasJacket) {
					OverheadNotificationAction.registerOverheadNotification(player, "You freeze");
					if (player.getActualLife() > 1) {
						player.increaseLifeLentForFrost(1);
					}
				} else {
					boolean wasFrozen = player.getLifeLentForFrost() > 0;
					player.setLifeLentForFrost(0);
					if (wasFrozen) {
						OverheadNotificationAction.registerOverheadNotification(player, "You warm up");
					}
				}
			}
		});	
	}
	
	private void checkRadiation(ApplicationContext applicationContext) {
		CustomTiledMap map = applicationContext.getCustomTiledMap();
		Player player = ApplicationContextUtils.getPlayer();
		
		int radiationTileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_RADIATION);
		
		if (TileType.RADIATION.getTileId() == radiationTileId) {
			player.decreaseLife(2);
			OverheadNotificationAction.registerOverheadNotification(player, "Radiation: -2");
		}
		
	}
	
	public List<PieceInteractionListener> getPieceInteractionListeners() {
		return pieceInteractionListeners;
	}

}
