package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.setPositionX(140);
		player.setPositionY(58);
		player.setMaxLife(200);
		player.setLife(200);
	}

}
