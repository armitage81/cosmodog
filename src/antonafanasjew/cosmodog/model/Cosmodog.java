package antonafanasjew.cosmodog.model;

import java.util.Map;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.GameLifeCycle;
import antonafanasjew.cosmodog.InputHandlers;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.WaterValidator;
import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogScorePersistor;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.ArmorInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.BoatInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.DynamiteInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.InfobitInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.InsightInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.MedipackInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SoulEssenceInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SuppliesInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.VehicleInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.WeaponInteraction;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.pathfinding.TileBasedMapFactory;
import antonafanasjew.cosmodog.pathfinding.TravelTimeCalculator;
import antonafanasjew.cosmodog.sight.SightRadiusCalculator;


/**
 * Main game model that will contain data beyond the game session.
 * F.i. it will include score list, user profiles and so on.
 */
public class Cosmodog extends CosmodogModel {

    private static final long serialVersionUID = -5631342281411149420L;

    private User user;
    private CosmodogGame cosmodogGame;
    private ScoreList scoreList;
    private CollisionValidator collisionValidator;
    private WaterValidator waterValidator;
    private SightRadiusCalculator sightRadiusCalculator;
    private TravelTimeCalculator travelTimeCalculator;
    private PathFinder pathFinder;
    private GameLifeCycle gameLifeCycle = new GameLifeCycle();
	private InputHandlers inputHandlers = new InputHandlers();
	private CosmodogScorePersistor scorePersistor;
	private CosmodogGamePersistor gamePersistor;
	private TileBasedMapFactory tileBasedMapFactory = new TileBasedMapFactory();
	private Map<String, PieceInteraction> pieceInteractionMap = Maps.newHashMap();
	
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public CosmodogGame getCosmodogGame() {
        return cosmodogGame;
    }
    public void setCosmodogGame(CosmodogGame cosmodogGame) {
        this.cosmodogGame = cosmodogGame;
    }
    public ScoreList getScoreList() {
        return scoreList;
    }
    public void setScoreList(ScoreList scoreList) {
        this.scoreList = scoreList;
    }
	public CollisionValidator getCollisionValidator() {
		return collisionValidator;
	}
	public void setCollisionValidator(CollisionValidator collisionValidator) {
		this.collisionValidator = collisionValidator;
	}
	public TravelTimeCalculator getTravelTimeCalculator() {
		return travelTimeCalculator;
	}
	public void setTravelTimeCalculator(TravelTimeCalculator travelTimeCalculator) {
		this.travelTimeCalculator = travelTimeCalculator;
	}
	public GameLifeCycle getGameLifeCycle() {
		return gameLifeCycle;
	}
	

	public InputHandlers getInputHandlers() {
		return inputHandlers;
	}


	public CosmodogGamePersistor getGamePersistor() {
		return gamePersistor;
	}

	public void setGamePersistor(CosmodogGamePersistor gamePersistor) {
		this.gamePersistor = gamePersistor;
	}
	
	public CosmodogScorePersistor getScorePersistor() {
		return scorePersistor;
	}
	public void setScorePersistor(CosmodogScorePersistor scorePersistor) {
		this.scorePersistor = scorePersistor;
	}
	public PathFinder getPathFinder() {
		return pathFinder;
	}
	
	public void setPathFinder(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}

	public TileBasedMapFactory getTileBasedMapFactory() {
		return tileBasedMapFactory;
	}
	public SightRadiusCalculator getSightRadiusCalculator() {
		return sightRadiusCalculator;
	}
	public void setSightRadiusCalculator(SightRadiusCalculator sightRadiusCalculator) {
		this.sightRadiusCalculator = sightRadiusCalculator;
	}
	public WaterValidator getWaterValidator() {
		return waterValidator;
	}
	public void setWaterValidator(WaterValidator waterValidator) {
		this.waterValidator = waterValidator;
	}
	public Map<String, PieceInteraction> getPieceInteractionMap() {
		return pieceInteractionMap;
	}
}
