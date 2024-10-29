package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.spacelift.SpaceLiftAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class SpaceLiftRenderer extends AbstractRenderer {

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        ActionRegistry actionRegistry = ApplicationContextUtils
                .getCosmodogGame()
                .getActionRegistry();

        SpaceLiftAction action = actionRegistry.getRegisteredAction(AsyncActionType.SPACE_LIFT, SpaceLiftAction.class);

        if (action == null) {
            return;
        }

        int currentPhaseNumber = action.getPhaseRegistry().currentPhaseNumber().orElse(-1);

        if (currentPhaseNumber == -1) {
            return;
        }

        AsyncAction currentPhase = action.getPhaseRegistry().currentPhase().get();

        if (currentPhaseNumber == 0) {

            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            Animation groundDoorAnimation = ApplicationContext.instance().getAnimations().get("spaceliftGroundDoor");

            int tileLength = TileUtils.tileLengthSupplier.get();

            DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();

            graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

            Player player = ApplicationContextUtils.getPlayer();
            CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
            CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

            Cam cam = cosmodogGame.getCam();

            int scaledTileLength = (int) (tileLength * cam.getZoomFactor());

            int camX = (int) cam.viewCopy().x();
            int camY = (int) cam.viewCopy().y();

            int x = -(int) ((camX % scaledTileLength));
            int y = -(int) ((camY % scaledTileLength));

            int tileNoX = camX / scaledTileLength;
            int tileNoY = camY / scaledTileLength;

            groundDoorAnimation.draw((player.getPosition().getX() - tileNoX) * tileLength, (player.getPosition().getY() - tileNoY) * tileLength);

        }

    }

}
