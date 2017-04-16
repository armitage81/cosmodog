package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OnScreenNotificationAction;
import antonafanasjew.cosmodog.actions.notification.OnScreenNotificationAction.OnScreenNotificationTransition;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class OnScreenNotificationRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		OnScreenNotificationAction action = (OnScreenNotificationAction)game.getActionRegistry().getRegisteredAction(AsyncActionType.ONSCREEN_NOTIFICATION);
		if (action != null) {
			OnScreenNotificationTransition transition = action.getTransition();
			if (transition != null) {
				LetterTextRenderer ltr = new LetterTextRenderer();
				ltr.render(gameContainer, graphics, drawingContext, LetterTextRenderingParameter.fromTextScaleFactorAndAlignment(action.getText(), transition.textScale(), LetterTextRenderingParameter.HOR_ALIGNMENT_CENTER, LetterTextRenderingParameter.VER_ALIGNMENT_CENTER));
			}
		}
		
	}

}
