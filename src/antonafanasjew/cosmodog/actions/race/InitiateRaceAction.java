package antonafanasjew.cosmodog.actions.race;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.camera.CamMovementActionWithConstantSpeed;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.actions.popup.WaitAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Music;

public class InitiateRaceAction extends PhaseBasedAction {

    private Race race;

    public InitiateRaceAction(Race race) {
        this.race = race;
    }

    @Override
    protected void onTriggerInternal() {

        int tileLength = TileUtils.tileLengthSupplier.get();
        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        Race race = PlayerMovementCache.getInstance().getActiveRace();

        Position start = race.getPolePosition().getPosition().scale(tileLength).translate(tileLength / 2.0f, tileLength / 2.0f);
        Position finish = race.getFinishLine().getPosition().scale(tileLength).translate(tileLength / 2.0f, tileLength / 2.0f);

        player.getInventory().put(InventoryItemType.VEHICLE, new VehicleInventoryItem(new Vehicle()));

        getPhaseRegistry().registerPhase("panningtofinish", new CamMovementActionWithConstantSpeed(128, finish, game));
        getPhaseRegistry().registerPhase("waiting", new WaitAction(500));
        getPhaseRegistry().registerPhase("instruction", new PopUpNotificationAction("Master the car and reach the finish line in time to win a reward. Good luck!"));
        getPhaseRegistry().registerPhase("panningtoplayer", new CamMovementActionWithConstantSpeed(128, start, game));

        race.start();

    }

    @Override
    public void onEnd() {

    }
}
