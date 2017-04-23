package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.setPositionX(55);
		player.setPositionY(236);
	}

}
