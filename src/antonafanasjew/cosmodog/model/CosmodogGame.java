package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.model.portals.FixedSizeQueue;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.sound.AmbientSoundRegistry;
import antonafanasjew.cosmodog.timing.Timer;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;
import com.google.common.collect.Maps;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Model of a game session. This will contain the data about the map, actors and
 * the progression
 */
public class CosmodogGame extends CosmodogModel {

	@Serial
	private static final long serialVersionUID = 6194313671668632967L;

	private transient ActionRegistry actionRegistry;
	
	private transient ActionRegistry interfaceActionRegistry;
	
	private transient AmbientSoundRegistry ambientSoundRegistry;
	
	private transient TextFrame textFrame;
	
	private transient Book openBook;
	
	private transient String openBookTitle;
	
	private transient CollectibleTool currentlyFoundTool;
	
	private transient InGameMenu inGameMenu;
	
	private transient RuleBook ruleBook;
	
	private String gameName;
	
	private User user;

	private PlanetaryCalendar planetaryCalendar;
	
	private Timer timer;

	private Player player;

	private final Map<MapType, CosmodogMap> maps = Maps.newHashMap();

	private FixedSizeQueue<Portal> portals = new FixedSizeQueue<Portal>(2);

	private Cam cam;

	/**
	 * This method should be called after each creation of a CosmodogGame object. It will initialize the non persistent fields
	 * before or after all persistent fields will be initialized (via deserialization or via the bean initialization at the start of the game)
	 */
	public void initTransientFields() {
		InitializationUtils.initializeCosmodogGameTransient(this);
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Map<MapType, CosmodogMap> getMaps() {
		return maps;
	}

	public CosmodogMap mapOfPlayerLocation() {
		return maps.get(player.getPosition().getMapType());
	}

	public Cam getCam() {
		return cam;
	}

	public void setCam(Cam cam) {
		this.cam = cam;
	}

	public PlanetaryCalendar getPlanetaryCalendar() {
		return planetaryCalendar;
	}

	public void setPlanetaryCalendar(PlanetaryCalendar planetaryCalendar) {
		this.planetaryCalendar = planetaryCalendar;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	public RuleBook getRuleBook() {
		return ruleBook;
	}

	public void setRuleBook(RuleBook ruleBook) {
		this.ruleBook = ruleBook;
	}

	public ActionRegistry getInterfaceActionRegistry() {
		return interfaceActionRegistry;
	}

	public TextFrame getTextFrame() {
		return textFrame;
	}

	public void setTextFrame(TextFrame textFrame) {
		this.textFrame = textFrame;
	}

	public Book getOpenBook() {
		return openBook;
	}
	
	public void setOpenBook(Book openBook) {
		this.openBook = openBook;
	}
	
	public String getOpenBookTitle() {
		return openBookTitle;
	}
	
	public void setOpenBookTitle(String openBookTitle) {
		this.openBookTitle = openBookTitle;
	}
	
	public CollectibleTool getCurrentlyFoundTool() {
		return currentlyFoundTool;
	}
	
	public void setCurrentlyFoundTool(CollectibleTool currentlyFoundTool) {
		this.currentlyFoundTool = currentlyFoundTool;
	}
	
	public InGameMenu getInGameMenu() {
		return inGameMenu;
	}

	public void setInGameMenu(InGameMenu inGameMenu) {
		this.inGameMenu = inGameMenu;
	}
	
	public void setActionRegistry(ActionRegistry actionRegistry) {
		this.actionRegistry = actionRegistry;
	}

	public void setInterfaceActionRegistry(ActionRegistry interfaceActionRegistry) {
		this.interfaceActionRegistry = interfaceActionRegistry;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public AmbientSoundRegistry getAmbientSoundRegistry() {
		return ambientSoundRegistry;
	}

	public void setAmbientSoundRegistry(AmbientSoundRegistry ambientSoundRegistry) {
		this.ambientSoundRegistry = ambientSoundRegistry;
	}
	
	public Optional<Portal> portal(Position position, DirectionType directionType) {
		for (Portal portal : portals) {
			if (portal.position.equals(position) && portal.directionType == directionType) {
				return Optional.of(portal);
			}
		}
		return Optional.empty();
	}

	public void createPortal(Portal portal) {
		portals.offer(portal);
	}

	public List<Portal> getPortals() {
		return portals.stream().toList();
	}

	public void clearPortals() {
		portals.clear();
	}

	/*
	Calculates the target entrance of the player based on his current position and direction.
	In the most cases it will be the adjacent position in the direction the player is facing.
	But in case of portals, it could be the position of the other portal.
	Since portal can cross different maps, the target entrance can be on a different map.
	This is the reason why the method is located in the CosmodogGame class and not in the CosmodogMap class.
	 */
	public Entrance targetEntrance(Actor actor, DirectionType directionType) {

		Position envisionedPosition;
		DirectionType envisionedPositionEntrance = directionType;
		boolean usedPortal = false;
		Portal entrancePortal = null;
		Portal exitPortal = null;

		Position actorsPosition = Position.fromCoordinates(actor.getPosition().getX(), actor.getPosition().getY(), actor.getPosition().getMapType());

		Position facedAdjacentPosition = DirectionType.facedAdjacentPosition(actorsPosition, directionType);

		DirectionType directionFacingActor = DirectionType.reverse(directionType);

		Optional<Portal> optPortal = portal(facedAdjacentPosition, directionFacingActor);
		if (optPortal.isPresent()) {
			Optional<Portal> otherPortal = Optional.empty();
			for (Portal portal : getPortals()) {
				if (!portal.position.equals(facedAdjacentPosition) || portal.directionType != directionFacingActor) {
					otherPortal = Optional.of(portal);
					break;
				}
			}
			if (otherPortal.isPresent()) {
				envisionedPosition = DirectionType.facedAdjacentPosition(otherPortal.get().position, otherPortal.get().directionType);
				envisionedPositionEntrance = otherPortal.get().directionType;
				usedPortal = true;
				entrancePortal = optPortal.get();
				exitPortal = otherPortal.get();
			} else {
				envisionedPosition = facedAdjacentPosition;
			}
		} else {
			envisionedPosition = facedAdjacentPosition;
		}

		Entrance entrance;
		if (usedPortal) {
			entrance = Entrance.instanceForPortals(envisionedPosition, envisionedPositionEntrance, entrancePortal, exitPortal);
		} else {
			entrance = Entrance.instance(envisionedPosition, envisionedPositionEntrance);
		}
		return entrance;
	}

}
