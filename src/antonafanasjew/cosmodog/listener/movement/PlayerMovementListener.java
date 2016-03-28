package antonafanasjew.cosmodog.listener.movement;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.collision.WaterValidator;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.listener.pieceinteraction.ComposedPieceInteractionListener;
import antonafanasjew.cosmodog.listener.pieceinteraction.PieceInteractionListener;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleItem;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Effect;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.FuelTankInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.notifications.Notification;
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
	private int oldThirst = -1;
	
	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Player player = (Player)actor;
		oldThirst = player.getThirst();
		applyTime(player, x1, y1, x2, y2, applicationContext);
	}
	
	@Override
	public void onInteractingWithTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		collectWater(applicationContext);
		collectFuel(applicationContext);
	}
	
	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		
		checkStarvation(applicationContext);
		checkDehydration(applicationContext);
	}
	
	private void applyTime(Player player, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		int timePassed = cosmodog.getTravelTimeCalculator().calculateTravelTime(applicationContext, player, x2, y2);
		PlanetaryCalendar calendar = cosmodogGame.getPlanetaryCalendar();
		calendar.addMinutes(timePassed);
		
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
				
				if (piece instanceof Collectible) {
					Collectible collectible = (Collectible) piece;
					it.remove();
					if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_INFOBIT)) {
						Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
							@Override
							public void run() {
								cosmodogGame.getPlayerStatusNotificationList().add(new Notification("+" + Constants.SCORE_PER_INFOBIT, 500));
								player.getGameProgress().addToScore(Constants.SCORE_PER_INFOBIT);
								player.getGameProgress().addInfobit();
								applicationContext.getSoundResources().get(SoundResources.SOUND_COLLECTED).play();
							}
						});
					} else if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_INSIGHT)) {
						
						InventoryItem item = player.getInventory().get(InventoryItem.INVENTORY_ITEM_INSIGHT);
						
						if (item != null) {
							InsightInventoryItem cosmodogCollectionItem = (InsightInventoryItem)item;
							cosmodogCollectionItem.increaseNumber();
						} else {
							InsightInventoryItem cosmodogCollectionItem = new InsightInventoryItem();
							cosmodogCollectionItem.setNumber(1);
							player.getInventory().put(InventoryItem.INVENTORY_ITEM_INSIGHT, cosmodogCollectionItem);
						}
						
						//Now remove the electricity effect from the monolyth.
						int insightPosX = collectible.getPositionX();
						int insightPosY = collectible.getPositionY();
						
						int effectPosX = insightPosX;
						int effectPosY = insightPosY - 1;
						
						Set<Piece> effectPieces = cosmodogMap.getEffectPieces();
						Iterator<Piece> effectPiecesIt = effectPieces.iterator();
						while (effectPiecesIt.hasNext()) {
							Piece effectPiece = effectPiecesIt.next();
							if (effectPiece.getPositionX() == effectPosX && effectPiece.getPositionY() == effectPosY) {
								if (effectPiece instanceof Effect) {
									Effect effect = (Effect)effectPiece;
									if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ELECTRICITY)) {
										effectPiecesIt.remove();
										break;
									}
								}
								
							}
						}
						
						
						
					} else if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_SUPPLIES)) {
						Features.getInstance().featureBoundProcedure(Features.FEATURE_HUNGER, new Runnable() {
							@Override
							public void run() {
								cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundSupply()), true, false);
								applicationContext.getSoundResources().get(SoundResources.SOUND_EATEN).play();
								player.setHunger(0);
							}
						});
					} else if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_SOULESSENCE)) {
						Features.getInstance().featureBoundProcedure(Features.FEATURE_SOULESSENCE, new Runnable() {
							@Override
							public void run() {
								player.increaseMaxLife(Player.MAX_LIFE_INCREASE, true);
							}
						});
					} else if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_ARMOR)) {
						Set<Piece> mapPieces = cosmodogMap.getMapPieces();
						
						for (Piece piece2 : mapPieces) {
							if (piece2 instanceof Vehicle) {
								Vehicle vehicle = ((Vehicle)piece2);
								vehicle.increaseMaxLife(5, true);
							}
						}
						
						if (player.getInventory().hasVehicle()) {
							Vehicle vehicle = ((VehicleInventoryItem)(player.getInventory().get(InventoryItem.INVENTORY_ITEM_VEHICLE))).getVehicle();
							vehicle.increaseMaxLife(5, true);
						}
						
					} else if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_MEDIPACK)) {
						cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundMedipack()), true, false);
						applicationContext.getSoundResources().get(SoundResources.SOUND_EATEN).play();
						player.setLife(player.getMaxLife());
					} else if (collectible.getCollectibleType().equals(Collectible.COLLECTIBLE_TYPE_ITEM)) {
						CollectibleItem collectibleItem = (CollectibleItem)collectible;
						collectItem(collectibleItem, applicationContext);
					}
					
				} else if (piece instanceof Vehicle) {

					composedPieceInteractionListener.beforeInteraction(piece);
					
					VehicleInventoryItem vehicleInventoryItem = new VehicleInventoryItem((Vehicle)piece);
					
					if (player.getInventory().get(InventoryItem.INVENTORY_ITEM_FUEL_TANK) != null) {
						vehicleInventoryItem.getVehicle().setFuel(Vehicle.MAX_FUEL);
						player.getInventory().remove(InventoryItem.INVENTORY_ITEM_FUEL_TANK);
					}

					if (vehicleInventoryItem.getVehicle().getFuel() > 0) {
						cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundVehicle()), true, false);
					} else {
						cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundVehicleWithoutFuel()), true, false);
					}
					
					player.getInventory().put(InventoryItem.INVENTORY_ITEM_VEHICLE, vehicleInventoryItem);
					it.remove();
					
					composedPieceInteractionListener.afterInteraction(piece);
				}
				
				composedPieceInteractionListener.afterInteraction(piece);
			}
		}
	}


	private void collectItem(CollectibleItem collectibleItem, ApplicationContext applicationContext) {
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		CosmodogMap cosmodogMap = cosmodog.getCosmodogGame().getMap();
		
		if (collectibleItem.getItemType().equals(CollectibleItem.ITEM_TYPE_BOAT)) {
			cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundBoat()), true, false);
			player.getInventory().put(InventoryItem.INVENTORY_ITEM_BOAT, new BoatInventoryItem());
			//PiecesUtils.removeAllCollectibleItems(CollectibleItem.ITEM_TYPE_BOAT, cosmodogMap);
		}
		
		if (collectibleItem.getItemType().equals(CollectibleItem.ITEM_TYPE_DYNAMITE)) {
			cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundDynamite()), true, false);
			player.getInventory().put(InventoryItem.INVENTORY_ITEM_DYNAMITE, new DynamiteInventoryItem());
			//PiecesUtils.removeAllCollectibleItems(CollectibleItem.ITEM_TYPE_DYNAMITE, cosmodogMap);
		}
	}

	private void collectWater(ApplicationContext applicationContext) {
		
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
					if (oldThirst > 0) {
						cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundWater()), true, false);
						applicationContext.getSoundResources().get(SoundResources.SOUND_DRUNK).play();
					}
					player.setThirst(0);
				}
			}
			
		});
	}
	

	/*
	 * Take care. Fuel meta data is stored on the 'collectibles' layer, but it is not a 
	 * collectible. It is more a region, like a water place.
	 */
	private void collectFuel(ApplicationContext applicationContext) {
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_FUEL, new Runnable() {

			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();
				CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
				int collectiblesLayerTileId = tiledMap.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_COLLECTIBLES);
				if (collectiblesLayerTileId == Tiles.FUEL_TILE_ID) {
					if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
						VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItem.INVENTORY_ITEM_VEHICLE);
						Vehicle vehicle = vehicleItem.getVehicle();
						vehicle.setFuel(Vehicle.MAX_FUEL);
					} else {
						if (player.getInventory().get(InventoryItem.INVENTORY_ITEM_FUEL_TANK) == null) {
							player.getInventory().put(InventoryItem.INVENTORY_ITEM_FUEL_TANK, new FuelTankInventoryItem());
						}
					}
				}
			}
			
		});
		
	}
	
	private void checkStarvation(ApplicationContext applicationContext) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_HUNGER, new Runnable() {
			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();				
				if (player.starving()) {
					player.decreaseLife(1);
					if (player.dead()) {
    					cosmodog.getGameLifeCycle().setStartNewGame(true);
    					CosmodogStarter.instance.enterState(CosmodogStarter.GAME_OVER_STATE_ID, new FadeOutTransition(), new FadeInTransition());
					}
				}
			}
		});
		
	}
	
	private void checkDehydration(ApplicationContext applicationContext) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_THIRST, new Runnable() {
			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				Player player = cosmodog.getCosmodogGame().getPlayer();
				if (player.dehydrating()) {
					
					player.decreaseLife(1);
					if (player.dead()) {
    					cosmodog.getGameLifeCycle().setStartNewGame(true);
    					CosmodogStarter.instance.enterState(CosmodogStarter.GAME_OVER_STATE_ID, new FadeOutTransition(), new FadeInTransition());
					}
				}
			}
		});		
	}

	public List<PieceInteractionListener> getPieceInteractionListeners() {
		return pieceInteractionListeners;
	}

}
