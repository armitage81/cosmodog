package antonafanasjew.cosmodog.rules;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.rules.events.GameEvent;


/**
 * A rule is an action whose execution is dependent on the event type and the trigger logic.
 * If no eventTypes are given, all will be accepted.
 */
public class Rule implements Serializable {
	
	
	private static final long serialVersionUID = 3076763824308264337L;

	public static final int RULE_PRIORITY_EARLIEST = 0;
	public static final int RULE_PRIORITY_10 = 10;
	public static final int RULE_PRIORITY_20 = 20;
	public static final int RULE_PRIORITY_30 = 30;
	public static final int RULE_PRIORITY_40 = 40;
	public static final int RULE_PRIORITY_50 = 50;
	public static final int RULE_PRIORITY_60 = 60;
	public static final int RULE_PRIORITY_70 = 70;
	public static final int RULE_PRIORITY_80 = 80;
	public static final int RULE_PRIORITY_90 = 90;
	public static final int RULE_PRIORITY_LATEST = 100;
	
	
	
	public static final String RULE_DIALOG_AFTER_LANDING = "dialog.afterlanding";
	public static final String RULE_DIALOG_ALISA_ENTERS = "dialog.alisaenters";
	public static final String RULE_WINNING = "winning";
	public static final String RULE_SCORE_REWARD = "scorereward";
	
	public static final String RULE_WORM_DELAY_PHASE2 = "WormDelayToPhase2";
	public static final String RULE_WORM_DELAY_PHASE3 = "WormDelayToPhase3";
	public static final String RULE_WORM_DELAY_PHASE4 = "WormDelayToPhase4";
	public static final String RULE_PICK_UP_BLUE_KEYCARD = "PickUpBlueKeyCard";
	public static final String RULE_OPEN_GATE_TO_LAUNCH_POD = "OpenGateToLaunchPod";
	public static final String RULE_ACTIVATE_TELEPORT = "ActivateTeleport";
	public static final String RULE_DAMAGE_LAST_BOSS = "DamageLastBoss";
	public static final String RULE_FOUND_SECRET = "FoundSecret";
	public static final String RULE_COLLECTED_ALL_INSIGHTS = "CollectedAllInsights";

	public static final String RULE_OPERATE_SECRET_DOORS_IN_SOKOBAN = "OperateSecretDoorsInSokoban";
	
	private String id;
	private Collection<Class<? extends GameEvent>> eventTypes;
	private RuleTrigger ruleTrigger;
	private RuleAction ruleAction;
	private int priority;

	public Rule(String id, RuleTrigger ruleTrigger, RuleAction ruleAction) {
		this(id, Lists.newArrayList(), ruleTrigger, ruleAction, RULE_PRIORITY_50);
	}
	
	public Rule(String id, RuleTrigger ruleTrigger, RuleAction ruleAction, int priority) {
		this(id, Lists.newArrayList(), ruleTrigger, ruleAction, priority);
	}
	
	public Rule(String id, Collection<Class<? extends GameEvent>> eventTypes, RuleTrigger ruleTrigger, RuleAction ruleAction, int priority) {
		this.id = id;
		this.eventTypes = eventTypes;
		this.ruleTrigger = ruleTrigger;
		this.ruleAction = ruleAction;
		this.priority = priority;
	}
	
	public void apply(GameEvent event) {
		if (eventTypes.isEmpty() || eventTypes.contains(event.getClass())) {
    		if (ruleTrigger.accept(event)) {
    			ruleAction.execute(event);
    		}
		}
	}

	public String getId() {
		return id;
	}

	public int getPriority() {
		return priority;
	}
	
}
