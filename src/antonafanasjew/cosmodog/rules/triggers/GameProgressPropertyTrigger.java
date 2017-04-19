package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GameProgressPropertyTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = -7325438979653214792L;

	private String gameProgressProperty;
	private String value;
	private String valueForNull;
	
	public GameProgressPropertyTrigger(String gameProgressProperty, String value) {
		this(gameProgressProperty, value, "false");
	}
	
	public GameProgressPropertyTrigger(String gameProgressProperty, String value, String valueForNull) {
		this.gameProgressProperty = gameProgressProperty;
		this.value = value;
		this.valueForNull = valueForNull;
	}

	@Override
	public boolean accept(GameEvent event) {
		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		String actualValue = gameProgress.getProgressProperties().get(gameProgressProperty);
		
		if (actualValue == null) {
			actualValue = valueForNull;
		}
		
		return actualValue.equals(value);
	}

}
