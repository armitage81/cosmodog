package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestLavaRivers {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				Features.getInstance().setFeature(Features.FEATURE_GODFISTS, true);
				player.setPositionX(230);
				player.setPositionY(174);
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("246/175;260/174"));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}