package antonafanasjew.cosmodog.model;

import java.util.Map;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameLifeCycle;
import antonafanasjew.cosmodog.InputHandlers;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.WaterValidator;
import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogScorePersistor;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.pathfinding.TileBasedMapFactory;
import antonafanasjew.cosmodog.pathfinding.TravelTimeCalculator;
import antonafanasjew.cosmodog.player.DefaultPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.sight.SightModifier;

import com.google.common.collect.Maps;


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
    private SightModifier sightModifier;
    private TravelTimeCalculator travelTimeCalculator;
    private PathFinder pathFinder;
    private GameLifeCycle gameLifeCycle = new GameLifeCycle();
	private InputHandlers inputHandlers = new InputHandlers();
	private CosmodogScorePersistor scorePersistor;
	private CosmodogGamePersistor gamePersistor;
	private TileBasedMapFactory tileBasedMapFactory = new TileBasedMapFactory();
	private Map<String, PieceInteraction> pieceInteractionMap = Maps.newHashMap();
	private PlayerBuilder playerBuilder = new DefaultPlayerBuilder();
	
	public Cosmodog() {
		try {
			if (System.getProperty("playerBuilder") != null) {
				String playerBuilderTypeName = System.getProperty("playerBuilder");
				@SuppressWarnings("rawtypes")
				Class playerBuilderClass = Class.forName(playerBuilderTypeName);
				PlayerBuilder playerBuilder = (PlayerBuilder)playerBuilderClass.newInstance();
				this.playerBuilder = playerBuilder;
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
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
		//The cache is updated with every player movement. But it has to be updated at the init time as well.
		Player player = cosmodogGame.getPlayer(); 
		PlayerMovementCache.getInstance().afterMovement(player, player.getPositionX(), player.getPositionY(), player.getPositionX(), player.getPositionY(), ApplicationContext.instance());
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

	public WaterValidator getWaterValidator() {
		return waterValidator;
	}
	public void setWaterValidator(WaterValidator waterValidator) {
		this.waterValidator = waterValidator;
	}
	public Map<String, PieceInteraction> getPieceInteractionMap() {
		return pieceInteractionMap;
	}
	
	public SightModifier getSightModifier() {
		return sightModifier;
	}
	
	public void setSightModifier(SightModifier sightModifier) {
		this.sightModifier = sightModifier;
	}
	public PlayerBuilder getPlayerBuilder() {
		return playerBuilder;
	}
}
