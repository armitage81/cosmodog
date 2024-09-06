package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.generic.FadingAction;
import antonafanasjew.cosmodog.actions.letterplateriddle.WrongSequenceAction;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.dying.DyingAction;
import antonafanasjew.cosmodog.actions.dying.DyingAction.DyingTransition;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class WrongSequenceInLetterPlateRiddleRenderer extends AbstractRenderer {

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();


        ApplicationContext applicationContext = ApplicationContext.instance();
        Cosmodog cosmodog = applicationContext.getCosmodog();
        CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();



        Player player = cosmodogGame.getPlayer();
        CosmodogMap map = ApplicationContextUtils.getCosmodogMap();


        WrongSequenceAction wrongSequenceAction = (WrongSequenceAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.WRONG_SEQUENCE_IN_LETTER_PLATE_RIDDLE);

        if (wrongSequenceAction == null) {
            return;
        }

        float opacity = 1f;
        AsyncAction currentAction = wrongSequenceAction.getActionPhaseRegistry().getRegisteredAction(AsyncActionType.CUTSCENE);

        if (currentAction instanceof FadingAction fadingAction) {
            opacity = fadingAction.getTransition().getValue();

            if (fadingAction.isFadingInNotFadingOut()) {
                opacity = 1f - opacity;
            }
        }

        graphics.setColor(new Color(0f, 0f, 0f, opacity));
        graphics.fillRect(gameContainerDrawingContext.x(), gameContainerDrawingContext.y(), gameContainerDrawingContext.w(), gameContainerDrawingContext.h());
    }
}
