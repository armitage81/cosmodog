package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestRadiationPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPositionX(43);
		player.setPositionY(224);
		
	}

}
