package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestTicket72VolcanoExit {

    public static void main(String[] args) throws SlickException {

        PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {

            @Override
            protected void updatePlayer(Player player) {
                player.setPositionX(185);
                player.setPositionY(186);
                player.setMaxLife(100);
                player.setLife(100);
                player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("181/185;185/186"));


            }
        };

        CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

        TestStarter.main(args);
    }
}
