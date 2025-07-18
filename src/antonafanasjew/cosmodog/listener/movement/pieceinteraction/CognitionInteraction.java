package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import org.newdawn.slick.Color;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.actions.narration.EndingNarrationAction;

public class CognitionInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		String gameLogSeries = GameLogs.SERIES_CUTSCENES;
		String gameLogId = "006.decision";
		
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		GameLog gameLog = gameLogs.getGameLogBySeriesAndId(gameLogSeries, gameLogId);
		
		AsyncAction gameLogAction = new EndingNarrationAction(gameLog);

		cosmodogGame.getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, gameLogAction);
		cosmodogGame.getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new FixedLengthAsyncAction(1000) {
			private static final long serialVersionUID = -6998164781879026113L;

			@Override
			public void onEnd() {
				player.getGameProgress().setWon(true);
				
				int lengthOfFadeOutTransition = Features.getInstance().featureOn(Features.FEATURE_CUTSCENES) ? 5000 : 0;
				
				CosmodogStarter.instance.enterState(CosmodogStarter.OUTRO2_STATE_ID, new FadeOutTransition(Color.white, lengthOfFadeOutTransition), new FadeInTransition());
			}
		});
		
		
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

}
