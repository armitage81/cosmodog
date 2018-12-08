package antonafanasjew.cosmodog.rules.actions.async;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.inventory.LogPlayer;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GameLogAction extends AbstractAsyncAction {

	private static final long serialVersionUID = 4542769916577259433L;

	private GameLog gameLog;

	public GameLogAction(GameLog gameLog) {
		this.gameLog = gameLog;
	}
	
	@Override
	public void onTrigger() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		DrawingContext cutsceneTextDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		
		cosmodogGame.setOpenGameLog(new GameLogState(gameLog, new TextPageConstraints(cutsceneTextDrawingContext.w(), cutsceneTextDrawingContext.h())));
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG).handleInput(gc, sbg, after - before, applicationContext);
	}
	
	@Override
	public void onEnd() {
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		Player player = ApplicationContextUtils.getPlayer();
		LogPlayer logPlayer = player.getLogPlayer();
		String series = gameLog.getCategory();
		String id = gameLog.getIdInCategory();
		if (GameLogs.SPECIFIC_LOGS_SERIES.contains(series)) {
			short unsortedLogId = gameLogs.getGameLogNumberById(series, id);
			logPlayer.addSpecificLog(series, unsortedLogId);
		} else {
			logPlayer.addLogToSeries(series);
		}
	}
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenGameLog() == null;
	}
	
	
	
}
