package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

public abstract class AbstractPlayerBuilder implements PlayerBuilder {

	@Override
	public Player buildPlayer() {
		Player player = Player.fromPosition(Position.fromCoordinates(210, 250, MapType.MAIN));
		updatePlayer(player);
		return player;
	}

	protected abstract void updatePlayer(Player player);

}
