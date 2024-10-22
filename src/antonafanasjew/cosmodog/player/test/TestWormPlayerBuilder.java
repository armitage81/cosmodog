package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;

public class TestWormPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPosition(Position.fromCoordinates(324, 348, MapType.MAIN));
				
	}

}