package antonafanasjew.cosmodog.actions.narration;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GameLogNarrationAction extends AbstractNarrationAction {

	private static final long serialVersionUID = -5461864685716594415L;

	public GameLogNarrationAction(GameLog gameLog) {
		super(gameLog);
	}

	@Override
	public void onTriggerInternally() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		DrawingContext textDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogContentDrawingContext();
		String text = getGameLog().getLogText();
		String title = getGameLog().getHeader();
		TextPageConstraints tpc = TextPageConstraints.fromDc(textDc);
		Book book = tpc.textToBook(text, FontRefToFontTypeMap.forNarration());
		cosmodogGame.setOpenBook(book);
		cosmodogGame.setOpenBookTitle(title);

	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG).handleInput(gc, sbg, after - before, applicationContext);
	}
	
	@Override
	public boolean hasFinished() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		return cosmodogGame.getOpenBook() == null;
	}

}
