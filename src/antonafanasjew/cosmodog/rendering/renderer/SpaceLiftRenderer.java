package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.*;
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

        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

        Cam cam = cosmodogGame.getCam();
        Cam.CamTilePosition camTilePosition = cam.camTilePosition();
        Vector playerVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition(), camTilePosition);

        DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
        graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
        graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
        graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

        //Drawing the spacelift door that is closing or already closed.
        float verticalDoorOffset = 0;
        float visibleDoorHeight = tileLength;
        if (currentPhaseNumber == 0) {
            Object playedSoundAlready = currentPhase.getProperties().get("playedSoundAlready");
            if (playedSoundAlready == null) {
                ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS).play();
                currentPhase.getProperties().put("playedSoundAlready", true);
            }
            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            verticalDoorOffset = tileLength - tileLength * completionRate;
            visibleDoorHeight = tileLength - verticalDoorOffset;
        }
        float doorX = playerVectorRelatedToCam.getX();
        float doorY = playerVectorRelatedToCam.getY() + verticalDoorOffset;
        float doorHeight = visibleDoorHeight;
        Animation groundDoorAnimation = ApplicationContext.instance().getAnimations().get("spaceliftGroundDoor");
        Image doorImage = groundDoorAnimation.getCurrentFrame().getSubImage(0, 0, (int) (float) tileLength, (int)doorHeight);
        doorImage.draw(doorX, doorY, (float) tileLength, doorHeight);

        //Drawing the spacelift ray.
        if (currentPhaseNumber >= 3) {
            Vector firstRayTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -4), camTilePosition);
            Animation rayAnimation = ApplicationContext.instance().getAnimations().get("spaceliftRay");
            for (int i = 0; i < 20; i++) {
                float rayX = firstRayTileRelatedToCam.getX() + (float) (tileLength / 2);
                float rayY = firstRayTileRelatedToCam.getY() - i * tileLength;
                rayAnimation.draw(rayX, rayY);
            }
        }

        //Drawing the spacelift cabin that is rising in the stratosphere.
        float verticalCabinOffset = 0;
        float visibleCabinHeight = 0;
        if (currentPhaseNumber == 4) {
            float exponentialValue = currentPhase.getProperty("value");
            int maxCabinOffset = 20 * tileLength;
            verticalCabinOffset = maxCabinOffset * exponentialValue;
            visibleCabinHeight = Math.min(4 * tileLength, verticalCabinOffset);
        }

        Vector initialCabinTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -3), camTilePosition);
        float cabinX = initialCabinTileRelatedToCam.getX();
        float cabinY = initialCabinTileRelatedToCam.getY() - verticalCabinOffset;
        Animation cabinAnimation = ApplicationContext.instance().getAnimations().get("spaceliftCabin");
        Image cabinImage = cabinAnimation.getCurrentFrame().getSubImage(0, 0, cabinAnimation.getWidth(), (int)visibleCabinHeight);
        cabinImage.draw(cabinX, cabinY, cabinImage.getWidth(), visibleCabinHeight);



        graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
        graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
        graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

    }

}
