package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
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
import org.newdawn.slick.*;

public class SpaceliftGoingUpRenderer extends AbstractRenderer {

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        ActionRegistry actionRegistry = ApplicationContextUtils
                .getCosmodogGame()
                .getActionRegistry();

        SpaceLiftAction action = actionRegistry.getRegisteredAction(AsyncActionType.SPACE_LIFT, SpaceLiftAction.class);

        if (action == null) {
            return;
        }

        if (!action.isUpNotDown()) {
            return;
        }

        int currentPhaseNumber = action.getPhaseRegistry().currentPhaseNumber().orElse(-1);

        if (currentPhaseNumber == -1) {
            return;
        }

        AsyncAction currentPhase = action.getPhaseRegistry().currentPhase().get();

        int tileLength = TileUtils.tileLengthSupplier.get();

        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
        Cam cam = cosmodogGame.getCam();
        Cam.CamTilePosition camTilePosition = cam.camTilePosition();

        Vector playerVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition(), camTilePosition);
        Vector firstVisibleRayTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -4), camTilePosition);

        DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
        graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
        graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
        graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

        Animation groundDoorAnimation = ApplicationContext.instance().getAnimations().get("spaceliftGroundDoor");
        Animation rayAnimation = ApplicationContext.instance().getAnimations().get("spaceliftRay");
        Animation cabinAnimation = ApplicationContext.instance().getAnimations().get("spaceliftCabin");

        //Door is closing, partial door, no ray, no cabin
        if (currentPhaseNumber == 0) {
            float verticalDoorOffset = 0;
            float visibleDoorHeight = tileLength;
            Object playedSoundAlready = currentPhase.getProperties().get("playedSoundAlready");
            if (playedSoundAlready == null) {
                ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS).play();
                currentPhase.getProperties().put("playedSoundAlready", true);
            }
            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            verticalDoorOffset = tileLength - tileLength * completionRate;
            visibleDoorHeight = tileLength - verticalDoorOffset;
            float doorX = playerVectorRelatedToCam.getX();
            float doorY = playerVectorRelatedToCam.getY() + verticalDoorOffset;
            float doorHeight = visibleDoorHeight;
            Image doorImage = groundDoorAnimation.getCurrentFrame().getSubImage(0, 0, (int) (float) tileLength, (int)doorHeight);
            doorImage.draw(doorX, doorY, (float) tileLength, doorHeight);
        }

        //Suspense waiting, door closed, no ray, no cabin.
        if (currentPhaseNumber == 1) {
            groundDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);
        }

        //Camera movement, door closed, no ray, no cabin
        if (currentPhaseNumber == 2) {
            groundDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);
        }

        //Ray appears, door closed, ray, no cabin
        if (currentPhaseNumber == 3) {

            groundDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);

            for (int i = 0; i < 20; i++) {
                float rayX = firstVisibleRayTileRelatedToCam.getX() + (float) (tileLength / 2);
                float rayY = firstVisibleRayTileRelatedToCam.getY() - i * tileLength;
                rayAnimation.draw(rayX, rayY);
            }
        }

        //Launching lift, door closed, ray, cabin
        if (currentPhaseNumber == 4) {
            groundDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);

            for (int i = 0; i < 20; i++) {
                float rayX = firstVisibleRayTileRelatedToCam.getX() + (float) (tileLength / 2);
                float rayY = firstVisibleRayTileRelatedToCam.getY() - i * tileLength;
                rayAnimation.draw(rayX, rayY);
            }

            float verticalCabinOffset = 0;
            float visibleCabinHeight = 0;
            float exponentialValue = currentPhase.getProperty("value");
            int maxCabinOffset = 20 * tileLength;
            verticalCabinOffset = maxCabinOffset * exponentialValue;
            visibleCabinHeight = Math.min(4 * tileLength, verticalCabinOffset);
            Vector initialCabinTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -3), camTilePosition);
            float cabinX = initialCabinTileRelatedToCam.getX();
            float cabinY = initialCabinTileRelatedToCam.getY() - verticalCabinOffset;
            Image cabinImage = cabinAnimation.getCurrentFrame().getSubImage(0, 0, cabinAnimation.getWidth(), (int)visibleCabinHeight);
            cabinImage.draw(cabinX, cabinY, cabinImage.getWidth(), visibleCabinHeight);
        }


        //Traveling in the stratosphere: Background, ray cabin.
        if (currentPhaseNumber == 6) {
            Color color = new Color(0, 0, 1 - ((FixedLengthAsyncAction)currentPhase).getCompletionRate());
            graphics.setColor(color);
            graphics.fillRect(sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h());

            for (int i = -20; i < 20; i++) {
                float rayX = firstVisibleRayTileRelatedToCam.getX() + (float) (tileLength / 2);
                float rayY = firstVisibleRayTileRelatedToCam.getY() - i * tileLength;
                rayAnimation.draw(rayX, rayY);
            }

            Vector initialCabinTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -3), camTilePosition);
            float cabinX = initialCabinTileRelatedToCam.getX();
            float cabinY = initialCabinTileRelatedToCam.getY();
            cabinAnimation.draw(cabinX, cabinY);
        }

        graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
        graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
        graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

    }
}
