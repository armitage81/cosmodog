package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.generic.FadingAction;
import antonafanasjew.cosmodog.actions.respawn.RespawnAction;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class RespawnRenderer extends AbstractRenderer {

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();


        ApplicationContext applicationContext = ApplicationContext.instance();
        Cosmodog cosmodog = applicationContext.getCosmodog();
        CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();



        Player player = cosmodogGame.getPlayer();
        CosmodogMap map = ApplicationContextUtils.getCosmodogMap();


        RespawnAction respawnAction = (RespawnAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.RESPAWNING);

        if (respawnAction == null) {
            return;
        }

        float opacity = 1f;
        AsyncAction currentAction = respawnAction.getActionPhaseRegistry().getRegisteredAction(AsyncActionType.CUTSCENE);

        if (currentAction == null) {
            opacity = 0;
        }

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
