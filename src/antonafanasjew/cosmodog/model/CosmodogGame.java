package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.sound.AmbientSoundRegistry;
import antonafanasjew.cosmodog.timing.Timer;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransitionRegistry;
import antonafanasjew.cosmodog.view.transitions.DialogWithAlisaTransition;
import antonafanasjew.cosmodog.view.transitions.EndingTransition;
import antonafanasjew.cosmodog.view.transitions.MonolithTransition;
import antonafanasjew.cosmodog.view.transitions.MovementAttemptTransition;
import antonafanasjew.cosmodog.view.transitions.TeleportationTransition;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;

/**
 * Model of a game session. This will contain the data about the map, actors and
 * the progression
 */
public class CosmodogGame extends CosmodogModel {

	private static final long serialVersionUID = 6194313671668632967L;

	private transient ActionRegistry actionRegistry;
	
	private transient ActionRegistry interfaceActionRegistry;
	
	private transient AmbientSoundRegistry ambientSoundRegistry;
	
	private transient MovementAttemptTransition movementAttemptTransition;
	
	private transient TeleportationTransition teleportationTransition;
	
	private transient ActorTransitionRegistry actorTransitionRegistry;

	private transient TextFrame textFrame;
	
	private transient Book openBook;
	
	private transient String openBookTitle;
	
	private transient DialogWithAlisaTransition dialogWithAlisaTransition;
	
	private transient MonolithTransition monolithTransition;
	
	private transient EndingTransition endingTransition;
	
	private transient CollectibleTool currentlyFoundTool;
	
	private transient InGameMenu inGameMenu;
	
	private transient RuleBook ruleBook;
	
	private String gameName;
	
	private User user;

	private PlanetaryCalendar planetaryCalendar;
	
	private Timer timer;

	private Player player;

	private CosmodogMap map;

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

	public CosmodogMap getMap() {
		return map;
	}

	public void setMap(CosmodogMap map) {
		this.map = map;
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

	public ActorTransitionRegistry getActorTransitionRegistry() {
		return actorTransitionRegistry;
	}

	public MovementAttemptTransition getMovementAttemptTransition() {
		return movementAttemptTransition;
	}

	public void setMovementAttemptTransition(MovementAttemptTransition movementAttemptTransition) {
		this.movementAttemptTransition = movementAttemptTransition;
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
	
	public TeleportationTransition getTeleportationTransition() {
		return teleportationTransition;
	}

	public void setActionRegistry(ActionRegistry actionRegistry) {
		this.actionRegistry = actionRegistry;
	}

	public void setInterfaceActionRegistry(ActionRegistry interfaceActionRegistry) {
		this.interfaceActionRegistry = interfaceActionRegistry;
	}

	public void setTeleportationTransition(TeleportationTransition teleportationTransition) {
		this.teleportationTransition = teleportationTransition;
	}

	public void setActorTransitionRegistry(ActorTransitionRegistry actorTransitionRegistry) {
		this.actorTransitionRegistry = actorTransitionRegistry;
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
	
	public DialogWithAlisaTransition getDialogWithAlisaTransition() {
		return dialogWithAlisaTransition;
	}
	
	public void setDialogWithAlisaTransition(DialogWithAlisaTransition dialogWithAlisaTransition) {
		this.dialogWithAlisaTransition = dialogWithAlisaTransition;
	}
	
	public MonolithTransition getMonolithTransition() {
		return monolithTransition;
	}
	
	public void setMonolithTransition(MonolithTransition monolithTransition) {
		this.monolithTransition = monolithTransition;
	}
	
	public EndingTransition getEndingTransition() {
		return endingTransition;
	}
	
	public void setEndingTransition(EndingTransition endingTransition) {
		this.endingTransition = endingTransition;
	}
}
