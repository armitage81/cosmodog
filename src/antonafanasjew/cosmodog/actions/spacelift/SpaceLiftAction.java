package antonafanasjew.cosmodog.actions.spacelift;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ExponentialAction;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.LadderAction;
import antonafanasjew.cosmodog.actions.ParabolicAction;
import antonafanasjew.cosmodog.actions.camera.CamMovementAction;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;

public class SpaceLiftAction extends PhaseBasedAction {

    public SpaceLiftAction() {

    }

    @Override
    protected void onTriggerInternal() {

        int tileLength = TileUtils.tileLengthSupplier.get();

        CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

        Position playerPosition = ApplicationContextUtils.getPlayer().getPosition();
        Position spaceLiftShaftPosition = playerPosition.shifted(-1, -5);

        Position camCenterPixelPosition = Position.fromCoordinates(
                spaceLiftShaftPosition.getX() * tileLength,
                spaceLiftShaftPosition.getY() * tileLength,
                playerPosition.getMapType());


        Position playerPixelPosition = Position.fromCoordinates(
                playerPosition.getX() * tileLength,
                playerPosition.getY() * tileLength,
                MapType.SPACE);

        FixedLengthAsyncAction closingDoor = new FixedLengthAsyncAction(1000);
        FixedLengthAsyncAction suspenseWaiting = new FixedLengthAsyncAction(300);
        CamMovementAction focusingOnLift = new CamMovementAction(2000, camCenterPixelPosition, cosmodogGame);
        FixedLengthAsyncAction preparingLift = new FixedLengthAsyncAction(500);
        ExponentialAction launchingLift = new ExponentialAction(4000);
        //FixedLengthAsyncAction traveling = new FixedLengthAsyncAction(5000);


        FixedLengthAsyncAction changingPosition = new FixedLengthAsyncAction(1) {

            @Override
            public void onEnd() {
                Player player = ApplicationContextUtils.getPlayer();
                CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
                Cam cam = game.getCam();
                player.switchPlane(MapType.SPACE);
                player.setDirection(DirectionType.DOWN);
                cam.focusOnPiece(game, 0, 0, player);

            }
        };


        //ParabolicAction arriving = new ParabolicAction(5000);
        //FixedLengthAsyncAction waiting = new FixedLengthAsyncAction(2000);
        //FixedLengthAsyncAction openingDoor = new FixedLengthAsyncAction(1000);

        getPhaseRegistry().registerPhase(closingDoor);
        getPhaseRegistry().registerPhase(suspenseWaiting);
        getPhaseRegistry().registerPhase(focusingOnLift);
        getPhaseRegistry().registerPhase(preparingLift);
        getPhaseRegistry().registerPhase(launchingLift);
        getPhaseRegistry().registerPhase(changingPosition);

        /*


        getPhaseRegistry().registerPhase(traveling);
        getPhaseRegistry().registerPhase(arriving);
        getPhaseRegistry().registerPhase(waiting);
        getPhaseRegistry().registerPhase(focusingOnPlayer);
        getPhaseRegistry().registerPhase(openingDoor);
         */
    }

}
