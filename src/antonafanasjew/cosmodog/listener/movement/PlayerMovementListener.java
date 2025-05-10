package antonafanasjew.cosmodog.listener.movement;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.MineExplosionAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.RadiationDamageAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.ShockDamageAction;
import antonafanasjew.cosmodog.actions.death.WormAttackAction;
import antonafanasjew.cosmodog.actions.mechanism.SwitchingOneWayBollardAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.weather.SnowfallChangeAction;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.LetterPlate;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Emp;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.model.portals.interfaces.PresenceDetector;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.rules.events.GameEventTeleported;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.*;
import antonafanasjew.cosmodog.waterplaces.WaterValidator;
import com.google.common.collect.Lists;

public class PlayerMovementListener implements MovementListener {

	@Serial
	private static final long serialVersionUID = -1789226092040648128L;

	public static final int AUTOSAVE_INTERVAL_IN_TURNS = 20;
	
	//This is used to compare players values before and after modification.
	private int oldWater = -1;
	private boolean oldDehydrating = false;
	private boolean oldStarving = false;
	private int oldTurnsWormAlerted = -1;
	
	@Override
	public void onEnteringTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		Player player = (Player)actor;
		oldWater = player.getWater();
		oldDehydrating = player.dehydrating();
		oldStarving = player.starving();
		oldTurnsWormAlerted = player.getTurnsWormAlerted();
		addTime();
		updateWormAlert(player, applicationContext );
		updatePoisonCount(player, applicationContext);
		updateWater(position1, position2);
		updateFood(position1, position2);
		updateFuel(position1, position2);


