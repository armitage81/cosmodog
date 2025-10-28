package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.Map;

public abstract class AbstractPlayerBuilder implements PlayerBuilder {

	@Override
	public Player buildPlayer() {
		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();
		Player player = Player.fromPosition(Position.fromCoordinates(5, 3, mapDescriptors.get(MapDescriptor.MAP_NAME_MAIN)));
		updatePlayer(player);
		return player;
	}

	protected abstract void updatePlayer(Player player);

}
