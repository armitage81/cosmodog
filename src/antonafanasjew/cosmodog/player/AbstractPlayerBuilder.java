package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;

public abstract class AbstractPlayerBuilder implements PlayerBuilder {

	@Override
	public Player buildPlayer() {
		Player player = Player.fromPosition(5, 3);
		updatePlayer(player);
		return player;
	}

	protected abstract void updatePlayer(Player player);

}
