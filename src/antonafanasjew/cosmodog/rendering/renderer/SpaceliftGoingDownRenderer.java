package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.SpriteSheets;
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
import antonafanasjew.cosmodog.rendering.decoration.SpaceliftDecoration;
import antonafanasjew.cosmodog.rendering.renderer.functions.SpaceLiftFadingFunction;
import antonafanasjew.cosmodog.rendering.renderer.functions.SpaceLiftSkyTurningBlackFunction;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import antonafanasjew.particlepattern.model.Particle;
import antonafanasjew.particlepattern.model.ParticlePattern;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SpaceliftGoingDownRenderer extends AbstractRenderer {

    List<Float> starXPositions = new ArrayList<>();
    List<Float> starYPositions = new ArrayList<>();

    public SpaceliftGoingDownRenderer() {
        Random random = new Random(3);
        DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
        for (int i = 0; i < 30; i++) {
            starXPositions.add((float)(random.nextInt((int)sceneDrawingContext.w())));
            starYPositions.add((float)(random.nextInt((int)sceneDrawingContext.h())));
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        ActionRegistry actionRegistry = ApplicationContextUtils
                .getCosmodogGame()
                .getActionRegistry();

        SpaceLiftAction action = actionRegistry.getRegisteredAction(AsyncActionType.SPACE_LIFT, SpaceLiftAction.class);

        if (action == null) {
            return;
        }

        if (action.isUpNotDown()) {
            return;
        }

        int currentPhaseNumber = action.getPhaseRegistry().currentPhaseNumber().orElse(-1);


        if (currentPhaseNumber == -1) {
            return;
        }



        AsyncAction currentPhase = action.getPhaseRegistry().currentPhase().get();
        String phaseName = currentPhase.getProperty("phaseName");

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
        Animation spaceDoorAnimation = ApplicationContext.instance().getAnimations().get("spaceliftSpaceDoor");
        Animation rayAnimation = ApplicationContext.instance().getAnimations().get("spaceliftRay");
        Animation cabinAnimation = ApplicationContext.instance().getAnimations().get("spaceliftCabin");

        SpriteSheet cloudSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS);
        Image cloudImage = cloudSpriteSheet.getSprite(0, 0);

        //Door is closing
        if (phaseName.equals("closingDoor")) {
            float visibleDoorWidth = tileLength;
            Object playedSoundAlready = currentPhase.getProperties().get("playedClosingSoundAlready");
            if (playedSoundAlready == null) {
                ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS).play();
                currentPhase.getProperties().put("playedClosingSoundAlready", true);
            }

            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            visibleDoorWidth = tileLength * completionRate;
            float doorX = playerVectorRelatedToCam.getX();
            float doorY = playerVectorRelatedToCam.getY();
            Image doorImage = spaceDoorAnimation.getCurrentFrame().getSubImage((int)(tileLength - visibleDoorWidth), 0, (int)visibleDoorWidth, tileLength);
            doorImage.draw(doorX, doorY, visibleDoorWidth, (float)tileLength);
        }

        if (phaseName.equals("goingDown")) {

            spaceDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);

            Object playedSoundAlready = currentPhase.getProperties().get("playedLiftInMotionAlready");
            if (playedSoundAlready == null) {
                ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SPACE_LIFT).play();
                currentPhase.getProperties().put("playedLiftInMotionAlready", true);
            }

            float completionRate = ((FixedLengthAsyncAction) currentPhase).getCompletionRate();
            float skyOpacity = SpaceLiftSkyTurningBlackFunction.instance(0.4f, 0.3f).apply(completionRate);
            float starOpacity = 1 - skyOpacity;
            graphics.setColor(Color.black);
            graphics.fillRect(sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h());
            Color skyColor = new Color(0f,0f,1f, skyOpacity);
            graphics.setColor(skyColor);
            graphics.fillRect(sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h());

            graphics.setColor(new Color(1, 1, 1, starOpacity));
            float starOffsetY = 1- completionRate * 100;
            for (int i = 0; i < starXPositions.size(); i++) {
                float starX = starXPositions.get(i) / cam.getZoomFactor();
                float starY = starYPositions.get(i) / cam.getZoomFactor();
                graphics.fillRect(starX, starY + starOffsetY, 2, 2);
            }

            for (int i = -20; i < 20; i++) {
                float rayX = firstVisibleRayTileRelatedToCam.getX() + (float) (tileLength / 2);
                float rayY = firstVisibleRayTileRelatedToCam.getY() - i * tileLength;
                rayAnimation.draw(rayX, rayY);
            }

            Vector initialCabinTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -3), camTilePosition);
            float cabinX = initialCabinTileRelatedToCam.getX();
            float cabinY = initialCabinTileRelatedToCam.getY();
            cabinAnimation.draw(cabinX, cabinY);

            if (completionRate >= 0.4) {
                SpaceliftDecoration spaceliftDecoration = SpaceliftDecoration.instanceForGoingDown();
                PlacedRectangle view = cam.viewCopy();
                ParticlePattern currentPattern = spaceliftDecoration.particlePatternForPlaceAndTime(view);
                Set<Particle> particles = currentPattern.particlesSet();
                //Note: particles are defined related to the particle pattern, but we draw related to the cam size.
                //Hence we need to translate the difference first.
                Rectangle particlePatternSurface = spaceliftDecoration.getParticlePatternSurface();
                particlePatternSurface = Rectangle.fromSize(particlePatternSurface.getWidth() * cam.getZoomFactor(), particlePatternSurface.getHeight() * cam.getZoomFactor());
                float centerOffsetX = -(particlePatternSurface.getWidth() - cam.viewCopy().width()) / 2f;
                float centerOffsetY = -(particlePatternSurface.getHeight() - cam.viewCopy().height()) / 2f;
                Vector particlePatternSurfaceOffsetRelatedToCam = new Vector(centerOffsetX, centerOffsetY);
                graphics.translate(particlePatternSurfaceOffsetRelatedToCam.getX(), particlePatternSurfaceOffsetRelatedToCam.getY());
                for (Particle particle : particles) {
                    float cloudOpacity = (completionRate - 0.4f) / 0.6f;
                    Color cloudFilterColor = new Color(1, 1, 1, cloudOpacity);
                    cloudImage.draw(particle.getOffset().getX() * cam.getZoomFactor(), particle.getOffset().getY() * cam.getZoomFactor(), cloudImage.getWidth() * cam.getZoomFactor(), cloudImage.getHeight() * cam.getZoomFactor(), cloudFilterColor);
                }
                graphics.translate(-particlePatternSurfaceOffsetRelatedToCam.getX(), -particlePatternSurfaceOffsetRelatedToCam.getY());
            }
            //Fading
            graphics.setColor(new Color(0, 0, 0, 1 - SpaceLiftFadingFunction.instance(0.05f, 0.1f, 0.1f, 0.1f).apply(completionRate)));
            graphics.fillRect(sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h());

        }

        if (phaseName.equals("landingLift")) {

            groundDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);

            for (int i = 0; i < 20; i++) {
                float rayX = firstVisibleRayTileRelatedToCam.getX() + (float) (tileLength / 2);
                float rayY = firstVisibleRayTileRelatedToCam.getY() - i * tileLength;
                rayAnimation.draw(rayX, rayY);
            }


            float verticalCabinOffset = 0;
            float visibleCabinHeight = 0;
            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            int maxCabinOffset = 20 * tileLength;
            verticalCabinOffset = maxCabinOffset - maxCabinOffset * completionRate;
            System.out.println(verticalCabinOffset);
            visibleCabinHeight = Math.min(4 * tileLength, verticalCabinOffset);
            Vector initialCabinTileRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition().shifted(-1, -3), camTilePosition);
            float cabinX = initialCabinTileRelatedToCam.getX();
            float cabinY = initialCabinTileRelatedToCam.getY() - verticalCabinOffset;
            Image cabinImage = cabinAnimation.getCurrentFrame().getSubImage(0, 0, cabinAnimation.getWidth(), (int)visibleCabinHeight);
            cabinImage.draw(cabinX, cabinY, cabinImage.getWidth(), visibleCabinHeight);
        }

        if (phaseName.equals("focusingOnPlayer")) {
            groundDoorAnimation.draw(playerVectorRelatedToCam.getX(), playerVectorRelatedToCam.getY(), tileLength, tileLength);
        }


        if (phaseName.equals("openingDoor")) {
            float verticalDoorOffset = 0;
            float visibleDoorHeight = tileLength;
            Object playedSoundAlready = currentPhase.getProperties().get("playedOpeningSoundAlready");
            if (playedSoundAlready == null) {
                ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS).play();
                currentPhase.getProperties().put("playedOpeningSoundAlready", true);
            }
            float completionRate = ((FixedLengthAsyncAction)currentPhase).getCompletionRate();
            verticalDoorOffset = tileLength * completionRate;
            visibleDoorHeight = tileLength - verticalDoorOffset;
            float doorX = playerVectorRelatedToCam.getX();
            float doorY = playerVectorRelatedToCam.getY() + verticalDoorOffset;
            float doorHeight = visibleDoorHeight;
            Image doorImage = groundDoorAnimation.getCurrentFrame().getSubImage(0, 0, (int) (float) tileLength, (int)doorHeight);
            doorImage.draw(doorX, doorY, (float) tileLength, doorHeight);
        }

        graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
        graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
        graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());

    }
}
