package antonafanasjew.cosmodog.listener.movement;

import java.util.Collection;
import java.util.Iterator;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction;
import antonafanasjew.cosmodog.actions.cutscenes.RadiationDamageAction;
import antonafanasjew.cosmodog.actions.cutscenes.ShockDamageAction;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.collision.WaterValidator;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.globals.Objects;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.LetterPlate;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.inventory.FuelTankInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.MineDetectorInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

/**
 * Note: this class is not thread-save
 */
public class PlayerMovementListener extends MovementListenerAdapter {

	private static final int TURNS_BEFORE_DEATH_BY_POISON = 3;

	private static final long serialVersionUID = -1789226092040648128L;

	
	//This is used to compare players values before and after modification.
	private int oldWater = -1;
	private boolean oldDehydrating = false;
	private boolean oldStarving = false;
	private int oldTurnsWormAlerted = -1;
	
	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Player player = (Player)actor;
		oldWater = player.getWater();
		oldDehydrating = player.dehydrating();
		oldStarving = player.starving();
		oldTurnsWormAlerted = player.getTurnsWormAlerted();
		addTime();
		updateWormAlert(player, applicationContext );
		updatePoisonCount(player, applicationContext);
		updateWater(x1, y1, x2, y2);
		updateFood(x1, y1, x2, y2);
		updateFuel(x1, y1, x2, y2);
		
	}

	private void updateFuel(int x1, int y1, int x2, int y2) {
		
		ApplicationContext appCx = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Player player = ApplicationContextUtils.getPlayer();
		if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
			VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = vehicleInventoryItem.getVehicle();
			int fuelCosts = cosmodog.getFuelConsumer().turnCosts(x1, y1, x2, y2, player, map, appCx);
			vehicle.decreaseFuel(fuelCosts);
		}
	}
	
	private void updateWater(int x1, int y1, int x2, int y2) {
		
		ApplicationContext appCx = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Player player = ApplicationContextUtils.getPlayer();
		int waterCosts = cosmodog.getWaterConsumer().turnCosts(x1, y1, x2, y2, player, map, appCx);
		player.decreaseWater(waterCosts);
	}
	
	private void updateFood(int x1, int y1, int x2, int y2) {
		
		ApplicationContext appCx = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Player player = ApplicationContextUtils.getPlayer();
		int foodCosts = cosmodog.getFoodConsumer().turnCosts(x1, y1, x2, y2, player, map, appCx);
		player.decreaseFood(foodCosts);
	}
	
	@Override
	public void beforeWaiting(Actor actor, ApplicationContext applicationContext) {
		Player player = (Player)actor;
		oldTurnsWormAlerted = player.getTurnsWormAlerted();
		addTime();
		updateWormAlert(player, applicationContext );
		updatePoisonCount(player, applicationContext);
		updateWater(actor.getPositionX(), actor.getPositionY(), actor.getPositionX(), actor.getPositionY());
		updateFood(actor.getPositionX(), actor.getPositionY(), actor.getPositionX(), actor.getPositionY());
		updateFuel(actor.getPositionX(), actor.getPositionY(), actor.getPositionX(), actor.getPositionY());
	}
	
	
	@Override
	public void onInteractingWithTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		refillWater(applicationContext);
		refillFuel(applicationContext);
		detectMines(applicationContext);
		changeLettersOnLetterPlates(applicationContext);
		activatePressureButton(applicationContext);
		checkDecontamination(applicationContext);
		checkContamination(applicationContext);
	}
	
	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		checkStarvation(applicationContext);
		checkDehydration(applicationContext);
		checkTemperature(applicationContext);
		checkRadiation(applicationContext);
		checkElectricity(applicationContext);
		checkWorm(applicationContext);
		checkMine(applicationContext);
		checkContaminationStatus(applicationContext);
		ApplicationContextUtils.getGameProgress().incTurn();
	}
	
	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		refillWater(applicationContext);
		refillFuel(applicationContext);
		detectMines(applicationContext);
		changeLettersOnLetterPlates(applicationContext);
		checkStarvation(applicationContext);
		checkDehydration(applicationContext);
		checkTemperature(applicationContext);
		checkRadiation(applicationContext);
		checkElectricity(applicationContext);
		checkWorm(applicationContext);
		checkMine(applicationContext);
		checkContaminationStatus(applicationContext);
		ApplicationContextUtils.getGameProgress().incTurn();
	}
	
	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		refillWater(applicationContext);
		refillFuel(applicationContext);
		detectMines(applicationContext);
		changeLettersOnLetterPlates(applicationContext);
	}

	private void collectCollectibles(ApplicationContext applicationContext) {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		CosmodogMap cosmodogMap = cosmodog.getCosmodogGame().getMap();
		
		Collection<Piece> pieces = cosmodogMap.getMapPieces().values();
		Iterator<Piece> it = pieces.iterator();

		while (it.hasNext()) {
			Piece piece = it.next();
			
			if (piece.getPositionX() == player.getPositionX() && piece.getPositionY() == player.getPositionY()) {
				
				String pieceType = PiecesUtils.pieceType(piece);
				
				PieceInteraction pieceInteraction = cosmodog.getPieceInteractionMap().get(pieceType);
				
				if (pieceInteraction != null) {
					
					if (piece.interactive(piece, applicationContext, cosmodogGame, player)) {
						pieceInteraction.beforeInteraction(piece, applicationContext, cosmodogGame, player);
						pieceInteraction.interactWithPiece(piece, applicationContext, cosmodogGame, player);
						pieceInteraction.afterInteraction(piece, applicationContext, cosmodogGame, player);
						it.remove();
					}
					
				}
				
			}
		}
	}


	private void refillWater(ApplicationContext applicationContext) {
		
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		
		WaterValidator waterValidator = cosmodog.getWaterValidator();
		
		boolean hasWaterAccess = waterValidator.waterInReach(player, map, player.getPositionX(), player.getPositionY());
		
		if (hasWaterAccess) {
			if (oldWater < player.getCurrentMaxWater()) {
				applicationContext.getSoundResources().get(SoundResources.SOUND_DRUNK).play();
				OverheadNotificationAction.registerOverheadNotification(player, "You drink the water");
			}
			player.setWater(player.getCurrentMaxWater());
			player.setLifeLentForThirst(0);
		}
	}
	

	/*
	 * Take care. Fuel meta data is stored on the 'collectibles' layer, but it is not a 
	 * collectible. It is more a region, like a water place.
	 */
	private void refillFuel(ApplicationContext applicationContext) {
		
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = cosmodog.getCosmodogGame().getPlayer();

		int collectiblesLayerTileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_COLLECTIBLES);
		if (TileType.FUEL.getTileId() == collectiblesLayerTileId) {
			String notificationText = null;
			if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
				VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
				Vehicle vehicle = vehicleItem.getVehicle();
				if (vehicle.getFuel() < Vehicle.MAX_FUEL) {
					vehicle.setFuel(Vehicle.MAX_FUEL);
					notificationText = "You refueled the car.";
				}
			} else {
				if (player.getInventory().get(InventoryItemType.FUEL_TANK) == null) {
					player.getInventory().put(InventoryItemType.FUEL_TANK, new FuelTankInventoryItem());
					notificationText = "You took a fuel canister.";
				}
			}
			
			if (notificationText != null) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_COLLECTED).play();
				ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
				actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new PopUpNotificationAction(notificationText));
			}
			
		}
		
	}
	
	private void changeLettersOnLetterPlates(ApplicationContext applicationContext) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Collection<DynamicPiece> letterPlates = map.getDynamicPieces().get(LetterPlate.class);
		
		for (DynamicPiece piece : letterPlates) {
			LetterPlate letterPlate = (LetterPlate)piece;

			if (letterPlate.getPositionX() == player.getPositionX() && letterPlate.getPositionY() == player.getPositionY()) {
				continue;
			}
			
			if (letterPlate.isActive() == false) {
				continue;
			}
			
			letterPlate.interactWhenApproaching();
		}
	}
	
	private void detectMines(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		MineDetectorInventoryItem mineDetector = (MineDetectorInventoryItem)player.getInventory().get(InventoryItemType.MINEDETECTOR);
		
		Collection<DynamicPiece> mines = map.getDynamicPieces().get(Mine.class);
		
		for (DynamicPiece piece : mines) {
			Mine mine =(Mine)piece;
			if (mine.getState() == Mine.STATE_DEACTIVATED) {
				continue;
			}
			
			if (mine.getState() == Mine.STATE_DESTROYED) {
				continue;
			}
			
			boolean mineIsVisible = mineDetector == null ? false : PiecesUtils.distanceBetweenPieces(player, mine) < MineDetectorInventoryItem.DETECTION_DISTANCE;
			
			mine.setState(mineIsVisible ? Mine.STATE_VISIBLE : Mine.STATE_INVISIBLE);
		}
		
	}
	
	private void activatePressureButton(ApplicationContext applicationContext) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Collection<DynamicPiece> buttons = map.getDynamicPieces().get(PressureButton.class);
		
		for (DynamicPiece piece : buttons) {
			PressureButton button = (PressureButton)piece;
			
			if (button.getPositionX() == player.getPositionX() && button.getPositionY() == player.getPositionY()) {
				if (button.getState() == PressureButton.STATE_DEACTIVATED) {
					button.setState(PressureButton.STATE_ACTIVATED);
				}
				break;
			}
			
		}
	}
	
	/**
	 * Check whether the player should be contaminated. It means only that each next turn, the player will move closer to death unless decontaminated.
	 */
	private void checkContamination(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		DynamicPiece piece = map.dynamicPieceAtPosition(player.getPositionX(), player.getPositionY());
		if (piece == null || !(piece instanceof Poison)) {
			return;
		}
		
		Poison poison = (Poison)piece;
		
		if (poison.isDeactivated()) {
			return;
		}
		
		if (player.getInventory().hasVehicle()) {
			return;
		}
		
		if (player.getInventory().hasItem(InventoryItemType.ANTIDOTE)) {
			return;
		}
		
		if (!player.isPoisoned()) {
			String text = "<font:critical> Contaminated!";
			OverheadNotificationAction.registerOverheadNotification(player, text);
			text = "<font:critical> Death by poison in " + TURNS_BEFORE_DEATH_BY_POISON + " turns !!!";
			OverheadNotificationAction.registerOverheadNotification(player, text);
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_POISONED).play();
		}
		
		player.contaminate();
	
	}
	
	/**
	 * Removes the contamination flag if the player stays on the decontamination stop.
	 */
	private void checkDecontamination(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		boolean onDecontaminationSpot = TileType.DECONTAMINATION_SPOT.getTileId() == map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_GEAR);
		if (onDecontaminationSpot) {
			
			if (player.isPoisoned()) {
				String text = "Decontaminated.";
				OverheadNotificationAction.registerOverheadNotification(player, text);
			}
			
			player.decontaminate();
			
		}
	
	}
	
	/*
	 * Player will never really starve. He stays alive with one life point in the worst case.
	 */
	private void checkStarvation(ApplicationContext applicationContext) {
		Cosmodog cosmodog = applicationContext.getCosmodog();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		if (player.starving()) {
			if (!oldStarving) {
				OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You are hungry");
			}
			if (player.getActualLife() > 1) {
				player.increaseLifeLentForHunger(1);
			}
		}
	}
	
	/*
	 * Player will never really dehydrate. He stays alive with one life point in the worst case.
	 */
	private void checkDehydration(ApplicationContext applicationContext) {
		Cosmodog cosmodog = applicationContext.getCosmodog();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		if (player.dehydrating()) {
			if (!oldDehydrating) {
				OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You are thirsty");
			}
			if (player.getActualLife() > 1) {
				player.increaseLifeLentForThirst(1);
			}
		}
	}

	private void checkTemperature(ApplicationContext applicationContext) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_TEMPERATURE, new Runnable() {
			@Override
			public void run() {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
				
				Player player = cosmodog.getCosmodogGame().getPlayer();
				int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_TEMPERATURE);
				
				boolean coldTile = TileType.getByLayerAndTileId(Layers.LAYER_META_TEMPERATURE, tileId) == TileType.META_TEMPERATURE_COLD;
				boolean inCar = player.getInventory().get(InventoryItemType.VEHICLE) != null;
				boolean inPlatform = player.getInventory().get(InventoryItemType.PLATFORM) != null;
				boolean hasJacket = player.getInventory().get(InventoryItemType.JACKET) != null;
				
				boolean wasFrozen = player.getLifeLentForFrost() > 0;

				if (coldTile && !inCar && !inPlatform && !hasJacket) {
					if (player.getActualLife() > 1) {
						player.increaseLifeLentForFrost(1);
					}
					if (!wasFrozen) {
						OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You freeze");
					}
				} else {
					player.setLifeLentForFrost(0);
					if (wasFrozen) {
						OverheadNotificationAction.registerOverheadNotification(player, "You warm up");
					}
				}
			}
		});	
	}
	
	private void checkRadiation(ApplicationContext applicationContext) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = ApplicationContextUtils.getPlayer();
		
		int radiationTileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_RADIATION);
		
		if (TileType.RADIATION.getTileId() == radiationTileId) {
			if (player.getInventory().get(InventoryItemType.RADIOACTIVESUIT) == null) {
				if (Features.getInstance().featureOn(Features.FEATURE_DAMAGE)) {
					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.RADIATION_DAMAGE, new RadiationDamageAction(500));
				}
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Radiation: Suppressed by suit");
			}
		}
		
	}
	
	private void checkElectricity(ApplicationContext applicationContext) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = ApplicationContextUtils.getPlayer();
		
		int electricityTileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_RADIATION);
		
		if (TileType.ELECTRICITY.getTileId() == electricityTileId) {
			if (Features.getInstance().featureOn(Features.FEATURE_DAMAGE)) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.SHOCK_DAMAGE, new ShockDamageAction(500));
			}
		}
		
	}
	
	private void addTime() {
		PlanetaryCalendar calendar = ApplicationContextUtils.getCosmodogGame().getPlanetaryCalendar();
		calendar.addMinutes(Constants.MINUTES_PER_TURN);
	}

	private void updateWormAlert(Player player, ApplicationContext applicationContext) {

		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		TiledObjectGroup wormsObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_WORMS);
		TiledObject wormsSouthEastObject = wormsObjectGroup.getObjects().get(Objects.OBJECT_WORMS_SOUTH_EAST);
		boolean inWormRegion = RegionUtils.pieceInRegion(player, wormsSouthEastObject, map.getTileWidth(), map.getTileHeight());
		boolean inSnow = isPlayerOnGroundTypeTile(TileType.GROUND_TYPE_SNOW, map, player);
		boolean onPlatform = CosmodogMapUtils.isTileOnPlatform(player.getPositionX(), player.getPositionY());
		boolean inPlatform = player.getInventory().hasPlatform();
		boolean wormActive = player.getGameProgress().isWormActive();
		boolean wormAlerted = wormActive && inWormRegion && inSnow && !onPlatform && !inPlatform;
		if (wormAlerted) {
			player.increaseTurnsWormAlerted();
		} else {
			player.resetTurnsWormAlerted();
		}
		
		
	}
	
	private void updatePoisonCount(Player player, ApplicationContext applicationContext) {
		if (player.isPoisoned()) {
			player.increaseTurnsPoisoned();
		}
	}
	
	private void checkWorm(ApplicationContext applicationContext) {
		
		Player player = ApplicationContextUtils.getPlayer();
		
		boolean wormActive = player.getGameProgress().isWormActive();
		
		if (!wormActive) {
			return;
		}
		
		int turnsWormAlerted = player.getTurnsWormAlerted();
		if (oldTurnsWormAlerted != turnsWormAlerted) {
			
			if (turnsWormAlerted == 0) {
				String wormAlertText = "The worm departs.";
				OverheadNotificationAction.registerOverheadNotification(player, wormAlertText);
			} else {
			
				int turnsUntilWormAttack = player.getGameProgress().getTurnsTillWormAppears() - turnsWormAlerted;
				
				if (turnsUntilWormAttack > 0) {
					String wormAlertText = "<font:critical> WORM ATTACK IN " + turnsUntilWormAttack + " TURNS !!!";
					OverheadNotificationAction.registerOverheadNotification(player, wormAlertText);
				} else {
					CosmodogGame cosmodogGame = applicationContext.getCosmodog().getCosmodogGame();
					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.WORM_ATTACK, new WormAttackAction(5000));
				}
			
			}
		}
	}

	private void checkContaminationStatus(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		int turnsPoisoned = player.getTurnsPoisoned();
		if (turnsPoisoned != 0) {
			
			int turnsTillDeath = TURNS_BEFORE_DEATH_BY_POISON - turnsPoisoned;
			if (turnsTillDeath > 0) {
				String text = "<font:critical> DEATH BY POISON IN " + turnsTillDeath + " TURNS !!!";
				OverheadNotificationAction.registerOverheadNotification(player, text);
			} else {
				ApplicationContextUtils.getPlayer().setLife(0);
			}
			
		}
	}
	
	private void checkMine(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Collection<DynamicPiece> mines = map.getDynamicPieces().get(Mine.class);
		
		Mine mineUnderPlayer = null;
		
		for (DynamicPiece piece : mines) {
			Mine mine = (Mine)piece;
			if (mine.getPositionX() == player.getPositionX() && mine.getPositionY() == player.getPositionY()) {
				mineUnderPlayer = mine;
				break;
			}
		}
		
		if (mineUnderPlayer != null) {
			short mineState = mineUnderPlayer.getState();
			if (mineState != Mine.STATE_DEACTIVATED && mineState != Mine.STATE_DESTROYED) {
				mineUnderPlayer.setState(Mine.STATE_DESTROYED);
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new MineExplosionAction(500));
			}
		}
		
	}
	
	private boolean isPlayerOnGroundTypeTile(TileType tileType, CosmodogMap map, Player player) {
		boolean retVal = false;
		int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
		retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(tileType);
		
		return retVal;
	}
}
