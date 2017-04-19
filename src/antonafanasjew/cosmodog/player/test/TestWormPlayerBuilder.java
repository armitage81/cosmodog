package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestWormPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPositionX(324);
		player.setPositionY(348);
				
	}

}