package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
//		player.setMaxLife(100);
//		player.setLife(100);
//		player.setPositionX(6);
//		player.setPositionY(58);
		player.setPositionX(270);
		player.setPositionY(95);
	}

}
