package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.notifications.NotificationList;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.timing.Chronometer;
import antonafanasjew.cosmodog.view.transitions.ActorTransitionRegistry;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxStateUpdater;
import antonafanasjew.cosmodog.writing.textframe.TextFrame;

/**
 * Model of a game session. This will contain the data about the map, actors and
 * the progression
 */
public class CosmodogGame extends CosmodogModel {

	private static final long serialVersionUID = 6194313671668632967L;

	private ActionRegistry actionRegistry = new ActionRegistry();
	
	private ActionRegistry interfaceActionRegistry = new ActionRegistry();
	
	private FightPhaseTransition fightPhaseTransition = null;
	
	private ActorTransitionRegistry actorTransitionRegistry = new ActorTransitionRegistry();

	private PlanetaryCalendar planetaryCalendar;

	private Chronometer chronometer = new Chronometer();

	private WritingTextBoxStateUpdater tutorialStateUpdater;
	private WritingTextBoxStateUpdater commentsStateUpdater;
	
	private TextFrame textFrame = null;
	
	private NotificationList playerStatusNotificationList = new NotificationList();
		
	private RuleBook ruleBook = new RuleBook();
	
	
	private WritingTextBoxState writingTextBoxState;
	
	private User user;

	private Player player;

	private CosmodogMap map;

	private Cam cam;

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

	public Chronometer getChronometer() {
		return chronometer;
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	/**
	 * Contains the tutorial messages.
	 * @return Tutorial messages.
	 */
	public WritingTextBoxStateUpdater getTutorialStateUpdater() {
		return tutorialStateUpdater;
	}

	public void setTutorialStateUpdater(WritingTextBoxStateUpdater tutorialStateUpdater) {
		this.tutorialStateUpdater = tutorialStateUpdater;
	}
	
	/**
	 * Contains comments of the characters in the game that do not interrupt the controls (e.g. Alisa's comments)
	 * @return State of the comment collection.
	 */
	public WritingTextBoxStateUpdater getCommentsStateUpdater() {
		return commentsStateUpdater;
	}

	public void setCommentsStateUpdater(WritingTextBoxStateUpdater commentsStateUpdater) {
		this.commentsStateUpdater = commentsStateUpdater;
	}
	
	
	/**
	 * Contains the over-head text messages (e.g. score points added.) 
	 * @return List of over-head messages 
	 */
	public NotificationList getPlayerStatusNotificationList() {
		return playerStatusNotificationList;
	}

	public void setPlayerStatusNotificationList(NotificationList playerStatusNotificationList) {
		this.playerStatusNotificationList = playerStatusNotificationList;
	}

	public RuleBook getRuleBook() {
		return ruleBook;
	}

	public void setRuleBook(RuleBook ruleBook) {
		this.ruleBook = ruleBook;
	}

	/**
	 * Normally, this is returning null. 
	 * In case, it is not null, the dialog box is open
	 * and user input should be changed accordingly.
	 */
	public WritingTextBoxState getWritingTextBoxState() {
		return writingTextBoxState;
	}

	public void setWritingTextBoxState(WritingTextBoxState writingTextBoxState) {
		this.writingTextBoxState = writingTextBoxState;
	}

	public ActorTransitionRegistry getActorTransitionRegistry() {
		return actorTransitionRegistry;
	}

	public FightPhaseTransition getFightPhaseTransition() {
		return fightPhaseTransition;
	}

	public void setFightPhaseTransition(FightPhaseTransition fightPhaseTransition) {
		this.fightPhaseTransition = fightPhaseTransition;
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

}
