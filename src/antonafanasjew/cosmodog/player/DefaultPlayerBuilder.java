package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
//		player.setMaxLife(100);
//		player.setLife(100);
		player.setPositionX(50);
		player.setPositionY(31);
	}

}
