package integration_test;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestRemovalAmbientSoundInCaseOfCutscenesAndMenus {


	/**
	 * Test cases:
	 * - Check that ambient sound disappears when opening a menu.
	 * - Check that ambient sound disappears when a dialog with Alisa happens.
	 * - Check that ambient sound disappears when a monolith cutscene happens.
	 * - Check that ambient sound disappears when the ending scene happens.
	 * - Check that ambient sound disappears when a log entry has been found and opened.
	 * - Check that ambient sounds start again when cutscenes or log entries are finished/closed.
	 * - Check for the above cases also that the background music is set lower at the beginning and back again at the end.
	 */

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(44, 58, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);
				player.getGameProgress().setWormActive(false);

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("231/192");
				debuggerPositions.add("331/125");
				debuggerPositions.add("211/249");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
