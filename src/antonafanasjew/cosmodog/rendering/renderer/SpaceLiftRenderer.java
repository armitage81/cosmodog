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
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

@SuppressWarnings("OptionalGetWithoutIsPresent")
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

        int tileLength = TileUtils.tileLengthSupplier.get();

        float verticalDoorOffset = 0;
        float visibleDoorHeight = tileLength;

        if (currentPhaseNumber == 0) {
            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            verticalDoorOffset = tileLength - tileLength * completionRate;
            visibleDoorHeight = tileLength - verticalDoorOffset;
        }

        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

        Cam cam = cosmodogGame.getCam();
        Cam.CamTilePosition camTilePosition = cam.camTilePosition();
        Vector playerVectorRelatedToCam = Cam.pieceVectorRelatedToCamTilePosition(player, camTilePosition);

        DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
        graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
        graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
        graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

        //Drawing the spacelift door that is closing or already closed.
        float doorX = playerVectorRelatedToCam.getX();
        float doorY = playerVectorRelatedToCam.getY() + verticalDoorOffset;
        float doorHeight = visibleDoorHeight;
        Animation groundDoorAnimation = ApplicationContext.instance().getAnimations().get("spaceliftGroundDoor");
        Image doorImage = groundDoorAnimation.getCurrentFrame().getSubImage(0, 0, (int) (float) tileLength, (int)doorHeight);
        doorImage.draw(doorX, doorY, (float) tileLength, doorHeight);

        graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
        graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
        graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

    }

}
