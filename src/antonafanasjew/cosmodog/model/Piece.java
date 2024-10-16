package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a piece on the map. It could be an item, an actor or the player.
 */
public class Piece extends CosmodogModel {

	private static final long serialVersionUID = -7878012563623429751L;

	private Position position;

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public boolean interactive(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		return true;
	}
	
}
