package antonafanasjew.cosmodog.rules.actions;

import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class SetGameProgressPropertyAction extends AbstractRuleAction {

	private static final long serialVersionUID = 5740390670029721375L;

	private String gameProgressProperty;
	private String value;

	public SetGameProgressPropertyAction(String gameProgressProperty, String value) {
		this.gameProgressProperty = gameProgressProperty;
		this.value = value;
	}

	@Override
	public void execute(GameEvent event) {
		ApplicationContextUtils.getGameProgress().getProgressProperties().put(gameProgressProperty, value);
	}

}
