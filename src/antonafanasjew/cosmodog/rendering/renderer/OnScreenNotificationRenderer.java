package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OnScreenNotificationAction;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class OnScreenNotificationRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext secretFoundMessageDc = DrawingContextProviderHolder.get().getDrawingContextProvider().secretFoundMessageDrawingContext();
		
		graphics.translate(secretFoundMessageDc.x(), secretFoundMessageDc.y());
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		OnScreenNotificationAction action = (OnScreenNotificationAction)game.getActionRegistry().getRegisteredAction(AsyncActionType.ONSCREEN_NOTIFICATION);
		if (action != null) {
			LetterTextRenderer ltr = LetterTextRenderer.getInstance().withDrawingContext(secretFoundMessageDc);
			ltr.render(gameContainer, graphics, LetterTextRenderingParameter.fromTextScaleFactorAndAlignment(action.getText(), action.textScale(), LetterTextRenderingParameter.HOR_ALIGNMENT_CENTER, LetterTextRenderingParameter.VER_ALIGNMENT_CENTER));
		}
		
	}

}
