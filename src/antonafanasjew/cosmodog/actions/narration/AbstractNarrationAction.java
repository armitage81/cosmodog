package antonafanasjew.cosmodog.actions.narration;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public abstract class AbstractNarrationAction extends AbstractAsyncAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private GameLog gameLog;

	public AbstractNarrationAction(GameLog gameLog) {
		this.gameLog = gameLog;
	}
	
	public GameLog getGameLog() {
		return gameLog;
	}

	@Override
	public void onTrigger() {
		//We don't want ambient sounds, like fire, electricity, energy walls etc, during cutscenes. They will be restarted by the effect renderer automatically when the window is closed.
		ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
		String currentMusicId = MusicUtils.currentMapMusicId();
		ApplicationContext.instance().getMusicResources().get(currentMusicId).setVolume(0.1f);
		onTriggerInternally();
	}

	protected abstract void onTriggerInternally();

	@Override
	public void onEnd() {

		String currentMusicId = MusicUtils.currentMapMusicId();
		ApplicationContext.instance().getMusicResources().get(currentMusicId).setVolume(1f);

		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		Player player = ApplicationContextUtils.getPlayer();
		LogPlayer logPlayer = player.getLogPlayer();
		String series = getGameLog().getCategory();
		String id = getGameLog().getIdInCategory();
		if (GameLogs.SPECIFIC_LOGS_SERIES.contains(series)) {
			short unsortedLogId = gameLogs.getGameLogNumberById(series, id);
			logPlayer.addSpecificLog(series, unsortedLogId);
		} else {
			logPlayer.addLogToSeries(series);
		}
	}
	
	
	
}
