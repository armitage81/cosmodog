package antonafanasjew.cosmodog.rules.actions;

import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;

/**
 * This action is a feature bound decorator around an actual action.
 * Its only purpose is to block the execution of the actual action in case 
 * the corresponding feature is disabled.
 */
public class FeatureBoundAction extends AbstractRuleAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private String feature;
	private RuleAction delegate;

	public FeatureBoundAction(String feature, RuleAction delegate) {
		this.feature = feature;
		this.delegate = delegate;
	}
	
	@Override
	public void execute(GameEvent event) {
		Features.getInstance().featureBoundProcedure(feature, new Runnable() {
			@Override
			public void run() {
				delegate.execute(event);
			}
		});
	}
	
}
