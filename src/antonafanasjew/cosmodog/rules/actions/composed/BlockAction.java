package antonafanasjew.cosmodog.rules.actions.composed;

import java.util.List;

import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;

import com.google.common.collect.Lists;

public class BlockAction extends AbstractRuleAction {

	private static final long serialVersionUID = -1164460093370534532L;

	private List<RuleAction> actions = Lists.newArrayList();
	
	public static BlockAction block(List<RuleAction> actions) {
		return new BlockAction(actions.toArray(new RuleAction[actions.size()]));
	}
	
	public static BlockAction block(RuleAction... actions) {
		return new BlockAction(actions);
	}
	
	public BlockAction(RuleAction... actions) {
		this.actions.addAll(Lists.newArrayList(actions));
	}

	@Override
	public void execute(GameEvent event) {
		for (RuleAction action : actions) {
			action.execute(event);
		}
	}

}
