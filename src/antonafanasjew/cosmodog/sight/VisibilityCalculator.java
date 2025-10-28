package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.EnemiesUtils;

import java.util.Set;

public class VisibilityCalculator {

	private Vision fullVision;
	private Vision nightVision;
	private Vision stealthVision;
	
	public static VisibilityCalculator create(Vision fullVision, Vision nightVision, Vision stealthVision) {
		VisibilityCalculator calc = new VisibilityCalculator();
		calc.fullVision = fullVision;
		calc.nightVision = nightVision;
		calc.stealthVision = stealthVision;
		return calc;
	}

	public boolean visible(Actor observer, PlanetaryCalendar cal, CosmodogMap map, Player player) {

		return visible(observer, cal, map, player, player);
	}

	public boolean visible(Actor observer, PlanetaryCalendar cal, CosmodogMap map, Player player, Piece piece) {

		//Deactivated units do not see anything.
		//Examples are Hercules units at night or all units when the player is in a puzzle room or safe space.
		if (observer instanceof Enemy enemy) {

			if (!EnemiesUtils.enemyActive(enemy)) {
				return false;
			}
		}

		Vision vision = getVision(cal, map, player);

		Position piecePosition = piece.getPosition();

		return vision.visible(observer, piecePosition, map.getMapDescriptor().getWidth(), map.getMapDescriptor().getHeight());
	}

	public Set<Position> allVisiblePositions(Actor observer, PlanetaryCalendar cal, CosmodogMap map, Player player) {
		Vision vision = getVision(cal, map, player);
		return vision.visiblePositions(observer, map.getMapDescriptor().getWidth(), map.getMapDescriptor().getHeight());
	}
	
	public int maxVisibilityRange(PlanetaryCalendar cal, CosmodogMap map, Player player) {

		Vision vision = getVision(cal, map, player);

		int max = 0;
		for (Position element : vision.getElements()) {
			int x = (int)element.getX();
			int y = (int)element.getY();
			x = x < 0 ? -x : x;
			y = y < 0 ? -y : y;
			max = Math.max(max, x);
			max = Math.max(max, y);
		}
		return max;
	}

	private Vision getVision(PlanetaryCalendar cal, CosmodogMap map, Player player) {
		Vision vision = new Vision();

		if (Vision.playerHidden(map, player)) {
			vision.getElements().addAll(stealthVision.getElements());
		} else if (Vision.playerNotHiddenAndItIsNight(cal, map, player)) {
			vision.getElements().addAll(stealthVision.getElements());
			vision.getElements().addAll(nightVision.getElements());
		} else {
			vision.getElements().addAll(stealthVision.getElements());
			vision.getElements().addAll(nightVision.getElements());
			vision.getElements().addAll(fullVision.getElements());
		}
		return vision;
	}
	
}
