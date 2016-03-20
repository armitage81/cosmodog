package antonafanasjew.cosmodog.rules.actions;

import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;

public class SysoutAction extends AbstractRuleAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private String output;

	public SysoutAction(String output) {
		this.output = output;
	}
	
	@Override
	public void execute(GameEvent event) {
		System.out.println(output);
	}

}