		GameEventUtils.throwEvent(new GameEventChangedPosition());
		
	}

	private void updateFuel(Position position1, Position position2) {
		
		ApplicationContext appCx = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Player player = ApplicationContextUtils.getPlayer();
		if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
			VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = vehicleInventoryItem.getVehicle();
			int fuelCosts = cosmodog.getFuelConsumer().turnCosts(position1, position2, player, map, appCx);
			vehicle.decreaseFuel(fuelCosts);
		}
	}
	
	private void updateWater(Position position1, Position position2) {
		
		ApplicationContext appCx = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Player player = ApplicationContextUtils.getPlayer();
		int waterCosts = cosmodog.getWaterConsumer().turnCosts(position1, position2, player, map, appCx);
		player.decreaseWater(waterCosts);
	}
	
	private void updateFood(Position position1, Position position2) {
		
		ApplicationContext appCx = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Player player = ApplicationContextUtils.getPlayer();
		int foodCosts = cosmodog.getFoodConsumer().turnCosts(position1, position2, player, map, appCx);
		player.decreaseFood(foodCosts);
	}
	
	@Override
	public void beforeWaiting(Actor actor, ApplicationContext applicationContext) {
		Player player = (Player)actor;
		player.deactivatePortalRay();
		oldTurnsWormAlerted = player.getTurnsWormAlerted();
		addTime();
		updateWormAlert(player, applicationContext );
		updatePoisonCount(player, applicationContext);
		updateWater(actor.getPosition(), actor.getPosition());
		updateFood(actor.getPosition(), actor.getPosition());
		updateFuel(actor.getPosition(), actor.getPosition());
	}
	
	
	@Override
	public void onInteractingWithTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
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
	public void beforeTurning(DirectionType before, DirectionType after) {
		Player player = ApplicationContextUtils.getPlayer();
		player.deactivatePortalRay();
	}

	@Override
	public void afterTurning(DirectionType before, DirectionType after) {

	}

	@Override
	public void beforeBlock(Actor actor, Position position1, Position position2) {

	}

	@Override
	public void afterBlock(Actor actor, Position position1, Position position2) {
		updatePortalRay();
	}

	@Override
	public void beforeMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void onLeavingTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		checkStarvation(applicationContext);
		checkDehydration(applicationContext);
		checkTemperature(applicationContext);
		checkRadiation(applicationContext);
		checkElectricity(applicationContext);
		checkWorm(applicationContext);
		checkMine(applicationContext);
		updateSnowfall();
		checkContaminationStatus(applicationContext);
		updatePresenseDetectors();
		updatePortalRay();
		ApplicationContextUtils.getGameProgress().incTurn();
		ApplicationContextUtils.getCosmodogGame().getTimer().updatePlayTime();

		GameEventUtils.throwEvent(new GameEventEndedTurn());

		updateCache(actor, position1, position2);

		if (timeToAutosave()) {
			autosave();
		}
	}

	private void updateCache(Actor actor, Position position1, Position position2) {
		PlayerMovementCache.getInstance().update(actor, position1, position2);
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
		updatePortalRay();
		ApplicationContextUtils.getGameProgress().incTurn();
		ApplicationContextUtils.getCosmodogGame().getTimer().updatePlayTime();

		updateCache(actor, actor.getPosition(), actor.getPosition());

		if (timeToAutosave()) {
			autosave();
		}
	}

	@Override
	public void beforeTeleportation(Actor actor, ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		player.deactivatePortalRay();
	}

	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		refillWater(applicationContext);
		refillFuel(applicationContext);
		detectMines(applicationContext);
		updateSnowfall();
		updatePortalRay();
		changeLettersOnLetterPlates(applicationContext);

		updateCache(actor, actor.getPosition(), actor.getPosition());

		GameEventUtils.throwEvent(new GameEventTeleported());
	}

	@Override
	public void beforeRespawn(Actor actor, ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		player.deactivatePortalRay();
	}

	@Override
	public void afterRespawn(Actor actor, ApplicationContext applicationContext) {
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
		updateSnowfall();
		updatePortalRay();
		checkContaminationStatus(applicationContext);
		ApplicationContextUtils.getGameProgress().incTurn();
		ApplicationContextUtils.getCosmodogGame().getTimer().updatePlayTime();

		updateCache(actor, actor.getPosition(), actor.getPosition());
	}

	@Override
	public void afterFight(Actor actor, ApplicationContext applicationContext) {
		updateCache(actor, actor.getPosition(), actor.getPosition());
	}

	@Override
	public void beforeSwitchingPlane(Actor actor, ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		player.deactivatePortalRay();
	}

	@Override
	public void afterSwitchingPlane(Actor actor, ApplicationContext applicationContext) {
		collectCollectibles(applicationContext);
		refillWater(applicationContext);
		refillFuel(applicationContext);
		detectMines(applicationContext);
		updateSnowfall();
		updatePortalRay();
		changeLettersOnLetterPlates(applicationContext);

		updateCache(actor, actor.getPosition(), actor.getPosition());
	}

	private void collectCollectibles(ApplicationContext applicationContext) {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		CosmodogMap cosmodogMap = cosmodog.getCosmodogGame().mapOfPlayerLocation();
		
		Collection<Piece> pieces = cosmodogMap.getMapPieces().values();
		Iterator<Piece> it = pieces.iterator();

		while (it.hasNext()) {
			Piece piece = it.next();
			
			if (piece.getPosition().equals(player.getPosition())) {
				
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
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = cosmodog.getCosmodogGame().getPlayer();
		
		WaterValidator waterValidator = cosmodog.getWaterValidator();
		
		boolean hasWaterAccess = waterValidator.waterInReach(player, map, player.getPosition());
		
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
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = cosmodog.getCosmodogGame().getPlayer();

		int collectiblesLayerTileId = map.getTileId(player.getPosition(), Layers.LAYER_META_COLLECTIBLES);
		if (TileType.FUEL.getTileId() == collectiblesLayerTileId) {
			String notificationText = null;
			if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
				VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
				Vehicle vehicle = vehicleItem.getVehicle();
				if (vehicle.getFuel() < vehicle.getMaxFuel()) {
					vehicle.setFuel(vehicle.getMaxFuel());
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
				actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction(notificationText));
			}
			
		}
		
	}
	
	private void changeLettersOnLetterPlates(ApplicationContext applicationContext) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Collection<DynamicPiece> letterPlates = map.getDynamicPieces().get(LetterPlate.class);
		
		for (DynamicPiece piece : letterPlates) {
			LetterPlate letterPlate = (LetterPlate)piece;

			if (letterPlate.getPosition().equals(player.getPosition())) {
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
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
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
			
			boolean mineIsVisible = mineDetector != null && PiecesUtils.distanceBetweenPieces(player, mine) < MineDetectorInventoryItem.DETECTION_DISTANCE;
			
			mine.setState(mineIsVisible ? Mine.STATE_VISIBLE : Mine.STATE_INVISIBLE);
		}
		
	}
	
	private void activatePressureButton(ApplicationContext applicationContext) {
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Collection<DynamicPiece> buttons = map.getDynamicPieces().get(PressureButton.class);
		
		for (DynamicPiece piece : buttons) {
			PressureButton button = (PressureButton)piece;
			
			if (button.getPosition().equals(player.getPosition())) {
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
		DynamicPiece piece = ApplicationContextUtils.getCosmodogGame().dynamicPieceAtPosition(player.getPosition());
		if (!(piece instanceof Poison poison)) {
			return;
		}

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
			text = "<font:critical> Death by poison in " + Constants.TURNS_BEFORE_DEATH_BY_POISON + " turns !!!";
			OverheadNotificationAction.registerOverheadNotification(player, text);
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_POISONED).play();
		}
		
		player.contaminate();
	
	}

	@Override
	public void beforeFight(Actor actor, ApplicationContext applicationContext) {

	}

	/**
	 * Removes the contamination flag if the player stays on the decontamination stop.
	 */
	private void checkDecontamination(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		boolean onDecontaminationSpot = TileType.DECONTAMINATION_SPOT.getTileId() == map.getTileId(player.getPosition(), Layers.LAYER_GEAR);
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
				CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
				
				Player player = cosmodog.getCosmodogGame().getPlayer();
				int tileId = map.getTileId(player.getPosition(), Layers.LAYER_META_TEMPERATURE);
				
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
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		
		int radiationTileId = map.getTileId(player.getPosition(), Layers.LAYER_META_RADIATION);
		
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
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		
		int electricityTileId = map.getTileId(player.getPosition(), Layers.LAYER_META_RADIATION);
		
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

	private Optional<TiledObject> wormRegion (Player player, ApplicationContext applicationContext) {

		Optional<TiledObject> retVal = Optional.empty();

		int tileLength = TileUtils.tileLengthSupplier.get();

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		List<TiledObject> wormRegions = Lists.newArrayList();

		TiledObjectGroup wormsObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_WORMS);

		wormRegions.addAll(wormsObjectGroup.getObjects().values());

		for (TiledObject wormRegion : wormRegions) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), wormRegion)) {
				retVal = Optional.of(wormRegion);
				break;
			}
		}
		return retVal;
	}

	private void updateWormAlert(Player player, ApplicationContext applicationContext) {

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		boolean inWormRegion = wormRegion(player, applicationContext).isPresent();

		boolean inSnow = isPlayerOnGroundTypeTile(TileType.GROUND_TYPE_SNOW, map, player);
		boolean onPlatform = CosmodogMapUtils.isTileOnPlatform(player.getPosition());
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
					TiledObject wormRegion = wormRegion(player, applicationContext).get(); //We now it is there since the turns till worm attack is > 0.
					CosmodogGame cosmodogGame = applicationContext.getCosmodog().getCosmodogGame();
					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.WORM_ATTACK, new WormAttackAction(5000, wormRegion));
				}
			
			}
		}
	}

	private void checkContaminationStatus(ApplicationContext applicationContext) {
		Player player = ApplicationContextUtils.getPlayer();
		int turnsPoisoned = player.getTurnsPoisoned();
		if (turnsPoisoned != 0) {
			
			int turnsTillDeath = Constants.TURNS_BEFORE_DEATH_BY_POISON - turnsPoisoned;
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
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Collection<DynamicPiece> mines = map.getDynamicPieces().get(Mine.class);
		
		Mine mineUnderPlayer = null;
		
		for (DynamicPiece piece : mines) {
			Mine mine = (Mine)piece;
			if (mine.getPosition().equals(player.getPosition())) {
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
		int tileId = map.getTileId(player.getPosition(), Layers.LAYER_META_GROUNDTYPES);
		retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(tileType);
		
		return retVal;
	}

	private void updateSnowfall() {

		int tileLength = TileUtils.tileLengthSupplier.get();

		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		TiledObjectGroup weatherObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_WEATHER);
		TiledObject snowfallRegion = weatherObjectGroup.getObjects().get("Snowfall");
		TiledObject snowfallBorderRegion = weatherObjectGroup.getObjects().get("Snowfall_border");
		boolean inSnowfall = RegionUtils.tileInRegion(player.getPosition(), map.getMapType(), snowfallRegion);
		boolean inSnowfallBorder = RegionUtils.tileInRegion(player.getPosition(), map.getMapType(), snowfallBorderRegion);
		boolean underRoof = !RegionUtils.roofsOverPiece(player, map).isEmpty() && RegionUtils.roofRemovalBlockersOverPiece(player, map).isEmpty();
		SnowfallChangeAction snowfallChangeAction =  (SnowfallChangeAction) game.getActionRegistry().getRegisteredAction(AsyncActionType.SNOWFALL_CHANGE);

		if (underRoof || !inSnowfallBorder) {
			if (snowfallChangeAction != null) {
				snowfallChangeAction.setFadesInNotOut(false);
			}
		} else if (inSnowfall) {
			if (snowfallChangeAction == null) {
				snowfallChangeAction = SnowfallChangeAction.fadingInActionInstance(3000);
				game.getActionRegistry().registerAction(AsyncActionType.SNOWFALL_CHANGE, snowfallChangeAction);
			} else {
				snowfallChangeAction.setFadesInNotOut(true);
			}
		}

	}

	private void updatePortalRay() {
		//Some things on the map change after the player moves. For instance, presence detectors can close doors.
		//These changes happen as asynchronous actions. If we would update the ray directly, it would not pick up those changes.
		//That is why we pack it into a short action of type MODAL_WINDOW. This way, the update will be queued up after all
		//those changes.
		AsyncAction portalRayUpdateAction = new FixedLengthAsyncAction(0) {
			@Override
			public void onEnd() {
				Player player = ApplicationContextUtils.getPlayer();
				CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
				int tileId = map.getTileId(player.getPosition(), Layers.LAYER_META_PORTALS);
				TileType tileType = TileType.getByLayerAndTileId(Layers.LAYER_META_PORTALS, tileId);

				boolean emittable = tileType.equals(TileType.PORTAL_RAY_EMITTABLE);
				boolean onEmpField = map.dynamicPieceAtPosition(Emp.class, player.getPosition()).isPresent();

				if (emittable && !onEmpField) {
					player.activatePortalRay();
				} else {
					player.deactivatePortalRay();
				}
			}
		};
		ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
		actionRegistry.registerAction(AsyncActionType.MOVEMENT, portalRayUpdateAction);

	}

	private void updatePresenseDetectors() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		Set<DynamicPiece> presenceDetectorDynamicPieces = map
				.getDynamicPieces()
				.values()
				.stream()
				.filter(e -> e instanceof PresenceDetector)
				.collect(Collectors.toSet());

		for (DynamicPiece presenceDetectorDynamicPiece : presenceDetectorDynamicPieces) {
			Position position = presenceDetectorDynamicPiece.getPosition();
			if (player.getPosition().equals(position)) {
				((PresenceDetector)presenceDetectorDynamicPiece).presenceDetected(game, player);
			} else {
				Optional<DynamicPiece> optMoveableDynamicPiece = map.dynamicPiecesAtPosition(position).stream().filter(e -> e instanceof  MoveableDynamicPiece).findFirst();

				if (optMoveableDynamicPiece.isPresent()) {
					((PresenceDetector)presenceDetectorDynamicPiece).presenceDetected(game, ((MoveableDynamicPiece)optMoveableDynamicPiece.get()).asActor());
				} else {
					((PresenceDetector)presenceDetectorDynamicPiece).presenceLost(game);
				}
			}
		}
	}

	private boolean timeToAutosave() {
		int turn = ApplicationContextUtils.getGameProgress().getTurn();
		return turn > 0 && turn % AUTOSAVE_INTERVAL_IN_TURNS == 0;
	}

	private void autosave() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		cosmodog.getGamePersistor().saveCosmodogGame(cosmodogGame, PathUtils.gameSaveDir() + "/" + cosmodogGame.getGameName() + ".sav");
	}


}
