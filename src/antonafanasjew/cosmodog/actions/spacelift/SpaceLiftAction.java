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

    private boolean upNotDown;

    public SpaceLiftAction(boolean upNotDown) {
        this.upNotDown = upNotDown;
    }

    public boolean isUpNotDown() {
        return upNotDown;
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

        if (upNotDown) {

            FixedLengthAsyncAction closingDoor = new FixedLengthAsyncAction(1000);
            FixedLengthAsyncAction suspenseWaiting = new FixedLengthAsyncAction(300);
            CamMovementAction focusingOnLift = new CamMovementAction(2000, camCenterPixelPosition, cosmodogGame);
            FixedLengthAsyncAction preparingLift = new FixedLengthAsyncAction(500);
            ExponentialAction launchingLift = new ExponentialAction(2000);
            FixedLengthAsyncAction traveling = new FixedLengthAsyncAction(15000);
            FixedLengthAsyncAction coupling = new FixedLengthAsyncAction(3000);
            FixedLengthAsyncAction openingDoor = new FixedLengthAsyncAction(1000);


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

            getPhaseRegistry().registerPhase(closingDoor);
            getPhaseRegistry().registerPhase(suspenseWaiting);
            getPhaseRegistry().registerPhase(focusingOnLift);
            getPhaseRegistry().registerPhase(preparingLift);
            getPhaseRegistry().registerPhase(launchingLift);
            getPhaseRegistry().registerPhase(changingPosition);
            getPhaseRegistry().registerPhase(traveling);
            getPhaseRegistry().registerPhase(coupling);
            getPhaseRegistry().registerPhase(openingDoor);

        } else {

            FixedLengthAsyncAction closingDoor = new FixedLengthAsyncAction(1000);
            FixedLengthAsyncAction suspenseWaiting = new FixedLengthAsyncAction(3000);

            FixedLengthAsyncAction changingPosition = new FixedLengthAsyncAction(1) {

                @Override
                public void onEnd() {
                    Player player = ApplicationContextUtils.getPlayer();
                    CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
                    Cam cam = game.getCam();
                    player.switchPlane(MapType.MAIN);
                    player.setDirection(DirectionType.DOWN);
                    cam.focusOnPiece(game, 0, 0, player);

                }
            };

            FixedLengthAsyncAction traveling = new FixedLengthAsyncAction(15000);
            FixedLengthAsyncAction landingLift = new ExponentialAction(3000);
            FixedLengthAsyncAction openingDoor = new FixedLengthAsyncAction(1000);

            getPhaseRegistry().registerPhase(closingDoor);
            getPhaseRegistry().registerPhase(suspenseWaiting);
            getPhaseRegistry().registerPhase(traveling);
            getPhaseRegistry().registerPhase(changingPosition);
            getPhaseRegistry().registerPhase(landingLift);
            getPhaseRegistry().registerPhase(openingDoor);
        }

    }

}
