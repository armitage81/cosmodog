package antonafanasjew.cosmodog.collision.validators.npc;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.structures.PortalPuzzle;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.structures.SafeSpace;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledTileObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CollisionUtils;
import antonafanasjew.cosmodog.util.TileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines collision for flying vehicles, which is everything what is not accessible by wheels, foot or boat.  
 */
public class PuzzleCollisionValidatorForNpc extends AbstractCollisionValidator {


	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {

		CollisionStatus passable = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
		CollisionStatus notPassable = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.PUZZLE_REGION);

		int tileLength = TileUtils.tileLengthSupplier.get();

		List<SafeSpace> safeSpaces = map.getSafeSpaces();
		List<Race> races = map.getRaces();
		List<PortalPuzzle> portalPuzzles = map.getPortalPuzzles();
		List<MoveableGroup> moveableGroups = map.getMoveableGroups();

		List<TiledObject> forbiddenRegions = new ArrayList<>();
		forbiddenRegions.addAll(safeSpaces.stream().map(SafeSpace::getRegion).toList());
		forbiddenRegions.addAll(races.stream().map(Race::getRegion).toList());
		forbiddenRegions.addAll(portalPuzzles.stream().map(PortalPuzzle::getRegion).toList());
		forbiddenRegions.addAll(moveableGroups.stream().map(MoveableGroup::getRegion).toList());


		int x = (int)(entrance.getPosition().getX() * tileLength);
		int y = (int)(entrance.getPosition().getY() * tileLength);

		PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, tileLength, tileLength, entrance.getPosition().getMapDescriptor());

		for (TiledObject forbiddenRegion : forbiddenRegions) {
			boolean intersects = CollisionUtils.intersects(r, map.getMapDescriptor(), forbiddenRegion);
			if (intersects) {
				return notPassable;
			}
		}

		return passable;
	}

}
