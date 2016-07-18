package antonafanasjew.cosmodog.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.comparators.ComparableComparator;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.listener.movement.MovementListenerAdapter;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

import com.google.common.collect.Lists;

/**
 * This class holds values that need to be calculated only once per player movement.
 * It implements the movement listener to be updated when the player has moved.
 * Its values can be retrieved many times per turn instead of being calculated again and again.
 * Singleton. 
 */
public class PlayerMovementCache extends MovementListenerAdapter {

	private static final long serialVersionUID = 3422584256506346853L;
	
	private static PlayerMovementCache instance = null;
	
	public static PlayerMovementCache getInstance() {
		if (instance == null) {
			instance = new PlayerMovementCache();
		}
		return instance;
	}
	
	private PlayerMovementCache() {

	}
	
	private Piece closestSupply;

	
	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		recalculateClosestSupplyPosition(actor, x1, y1, x2, y2, applicationContext);
	}
	
	public Piece getClosestSupply() {
		return closestSupply;
	}

	private void recalculateClosestSupplyPosition(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		CosmodogMap map = applicationContext.getCosmodog().getCosmodogGame().getMap();
		Set<Piece> pieces = map.getSupplies();
		List<Piece> piecesSortedByProximity = Lists.newArrayList(pieces);
		Collections.sort(piecesSortedByProximity, new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				Position o1Pos = Position.fromCoordinates(o1.getPositionX(), o1.getPositionY());
				Position o2Pos = Position.fromCoordinates(o2.getPositionX(), o2.getPositionY());
				Position playerPos = Position.fromCoordinates(actor.getPositionX(), actor.getPositionY());
				
				float distance1 = CosmodogMapUtils.distanceBetweenPositions(o1Pos, playerPos);
				float distance2 = CosmodogMapUtils.distanceBetweenPositions(o2Pos, playerPos);
				
				return distance1 > distance2 ? 1 : (distance1 < distance2 ? -1 : 0);
			}

		});
		
		closestSupply = piecesSortedByProximity.isEmpty() ? null : piecesSortedByProximity.get(0);
		
	}
	
}

