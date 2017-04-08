package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GameProgressPropertyTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = -7325438979653214792L;

	private String gameProgressProperty;
	private String value;

	public GameProgressPropertyTrigger(String gameProgressProperty, String value) {
		this.gameProgressProperty = gameProgressProperty;
		this.value = value;
	}

	@Override
	public boolean accept(GameEvent event) {
		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		String actualValue = gameProgress.getProgressProperties().get(gameProgressProperty);
		
		boolean nullIsOk = "false".equals(value); //In case the value is 'false', a missing property is also ok.
		
		return ((actualValue != null) && actualValue.equals(value)) || (nullIsOk && actualValue == null);
	}

}
