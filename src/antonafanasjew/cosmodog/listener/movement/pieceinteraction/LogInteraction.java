package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.rules.actions.async.GameLogNarrationAction;

public class LogInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		//Here we want to update the log player with the new log and register the log window action to show the text.
		
		LogPlayer logPlayer = player.getLogPlayer();
		
		CollectibleLog log = (CollectibleLog)piece;
		String series = log.getLogSeries();
		String id = log.getLogId();
		
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		GameLog concreteLog;
		
		if (GameLogs.SPECIFIC_LOGS_SERIES.contains(series)) {
			concreteLog = gameLogs.getUnsortedGameLogById(series, id);
		} else {
			List<GameLog> logsForSeries = gameLogs.getGameLogsForSeries(series);
			int noOfFoundLogs = logPlayer.noOfFoundLogsForSeries(series);
			concreteLog = logsForSeries.get(noOfFoundLogs); //it's the next one because list is zero-based.
		}
		
		cosmodogGame.getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new GameLogNarrationAction(concreteLog));
		
		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
