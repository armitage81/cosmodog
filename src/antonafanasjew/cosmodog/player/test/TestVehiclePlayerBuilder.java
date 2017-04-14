package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestVehiclePlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		for (int i = 0; i < 20; i++) {
			player.getGameProgress().addInfobit();
		}
		player.setPositionX(7);
		player.setPositionY(40);
		
	}

}
