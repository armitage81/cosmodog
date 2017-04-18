package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.setPositionX(264);
		player.setPositionY(25);
	}

}
