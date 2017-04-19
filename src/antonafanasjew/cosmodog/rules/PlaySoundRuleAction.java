package antonafanasjew.cosmodog.rules;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rules.events.GameEvent;

public class PlaySoundRuleAction extends AbstractRuleAction {

	private static final long serialVersionUID = -5865149753321592257L;
	
	private String soundResource;
	
	public PlaySoundRuleAction(String soundResource) {
		this.soundResource = soundResource;
	}
	
	public static PlaySoundRuleAction fromSoundResource(String soundResource) {
		PlaySoundRuleAction retVal = new PlaySoundRuleAction(soundResource);
		return retVal;
	}
	
	@Override
	public void execute(GameEvent event) {
		ApplicationContext.instance().getSoundResources().get(soundResource).play();		
	}

}
