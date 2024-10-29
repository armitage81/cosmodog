package antonafanasjew.cosmodog.actions.narration;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

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
	public void onEnd() {
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
