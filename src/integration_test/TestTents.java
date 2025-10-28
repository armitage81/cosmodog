package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

public class TestTents {

	/**
	 * - The items in tents must be desaturated to not be confused with interactive elements of the game.
	 * - The tents should look good when outside
	 * - The tents should look good when inside
	 */

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(341, 186, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("324/174;316/183;329/209;331/219;322/235;335/248;341/186"));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
